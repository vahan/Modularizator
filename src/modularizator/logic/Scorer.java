package modularizator.logic;

import modularizator.ScorerTypes;

/**
 * Abstract class to be extended by all scoring methods
 * @author vahan
 *
 */
public abstract class Scorer {
	/**
	 * The network to be scored
	 */
	protected final Network network;
	/**
	 * Type of the scorer
	 */
	protected final ScorerTypes type;
	
	
	/**
	 * Constructor
	 * @param network
	 */
	public Scorer(Network network, ScorerTypes type) {
		this.network = network;
		this.type = type;
	}
	
	public Network getNetwork() {
		return network;
	}
	
	public ScorerTypes getType() {
		return type;
	}
	
	/**
	 * Calculates the score and returns it
	 * @return
	 */
	public abstract double getScore();
	

}
