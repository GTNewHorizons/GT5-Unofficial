package gregtech.common.tileentities.casings.upgrade;

import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.UpgradableModularMuTE;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;

public class Heater extends UpgradeCasing {

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.modular.heater";
    }

    @Override
    protected void customWork(IMultiBlockController target) {
        if (target instanceof UpgradableModularMuTE upgradable) {
            upgradable.increaseMucCount("heater", this.tier);
        }
    }
}
