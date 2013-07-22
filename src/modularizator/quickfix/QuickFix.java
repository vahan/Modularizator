package modularizator.quickfix;


import modularizator.Fixer;
import modularizator.actions.BaseAction;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;

public class QuickFix implements IMarkerResolution {

	public final static String ATTRIBUTE_NEWSOURCE = "newsource";
	
	private Fixer fixer;
	
	public QuickFix(IMarker marker) {
		this.fixer = new Fixer(marker);
	}
	
	@Override
	public String getLabel() {
		String label = "Move to " + fixer.getNewSource().getElementName();
		return label;
	}

	@Override
	public void run(IMarker marker) {
		IResource resource = marker.getResource();
		try {
			resource.deleteMarkers(BaseAction.MARKER_TYPE, true, IResource.DEPTH_ZERO);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fixer.fix();
	}
	

}
