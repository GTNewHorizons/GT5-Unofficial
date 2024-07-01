package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.GT_MetaTileEntity_RedstoneButtonPanel;

public class GregtechRedstoneButtonPanel {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Button Panel.");
        if (CORE.ConfigSwitches.enableMachine_RedstoneBlocks) {
            GregtechItemList.RedstoneButtonPanel.set(new GT_MetaTileEntity_RedstoneButtonPanel(31800).getStackForm(1L));
        }
    }
}
