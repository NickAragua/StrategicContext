package strategicMap;

public class Board {
    private Hex[][] hexes;
    int width;
    int height;
    
    public Board(int width, int height) {
        hexes = new Hex[width][height];
        this.width = width;
        this.height = height;
        
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                hexes[x][y] = new Hex();
                hexes[x][y].red = x * 10;
                hexes[x][y].green = y * 10;
            }
        }
    }
    
    public Hex getHex(int x, int y) {
        return hexes[x][y];
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
}
