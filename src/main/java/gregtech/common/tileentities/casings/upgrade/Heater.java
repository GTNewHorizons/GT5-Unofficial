package gregtech.common.tileentities.casings.upgrade;

import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;

public class Heater extends UpgradeCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.modular.heater";
    }

    @Override
    protected void customWork(IMultiBlockController target) {
        target.increaseMucCount("heater", this.tier);
    }

    @Override
    public boolean breakBlock() {
        final IMultiBlockController controller = getTarget(false);
        if (controller != null) {
            controller.resetMucCount();
        }
        return super.breakBlock();
    }
}
