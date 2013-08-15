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
		
		//show();
		
		/*GraphWindow window = new GraphWindow(network);
		Thread thread = new Thread(window);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private void makeGephi() {
		// Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		DirectedGraph dGraph = graphModel.getDirectedGraph();
		//Represent the network via clusters
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
		
		// Preview configuration
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel prModel = previewController.getModel();
		PreviewProperties props = prModel.getProperties();
		props.putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.FALSE);
		props.putValue(PreviewProperty.NODE_BORDER_WIDTH, 0f);
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
		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
		layout.setOptimalDistance(200f);
		layout.initAlgo();
		for (int i = 0; i < 10000 && layout.canAlgo(); ++i) {
			layout.goAlgo();
		}
		
		//New Processing target, get the PApplet
		ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		PApplet applet = target.getApplet();
		applet.init();

		//Refresh the preview and reset the zoom
		previewController.render(target);
		target.refresh();
		target.resetZoom();

		//Add the applet to a JFrame and display
		JFrame frame = new JFrame("Interaction Graph");
		frame.setLayout(new BorderLayout());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(applet, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}
	
	
	private void showGephi(DirectedGraph dGraph) {
		/*GraphModel graphModel = dGraph.getGraphModel();
		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
		layout.setOptimalDistance(200f);
		layout.initAlgo();
		for (int i = 0; i < 100 && layout.canAlgo(); ++i) {
			layout.goAlgo();
		}*/
		
		// Preview configuration
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel prModel = previewController.getModel();
		PreviewProperties props = prModel.getProperties();
		props.putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		previewController.refreshPreview();
		
		//New Processing target, get the PApplet
		ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		PApplet applet = target.getApplet();
		applet.init();

		//Refresh the preview and reset the zoom
		previewController.render(target);
		target.refresh();
		target.resetZoom();

		//Add the applet to a JFrame and display
		JFrame frame = new JFrame("Interaction Graph");
		frame.setLayout(new BorderLayout());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(applet, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}
	

	private void show() {
		//Represent the network via clusters
		/*HashMap<Cluster, ArrayList<ICompilationUnit>> clusters = new HashMap<Cluster, ArrayList<ICompilationUnit>>();
		for (Entry<ICompilationUnit, Cluster> entry : network.getClusters().entrySet()) {
			ArrayList<ICompilationUnit> compUnits = clusters.get(entry.getValue());
			if (compUnits == null) {
				compUnits = new ArrayList<ICompilationUnit>();
				compUnits.add(entry.getKey());
				clusters.put(entry.getValue(), compUnits);
			} else {
				compUnits.add(entry.getKey());
			}
		}
		//Make a dot string
		//Add vertices to the dot string
		String dotStr = "digraph NAME { node [label=\"\", shape=point, style=filled, fillcolor=red]; ";
		for (Entry<Cluster, ArrayList<ICompilationUnit>> entry : clusters.entrySet()) {
			String clusterName = entry.getKey().getModel().getElementName();
			dotStr += "subgraph cl_" + format(clusterName) + " { rank = same; ";
			String color = randomColor();
			//dotStr += "node [fillcolor=red, color=\"" + color + "\"]; ";
			for (ICompilationUnit compUnit : entry.getValue()) {
				dotStr += vertexName(compUnit) + " [fillcolor=green, color=\"" + color + "\"]; ";
			}
			dotStr += " } ";
		}
		//Add edges to the dot string
		for (DefaultEdge edge : network.edgeSet()) {
			ICompilationUnit source = network.getEdgeSource(edge);
			ICompilationUnit target = network.getEdgeTarget(edge);
			dotStr += " " + vertexName(source) + " -> " + vertexName(target) + "; ";
		}
		
		dotStr += "} ";*/
		BufferedReader br = null;
		String dotStr = "";
		try {
 			String sCurrentLine;
 			br = new BufferedReader(new FileReader("/home/vahan/Desktop/test.dot"));
 			while ((sCurrentLine = br.readLine()) != null) {
				dotStr += sCurrentLine + "\n";
			}
 		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		MessageDialog.openInformation(shell, "dotStr", dotStr);
		
		//Draw it on a new shell
		Shell shell = new Shell();
		DotGraph graph = new DotGraph(dotStr, shell, SWT.NONE);
		//DotGraph graph = new DotGraph("digraph { subgraph cluster1 { n1; } subgraph cluster2 { n2; } subgraph cluster3 { n3; } n1 -> n2; n2 -> n3; n3 -> n1; }", shell, SWT.NONE);
		
		//graph.pack();
		//shell.pack();
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
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
	
	private String randomColor() {
		double r = Math.round(rand.nextFloat() * 10000.0) / 10000.0;
		double g = Math.round(rand.nextFloat() * 10000.0) / 10000.0;
		double b = Math.round(rand.nextFloat() * 10000.0) / 10000.0;
		return Double.toString(r) + "," + g + "," + b;
	}

}
