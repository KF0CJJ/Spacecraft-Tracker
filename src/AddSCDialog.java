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
import java.awt.event.*;
import javax.swing.*;


public class AddSCDialog extends Dialog implements ActionListener{

	protected Object result;
	protected Shell shell;
	private Text scIDBox;
	public String SC;
	private Text scNickBox;
	private Text freqBox;
	private Button addSCCancel;
	private Button addSCEnter;
	
	private double freq;
	private String nick;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	
	
	public AddSCDialog(Shell parent, int style) {
		super(parent, style);
		setText("Create new S/C");
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
		shell.setSize(300, 209);
		shell.setLayout(new FormLayout());
		
		scIDBox = new Text(shell, SWT.BORDER);
		scIDBox.setToolTipText("Enter the ID that JPL Horizons will use to find this SC");
		scIDBox.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		scIDBox.setForeground(SWTResourceManager.getColor(0, 255, 0));
		scIDBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_scIDBox = new FormData();
		fd_scIDBox.left = new FormAttachment(0, 69);
		fd_scIDBox.right = new FormAttachment(100, -65);
		fd_scIDBox.top = new FormAttachment(0, 10);
		scIDBox.setLayoutData(fd_scIDBox);
		
		Label addSCLabel = new Label(shell, SWT.NONE);
		addSCLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		addSCLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		addSCLabel.setAlignment(SWT.CENTER);
		FormData fd_addSCLabel = new FormData();
		fd_addSCLabel.left = new FormAttachment(scIDBox, -150);
		fd_addSCLabel.right = new FormAttachment(scIDBox, 0, SWT.RIGHT);
		fd_addSCLabel.top = new FormAttachment(scIDBox, 6);
		addSCLabel.setLayoutData(fd_addSCLabel);
		addSCLabel.setText("Type S/C Name or ID");
		
		addSCEnter = new Button(shell, SWT.NONE);
		addSCEnter.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		addSCEnter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				
				
			}
		});
		FormData fd_addSCEnter = new FormData();
		fd_addSCEnter.left = new FormAttachment(0, 10);
		addSCEnter.setLayoutData(fd_addSCEnter);
		addSCEnter.setText("Enter");
		
		addSCCancel = new Button(shell, SWT.NONE);
		fd_scIDBox.bottom = new FormAttachment(addSCCancel, -101);
		fd_addSCEnter.top = new FormAttachment(addSCCancel, 0, SWT.TOP);
		addSCCancel.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		FormData fd_addSCCancel = new FormData();
		fd_addSCCancel.bottom = new FormAttachment(100, -10);
		fd_addSCCancel.right = new FormAttachment(100, -10);
		addSCCancel.setLayoutData(fd_addSCCancel);
		addSCCancel.setText("Cancel");
		
		scNickBox = new Text(shell, SWT.BORDER);
		scNickBox.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		scNickBox.setToolTipText("Type a nickname that you will use to find this S/C");
		FormData fd_scNickBox = new FormData();
		fd_scNickBox.bottom = new FormAttachment(addSCLabel, 31, SWT.BOTTOM);
		fd_scNickBox.top = new FormAttachment(addSCLabel, 6);
		fd_scNickBox.left = new FormAttachment(scIDBox, 0, SWT.LEFT);
		fd_scNickBox.right = new FormAttachment(100, -65);
		scNickBox.setLayoutData(fd_scNickBox);
		
		Label lblTypeScNickname = new Label(shell, SWT.NONE);
		lblTypeScNickname.setText("Type S/C Nickname");
		lblTypeScNickname.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		lblTypeScNickname.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblTypeScNickname.setAlignment(SWT.CENTER);
		FormData fd_lblTypeScNickname = new FormData();
		fd_lblTypeScNickname.top = new FormAttachment(scNickBox, 6);
		fd_lblTypeScNickname.right = new FormAttachment(scIDBox, 0, SWT.RIGHT);
		fd_lblTypeScNickname.left = new FormAttachment(scIDBox, 0, SWT.LEFT);
		lblTypeScNickname.setLayoutData(fd_lblTypeScNickname);
		
		freqBox = new Text(shell, SWT.BORDER);
		fd_lblTypeScNickname.bottom = new FormAttachment(freqBox, -6);
		freqBox.setToolTipText("Type the frequency that will be used in doppler calculations");
		freqBox.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		freqBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		freqBox.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		FormData fd_freqBox = new FormData();
		fd_freqBox.left = new FormAttachment(scIDBox, 0, SWT.LEFT);
		fd_freqBox.right = new FormAttachment(addSCCancel, -7);
		fd_freqBox.top = new FormAttachment(0, 114);
		freqBox.setLayoutData(fd_freqBox);
		
		Label lblTypeScFrequency = new Label(shell, SWT.NONE);
		fd_freqBox.bottom = new FormAttachment(lblTypeScFrequency, 2);
		lblTypeScFrequency.setText("Type S/C Frequency");
		lblTypeScFrequency.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		lblTypeScFrequency.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblTypeScFrequency.setAlignment(SWT.CENTER);
		FormData fd_lblTypeScFrequency = new FormData();
		fd_lblTypeScFrequency.top = new FormAttachment(0, 141);
		fd_lblTypeScFrequency.right = new FormAttachment(scIDBox, 0, SWT.RIGHT);
		fd_lblTypeScFrequency.left = new FormAttachment(scIDBox, 0, SWT.LEFT);
		lblTypeScFrequency.setLayoutData(fd_lblTypeScFrequency);
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==addSCEnter) {
			SC = scIDBox.getText();
			freq = Double.parseDouble(freqBox.getText());
			nick = scNickBox.getText();
			
			
		}
	}
	public String getSC() {
		return SC;
	}
	public double getFreq() {
		return freq;
	}
	public String getNick() {
		return nick;
	}
}
