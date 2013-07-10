package logic;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Network extends DefaultDirectedGraph<Object, DefaultEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2143531432815293678L;

	private ArrayList<Cluster> clusters;

	public Network(Class<? extends DefaultEdge> edgeClass, ArrayList<Cluster> clusters) {
		super(edgeClass);

		this.clusters = clusters;
		initVertices();
	}

	private void initVertices() {
		for (Cluster cluster : clusters) {
			for (Object vertex : cluster.getVertices()) {
				super.addVertex(vertex);
			}
		}

	}

	public int clustersCount() {
		return clusters.size();
	}

	public void addCluster(Cluster cluster) {
		clusters.add(cluster);
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
	public void changeClusterAssignment(Object vertex, int oldClusterInd,
			int newClusterInd) {
		Cluster newCluster = clusters.get(newClusterInd);
		newCluster.getVertices().add(vertex);
		Cluster oldCluster = clusters.get(oldClusterInd);
		oldCluster.getVertices().remove(vertex);

	}

}
