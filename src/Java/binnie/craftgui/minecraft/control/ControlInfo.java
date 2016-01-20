package binnie.craftgui.minecraft.control;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.Tooltip.Type;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.resource.minecraft.CraftGUITexture;

public class ControlInfo
  extends Control
  implements ITooltip
{
  private String info;
  
  public ControlInfo(IWidget parent, float x, float y, String info)
  {
    super(parent, x, y, 16.0F, 16.0F);
    addAttribute(Attribute.MouseOver);
    this.info = info;
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(CraftGUITexture.InfoButton, getArea());
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    tooltip.setType(Tooltip.Type.Information);
    tooltip.add("Info");
    tooltip.add(this.info);
    tooltip.setMaxWidth(200);
  }
}
