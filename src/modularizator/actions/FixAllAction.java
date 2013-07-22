package modularizator.actions;

import modularizator.Fixer;
import modularizator.quickfix.QuickFix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;

public class FixAllAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		IWorkspaceRoot root = selectedProject.getWorkspace().getRoot();
		try {
			IMarker[] markers = root.findMarkers("org.eclipse.core.resources.problemmarker", true, IResource.DEPTH_INFINITE);
			for (IMarker marker : markers) {
				IResource source = marker.getResource();
				IProject project = source.getProject();
				IJavaProject javaProject = JavaCore.create(project);
				IPackageFragment[] packageFragments = javaProject.getPackageFragments();
				String newSourceName = (String) marker.getAttribute(QuickFix.ATTRIBUTE_NEWSOURCE);
				IJavaElement newSource = null;
				for (IPackageFragment pckgFrg : packageFragments) {
					if (pckgFrg.getElementName().equals(newSourceName))
						newSource = pckgFrg;
				}
				Fixer fixer = new Fixer(newSource);
				fixer.fix(marker);
			}
			removeAllMarkers();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}



}
