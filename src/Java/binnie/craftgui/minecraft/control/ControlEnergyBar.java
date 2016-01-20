package binnie.craftgui.minecraft.control;

import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.core.machines.power.IPoweredMachine;
import binnie.core.machines.power.IProcess;
import binnie.core.machines.power.PowerInfo;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.Tooltip.Type;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.minecraft.ContainerCraftGUI;
import binnie.craftgui.minecraft.GuiCraftGUI;
import binnie.craftgui.minecraft.MinecraftTooltip;
import binnie.craftgui.minecraft.MinecraftTooltip.Type;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import net.minecraft.inventory.IInventory;
import org.lwjgl.opengl.GL11;

public class ControlEnergyBar
  extends Control
  implements ITooltip
{
  public static boolean isError;
  private Position direction;
  
  public ControlEnergyBar(IWidget parent, int x, int y, int width, int height, Position direction)
  {
    super(parent, x, y, width, height);
    this.direction = direction;
    addAttribute(Attribute.MouseOver);
  }
  
  public IPoweredMachine getClientPower()
  {
    IInventory inventory = Window.get(this).getInventory();
    TileEntityMachine machine = (TileEntityMachine)((inventory instanceof TileEntityMachine) ? inventory : null);
    if (machine == null) {
      return null;
    }
    IPoweredMachine clientPower = (IPoweredMachine)machine.getMachine().getInterface(IPoweredMachine.class);
    return clientPower;
  }
  
  public float getPercentage()
  {
    float percentage = 100.0F * getStoredEnergy() / getMaxEnergy();
    if (percentage > 100.0F) {
      percentage = 100.0F;
    }
    return percentage;
  }
  
  private float getStoredEnergy()
  {
    return Window.get(this).getContainer().getPowerInfo().getStoredEnergy();
  }
  
  private float getMaxEnergy()
  {
    return Window.get(this).getContainer().getPowerInfo().getMaxEnergy();
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    tooltip.add((int)getPercentage() + "% charged");
    
    tooltip.add(getStoredEnergy() + "/" + getMaxEnergy() + " RF");
  }
  
  public void getHelpTooltip(Tooltip tooltip)
  {
    tooltip.add("Energy Bar");
    tooltip.add("Current: " + getStoredEnergy() + " RF (" + (int)getPercentage() + "%)");
    tooltip.add("Capacity: " + getMaxEnergy() + " RF");
    
    IProcess process = (IProcess)Machine.getInterface(IProcess.class, Window.get(this).getInventory());
    if (process != null) {
      tooltip.add("Usage: " + (int)process.getEnergyPerTick() + " RF");
    }
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(CraftGUITexture.EnergyBarBack, getArea());
    
    float percentage = getPercentage() / 100.0F;
    
    CraftGUI.Render.colour(getColourFromPercentage(percentage));
    
    IArea area = getArea();
    switch (1.$SwitchMap$binnie$craftgui$core$geometry$Position[this.direction.ordinal()])
    {
    case 1: 
    case 2: 
      float height = area.size().y() * percentage;
      area.setSize(new IPoint(area.size().x(), height));
      
      break;
    case 3: 
    case 4: 
      float width = area.size().x() * percentage;
      area.setSize(new IPoint(width, area.size().y()));
    }
    if ((isMouseOver()) && (Window.get(this).getGui().isHelpMode()))
    {
      int c = -1442840576 + MinecraftTooltip.getOutline(Tooltip.Type.Help);
      CraftGUI.Render.gradientRect(getArea().inset(1), c, c);
    }
    else if (isError)
    {
      int c = -1442840576 + MinecraftTooltip.getOutline(MinecraftTooltip.Type.Error);
      CraftGUI.Render.gradientRect(getArea().inset(1), c, c);
    }
    CraftGUI.Render.texture(CraftGUITexture.EnergyBarGlow, area);
    
    GL11.glColor3d(1.0D, 1.0D, 1.0D);
    
    CraftGUI.Render.texture(CraftGUITexture.EnergyBarGlass, getArea());
  }
  
  public void onRenderForeground()
  {
    if ((isMouseOver()) && (Window.get(this).getGui().isHelpMode()))
    {
      IArea area = getArea();
      CraftGUI.Render.colour(MinecraftTooltip.getOutline(Tooltip.Type.Help));
      CraftGUI.Render.texture(CraftGUITexture.Outline, area.outset(1));
    }
    else if (isError)
    {
      IArea area = getArea();
      CraftGUI.Render.colour(MinecraftTooltip.getOutline(MinecraftTooltip.Type.Error));
      CraftGUI.Render.texture(CraftGUITexture.Outline, area.outset(1));
    }
  }
  
  public int getColourFromPercentage(float percentage)
  {
    int colour = 16777215;
    if (percentage > 0.5D)
    {
      int r = (int)((1.0D - 2.0D * (percentage - 0.5D)) * 255.0D);
      colour = (r << 16) + 65280;
    }
    else
    {
      int g = (int)(255.0F * (2.0F * percentage));
      colour = 16711680 + (g << 8);
    }
    return colour;
  }
}
