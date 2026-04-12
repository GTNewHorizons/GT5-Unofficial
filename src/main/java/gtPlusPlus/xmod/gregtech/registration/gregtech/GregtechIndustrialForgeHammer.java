package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_IndustrialForgeHammer;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialForgeHammer;

public class GregtechIndustrialForgeHammer {

    public static void run() {
        run1();
    }

    private static void run1() {
        GregtechItemList.Controller_IndustrialForgeHammer.set(
            new MTEIndustrialForgeHammer(
                Controller_IndustrialForgeHammer.ID,
                "industrialhammer.controller.tier.single",
                "Industrial Sledgehammer").getStackForm(1L));
    }
}
