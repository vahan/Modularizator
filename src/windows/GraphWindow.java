package windows;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;

import logic.Cluster;
import logic.Network;

import org.eclipse.jdt.core.ICompilationUnit;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class GraphWindow extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6879792871504557695L;
	
	private static final int VERTEX_WIDTH = 80;
	private static final int VERTEX_HEIGHT = 30;
	private static final int VERTEX_X = 20;
	private static final int VERTEX_Y = 20;
	
	private Network network;
	
	public GraphWindow(Network network) {
		super("Visualized network of the classes");
		this.network = network;
		
	}
	
	
	@Override
	public void run() {
		mxGraph graph = construct();
		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
		pack();
		setVisible(true);
	}
	
	
	private mxGraph construct() {
		mxGraph graph = new mxGraph();
		
		HashMap<ICompilationUnit, Cluster> verticesInClusters = network.getClusters();
		Set<ICompilationUnit> vertices = verticesInClusters.keySet();
		HashMap<Cluster, Object> clusters = new HashMap<Cluster, Object>();
		Object root = graph.getDefaultParent();
		
		graph.getModel().beginUpdate();
		for (Entry<ICompilationUnit, Cluster> entry : verticesInClusters.entrySet()) {
			ICompilationUnit compUnit = entry.getKey();
			Cluster cluster = entry.getValue();
			Object parent = clusters.get(cluster);
			if (parent == null) {
				parent = graph.insertVertex(root, null, "", 0, 0, 0, 0);
				clusters.put(cluster, parent);
			}
			try {
				Object v1 = graph.insertVertex(parent, null, compUnit.getElementName(), 0, 0, VERTEX_WIDTH, VERTEX_HEIGHT);
				
			} finally {
				// Updates the display
				graph.getModel().endUpdate();
			}
		}
		mxGraphLayout layout = new mxHierarchicalLayout(graph);
		layout.execute(root);
		
		return graph;
	}

}
