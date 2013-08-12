package modularizator.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import logic.Cluster;
import logic.Network;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.gef4.zest.dot.DotGraph;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jgrapht.graph.DefaultEdge;


public class VisualizeAction extends BaseAction {
	
	private Network network = null;
	
	private Random rand = new Random();
	
	@Override
	public void run(IAction action) {
		network = readNetwork();
		
		show();
		
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

	private void show() {
		//Represent the network via clusters
		HashMap<Cluster, ArrayList<ICompilationUnit>> clusters = new HashMap<Cluster, ArrayList<ICompilationUnit>>();
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
		String dotStr = "digraph NAME { node [shape=point]; ";
		for (Entry<Cluster, ArrayList<ICompilationUnit>> entry : clusters.entrySet()) {
			String clusterName = entry.getKey().getModel().getElementName();
			dotStr += "subgraph cluster_" + format(clusterName) + " { ";
			
			dotStr += "node [label=\"\",style=filled,color=\"" + randomColor() + "\"]; ";
			for (ICompilationUnit compUnit : entry.getValue()) {
				dotStr += vertexName(compUnit) + "; ";
			}
			dotStr += " } ";
		}
		//Add edges to the dot string
		for (DefaultEdge edge : network.edgeSet()) {
			ICompilationUnit source = network.getEdgeSource(edge);
			ICompilationUnit target = network.getEdgeTarget(edge);
			dotStr += " " + vertexName(source) + " -> " + vertexName(target) + "; ";
		}
		
		dotStr += "} ";
		
		MessageDialog.openInformation(shell, "dotStr", dotStr);
		
		//Draw it on a new shell
		Shell shell = new Shell();
		DotGraph graph = new DotGraph(dotStr, shell, SWT.NONE);
		//DotGraph graph = new DotGraph("digraph { subgraph cluster1 { n1; } subgraph cluster2 { n2; } subgraph cluster3 { n3; } n1 -> n2; n2 -> n3; n3 -> n1; }", shell, SWT.NONE);
		
		shell.setText(DotGraph.class.getSimpleName());
		shell.setLayout(new FillLayout());
		shell.setSize(600, 300);
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
