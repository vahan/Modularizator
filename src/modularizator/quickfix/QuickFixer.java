package modularizator.quickfix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class QuickFixer implements IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		IResource source = marker.getResource();
		IProject project = source.getProject();
		
		IJavaProject javaProject = JavaCore.create(project);
		
		try {
			IPackageFragment[] packageFragments = javaProject.getPackageFragments();
			String newSourceName = (String) marker.getAttribute(QuickFix.ATTRIBUTE_NEWSOURCE);
			IJavaElement newSource = null;
			for (IPackageFragment pckgFrg : packageFragments) {
				if (pckgFrg.getElementName().equals(newSourceName))
					newSource = pckgFrg;
			}
			return new IMarkerResolution[] {
				new QuickFix(newSource),
			};
		} catch (CoreException e) {
			return new IMarkerResolution[0];
		}
	}

}
