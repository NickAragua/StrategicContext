package strategicMapUI;
import java.awt.Color;

import strategicMap.Board;
import strategicMap.Coords;
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
    
    public boolean hasSelectedHex() {
        return selectedCoords != null;
    }
}
