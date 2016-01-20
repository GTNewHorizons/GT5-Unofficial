package binnie.craftgui.controls.core;

import java.util.Collection;

public abstract interface IControlValues<T>
  extends IControlValue<T>
{
  public abstract Collection<T> getValues();
  
  public abstract void setValues(Collection<T> paramCollection);
}
