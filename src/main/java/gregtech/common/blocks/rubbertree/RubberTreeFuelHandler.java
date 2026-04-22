package gregtech.common.blocks.rubbertree;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.IFuelHandler;
import gregtech.api.GregTechAPI;

public class RubberTreeFuelHandler implements IFuelHandler {

    private static final int RUBBER_SAPLING_BURN_TIME = 100; // like a vanilla sapling

    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel == null) {
            return 0;
        }

        Item rubberSaplingItem = Item.getItemFromBlock(GregTechAPI.sBlockRubberSapling);
        if (rubberSaplingItem != null && fuel.getItem() == rubberSaplingItem) {
            return RUBBER_SAPLING_BURN_TIME;
        }

        return 0;
    }
}
