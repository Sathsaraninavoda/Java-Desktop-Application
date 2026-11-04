package thambapanni.main;

import javax.swing.SwingUtilities;
import view.LoginView;

/**
 * Thambapanni Sobha - Sri Lankan Heritage E-commerce Desktop Application
 * Main entry point for the MVC-based desktop application
 *
 * @author Ronit
 */
public class JavaFinalProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Set system look and feel
        try {
            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Fall back to default look and feel
        }

        // Start the application with login view
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
