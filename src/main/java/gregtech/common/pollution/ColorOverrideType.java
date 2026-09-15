package gregtech.common.pollution;

import gregtech.GTMod;

public enum ColorOverrideType {

    FLOWER,
    GRASS,
    LEAVES,
    LIQUID;

    public static ColorOverrideType fromName(String name) {
        return switch (name) {
            case "FLOWER" -> FLOWER;
            case "GRASS" -> GRASS;
            case "LEAVES" -> LEAVES;
            case "LIQUID" -> LIQUID;
            default -> throw new RuntimeException();
        };
    }

    public int getColor(int oColor, int x, int z) {
        return switch (this) {
            case FLOWER -> GTMod.clientProxy().mPollutionRenderer.colorFoliage(oColor, x, z);
            case GRASS -> GTMod.clientProxy().mPollutionRenderer.colorGrass(oColor, x, z);
            case LEAVES -> GTMod.clientProxy().mPollutionRenderer.colorLeaves(oColor, x, z);
            case LIQUID -> GTMod.clientProxy().mPollutionRenderer.colorLiquid(oColor, x, z);
        };
    }
}
