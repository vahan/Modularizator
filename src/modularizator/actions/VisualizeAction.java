package modularizator.actions;

import java.util.HashMap;
import java.util.Map.Entry;

import logic.Cluster;
import logic.Network;

import org.eclipse.gef4.zest.dot.DotGraph;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jgrapht.graph.DefaultEdge;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.view.mxGraph;

import windows.GraphWindow;


public class VisualizeAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		Network network = readNetwork();
		
		show(network);
		
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

	private void show(Network network) {
		Shell shell = new Shell();
		DotGraph graph = new DotGraph("digraph {}", shell, SWT.NONE);
		for (DefaultEdge edge : network.edgeSet()) {
			String source = network.getEdgeSource(edge).getElementName();
			source = source.substring(0, source.indexOf("."));
			String target = network.getEdgeTarget(edge).getElementName();
			target = target.substring(0, target.indexOf("."));
			graph.add(source + "->" + target);
		}
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

}
