package modularizator;

import java.util.HashMap;

import logic.Cluster;
import logic.Network;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jgrapht.graph.DefaultEdge;

public class NetworkReader {

	private final IJavaProject javaProject;

	private Network network = new Network(DefaultEdge.class, new HashMap<Object, Cluster>());

	public NetworkReader(IJavaProject javaProject) {
		this.javaProject = javaProject;
	}

	public Network read() {
		readClusters();
		return network;
	}

	private void readClusters() {
		try {
			IPackageFragment[] packageFragments = javaProject
					.getPackageFragments();
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
			addEdges(compUnit);
			network.add(compUnit, packageFrg);
		}
	}

	private void addEdges(ICompilationUnit source) throws JavaModelException {
		network.addVertex(source);
		IImportDeclaration[] imports = source.getImports();
		for (IImportDeclaration impDec : imports) {
			if (!impDec.isOnDemand())
				continue;
			String elemName = impDec.getElementName();
			String className = elemName.substring(elemName.lastIndexOf("."));
			if (className.equals("*")) {
				// TODO: Deal with the * imports
				continue;
			}
			IType iType = javaProject.findType(elemName);
			if (iType == null)
				continue; //TODO: sometimes it gets nulls, but should it??
			org.eclipse.jdt.core.ICompilationUnit target = iType.getCompilationUnit();

			network.addVertex(target); // The vertex must be in the graph before
										// its edge can be added
			network.addEdge(source, target);
		}

	}

}
