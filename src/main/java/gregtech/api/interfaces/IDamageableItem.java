package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

public interface IDamageableItem {

    boolean doDamageToItem(ItemStack aStack, int aVanillaDamage);
}
