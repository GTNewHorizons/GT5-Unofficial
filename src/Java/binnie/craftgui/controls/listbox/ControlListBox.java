package binnie.craftgui.controls.listbox;

import binnie.core.util.IValidator;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.controls.scroll.ControlScrollableContent;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.events.EventKey.Down;
import binnie.craftgui.events.EventKey.Down.Handler;
import java.util.Collection;

public class ControlListBox<T>
  extends ControlScrollableContent<ControlList<T>>
  implements IControlValue<T>
{
  public ControlListBox(IWidget parent, float x, float y, float w, float h, float scrollBarSize)
  {
    super(parent, x, y, w, h, scrollBarSize);
  }
  
  public void initialise()
  {
    setScrollableContent(new ControlList(this, 1.0F, 1.0F, w() - 2.0F - this.scrollBarSize, h() - 2.0F));
    
    addEventHandler(new EventKey.Down.Handler()
    {
      public void onEvent(EventKey.Down event)
      {
        EventKey.Down eventKey = event;
        if (ControlListBox.this.calculateIsMouseOver())
        {
          int currentIndex = ((ControlList)ControlListBox.this.getContent()).getCurrentIndex();
          if (eventKey.getKey() == 208)
          {
            currentIndex++;
            if (currentIndex >= ((ControlList)ControlListBox.this.getContent()).getOptions().size()) {
              currentIndex = 0;
            }
          }
          else if (eventKey.getKey() == 200)
          {
            currentIndex--;
            if (currentIndex < 0) {
              currentIndex = ((ControlList)ControlListBox.this.getContent()).getOptions().size() - 1;
            }
          }
          ((ControlList)ControlListBox.this.getContent()).setIndex(currentIndex);
        }
      }
    });
  }
  
  public final T getValue()
  {
    return ((ControlList)getContent()).getValue();
  }
  
  public final void setValue(T value)
  {
    ((ControlList)getContent()).setValue(value);
  }
  
  public void setOptions(Collection<T> options)
  {
    ((ControlList)getContent()).setOptions(options);
  }
  
  public IWidget createOption(T value, int y)
  {
    return new ControlOption((ControlList)getContent(), value, y);
  }
  
  public void setValidator(IValidator<IWidget> validator)
  {
    ((ControlList)getContent()).setValidator(validator);
  }
}
