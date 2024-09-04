package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_WireFactory;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialWireMill;

public class GregtechIndustrialWiremill {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Wire Factory Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_IndustrialWireMill) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Wire Factory Multiblock
        GregtechItemList.Industrial_WireFactory.set(
            new MTEIndustrialWireMill(
                Industrial_WireFactory.ID,
                "industrialwiremill.controller.tier.single",
                "Wire Factory Controller").getStackForm(1L));
    }
}
