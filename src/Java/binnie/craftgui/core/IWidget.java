package binnie.craftgui.core;

import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.events.Event;
import binnie.craftgui.events.EventHandler;
import java.util.List;

public abstract interface IWidget
{
  public abstract IWidget getParent();
  
  public abstract void deleteChild(IWidget paramIWidget);
  
  public abstract void deleteAllChildren();
  
  public abstract ITopLevelWidget getSuperParent();
  
  public abstract boolean isTopLevel();
  
  public abstract IPoint getPosition();
  
  public abstract IPoint pos();
  
  public abstract void setPosition(IPoint paramIPoint);
  
  public abstract IPoint getSize();
  
  public abstract IPoint size();
  
  public abstract void setSize(IPoint paramIPoint);
  
  public abstract IPoint getOriginalPosition();
  
  public abstract IPoint getAbsolutePosition();
  
  public abstract IPoint getOriginalAbsolutePosition();
  
  public abstract IPoint getOffset();
  
  public abstract IArea getArea();
  
  public abstract IArea area();
  
  public abstract void setOffset(IPoint paramIPoint);
  
  public abstract IPoint getMousePosition();
  
  public abstract IPoint getRelativeMousePosition();
  
  public abstract void setColour(int paramInt);
  
  public abstract int getColour();
  
  public abstract void render();
  
  public abstract void updateClient();
  
  public abstract void enable();
  
  public abstract void disable();
  
  public abstract void show();
  
  public abstract void hide();
  
  public abstract boolean calculateIsMouseOver();
  
  public abstract boolean isEnabled();
  
  public abstract boolean isVisible();
  
  public abstract boolean isFocused();
  
  public abstract boolean isMouseOver();
  
  public abstract boolean isDragged();
  
  public abstract boolean isChildVisible(IWidget paramIWidget);
  
  public abstract boolean isChildEnabled(IWidget paramIWidget);
  
  public abstract boolean canMouseOver();
  
  public abstract boolean canFocus();
  
  public abstract IWidget addWidget(IWidget paramIWidget);
  
  public abstract List<IWidget> getWidgets();
  
  public abstract void callEvent(Event paramEvent);
  
  public abstract void recieveEvent(Event paramEvent);
  
  public abstract void onUpdateClient();
  
  public abstract void delete();
  
  public abstract void onDelete();
  
  public abstract <T> T getWidget(Class<T> paramClass);
  
  public abstract IArea getCroppedZone();
  
  public abstract void setCroppedZone(IWidget paramIWidget, IArea paramIArea);
  
  public abstract boolean isCroppedWidet();
  
  public abstract IWidget getCropWidget();
  
  public abstract boolean isMouseOverWidget(IPoint paramIPoint);
  
  public abstract int getLevel();
  
  public abstract boolean isDescendant(IWidget paramIWidget);
  
  public abstract List<IWidgetAttribute> getAttributes();
  
  public abstract boolean hasAttribute(IWidgetAttribute paramIWidgetAttribute);
  
  public abstract boolean addAttribute(IWidgetAttribute paramIWidgetAttribute);
  
  public abstract <E extends Event> void addEventHandler(EventHandler<E> paramEventHandler);
  
  public abstract <E extends Event> void addSelfEventHandler(EventHandler<E> paramEventHandler);
  
  public abstract boolean contains(IPoint paramIPoint);
  
  public abstract float x();
  
  public abstract float y();
  
  public abstract float w();
  
  public abstract float h();
  
  public abstract void onRender(RenderStage paramRenderStage);
}
