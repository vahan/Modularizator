package modularizator;

/**
 * Contains all types of implemented scoring methods
 * Note that, if a new score type is implemented, 
 * an according type must be added to this enum in order
 * to make it visible in the Properties view.
 * Also remember to add a case for in the switch clause in Modularizator.initScorer() method
 * @author vahan
 *
 */
public enum ScorerTypes {
	Marcelo,
	Turbo;
	
	public static ScorerTypes getDefault() {
		return Marcelo;
	}

}
