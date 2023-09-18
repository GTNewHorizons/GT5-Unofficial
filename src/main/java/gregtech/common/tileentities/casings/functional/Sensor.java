package gregtech.common.tileentities.casings.functional;

import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;

public class Sensor extends FunctionalCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.functional.sensor";
    }

    @Override
    public float getPartWeight() {
        return 7f;
    }
}
