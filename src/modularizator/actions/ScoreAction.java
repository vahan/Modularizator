package modularizator.actions;

import logic.Network;
import logic.Scorer;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;

public class ScoreAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		showScore();

	}

	private void showScore() {
		Network network = readNetwork();
		modularizator.initScorer(network);
		Scorer scorer = modularizator.getScorer();
		double score = scorer.getScore();
		
		MessageDialog.openInformation(shell, "Score", Double.toString(score));
	}


}
