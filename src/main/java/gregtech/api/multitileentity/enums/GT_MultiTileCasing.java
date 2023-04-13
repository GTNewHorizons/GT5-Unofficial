package gregtech.api.multitileentity.enums;

import gregtech.api.enums.GT_Values;

public enum GT_MultiTileCasing {

    CokeOven(0),
    Chemical(1),
    NONE(GT_Values.W);

    private final short meta;

    GT_MultiTileCasing(int meta) {
        this.meta = (short) meta;
    }

    public short getId() {
        return meta;
    }
}
