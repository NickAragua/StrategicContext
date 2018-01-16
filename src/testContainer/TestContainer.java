package testContainer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import strategicMap.Board;
import strategicMapUI.BoardState;
import strategicMapUI.BoardPanel;

public class TestContainer {
    public static void main(String[] args) {

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }
    
    public static void createAndShowGUI()
    {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.getContentPane().setLayout(new GridBagLayout());
    
        BoardState boardState = new BoardState(new Board(10, 10));
        BoardPanel boardPanel = new BoardPanel(boardState);
        
        //boardPanel.setPreferredSize(new Dimension(824, 768));
        //boardPanel.setFocusable(true);
        //boardPanel.setFocusCycleRoot(true);
        
        GridBagConstraints boardPaneConstraints = new GridBagConstraints();
        boardPaneConstraints.gridx = GridBagConstraints.RELATIVE;
        boardPaneConstraints.gridy = 0;
        boardPaneConstraints.gridheight = GridBagConstraints.REMAINDER;
        boardPaneConstraints.gridwidth = GridBagConstraints.REMAINDER;
        boardPaneConstraints.weightx = .9;
        boardPaneConstraints.weighty = 1;
        boardPaneConstraints.fill = GridBagConstraints.BOTH;
        frame.getContentPane().add(boardPanel, boardPaneConstraints);
        
        boardPanel.addMouseWheelListener(boardPanel);
        boardPanel.addMouseListener(boardPanel);
        boardPanel.addMouseMotionListener(boardPanel);
        //boardPanel.addKeyListener(boardPanel);
        //infoPane.addKeyListener(boardPanel);
        /*for(Component c : frame.getContentPane().getComponents()) {
            c.addKeyListener(boardPanel);
        }
        frame.addKeyListener(boardPanel);*/
        
        frame.pack();
        frame.setVisible(true);
    }
}
