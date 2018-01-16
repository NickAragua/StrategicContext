package strategicMap;

import strategicMap.terrain.PlainsTerrain;
import strategicMap.terrain.TerrainType;

public class Hex {
    public int red;
    public int green;
    private TerrainType terrain;

    public TerrainType getTerrain() {
        if (null == terrain) {
            terrain = new PlainsTerrain();
        }
        return terrain;
    }

    public void setTerrain(TerrainType terrain) {
        this.terrain = terrain;
    }

}
