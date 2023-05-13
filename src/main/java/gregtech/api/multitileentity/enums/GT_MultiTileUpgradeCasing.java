package gregtech.api.multitileentity.enums;

import gregtech.api.enums.GT_Values;

public enum GT_MultiTileUpgradeCasing {

    Cleanroom(0),
    NONE(GT_Values.W);

    private final int meta;

    GT_MultiTileUpgradeCasing(int meta) {
        this.meta = meta;
    }

    public int getId() {
        return meta;
    }
}
