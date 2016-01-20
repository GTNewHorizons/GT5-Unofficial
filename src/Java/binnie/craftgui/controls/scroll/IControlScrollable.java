package binnie.craftgui.controls.scroll;

import binnie.craftgui.core.IWidget;

public abstract interface IControlScrollable
  extends IWidget
{
  public abstract float getPercentageShown();
  
  public abstract float getPercentageIndex();
  
  public abstract void movePercentage(float paramFloat);
  
  public abstract void setPercentageIndex(float paramFloat);
  
  public abstract float getMovementRange();
}
