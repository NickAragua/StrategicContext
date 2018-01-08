package strategicMap;

public class Coords {
    private int x;
    private int y;
    
    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public boolean equals(Object other) {
        if(!(other instanceof Coords)) {
            return false;
        }
        
        return ((Coords) other).getX() == x &&
                ((Coords) other).getY() == y;
    }
    
    public int hashCode() {
        return x << 16 + y; // Pretty sure we won't be playing on a board bigger than 2^16
    }
}
