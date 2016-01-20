package binnie.core.triggers;

import net.minecraft.inventory.IInventory;

public class TriggerInventory
{
  private static Boolean isSlotEmpty(IInventory inventory, int slot)
  {
    return Boolean.valueOf(inventory.getStackInSlot(slot) != null);
  }
}
