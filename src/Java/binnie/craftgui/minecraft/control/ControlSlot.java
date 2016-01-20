package binnie.craftgui.minecraft.control;

import binnie.core.machines.inventory.InventorySlot;
import binnie.core.machines.inventory.MachineSide;
import binnie.core.machines.inventory.SlotValidator;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITopLevelWidget;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.minecraft.ContainerCraftGUI;
import binnie.craftgui.minecraft.CustomSlot;
import binnie.craftgui.minecraft.GuiCraftGUI;
import binnie.craftgui.minecraft.InventoryType;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.WindowInventory;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ControlSlot
  extends ControlSlotBase
{
  public static Map<EnumHighlighting, List<Integer>> highlighting = new HashMap();
  public static boolean shiftClickActive = false;
  
  static
  {
    for (EnumHighlighting h : EnumHighlighting.values()) {
      highlighting.put(h, new ArrayList());
    }
  }
  
  public Slot slot = null;
  
  public ControlSlot(IWidget parent, float x, float y)
  {
    super(parent, x, y);
    
    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        if (ControlSlot.this.slot != null)
        {
          Window.get(ControlSlot.this.getWidget()).getGui();((Window)ControlSlot.this.getSuperParent()).getGui().getMinecraft().playerController.windowClick(((Window)ControlSlot.this.getSuperParent()).getContainer().windowId, ControlSlot.this.slot.slotNumber, event.getButton(), GuiCraftGUI.isShiftKeyDown() ? 1 : 0, ((Window)ControlSlot.this.getSuperParent()).getGui().getMinecraft().thePlayer);
        }
      }
    });
  }
  
  public ControlSlot(IWidget parent, int x, int y, Slot slot)
  {
    super(parent, x, y);
    this.slot = slot;
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(CraftGUITexture.Slot, IPoint.ZERO);
    if (this.slot == null) {
      return;
    }
    InventorySlot islot = getInventorySlot();
    if ((islot != null) && (islot.getValidator() != null))
    {
      IIcon icon = islot.getValidator().getIcon(!islot.getInputSides().isEmpty());
      if (icon != null) {
        CraftGUI.Render.iconItem(new IPoint(1.0F, 1.0F), icon);
      }
    }
    boolean highlighted = false;
    for (Map.Entry<EnumHighlighting, List<Integer>> highlight : highlighting.entrySet()) {
      if ((highlight.getKey() != EnumHighlighting.ShiftClick) || (shiftClickActive)) {
        if ((!highlighted) && (((List)highlight.getValue()).contains(Integer.valueOf(this.slot.slotNumber))))
        {
          highlighted = true;
          
          int c = -1442840576 + Math.min(((EnumHighlighting)highlight.getKey()).getColour(), 16777215);
          CraftGUI.Render.gradientRect(new IArea(1.0F, 1.0F, 16.0F, 16.0F), c, c);
        }
      }
    }
    if ((!highlighted) && (getSuperParent().getMousedOverWidget() == this)) {
      if ((Window.get(this).getGui().getDraggedItem() != null) && (!this.slot.isItemValid(Window.get(this).getGui().getDraggedItem()))) {
        CraftGUI.Render.gradientRect(new IArea(1.0F, 1.0F, 16.0F, 16.0F), -1426089575, -1426089575);
      } else {
        CraftGUI.Render.gradientRect(new IArea(1.0F, 1.0F, 16.0F, 16.0F), -2130706433, -2130706433);
      }
    }
  }
  
  public void onRenderOverlay()
  {
    if (this.slot == null) {
      return;
    }
    boolean highlighted = false;
    for (Map.Entry<EnumHighlighting, List<Integer>> highlight : highlighting.entrySet()) {
      if ((highlight.getKey() != EnumHighlighting.ShiftClick) || (shiftClickActive)) {
        if ((!highlighted) && (((List)highlight.getValue()).contains(Integer.valueOf(this.slot.slotNumber))))
        {
          highlighted = true;
          int c = ((EnumHighlighting)highlight.getKey()).getColour();
          IArea area = getArea();
          if (((getParent() instanceof ControlSlotArray)) || ((getParent() instanceof ControlPlayerInventory)))
          {
            area = getParent().getArea();
            area.setPosition(IPoint.ZERO.sub(getPosition()));
          }
          CraftGUI.Render.colour(c);
          CraftGUI.Render.texture(CraftGUITexture.Outline, area.outset(1));
        }
      }
    }
  }
  
  public void onUpdateClient()
  {
    super.onUpdateClient();
    if (this.slot == null) {
      return;
    }
    if ((isMouseOver()) && (GuiScreen.isShiftKeyDown()))
    {
      Window.get(this).getContainer().setMouseOverSlot(this.slot);
      shiftClickActive = true;
    }
    if (Window.get(this).getGui().isHelpMode()) {
      if (isMouseOver()) {
        for (ControlSlot slot2 : getControlSlots()) {
          if (slot2.slot != null) {
            ((List)highlighting.get(EnumHighlighting.Help)).add(Integer.valueOf(slot2.slot.slotNumber));
          }
        }
      }
    }
  }
  
  private List<ControlSlot> getControlSlots()
  {
    List<ControlSlot> slots = new ArrayList();
    if (((getParent() instanceof ControlSlotArray)) || ((getParent() instanceof ControlPlayerInventory))) {
      for (IWidget child : getParent().getWidgets()) {
        slots.add((ControlSlot)child);
      }
    } else {
      slots.add(this);
    }
    return slots;
  }
  
  public ItemStack getItemStack()
  {
    if (this.slot != null) {
      return this.slot.getStack();
    }
    return null;
  }
  
  public ControlSlot assign(int index)
  {
    return assign(InventoryType.Machine, index);
  }
  
  public ControlSlot assign(InventoryType inventory, int index)
  {
    if (this.slot != null) {
      return this;
    }
    this.slot = ((Window)getSuperParent()).getContainer().getOrCreateSlot(inventory, index);
    return this;
  }
  
  public void getHelpTooltip(Tooltip tooltip)
  {
    if (this.slot == null) {
      return;
    }
    InventorySlot slot = getInventorySlot();
    if (getInventorySlot() != null)
    {
      tooltip.add(slot.getName());
      tooltip.add("Insert Side: " + MachineSide.asString(slot.getInputSides()));
      tooltip.add("Extract Side: " + MachineSide.asString(slot.getOutputSides()));
      if (slot.isReadOnly()) {
        tooltip.add("Pickup Only Slot");
      }
      tooltip.add("Accepts: " + (slot.getValidator() == null ? "Any Item" : slot.getValidator().getTooltip()));
    }
    else if ((this.slot.inventory instanceof WindowInventory))
    {
      SlotValidator s = ((WindowInventory)this.slot.inventory).getValidator(this.slot.getSlotIndex());
      tooltip.add("Accepts: " + (s == null ? "Any Item" : s.getTooltip()));
    }
    else if ((this.slot.inventory instanceof InventoryPlayer))
    {
      tooltip.add("Player Inventory");
    }
  }
  
  public InventorySlot getInventorySlot()
  {
    return (this.slot instanceof CustomSlot) ? ((CustomSlot)this.slot).getInventorySlot() : null;
  }
}
