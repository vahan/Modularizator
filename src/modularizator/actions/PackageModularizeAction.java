package modularizator.actions;


import logic.Algorithm;
import logic.MarceloAlgorithm;
import logic.Network;
import logic.PackageModularizator;
import logic.Scorer;
import modularizator.NetworkReader;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
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
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class PackageModularizeAction implements IObjectActionDelegate {
	
	private IProject selectedProject;
	
	private Shell shell;
	
	private PackageModularizator modularizator;
	
	private Algorithm algorithm;
	
	private Scorer scorer;
	
	private static final int nSteps = 1000; //TODO: make user-defined
	
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
		
		modularizator = new PackageModularizator(network, nSteps);
		algorithm = new MarceloAlgorithm(modularizator.getNetwork(), nSteps);
		
		MessageDialog.openInformation(
			shell,
			"Package Modularizator Tips",
			"run: javaProject = " + javaProject.getElementName());
		
		DirectedGraph<Object, DefaultEdge> optimizedGraph = algorithm.optimize(scorer);
		
		showOptimizedGraph(optimizedGraph);
		
		MessageDialog.openInformation(
			shell,
			"Package Modularizator Tips",
			"See individual files for tips");
	}


	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object firstElement = strSelection.getFirstElement();
		String projectName = "";
		if (firstElement instanceof IAdaptable)
		{
			IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
			projectName = project.getName();
		}
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		selectedProject = workspaceRoot.getProject(projectName);
		
		//IJavaProject project = javaModel.getJavaProject();
		
		MessageDialog.openInformation(
				shell,
				"Package Modularizator Tips",
				"selectionChanged selection: " + selectedProject.getName());
	}

	
	private void showOptimizedGraph(DirectedGraph<Object, DefaultEdge> graph) {
		MessageDialog.openInformation(
				shell.getShell(),
				"TODO",
				"show the graph tips on individual project files");
		//TODO: show the graph tips on individual project files
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		
	}
}