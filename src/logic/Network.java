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
	
	
	private ArrayList<ArrayList<Object>> clusters;
	

	public Network(Class<? extends DefaultEdge> edgeClass, ArrayList<ArrayList<Object>> clusters) {
		super(edgeClass);
		
		this.clusters = clusters;
		initVertices();
	}
	
	
	private void initVertices() {
		for (ArrayList<Object> cluster : clusters) {
			for (Object vertex : cluster) {
				super.addVertex(vertex);
			}
		}
		
	}


	public int clustersCount() {
		return clusters.size();
	}
	
	
	public void addCluster(ArrayList<Object> cluster) {
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
	 * @param vertex			Object to move
	 * @param oldClusterInd		The index of the cluster where the Object belongs before moving
	 * @param newClusterInd		The index of the cluster where the Object will be moved
	 */
	public void changeClusterAssignment(Object vertex, int oldClusterInd, int newClusterInd) {
		ArrayList<Object> newCluster = clusters.get(newClusterInd);
		newCluster.add(vertex);
		ArrayList<Object> oldCluster = clusters.get(oldClusterInd);
		oldCluster.remove(vertex);
		
	}

}
