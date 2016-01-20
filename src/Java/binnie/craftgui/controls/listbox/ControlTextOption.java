package binnie.craftgui.controls.listbox;

import binnie.craftgui.controls.ControlText;
import binnie.craftgui.core.geometry.TextJustification;
import binnie.craftgui.events.EventHandler.Origin;
import binnie.craftgui.events.EventWidget.ChangeColour;
import binnie.craftgui.events.EventWidget.ChangeColour.Handler;

public class ControlTextOption<T>
  extends ControlOption<T>
{
  public ControlTextOption(ControlList<T> controlList, T option, String optionName, int y)
  {
    super(controlList, option, y);
    this.textWidget = new ControlText(this, getArea(), optionName, TextJustification.MiddleCenter);
    
    addEventHandler(new EventWidget.ChangeColour.Handler()
    {
      public void onEvent(EventWidget.ChangeColour event)
      {
        ControlTextOption.this.textWidget.setColour(ControlTextOption.this.getColour());
      }
    }.setOrigin(EventHandler.Origin.Self, this));
  }
  
  public ControlTextOption(ControlList<T> controlList, T option, int y)
  {
    this(controlList, option, option.toString(), y);
  }
  
  protected ControlText textWidget = null;
  
  public String getText()
  {
    return this.textWidget.getValue();
  }
}
