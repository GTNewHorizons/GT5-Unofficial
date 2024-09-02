package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_IndustrialForgeHammer;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialForgeHammer;

public class GregtechIndustrialForgeHammer {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Forge Hammer Multiblock.");
        run1();
    }

    private static void run1() {
        GregtechItemList.Controller_IndustrialForgeHammer.set(
            new GregtechMetaTileEntity_IndustrialForgeHammer(
                Controller_IndustrialForgeHammer.ID,
                "industrialhammer.controller.tier.single",
                "Industrial Sledgehammer").getStackForm(1L));
    }
}
