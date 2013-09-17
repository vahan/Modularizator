package modularizator.logic;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.internal.ui.actions.WorkbenchRunnableAdapter;
import org.eclipse.jdt.internal.ui.wizards.NewPackageCreationWizard;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Represents clusters of a network
 * @author vahan
 *
 */
@SuppressWarnings("restriction")
public class Cluster implements Cloneable {
	/**
	 * Static counter to make unique IDs for each cluster
	 */
	private static int counter = 0;
	/**
	 * All so far created clusters stored to be easily accessible by IDs
	 */
	private static HashMap<Integer, Cluster> clusters = new HashMap<Integer, Cluster>();
	/**
	 * Unique ID
	 */
	private final int id;
	/**
	 * The Java source element that is represented by the cluster. Usually a package
	 */
	private final IJavaElement sourceElement;
	/**
	 * Vertices contained in this cluster
	 */
	private final HashSet<ICompilationUnit> vertices = new HashSet<ICompilationUnit>();

	/**
	 * Constructor
	 * Creates a new cluster on an existing package
	 * @param elem
	 */
	public Cluster(IJavaElement elem) {
		id = counter;
		this.sourceElement = elem;
		clusters.put(id, this);
		counter++;
	}
	
	/**
	 * Creates a new cluster on a new package
	 * @param name
	 */
	public Cluster(IJavaProject project, String path) {
		this(createPackage(project, path));
	}
	
	private static IPackageFragment createPackage(IJavaProject project, String path) {
		//TODO: create the new folder and return it as IPackageFragment
		// Use JavaCore.create() maybe
		NewPackageCreationWizard wizard = new NewPackageCreationWizard();
		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		wizard.setContainer(new WizardDialog(activeShell, wizard));
		
		WizardDialog dialog = new WizardDialog(activeShell, wizard);
		dialog.create();
		wizard.performFinish();
		//dialog.();
		/*IRunnableWithProgress runnable = null;
		if (rule != null)
			runnable= new WorkbenchRunnableAdapter(op, rule, true);
		else
			runnable= new WorkbenchRunnableAdapter(op, getSchedulingRule());
		try {
			dialog.run(true, true, runnable);
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return null;
	}
	
	/**
	 * Find the cluster with the given id
	 * @param id	the ID to be searched for
	 * @return		the cluster with the searched id
	 */
	public static Cluster getCluster(int id) {
		return clusters.get(id);
	}
	
	/**
	 * Returns the number of so far created clusters
	 * @return	the number of so far created clusters
	 */
	public static int getClustersCount() {
		return counter;
	}
	
	public int getId() {
		return id;
	}
	
	public IJavaElement getModel() {
		return sourceElement;
	}
	
	public HashSet<ICompilationUnit> getVertices() {
		return vertices;
	}
	
	@Override
	/**
	 * Makes a new cluster object with the same source element, but a new ID.
	 * Needed for cloning networks
	 */
	public Object clone() {
		Cluster cloned = new Cluster(sourceElement);
		
		return cloned;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sourceElement == null) ? 0 : sourceElement.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cluster other = (Cluster) obj;
		if (sourceElement == null) {
			if (other.sourceElement != null)
				return false;
		} else if (!sourceElement.equals(other.sourceElement))
			return false;
		return true;
	}
	

}
