package binnie.craftgui.minecraft.control;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import net.minecraft.util.IIcon;

public class ControlIconDisplay
  extends Control
{
  private IIcon icon = null;
  
  public ControlIconDisplay(IWidget parent, float x, float y, IIcon icon)
  {
    super(parent, x, y, 16.0F, 16.0F);
    this.icon = icon;
  }
  
  public void onRenderForeground()
  {
    CraftGUI.Render.iconItem(IPoint.ZERO, this.icon);
  }
}
