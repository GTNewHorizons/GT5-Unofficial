package gtPlusPlus.core.slots;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.FoodUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class SlotLunchBox extends SlotGtTool {

    public SlotLunchBox(final IInventory base, final int x, final int y, final int z) {
        super(base, x, y, z);
    }

    @Override
    public boolean isItemValid(final ItemStack itemstack) {
        return isItemValid_STATIC(itemstack);
    }

    public static boolean isItemValid_STATIC(final ItemStack itemstack) {
        if ((itemstack.getItem() instanceof ItemFood) || (FoodUtils.isFood(itemstack))) {
            Logger.WARNING(itemstack.getDisplayName() + " is a valid food.");
            return true;
        }
        Logger.WARNING(itemstack.getDisplayName() + " is not a valid food.");
        return false;
    }
}
