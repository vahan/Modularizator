package logic;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public abstract class Algorithm {
	
	protected String name;
	
	protected final Graph<Module, DefaultEdge> graph;
	
	protected Algorithm(Graph<Module, DefaultEdge> graph, String name) {
		this.graph = graph;
		this.name = name;
	}
	
	public abstract DirectedGraph<Module, DefaultEdge> optimize(Scorer scorer);
	
	
	public String getName() {
		return name;
	}
	
}
