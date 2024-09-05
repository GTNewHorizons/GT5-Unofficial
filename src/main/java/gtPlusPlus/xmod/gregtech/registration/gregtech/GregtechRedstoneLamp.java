package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.MTERedstoneLamp;

public class GregtechRedstoneLamp {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Lamp.");
        if (GTPPCore.ConfigSwitches.enableMachine_RedstoneBlocks) {
            GregtechItemList.RedstoneLamp.set(new MTERedstoneLamp(31803).getStackForm(1L));
        }
    }
}
