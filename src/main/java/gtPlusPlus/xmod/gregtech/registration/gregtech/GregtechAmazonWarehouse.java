package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.misc.GMTE_AmazonPackager;

import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Amazon_Warehouse_Controller;

public class GregtechAmazonWarehouse {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Amazon Warehouse Multiblock.");
        run1();
    }

    private static void run1() {
        // Amazon packager multiblock
        GregtechItemList.Amazon_Warehouse_Controller.set(
            new GMTE_AmazonPackager(Amazon_Warehouse_Controller.ID, "amazonprime.controller.tier.single", "Amazon Warehousing Depot.")
                .getStackForm(1L));
    }
}
