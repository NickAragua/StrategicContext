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
}
