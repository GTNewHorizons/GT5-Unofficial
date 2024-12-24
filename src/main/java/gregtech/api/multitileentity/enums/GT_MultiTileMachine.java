package gregtech.api.multitileentity.enums;

import gregtech.api.enums.GTValues;

public enum GT_MultiTileMachine {

    CokeOven(0),
    Macerator(1),
    NONE(GTValues.W);

    private final int meta;

    GT_MultiTileMachine(int meta) {
        this.meta = meta;
    }

    public int getId() {
        return meta;
    }
}
