package binnie.craftgui.extratrees.kitchen;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.ITopLevelWidget;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.minecraft.Window;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class ControlFluidDisplay
  extends Control
  implements ITooltip
{
  FluidStack itemStack = null;
  public boolean hastooltip = false;
  
  public void setTooltip()
  {
    this.hastooltip = true;
    addAttribute(Attribute.MouseOver);
  }
  
  public ControlFluidDisplay(IWidget parent, float f, float y)
  {
    this(parent, f, y, 16.0F);
  }
  
  public ControlFluidDisplay(IWidget parent, float f, float y, FluidStack stack, boolean tooltip)
  {
    this(parent, f, y, 16.0F);
    setItemStack(stack);
    if (tooltip) {
      setTooltip();
    }
  }
  
  public ControlFluidDisplay(IWidget parent, float x, float y, float size)
  {
    super(parent, x, y, size, size);
  }
  
  public void onRenderForeground()
  {
    if (this.itemStack == null) {
      return;
    }
    IPoint relativeToWindow = getAbsolutePosition().sub(getSuperParent().getPosition());
    if ((relativeToWindow.x() > Window.get(this).getSize().x() + 100.0F) || (relativeToWindow.y() > Window.get(this).getSize().y() + 100.0F)) {
      return;
    }
    if (this.itemStack != null)
    {
      Fluid fluid = this.itemStack.getFluid();
      
      int hex = fluid.getColor(this.itemStack);
      
      int r = (hex & 0xFF0000) >> 16;
      int g = (hex & 0xFF00) >> 8;
      int b = hex & 0xFF;
      
      IIcon icon = this.itemStack.getFluid().getIcon(this.itemStack);
      
      GL11.glColor4f(r / 255.0F, g / 255.0F, b / 255.0F, 1.0F);
      
      GL11.glEnable(3042);
      
      GL11.glBlendFunc(770, 771);
      if (getSize().x() != 16.0F)
      {
        GL11.glPushMatrix();
        float scale = getSize().x() / 16.0F;
        GL11.glScalef(scale, scale, 1.0F);
        CraftGUI.Render.iconBlock(IPoint.ZERO, this.itemStack.getFluid().getIcon(this.itemStack));
        GL11.glPopMatrix();
      }
      else
      {
        CraftGUI.Render.iconBlock(IPoint.ZERO, this.itemStack.getFluid().getIcon(this.itemStack));
      }
      GL11.glDisable(3042);
    }
  }
  
  public void setItemStack(FluidStack itemStack)
  {
    this.itemStack = itemStack;
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    if ((this.hastooltip) && (this.itemStack != null)) {
      tooltip.add(this.itemStack.getLocalizedName());
    }
  }
}
