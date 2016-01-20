package binnie.craftgui.events;

import binnie.craftgui.core.IWidget;

public class EventTextEdit
  extends EventValueChanged<String>
{
  public EventTextEdit(IWidget origin, String text)
  {
    super(origin, text);
  }
  
  public static abstract class Handler
    extends EventHandler<EventTextEdit>
  {
    public Handler()
    {
      super();
    }
  }
}
