package modularizator.logic;

public class MILPAlgorithm extends Algorithm {

	protected MILPAlgorithm(Network network, int nSteps) {
		super(network, "MILP", nSteps);
	}

	@Override
	public Network optimize() {
		// TODO Auto-generated method stub
		System.out.println("Running the MILP algorithm like a boss");
		return network;
	}

}
