package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.GT_MetaTileEntity_RedstoneStrengthScale;

public class GregtechRedstoneStrengthScale {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Strength Scale.");
        if (CORE.ConfigSwitches.enableMachine_RedstoneBlocks) {
            GregtechItemList.RedstoneStrengthScale
                .set(new GT_MetaTileEntity_RedstoneStrengthScale(31805).getStackForm(1L));
        }
    }
}
