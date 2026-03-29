package gregtech.common.gui.modularui.singleblock;

import gregtech.common.gui.modularui.singleblock.base.MTEBasicTankBaseGui;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.MTETieredTank;

public class MTETieredTankGui extends MTEBasicTankBaseGui<MTETieredTank> {

    public MTETieredTankGui(MTETieredTank machine) {
        super(machine);
    }

    @Override
    protected boolean supportsMuffler() {
        return false;
    }

    @Override
    protected boolean supportsPowerSwitch() {
        return false;
    }
}
