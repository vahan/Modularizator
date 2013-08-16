package logic;

import java.util.HashMap;
import java.util.Random;

/**
 * Abstract class for all modularizing algorithms
 * @author vahan
 *
 */
public abstract class Algorithm {
	/**
	 * Name of the algorithm, is used for GUI purposes
	 */
	protected final String name;
	/**
	 * The network to be optimized by the algorithm
	 */
	protected final Network network;
	/**
	 * Number of iterations
	 */
	protected int nSteps;
	/**
	 * Pseudo-random number generator object 
	 */
	protected Random rnd = new Random();
	/**
	 * Trace array of changes. 
	 */
	protected HashMap<Object, Cluster> changes = new HashMap<Object, Cluster>();
	/**
	 * Constructor
	 * @param network
	 * @param name
	 * @param nSteps
	 */
	protected Algorithm(Network network, String name, int nSteps) {
		this.network = network;
		this.name = name;
		this.nSteps = nSteps;
	}
	
	public HashMap<Object, Cluster> getChanges() {
		return changes;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * Abstract method that runs the optimization algortihm on the network
	 * @return	the new optimized network
	 */
	public abstract Network optimize();

}
