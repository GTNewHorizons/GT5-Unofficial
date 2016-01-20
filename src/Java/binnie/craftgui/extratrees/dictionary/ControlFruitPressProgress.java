package binnie.craftgui.extratrees.dictionary;

import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.minecraft.ContainerCraftGUI;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlProgressBase;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.StandardTexture;
import binnie.extratrees.core.ExtraTreeTexture;
import binnie.extratrees.machines.Press;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class ControlFruitPressProgress
  extends ControlProgressBase
{
  static Texture PressTexture = new StandardTexture(6, 0, 24, 52, ExtraTreeTexture.Gui);
  static Texture PressSlot = new StandardTexture(9, 52, 34, 17, ExtraTreeTexture.Gui);
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(PressSlot, new IPoint(3.0F, 52.0F));
    
    ItemStack input = Window.get(this).getContainer().getSlotFromInventory(Window.get(this).getInventory(), Press.slotCurrent).getStack();
    if ((input == null) || (Press.getOutput(input) == null)) {
      return;
    }
    Fluid fluid = Press.getOutput(input).getFluid();
    
    int hex = fluid.getColor(Press.getOutput(input));
    
    int r = (hex & 0xFF0000) >> 16;
    int g = (hex & 0xFF00) >> 8;
    int b = hex & 0xFF;
    
    IIcon icon = fluid.getIcon();
    
    GL11.glColor4f(r / 255.0F, g / 255.0F, b / 255.0F, 1.0F);
    
    GL11.glEnable(3042);
    
    GL11.glBlendFunc(770, 771);
    
    CraftGUI.Render.iconBlock(new IPoint(4.0F, 52.0F), fluid.getIcon());
    
    GL11.glDisable(3042);
    
    icon = input.getIconIndex();
    CraftGUI.Render.iconItem(new IPoint(4.0F, 52.0F), icon);
  }
  
  public void onRenderForeground()
  {
    CraftGUI.Render.texture(PressTexture, new IPoint(0.0F, 16.0F * this.progress));
  }
  
  protected ControlFruitPressProgress(IWidget parent, float x, float y)
  {
    super(parent, x, y, 37.0F, 69.0F);
    addAttribute(Attribute.MouseOver);
    
    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        if (event.getButton() == 0)
        {
          NBTTagCompound action = new NBTTagCompound();
          Window.get(ControlFruitPressProgress.this.getWidget()).sendClientAction("fruitpress-click", action);
        }
        else if (event.getButton() == 1)
        {
          NBTTagCompound action = new NBTTagCompound();
          Window.get(ControlFruitPressProgress.this.getWidget()).sendClientAction("clear-fruit", action);
        }
      }
    });
  }
}
