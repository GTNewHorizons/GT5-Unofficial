package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.creative.GT_MetaTileEntity_InfiniteItemHolder;

import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Infinite_Item_Chest;

public class GregtechThreadedBuffers {

    public static void run() {
        GregtechItemList.Infinite_Item_Chest.set(
            (new GT_MetaTileEntity_InfiniteItemHolder(Infinite_Item_Chest.ID, "infinite.chest.tier.01", "Infinite Item Chest", 1))
                .getStackForm(1L));
    }
}
