package binnie.craftgui.extratrees.dictionary;

import binnie.core.machines.Machine;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.minecraft.control.ControlProgressBase;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.StandardTexture;
import binnie.extratrees.core.ExtraTreeTexture;
import binnie.extratrees.machines.Distillery.ComponentDistilleryLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class ControlDistilleryProgress
  extends ControlProgressBase
{
  static Texture DistilleryBase = new StandardTexture(43, 0, 58, 66, ExtraTreeTexture.Gui);
  static Texture DistilleryOverlay = new StandardTexture(139, 0, 18, 66, ExtraTreeTexture.Gui);
  static Texture LiquidFlow = new StandardTexture(101, 0, 38, 66, ExtraTreeTexture.Gui);
  static Texture Output = new StandardTexture(68, 66, 17, 7, ExtraTreeTexture.Gui);
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(DistilleryBase, new IPoint(0.0F, 0.0F));
    CraftGUI.Render.texturePercentage(LiquidFlow, new IArea(18.0F, 0.0F, 38.0F, 66.0F), Position.Left, this.progress);
    
    Distillery.ComponentDistilleryLogic component = (Distillery.ComponentDistilleryLogic)Machine.getInterface(Distillery.ComponentDistilleryLogic.class, Window.get(this).getInventory());
    

    FluidStack stack = null;
    if (component != null) {
      stack = component.currentFluid;
    }
    if (stack != null) {
      for (int y = 0; y < 4; y++) {
        renderFluid(stack, new IPoint(1.0F, 1 + y * 16));
      }
    }
  }
  
  public void onRenderForeground()
  {
    int level = ((Distillery.ComponentDistilleryLogic)Machine.getInterface(Distillery.ComponentDistilleryLogic.class, Window.get(this).getInventory())).level;
    CraftGUI.Render.texture(Output, new IPoint(47.0F, 14 + level * 15));
    CraftGUI.Render.texture(DistilleryOverlay, new IPoint(0.0F, 0.0F));
  }
  
  protected ControlDistilleryProgress(IWidget parent, float x, float y)
  {
    super(parent, x, y, 58.0F, 66.0F);
    
    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        int distillationLevel = -1;
        if (new IArea(45.0F, 8.0F, 19.0F, 11.0F).contains(ControlDistilleryProgress.this.getRelativeMousePosition())) {
          distillationLevel = 0;
        } else if (new IArea(45.0F, 23.0F, 19.0F, 11.0F).contains(ControlDistilleryProgress.this.getRelativeMousePosition())) {
          distillationLevel = 1;
        } else if (new IArea(45.0F, 38.0F, 19.0F, 11.0F).contains(ControlDistilleryProgress.this.getRelativeMousePosition())) {
          distillationLevel = 2;
        }
        if (distillationLevel >= 0)
        {
          NBTTagCompound nbt = new NBTTagCompound();
          nbt.setByte("i", (byte)distillationLevel);
          Window.get(ControlDistilleryProgress.this.getWidget()).sendClientAction("still-level", nbt);
        }
      }
    });
  }
  
  public void renderFluid(FluidStack fluid, IPoint pos)
  {
    int hex = fluid.getFluid().getColor(fluid);
    
    int r = (hex & 0xFF0000) >> 16;
    int g = (hex & 0xFF00) >> 8;
    int b = hex & 0xFF;
    
    IIcon icon = fluid.getFluid().getIcon();
    
    GL11.glColor4f(r / 255.0F, g / 255.0F, b / 255.0F, 1.0F);
    
    GL11.glEnable(3042);
    
    GL11.glBlendFunc(770, 771);
    
    CraftGUI.Render.iconBlock(pos, fluid.getFluid().getIcon());
    
    GL11.glDisable(3042);
  }
}
