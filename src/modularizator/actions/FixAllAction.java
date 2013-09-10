package modularizator.actions;

import javax.swing.JOptionPane;

import modularizator.quickfix.Fixer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

/**
 * Used to fix all modularizator markers
 * @author vahan
 *
 */
public class FixAllAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		IWorkspaceRoot root = selectedProject.getWorkspace().getRoot();
		try {
			IMarker[] markers = root.findMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
			int reply = JOptionPane.showConfirmDialog(null, "Are you sure moving " + markers.length + " classes into new packages? \nThis action cannot be undone",
					"Confirm", JOptionPane.YES_NO_OPTION);
			if (reply != JOptionPane.YES_OPTION) {
				return;
			}
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
