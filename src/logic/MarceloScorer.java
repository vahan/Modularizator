package logic;


import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

/**
 * Returns a number representing a certain score on the network
 * @author vahan
 *
 */
public class MarceloScorer extends Scorer {
	
	/**
	 * Constructor
	 * @param network
	 */
	public MarceloScorer(Network network) {
		super(network);
	}
	
	@Override
	public double getScore() {
		return newmanModularityDirected();
	}

	/**
	 * Marcelo know how it works
	 * @return
	 */
	private double newmanModularityDirected() {
		int clustersCount = Cluster.getClustersCount();
		double[][] e = new double[clustersCount][clustersCount]; // fractions of edges linking vertices of cluster i to vertices of cluster j
		double[] a = new double[clustersCount]; // fractions of edges that connect to community i COLUMN
		double[] b = new double[clustersCount]; // fractions of edges that connect to community i ROW
		for (int i = 0; i < clustersCount; ++i) {
			for (int j = 0; j < clustersCount; ++j) {
				e[i][j] = 0.0;
			}
		}
		Set<DefaultEdge> edgeSet = network.edgeSet();
		for (DefaultEdge edge : edgeSet) {
			Object sourceVertex = network.getEdgeSource(edge);
			Object targetVertex = network.getEdgeTarget(edge);
			Cluster sourceCluster = network.getCluster(sourceVertex);
			if (sourceCluster == null)
				continue;
			Cluster targetCluster = network.getCluster(targetVertex);
			if (targetCluster == null)
				continue;
			e[sourceCluster.getId()][targetCluster.getId()]++;
		}
		int edgesCount = edgeSet.size();
		if (edgesCount == 0)
			return 0;
		for (int i = 0; i < clustersCount; ++i) {
			a[i] = 0;
			for (int j = 0; j < clustersCount; ++j) {
				e[i][j] /= edgesCount;
				a[i] += e[i][j];
			}
		}
		for (int j = 0; j < clustersCount; ++j) {
			b[j] = 0;
			for (int i = 0; i < clustersCount; ++i) {
				b[j] += e[i][j];
			}
		}
		double sumF = 0.0;
		double Q = 0.0;
		for (int i = 0; i < clustersCount; ++i) {
			sumF += a[i] * b[i];
			Q += e[i][i] - a[i] * b[i];
		}
		
		return ((1 - sumF) != 0) ? Q / (1 - sumF) : 0;
	}

}
