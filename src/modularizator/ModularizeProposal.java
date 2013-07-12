package modularizator;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class ModularizeProposal implements IJavaCompletionProposal {

	private final IPackageFragment suggestedPackage;
	
	private static final int relevance = 5;
	
	public ModularizeProposal(IPackageFragment suggestedPackage) {
		this.suggestedPackage = suggestedPackage;
	}
	
	
	@Override
	public int getRelevance() {
		return relevance;
	}

	@Override
	public void apply(IDocument document) {
		// TODO Auto-generated method stub
		MessageDialog.openInformation(null, "Changes were applied", "Changes were applied");
		
		
	}

	@Override
	public Point getSelection(IDocument document) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAdditionalProposalInfo() {
		String info = "Yeah!";
		return info;
	}

	@Override
	public String getDisplayString() {
		String display = "Move to package " + suggestedPackage.getElementName();
		return display;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformation getContextInformation() {
		// TODO Auto-generated method stub
		return null;
	}

}
