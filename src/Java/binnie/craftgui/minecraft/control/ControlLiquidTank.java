package binnie.craftgui.minecraft.control;

import binnie.core.BinnieCore;
import binnie.core.machines.Machine;
import binnie.core.machines.inventory.MachineSide;
import binnie.core.machines.inventory.TankSlot;
import binnie.core.machines.inventory.Validator;
import binnie.core.machines.power.ITankMachine;
import binnie.core.machines.power.TankInfo;
import binnie.core.proxy.BinnieProxy;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.ITopLevelWidget;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.Tooltip.Type;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.minecraft.ContainerCraftGUI;
import binnie.craftgui.minecraft.GuiCraftGUI;
import binnie.craftgui.minecraft.MinecraftTooltip;
import binnie.craftgui.minecraft.MinecraftTooltip.Type;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class ControlLiquidTank
  extends Control
  implements ITooltip
{
  public static List<Integer> tankError = new ArrayList();
  private int tankID = 0;
  private boolean horizontal = false;
  
  public ControlLiquidTank(IWidget parent, int x, int y)
  {
    this(parent, x, y, false);
  }
  
  public ControlLiquidTank(IWidget parent, int x, int y, boolean horizontal)
  {
    super(parent, x, y, horizontal ? 60.0F : 18.0F, horizontal ? 18.0F : 60.0F);
    this.horizontal = horizontal;
    addAttribute(Attribute.MouseOver);
    
    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        if (event.getButton() == 0)
        {
          NBTTagCompound nbt = new NBTTagCompound();
          nbt.setByte("id", (byte)ControlLiquidTank.this.tankID);
          Window.get(ControlLiquidTank.this.getWidget()).sendClientAction("tank-click", nbt);
        }
      }
    });
  }
  
  public void setTankID(int tank)
  {
    this.tankID = tank;
  }
  
  public TankInfo getTank()
  {
    return Window.get(this).getContainer().getTankInfo(this.tankID);
  }
  
  public boolean isTankValid()
  {
    return !getTank().isEmpty();
  }
  
  public int getTankCapacity()
  {
    return (int)getTank().getCapacity();
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(this.horizontal ? CraftGUITexture.HorizontalLiquidTank : CraftGUITexture.LiquidTank, IPoint.ZERO);
    if ((isMouseOver()) && (Window.get(this).getGui().isHelpMode()))
    {
      int c = -1442840576 + MinecraftTooltip.getOutline(Tooltip.Type.Help);
      CraftGUI.Render.gradientRect(getArea().inset(1), c, c);
    }
    else if (tankError.contains(Integer.valueOf(this.tankID)))
    {
      int c = -1442840576 + MinecraftTooltip.getOutline(MinecraftTooltip.Type.Error);
      CraftGUI.Render.gradientRect(getArea().inset(1), c, c);
    }
    else if (getSuperParent().getMousedOverWidget() == this)
    {
      if (Window.get(this).getGui().getDraggedItem() != null) {
        CraftGUI.Render.gradientRect(getArea().inset(1), -1426089575, -1426089575);
      } else {
        CraftGUI.Render.gradientRect(getArea().inset(1), -2130706433, -2130706433);
      }
    }
    if (isTankValid())
    {
      Object content = null;
      
      float height = this.horizontal ? 16.0F : 58.0F;
      
      int squaled = (int)(height * (getTank().getAmount() / getTank().getCapacity()));
      


      int yPos = (int)height + 1;
      
      Fluid fluid = getTank().liquid.getFluid();
      
      int hex = fluid.getColor(getTank().liquid);
      
      int r = (hex & 0xFF0000) >> 16;
      int g = (hex & 0xFF00) >> 8;
      int b = hex & 0xFF;
      
      GL11.glColor4f(r / 255.0F, g / 255.0F, b / 255.0F, 1.0F);
      
      GL11.glEnable(3042);
      
      GL11.glBlendFunc(770, 771);
      
      IPoint pos = getAbsolutePosition();
      IPoint offset = new IPoint(0.0F, height - squaled);
      IArea limited = getArea().inset(1);
      if (this.horizontal) {
        limited.setSize(new IPoint(limited.w() - 1.0F, limited.h()));
      }
      CraftGUI.Render.limitArea(new IArea(limited.pos().add(pos).add(offset), limited.size().sub(offset)));
      

      GL11.glEnable(3089);
      
      BinnieCore.proxy.bindTexture(TextureMap.locationItemsTexture);
      for (int y = 0; y < height; y += 16) {
        for (int x = 0; x < (this.horizontal ? 58 : 16); x += 16)
        {
          IIcon icon = fluid.getIcon();
          
          CraftGUI.Render.iconBlock(new IPoint(1 + x, 1 + y), icon);
        }
      }
      GL11.glDisable(3089);
      GL11.glDisable(3042);
    }
  }
  
  public void onRenderForeground()
  {
    CraftGUI.Render.texture(this.horizontal ? CraftGUITexture.HorizontalLiquidTankOverlay : CraftGUITexture.LiquidTankOverlay, IPoint.ZERO);
    if ((isMouseOver()) && (Window.get(this).getGui().isHelpMode()))
    {
      IArea area = getArea();
      CraftGUI.Render.colour(MinecraftTooltip.getOutline(Tooltip.Type.Help));
      CraftGUI.Render.texture(CraftGUITexture.Outline, area.outset(1));
    }
    if (tankError.contains(Integer.valueOf(this.tankID)))
    {
      IArea area = getArea();
      CraftGUI.Render.colour(MinecraftTooltip.getOutline(MinecraftTooltip.Type.Error));
      CraftGUI.Render.texture(CraftGUITexture.Outline, area.outset(1));
    }
  }
  
  public void getHelpTooltip(Tooltip tooltip)
  {
    if (getTankSlot() != null)
    {
      TankSlot slot = getTankSlot();
      tooltip.add(slot.getName());
      tooltip.add("Capacity: " + getTankCapacity() + " mB");
      tooltip.add("Insert Side: " + MachineSide.asString(slot.getInputSides()));
      tooltip.add("Extract Side: " + MachineSide.asString(slot.getOutputSides()));
      if (slot.isReadOnly()) {
        tooltip.add("Output Only Tank");
      }
      tooltip.add("Accepts: " + (slot.getValidator() == null ? "Any Item" : slot.getValidator().getTooltip()));
      
      return;
    }
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    if (isTankValid())
    {
      int percentage = (int)(100.0D * getTank().getAmount() / getTankCapacity());
      
      tooltip.add(getTank().getName());
      
      tooltip.add(percentage + "% full");
      tooltip.add((int)getTank().getAmount() + " mB");
      


      return;
    }
    tooltip.add("Empty");
  }
  
  private TankSlot getTankSlot()
  {
    ITankMachine tank = (ITankMachine)Machine.getInterface(ITankMachine.class, Window.get(this).getInventory());
    
    return tank != null ? tank.getTankSlot(this.tankID) : null;
  }
}
