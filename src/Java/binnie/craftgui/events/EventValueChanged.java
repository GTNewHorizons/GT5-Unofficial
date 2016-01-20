package binnie.craftgui.events;

import binnie.craftgui.core.IWidget;

public class EventValueChanged<T>
  extends Event
{
  public T value;
  
  public EventValueChanged(IWidget origin, T value)
  {
    super(origin);
    this.value = value;
  }
  
  public T getValue()
  {
    return this.value;
  }
  
  public static abstract class Handler
    extends EventHandler<EventValueChanged>
  {
    public Handler()
    {
      super();
    }
  }
}
