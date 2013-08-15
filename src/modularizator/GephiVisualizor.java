package modularizator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.jgrapht.graph.DefaultEdge;
import org.openide.util.Lookup;

import processing.core.PApplet;
import logic.Cluster;
import logic.Network;

public class GephiVisualizor extends JFrame implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5978940395530143652L;
	private final Network network;
	
	public GephiVisualizor(Network network) {
		this.network = network;
	}
	
	@Override
	public void run() {
		exportAndOpen();
		//showInApplet();
	}
	
	public void exportAndOpen() {
		//Preview
		PApplet applet = makeApplet();
		//Export
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		String fileName = "/home/vahan/Desktop/" + network.getName() + ".pdf";
		File file = new File(fileName);
		try {
			ec.exportFile(file);
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
			}
			else {
				JOptionPane.showMessageDialog(null, 
						"Can't open the exported file from " + file, "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}
	
	public void showInApplet() {
		PApplet applet = makeApplet();
		//Add the applet to a JFrame and display it
		setTitle(network.getName());
		setLayout(new BorderLayout());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(applet, BorderLayout.CENTER);

		pack();
		setVisible(true);
	}
	
	public PApplet makeApplet() {
		DirectedGraph dGraph = makeGephiGraph(network);

        System.out.println("Nodes: " + dGraph.getNodeCount());
        System.out.println("Edges: " + dGraph.getEdgeCount());
        
		GraphModel graphModel = dGraph.getGraphModel();
		//Run the layout algorithm
		runYifanHuLayoutAlg(graphModel);
		//Color nodes according to their labels
		color(dGraph);
		//Set the previeController
		PreviewController previewController = setPreviewAttributes();
		//New Processing target, get the PApplet
		ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		PApplet applet = target.getApplet();
		applet.init();
		//Refresh the preview and reset the zoom
		previewController.render(target);
		target.refresh();
		//target.resetZoom();
		
		return applet;
	}

	private DirectedGraph makeGephiGraph(Network network) {
		// Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		DirectedGraph dGraph = graphModel.getDirectedGraph();//Represent the network via clusters
		HashMap<ICompilationUnit, Cluster> nodes = network.getClusters();
		//Add vertices to the graph
		for (Entry<ICompilationUnit, Cluster> entry : nodes.entrySet()) {
			ICompilationUnit cu = entry.getKey();
			Cluster cluster = entry.getValue();
			Node node = graphModel.factory().newNode(vertexName(cu));
			node.getNodeData().setLabel(Integer.toString(cluster.getId()));
			dGraph.addNode(node);
		}
		//Add edges to graph
		for (DefaultEdge e : network.edgeSet()) {
			ICompilationUnit source = network.getEdgeSource(e);
			Node sourceNode = dGraph.getNode(vertexName(source));
			ICompilationUnit target = network.getEdgeTarget(e);
			Node targetNode = dGraph.getNode(vertexName(target));
			Edge edge = graphModel.factory().newEdge(sourceNode, targetNode, 1f, true);
			edge.getEdgeData().setLabel(sourceNode.getNodeData().getLabel());
			dGraph.addEdge(edge);
		}
		return dGraph;
	}
	
	private PreviewController setPreviewAttributes() {
		// Preview configuration
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel prModel = previewController.getModel();
		PreviewProperties props = prModel.getProperties();
		props.putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(EdgeColor.Mode.SOURCE));
		props.putValue(PreviewProperty.EDGE_THICKNESS, new Float(0.2f));
		props.putValue(PreviewProperty.NODE_BORDER_WIDTH, 0f);
		props.putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE);
		previewController.refreshPreview();
		return previewController;
	}
	
	private void runYifanHuLayoutAlg(GraphModel graphModel) {
		// Run Yifan Hu layout algorithm on the graph
		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
		layout.setOptimalDistance(20f);
		layout.initAlgo();
		for (int i = 0; i < 100 && layout.canAlgo(); ++i) {
			layout.goAlgo();
		}
	}
	
	private void color(DirectedGraph dGraph) {
		//Partition the graph and color accordingly
		PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		Partition p = partitionController.buildPartition(attributeModel.getNodeTable().getColumn("label"), dGraph);
		NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
		nodeColorTransformer.randomizeColors(p);
		partitionController.transform(p, nodeColorTransformer);
	}
	
	private String vertexName(ICompilationUnit vertex) {
		Cluster cluster = network.getCluster(vertex);
		String vertexName = vertex.getElementName().substring(0, vertex.getElementName().lastIndexOf("."));
		String name = format(cluster.getModel().getElementName()) + "_" + vertexName;
		return name;
	}
	
	private String format(String name) {
		String formatted = StringUtils.replace(name, ".", "_");
		return formatted;
	}


}
