package gregtech.common.tileentities.casings.upgrade;

import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;

public class Wireless extends UpgradeCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.functional.wireless";
    }

    @Override
    protected void customWork(IMultiBlockController target) {
        target.setWirelessSupport(true);
    }

    @Override
    public boolean onBlockBroken() {
        final IMultiBlockController controller = getTarget(false);
        if (controller != null) {
            controller.setWirelessSupport(false);
        }
        return super.onBlockBroken();
    }
}
