package gregtech.common.tileentities.casings.upgrade;

import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;

public class Laser extends UpgradeCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.functional.laser";
    }

    @Override
    protected void customWork(IMultiBlockController target) {
        target.setLaserSupport(true);
    }

    @Override
    public boolean breakBlock() {
        final IMultiBlockController controller = getTarget(false);
        if (controller != null) {
            controller.setLaserSupport(false);
        }
        return super.breakBlock();
    }
}
