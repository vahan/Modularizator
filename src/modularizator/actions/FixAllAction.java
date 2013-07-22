package modularizator.actions;

import modularizator.Fixer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

public class FixAllAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		IWorkspaceRoot root = selectedProject.getWorkspace().getRoot();
		try {
			IMarker[] markers = root.findMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
			for (IMarker marker : markers) {
				Fixer fixer = new Fixer(marker);
				fixer.fix();
			}
			removeAllMarkers();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}



}
