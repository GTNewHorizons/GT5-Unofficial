package binnie.craftgui.events;

import binnie.craftgui.core.IWidget;
import java.util.List;

public abstract class EventHandler<E extends Event>
{
  Class<E> eventClass;
  Origin origin = Origin.Any;
  IWidget relative = null;
  
  public EventHandler(Class<E> eventClass)
  {
    this.eventClass = eventClass;
  }
  
  public EventHandler setOrigin(Origin origin, IWidget relative)
  {
    this.origin = origin;
    this.relative = relative;
    return this;
  }
  
  public abstract void onEvent(E paramE);
  
  public final boolean handles(Event e)
  {
    return (this.eventClass.isInstance(e)) && (this.origin.isOrigin(e.getOrigin(), this.relative));
  }
  
  public static enum Origin
  {
    Any,  Self,  Parent,  DirectChild;
    
    private Origin() {}
    
    public boolean isOrigin(IWidget origin, IWidget test)
    {
      switch (EventHandler.1.$SwitchMap$binnie$craftgui$events$EventHandler$Origin[ordinal()])
      {
      case 1: 
        return true;
      case 2: 
        return test.getWidgets().contains(origin);
      case 3: 
        return test.getParent() == origin;
      case 4: 
        return test == origin;
      }
      return false;
    }
  }
}
