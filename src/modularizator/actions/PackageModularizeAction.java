package modularizator.actions;

import logic.Algorithm;
import logic.MarceloAlgorithm;
import logic.Vertex;
import logic.PackageModularizator;
import logic.Scorer;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
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
public class PackageModularizeAction implements IWorkbenchWindowActionDelegate {
	
	private IWorkbenchWindow window;
	
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
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	@Override
	public void run(IAction action) {
		
		DirectedGraph<Vertex, DefaultEdge> optimizedGraph = algorithm.optimize(scorer);
		
		showOptimizedGraph(optimizedGraph);
		
		MessageDialog.openInformation(
			window.getShell(),
			"Package Modularizator Tips",
			"See individual files for tips");
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	@Override
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
	
	
	private void showOptimizedGraph(DirectedGraph<Vertex, DefaultEdge> graph) {
		MessageDialog.openInformation(
				window.getShell(),
				"TODO",
				"show the graph tips on individual project files");
		//TODO: show the graph tips on individual project files
	}
}