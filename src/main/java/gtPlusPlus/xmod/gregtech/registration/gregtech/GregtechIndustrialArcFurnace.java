package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Arc_Furnace;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialArcFurnace;

public class GregtechIndustrialArcFurnace {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Arc Furnace Multiblock.");
        run1();
    }

    private static void run1() {
        // Industrial Arc Furnace Multiblock
        GregtechItemList.Industrial_Arc_Furnace.set(
            new MTEIndustrialArcFurnace(
                Industrial_Arc_Furnace.ID,
                "industrialarcfurnace.controller.tier.single",
                "High Current Industrial Arc Furnace").getStackForm(1L));
    }
}
