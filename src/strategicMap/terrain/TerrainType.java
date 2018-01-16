package strategicMap.terrain;

import strategicMap.HexDirection;
import strategicMap.StrategicMovementType;

import java.util.ArrayList;

public abstract class TerrainType {
    public static final int PLAINS = 1;
    public static final int ARCTIC = 2;
    public static final int DESERT = 3;
    public static final int HILLS = 4;
    public static final int MOUNTAINS = 5;
    public static final int WATER = 6;

    public abstract int getType();
    public abstract String getTileName();

    private ArrayList<TerrainMod> terrainMods;

    /**
     * The movement point cost to enter a hex of this terrain type,
     * given the movement type used.
     * @param movementType The movement type used to enter this hex.
     * @return int
     */
    public abstract int getEnterMoveCost(StrategicMovementType movementType);

    /**
     * @see #getEnterMoveCost(StrategicMovementType)
     * @param direction The direction from which a unit enters this hex.
     *                  Only relevant for special cases.
     */
    public int getEnterMoveCost(StrategicMovementType movementType, HexDirection direction) {
        return getEnterMoveCost(movementType);
    }

    /**
     * The movement point cost to change facing by one edge while on
     * a hex of this terrain type. Same cost as entering the hex by default.
     * @see #getEnterMoveCost(StrategicMovementType)
     * @param movementType The movement type the unit uses to change facing.
     * @return int
     */
    public int getFaceChangeMoveCost(StrategicMovementType movementType) {
        return getEnterMoveCost(movementType);
    }

    /**
     * When exiting this hex
     * @return
     */
    public int getExitMoveCostModifier() {
        return 0;
    }

    /**
     * Checks if any TerrainMods applied to this hex modify the movement point cost to
     * enter this hex.
     * @param direction The direction from which a unit is entering this hex.
     * @return
     */
    protected int getEnterCostModifiers(HexDirection direction) {
        int totalMod = 0;
        for(TerrainMod tmod : terrainMods) {
            totalMod += tmod.getEnterCostMod(direction);
        }
        return totalMod;
    }

    /**
     * Checks if any TerrainMods applied to this hex modify the movement point cost to
     * enter this hex.
     * @return
     */
    protected int getEnterCostModifiers() {
        return getEnterCostModifiers(HexDirection.ANY);
    }

}
