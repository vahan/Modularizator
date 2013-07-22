package modularizator.views;

import logic.Modularizator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

public class PropertiesView extends ViewPart {
	private Label labelT;
	private Label labelNSteps;
	private Text textT;
	private Text textNSteps;

	public PropertiesView() {
		super();
	}

	public void setFocus() {
		labelT.setFocus();
	}

	public void createPartControl(Composite parent) {
		GridLayout grid = new GridLayout(2, true);
		parent.setLayout(grid);
		labelT = new Label(parent, 0);
		labelNSteps = new Label(parent, 0);
		labelT.setText("T");
		labelNSteps.setText("Number of Steps");
		textT = new Text(parent, SWT.BORDER_SOLID);
		textT.setText(Double.toString(Modularizator.getInstance().getT()));
		ModifyListener listenerT = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text source = (Text) e.getSource();
				double val = Double.parseDouble(source.getText().trim());
				Modularizator.getInstance().setT(val);
			}
		};
		textT.addModifyListener(listenerT);
		textNSteps = new Text(parent, SWT.BORDER_SOLID);
		textNSteps.setText(Integer.toString(Modularizator.getInstance().getNSteps()));
		ModifyListener listenerNSteps = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text source = (Text) e.getSource();
				int val = Integer.parseInt(source.getText().trim());
				Modularizator.getInstance().setNSteps(val);
			}
		};
		textNSteps.addModifyListener(listenerNSteps);
	}

}
