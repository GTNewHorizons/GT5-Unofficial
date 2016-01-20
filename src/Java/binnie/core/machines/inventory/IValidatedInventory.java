package binnie.core.machines.inventory;

import net.minecraft.inventory.IInventory;

abstract interface IValidatedInventory
  extends IInventory
{
  public abstract boolean isReadOnly(int paramInt);
}
