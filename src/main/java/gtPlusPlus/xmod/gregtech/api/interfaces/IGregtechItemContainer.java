package gtPlusPlus.xmod.gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IGregtechItemContainer {

    Item getItem();

    Block getBlock();

    boolean isStackEqual(Object aStack);

    boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT);

    ItemStack get(long aAmount, Object... aReplacements);

    ItemStack getWildcard(long aAmount, Object... aReplacements);

    ItemStack getUndamaged(long aAmount, Object... aReplacements);

    ItemStack getAlmostBroken(long aAmount, Object... aReplacements);

    ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements);

    IGregtechItemContainer set(Item aItem);

    IGregtechItemContainer set(ItemStack aStack);

    IGregtechItemContainer registerOre(Object... aOreNames);

    IGregtechItemContainer registerWildcardAsOre(Object... aOreNames);

    ItemStack getWithCharge(long aAmount, int aEnergy, Object... aReplacements);

    ItemStack getWithName(long aAmount, String aDisplayName, Object... aReplacements);

    boolean hasBeenSet();
}
