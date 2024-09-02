package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.TreeFarmer_Structural;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTETreeFarmerStructural;

public class GregtechTreeFarmerTE {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Tree Farmer Structural Block.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_TreeFarmer) {
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.TreeFarmer_Structural.set(
            new MTETreeFarmerStructural(TreeFarmer_Structural.ID, "treefarmer.structural", "Farm Keeper", 0)
                .getStackForm(1L));
    }
}
