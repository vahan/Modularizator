package modularizator.actions;

import java.util.HashMap;

import logic.Algorithm;
import logic.Cluster;
import logic.Modularizator;
import logic.Network;
import logic.Scorer;
import modularizator.NetworkReader;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.refactoring.nls.KeyValuePair;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class PackageModularizeAction implements IObjectActionDelegate {

	private IProject selectedProject;

	private Shell shell;

	private Modularizator modularizator = Modularizator.getInstance();

	private Algorithm algorithm;

	private Scorer scorer;

	private static final int nSteps = 1000; // TODO: make user-defined

	/**
	 * The constructor.
	 */
	public PackageModularizeAction() {
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

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (!(selection instanceof IStructuredSelection))
			return;
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object firstElement = strSelection.getFirstElement();
		String projectName = "";
		IAdaptable iAdaptObj = (IAdaptable) firstElement;
		if (iAdaptObj == null)
			return;
		IProject project = (IProject) iAdaptObj.getAdapter(IProject.class);
		if (project == null)
			return;
		projectName = project.getName();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		selectedProject = workspaceRoot.getProject(projectName);
	}

	private void showSuggestions(HashMap<Object, Cluster> changes) {
		MessageDialog.openInformation(shell.getShell(), "TODO",
				"show the graph tips on individual project files");
		// TODO: show the graph tips on individual project files
		
		for (Object vertex : changes.keySet()) {
			ICompilationUnit compUnit = (ICompilationUnit) vertex;
			Cluster cluster = changes.get(vertex);
			System.out.println("Move class '" + compUnit.getElementName() + "' to " + cluster.getModel().getElementName());
		}
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();

	}
}