package strategicMapUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import strategicMap.Coords;
import strategicMap.Encounter;


public class BoardPanel extends JPanel implements MouseWheelListener, MouseMotionListener, ActionListener {
    private static final int HEX_X_RADIUS = 42;
    private static final int HEX_Y_RADIUS = 37;

    private float scale = 1f;
    private int xOrigin = 0;
    private int yOrigin = 0;
    
    private int postReleaseXOrigin = 0;
    private int postReleaseYOrigin = 0;
    
    Point mouseDragStartPoint = new Point(0, 0);
    Point mouseDragEndPoint = new Point(0, 0);
    
    AffineTransform postOriginTransform;
    
    Point clickedPoint;
    
    BoardState boardState;
    
    ArrayList<ArrayList<Polygon>> hexes = new ArrayList<>(); 
    
    InfoPanel infoPanel;
    EncounterWizardPanel encounterPanel;
    
    JPopupMenu rightClickMenu;
    
    /**
     * Constructor - initializes a board panel and its persistent components.
     * @param state - The object used to interact with the underlying game rules
     */
    public BoardPanel(BoardState state) {
        boardState = state;
        
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePressedHandler(e);
            }
            
            public void mouseReleased(MouseEvent e) {
                mouseReleasedHandler(e);
            }
        });
        
        this.addMouseWheelListener(this);
        this.addMouseMotionListener(this);
        this.setLayout(null);
        
        infoPanel = new InfoPanel();
        infoPanel.setPreferredSize(new Dimension(200, 200));
        infoPanel.setBounds(10, 10, 200, 200);
        infoPanel.setVisible(false);
        add(infoPanel);
        
        encounterPanel = new EncounterWizardPanel(boardState);
        encounterPanel.setPreferredSize(new Dimension (200, 200));
        encounterPanel.setBounds(10, 10, 200, 200);
        encounterPanel.setVisible(false);
        add(encounterPanel);
                
        rightClickMenu = new JPopupMenu();
        JMenuItem selectItem = new JMenuItem("Select");
        selectItem.setActionCommand("Select");
        selectItem.addActionListener(this);
        rightClickMenu.add(selectItem);
        
        JMenuItem viewItem = new JMenuItem("View");
        viewItem.setActionCommand("View");
        viewItem.addActionListener(this);
        rightClickMenu.add(viewItem);

    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2D = (Graphics2D) g;   
        AffineTransform originTransform = g2D.getTransform();
        g2D.translate(xOrigin, yOrigin + HEX_Y_RADIUS);
        g2D.scale(scale, scale);
        
        postOriginTransform = g2D.getTransform();

        drawHexes(g2D, true);
        g2D.setTransform(postOriginTransform);
        drawHexes(g2D, false);
        g2D.setTransform(postOriginTransform);
        drawForces(g2D);
        
        g2D.setTransform(originTransform);
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension((int) (boardState.getWidth() * HEX_X_RADIUS * 2 * scale), (int) (boardState.getHeight() * HEX_Y_RADIUS * 2 * scale));
    }
    
    /**
     * This method contains a dirty secret hack: 
     * @param g2D
     * @param outline
     * @param detectHex
     */
    private void drawHexes(Graphics2D g2D, boolean outline) {
        Polygon graphHex = new Polygon();
        int xRadius = HEX_X_RADIUS;
        int yRadius = HEX_Y_RADIUS;
        
        graphHex.addPoint(-xRadius/2, -yRadius);
        graphHex.addPoint(-xRadius, 0);
        graphHex.addPoint(-xRadius/2, yRadius);
        graphHex.addPoint(xRadius/2, yRadius);
        graphHex.addPoint(xRadius, 0);
        graphHex.addPoint(xRadius/2, -yRadius);

        graphHex.translate(xRadius, yRadius);
        
        for(int x = 0; x < boardState.getWidth(); x++) {            
            for(int y = boardState.getHeight() - 1; y >= 0; y--) {
                if(outline) {
                    g2D.setColor(new Color(0, 0, 0));
                    g2D.drawPolygon(graphHex);
                } else {
                    g2D.setColor(boardState.getHexColor(x, y));
                    g2D.fillPolygon(graphHex);

                    BufferedImage tile;
                    try {
                        tile = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + boardState.getHex(x,y).getTerrain().getTileName() + ".png"));
                        g2D.setClip(graphHex);
                        g2D.drawImage(tile, null, (int) graphHex.getBounds2D().getX(), (int) graphHex.getBounds2D().getY() + 1);
                        g2D.setClip(null);
                    } catch (IOException e) {}
                }
                
                g2D.drawString(x + "," + y, graphHex.xpoints[0] + (xRadius / 4), graphHex.ypoints[0] + yRadius);
                
                int[] downwardVector = getDownwardYVector();
                graphHex.translate(downwardVector[0], downwardVector[1]);
            }
            
            int[] translationVector = getRightAndUPVector(x % 2 == 0);
            graphHex.translate(translationVector[0], translationVector[1]);
        }
       
    }

    /**
     * Helper function that detects which hex was clicked
     * by basically replicating the drawHexes function as a 'dry run'.
     * @param point
     */
    private Coords detectClickedHex(Point point) {
        Polygon graphHex = new Polygon();
        int xRadius = (int) (HEX_X_RADIUS * scale);
        int yRadius = (int) (HEX_Y_RADIUS * scale);
        
        graphHex.addPoint(-xRadius/2, -yRadius);
        graphHex.addPoint(-xRadius, 0);
        graphHex.addPoint(-xRadius/2, yRadius);
        graphHex.addPoint(xRadius/2, yRadius);
        graphHex.addPoint(xRadius, 0);
        graphHex.addPoint(xRadius/2, -yRadius);

        graphHex.translate( xRadius + (int) postOriginTransform.getTranslateX(), (int) (yRadius) + (int) postOriginTransform.getTranslateY());
        AffineTransform noScaleTransform = new AffineTransform();
        noScaleTransform.setToTranslation(postOriginTransform.getTranslateX(), postOriginTransform.getTranslateY() - yRadius/2);
        Point2D transformedClickedPoint = noScaleTransform.transform((Point2D) point, null);
        
        for(int x = 0; x < boardState.getWidth(); x++) {            
            for(int y = boardState.getHeight() - 1; y >= 0; y--) {
                if(graphHex.contains(transformedClickedPoint)) {
                    int detectedY = y;
                    
                    //boardState.setSelectedHex(new Coords(x, detectedY));
                    return new Coords(x, detectedY);
                }
                
                int[] downwardVector = getDownwardYVector();
                graphHex.translate((int) (downwardVector[0] * scale), (int) (downwardVector[1] * scale));
            }
            
            int[] translationVector = getRightAndUPVector(x % 2 == 0);
            graphHex.translate((int) (translationVector[0] * scale), (int) (translationVector[1] * scale));
        }
        
        // we have not detected a clicked hex, so de-select
        //boardState.setSelectedHex(null);
        return null;
    }
    
    /**
     * Helper function that draws the forces currently on the map
     * @param g2D Graphics object on which to draw
     */
    private void drawForces(Graphics2D g2D) {
        int xRadius = HEX_X_RADIUS * 3 / 4;
        int yRadius = HEX_Y_RADIUS * 3 / 4;
        
        // make note of the current transform
        AffineTransform push = g2D.getTransform();
        
        RoundRectangle2D forceIcon = new RoundRectangle2D.Float(HEX_X_RADIUS / 2, HEX_Y_RADIUS / 2, xRadius, yRadius, HEX_X_RADIUS / 8, HEX_Y_RADIUS / 8);
        for(int x = 0; x < boardState.getWidth(); x++) {
            for(int y = boardState.getHeight() - 1; y >= 0; y--) {
                if(boardState.getForceCount(new Coords(x, y), 1) > 0) {
                    g2D.setColor(Color.BLUE);
                    g2D.fill(forceIcon);
                    g2D.setColor(Color.black);
                    g2D.draw(forceIcon);
                }
                
                int[] downwardVector = getDownwardYVector();
                g2D.translate(downwardVector[0], downwardVector[1]);
            }
            
            int[] translationVector = getRightAndUPVector(x % 2 == 0);
            g2D.translate(translationVector[0], translationVector[1]);
        }
        
        // put everything back the way it was
        g2D.setTransform(push);
    }
    
    /**
     * Returns the translation that we need to make to render the "next downward" hex.
     * @return Two dimensional array with the first element being the x vector and the second being the y vector
     */
    private int[] getDownwardYVector() {
        return new int[] { 0, (int) (HEX_Y_RADIUS * 2) };
    }
    
    /**
     * Returns the translation that we need to make to move from the bottom of a column to the top of the next
     * column to the right.
     * @param evenColumn Whether the column we're currently in is odd or even
     * @return Two dimensional array with the first element being the x vector and the second being the y vector
     */
    private int[] getRightAndUPVector(boolean evenColumn) {
        int yRadius = (int) (HEX_Y_RADIUS);
        int xRadius = (int) (HEX_X_RADIUS);
        
        int yTranslation = boardState.getHeight() * yRadius * 2;
        if(evenColumn) {
            yTranslation += yRadius;
        } else {
            yTranslation -= yRadius;
        }
        
        return new int[] {(int) (xRadius * 1.5), -yTranslation};
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scale += (float) e.getWheelRotation() / 10;
        postOriginTransform.scale(scale, scale);
        this.repaint();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        /*if(mouseDragStartPoint == null) {
            return;
        }        
        
        int dx = e.getX() - (int) mouseDragStartPoint.getX() + (int) mouseDragEndPoint.getX();
        int dy = e.getY() - (int) mouseDragStartPoint.getY() + (int) mouseDragEndPoint.getY();
        
        if(dx > 10 || dy > 10) {        
            xOrigin = dx;
            yOrigin = dy;
            this.repaint();
        }*/
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    public void mouseReleasedHandler(MouseEvent e) {
        clickedPoint = e.getPoint();
        Coords detectedHex = detectClickedHex(clickedPoint);
        
        // for now, if we haven't clicked in a valid location, don't do anything
        if(detectedHex == null) {
            return;
        }
                
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            // if we have a force or forces selected
            // have the board state give us a path object
            // and then we will draw it
        }
        else if(e.getButton() == MouseEvent.BUTTON3) {
            // dynamically set the popup menu items
            
            rightClickMenu.show(e.getComponent(), (int) clickedPoint.getX(), (int) clickedPoint.getY());
        }
        /*else if(e.getButton() == MouseEvent.BUTTON3)
        {
            if(boardState.getSelectedForces() != null &&
                    boardState.getSelectedForces().size() > 0) {
                Coords startPosition = e.isShiftDown() ? boardState.getSelectedForces().get(0).getDestination() :
                                                        boardState.getSelectedForces().get(0).getPosition();
                
                List<Coords> path = PathFinder.findGreedyPath(startPosition, detectClickedSquare(e));
                
                for(StrategicForce sf : boardState.getSelectedForces()) {
                    if(e.isShiftDown()) {
                        sf.addToPlannedRoute(path);
                    } else {
                        sf.setPlannedRoute(path);
                    }
                }
            }
        }
        */
        mouseDragEndPoint = e.getPoint();
        this.repaint();
    }

    public void mousePressedHandler(MouseEvent e) {
        mouseDragStartPoint = e.getPoint();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Coords detectedHex = detectClickedHex(clickedPoint);
        
        switch(e.getActionCommand()) {
        case "Select":
            boardState.setSelectedHex(detectedHex);
            showPanelsForHex(detectedHex);
            break;
        case "View":
            showPanelsForHex(detectedHex);
        }
    }
    
    private void showPanelsForHex(Coords hex) {
        infoPanel.displayInfo(boardState.getHexDetails(hex));
        infoPanel.setVisible(true);
        
        Encounter selectedEncounter = boardState.getEncounterAt(hex);
        if(selectedEncounter != null) {
            encounterPanel.setEncounter(selectedEncounter);
            encounterPanel.setVisible(!selectedEncounter.isFinalized());
        } else {
            encounterPanel.setVisible(false);
        }
    }
}
