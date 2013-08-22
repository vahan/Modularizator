package modularizator.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.eclipse.jface.action.IAction;

import modularizator.GephiVisualizor;
import modularizator.logic.Network;

/**
 * Used to shows the network via Gephi toolkit
 * @author vahan
 *
 */
public class VisualizeAction extends BaseAction {
	/**
	 * The network to be visualized
	 */
	Network network;
	
	@Override
	public void run(IAction action) {
		network = readNetwork();
		long id = new Date().getTime();
		try {
			GephiVisualizor win = new GephiVisualizor(network, id);
			javax.swing.SwingUtilities.invokeAndWait(win);
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	
	
	
	
}
