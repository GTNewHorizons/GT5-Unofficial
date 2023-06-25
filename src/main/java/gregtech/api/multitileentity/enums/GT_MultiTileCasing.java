package gregtech.api.multitileentity.enums;

import gregtech.api.enums.GT_Values;

public enum GT_MultiTileCasing {

    CokeOven(0),
    Chemical(1),
    Distillation(2),
    LaserEngraver(4),
    Mirror(5),
    BlackLaserEngraverCasing(6),
    LaserEngraverUpgrade1(7),
    LaserEngraverUpgrade2(8),
    LaserEngraverUpgrade3(9),
    LaserEngraverUpgrade4(10),
    NONE(GT_Values.W);

    private final int meta;

    GT_MultiTileCasing(int meta) {
        this.meta = meta;
    }

    public int getId() {
        return meta;
    }
}
