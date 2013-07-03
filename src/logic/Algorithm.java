package logic;

import java.util.Random;


public abstract class Algorithm {
	
	public static final int T = 1; //TODO set the correct value!
	
	protected final String name;
	
	protected final Network network;
	
	protected int nSteps;
	
	protected Random rnd = new Random();
	
	protected Algorithm(Network network, String name, int nSteps) {
		this.network = network;
		this.name = name;
		this.nSteps = nSteps;
	}
	
	public abstract Network optimize(Scorer scorer);
	
	
	public String getName() {
		return name;
	}
	
	
}
