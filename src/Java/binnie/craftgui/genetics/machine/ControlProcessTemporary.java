package binnie.craftgui.genetics.machine;

import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.minecraft.control.ControlMachineProgress;

public class ControlProcessTemporary
  extends ControlMachineProgress
{
  public ControlProcessTemporary(IWidget parent, int x, int y, int width, int height)
  {
    super(parent, x, y, null, null, null);
    setSize(new IPoint(width, height));
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.solid(getArea(), -4868683);
    
    float w = getSize().y() * this.progress / 100.0F;
    
    CraftGUI.Render.solid(new IArea(getArea().x(), getArea().y(), w, getArea().h()), -65536);
  }
}
