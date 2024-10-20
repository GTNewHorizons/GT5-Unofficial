package gregtech.common.pollution;

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
            case FLOWER -> PollutionRenderer.colorFoliage(oColor, x, z);
            case GRASS -> PollutionRenderer.colorGrass(oColor, x, z);
            case LEAVES -> PollutionRenderer.colorLeaves(oColor, x, z);
            case LIQUID -> PollutionRenderer.colorLiquid(oColor, x, z);
        };
    }
}
