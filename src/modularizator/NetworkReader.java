package modularizator;

import java.util.HashMap;

import logic.Cluster;
import logic.Network;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.jgrapht.graph.DefaultEdge;

public class NetworkReader {

	private final IJavaProject javaProject;

	private Network network = new Network(DefaultEdge.class, new HashMap<ICompilationUnit, Cluster>(), "Initial structure");
	
	public NetworkReader(IJavaProject javaProject) {
		this.javaProject = javaProject;
	}

	public Network read() {
		readClusters();
		return network;
	}

	private void readClusters() {
		try {
			IPackageFragment[] packageFragments = javaProject.getPackageFragments();
			for (IPackageFragment packageFrg : packageFragments) {
				if (packageFrg.getKind() != IPackageFragmentRoot.K_SOURCE)
					continue;
				ICompilationUnit[] compUnits = packageFrg.getCompilationUnits();
				readCluster(compUnits, packageFrg);
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	private void readCluster(ICompilationUnit[] compUnits, IPackageFragment packageFrg)
			throws JavaModelException {
		for (ICompilationUnit compUnit : compUnits) {
			//addEdges(compUnit);
			addEdges(compUnit);
			network.add(compUnit, packageFrg);
		}
	}
	
	private void addEdges(final ICompilationUnit target) {
		network.addVertex(target);
		String elemName = target.getElementName();
		elemName = elemName.substring(0, elemName.lastIndexOf("."));
		System.out.println("--------" + elemName);
		SearchPattern pattern = SearchPattern.createPattern(elemName,
				IJavaSearchConstants.TYPE, IJavaSearchConstants.REFERENCES, SearchPattern.R_EXACT_MATCH);
		if (pattern == null)
			return;
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		// step3: define a result collector
		SearchRequestor requestor = new SearchRequestor() {
			public void acceptSearchMatch(SearchMatch match) {
				if (match == null)
					return;
				if (match.getElement() == null)
					return;
				IJavaElement elem = (IJavaElement) match.getElement();
				ICompilationUnit source = getCompilationUnit(elem);
				if (source == null)
					return;
				System.out.println(source.getClass());
				network.addVertex(source);
				network.addEdge(source, target);
			}
		};
		SearchEngine searchEngine = new SearchEngine();
		try {
			searchEngine.search(pattern, 
					new SearchParticipant[] { 
						SearchEngine.getDefaultSearchParticipant()
					}, 
					scope, requestor, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the compilation unit containing this element
	 * @param elem
	 * @return
	 */
	private ICompilationUnit getCompilationUnit(IJavaElement elem) {
		while (elem != null) {
			if (elem instanceof ICompilationUnit)
				return (ICompilationUnit) elem;
			elem = elem.getParent();
		}
		return null;
	}

}
