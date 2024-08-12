package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.GT_MetaTileEntity_RedstoneLamp;

public class GregtechRedstoneLamp {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Lamp.");
        if (CORE.ConfigSwitches.enableMachine_RedstoneBlocks) {
            GregtechItemList.RedstoneLamp.set(new GT_MetaTileEntity_RedstoneLamp(31803).getStackForm(1L));
        }
    }
}
