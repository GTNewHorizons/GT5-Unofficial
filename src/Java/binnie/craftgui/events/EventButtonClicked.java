package binnie.craftgui.events;

import binnie.craftgui.core.IWidget;

public class EventButtonClicked
  extends Event
{
  public EventButtonClicked(IWidget origin)
  {
    super(origin);
  }
  
  public static abstract class Handler
    extends EventHandler<EventButtonClicked>
  {
    public Handler()
    {
      super();
    }
  }
}
