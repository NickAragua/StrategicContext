package strategicMapUI;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

/**
 * A simple JInternalFrame that's capable of displaying informational text.
 * @author Andrew
 *
 */
public class InfoPanel extends JInternalFrame {
    private JLabel infoLabel = new JLabel();
    
    /**
     * Constructor. Sets up a JInternalFrame that's resizable and closable but can't be mini/maximized or turned into an icon (?).
     */
    public InfoPanel() {
        super("Detailed Info", true, true, false, false);
        
        this.add(infoLabel);
    }
    
    /**
     * Displays an arbitrary piece of text on the embedded label.
     * @param text
     */
    public void displayInfo(String text) {
        infoLabel.setText(text);
        this.validate();
    }
}
