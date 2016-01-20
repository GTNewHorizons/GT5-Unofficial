package binnie.craftgui.extratrees.kitchen;

import binnie.craftgui.controls.button.ControlButton;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.minecraft.Window;

public abstract class ControlDropdownButton
  extends ControlButton
{
  public ControlDropdownButton(IWidget parent, float x, float y, float width, float height, String text)
  {
    super(parent, x, y, width, height, text);
    
    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        ControlDropDownMenu menu = (ControlDropDownMenu)ControlDropdownButton.this.getWidget(ControlDropDownMenu.class);
        ControlDropdownButton.this.deleteChild((IWidget)ControlDropdownButton.this.getWidget(ControlDropDownMenu.class));
        if (ControlDropdownButton.this.getWidget(ControlDropDownMenu.class) == null)
        {
          menu = ControlDropdownButton.this.createDropDownMenu();
          Window.get(ControlDropdownButton.this.getWidget()).setFocusedWidget(menu);
        }
        else
        {
          ControlDropdownButton.this.deleteChild((IWidget)ControlDropdownButton.this.getWidget(ControlDropDownMenu.class));
        }
      }
    });
  }
  
  public abstract ControlDropDownMenu createDropDownMenu();
}
