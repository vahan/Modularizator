package modularizator.logic;

import java.util.HashMap;

import modularizator.Logger;


/**
 * Singleton class to deal with all the modularizations
 * 
 * @author vahan
 * 
 */
public class Modularizator {
	/**
	 * A modularizing algorithm to run on the network
	 */
	private Algorithm algorithm;
	/**
	 * A network scoring method
	 */
	private Scorer scorer;
	/**
	 * Temperature, network measure
	 */
	private double T;
	/**
	 * Number of iteration steps for the algorithm
	 */
	private int nSteps;
	/**
	 * Number of iterations for the layout algorithm (Yifan Hu)
	 */
	private int layoutSteps;
	/**
	 * Folder where all output files will be put
	 */
	private String outputFolder;
	/**
	 * Default value for T
	 */
	private static final double T_DEFAULT = 1.0/2000000;
	/**
	 * Default value for nSteps
	 */
	private static final int NSTEPS_DEFAULT = 100000;
	/**
	 * Default number of iterations for the layout algorithm (Yifan Hu)
	 */
	private static final int LAYOUT_STEPS_DEFAULT = 10000;
	/**
	 * Default output folder
	 */
	private static final String OUTPUT_FOLDER_DEFAULT = "";
	/**
	 * A log string
	 */
	private Logger logger = new Logger();
	/**
	 * The only instance of the object
	 */
	private static Modularizator instance;

	private Modularizator() {
		T = T_DEFAULT;
		nSteps = NSTEPS_DEFAULT;
		layoutSteps = LAYOUT_STEPS_DEFAULT;
		outputFolder = OUTPUT_FOLDER_DEFAULT;
	}

	/**
	 * If no instance was created before, creates a new one,
	 * then returns the only existing instance
	 * @return	the only instance
	 */
	public static Modularizator getInstance() {
		if (instance == null)
			instance = new Modularizator();
		return instance;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}
	
	public Scorer getScorer() {
		return scorer;
	}
	
	public double getT() {
		return T;
	}
	
	public void setT(double T) {
		this.T = T;
	}
	
	public int getNSteps() {
		return nSteps;
	}
	
	public void setNSteps(int nSteps) {
		this.nSteps = nSteps;
	}
	
	public int getLayoutSteps() {
		return layoutSteps;
	}
	
	public void setLayoutSteps(int layoutSteps) {
		this.layoutSteps = layoutSteps;
	}
	
	public String getOutputFolder() {
		return outputFolder;
	}
	
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 * Initializes all algorithm objects
	 */
	public void initAlgorithm(Network network) {
		algorithm = new MarceloAlgorithm(network, nSteps);
	}
	/**
	 * Initializes all scoring objects
	 * @param network
	 */
	public void initScorer(Network network) {
		scorer = new MarceloScorer(network);
	}
	/**
	 * Gives the changes made after running the algorithm
	 * @return
	 */
	public HashMap<Object, Cluster> getChanges() {
		return algorithm.getChanges();
	}

}
