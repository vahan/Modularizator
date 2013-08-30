package modularizator.logic;


import java.util.HashSet;

import modularizator.ScorerTypes;

public class TurboScorer extends Scorer {

	public TurboScorer(Network network) {
		super(network, ScorerTypes.Turbo);
	}

	@Override
	public double getScore() {
		double score = getTurboMQ();
		
		return score;
	}
	
	private double getTurboMQ() {
		double turboMQ = 0;
		HashSet<Cluster> clusters = new HashSet<Cluster>();
		for (Cluster cluster : network.getClusters().values()) {
			if (clusters.contains(cluster))
				continue;
			clusters.add(cluster);
			turboMQ += network.getCF(cluster);
		}
		
		return turboMQ;
	}

}
