package modularizator.actions;

import logic.Network;

import org.eclipse.jface.action.IAction;

import windows.GraphWindow;


public class VisualizeAction extends BaseAction {
	
	@Override
	public void run(IAction action) {
		Network network = readNetwork();
		
		GraphWindow window = new GraphWindow(network);
		Thread thread = new Thread(window);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
