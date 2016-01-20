package binnie.craftgui.events;

import binnie.craftgui.core.IWidget;

public class EventToggleButtonClicked
  extends Event
{
  boolean toggled;
  
  public EventToggleButtonClicked(IWidget origin, boolean toggled)
  {
    super(origin);
    this.toggled = toggled;
  }
  
  public boolean isActive()
  {
    return this.toggled;
  }
}
