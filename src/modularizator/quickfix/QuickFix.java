package modularizator.quickfix;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IMarkerResolution;

public class QuickFix implements IMarkerResolution {

	public final static String ATTRIBUTE_NEWSOURCE = "newsource";
	
	private IJavaElement newContainer;
	
	public QuickFix(IJavaElement newContainer) {
		this.newContainer = newContainer;
	}
	
	@Override
	public String getLabel() {
		String label = "Move to " + newContainer.getElementName();
		return label;
	}

	@Override
	public void run(IMarker marker) {
		IResource source = marker.getResource();
		IProject project = source.getProject();

		IFile file = (IFile) project.findMember(source.getProjectRelativePath());
		IPath destination = newContainer.getPath().append(file.getName());

		try {
			file.move(destination, true, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*IPath destination = newSource.getProjectRelativePath();
		destination.append("/src");
		destination = destination.append(source.getName());
		try {
			source.move(destination, false, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
