package modularizator;

import logic.Cluster;
import logic.Modularizator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickAssistProcessor;
import org.eclipse.jface.dialogs.MessageDialog;

public class QuickAssistProcessor implements IQuickAssistProcessor {

	@Override
	public boolean hasAssists(IInvocationContext context) throws CoreException {
		Modularizator modularizator = Modularizator.getInstance();
		
		return modularizator.getChanges().size() > 0;
	}

	@Override
	public IJavaCompletionProposal[] getAssists(IInvocationContext context,
			IProblemLocation[] locations) throws CoreException {
		
		Modularizator modularizator = Modularizator.getInstance();
		CompilationUnit compUnit = context.getASTRoot();
		PackageDeclaration pckgUnit = compUnit.getPackage();
		
		Cluster cluster = modularizator.getChanges().get(compUnit);
		IPackageFragment suggestedPackage = (IPackageFragment) cluster.getModel();
		
		ModularizeProposal proposal = new ModularizeProposal(suggestedPackage);
		
		MessageDialog.openInformation(null, "getAssists", proposal.getDisplayString());
		
		return new IJavaCompletionProposal[] {proposal};
	}

}
