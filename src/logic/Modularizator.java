package logic;

import java.util.HashMap;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;

/**
 * Singleton class
 * 
 * @author vahan
 * 
 */
public class Modularizator {
	private Algorithm algorithm;

	private Network network;

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

	public Network getNetwork() {
		return network;
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
