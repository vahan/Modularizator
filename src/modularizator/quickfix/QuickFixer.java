package modularizator.quickfix;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.actions.ProjectActionGroup;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

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