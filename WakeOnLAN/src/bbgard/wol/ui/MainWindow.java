package bbgard.wol.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import bbgard.wol.Controller;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class MainWindow {

	protected Shell shlWakeOnLan;		// The Shell
	private Controller theController;	// Reference to the Controller

	private Text textIP;				// IP address String
	private Text textMAC;				// MAC address String
	private Button btnWake;				// Wake button
	private Label lblMessages;			// Used to display messages to user
	
//	private Feedback feedback;			// Feedback class that stores all error messages & feedback
	
	/**
	 * Constructor - sets the Controller reference
	 */
	public MainWindow(Controller theController) {
		this.theController = theController;
	}

	/**
	 * Open the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlWakeOnLan.open();
		shlWakeOnLan.layout();
		while (!shlWakeOnLan.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlWakeOnLan = new Shell();
		shlWakeOnLan.setMinimumSize(new Point(980, 580));
		shlWakeOnLan.setBackground(SWTResourceManager.getColor(249, 249, 249));
		shlWakeOnLan.setSize(980, 580);
		shlWakeOnLan.setText("Wake On LAN");

		Label seperatorTop = new Label(shlWakeOnLan, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_NONE);
		seperatorTop.setLocation(0, -1);
		seperatorTop.setSize(966, 3);
		seperatorTop.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		seperatorTop.setFont(SWTResourceManager.getFont("Segoe UI Black", 9, SWT.NORMAL));
		seperatorTop.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));

		Group grpLeftPanel = new Group(shlWakeOnLan, SWT.NONE);
		grpLeftPanel.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		grpLeftPanel.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		grpLeftPanel.setBackground(SWTResourceManager.getColor(238, 238, 238));
		grpLeftPanel.setBounds(0, -28, 232, 587);

		Label lblInstructions = new Label(grpLeftPanel, SWT.NONE);
		lblInstructions.setFont(SWTResourceManager.getFont("Segoe UI Semibold", 12, SWT.NORMAL));
		lblInstructions.setBackground(SWTResourceManager.getColor(238, 238, 238));
		lblInstructions.setBounds(23, 66, 188, 28);
		lblInstructions.setText("Instructions");

		Label lblEnterTheIp = new Label(grpLeftPanel, SWT.WRAP);
		lblEnterTheIp.setBackground(SWTResourceManager.getColor(238, 238, 238));
		lblEnterTheIp.setFont(SWTResourceManager.getFont("Segoe UI Light", 11, SWT.ITALIC));
		lblEnterTheIp.setBounds(23, 103, 188, 179);
		lblEnterTheIp.setText(
				"Enter the IP and MAC addresses of the device to wake. \n\nPress the \"Wake\" button to send a magic packet.");

		Label seperatorVertical = new Label(grpLeftPanel, SWT.SEPARATOR | SWT.SHADOW_NONE);
		seperatorVertical.setBounds(230, 28, 4, 540);
		seperatorVertical.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		seperatorVertical.setFont(SWTResourceManager.getFont("Segoe UI Black", 9, SWT.NORMAL));
		seperatorVertical.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));

		Group grpAddressPanel = new Group(shlWakeOnLan, SWT.NONE);
		grpAddressPanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		grpAddressPanel.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		grpAddressPanel.setBounds(232, -28, 735, 301);

		Group grpWakeDevice = new Group(grpAddressPanel, SWT.NONE);
		grpWakeDevice.setFont(SWTResourceManager.getFont("Segoe UI Semibold", 11, SWT.NORMAL));
		grpWakeDevice.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		grpWakeDevice.setBounds(48, 48, 572, 203);

		Label lblWakeDevice = new Label(grpWakeDevice, SWT.NONE);
		lblWakeDevice.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblWakeDevice.setFont(SWTResourceManager.getFont("Segoe UI Semibold", 12, SWT.NORMAL));
		lblWakeDevice.setBounds(22, 22, 238, 39);
		lblWakeDevice.setText("Wake Device");

		Label lblIp = new Label(grpWakeDevice, SWT.NONE);
		lblIp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblIp.setFont(SWTResourceManager.getFont("Segoe UI Light", 12, SWT.NORMAL));
		lblIp.setBounds(22, 75, 48, 33);
		lblIp.setText("IP:");

		Label lblMac = new Label(grpWakeDevice, SWT.NONE);
		lblMac.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMac.setText("MAC:");
		lblMac.setFont(SWTResourceManager.getFont("Segoe UI Light", 12, SWT.NORMAL));
		lblMac.setBounds(22, 134, 48, 33);

		textIP = new Text(grpWakeDevice, SWT.BORDER);
		textIP.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		textIP.setBounds(85, 68, 342, 40);

		textMAC = new Text(grpWakeDevice, SWT.BORDER);
		textMAC.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		textMAC.setBounds(84, 128, 343, 40);

		Group groupConnectionsPanel = new Group(shlWakeOnLan, SWT.NONE);
		groupConnectionsPanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		groupConnectionsPanel.setBounds(232, 264, 735, 281);

		TabFolder tabConnections = new TabFolder(groupConnectionsPanel, SWT.NONE);
		tabConnections.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tabConnections.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		tabConnections.setBounds(1, 10, 759, 268);

		TabItem tbtmRecent = new TabItem(tabConnections, SWT.NONE);
		tbtmRecent.setText("Recent");

		TabItem tbtmSaved = new TabItem(tabConnections, SWT.NONE);
		tbtmSaved.setText("Saved");

		TabItem tbtmDiscovered = new TabItem(tabConnections, SWT.NONE);
		tbtmDiscovered.setText("Discovered");

		/**
		 * Button Setup
		 */
		btnWake = new Button(grpWakeDevice, SWT.NONE);
		btnWake.setForeground(SWTResourceManager.getColor(255, 255, 255));
		btnWake.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		btnWake.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnWake.setBounds(452, 126, 96, 45);
		btnWake.setText("Wake");
		
		lblMessages = new Label(grpAddressPanel, SWT.NONE);
		lblMessages.setAlignment(SWT.RIGHT);
		lblMessages.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblMessages.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMessages.setFont(SWTResourceManager.getFont("Segoe UI Semibold", 11, SWT.ITALIC));
		lblMessages.setBounds(58, 257, 542, 34);

		// Disable button until IP and MAC have been entered?
		// btnWake.setEnabled(false);

		// Wake button
		btnWake.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// If IP and MAC are valid, ask controller to wake device
				if (isIpValid() && isMacValid()) {
					lblMessages.setText(theController.wakeDevice(textIP.getText(), textMAC.getText()));
				}
				else {
					lblMessages.setText(Feedback.INVALID_ADDRESS);
				}
			}
		}); // End Wake button
		
		// IP address field
		textIP.addVerifyListener(new VerifyListener() {
			// Verifies IP address text being entered - digits and decimals only
			@Override
			public void verifyText(VerifyEvent e) {
				String allowedCharacters = "0123456789.";
				
				// Verify that first digit is not zero
				if(textIP.getText().length() == 0 && e.keyCode == '0') {
					e.doit = false;
					lblMessages.setText(Feedback.ZERO_IP);			
				} 
				// Allow arrow keys and delete/backspace and full stop
				else if (e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT || e.keyCode == SWT.BS || e.keyCode == SWT.DEL || e.keyCode == SWT.KEYPAD_DECIMAL) {
					e.doit = true;
				} else {			
					boolean allow = false;
					for (int i = 0; i < e.text.length(); i++) {
						char c = e.text.charAt(i);
						
						// Check for allowed characters
						// TODO more complex check for max 3 digits before stop, numbers in range 0-255
						allow = allowedCharacters.indexOf(c) > -1;
						
						if(!allow) {
							lblMessages.setText(Feedback.INVALID_IP);
							break;
						}
						else {
							lblMessages.setText("");
						}
					}
					e.doit = allow;
				}
				
			}
			
		});
	}
	
	/**
	 * Checks for a valid IP address in the textIp field
	 */
	private Boolean isIpValid() {
		return true;
	}
	
	/**
	 * Checks for a valid MAC address in the textMac field
	 */
	private Boolean isMacValid() {
		return true;
	}
}
