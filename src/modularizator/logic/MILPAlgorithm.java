package modularizator.logic;

/**
 * Uses the MILP formulation of the problem found in 
 * Koehler, Famp, Aranujo "MILP Formulation for the SCP" paper
 * @author vahan
 *
 */
public class MILPAlgorithm extends Algorithm {

	protected MILPAlgorithm(Network network, int nSteps) {
		super(network, "MILP", nSteps);
	}

	@Override
	public Network optimize() {
		// TODO Auto-generated method stub
		System.out.println("Running the MILP algorithm like a boss");
		//Preprocessing steps
		Network clonedNetwork = (Network) network.clone();
		clonedNetwork = preprocessNodes(clonedNetwork);
		clonedNetwork = preprocessValidInequalitiesStructure(clonedNetwork);
		clonedNetwork = preprocessEliminateSymmetrics(clonedNetwork);
		clonedNetwork = preprocessValidInequalitiesUpperBounds(clonedNetwork);
		
		//Construct and solve the MILP
		Network optimizedNetwork = solveMILP(clonedNetwork);
		
		return optimizedNetwork;
	}
	
	/**
	 * Constructs and solves the MILP described in section 3.3 of the referenced paper
	 * @param network
	 */
	private Network solveMILP(Network network) {
		
		
		return network;
	}
	
	
	/**
	 * Performs preprocessing step to decrease the number of variables of the constructed MILP.
	 * See Theorem 4.1 of the referenced paper.
	 * @return
	 */
	private Network preprocessNodes(Network network) {
		
		return network;
	}
	
	/**
	 * Performs preprocessing step by adding valid constraints of the constructed MILP based on the MDG structure.
	 * See Theorem 4.2 of the referenced paper.
	 * @return
	 */
	private Network preprocessValidInequalitiesStructure(Network network) {
		
		return network;
	}
	
	/**
	 * Performs preprocessing step to eliminate symmetric solutions.
	 * See Theorem 4.3 of the referenced paper.
	 * @return
	 */
	private Network preprocessEliminateSymmetrics(Network network) {
		
		return network;
	}
	
	/**
	 * Performs preprocessing step by adding valid constraints of the constructed MILP based on variable upper bounds.
	 * See Theorem 4.2 of the referenced paper.
	 * @return
	 */
	private Network preprocessValidInequalitiesUpperBounds(Network network) {
		
		return network;
	}

}
