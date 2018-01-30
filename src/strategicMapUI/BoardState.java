package strategicMapUI;
import java.awt.Color;

import strategicMap.Board;
import strategicMap.Coords;
import strategicMap.Encounter;
import strategicMap.Force;
import strategicMap.Hex;

/**
 * This class represents the UI-related state of the board, such as
 * which hex, units, encounters we have selected.
 * It also handles interaction between the UI and the underlying data.
 * @author NickAragua
 *
 */
public class BoardState {
    private Board board;
    private Coords selectedCoords;
    
    public BoardState(Board board) {
        this.board = board;
    }
    
    public Color getHexColor(int x, int y) {
        Hex hex = board.getHex(x, y);
        
        if(selectedCoords != null && selectedCoords.equals(new Coords(x, y))) {
            return new Color(0, 0, 200);
        }
        
        return new Color(hex.red, hex.green, 0);
    }

    public Hex getHex(int x, int y) {
        return board.getHex(x, y);
    }
    
    public int getForceCount(Coords coords, int team) {
        return board.getForceCount(coords, team);
    }
    
    public int getWidth() {
        return board.getWidth();
    }
    
    public int getHeight() {
        return board.getHeight();
    }
    
    public void setSelectedHex(Coords coords) {
        selectedCoords = coords;
    }
    
    public Coords getSelectedHex() {
        return selectedCoords;
    }
    
    /**
     * Generates an html string suitable for displaying the details of a hex in the info panel.
     * @return
     */
    public String getSelectedHexDetails() {
        StringBuilder forceBuilder = new StringBuilder();
        for(Force force : board.getForcesAt(selectedCoords)) {
            forceBuilder.append(force.getFullSummary());
            forceBuilder.append("<br/>");
        }
        
        return String.format("<html>Hex (%s, %s) selected.<br/>%s</html>", selectedCoords.getX(), selectedCoords.getY(), forceBuilder.toString());
    }
    
    public boolean hasSelectedHex() {
        return selectedCoords != null;
    }
    
    public Encounter getSelectedEncounter() {
        if(selectedCoords != null) {
            return board.getEncounterAt(selectedCoords);
        }
        
        return null;
    }
    
    public void removeEncounterChain(Encounter enc) {
        board.removeEncounter(enc);
    }
}
