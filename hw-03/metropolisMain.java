import javax.swing.*;
/**
 * The main class for the Metropolis application.
 *
 * This class contains the main method which serves as the entry point to launch the GUI of the Metropolis Viewer application.
 * It uses Swing to create the GUI and starts the application on the Event Dispatch Thread to ensure safety.
 */
public class metropolisMain {
    /**
     * The main method which is the entry point of the program.
     *
     * It initializes the MetropolisGUI in a thread-safe manner using SwingUtilities.invokeLater().
     */

     public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> new MetropolisGUI());
        }


}
