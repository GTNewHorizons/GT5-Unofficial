package gtPlusPlus.core.handler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.IFuelHandler;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BurnableFuelHandler implements IFuelHandler {

    @Override
    public int getBurnTime(ItemStack aStack) {
        // Iterate over my burnables.
        for (Pair<Integer, ItemStack> temp : GTPPCore.burnables) {
            int aStackID = Item.getIdFromItem(aStack.getItem());
            int burnID = Item.getIdFromItem(
                temp.getValue()
                    .getItem());
            if (aStackID == burnID) {
                int burn = temp.getKey();
                ItemStack fuel = temp.getValue();
                ItemStack testItem = ItemUtils.getSimpleStack(fuel, aStack.stackSize);

                if (aStack.isItemEqual(testItem)) {
                    return burn;
                }
            }
        }

        // If it's not my fuel, return 0.
        return 0;
    }
}
