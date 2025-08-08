package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_TreeFarm;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTETreeFarm;

public class GregtechIndustrialTreeFarm {

    public static void run() {

        Logger.INFO("Gregtech5u Content | Registering Tree Farm Multiblock.");
        run1();

    }

    private static void run1() {
        GregtechItemList.Industrial_TreeFarm.set(
            new MTETreeFarm(Industrial_TreeFarm.ID, "treefarm.controller.tier.single", "Tree Growth Simulator")
                .getStackForm(1L));
    }
}
