package binnie.craftgui.controls.core;

import binnie.craftgui.core.IWidget;

public abstract interface IControlValue<T>
  extends IWidget
{
  public abstract T getValue();
  
  public abstract void setValue(T paramT);
}
