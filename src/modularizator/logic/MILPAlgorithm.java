package modularizator.logic;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.cplex.IloCplex;

import java.util.HashSet;

/**
 * Uses the MILP formulation of the problem found in 
 * Koehler, Famp, Aranujo "MILP Formulation for the SCP" paper
 * @author vahan
 *
 */
public class MILPAlgorithm extends Algorithm {

	protected MILPAlgorithm(Network network, int nSteps) {
		super(network, "MILP", nSteps);
	}

	@Override
	public Network optimize() {
		// TODO Auto-generated method stub
		System.out.println("Running the MILP algorithm like a boss");
		//Preprocessing steps
		Network clonedNetwork = (Network) network.clone();
		clonedNetwork = preprocessNodes(clonedNetwork);
		clonedNetwork = preprocessValidInequalitiesStructure(clonedNetwork);
		clonedNetwork = preprocessEliminateSymmetrics(clonedNetwork);
		clonedNetwork = preprocessValidInequalitiesUpperBounds(clonedNetwork);
		
		//Construct and solve the MILP
		Network optimizedNetwork = solveMILP(clonedNetwork);
		
		return optimizedNetwork;
	}
	
	/**
	 * Constructs and solves the MILP described in section 3.3 of the referenced paper
	 * @param network
	 */
	private Network solveMILP(Network network) {
		HashSet<Cluster> clusters = new HashSet<Cluster>(network.getClusters().values());
		int K = clusters.size(); //number of clusters
		int V = network.getClusters().size(); //number of vertices
		int E = network.edgeSet().size(); //number of edges
		
		try {
			IloCplex cplex = new IloCplex(); //create a cplex object
			//add variables and constraints
			IloNumVar[] r = cplex.numVarArray(K, 0.0, 1.0); // (43)
			IloNumVar[][] s = new IloNumVar[V][K];
			IloIntVar[][] x = new IloIntVar[V][K];
			IloNumVar[][][] t = new IloNumVar[V][V][K];
			for (int u = 0; u < V; ++u) {
				s[u] = cplex.numVarArray(K, 0.0, 1.0); // (44)
				x[u] = cplex.boolVarArray(K); // (46)
				cplex.addEq(cplex.sum(x[u]), 1); // (10)
			}
			for (int u = 0; u < V; ++u) {
				for (int v = 0; v < V; ++v) {
					t[u][v] = cplex.numVarArray(K, 0.0, 1.0); //(45)
					for (int k = 0; k < K; ++k) {
						//cplex.addLe(t[u][v][k], x[u][k]); // (13)
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
						sumT = cplex.sum(sumT, t[u][v][k]);
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
				System.out.println("Successfully solved");
			}
		} catch (IloException e) {
			System.err.println("Concert exception caught: " + e); 
		}
		
		return network;
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
