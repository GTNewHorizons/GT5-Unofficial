package binnie.craftgui.botany;

import binnie.botany.api.IFlowerColour;
import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.renderer.Renderer;

public class ControlColourDisplay
  extends Control
  implements IControlValue<IFlowerColour>, ITooltip
{
  IFlowerColour value;
  
  public ControlColourDisplay(IWidget parent, float x, float y)
  {
    super(parent, x, y, 16.0F, 16.0F);
    addAttribute(Attribute.MouseOver);
  }
  
  public IFlowerColour getValue()
  {
    return this.value;
  }
  
  public void setValue(IFlowerColour value)
  {
    this.value = value;
  }
  
  public void onRenderBackground()
  {
    CraftGUI.Render.solid(getArea(), -1);
    CraftGUI.Render.solid(getArea().inset(1), -16777216 + getValue().getColor(false));
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    super.getTooltip(tooltip);
    tooltip.add(this.value.getName());
  }
}
