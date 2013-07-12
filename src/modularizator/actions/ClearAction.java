package modularizator.actions;

import org.eclipse.jface.action.IAction;

public class ClearAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		removeAllMarkers();

	}


}
