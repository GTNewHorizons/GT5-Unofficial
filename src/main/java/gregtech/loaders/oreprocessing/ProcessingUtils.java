package gregtech.loaders.oreprocessing;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.NBTPersist;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ProcessingUtils {
    public static String itemStackKey(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return null;

        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        if (id == null) return null;

        String key = id + "@" + Items.feather.getDamage(stack);
        if (stack.hasTagCompound()) key += "#nbt=" + NBTPersist.toJsonObjectExact(stack.getTagCompound());
        return key;
    }
}
