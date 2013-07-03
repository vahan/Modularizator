package logic;

import java.util.HashMap;
import java.util.Random;

import org.jgrapht.Graphs;

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
	
	
	protected Network change(Network changedNetwork, HashMap<Vertex, Integer> simulatedModuleAssignments) {
		Vertex rndVertex = network.getRandomVertex(rnd);
		
		// Count the number of dependencies to the same module as well as to other modules
		HashMap<Integer, Integer> moduleDependencies = new HashMap<Integer, Integer>();
		for (int i = 0; i < network.clustersCount(); i++)
			moduleDependencies.put(i, 0);
		for (Vertex v : Graphs.neighborListOf(network, rndVertex)) {
			int moduleAss = simulatedModuleAssignments.get(v);
			moduleDependencies.put(moduleAss, moduleDependencies.get(v) + 1);
		}
		
		HashMap<Integer, Double> moduleProbs = new HashMap<Integer, Double>();
		
		// Find maximum count
		int nmax = -Integer.MAX_VALUE;
		for (int i = 0; i < changedNetwork.clustersCount(); i++)
			if (nmax < moduleDependencies.get(i))
				nmax = moduleDependencies.get(i);
		double gamma = (double)nmax / T;

		//TODO who the hell is 'c'?
		double c = 0;
		for (int i = 0; i < changedNetwork.clustersCount(); i++)
		{
			moduleProbs.put(i, Math.exp((double) moduleDependencies.get(i) / T - gamma));
			c += Math.exp((double) moduleDependencies.get(i) / T - gamma);
		}
		
		double rand = rnd.nextDouble() * c;
		int pos = 0;
		int oldpos = 0;
		double acc = moduleProbs.get(0);
		
		while (acc < rand)
		{
			pos++;
			acc += moduleProbs.get(pos);			
		}
		
		oldpos = simulatedModuleAssignments.get(rndVertex); 
		simulatedModuleAssignments.put(rndVertex, pos);	

		if (pos != oldpos)
			changedNetwork.changeClusterAssignment(rndVertex, oldpos, pos);
		
		return changedNetwork;
	}
	
}
