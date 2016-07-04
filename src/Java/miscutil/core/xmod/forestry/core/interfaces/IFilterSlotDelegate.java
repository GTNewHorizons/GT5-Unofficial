package miscutil.core.xmod.forestry.core.interfaces;

import net.minecraft.item.ItemStack;

public abstract interface IFilterSlotDelegate
{
  public abstract boolean canSlotAccept(int paramInt, ItemStack paramItemStack);
  
  public abstract boolean isLocked(int paramInt);
}
