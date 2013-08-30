package modularizator;

public enum AlgorithmTypes {
	Marcelo,
	MILP;
	
	public static AlgorithmTypes getDefault() {
		return Marcelo;
	}
}
