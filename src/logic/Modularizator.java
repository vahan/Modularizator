package logic;

import java.util.HashMap;


/**
 * Singleton class to deal with all the modularizations
 * 
 * @author vahan
 * 
 */
public class Modularizator {
	/**
	 * A modularizing algorithm to run on the network
	 */
	private Algorithm algorithm;
	/**
	 * A network scoring method
	 */
	private Scorer scorer;
	/**
	 * Temperature, network measure
	 */
	private double T;
	/**
	 * Number of iteration steps for the algorithm
	 */
	private int nSteps;
	/**
	 * Default value for T
	 */
	private static double T_DEFAULT = 1.0/2000000;
	/**
	 * Default value for nSteps
	 */
	private static int NSTEPS_DEFAULT = 1000;
	/**
	 * The only instance of the object
	 */
	private static Modularizator instance;

	private Modularizator() {
		T = T_DEFAULT;
		nSteps = NSTEPS_DEFAULT;
	}

	/**
	 * If no instance was created before, creates a new one,
	 * then returns the only existing instance
	 * @return	the only instance
	 */
	public static Modularizator getInstance() {
		if (instance == null)
			instance = new Modularizator();
		return instance;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}
	
	public Scorer getScorer() {
		return scorer;
	}
	
	public double getT() {
		return T;
	}
	
	public void setT(double T) {
		this.T = T;
	}
	
	public int getNSteps() {
		return nSteps;
	}
	
	public void setNSteps(int nSteps) {
		this.nSteps = nSteps;
	}

	/**
	 * Initializes all algorithm objects
	 */
	public void initAlgorithm(Network network) {
		algorithm = new MarceloAlgorithm(network, nSteps);
	}
	/**
	 * Initializes all scoring objects
	 * @param network
	 */
	public void initScorer(Network network) {
		scorer = new MarceloScorer(network);
	}
	/**
	 * Gives the changes made after running the algorithm
	 * @return
	 */
	public HashMap<Object, Cluster> getChanges() {
		return algorithm.getChanges();
	}

}
