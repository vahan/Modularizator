package modularizator.actions;

import java.util.HashMap;

import logic.Algorithm;
import logic.Cluster;
import logic.Network;
import logic.Scorer;
import modularizator.NetworkReader;
import modularizator.quickfix.QuickFix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class ModularizeAction extends BaseAction {
	private Algorithm algorithm;

	private Scorer scorer;

	private static final int nSteps = 1000; // TODO: make user-defined

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
		IJavaProject javaProject = JavaCore.create(selectedProject);
		NetworkReader reader = new NetworkReader(javaProject);
		Network network = reader.read();

		modularizator.initAlgorithms(network, nSteps);
		algorithm = modularizator.getAlgorithm();

		Network optimizedNetwork = algorithm.optimize(scorer);
		showSuggestions(modularizator.getChanges());
	}

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
				System.out.println(marker.exists());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			System.out.println("Move class '" + compUnit.getElementName() + "' to " + cluster.getModel().getElementName());
		}
	}
}