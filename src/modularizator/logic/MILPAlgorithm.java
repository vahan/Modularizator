package modularizator.logic;

import java.util.HashMap;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import org.eclipse.jdt.core.ICompilationUnit;
import org.jgrapht.graph.DefaultEdge;

import com.google.common.collect.HashBiMap;

/**
 * Uses the MILP formulation of the problem found in 
 * Koehler, Famp, Aranujo "MILP Formulation for the SCP" paper
 * @author vahan
 *
 */
public class MILPAlgorithm extends Algorithm {
	
	private HashBiMap<ICompilationUnit, Integer> vertices = HashBiMap.create();
	
	public MILPAlgorithm(Network network, int nSteps) {
		super(network, "MILP", nSteps);
	}

	@Override
	public Network optimize() {
		// TODO Auto-generated method stub
		System.out.println("Running the MILP algorithm like a boss");
		//Preprocessing
		Network clonedNetwork = preprocess(network);
		
		//Construct and solve the MILP
		Network optimizedNetwork = solveMILP(clonedNetwork);
		
		return optimizedNetwork;
	}
	
	/**
	 * TODO: Yeah, I know it's a huge method, but I'm also too lazy to split it
	 * Constructs and solves the MILP described in section 3.3 of the referenced paper
	 * @param network
	 */
	private Network solveMILP(Network network) {
		init(network);
		
		int V = network.vertexSet().size(); //number of vertices
		int K = V; // new HashSet<Cluster>(network.getClusters().values()).size(); //number of clusters
		double[][] c = coeffs(network);
		Network optimNetwork = null;
		try {
			IloCplex cplex = new IloCplex(); //create a cplex object
			//add variables and constraints
			IloNumVar[] r = cplex.numVarArray(K, 0.0, 1.0); // (43)
			IloNumVar[][] s = new IloNumVar[V][K];
			IloIntVar[][] x = new IloIntVar[V][K];
			IloNumVar[][][] t = new IloIntVar[V][V][K];
			for (int u = 0; u < V; ++u) {
				s[u] = cplex.numVarArray(K, 0.0, 1.0); // (44)
				x[u] = cplex.boolVarArray(K); // (46)
				cplex.addEq(cplex.sum(x[u]), 1); // (10)
			}
			for (int u = 0; u < V; ++u) {
				for (int v = 0; v < V; ++v) {
					t[u][v] = cplex.boolVarArray(K); //(45), it must be binary
					for (int k = 0; k < K; ++k) {
						//cplex.addLe(t[u][v][k], x[u][k]); // (13) //redundant?
						cplex.addLe(t[u][v][k], x[v][k]); // (14)
						cplex.addGe(t[u][v][k], cplex.sum(cplex.sum(x[u][k], x[v][k]), -1)); // (15)
					}
				}
			}
			for (int k = 0; k < K; ++k) {
				IloNumExpr sumX = cplex.constant(0);
				for (int u = 0; u < V; ++u) {
					sumX = cplex.sum(sumX, x[u][k]);
				}
				cplex.addLe(cplex.diff(r[k], sumX), 0); // (37)
				IloNumExpr sumS = cplex.constant(0);
				IloNumExpr sumT = cplex.constant(0);
				for (int u = 0; u < V; ++u) {
					cplex.addLe(cplex.diff(s[u][k], r[k]), 0); // (39)
					cplex.addLe(cplex.diff(s[u][k], x[u][k]), 0); //(40)
					cplex.addGe(cplex.diff(s[u][k], cplex.sum(r[k], x[u][k])), -1); // (41)
					// (42) is redundant, follows from (44)
					for (int v = 0; v < V; ++v) {
						sumS = cplex.sum(sumS, s[u][k]);
						sumS = cplex.sum(sumS, s[v][k]);
						sumS = cplex.prod(sumS, c[u][v]);
						sumT = cplex.sum(sumT, t[u][v][k]);
						sumT = cplex.prod(sumT, c[u][v]);
					}
				}
				cplex.addEq(cplex.diff(sumS, cplex.prod(2, sumT)), 0); //(38)
			}
			//add objective function
			cplex.addMaximize(cplex.sum(r));
			//solve
			boolean res = cplex.solve();
			if (!res) {
				System.err.println("Cplex was not able to find a feasible solution");
			} else {
				//Interpret the solution into a network
				HashMap<ICompilationUnit, Cluster> optimClusters = new HashMap<ICompilationUnit, Cluster>();
				HashBiMap<Integer, Cluster> clusterIndices = HashBiMap.create();
				System.out.println("Successfully solved");
				//double[] rvals = cplex.getValues(r);
				//double[][] svals = new double[V][K];
				double[][] xvals = new double[V][K];
				//double[][][] tvals = new double[V][V][K];
				for (int u = 0; u < V; ++u) {
					//svals[u] = cplex.getValues(s[u]);
					xvals[u] = cplex.getValues(x[u]);
					for (int k = 0; k < K; ++k) {
						if (xvals[u][k] == 0)
							continue;
						Cluster cluster = clusterIndices.get(k);
						if (cluster == null) {
							cluster = new Cluster(network.getProject(), "cluster_" + k);
							clusterIndices.put(k, cluster);
						}
						
						ICompilationUnit vertex = vertices.inverse().get(u);
						network.add(vertex, cluster);
						changes.put(vertex, cluster);
					}
					//for (int v = 0; v < V; ++v) {
					//	tvals[u][v] = cplex.getValues(t[u][v]);
					//}
				}
				/*double[][][] wvals = new double[V][V][K];
				for (int k = 0; k < K; ++k) {
					for (int u = 0; u < V; ++u) {
						for ( int v = 0; v < V; ++v) {
							wvals[u][v][k] = xvals[u][k] - xvals[v][k] - 2 * tvals[u][v][k];
						}
					}
				}*/
				optimNetwork = new Network(DefaultEdge.class,
						optimClusters,
						"Optimized structure");
			}
		} catch (IloException e) {
			System.err.println("Concert exception caught: " + e); 
		}
		
		return optimNetwork;
	}
	
	
	private void init(Network network) {
		int vertexIndex = 0;
		for (ICompilationUnit vertex : network.vertexSet()) {
			vertices.put(vertex, vertexIndex);
			vertexIndex++;
		}
	}
	
	
	private double[][] coeffs(Network network) {
		int V = network.vertexSet().size(); //number of vertices
		
		double[][] coeffs = new double[V][V];
		for (DefaultEdge edge : network.edgeSet()) {
			int u = vertices.get(network.getEdgeSource(edge));
			int v = vertices.get(network.getEdgeTarget(edge));
			coeffs[u][v] = 1;
		}
		
		return coeffs;
	}
	
	
	private Network preprocess(Network network) {
		Network clonedNetwork = (Network) network.clone();
		clonedNetwork = preprocessNodes(clonedNetwork);
		clonedNetwork = preprocessValidInequalitiesStructure(clonedNetwork);
		clonedNetwork = preprocessEliminateSymmetrics(clonedNetwork);
		clonedNetwork = preprocessValidInequalitiesUpperBounds(clonedNetwork);
		
		return clonedNetwork;
	}
	
	/**
	 * Performs preprocessing step to decrease the number of variables of the constructed MILP.
	 * See Theorem 4.1 of the referenced paper.
	 * @return
	 */
	private Network preprocessNodes(Network network) {
		//TODO
		
		return network;
	}
	
	/**
	 * Performs preprocessing step by adding valid constraints of the constructed MILP based on the MDG structure.
	 * See Theorem 4.2 of the referenced paper.
	 * @return
	 */
	private Network preprocessValidInequalitiesStructure(Network network) {
		//TODO
		
		return network;
	}
	
	/**
	 * Performs preprocessing step to eliminate symmetric solutions.
	 * See Theorem 4.3 of the referenced paper.
	 * @return
	 */
	private Network preprocessEliminateSymmetrics(Network network) {
		//TODO
		
		return network;
	}
	
	/**
	 * Performs preprocessing step by adding valid constraints of the constructed MILP based on variable upper bounds.
	 * See Theorem 4.2 of the referenced paper.
	 * @return
	 */
	private Network preprocessValidInequalitiesUpperBounds(Network network) {
		//TODO
		
		return network;
	}

}
