package logic;

import java.util.HashMap;

public abstract class Modularizator {
	
	
	private HashMap<String, Algorithm> algorithms = new HashMap<String, Algorithm>();
	
	private Network network;
	
	private int nSteps;
	
	
	public Modularizator(Network network, int nSteps) {
		this.network = network;
		this.nSteps = nSteps;
		
		initAlgorithms();
	}
	
	
	public Network getNetwork() {
		return network;
	}
	
	
	
	/**
	 * Initialize all algorithm objects
	 */
	private void initAlgorithms() {
		MarceloAlgorithm marceloAlg = new MarceloAlgorithm(network, nSteps);
		algorithms.put(marceloAlg.getName(), marceloAlg);
		
	}
	
}
