package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.MTERedstoneButtonPanel;

public class GregtechRedstoneButtonPanel {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Button Panel.");
        if (GTPPCore.ConfigSwitches.enableMachine_RedstoneBlocks) {
            GregtechItemList.RedstoneButtonPanel.set(new MTERedstoneButtonPanel(31800).getStackForm(1L));
        }
    }
}
