package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Infinite_Item_Chest;

import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.creative.MTEInfiniteItemHolder;

public class GregtechThreadedBuffers {

    public static void run() {
        GregtechItemList.Infinite_Item_Chest.set(
            (new MTEInfiniteItemHolder(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Infinite_Item_Chest.ID)
                    .translateKey("infinite.chest.tier.01")
                    .nameEnglish("Infinite Item Chest")
                    .tier(1)
                    .build())).getStackForm(1L));
    }
}
