package binnie.craftgui.controls.page;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.IWidget;

public class ControlPage<T>
  extends Control
  implements IControlValue<T>
{
  T value;
  
  public ControlPage(IWidget parent, T value)
  {
    this(parent, 0.0F, 0.0F, parent.w(), parent.h(), value);
  }
  
  public ControlPage(IWidget parent, float x, float y, float w, float h, T value)
  {
    super(parent, x, y, w, h);
    setValue(value);
    if (((parent instanceof IControlValue)) && (((IControlValue)parent).getValue() == null)) {
      ((IControlValue)parent).setValue(value);
    }
  }
  
  public T getValue()
  {
    return this.value;
  }
  
  public void setValue(T value)
  {
    this.value = value;
  }
}
