package logic;

import java.util.HashMap;


/**
 * Singleton class
 * 
 * @author vahan
 * 
 */
public class Modularizator {
	private Algorithm algorithm;
	private Scorer scorer;
	private double T;
	private int nSteps;

	private static double T_DEFAULT = 1.0/2000000;
	private static int NSTEPS_DEFAULT = 1000;
	private static Modularizator instance;

	private Modularizator() {
		T = T_DEFAULT;
		nSteps = NSTEPS_DEFAULT;
	}

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
	 * Initialize all algorithm objects
	 */
	public void initAlgorithm(Network network) {
		algorithm = new MarceloAlgorithm(network, nSteps);
	}
	
	public void initScorer(Network network) {
		scorer = new MarceloScorer(network);
	}

	public HashMap<Object, Cluster> getChanges() {
		return algorithm.getChanges();
	}

}
