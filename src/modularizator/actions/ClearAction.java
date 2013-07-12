package modularizator.actions;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

public class ClearAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		IWorkspaceRoot root = selectedProject.getWorkspace().getRoot();
		removeAllMarkers(root);

	}

	private void removeAllMarkers(IWorkspaceRoot root) {
		try {
			root.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
