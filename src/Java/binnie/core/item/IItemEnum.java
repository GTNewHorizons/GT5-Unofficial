package binnie.core.item;

import net.minecraft.item.ItemStack;

public abstract interface IItemEnum
{
  public abstract boolean isActive();
  
  public abstract String getName(ItemStack paramItemStack);
  
  public abstract int ordinal();
  
  public abstract ItemStack get(int paramInt);
}
