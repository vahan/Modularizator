package logic;

/**
 * Abstract class to be extended by all scoring methods
 * @author vahan
 *
 */
public abstract class Scorer {
	/**
	 * The network to be scored
	 */
	protected final Network network;
	/**
	 * Constructor
	 * @param network
	 */
	public Scorer(Network network) {
		this.network = network;
	}
	
	public Network getNetwork() {
		return network;
	}
	/**
	 * Calculates the score and returns it
	 * @return
	 */
	public abstract double getScore();
	

}
