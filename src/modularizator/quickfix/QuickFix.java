package modularizator.quickfix;

import modularizator.actions.BaseAction;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;

/**
 * Used to fix the modularizator markers
 * @author vahan
 *
 */
public class QuickFix implements IMarkerResolution {
	/**
	 * 
	 */
	public final static String ATTRIBUTE_NEWSOURCE = "newsource";
	/**
	 * The fixer object that makes fix
	 */
	private Fixer fixer;
	/**
	 * Constructor
	 * @param marker
	 */
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
		boolean success = fixer.fix();
		if (!success)
			return;
		IResource resource = marker.getResource();
		try {
			resource.deleteMarkers(BaseAction.MARKER_TYPE, true, IResource.DEPTH_ZERO);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	

}
