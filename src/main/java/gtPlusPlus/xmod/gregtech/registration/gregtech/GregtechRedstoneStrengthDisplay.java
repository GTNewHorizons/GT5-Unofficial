package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.MTERedstoneStrengthDisplay;

public class GregtechRedstoneStrengthDisplay {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Strength Display.");
        if (GTPPCore.ConfigSwitches.enableMachine_RedstoneBlocks) {
            GregtechItemList.RedstoneStrengthDisplay.set(
                new MTERedstoneStrengthDisplay(
                    31804,
                    "redstone.display",
                    "Redstone Strength Display",
                    "Displays Redstone Strength").getStackForm(1L));
        }
    }
}
