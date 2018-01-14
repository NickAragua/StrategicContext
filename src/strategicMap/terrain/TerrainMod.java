package strategicMap.terrain;

import strategicMap.HexDirection;

public abstract class TerrainMod {
    public static final int WOODS = 1;
    public static final int JUNGLE = 2;
    public static final int SWAMP = 3;
    public static final int ROAD = 4;
    public static final int RIVER = 5;
    public static final int SUBURBAN = 6;
    public static final int URBAN = 7;
    public static final int LAVA = 8;
    public static final int CLIFFS = 9;
    public static final int CANYON = 10;

    public static int getEnterCostMod() {
        return 0;
    }

    public static int getEnterCostMod(HexDirection direction) {
        return getEnterCostMod();
    }

}
