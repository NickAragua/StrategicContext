package strategicMap.terrain;

import strategicMap.HexDirection;
import strategicMap.StrategicMovementType;

public class PlainsTerrain extends TerrainType {
    private static final String type = "Plains";
    private static final int baseMoveCost = 1;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getEnterMoveCost(StrategicMovementType movementType) {
        return getEnterMoveCost(movementType, HexDirection.ANY);
    }

    @Override
    public int getEnterMoveCost(StrategicMovementType movementType, HexDirection fromDirection) {
        return baseMoveCost + getEnterCostModifiers(fromDirection);
    }
}
