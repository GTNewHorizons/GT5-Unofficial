package gregtech.common.tileentities.machines.multi.purification;

import gregtech.api.enums.Materials;

public class PurifiedWaterHelpers {

    public static Materials getPurifiedWaterTier(int tier) {
        return switch (tier) {
            case 1 -> Materials.Grade1PurifiedWater;
            case 2 -> Materials.Grade2PurifiedWater;
            case 3 -> Materials.Grade3PurifiedWater;
            case 4 -> Materials.Grade4PurifiedWater;
            case 5 -> Materials.Grade5PurifiedWater;
            case 6 -> Materials.Grade6PurifiedWater;
            case 7 -> Materials.Grade7PurifiedWater;
            case 8 -> Materials.Grade8PurifiedWater;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        };
    }
}
