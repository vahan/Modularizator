package modularizator.logic;


import java.util.HashSet;

import modularizator.ScorerTypes;

/**
 * Implementation of the TurboScore network scoring method
 * @author vahan
 *
 */
public class TurboScorer extends Scorer {

	/**
	 * Constructor
	 * @param network
	 */
	public TurboScorer(Network network) {
		super(network, ScorerTypes.Turbo);
	}

	@Override
	public double getScore() {
		double score = getTurboMQ();
		
		return score;
	}
	
	/**
	 * Calculates and returns the MQ value of the network (check the Koehler, Fampa, Araujo paper)
	 * @return
	 */
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
