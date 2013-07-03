package logic;

import java.util.HashMap;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public abstract class Modularizator {
	
	private HashMap<String, Algorithm> algorithms = new HashMap<String, Algorithm>();
	
	private DirectedGraph<Module, DefaultEdge> graph;
	
	
	public Modularizator() {
		this.graph = getModuleGraph();
		
		initAlgorithms();
	}
	
	
	public DirectedGraph<Module, DefaultEdge> getGraph() {
		return graph;
	}
	
	/**
	 * Scan the project, create and return the graph of modules
	 * @return
	 */
	protected abstract DirectedGraph<Module, DefaultEdge> getModuleGraph();
	
	
	/**
	 * Initialize all algorithm objects
	 */
	private void initAlgorithms() {
		MarceloAlgorithm marceloAlg = new MarceloAlgorithm(graph);
		algorithms.put(marceloAlg.getName(), marceloAlg);
		
	}
	
}
