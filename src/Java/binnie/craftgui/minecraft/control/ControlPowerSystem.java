package binnie.craftgui.minecraft.control;

import binnie.core.machines.power.PowerSystem;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.Tooltip.Type;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.resource.minecraft.CraftGUITexture;

public class ControlPowerSystem
  extends Control
  implements ITooltip
{
  private PowerSystem system;
  
  public ControlPowerSystem(IWidget parent, float x, float y, PowerSystem system)
  {
    super(parent, x, y, 16.0F, 16.0F);
    addAttribute(Attribute.MouseOver);
    this.system = system;
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(CraftGUITexture.PowerButton, getArea());
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    tooltip.setType(Tooltip.Type.Power);
    tooltip.add("Power Supply");
    tooltip.add("Powered by " + this.system.getUnitName());
    tooltip.setMaxWidth(200);
  }
}
