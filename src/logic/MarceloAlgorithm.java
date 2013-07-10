package logic;

import java.util.HashMap;

import org.jgrapht.Graphs;

public class MarceloAlgorithm extends Algorithm {

	public MarceloAlgorithm(Network network, int nSteps) {
		super(network, "Marcelo", nSteps);
	}

	@Override
	public Network optimize(Scorer scorer) {
		HashMap<Object, Integer> moduleAssignments = new HashMap<Object, Integer>();
		int label = 0;
		int NClusters = network.clustersCount();
		for (Object v : network.vertexSet()) {
			moduleAssignments.put(v, label++ % NClusters);
		}

		int time = 0;
		Network changedNetwork = (Network) network.clone();
		while (time < nSteps) {
			changedNetwork = change(changedNetwork, moduleAssignments);
			time++;
		}
		
		return changedNetwork;
	}

	protected Network change(Network changedNetwork,
			HashMap<Object, Integer> moduleAssignments) {
		Object rndVertex = changedNetwork.getRandomVertex(rnd);

		// Count the number of dependencies to the same module as well as to other modules
		HashMap<Integer, Integer> moduleDependencies = new HashMap<Integer, Integer>();
		for (int i = 0; i < changedNetwork.clustersCount(); i++)
			moduleDependencies.put(i, 0);
		for (Object v : Graphs.neighborListOf(changedNetwork, rndVertex)) {
			int moduleAss = moduleAssignments.get(v);
			moduleDependencies.put(moduleAss, moduleDependencies.get(v) + 1);
		}

		HashMap<Integer, Double> moduleProbs = new HashMap<Integer, Double>();

		// Find maximum count
		int nmax = -Integer.MAX_VALUE;
		for (int i = 0; i < changedNetwork.clustersCount(); i++)
			if (nmax < moduleDependencies.get(i))
				nmax = moduleDependencies.get(i);
		double gamma = (double) nmax / T;

		double sum = 0;
		for (int i = 0; i < changedNetwork.clustersCount(); i++) {
			double exp = Math.exp((double) moduleDependencies.get(i) / T - gamma);
			moduleProbs.put(i, exp);
			sum += exp;
		}

		double rand = rnd.nextDouble() * sum;
		double acc = 0;
		int pos = -1;
		while (acc < rand) {
			acc += moduleProbs.get(++pos);
		}

		int oldpos = moduleAssignments.get(rndVertex);
		moduleAssignments.put(rndVertex, pos);

		if (pos != oldpos)
			changedNetwork.changeClusterAssignment(rndVertex, oldpos, pos);

		return changedNetwork;
	}

}
