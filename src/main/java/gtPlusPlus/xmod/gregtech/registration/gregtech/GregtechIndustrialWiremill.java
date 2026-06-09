package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_WireFactory;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialWireMillLegacy;

public class GregtechIndustrialWiremill {

    public static void run() {
        GregtechItemList.Industrial_WireFactory.set(
            new MTEIndustrialWireMillLegacy(
                Industrial_WireFactory.ID,
                "industrialwiremill.controller.tier.single",
                "Industrial Wire Factory").getStackForm(1L));
    }
}
