package modularizator.quickfix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.reorg.JavaMoveProcessor;
import org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgDestinationFactory;
import org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgPolicyFactory;
import org.eclipse.jdt.internal.corext.refactoring.reorg.IReorgPolicy.IMovePolicy;
import org.eclipse.jdt.internal.ui.refactoring.reorg.CreateTargetQueries;
import org.eclipse.jdt.internal.ui.refactoring.reorg.ReorgMoveWizard;
import org.eclipse.jdt.internal.ui.refactoring.reorg.ReorgQueries;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.CreateChangeOperation;
import org.eclipse.ltk.core.refactoring.IUndoManager;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.internal.ui.refactoring.RefactoringWizardDialog;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

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
	 * @param marker	the marker to be fixed
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
			e.printStackTrace();
		} catch (CoreException e) {
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
	public boolean fix() {
		IResource source = marker.getResource();
		IJavaElement javaElem = JavaCore.create(source);
		ICompilationUnit compUnit = (ICompilationUnit) javaElem;
		IProgressMonitor pm = new NullProgressMonitor();
		
		try {
			//create move policy and processor for the compilation unit
			IMovePolicy movePolicy = ReorgPolicyFactory.createMovePolicy(new IResource[] {compUnit.getResource()}, new IJavaElement[] {compUnit});
			JavaMoveProcessor processor = new JavaMoveProcessor(movePolicy);
			processor.setDestination(ReorgDestinationFactory.createDestination(newSource));
			//the refactoring object
			Refactoring refactoring = new ProcessorBasedRefactoring(processor);
			//set a refactoring wizard
			RefactoringWizard wizard = new ReorgMoveWizard(processor, refactoring);
			Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			wizard.setContainer(new RefactoringWizardDialog(activeShell, wizard));
			//set reorg queries (no idea what it is used for, but doesn't work without it)
			processor.setCreateTargetQueries(new CreateTargetQueries(wizard));
			processor.setReorgQueries(new ReorgQueries(activeShell));
			//perform the refactoring and return its success result
			performRefactoring(refactoring);
			boolean status = wizard.getChange() != null;
			System.out.println(status);
			return status;
		} catch (JavaModelException e) {
			e.printStackTrace();
			return false;
		} catch (OperationCanceledException e) {
			e.printStackTrace();
			return false;
		} finally {
			pm.done();
		}
	}
	
	/**
	 * Copied from https://eclipse.googlesource.com/jdt/eclipse.jdt.ui/+/8029c94ba8f0cb368deb09e3b2f19467d14ad8ed/org.eclipse.jdt.ui.tests.refactoring/test%20cases/org/eclipse/jdt/ui/tests/refactoring/RefactoringTest.java
	 * @param ref	the refactoring object
	 * @return		true if refactoring took place, false otherwise (e.g. if there was a conflict and the user chose to cancel the move)
	 */
	protected final boolean performRefactoring(Refactoring ref) {
		final CreateChangeOperation create = new CreateChangeOperation(
				new CheckConditionsOperation(ref, CheckConditionsOperation.ALL_CONDITIONS),
				RefactoringStatus.WARNING);
		final PerformChangeOperation perform = new PerformChangeOperation(create);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		try {
			workspace.run(perform, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		//TODO: return true if and only if the file was moved
		return true;
	}
	

}
