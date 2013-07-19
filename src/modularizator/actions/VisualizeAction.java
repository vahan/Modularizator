package modularizator.actions;

import java.awt.Color;
import java.awt.Dimension;

import logic.Network;

import org.eclipse.jface.action.IAction;
import org.jgraph.JGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.mxgraph.view.mxGraph;

import windows.GraphWindow;


public class VisualizeAction extends BaseAction {
	private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
	private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);
	
	@Override
	public void run(IAction action) {
		Network network = readNetwork();
		
		GraphWindow window = new GraphWindow(network);
		Thread thread = new Thread(window);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
