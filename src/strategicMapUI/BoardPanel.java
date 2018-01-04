package strategicMapUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import strategicMap.Coords;


public class BoardPanel extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {
    private static final int HEX_X_RADIUS = 40;
    private static final int HEX_Y_RADIUS = 40;
    
    private float scale = 1;
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
    
    public BoardPanel(BoardState state) {
        boardState = state;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2D = (Graphics2D) g;        
        g2D.translate(xOrigin, yOrigin + HEX_Y_RADIUS);
        g2D.scale(scale, scale);
        
        postOriginTransform = g2D.getTransform();

        drawHexes(g2D, false, false);
        g2D.setTransform(postOriginTransform);
        drawHexes(g2D, true, false);
        /*g2D.translate(dragTranslation.getX(), dragTranslation.getY());
        g2D.translate(SQUARE_HALF_SIZE, SQUARE_HALF_SIZE);
        AffineTransform originalTransform = g2D.getTransform();
        drawSquares(g2D, false);
        g2D.setTransform(originalTransform); // be nice, set the "cursor" back to where it started      
        drawSquares(g2D, true);
        g2D.setTransform(originalTransform);
        drawSelectedSquare(g2D);
        g2D.setTransform(originalTransform);
        drawForces(g2D);
        drawPlannedRoutes(g2D);
        drawExecutedMovements(g2D);
        drawEncounters(g2D);
        drawEncounterPaneLines(g2D);*/
        g2D.setTransform(postOriginTransform);
        
        //g2D.setTransform(realOriginalTransform);
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
    private void drawHexes(Graphics2D g2D, boolean outline, boolean detectHex) {
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
                }
                
                g2D.drawString(x + "," + y, graphHex.xpoints[0] + (xRadius / 4), graphHex.ypoints[0] + yRadius);
                graphHex.translate(0, yRadius * 2);
            }
            
            //g2D.setTransform(originalTransform);    
            int yTranslation = boardState.getHeight() * yRadius * 2;
            if(x % 2 == 0) {
                yTranslation += yRadius;
            } else {
                yTranslation -= yRadius;
            }
            
            graphHex.translate((int) (xRadius * 1.5), -yTranslation);
        }
       
    }

    /**
     * Helper function that detects which hex was clicked
     * by basically replicating the drawHexes function as a 'dry run'.
     * @param point
     */
    private void detectClickedHex(Point point) {
        Polygon graphHex = new Polygon();
        int xRadius = (int) (HEX_X_RADIUS * scale);
        int yRadius = (int) (HEX_Y_RADIUS * scale);
        
        graphHex.addPoint(-xRadius/2, -yRadius);
        graphHex.addPoint(-xRadius, 0);
        graphHex.addPoint(-xRadius/2, yRadius);
        graphHex.addPoint(xRadius/2, yRadius);
        graphHex.addPoint(xRadius, 0);
        graphHex.addPoint(xRadius/2, -yRadius);

        graphHex.translate(xRadius + (int) postOriginTransform.getTranslateX(), (int) (yRadius * 2) + (int) postOriginTransform.getTranslateY());
        AffineTransform noScaleTransform = new AffineTransform();
        noScaleTransform.setToTranslation(postOriginTransform.getTranslateX(), postOriginTransform.getTranslateY());
        Point2D transformedClickedPoint = noScaleTransform.transform((Point2D) point, null);
        
        for(int x = 0; x < boardState.getWidth(); x++) {            
            for(int y = boardState.getHeight() - 1; y >= 0; y--) {                    
                if(graphHex.contains(transformedClickedPoint)) {
                    int detectedY = y;
                    
                    boardState.setSelectedHex(new Coords(x, detectedY));
                    return;
                }
                
                graphHex.translate(0, yRadius * 2);
            }
            
            int yTranslation = boardState.getHeight() * yRadius * 2;
            if(x % 2 == 0) {
                yTranslation += yRadius;
            } else {
                yTranslation -= yRadius;
            }
            
            graphHex.translate((int) (xRadius * 1.5), -yTranslation);
        }
        
        // we have not detected a clicked hex, so de-select
        boardState.setSelectedHex(null);
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
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            clickedPoint = e.getPoint();
            detectClickedHex(clickedPoint);
            //boardState.setSelectedHex(detectClickedBoardCoords(e));
            
            /*if(boardState.getSelectedEncounter() != null) {
                int rX = (int) getRenderingX(boardState.getSelectedEncounter().getPosition().getX());
                int rY = (int) getRenderingY(boardState.getSelectedEncounter().getPosition().getY());
                
                encounterPane.setEncounter(boardState.getSelectedEncounter());
                encounterPane.setLocation(rX, rY);
                this.add(encounterPane);
                encounterPane.setVisible(true);
            } else {
                encounterPane.clearEncounter();
                this.remove(encounterPane);
            }*/
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
        
        this.repaint();*/
        mouseDragEndPoint = e.getPoint();
        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDragStartPoint = e.getPoint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Transforms a set of coordinates on the board panel into coordinates usable by the board.
     * @param localX
     * @param localY
     * @return
     */
    public Coords detectClickedBoardCoords(MouseEvent e) {
        int localX = e.getX();
        int localY = e.getY();
        
        localX += xOrigin + (HEX_X_RADIUS * 2* scale);
        localY += yOrigin + (HEX_Y_RADIUS * 2* scale);
        
        localX /= (HEX_X_RADIUS * 2 * scale);
        
        if(localX % 2 == 0) {
            localY -= HEX_Y_RADIUS * scale * 1.5;
        } else {
            localY += HEX_Y_RADIUS * scale * 1.5;
        }
        
        localY /= HEX_Y_RADIUS * 2 * scale;
        localY = boardState.getHeight() - localY + 1;

        System.out.println("Clicked" + e.getX() + ":" + e.getY() + " resolves to " + localX + ":" + localY);
        
        // now we have moved into a reference frame with 0,0 as the point in the center of the hex at (0, height)
        
        clickedPoint = new Point(localX, localY); //new Point(localX / (HEX_X_RADIUS * 2), localY / (HEX_Y_RADIUS * 2));
        
        return new Coords(localX - 1, localY);
    }
}
