package modularizator;

/**
 * Contains all types of implemented optimizing algorithms
 * Note that, if a new algorithm is implemented, 
 * an according type must be added to this enum in order
 * to make it visible in the Properties view.
 * Also remember to add a case for in the switch clause in Modularizator.initAlgorithm() method
 * @author vahan
 *
 */
public enum AlgorithmTypes {
	Marcelo,
	MILP;
	
	public static AlgorithmTypes getDefault() {
		return Marcelo;
	}
}
