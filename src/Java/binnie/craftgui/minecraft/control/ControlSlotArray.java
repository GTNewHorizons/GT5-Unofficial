package binnie.craftgui.minecraft.control;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.minecraft.InventoryType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ControlSlotArray
  extends Control
  implements Iterable<ControlSlot>
{
  private int rows;
  private int columns;
  private List<ControlSlot> slots = new ArrayList();
  
  public ControlSlotArray(IWidget parent, int x, int y, int columns, int rows)
  {
    super(parent, x, y, columns * 18, rows * 18);
    this.rows = rows;
    this.columns = columns;
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        this.slots.add(createSlot(column * 18, row * 18));
      }
    }
  }
  
  public ControlSlot createSlot(int x, int y)
  {
    return new ControlSlot(this, x, y);
  }
  
  public void setItemStacks(ItemStack[] array)
  {
    int i = 0;
    for (ItemStack item : array)
    {
      if (i >= this.slots.size()) {
        return;
      }
      ((ControlSlot)this.slots.get(i)).slot.putStack(item);
      i++;
    }
  }
  
  public ControlSlot getControlSlot(int i)
  {
    if ((i < 0) || (i >= this.slots.size())) {
      return null;
    }
    return (ControlSlot)this.slots.get(i);
  }
  
  public ControlSlotArray create(int[] index)
  {
    return create(InventoryType.Machine, index);
  }
  
  public ControlSlotArray create(InventoryType type, int[] index)
  {
    int i = 0;
    for (ControlSlot slot : this.slots) {
      slot.assign(type, index[(i++)]);
    }
    return this;
  }
  
  public Iterator<ControlSlot> iterator()
  {
    return this.slots.iterator();
  }
}
