package gtPlusPlus.xmod.gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface GregtechItemContainer {
	public ItemStack get(long aAmount, Object... aReplacements);

	public ItemStack getAlmostBroken(long aAmount, Object... aReplacements);

	public Block getBlock();

	public Item getItem();

	public ItemStack getUndamaged(long aAmount, Object... aReplacements);

	public ItemStack getWildcard(long aAmount, Object... aReplacements);

	public ItemStack getWithCharge(long aAmount, int aEnergy, Object... aReplacements);

	public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements);

	public ItemStack getWithName(long aAmount, String aDisplayName, Object... aReplacements);

	public boolean hasBeenSet();

	public boolean isStackEqual(Object aStack);

	public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT);

	public GregtechItemContainer registerOre(Object... aOreNames);

	public GregtechItemContainer registerWildcardAsOre(Object... aOreNames);

	public GregtechItemContainer set(Item aItem);

	public GregtechItemContainer set(ItemStack aStack);
}