package logic;

public abstract class Scorer {
	
	protected final Network network;
	
	public Scorer(Network network) {
		this.network = network;
	}
	
	public Network getNetwork() {
		return network;
	}
	
	public abstract double getScore();
	

}
