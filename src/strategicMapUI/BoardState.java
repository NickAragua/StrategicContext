package strategicMapUI;
import java.awt.Color;

import strategicMap.Board;
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
    
    public BoardState(Board board) {
        this.board = board;
    }
    
    public Color getHexColor(int x, int y) {
        Hex hex = board.getHex(x, y);
        
        return new Color(hex.red, hex.green, 0);
    }
    
    public int getWidth() {
        return board.getWidth();
    }
    
    public int getHeight() {
        return board.getHeight();
    }
}
