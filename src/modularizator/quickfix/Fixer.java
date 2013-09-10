package modularizator.quickfix;

import modularizator.logic.Modularizator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameCompilationUnitProcessor;
import org.eclipse.jdt.internal.corext.refactoring.reorg.IReorgDestination;
import org.eclipse.jdt.internal.corext.refactoring.reorg.JavaMoveProcessor;
import org.eclipse.jdt.internal.corext.refactoring.reorg.MoveCuUpdateCreator;
import org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgDestinationFactory;
import org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgPolicyFactory;
import org.eclipse.jdt.internal.corext.refactoring.reorg.IReorgPolicy.IMovePolicy;
import org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager;
import org.eclipse.jdt.internal.ui.refactoring.reorg.CreateTargetQueries;
import org.eclipse.jdt.internal.ui.refactoring.reorg.ReorgMoveStarter;
import org.eclipse.jdt.internal.ui.refactoring.reorg.ReorgMoveWizard;
import org.eclipse.jdt.internal.ui.refactoring.reorg.ReorgQueries;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.CreateChangeOperation;
import org.eclipse.ltk.core.refactoring.IUndoManager;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.ResourceChangeChecker;
import org.eclipse.ltk.core.refactoring.participants.ValidateEditChecker;
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
			/*CompositeChange composite = new DynamicValidationStateChange(RefactoringCoreMessages.RenameCompilationUnitChange_name);
			//Rename change
			RenameCompilationUnitProcessor renameProcessor = new RenameCompilationUnitProcessor(compUnit);
			renameProcessor.setNewElementName(renameProcessor.getCurrentElementName() + "_from_" + compUnit.getPackageDeclarations()[0].getElementName());
			CheckConditionsContext checker = new CheckConditionsContext();
			checker.add(new ResourceChangeChecker());
			checker.add(new ValidateEditChecker(null));
			renameProcessor.checkFinalConditions(pm, checker);
			Change renameChange = renameProcessor.createChange(new SubProgressMonitor(pm, 1));
			if (renameChange instanceof CompositeChange) {
				composite.merge(((CompositeChange) renameChange));
			} else {
				composite.add(renameChange);
			}
			Change undoRenamChange = composite.perform(pm);*/
			//Change undoRenamChange = renameChange.perform(pm);
			//compUnit = (ICompilationUnit) undoRenamChange.getModifiedElement();
			//Move change
			//The following code is copied from org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgPolicyFactory
			//TODO: Use org.eclipse.ltk.core.refactoring.CreateChangeOperation.run(IProgressMonitor)
			//
			
			IMovePolicy movePolicy = ReorgPolicyFactory.createMovePolicy(new IResource[] {compUnit.getResource()}, new IJavaElement[] {compUnit});
			JavaMoveProcessor processor = new JavaMoveProcessor(movePolicy);
			processor.setDestination(ReorgDestinationFactory.createDestination(newSource));
			Refactoring refactoring = new ProcessorBasedRefactoring(processor);
			
			RefactoringWizard wizard= new ReorgMoveWizard(processor, refactoring);
			Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			wizard.setContainer(new RefactoringWizardDialog(activeShell, wizard));
			processor.setCreateTargetQueries(new CreateTargetQueries(wizard));
			processor.setReorgQueries(new ReorgQueries(activeShell));
			
			RefactoringStatus status = performRefactoring(refactoring);
			
			System.out.println(status == null ? "null" : status.getSeverity());
			
			/*CheckConditionsOperation condOper = new CheckConditionsOperation(refactoring, CheckConditionsOperation.FINAL_CONDITIONS);
			CreateChangeOperation changeOper = new CreateChangeOperation(condOper, RefactoringStatus.WARNING);
			changeOper.run(pm);*/
			
			//The following commented code works. The problem is that it doesn't check for existing files 
			//and simply overwrites it if there is a conflict
			/*Change moveChange = new MoveCompilationUnitChange(compUnit, (IPackageFragment) newSource);
			//perform the change in a composite way, otherwise the references are not updated
			CompositeChange composite = new DynamicValidationStateChange(RefactoringCoreMessages.ReorgPolicy_move);
			MoveCuUpdateCreator creator = new MoveCuUpdateCreator(new ICompilationUnit[] {compUnit}, (IPackageFragment) newSource);
			TextChangeManager fChangeManager = creator.createChangeManager(new SubProgressMonitor(pm, 1), new RefactoringStatus());
			composite.merge(new CompositeChange(RefactoringCoreMessages.MoveRefactoring_reorganize_elements, fChangeManager.getAllChanges()));
			if (moveChange instanceof CompositeChange) {
				composite.merge(((CompositeChange) moveChange));
			} else {
				composite.add(moveChange);
			}
			composite.perform(pm);*/
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pm.done();
		}
		
	}
	
	//Copied from https://eclipse.googlesource.com/jdt/eclipse.jdt.ui/+/8029c94ba8f0cb368deb09e3b2f19467d14ad8ed/org.eclipse.jdt.ui.tests.refactoring/test%20cases/org/eclipse/jdt/ui/tests/refactoring/RefactoringTest.java
	protected final RefactoringStatus performRefactoring(Refactoring ref) {
        IUndoManager undoManager= getUndoManager();
        
        final CreateChangeOperation create= new CreateChangeOperation(
                new CheckConditionsOperation(ref, CheckConditionsOperation.ALL_CONDITIONS),
                RefactoringStatus.WARNING);
        final PerformChangeOperation perform= new PerformChangeOperation(create);
        perform.setUndoManager(undoManager, ref.getName());
        IWorkspace workspace= ResourcesPlugin.getWorkspace();
        try {
			workspace.run(perform, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Change undo= perform.getUndoChange();
			return null;
		}
        RefactoringStatus status= create.getConditionCheckingStatus();
        return status;
	}
	
	protected IUndoManager getUndoManager() {
        IUndoManager undoManager= RefactoringCore.getUndoManager();
        undoManager.flush();
        return undoManager;
}

}
