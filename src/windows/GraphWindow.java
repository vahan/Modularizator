package windows;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

import logic.Cluster;
import logic.Network;

import org.eclipse.jdt.core.ICompilationUnit;
import org.jgrapht.graph.DefaultEdge;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class GraphWindow extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6879792871504557695L;
	private static final Dimension DEFAULT_SIZE = new Dimension(1280, 1024);
	private static final int VERTEX_WIDTH = 30;
	private static final int VERTEX_HEIGHT = 10;
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
		setPreferredSize(DEFAULT_SIZE);
		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
		pack();
		setVisible(true);
	}
	
	private mxGraph construct() {
		mxGraph graph = new mxGraph();
		HashMap<Cluster, Object> clusters = new HashMap<Cluster, Object>();
		HashMap<ICompilationUnit, Object> vertices = new HashMap<ICompilationUnit, Object>();
		Object root = graph.getDefaultParent();
		
		graph.getModel().beginUpdate();
		int i = 0;
		for (DefaultEdge edge : network.edgeSet()) {
			ICompilationUnit source = network.getEdgeSource(edge);
			ICompilationUnit target = network.getEdgeTarget(edge);
			Cluster sourceCluster = network.getCluster(source);
			Cluster targetCluster = network.getCluster(target);
			Object sourceParent = clusters.get(sourceCluster);
			Object targetParent = clusters.get(targetCluster);
			if (sourceParent == null) {
				sourceParent = graph.insertVertex(root, null, "", 0, 0, 0, 0);
				clusters.put(sourceCluster, sourceParent);
			}
			if (targetParent == null) {
				targetParent = graph.insertVertex(root, null, "", 0, 0, 0, 0);
				clusters.put(targetCluster, targetParent);
			}
			try {
				Object v1 = vertices.get(source);
				if (v1 == null) {
					v1 = graph.insertVertex(sourceParent, null, source.getElementName(), 
							0, 0, VERTEX_WIDTH, VERTEX_HEIGHT);
					graph.getModel().setCollapsed(v1, true);
					vertices.put(source, v1);
				}
				Object v2 = vertices.get(target);
				if (v2 == null) {
					v2 = graph.insertVertex(targetParent, null, target.getElementName(), 
							i * 0, 0, VERTEX_WIDTH, VERTEX_HEIGHT);
					graph.getModel().setCollapsed(v2, true);
					vertices.put(target, v2);
				}
				Object edgeParent = (sourceCluster.equals(targetCluster)) ? sourceParent : root;
				Object e1 = graph.insertEdge(edgeParent, null, "", v1, v2);
			} finally {
				// Updates the display
				graph.getModel().endUpdate();
				i++;
			}
		}
		mxGraphLayout layout = new mxHierarchicalLayout(graph);
		for (Entry<Cluster, Object> entry : clusters.entrySet()) {
			layout.execute(entry.getValue());
		}
		layout.execute(root);
		
		return graph;
	}

}
