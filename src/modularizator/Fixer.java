package modularizator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.jdt.internal.corext.refactoring.changes.DynamicValidationStateChange;
import org.eclipse.jdt.internal.corext.refactoring.changes.MoveCompilationUnitChange;
import org.eclipse.jdt.internal.corext.refactoring.reorg.MoveCuUpdateCreator;
import org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

@SuppressWarnings("restriction")
public class Fixer {
	
	private IJavaElement newContainer;
	
	public Fixer(IJavaElement newContainer) {
		super();
		this.newContainer = newContainer;
	}

	public void fix(IMarker marker) {
		IResource source = marker.getResource();
		IJavaElement javaElem = JavaCore.create(source);
		ICompilationUnit compUnit = (ICompilationUnit) javaElem;
		IProgressMonitor pm = new NullProgressMonitor();
		
		try {
			//TODO The following code is copied from org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgPolicyFactory
			CompositeChange composite= new DynamicValidationStateChange(RefactoringCoreMessages.ReorgPolicy_move);
			MoveCuUpdateCreator creator= new MoveCuUpdateCreator(new ICompilationUnit[] {compUnit}, (IPackageFragment) newContainer);
			TextChangeManager fChangeManager = creator.createChangeManager(new SubProgressMonitor(pm, 1), new RefactoringStatus());
			composite.merge(new CompositeChange(RefactoringCoreMessages.MoveRefactoring_reorganize_elements, fChangeManager.getAllChanges()));
			Change change = new MoveCompilationUnitChange(compUnit, (IPackageFragment) newContainer);
			if (change instanceof CompositeChange) {
				composite.merge(((CompositeChange) change));
			} else {
				composite.add(change);
			}
			composite.perform(pm);
			
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
