package gregtech.common.tileentities.casings.upgrade;

import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;

public class Insulator extends UpgradeCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.modular.insulator";
    }

    @Override
    protected void customWork(IMultiBlockController target) {
        target.increaseMucCount("insulator", this.tier);
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
