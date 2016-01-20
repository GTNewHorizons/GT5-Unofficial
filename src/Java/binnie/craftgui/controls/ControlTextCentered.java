package binnie.craftgui.controls;

import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.TextJustification;

public class ControlTextCentered
  extends ControlText
{
  public ControlTextCentered(IWidget parent, float y, String text)
  {
    super(parent, new IArea(new IPoint(0.0F, y), new IPoint(parent.size().x(), 0.0F)), text, TextJustification.TopCenter);
  }
}
