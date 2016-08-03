package cofh.api.energy;

import net.minecraft.item.ItemStack;

public abstract interface IEnergyContainerItem
{
  public abstract int receiveEnergy(ItemStack paramItemStack, int paramInt, boolean paramBoolean);
  
  public abstract int extractEnergy(ItemStack paramItemStack, int paramInt, boolean paramBoolean);
  
  public abstract int getEnergyStored(ItemStack paramItemStack);
  
  public abstract int getMaxEnergyStored(ItemStack paramItemStack);
}
