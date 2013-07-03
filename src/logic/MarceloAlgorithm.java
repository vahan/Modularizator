package logic;

import java.util.HashMap;


public class MarceloAlgorithm extends Algorithm {
	
	
	public MarceloAlgorithm(Network network, int nSteps) {
		super(network, "Marcelo", nSteps);
	}

	@Override
	public Network optimize(Scorer scorer) {
		HashMap<Vertex, Integer> simulatedModuleAssignments = new HashMap<Vertex, Integer>();
		//assign module membership deterministically for the simulated network
		int my_V = 0;
		int NClusters;
		int nModules = network.vertexSet().size();
		if (nModules > 0)
			NClusters = nModules;
		else
			NClusters = network.clustersCount();
		for (Vertex v : network.vertexSet()) {
			simulatedModuleAssignments.put(v, my_V++ % NClusters);
		}
		
		int time = 0;
		Network changedNetwork = (Network) network.clone();
		while (time < nSteps) {
			changedNetwork = change(changedNetwork, simulatedModuleAssignments);
			time++;
		}
		
		return null;
	}

}
