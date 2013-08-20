package modularizator.actions;

import java.util.Date;
import java.util.HashMap;

import logic.Algorithm;
import logic.Cluster;
import logic.MarceloScorer;
import logic.Network;
import modularizator.GephiVisualizor;
import modularizator.quickfix.QuickFix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;


/**
 * Used to run the modularization
 * @author vahan
 *
 */
public class ModularizeAction extends BaseAction {
	/**
	 * The algorithm to be used for modularizing
	 */
	private Algorithm algorithm;

	/**
	 * The constructor.
	 */
	public ModularizeAction() {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		Network network = readNetwork();
		modularizator.initAlgorithm(network);
		algorithm = modularizator.getAlgorithm();

		System.out.println("Starting running the modularization algorithm");
		Date start = new Date();
		Network optimizedNetwork = algorithm.optimize();
		Date end = new Date();
		System.out.println("Finished after " + (end.getTime() - start.getTime()) / 100 + " seconds");
		
		double newScore = new MarceloScorer(optimizedNetwork).getScore();
		double oldScore = new MarceloScorer(network).getScore();
		showSuggestions(modularizator.getChanges());
		String msg = "Current score is "
				+ Double.toString(oldScore) + "\n" + "The new score will be "
				+ Double.toString(newScore);
		MessageDialog.openInformation(shell, "Score", msg);
		System.out.println(msg);
		showVisualizations(network, optimizedNetwork);
	}
	/**
	 * Shows the network before and after modularization
	 * The networks are imported into PDFs, which are then opened
	 * @param oldNetwork
	 * @param newNetwork
	 */
	private void showVisualizations(Network oldNetwork, Network newNetwork) {
		long id = new Date().getTime();
		GephiVisualizor newWin = new GephiVisualizor(newNetwork, id);
		//Thread newThread = new Thread(newWin);
		//newThread.start();
		newWin.run();
		GephiVisualizor oldWin = new GephiVisualizor(oldNetwork, id);
		oldWin.run();
		//Thread oldThread = new Thread(oldWin);
		//oldThread.start();
	}
	/**
	 * Shows all markers arising after running the modularization algorithm
	 * @param changes
	 */
	private void showSuggestions(HashMap<Object, Cluster> changes) {
		removeAllMarkers();
		for (Object vertex : changes.keySet()) {
			ICompilationUnit compUnit = (ICompilationUnit) vertex;
			Cluster cluster = changes.get(vertex);
			int line = 1; //TODO put the actual package declaration line
			String newSourceName = cluster.getModel().getElementName();
			String message = "Move to package " + newSourceName;
			try {
				IMarker marker = compUnit.getResource().createMarker(MARKER_NAME);
				marker.setAttribute(IMarker.LINE_NUMBER, line);
				marker.setAttribute(IMarker.MESSAGE, message);
				marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
				marker.setAttribute(QuickFix.ATTRIBUTE_NEWSOURCE, newSourceName);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			//System.out.println("Move class '" + compUnit.getElementName() + "' to " + cluster.getModel().getElementName());
		}
	}
}