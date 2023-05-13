package gregtech.common.tileentities.casings.upgrade;

import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;

public class Tank extends UpgradeCasing {

    @Override
    protected void customWork(IMultiBlockController aTarget) {

    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.tank";
    }
}
