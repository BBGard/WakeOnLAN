/**
 * 
 */
package bbgard.wol;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Button;

import bbgard.wol.ui.MainWindow;

/**
 * @author Benjamin Gardiner
 *
 */
public class Controller {
	Model theModel;
	MainWindow theWindow;
	
	Scanner scanner = new Scanner(System.in);
	
	public Controller(Model model) {
		this.theModel = model;
	}

	/**
	 * Starts the program
	 */
	public void begin() {
		// TODO start a background thread to scan for MAC addresses
		// Show the UI
		theWindow = new MainWindow(this);
		theWindow.open();
		
	}
	
	/**
	 * Retrieves a list of all active MAC addresses on the network
	 */
	public List<byte[]> getActiveMacs() {
		try {
			return theModel.getActiveMacs();
		} catch (IOException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*
	 * Calls wakeDevice function in Model
	 */
	public String wakeDevice(String macAddr) {
		return theModel.sendMagicPacket(macAddr);
	}

	/**
	 * Checks for a valid MAC address 
	 */
	public Boolean isMacValid(String mac) {
		return theModel.isMacValid(mac);		
	}

	
}
