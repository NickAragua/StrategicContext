package strategicMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Board {
    // hexes
    private Hex[][] hexes;
    // forces, sorted by team and coordinates
    private Map<Coords, Map<Integer, Set<Force>>> teamForces;
    int width;
    int height;
    
    public Board(int width, int height) {
        hexes = new Hex[width][height];
        this.width = width;
        this.height = height;
        teamForces = new HashMap<>();
        
        // just some code to place things on the board for testing
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                hexes[x][y] = new Hex();
                hexes[x][y].red = 255 - (x * 10);
                hexes[x][y].green = 255 - (y * 10);
            }
        }
        
        for(int x = 0; x < 5; x++) {
            Force force = new Force("Force " + x);
            Coords coords = new Coords(x, 5);
            addForce(1, force, coords);
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
    
    public void addForce(int team, Force force, Coords coords) {
        if(!teamForces.containsKey(coords)) {
            teamForces.put(coords, new HashMap<>());
        }
        
        if(!teamForces.get(coords).containsKey(team)) {
            teamForces.get(coords).put(team, new HashSet<>());
        }
        
        teamForces.get(coords).get(team).add(force);
    }
    
    public int getForceCount(Coords coords, int team) {
        if(teamForces.containsKey(coords) && teamForces.get(coords).containsKey(team)) {
            return teamForces.get(coords).get(team).size();
        }
        
        return 0;
    }
}
