package binnie.craftgui.minecraft.control;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.resource.Texture;

public class ControlImage
  extends Control
{
  private Object key = null;
  
  public ControlImage(IWidget parent, float x, float y, Texture text)
  {
    super(parent, x, y, text.w(), text.h());
    this.key = text;
  }
  
  public void onRenderForeground()
  {
    CraftGUI.Render.texture(this.key, IPoint.ZERO);
  }
}
