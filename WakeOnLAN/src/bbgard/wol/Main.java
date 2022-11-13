package bbgard.wol;

import javax.swing.SwingUtilities;
/**
 * Main class to run WakeOnLAN
 * @author Benjamin Gardiner
 *
 */

public class Main {

	public static void main(String[] args) {		
		
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {       
            	
            	try {
            		// Setup the Model, View and Controller
            		Model model = new Model();
            		// View view = new View();
            		Controller controller = new Controller(model);
            		
            		// Start the program
            		controller.begin();
        			
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
            }
        });
	}

}
