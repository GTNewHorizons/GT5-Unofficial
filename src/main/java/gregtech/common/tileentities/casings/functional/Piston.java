package gregtech.common.tileentities.casings.functional;

import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;

public class Piston extends FunctionalCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.functional.piston";
    }

    @Override
    public float getPartWeight() {
        return 2f;
    }
}
