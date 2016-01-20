package binnie.craftgui.minecraft.control;

import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.resource.Texture;

public class ControlMachineProgress
  extends ControlProgress
{
  public ControlMachineProgress(IWidget parent, int x, int y, Texture base, Texture progress, Position dir)
  {
    super(parent, x, y, base, progress, dir);
  }
}
