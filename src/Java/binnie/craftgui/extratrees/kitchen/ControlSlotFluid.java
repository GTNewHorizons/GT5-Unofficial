package binnie.craftgui.extratrees.kitchen;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.ITopLevelWidget;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventWidget.ChangeSize;
import binnie.craftgui.events.EventWidget.ChangeSize.Handler;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ControlSlotFluid
  extends Control
  implements ITooltip
{
  ControlFluidDisplay itemDisplay;
  FluidStack fluidStack;
  
  public ControlSlotFluid(IWidget parent, int x, int y, FluidStack fluid)
  {
    this(parent, x, y, 18, fluid);
  }
  
  public ControlSlotFluid(IWidget parent, int x, int y, int size, FluidStack fluid)
  {
    super(parent, x, y, size, size);
    addAttribute(Attribute.MouseOver);
    this.itemDisplay = new ControlFluidDisplay(this, 1.0F, 1.0F, size - 2);
    this.fluidStack = fluid;
    
    addSelfEventHandler(new EventWidget.ChangeSize.Handler()
    {
      public void onEvent(EventWidget.ChangeSize event)
      {
        if (ControlSlotFluid.this.itemDisplay != null) {
          ControlSlotFluid.this.itemDisplay.setSize(ControlSlotFluid.this.getSize().sub(new IPoint(2.0F, 2.0F)));
        }
      }
    });
  }
  
  public void onRenderBackground()
  {
    int size = (int)getSize().x();
    CraftGUI.Render.texture(CraftGUITexture.Slot, getArea());
    if (getSuperParent().getMousedOverWidget() == this) {
      CraftGUI.Render.gradientRect(new IArea(new IPoint(1.0F, 1.0F), getArea().size().sub(new IPoint(2.0F, 2.0F))), -2130706433, -2130706433);
    }
  }
  
  public void onUpdateClient()
  {
    super.onUpdateClient();
    this.itemDisplay.setItemStack(getFluidStack());
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    FluidStack item = getFluidStack();
    if (item == null) {
      return;
    }
    tooltip.add(item.getFluid().getLocalizedName());
  }
  
  public FluidStack getFluidStack()
  {
    return this.fluidStack;
  }
}
