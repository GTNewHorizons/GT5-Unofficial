package gtPlusPlus.xmod.gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IGregtechItemContainer {

    public Item getItem();

    public Block getBlock();

    public boolean isStackEqual(Object aStack);

    public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT);

    public ItemStack get(long aAmount, Object... aReplacements);

    public ItemStack getWildcard(long aAmount, Object... aReplacements);

    public ItemStack getUndamaged(long aAmount, Object... aReplacements);

    public ItemStack getAlmostBroken(long aAmount, Object... aReplacements);

    public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements);

    public IGregtechItemContainer set(Item aItem);

    public IGregtechItemContainer set(ItemStack aStack);

    public IGregtechItemContainer registerOre(Object... aOreNames);

    public IGregtechItemContainer registerWildcardAsOre(Object... aOreNames);

    public ItemStack getWithCharge(long aAmount, int aEnergy, Object... aReplacements);

    public ItemStack getWithName(long aAmount, String aDisplayName, Object... aReplacements);

    public boolean hasBeenSet();
}
