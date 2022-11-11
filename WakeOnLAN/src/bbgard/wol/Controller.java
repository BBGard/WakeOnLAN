/**
 * 
 */
package bbgard.wol;

import java.util.Scanner;

/**
 * @author Benjamin Gardiner
 *
 */
public class Controller {
	Model theModel;
	
	Scanner scanner = new Scanner(System.in);
	
	public Controller(Model model) {
		this.theModel = model;
	}

	public void begin() {
		// Starts the program
		// TODO start the ui
		System.out.println("Enter an ip address: ");
		String ipAddr = scanner.nextLine();
		
		System.out.println("Enter a MAC address: ");
		String macAddr = scanner.nextLine();
		
		System.out.println("Attempting to wake device at " + ipAddr + "MAC: " + macAddr);
		theModel.sendMagicPacket(ipAddr, macAddr);
		
	}

}
