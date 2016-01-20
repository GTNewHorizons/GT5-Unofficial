package binnie.craftgui.controls.button;

import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventValueChanged;
import java.util.ArrayList;
import java.util.List;

public class ControlEnumButton<T>
  extends ControlButton
  implements IControlValue<T>
{
  public static final String eventEnumChanged = "eventEnumButtonChanged";
  private T currentSelection;
  
  public String getText()
  {
    return this.currentSelection.toString();
  }
  
  public void onMouseClick(EventMouse.Down event)
  {
    int index = this.enumConstants.indexOf(this.currentSelection);
    if (index < this.enumConstants.size() - 1) {
      index++;
    } else {
      index = 0;
    }
    T newEnum = this.enumConstants.get(index);
    
    setValue(newEnum);
  }
  
  public void setValue(T selection)
  {
    if (this.currentSelection != selection)
    {
      this.currentSelection = selection;
      callEvent(new EventValueChanged(this, getValue()));
    }
  }
  
  private List<T> enumConstants = new ArrayList();
  
  public ControlEnumButton(IWidget parent, float x, float y, float width, float height, T[] values)
  {
    super(parent, x, y, width, height, "");
    for (T value : values) {
      this.enumConstants.add(value);
    }
    if (values.length > 0) {
      this.currentSelection = values[0];
    }
  }
  
  public T getValue()
  {
    return this.currentSelection;
  }
}
