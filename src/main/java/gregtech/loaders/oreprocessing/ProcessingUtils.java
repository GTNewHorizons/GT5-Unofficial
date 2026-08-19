package gregtech.loaders.oreprocessing;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.util.NBTPersist;

public class ProcessingUtils {

    public static String itemStackKey(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return null;

        String id = stack.getItem().delegate.name();

        String key = id + "@" + Items.feather.getDamage(stack);
        if (stack.hasTagCompound()) key += "#nbt=" + NBTPersist.toJsonObjectExact(stack.getTagCompound());
        return key;
    }
}
