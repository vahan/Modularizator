package logic;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Network extends DefaultDirectedGraph<Object, DefaultEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2143531432815293678L;

	private HashMap<Object, Cluster> clusters;

	public Network(Class<? extends DefaultEdge> edgeClass, HashMap<Object, Cluster> clusters) {
		super(edgeClass);
		this.clusters = clusters;
		initVertices();
	}
	
	public HashMap<Object, Cluster> getClusters() {
		return clusters;
	}

	private void initVertices() {
		for (Object vertex : clusters.keySet()) {
			super.addVertex(vertex);
		}
	}
	
	public Cluster getCluster(Object vertex) {
		return clusters.get(vertex);
	}

	public int clustersCount() {
		return clusters.size();
	}

	public void add(Object vertex, Cluster cluster) {
		clusters.put(vertex, cluster);
	}

	public Object getRandomVertex(Random rnd) {
		Set<Object> vertexSet = this.vertexSet();
		Object[] verteces = vertexSet.toArray(new Object[vertexSet.size()]);
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
	public void changeClusterAssignment(Object vertex, Cluster newCluster) {
		clusters.put(vertex, newCluster);
	}

	public void add(ICompilationUnit compUnit, IJavaElement elem) {
		// TODO Auto-generated method stub
		Cluster cluster = clusters.get(compUnit);
		if (cluster == null)
			cluster = new Cluster(elem);
		clusters.put(compUnit, cluster);
	}

}
