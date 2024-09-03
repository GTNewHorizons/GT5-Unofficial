package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.MTERedstoneCircuitBlock;

public class GregtechRedstoneCircuitBlock {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Circuit Block.");
        if (GTPPCore.ConfigSwitches.enableMachine_RedstoneBlocks) {
            GregtechItemList.RedstoneCircuitBlock.set(new MTERedstoneCircuitBlock(31801).getStackForm(1L));
        }
    }
}
