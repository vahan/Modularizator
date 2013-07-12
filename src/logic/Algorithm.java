package logic;

import java.util.HashMap;
import java.util.Random;

public abstract class Algorithm {

	public static final double T = 1.0/4; // TODO set the correct value!

	protected final String name;

	protected final Network network;

	protected int nSteps;

	protected Random rnd = new Random();
	
	protected HashMap<Object, Cluster> changes = new HashMap<Object, Cluster>();

	protected Algorithm(Network network, String name, int nSteps) {
		this.network = network;
		this.name = name;
		this.nSteps = nSteps;
	}
	
	public HashMap<Object, Cluster> getChanges() {
		return changes;
	}

	public abstract Network optimize(Scorer scorer);

	public String getName() {
		return name;
	}

}
