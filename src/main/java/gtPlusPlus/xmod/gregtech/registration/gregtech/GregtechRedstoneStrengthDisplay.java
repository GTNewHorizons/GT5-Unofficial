package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.RedstoneStrengthDisplay;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.MTERedstoneStrengthDisplay;

public class GregtechRedstoneStrengthDisplay {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Redstone Strength Display.");
        GregtechItemList.RedstoneStrengthDisplay.set(
            new MTERedstoneStrengthDisplay(
                RedstoneStrengthDisplay.ID,
                "redstone.display",
                "Redstone Strength Display",
                "Displays Redstone Strength").getStackForm(1L));
    }
}
