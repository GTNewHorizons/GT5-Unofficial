package gregtech.common.tileentities.casings.functional;

import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;

public class Pump extends FunctionalCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.functional.pump";
    }

    @Override
    public float getPartWeight() {
        return 2f;
    }
}
