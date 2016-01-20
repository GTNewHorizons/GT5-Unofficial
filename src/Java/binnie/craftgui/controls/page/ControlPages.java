package binnie.craftgui.controls.page;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.controls.core.IControlValues;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.events.EventValueChanged;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ControlPages<T>
  extends Control
  implements IControlValues<T>, IControlValue<T>
{
  public boolean isChildVisible(IWidget child)
  {
    if (child == null) {
      return false;
    }
    return this.value == ((IControlValue)child).getValue();
  }
  
  public ControlPages(IWidget parent, float x, float y, float w, float h)
  {
    super(parent, x, y, w, h);
  }
  
  T value = null;
  
  public void onAddChild(IWidget widget) {}
  
  public T getValue()
  {
    return this.value;
  }
  
  public void setValue(T value)
  {
    if (this.value != value)
    {
      this.value = value;
      callEvent(new EventValueChanged(this, value));
    }
  }
  
  public Collection<T> getValues()
  {
    List<T> list = new ArrayList();
    for (IWidget child : getWidgets()) {
      list.add(((IControlValue)child).getValue());
    }
    return list;
  }
  
  public void setValues(Collection<T> values) {}
}
