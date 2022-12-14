package bbgard.wol.ui;



import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.List;


public class MainWindow {

	protected Shell shlWakeOnLan;		// The Shell
	private Controller theController;	// Reference to the Controller
	private Text textMAC;				// MAC address String
	private Button btnWake;				// Wake button
	private Label lblMessages;			// Used to display messages to user
	private List recentList;				// List of the last 5 MAC addresses
	
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
		lblEnterTheIp.setText(Feedback.INSTRUCTIONS);

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
		grpWakeDevice.setBounds(48, 48, 451, 203);

		Label lblWakeDevice = new Label(grpWakeDevice, SWT.NONE);
		lblWakeDevice.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblWakeDevice.setFont(SWTResourceManager.getFont("Segoe UI Semibold", 12, SWT.NORMAL));
		lblWakeDevice.setBounds(22, 22, 238, 39);
		lblWakeDevice.setText("Wake Device");

		Label lblMac = new Label(grpWakeDevice, SWT.NONE);
		lblMac.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMac.setText("MAC:");
		lblMac.setFont(SWTResourceManager.getFont("Segoe UI Light", 12, SWT.NORMAL));
		lblMac.setBounds(22, 75, 48, 33);

		textMAC = new Text(grpWakeDevice, SWT.BORDER);
		textMAC.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		textMAC.setBounds(83, 68, 343, 40);

		Group groupConnectionsPanel = new Group(shlWakeOnLan, SWT.NONE);
		groupConnectionsPanel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		groupConnectionsPanel.setBounds(232, 264, 735, 281);

		TabFolder tabConnections = new TabFolder(groupConnectionsPanel, SWT.NONE);
		tabConnections.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tabConnections.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		tabConnections.setBounds(1, 10, 759, 268);

		TabItem tbtmRecent = new TabItem(tabConnections, SWT.NONE);
		tbtmRecent.setText("Recent");
		
		recentList = new List(tabConnections, SWT.BORDER);
		recentList.setBounds(0, 0, 100, 100);
		tbtmRecent.setControl(recentList);

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
		btnWake.setBounds(330, 129, 96, 45);
		btnWake.setText("Wake");
		
		lblMessages = new Label(grpWakeDevice, SWT.WRAP);
		lblMessages.setBounds(23, 129, 287, 64);
		lblMessages.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblMessages.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblMessages.setFont(SWTResourceManager.getFont("Segoe UI Semibold", 10, SWT.ITALIC));

		// Disable button until IP and MAC have been entered?
		// btnWake.setEnabled(false);
		
		// TODO trigger a network scan and populate the list of discovered addresses
		//theController.getActiveMacs();
		//System.out.println("done...");


		
		// Wake button
		btnWake.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// if MAC address is valid, ask controller to wake device
				if (theController.isMacValid(textMAC.getText())) {
					lblMessages.setText(theController.wakeDevice(textMAC.getText()));
					
					// Save the MAC to recents
					addAddressToRecent(textMAC.getText());
				}
				else {
					lblMessages.setText(Feedback.INVALID_MAC);
				}
			}			
		});
	}
	
	/**
	 * Saves the input MAC address to the recent list.
	 * If the MAC address does not already exist
	 * @param macAddr
	 */
	private void addAddressToRecent(String macAddr) {
		boolean inList = false;
		
		// Loop through recent list looking for matches
		for (String address : recentList.getItems()) {
			if(address.equals(macAddr)) {
				inList = true;
			}
		}
		
		// If no matches, add to 
		//	TODO check for list length first 
		if(!inList) {
			recentList.add(macAddr);
		}
		
	}
}
