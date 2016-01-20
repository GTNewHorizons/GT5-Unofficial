package binnie.craftgui.core;

import binnie.craftgui.core.geometry.IPoint;

public abstract interface ITopLevelWidget
  extends IWidget
{
  public abstract void setMousePosition(int paramInt1, int paramInt2);
  
  public abstract IPoint getAbsoluteMousePosition();
  
  public abstract IWidget getFocusedWidget();
  
  public abstract IWidget getMousedOverWidget();
  
  public abstract IWidget getDraggedWidget();
  
  public abstract boolean isFocused(IWidget paramIWidget);
  
  public abstract boolean isMouseOver(IWidget paramIWidget);
  
  public abstract boolean isDragged(IWidget paramIWidget);
  
  public abstract void updateTopLevel();
  
  public abstract void widgetDeleted(IWidget paramIWidget);
  
  public abstract IPoint getDragDistance();
}
