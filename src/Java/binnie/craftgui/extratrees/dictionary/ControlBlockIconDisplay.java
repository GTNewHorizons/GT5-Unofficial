package binnie.craftgui.extratrees.dictionary;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import net.minecraft.util.IIcon;

public class ControlBlockIconDisplay
  extends Control
{
  IIcon icon;
  
  public ControlBlockIconDisplay(IWidget parent, float x, float y, IIcon icon)
  {
    super(parent, x, y, 18.0F, 18.0F);
    this.icon = icon;
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.iconBlock(IPoint.ZERO, this.icon);
  }
}
