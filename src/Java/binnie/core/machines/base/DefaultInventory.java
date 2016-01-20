package binnie.core.machines.base;

import binnie.core.machines.inventory.IInventoryMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

class DefaultInventory
  implements IInventoryMachine
{
  public int getSizeInventory()
  {
    return 0;
  }
  
  public ItemStack getStackInSlot(int i)
  {
    return null;
  }
  
  public ItemStack decrStackSize(int i, int j)
  {
    return null;
  }
  
  public ItemStack getStackInSlotOnClosing(int i)
  {
    return null;
  }
  
  public void setInventorySlotContents(int i, ItemStack itemstack) {}
  
  public int getInventoryStackLimit()
  {
    return 64;
  }
  
  public boolean isUseableByPlayer(EntityPlayer entityplayer)
  {
    return false;
  }
  
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return false;
  }
  
  public int[] getAccessibleSlotsFromSide(int var1)
  {
    return new int[0];
  }
  
  public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {
    return false;
  }
  
  public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
    return false;
  }
  
  public boolean isReadOnly(int slot)
  {
    return false;
  }
  
  public String getInventoryName()
  {
    return "";
  }
  
  public boolean hasCustomInventoryName()
  {
    return false;
  }
  
  public void markDirty() {}
  
  public void openInventory() {}
  
  public void closeInventory() {}
}
