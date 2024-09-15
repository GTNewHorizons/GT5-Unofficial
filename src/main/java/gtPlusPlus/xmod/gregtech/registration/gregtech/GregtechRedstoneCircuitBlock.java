package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.MTERedstoneCircuitBlock;

import static gregtech.api.enums.MetaTileEntityIDs.RedstoneCircuitBlock;

public class GregtechRedstoneCircuitBlock {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Circuit Block.");
        GregtechItemList.RedstoneCircuitBlock.set(new MTERedstoneCircuitBlock(RedstoneCircuitBlock.ID).getStackForm(1L));
    }
}
