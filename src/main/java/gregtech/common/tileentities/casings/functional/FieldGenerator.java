package gregtech.common.tileentities.casings.functional;

import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;

public class FieldGenerator extends FunctionalCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.functional.field.generator";
    }

    @Override
    public float getPartWeight() {
        return 10f;
    }
}
