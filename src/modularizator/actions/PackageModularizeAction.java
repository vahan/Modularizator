package modularizator.actions;

import logic.Algorithm;
import logic.MarceloAlgorithm;
import logic.Vertex;
import logic.PackageModularizator;
import logic.Scorer;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;


/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class PackageModularizeAction implements IObjectActionDelegate {
	
	private Shell shell;
	
	private PackageModularizator modularizator;
	
	private Algorithm algorithm;
	
	private Scorer scorer;
	
	private static final int nSteps = 1000; //TODO: make user-defined
	
	/**
	 * The constructor.
	 */
	public PackageModularizeAction() {
		modularizator = new PackageModularizator(nSteps);
		algorithm = new MarceloAlgorithm(modularizator.getNetwork(), nSteps);
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		
		DirectedGraph<Vertex, DefaultEdge> optimizedGraph = algorithm.optimize(scorer);
		
		showOptimizedGraph(optimizedGraph);
		
		MessageDialog.openInformation(
			shell.getShell(),
			"Package Modularizator Tips",
			"See individual files for tips");
	}


	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	
	private void showOptimizedGraph(DirectedGraph<Vertex, DefaultEdge> graph) {
		MessageDialog.openInformation(
				shell.getShell(),
				"TODO",
				"show the graph tips on individual project files");
		//TODO: show the graph tips on individual project files
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		
	}
}