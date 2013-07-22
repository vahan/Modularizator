package logic;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jdt.core.ICompilationUnit;
import org.jgrapht.Graphs;

public class MarceloAlgorithm extends Algorithm {

	public MarceloAlgorithm(Network network, int nSteps) {
		super(network, "Marcelo", nSteps);
	}

	@Override
	public Network optimize(Scorer scorer) {
		HashMap<ICompilationUnit, Cluster> moduleAssignments = network.getClusters();

		int time = 0;
		Network changedNetwork = (Network) network.clone();
		while (time < nSteps) {
			changedNetwork = change(changedNetwork, moduleAssignments);
			time++;
		}
		
		return changedNetwork;
	}

	protected Network change(Network changedNetwork,
			HashMap<ICompilationUnit, Cluster> moduleAssignments) {
		ICompilationUnit rndVertex = changedNetwork.getRandomVertex(rnd);
		// Count the number of dependencies to the same module as well as to other modules
		HashMap<Cluster, Integer> moduleDependencies = new HashMap<Cluster, Integer>();
		for (Cluster cluster : changedNetwork.getClusters().values())
			moduleDependencies.put(cluster, 0);
		for (Object v : Graphs.neighborListOf(changedNetwork, rndVertex)) {
			Cluster moduleAss = moduleAssignments.get(v);
			moduleDependencies.put(moduleAss, moduleDependencies.get(moduleAss) + 1);
		}
		HashMap<Cluster, Double> moduleProbs = new HashMap<Cluster, Double>();
		// Find maximum count
		int nmax = -Integer.MAX_VALUE;
		for (Cluster cluster : changedNetwork.getClusters().values())
			if (nmax < moduleDependencies.get(cluster))
				nmax = moduleDependencies.get(cluster);
		double gamma = (double) nmax / Modularizator.getInstance().getT();

		double sum = 0;
		for (Cluster cluster : changedNetwork.getClusters().values()) {
			double exp = Math.exp((double) moduleDependencies.get(cluster) / Modularizator.getInstance().getT() - gamma);
			moduleProbs.put(cluster, exp);
			sum += exp;
		}
		double rand = rnd.nextDouble() * sum;
		double acc = 0;
		Iterator<Cluster> it = network.getClusters().values().iterator();
		Cluster pos = null;
		while (acc < rand) {
			pos = it.next();
			acc += moduleProbs.get(pos);
		}

		Cluster oldpos = moduleAssignments.get(rndVertex);
		moduleAssignments.put(rndVertex, pos);

		if (!pos.equals(oldpos)) {
			changedNetwork.changeClusterAssignment(rndVertex, pos);
			changes.put(rndVertex, pos);
		}

		return changedNetwork;
	}

}
