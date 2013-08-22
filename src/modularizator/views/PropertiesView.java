package modularizator.views;

import modularizator.logic.Modularizator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

/**
 * Creates a properties view for the plugin
 * @author vahan
 *
 */
public class PropertiesView extends ViewPart {
	/**
	 * Lable for T
	 */
	private Label labelT;
	/**
	 * Text entry box for T
	 */
	private Text textT;
	/**
	 * Label for nSteps
	 */
	private Label labelNSteps;
	/**
	 * Text entry box for nSteps
	 */
	private Text textNSteps;
	/**
	 * Label for layout steps
	 */
	private Label labelLayoutSteps;
	/**
	 * Text entry box for layoutSteps
	 */
	private Text textLayoutSteps;
	/**
	 * Label for output folder
	 */
	private Label labelOutputFolder;
	/**
	 * Button for opening the dialog
	 */
	private Button buttonOutputFolder;
	/**
	 * Label to show the output folder
	 */
	private Label labelShowOutputFolder;
	
	/**
	 * Constructor
	 */
	public PropertiesView() {
		super();
	}
	@Override
	public void setFocus() {
		labelT.setFocus();
	}
	@Override
	public void createPartControl(Composite parent) {
		GridLayout grid = new GridLayout(2, true);
		parent.setLayout(grid);
		// T
		labelT = new Label(parent, 0);
		labelT.setText("T");
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
		// nSteps
		labelNSteps = new Label(parent, 0);
		labelNSteps.setText("Number of Steps");
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
		// layoutSteps
		labelLayoutSteps = new Label(parent, 0);
		labelLayoutSteps.setText("Number of steps for the layout algorithm");
		textLayoutSteps = new Text(parent, SWT.BORDER_SOLID);
		textLayoutSteps.setText(Integer.toString(Modularizator.getInstance().getLayoutSteps()));
		ModifyListener listenerLayoutSteps = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text source = (Text) e.getSource();
				int val = Integer.parseInt(source.getText().trim());
				Modularizator.getInstance().setLayoutSteps(val);
			}
		};
		textT.addModifyListener(listenerLayoutSteps);
		// outputFolder
		labelOutputFolder = new Label(parent, 0);
		labelOutputFolder.setText("Output folder");
		buttonOutputFolder = new Button(parent, SWT.PUSH);
		buttonOutputFolder.setText("Browse...");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 4;
		labelShowOutputFolder = new Label(parent, SWT.BORDER);
		labelShowOutputFolder.setLayoutData(data);
		final Shell shell = this.getSite().getShell();
		buttonOutputFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog dlg = new DirectoryDialog(shell);
				dlg.setText("Output folder");
				dlg.setMessage("Select the output directory");
				String dir = dlg.open();
				if (dir != null) {
					Modularizator modularizator = Modularizator.getInstance();
					modularizator.setOutputFolder(dir);
					labelShowOutputFolder.setText(dir);
				}
			}
		});
	}

}
