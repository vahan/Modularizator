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

	private static Modularizator instance;

	private Modularizator() {

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

	/**
	 * Initialize all algorithm objects
	 */
	public void initAlgorithm(Network network, int nSteps) {
		algorithm = new MarceloAlgorithm(network, nSteps);
	}
	
	public void initScorer(Network network) {
		scorer = new MarceloScorer(network);
	}

	public HashMap<Object, Cluster> getChanges() {
		return algorithm.getChanges();
	}

}
