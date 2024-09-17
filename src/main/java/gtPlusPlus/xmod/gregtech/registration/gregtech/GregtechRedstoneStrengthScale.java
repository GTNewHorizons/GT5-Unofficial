package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.RedstoneStrengthScale;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.MTERedstoneStrengthScale;

public class GregtechRedstoneStrengthScale {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Strength Scale.");
        GregtechItemList.RedstoneStrengthScale
            .set(new MTERedstoneStrengthScale(RedstoneStrengthScale.ID).getStackForm(1L));
    }
}
