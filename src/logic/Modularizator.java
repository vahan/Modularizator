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

	/**
	 * Initialize all algorithm objects
	 */
	public void initAlgorithms(Network network, int nSteps) {
		algorithm = new MarceloAlgorithm(network, nSteps);
	}

	public HashMap<Object, Cluster> getChanges() {
		return algorithm.getChanges();
	}

}
