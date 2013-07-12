package modularizator.actions;

import logic.Modularizator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public abstract class BaseAction implements IObjectActionDelegate {
	
	public final static String MARKER_NAME = "modularizator.markers.problem";

	protected IProject selectedProject;

	protected Shell shell;

	protected Modularizator modularizator = Modularizator.getInstance();


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
	
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();

	}
}
