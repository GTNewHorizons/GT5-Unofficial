package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Infinite_Item_Chest;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.creative.MTEInfiniteItemHolder;

public class GregtechThreadedBuffers {

    public static void run() {
        GregtechItemList.Infinite_Item_Chest.set(
            (new MTEInfiniteItemHolder(Infinite_Item_Chest.ID, "infinite.chest.tier.01", "Infinite Item Chest", 1))
                .getStackForm(1L));
    }
}
