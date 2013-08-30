package modularizator.logic;

import modularizator.ScorerTypes;

public class TurboScorer extends Scorer {

	public TurboScorer(Network network) {
		super(network, ScorerTypes.Turbo);
	}

	@Override
	public double getScore() {
		double score = network.getTurboMQ();
		
		return score;
	}

}
