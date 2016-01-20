package binnie.core.machines.inventory;

public abstract interface IInventorySlots
{
  public abstract InventorySlot addSlot(int paramInt, String paramString);
  
  public abstract InventorySlot[] addSlotArray(int[] paramArrayOfInt, String paramString);
  
  public abstract InventorySlot getSlot(int paramInt);
  
  public abstract InventorySlot[] getSlots(int[] paramArrayOfInt);
  
  public abstract InventorySlot[] getAllSlots();
}
