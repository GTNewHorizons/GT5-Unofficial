package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.GT_MetaTileEntity_RedstoneCircuitBlock;

public class GregtechRedstoneCircuitBlock {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Circuit Block.");
        if (CORE.ConfigSwitches.enableMachine_RedstoneBlocks) {
            GregtechItemList.RedstoneCircuitBlock
                .set(new GT_MetaTileEntity_RedstoneCircuitBlock(31801).getStackForm(1L));
        }
    }
}
