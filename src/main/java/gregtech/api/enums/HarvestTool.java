package gregtech.api.enums;

public enum HarvestTool {

    // spotless:off
    WrenchLevel0    ((byte)  0, 0,  "wrench"),
    WrenchLevel1    ((byte)  1, 1,  "wrench"),
    WrenchLevel2    ((byte)  2, 2,  "wrench"),
    WrenchLevel3    ((byte)  3, 3,  "wrench"),
    WrenchPipeLevel0((byte)  4, 0,  "wrench"),
    WrenchPipeLevel1((byte)  5, 1,  "wrench"),
    WrenchPipeLevel2((byte)  6, 2,  "wrench"),
    WrenchPipeLevel3((byte)  7, 3,  "wrench"),
    CutterLevel0    ((byte)  8, 0,  "cutter"),
    CutterLevel1    ((byte)  9, 1,  "cutter"),
    CutterLevel2    ((byte) 10, 2,  "cutter"),
    CutterLevel3    ((byte) 11, 3,  "cutter"),
    PickaxeLevel0   ((byte) 12, 0, "pickaxe"),
    PickaxeLevel1   ((byte) 13, 1, "pickaxe"),
    PickaxeLevel2   ((byte) 14, 2, "pickaxe"),
    PickaxeLevel3   ((byte) 15, 3, "pickaxe"),
    ;
    // spotless:on

    private final byte meta;
    private final int harvestLevel;
    private final String harvestTool;

    HarvestTool(byte meta, int harvestLevel, String harvestTool) {
        this.meta = meta;
        this.harvestLevel = harvestLevel;
        this.harvestTool = harvestTool;
    }

    public static HarvestTool fromMeta(int meta) {
        // If the enum grows more, replace this with a static lookup table.
        return switch (meta) {
            // spotless:off
            case  0 -> WrenchLevel0;
            case  1 -> WrenchLevel1;
            case  2 -> WrenchLevel2;
            case  3 -> WrenchLevel3;
            case  4 -> WrenchPipeLevel0;
            case  5 -> WrenchPipeLevel1;
            case  6 -> WrenchPipeLevel2;
            case  7 -> WrenchPipeLevel3;
            case  8 -> CutterLevel0;
            case  9 -> CutterLevel1;
            case 10 -> CutterLevel2;
            case 11 -> CutterLevel3;
            case 12 -> PickaxeLevel0;
            case 13 -> PickaxeLevel1;
            case 14 -> PickaxeLevel2;
            case 15 -> PickaxeLevel3;
            default -> throw new IllegalArgumentException("Unknown tool meta: " + meta);
            // spotless:on
        };
    }

    public int getHarvestLevel() {
        return harvestLevel;
    }

    public String getHarvestTool() {
        return harvestTool;
    }

    public byte toTileEntityBaseType() {
        return meta;
    }
}
