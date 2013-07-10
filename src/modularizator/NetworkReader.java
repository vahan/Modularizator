package modularizator;

import java.util.ArrayList;

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

	private Network network = null;

	public NetworkReader(IJavaProject javaProject) {
		this.javaProject = javaProject;
	}

	public Network read() {
		readClusters();

		return network;

	}

	private void readClusters() {
		network = new Network(DefaultEdge.class, new ArrayList<Cluster>());
		try {
			IPackageFragment[] packageFragments = javaProject
					.getPackageFragments();
			for (IPackageFragment packageFrg : packageFragments) {
				if (packageFrg.getKind() != IPackageFragmentRoot.K_SOURCE)
					continue;
				ICompilationUnit[] compUnits = packageFrg.getCompilationUnits();
				Cluster cluster = readCluster(compUnits, packageFrg);
				network.addCluster(cluster);
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	private Cluster readCluster(ICompilationUnit[] compUnits, IPackageFragment packageFrg)
			throws JavaModelException {
		ArrayList<Object> cluster = new ArrayList<Object>();
		for (ICompilationUnit compUnit : compUnits) {
			addEdges(compUnit);
			cluster.add(compUnit);
		}

		return new Cluster(cluster, packageFrg);
	}

	private void addEdges(ICompilationUnit source) throws JavaModelException {
		network.addVertex(source);
		IImportDeclaration[] imports = source.getImports();
		for (IImportDeclaration impDec : imports) {
			if (!impDec.isOnDemand())
				continue;
			String elemName = impDec.getElementName();
			String className = elemName.substring(elemName.lastIndexOf("."));
			if (className == "*") {
				// TODO: Deal with the * imports
				continue;
			}
			IType iType = javaProject.findType(elemName);
			org.eclipse.jdt.core.ICompilationUnit target = iType
					.getCompilationUnit();

			network.addVertex(target); // The vertex must be in the graph before
										// its edge can be added
			network.addEdge(source, target);
		}

	}

}
