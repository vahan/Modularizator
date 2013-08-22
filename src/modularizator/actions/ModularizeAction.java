package modularizator.actions;

import java.util.Date;
import java.util.HashMap;

import modularizator.logic.Modularizator;
import modularizator.GephiVisualizor;
import modularizator.Logger;
import modularizator.logic.Algorithm;
import modularizator.logic.Cluster;
import modularizator.logic.MarceloScorer;
import modularizator.logic.Network;
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
	 * The active network
	 */
	private Network network = null;
	
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
		Logger logger = Modularizator.getInstance().getLogger();
		logger.addLog("Reading the network");
		Date start = new Date();
		network = readNetwork();
		Date end = new Date();
		logger.addLog("Done in " + (end.getTime() - start.getTime()) / 1000 + " seconds");
		
		modularizator.initAlgorithm(network);
		algorithm = modularizator.getAlgorithm();

		logger.addLog("Running the modularization algorithm");
		start = new Date();
		Network optimizedNetwork = algorithm.optimize();
		end = new Date();
		logger.addLog("Done in " + (end.getTime() - start.getTime()) / 1000 + " seconds");
		
		double newScore = new MarceloScorer(optimizedNetwork).getScore();
		double oldScore = new MarceloScorer(network).getScore();
		showSuggestions(modularizator.getChanges());
		String msg = "Current score is "
				+ Double.toString(oldScore) + "\n" + "The new score will be "
				+ Double.toString(newScore);
		MessageDialog.openInformation(shell, "Score", msg);
		logger.addLog(msg);
		
		logger.addLog("Showing the visualizations");
		start = new Date();
		long id = start.getTime();
		showVisualizations(network, optimizedNetwork, id);
		end = new Date();
		logger.addLog("Done in " + (end.getTime() - start.getTime()) / 1000 + " seconds");
		String outputFolder = Modularizator.getInstance().getOutputFolder();
		logger.save(outputFolder + "/" + network.getName() + "_log_" + id + ".txt");
	}
	/**
	 * Shows the network before and after modularization
	 * The networks are imported into PDFs, which are then opened
	 * @param oldNetwork
	 * @param newNetwork
	 */
	private void showVisualizations(Network oldNetwork, Network newNetwork, long id) {
		GephiVisualizor newWin = new GephiVisualizor(newNetwork, id);
		//Thread newThread = new Thread(newWin);
		//newThread.start();
		newWin.run();
		GephiVisualizor oldWin = new GephiVisualizor(oldNetwork, id);
		//Thread oldThread = new Thread(oldWin);
		//oldThread.start();
		oldWin.run();
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
			//Modularizator.getInstance().addLog("Move class '" + compUnit.getElementName() + "' to " + cluster.getModel().getElementName());
		}
	}
	
}