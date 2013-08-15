package modularizator.actions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.JFrame;

import logic.Cluster;
import logic.Network;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.gef4.zest.core.widgets.Graph;
import org.eclipse.gef4.zest.dot.DotGraph;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
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
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.jgrapht.graph.DefaultEdge;
import org.openide.util.Lookup;

import processing.core.PApplet;


public class VisualizeAction extends BaseAction {
	
	Network network;
	
	@Override
	public void run(IAction action) {
		network = readNetwork();
		
		PApplet applet = makeGephiApplet(network);
		showGephiApplet(applet);
	}
	
	public PApplet makeGephiApplet(Network network) {
		DirectedGraph dGraph = makeGephiGraph(network);
		GraphModel graphModel = dGraph.getGraphModel();
		//Run the layout algorithm
		runYifanHuLayout(graphModel);
		//Color nodes according to their labels
		color(dGraph);
		//Set the previeController
		PreviewController previewController = makeGephiPreviewController();
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
	
	private void showGephiApplet(PApplet applet) {
		//Add the applet to a JFrame and display it
		JFrame frame = new JFrame("Interaction Graph");
		frame.setLayout(new BorderLayout());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(applet, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
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
	
	private PreviewController makeGephiPreviewController() {
		// Preview configuration
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel prModel = previewController.getModel();
		PreviewProperties props = prModel.getProperties();
		props.putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(EdgeColor.Mode.SOURCE));
		props.putValue(PreviewProperty.EDGE_THICKNESS, new Float(0.1f));
		props.putValue(PreviewProperty.NODE_BORDER_WIDTH, 0f);
		props.putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE);
		previewController.refreshPreview();
		return previewController;
	}
	
	private void runYifanHuLayout(GraphModel graphModel) {
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
