/**
 * 
 */
package bbgard.wol;

import java.util.Scanner;

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
		// Show the UI
		theWindow = new MainWindow(this);
		theWindow.open();
		
	}

	/*
	 * Calls wakeDevice function in Model
	 */
	public String wakeDevice(String macAddr) {
		return theModel.sendMagicPacket(macAddr);
	}

	

	
}
