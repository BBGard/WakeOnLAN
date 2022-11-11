package bbgard.wol;
/**
 * Main class to run WakeOnLAN
 * @author Benjamin Gardiner
 *
 */

public class Main {

	public static void main(String[] args) {
		// Setup the Model, View and Controller
		Model model = new Model();
		// View view = new View();
		Controller controller = new Controller(model);
		
		// Start the program
		controller.begin();
		
		// Delete this, just a commit message
	}

}
