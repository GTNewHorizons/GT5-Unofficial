package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Amazon_Warehouse_Controller;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.misc.MTEAmazonPackagerLegacy;

public class GregtechAmazonWarehouse {

    public static void run() {
        run1();
    }

    private static void run1() {
        // Amazon packager multiblock
        GregtechItemList.Amazon_Warehouse_Controller.set(
            new MTEAmazonPackagerLegacy(
                Amazon_Warehouse_Controller.ID,
                "amazonprime.controller.tier.single",
                "Amazon Warehousing Depot").getStackForm(1L));
    }
}
