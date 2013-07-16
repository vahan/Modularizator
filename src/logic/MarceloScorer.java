package logic;


import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

public class MarceloScorer extends Scorer {
	
	
	public MarceloScorer(Network network) {
		super(network);
	}
	
	
	@Override
	public double getScore() {
		return newmanModularityDirected();
	}

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
			int sourceCluster = network.getCluster(sourceVertex).getId();
			int targetCluster = network.getCluster(targetVertex).getId();
			e[sourceCluster][targetCluster]++;
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
