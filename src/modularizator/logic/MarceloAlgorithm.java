package modularizator.logic;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jdt.core.ICompilationUnit;
import org.jgrapht.Graphs;

/**
 * A modularizing algorithm authored by Marcelo
 * @author vahan
 *
 */
public class MarceloAlgorithm extends Algorithm {

	/**
	 * Constructor
	 * @param network
	 * @param nSteps
	 */
	public MarceloAlgorithm(Network network, int nSteps) {
		super(network, "Marcelo", nSteps);
	}

	@Override
	/**
	 * Marcelo knows how it works
	 */
	public Network optimize() {
		int time = 0;
		Network changedNetwork = (Network) network.clone();
		changedNetwork.setName("Optimized Network");
		HashMap<ICompilationUnit, Cluster> moduleAssignments = changedNetwork.getClusters();
		
		while (time < nSteps) {
			changedNetwork = change(changedNetwork, moduleAssignments);
			time++;
		}
		
		return changedNetwork;
	}

	/**
	 * Commits a random change to the network
	 * @param changedNetwork
	 * @param moduleAssignments
	 * @return
	 */
	protected Network change(Network changedNetwork, HashMap<ICompilationUnit, Cluster> moduleAssignments) {
		ICompilationUnit rndVertex = changedNetwork.getRandomVertex(rnd);
		// Count the number of dependencies to the same module as well as to other modules
		HashMap<Cluster, Integer> moduleDependencies = new HashMap<Cluster, Integer>();
		for (Cluster cluster : changedNetwork.getClusters().values())
			moduleDependencies.put(cluster, 0);
		for (Object v : Graphs.neighborListOf(changedNetwork, rndVertex)) {
			Cluster moduleAss = moduleAssignments.get(v);
			if (moduleDependencies.get(moduleAss) == null)
				continue;
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
		Iterator<Cluster> it = changedNetwork.getClusters().values().iterator();
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
