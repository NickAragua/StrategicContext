package strategicMapUI;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class InfoPanel extends JInternalFrame {
    private JLabel infoLabel = new JLabel();
    
    public InfoPanel() {
        super("Detailed Info", true, true, false, false);
        
        this.add(infoLabel);
    }
    
    public void displayInfo(String text) {
        infoLabel.setText(text);
        this.validate();
    }
}
