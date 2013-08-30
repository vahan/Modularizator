package modularizator.views;

import modularizator.AlgorithmTypes;
import modularizator.logic.Modularizator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
	 * Label for the algorithm type
	 */
	private Label labelAlgorithm;
	/**
	 * Combo for the algorithm type
	 */
	private Combo comboAlgorithm;
	
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
		textT.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text source = (Text) e.getSource();
				double val = Double.parseDouble(source.getText().trim());
				Modularizator.getInstance().setT(val);
			}
		});
		// nSteps
		labelNSteps = new Label(parent, 0);
		labelNSteps.setText("Number of Steps");
		textNSteps = new Text(parent, SWT.BORDER_SOLID);
		textNSteps.setText(Integer.toString(Modularizator.getInstance().getNSteps()));
		textNSteps.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text source = (Text) e.getSource();
				int val = Integer.parseInt(source.getText().trim());
				Modularizator.getInstance().setNSteps(val);
			}
		});
		// layoutSteps
		labelLayoutSteps = new Label(parent, 0);
		labelLayoutSteps.setText("Number of steps for the layout algorithm");
		textLayoutSteps = new Text(parent, SWT.BORDER_SOLID);
		textLayoutSteps.setText(Integer.toString(Modularizator.getInstance().getLayoutSteps()));
		textT.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text source = (Text) e.getSource();
				int val = Integer.parseInt(source.getText().trim());
				Modularizator.getInstance().setLayoutSteps(val);
			}
		});
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
		//Algorithm type
		labelAlgorithm = new Label(parent, 0);
		labelAlgorithm.setText("Algorithm");
		comboAlgorithm = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		final AlgorithmTypes[] algTypes = AlgorithmTypes.values();
		for (int i = 0; i < algTypes.length; ++i) { //keep the indexing
			comboAlgorithm.add(algTypes[i].toString());
			if (algTypes[i].equals(AlgorithmTypes.getDefault())) {
				comboAlgorithm.select(comboAlgorithm.getItemCount() - 1);
			}
		}
		comboAlgorithm.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				Combo source = (Combo) e.getSource();
				int sel = source.getSelectionIndex();
				Modularizator.getInstance().setActiveAlgorithmType(sel == -1 ? null : algTypes[sel]);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				widgetSelected(e);
			}
		});
	}

}
