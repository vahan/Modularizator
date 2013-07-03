package logic;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Network extends DefaultDirectedGraph<Vertex, DefaultEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2143531432815293678L;
	
	
	private ArrayList<ArrayList<Vertex>> clusters = new ArrayList<ArrayList<Vertex>>();
	

	public Network(Class<? extends DefaultEdge> edgeClass, ArrayList<ArrayList<Vertex>> clusters) {
		super(edgeClass);
		
		this.clusters = clusters;
	}
	
	
	public int clustersCount() {
		return clusters.size();
	}
	
	
	public void addCluster(ArrayList<Vertex> cluster) {
		clusters.add(cluster);
	}
	
	
	public Vertex getRandomVertex(Random rnd) {
		Set<Vertex> vertexSet = this.vertexSet();
		Vertex[] verteces = vertexSet.toArray(new Vertex[vertexSet.size()]);
		int rndIndex = rnd.nextInt(verteces.length);
		
		return verteces[rndIndex];
	}

	/**
	 * Move the vertex from a cluster into a new one
	 * @param vertex			Vertex to move
	 * @param oldClusterInd		The index of the cluster where the vertex belongs before moving
	 * @param newClusterInd		The index of the cluster where the vertex will be moved
	 */
	public void changeClusterAssignment(Vertex vertex, int oldClusterInd, int newClusterInd) {
		ArrayList<Vertex> newCluster = clusters.get(newClusterInd);
		newCluster.add(vertex);
		ArrayList<Vertex> oldCluster = clusters.get(oldClusterInd);
		oldCluster.remove(vertex);
		
	}

}
