package binnie.craftgui.events;

import binnie.craftgui.core.IWidget;

public class EventValuesChanged<T>
  extends Event
{
  public T[] values;
  
  public EventValuesChanged(IWidget origin, T[] values)
  {
    super(origin);
    this.values = values;
  }
  
  public T[] getValues()
  {
    return this.values;
  }
}
