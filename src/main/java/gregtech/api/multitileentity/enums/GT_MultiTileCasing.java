package gregtech.api.multitileentity.enums;

import gregtech.api.enums.GTValues;

public enum GT_MultiTileCasing {

    CokeOven(0),
    Chemical(1),
    Distillation(2),
    Macerator(18000),
    LaserEngraver(4),
    Mirror(5),
    BlackLaserEngraverCasing(6),
    LaserEngraverUpgrade1(7),
    LaserEngraverUpgrade2(8),
    LaserEngraverUpgrade3(9),
    LaserEngraverUpgrade4(10),
    NONE(GTValues.W);

    private final int meta;

    GT_MultiTileCasing(int meta) {
        this.meta = meta;
    }

    public int getId() {
        return meta;
    }
}
