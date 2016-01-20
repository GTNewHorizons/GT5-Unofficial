package binnie.core.machines.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;

public abstract interface IInventoryMachine
  extends IInventory, ISidedInventory, IValidatedInventory
{}
