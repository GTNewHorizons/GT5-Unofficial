package gregtech.common.tileentities.casings.functional;

import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;

public class Conveyor extends FunctionalCasing {
    
    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.functional.conveyor";
    }

    @Override
    public float getPartModifier() {
        return 1.1f;
    }
}
