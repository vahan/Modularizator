package modularizator.actions;

import org.eclipse.jface.action.IAction;

/**
 * Used for clearing all modularizator markers
 * @author vahan
 *
 */
public class ClearAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		removeAllMarkers();

	}


}
