package binnie.craftgui.controls.tab;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.events.EventHandler.Origin;
import binnie.craftgui.events.EventValueChanged;
import binnie.craftgui.events.EventValueChanged.Handler;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ControlTabBar<T>
  extends Control
  implements IControlValue<T>
{
  T value;
  Position position;
  
  public ControlTabBar(IWidget parent, float x, float y, float width, float height, Position position)
  {
    super(parent, x, y, width, height);
    this.position = position;
    
    addEventHandler(new EventValueChanged.Handler()
    {
      public void onEvent(EventValueChanged event)
      {
        ControlTabBar.this.setValue(event.getValue());
      }
    }.setOrigin(EventHandler.Origin.DirectChild, this));
  }
  
  public void setValues(Collection<T> values)
  {
    for (int i = 0; i < getWidgets().size();) {
      deleteChild((IWidget)getWidgets().get(0));
    }
    float length = values.size();
    int tabDimension = (int)(getSize().y() / length);
    if ((this.position == Position.Top) || (this.position == Position.Bottom)) {
      tabDimension = (int)(getSize().x() / length);
    }
    int i = 0;
    for (T value : values)
    {
      IWidget tab;
      IWidget tab;
      if ((this.position == Position.Top) || (this.position == Position.Bottom)) {
        tab = createTab(i * tabDimension, 0.0F, tabDimension, getSize().y(), value);
      } else {
        tab = createTab(0.0F, i * tabDimension, getSize().x(), tabDimension, value);
      }
      i++;
    }
    if ((this.value == null) && (!values.isEmpty())) {
      setValue(values.iterator().next());
    }
  }
  
  public ControlTab<T> createTab(float x, float y, float w, float h, T value)
  {
    return new ControlTab(this, x, y, w, h, value);
  }
  
  public T getValue()
  {
    return this.value;
  }
  
  public void setValue(T value)
  {
    boolean change = this.value != value;
    this.value = value;
    if (change) {
      callEvent(new EventValueChanged(this, value));
    }
  }
  
  public Position getDirection()
  {
    return this.position;
  }
}
