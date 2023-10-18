package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredChest;

public class GregtechSuperChests {

    public static void run() {
        int mId = 946;

        String aSuffix = " [Disabled]";

        GregtechItemList.Super_Chest_LV.set(
                (new GT_MetaTileEntity_TieredChest(mId++, "super.chest.gtpp.tier.01", "Super Chest I" + aSuffix, 1))
                        .getStackForm(1L));
        GregtechItemList.Super_Chest_MV.set(
                (new GT_MetaTileEntity_TieredChest(mId++, "super.chest.gtpp.tier.02", "Super Chest II" + aSuffix, 2))
                        .getStackForm(1L));
        GregtechItemList.Super_Chest_HV.set(
                (new GT_MetaTileEntity_TieredChest(mId++, "super.chest.gtpp.tier.03", "Super Chest III" + aSuffix, 3))
                        .getStackForm(1L));
        GregtechItemList.Super_Chest_EV.set(
                (new GT_MetaTileEntity_TieredChest(mId++, "super.chest.gtpp.tier.04", "Super Chest IV" + aSuffix, 4))
                        .getStackForm(1L));
        GregtechItemList.Super_Chest_IV.set(
                (new GT_MetaTileEntity_TieredChest(mId++, "super.chest.gtpp.tier.05", "Super Chest V" + aSuffix, 5))
                        .getStackForm(1L));

    }
}
