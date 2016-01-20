package binnie.craftgui.botany;

import binnie.botany.api.IFlowerColour;
import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.listbox.ControlList;
import binnie.craftgui.controls.listbox.ControlTextOption;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.geometry.CraftGUIUtil;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;

public class ControlColourOption
  extends ControlTextOption<IFlowerColour>
{
  ControlColourDisplay controlBee;
  IPoint boxPosition;
  
  public ControlColourOption(ControlList<IFlowerColour> controlList, IFlowerColour option, int y)
  {
    super(controlList, option, option.getName(), y);
    setSize(new IPoint(getSize().x(), 20.0F));
    
    this.controlBee = new ControlColourDisplay(this, 2.0F, 2.0F);
    this.controlBee.setValue(option);
    
    addAttribute(Attribute.MouseOver);
    
    CraftGUIUtil.moveWidget(this.textWidget, new IPoint(22.0F, 0.0F));
    this.textWidget.setSize(this.textWidget.getSize().sub(new IPoint(24.0F, 0.0F)));
    
    int th = (int)CraftGUI.Render.textHeight(this.textWidget.getValue(), this.textWidget.getSize().x());
    int height = Math.max(20, th + 6);
    setSize(new IPoint(size().x(), height));
    this.textWidget.setSize(new IPoint(this.textWidget.getSize().x(), height));
    this.boxPosition = new IPoint(2.0F, (height - 18) / 2);
  }
}
