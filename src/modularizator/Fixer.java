package modularizator;

import modularizator.quickfix.QuickFix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
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
/**
 * Used to fixes the markers generated after modularization
 * @author vahan
 *
 */
public class Fixer {
	/**
	 * The marker to be fixed
	 */
	private final IMarker marker;
	/**
	 * The new location of the class, on which the marker is put
	 */
	private IJavaElement newSource = null;
	/**
	 * Constructor
	 * @param marker
	 */
	public Fixer(IMarker marker) {
		super();
		this.marker = marker;
		IResource source = marker.getResource();
		IProject project = source.getProject();
		IJavaProject javaProject = JavaCore.create(project);
		IPackageFragment[] packageFragments;
		try {
			packageFragments = javaProject.getPackageFragments();
			String newSourceName;
			newSourceName = (String) marker.getAttribute(QuickFix.ATTRIBUTE_NEWSOURCE);
			for (IPackageFragment pckgFrg : packageFragments) {
				if (pckgFrg.getElementName().equals(newSourceName)) {
					newSource = pckgFrg;
					break;
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public IMarker getMarker() {
		return marker;
	}
	
	
	public IJavaElement getNewSource() {
		return newSource;
	}
	
	/**
	 * Makes the fix
	 */
	public void fix() {
		IResource source = marker.getResource();
		IJavaElement javaElem = JavaCore.create(source);
		ICompilationUnit compUnit = (ICompilationUnit) javaElem;
		IProgressMonitor pm = new NullProgressMonitor();
		
		try {
			//The following code is copied from org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgPolicyFactory
			CompositeChange composite= new DynamicValidationStateChange(RefactoringCoreMessages.ReorgPolicy_move);
			MoveCuUpdateCreator creator= new MoveCuUpdateCreator(new ICompilationUnit[] {compUnit}, (IPackageFragment) newSource);
			TextChangeManager fChangeManager = creator.createChangeManager(new SubProgressMonitor(pm, 1), new RefactoringStatus());
			composite.merge(new CompositeChange(RefactoringCoreMessages.MoveRefactoring_reorganize_elements, fChangeManager.getAllChanges()));
			Change change = new MoveCompilationUnitChange(compUnit, (IPackageFragment) newSource);
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
