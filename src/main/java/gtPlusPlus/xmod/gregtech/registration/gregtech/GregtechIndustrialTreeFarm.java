package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_TreeFarm;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm;

public class GregtechIndustrialTreeFarm {

    public static void run() {
        if (CORE.ConfigSwitches.enableMultiblock_TreeFarmer) {
            Logger.INFO("Gregtech5u Content | Registering Tree Farm Multiblock.");
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.Industrial_TreeFarm.set(
            new GregtechMetaTileEntityTreeFarm(
                Industrial_TreeFarm.ID,
                "treefarm.controller.tier.single",
                "Tree Growth Simulator").getStackForm(1L));
    }
}
