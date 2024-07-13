package gregtech.common.tileentities.casings.upgrade;

import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;

public class Cleanroom extends UpgradeCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.functional.cleanroom";
    }

    @Override
    protected void customWork(IMultiBlockController target) {
        target.setCleanroom(true);
    }

    @Override
    public boolean onBlockBroken() {
        final IMultiBlockController controller = getTarget(false);
        if (controller != null) {
            controller.setCleanroom(false);
        }
        return super.onBlockBroken();
    }
}
