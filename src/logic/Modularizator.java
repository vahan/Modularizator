package logic;

import java.util.HashMap;

public abstract class Modularizator {
	
	
	private HashMap<String, Algorithm> algorithms = new HashMap<String, Algorithm>();
	
	private Network network;
	
	private int nSteps;
	
	
	public Modularizator(int nSteps) {
		this.network = getModuleNetwork();
		this.nSteps = nSteps;
		
		initAlgorithms();
	}
	
	
	public Network getNetwork() {
		return network;
	}
	
	/**
	 * Scan the project, create and return the graph of modules
	 * @return
	 */
	protected abstract Network getModuleNetwork();
	
	
	/**
	 * Initialize all algorithm objects
	 */
	private void initAlgorithms() {
		MarceloAlgorithm marceloAlg = new MarceloAlgorithm(network, nSteps);
		algorithms.put(marceloAlg.getName(), marceloAlg);
		
	}
	
}
