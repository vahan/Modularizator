package modularizator.actions;

import modularizator.NetworkReader;
import modularizator.logic.Modularizator;
import modularizator.logic.Network;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Abstract base class for all actions
 * @author vahan
 *
 */
public abstract class BaseAction implements IObjectActionDelegate {
	/**
	 * Fixed name for modularizator markers, used in extensions
	 */
	public final static String MARKER_NAME = "modularizator.markers.problem";
	/**
	 * Fixed type for modularizator markers, used in extensions
	 */
	public final static String MARKER_TYPE = "org.eclipse.core.resources.problemmarker";
	/**
	 * The currently selected eclipse project
	 */
	protected IProject selectedProject;
	/**
	 * The current shell, to be used as a parent shell for messages
	 */
	protected Shell shell;
	/**
	 * The modularizator object
	 */
	protected Modularizator modularizator = Modularizator.getInstance();


	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 * Changes the current project once the user selects a new one
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
	/**
	 * Reads the projects into a network
	 * @return	the generated network
	 */
	protected Network readNetwork() {
		IJavaProject javaProject = JavaCore.create(selectedProject);
		NetworkReader reader = new NetworkReader(javaProject);
		Network network = reader.read();
		return network;
	}
	/**
	 * Clears all modularizator markers
	 */
	protected void removeAllMarkers() {
		IWorkspaceRoot root = selectedProject.getWorkspace().getRoot();
		try {
			root.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
