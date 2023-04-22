package gregtech.common.tileentities.casings.functional;

import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;

public class RobotArm extends FunctionalCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.functional.robot.arm";
    }

    @Override
    public float getPartModifier() {
        return 1.5f;
    }
}
