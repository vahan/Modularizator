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
	
	private Network network = null;
	
	private Random rand = new Random();
	
	@Override
	public void run(IAction action) {
		network = readNetwork();
		
		makeGephi();
		
	}
	
	private void makeGephi() {
		// Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		DirectedGraph dGraph = graphModel.getDirectedGraph();
		//Add nodes and edges to graph
		for (DefaultEdge e : network.edgeSet()) {
			ICompilationUnit source = network.getEdgeSource(e);
			Cluster sourceCluster = network.getCluster(source);
			Node sourceNode = graphModel.factory().newNode(vertexName(source));
			sourceNode.getNodeData().setLabel(Integer.toString(sourceCluster.getId()));
			//sourceNode.getNodeData().setSize(5);
			dGraph.addNode(sourceNode);
			
			ICompilationUnit target = network.getEdgeTarget(e);
			Cluster targetCluster = network.getCluster(target);
			Node targetNode = graphModel.factory().newNode(vertexName(target));
			targetNode.getNodeData().setLabel(Integer.toString(targetCluster.getId()));
			//targetNode.getNodeData().setSize(5);
			dGraph.addNode(targetNode);
			
			Edge edge = graphModel.factory().newEdge(sourceNode, targetNode, 1f, true);
			//edge.getEdgeData().setLabel(sourceNode.getNodeData().getLabel());
			dGraph.addEdge(edge);
			System.out.println("Is " + dGraph.getEdge(sourceNode, targetNode) + " directed: " + edge.isDirected());
		}
		
		// Preview configuration
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel prModel = previewController.getModel();
		PreviewProperties props = prModel.getProperties();
		props.putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		props.putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.BLACK));
		//props.putValue(PreviewProperty.EDGE_THICKNESS, new Float(0.1f));
		//props.putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE);
		//props.putValue(PreviewProperty.NODE_BORDER_WIDTH, 0f);
		//props.putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.BLACK));
		props.putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE);
		previewController.refreshPreview();

		//Partition the graph and color accordingly
		PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		Partition p = partitionController.buildPartition(attributeModel.getNodeTable().getColumn("label"), dGraph);
		NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
		nodeColorTransformer.randomizeColors(p);
		partitionController.transform(p, nodeColorTransformer);
		
		// Run Yifan Hu layout algorithm on the graph
		/*YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
		layout.setOptimalDistance(200f);
		layout.initAlgo();
		for (int i = 0; i < 10000 && layout.canAlgo(); ++i) {
			layout.goAlgo();
		}*/
		
		//New Processing target, get the PApplet
		ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		PApplet applet = target.getApplet();
		applet.init();

		//Refresh the preview and reset the zoom
		previewController.render(target);
		target.refresh();
		//target.resetZoom();

		//Add the applet to a JFrame and display
		JFrame frame = new JFrame("Interaction Graph");
		frame.setLayout(new BorderLayout());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(applet, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
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
