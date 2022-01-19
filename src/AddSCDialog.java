import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class AddSCDialog extends Dialog implements ActionListener{

	protected Object result;
	protected Shell shell;
	private Text addSCBox;
	public String SC;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddSCDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		shell.setSize(200, 125);
		shell.setLayout(new FormLayout());
		
		addSCBox = new Text(shell, SWT.BORDER);
		addSCBox.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		addSCBox.setForeground(SWTResourceManager.getColor(0, 255, 0));
		addSCBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_addSCBox = new FormData();
		fd_addSCBox.top = new FormAttachment(0);
		fd_addSCBox.left = new FormAttachment(0, 10);
		fd_addSCBox.bottom = new FormAttachment(0, 30);
		fd_addSCBox.right = new FormAttachment(0, 174);
		addSCBox.setLayoutData(fd_addSCBox);
		
		Label addSCLabel = new Label(shell, SWT.NONE);
		addSCLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		addSCLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		addSCLabel.setAlignment(SWT.CENTER);
		FormData fd_addSCLabel = new FormData();
		fd_addSCLabel.top = new FormAttachment(addSCBox, 6);
		fd_addSCLabel.left = new FormAttachment(0, 32);
		addSCLabel.setLayoutData(fd_addSCLabel);
		addSCLabel.setText("Type S/C Name or ID");
		
		Button addSCEnter = new Button(shell, SWT.NONE);
		addSCEnter.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		addSCEnter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SC = addSCBox.getText();
				
				
			}
		});
		FormData fd_addSCEnter = new FormData();
		fd_addSCEnter.top = new FormAttachment(addSCLabel, 6);
		fd_addSCEnter.left = new FormAttachment(0, 10);
		addSCEnter.setLayoutData(fd_addSCEnter);
		addSCEnter.setText("Enter");
		
		Button addSCCancel = new Button(shell, SWT.NONE);
		addSCCancel.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		FormData fd_addSCCancel = new FormData();
		fd_addSCCancel.top = new FormAttachment(addSCLabel, 6);
		fd_addSCCancel.right = new FormAttachment(addSCBox, 0, SWT.RIGHT);
		addSCCancel.setLayoutData(fd_addSCCancel);
		addSCCancel.setText("Cancel");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
