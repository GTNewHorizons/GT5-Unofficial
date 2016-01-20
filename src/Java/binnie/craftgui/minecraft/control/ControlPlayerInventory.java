package binnie.craftgui.minecraft.control;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.minecraft.InventoryType;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ControlPlayerInventory
  extends Control
{
  private List<ControlSlot> slots = new ArrayList();
  
  public ControlPlayerInventory(IWidget parent, boolean wide)
  {
    super(parent, (int)(parent.getSize().x() / 2.0F) - (wide ? 110 : 81), (int)parent.getSize().y() - (wide ? 54 : 76) - 12, wide ? 'Ü' : '¢', wide ? 54 : 76);
    for (int row = 0; row < 3; row++) {
      for (int column = 0; column < 9; column++)
      {
        ControlSlot slot = new ControlSlot(this, (wide ? 58 : 0) + column * 18, row * 18);
        this.slots.add(slot);
      }
    }
    if (wide) {
      for (int i1 = 0; i1 < 9; i1++)
      {
        ControlSlot slot = new ControlSlot(this, i1 % 3 * 18, i1 / 3 * 18);
        this.slots.add(slot);
      }
    } else {
      for (int i1 = 0; i1 < 9; i1++)
      {
        ControlSlot slot = new ControlSlot(this, i1 * 18, 58.0F);
        this.slots.add(slot);
      }
    }
    create();
  }
  
  public ControlPlayerInventory(IWidget parent)
  {
    this(parent, false);
  }
  
  public ControlPlayerInventory(IWidget parent, int x, int y)
  {
    super(parent, x, y, 54.0F, 220.0F);
    for (int row = 0; row < 6; row++) {
      for (int column = 0; column < 6; column++)
      {
        ControlSlot slot = new ControlSlot(this, column * 18, row * 18);
        this.slots.add(slot);
      }
    }
    create();
  }
  
  public void create()
  {
    for (int row = 0; row < 3; row++) {
      for (int column = 0; column < 9; column++)
      {
        ControlSlot slot = (ControlSlot)this.slots.get(column + row * 9);
        slot.assign(InventoryType.Player, 9 + column + row * 9);
      }
    }
    for (int i1 = 0; i1 < 9; i1++)
    {
      ControlSlot slot = (ControlSlot)this.slots.get(27 + i1);
      slot.assign(InventoryType.Player, i1);
    }
  }
  
  public void addItem(ItemStack item)
  {
    if (item == null) {
      return;
    }
    for (ControlSlot slot : this.slots) {
      if (!slot.slot.getHasStack())
      {
        slot.slot.putStack(item);
        return;
      }
    }
  }
  
  public void addInventory(IInventory inventory)
  {
    for (int i = 0; i < inventory.getSizeInventory(); i++) {
      addItem(inventory.getStackInSlot(i));
    }
  }
  
  public ControlSlot getSlot(int i)
  {
    if ((i < 0) || (i >= this.slots.size())) {
      return null;
    }
    return (ControlSlot)this.slots.get(i);
  }
  
  public void onUpdateClient() {}
}
