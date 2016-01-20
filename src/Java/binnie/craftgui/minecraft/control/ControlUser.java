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

public class ControlUser
  extends Control
  implements ITooltip
{
  private String username = "";
  String team = "";
  
  public ControlUser(IWidget parent, float x, float y, String username)
  {
    super(parent, x, y, 16.0F, 16.0F);
    addAttribute(Attribute.MouseOver);
    this.username = username;
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(CraftGUITexture.UserButton, getArea());
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    tooltip.setType(Tooltip.Type.User);
    tooltip.add("Owner");
    if (this.username != "") {
      tooltip.add(this.username);
    }
    tooltip.setMaxWidth(200);
  }
}
