package modularizator.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.action.IAction;

import logic.Network;
import modularizator.GephiVisualizor;


public class VisualizeAction extends BaseAction {
	
	Network network;
	
	@Override
	public void run(IAction action) {
		network = readNetwork();
		
		try {
			GephiVisualizor win = new GephiVisualizor(network);
			javax.swing.SwingUtilities.invokeAndWait(win);
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	
	
	
	
}
