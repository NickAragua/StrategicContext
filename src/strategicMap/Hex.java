package strategicMap;

import strategicMap.terrain.TerrainType;

public class Hex {
    public int red;
    public int green;
    private TerrainType terrain;

    public TerrainType getTerrain() {
        return terrain;
    }

    public void setTerrain(TerrainType terrain) {
        this.terrain = terrain;
    }

}
