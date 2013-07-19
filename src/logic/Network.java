package logic;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Network extends DefaultDirectedGraph<ICompilationUnit, DefaultEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2143531432815293678L;

	private HashMap<ICompilationUnit, Cluster> clusters;
	
	public Network(Class<? extends DefaultEdge> edgeClass, HashMap<ICompilationUnit, Cluster> clusters) {
		super(edgeClass);
		this.clusters = clusters;
		initVertices();
	}
	
	public HashMap<ICompilationUnit, Cluster> getClusters() {
		return clusters;
	}
	
	public Cluster getCluster(Object vertex) {
		return clusters.get(vertex);
	}

	public int clustersCount() {
		return clusters.size();
	}

	public void add(ICompilationUnit vertex, Cluster cluster) {
		clusters.put(vertex, cluster);
	}

	public ICompilationUnit getRandomVertex(Random rnd) {
		Set<ICompilationUnit> vertexSet = this.vertexSet();
		ICompilationUnit[] verteces = vertexSet.toArray(new ICompilationUnit[vertexSet.size()]);
		int rndIndex = rnd.nextInt(verteces.length);

		return verteces[rndIndex];
	}

	/**
	 * Move the Object from a cluster into a new one
	 * 
	 * @param vertex
	 *            Object to move
	 * @param oldClusterInd
	 *            The index of the cluster where the Object belongs before
	 *            moving
	 * @param newClusterInd
	 *            The index of the cluster where the Object will be moved
	 */
	public void changeClusterAssignment(ICompilationUnit vertex, Cluster newCluster) {
		clusters.put(vertex, newCluster);
	}

	public void add(ICompilationUnit compUnit, IJavaElement elem) {
		Cluster cluster = clusters.get(compUnit);
		if (cluster == null)
			cluster = new Cluster(elem);
		clusters.put(compUnit, cluster);
	}
	
	public DefaultDirectedGraph<ICompilationUnit, DefaultEdge> getGraph() {
		DefaultDirectedGraph<ICompilationUnit, DefaultEdge> graph = new DefaultDirectedGraph<>(super.getEdgeFactory());
		for (ICompilationUnit vertex : super.vertexSet()) {
			graph.addVertex(vertex);
		}
		for (DefaultEdge edge : super.edgeSet()) {
			ICompilationUnit source = super.getEdgeSource(edge);
			ICompilationUnit target = super.getEdgeTarget(edge);
			graph.addEdge(source, target);
		}
		return graph;
	}

	private void initVertices() {
		for (ICompilationUnit vertex : clusters.keySet()) {
			super.addVertex(vertex);
		}
		
	}

}
