package binnie.craftgui.core;

import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.Event;
import binnie.craftgui.events.EventHandler;
import binnie.craftgui.events.EventHandler.Origin;
import binnie.craftgui.events.EventWidget.ChangeColour;
import binnie.craftgui.events.EventWidget.ChangeOffset;
import binnie.craftgui.events.EventWidget.ChangePosition;
import binnie.craftgui.events.EventWidget.ChangeSize;
import binnie.craftgui.events.EventWidget.Disable;
import binnie.craftgui.events.EventWidget.Enable;
import binnie.craftgui.events.EventWidget.Hide;
import binnie.craftgui.events.EventWidget.Show;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Widget
  implements IWidget
{
  public Widget(IWidget parent)
  {
    this.parent = parent;
    if (parent != null) {
      parent.addWidget(this);
    }
  }
  
  private IWidget parent = null;
  private List<IWidget> subWidgets = new ArrayList();
  private List<IWidgetAttribute> attributes = new ArrayList();
  
  public List<IWidgetAttribute> getAttributes()
  {
    return this.attributes;
  }
  
  public boolean hasAttribute(IWidgetAttribute attribute)
  {
    return this.attributes.contains(attribute);
  }
  
  public boolean addAttribute(IWidgetAttribute attribute)
  {
    return this.attributes.add(attribute);
  }
  
  public final void deleteChild(IWidget child)
  {
    if (child == null) {
      return;
    }
    child.delete();
    this.subWidgets.remove(child);
  }
  
  public final void deleteAllChildren()
  {
    while (!this.subWidgets.isEmpty()) {
      deleteChild((IWidget)this.subWidgets.get(0));
    }
  }
  
  public final IWidget getParent()
  {
    return this.parent;
  }
  
  public final ITopLevelWidget getSuperParent()
  {
    return isTopLevel() ? (ITopLevelWidget)this : this.parent.getSuperParent();
  }
  
  public final IWidget addWidget(IWidget widget)
  {
    if ((this.subWidgets.size() != 0) && (((IWidget)this.subWidgets.get(this.subWidgets.size() - 1)).hasAttribute(Attribute.AlwaysOnTop))) {
      this.subWidgets.add(this.subWidgets.size() - 1, widget);
    } else {
      this.subWidgets.add(widget);
    }
    onAddChild(widget);
    return widget;
  }
  
  protected void onAddChild(IWidget widget) {}
  
  public final List<IWidget> getWidgets()
  {
    return this.subWidgets;
  }
  
  public final boolean isTopLevel()
  {
    return this instanceof ITopLevelWidget;
  }
  
  private IPoint position = new IPoint(0.0F, 0.0F);
  private IPoint size = new IPoint(0.0F, 0.0F);
  private IPoint offset = new IPoint(0.0F, 0.0F);
  IArea cropArea;
  IWidget cropWidget;
  
  public final IPoint pos()
  {
    return this.position.add(this.offset);
  }
  
  public final IPoint size()
  {
    return this.size;
  }
  
  public final IArea area()
  {
    return getArea();
  }
  
  public final IPoint getPosition()
  {
    return pos();
  }
  
  public final IArea getArea()
  {
    return new IArea(IPoint.ZERO, size());
  }
  
  public final IPoint getOriginalPosition()
  {
    return this.position;
  }
  
  boolean cropped = false;
  
  public IArea getCroppedZone()
  {
    return this.cropArea;
  }
  
  public void setCroppedZone(IWidget relative, IArea area)
  {
    this.cropArea = area;
    this.cropped = true;
    this.cropWidget = relative;
  }
  
  public final IPoint getAbsolutePosition()
  {
    return isTopLevel() ? getPosition() : getParent().getAbsolutePosition().add(getPosition());
  }
  
  public final IPoint getOriginalAbsolutePosition()
  {
    return isTopLevel() ? getOriginalPosition() : getParent().getOriginalPosition().sub(getOriginalPosition());
  }
  
  public final IPoint getSize()
  {
    return size();
  }
  
  public final IPoint getOffset()
  {
    return this.offset;
  }
  
  public final void setPosition(IPoint vector)
  {
    if (!vector.equals(this.position))
    {
      this.position = new IPoint(vector);
      callEvent(new EventWidget.ChangePosition(this));
    }
  }
  
  public final void setSize(IPoint vector)
  {
    if (!vector.equals(this.size))
    {
      this.size = new IPoint(vector);
      callEvent(new EventWidget.ChangeSize(this));
    }
  }
  
  public final void setOffset(IPoint vector)
  {
    if (vector != this.offset)
    {
      this.offset = new IPoint(vector);
      callEvent(new EventWidget.ChangeOffset(this));
    }
  }
  
  int colour = 16777215;
  
  public final void setColour(int colour)
  {
    if (this.colour != colour)
    {
      this.colour = colour;
      callEvent(new EventWidget.ChangeColour(this));
    }
  }
  
  public final int getColour()
  {
    return this.colour;
  }
  
  public boolean canMouseOver()
  {
    return hasAttribute(Attribute.MouseOver);
  }
  
  public boolean canFocus()
  {
    return hasAttribute(Attribute.CanFocus);
  }
  
  private Collection<EventHandler> globalEventHandlers = new ArrayList();
  
  public void addEventHandler(EventHandler handler)
  {
    this.globalEventHandlers.add(handler);
  }
  
  public void addSelfEventHandler(EventHandler handler)
  {
    addEventHandler(handler.setOrigin(EventHandler.Origin.Self, this));
  }
  
  public final void callEvent(Event event)
  {
    getSuperParent().recieveEvent(event);
  }
  
  public final void recieveEvent(Event event)
  {
    for (EventHandler handler : this.globalEventHandlers) {
      if (handler.handles(event)) {
        handler.onEvent(event);
      }
    }
    try
    {
      for (IWidget child : getWidgets()) {
        child.recieveEvent(event);
      }
    }
    catch (ConcurrentModificationException e) {}
  }
  
  public final IPoint getMousePosition()
  {
    return getSuperParent().getAbsoluteMousePosition();
  }
  
  public final IPoint getRelativeMousePosition()
  {
    return isTopLevel() ? getMousePosition() : getParent().getRelativeMousePosition().sub(getPosition());
  }
  
  public boolean isCroppedWidet()
  {
    return this.cropped;
  }
  
  public final IWidget getCropWidget()
  {
    return this.cropWidget == null ? this : this.cropWidget;
  }
  
  public final void render()
  {
    if (isVisible())
    {
      CraftGUI.Render.preRender(this);
      onRender(RenderStage.PreChildren);
      for (IWidget widget : getWidgets()) {
        widget.render();
      }
      for (IWidget widget : getWidgets())
      {
        CraftGUI.Render.preRender(widget);
        widget.onRender(RenderStage.PostSiblings);
        CraftGUI.Render.postRender(widget);
      }
      onRender(RenderStage.PostChildren);
      CraftGUI.Render.postRender(this);
    }
  }
  
  public final void updateClient()
  {
    if (!isVisible()) {
      return;
    }
    if (getSuperParent() == this) {
      ((ITopLevelWidget)this).updateTopLevel();
    }
    onUpdateClient();
    
    List<IWidget> deletedWidgets = new ArrayList();
    for (IWidget widget : getWidgets()) {
      if (widget.hasAttribute(Attribute.NeedsDeletion)) {
        deletedWidgets.add(widget);
      } else {
        widget.updateClient();
      }
    }
    for (IWidget widget : deletedWidgets) {
      deleteChild(widget);
    }
  }
  
  public final boolean calculateIsMouseOver()
  {
    IPoint mouse = getRelativeMousePosition();
    if (!this.cropped) {
      return isMouseOverWidget(mouse);
    }
    IWidget cropRelative = this.cropWidget != null ? this.cropWidget : this;
    IPoint pos = IPoint.sub(cropRelative.getAbsolutePosition(), getAbsolutePosition());
    IPoint size = new IPoint(this.cropArea.size().x(), this.cropArea.size().y());
    boolean inCrop = (mouse.x() > pos.x()) && (mouse.y() > pos.y()) && (mouse.x() < pos.x() + size.x()) && (mouse.y() < pos.y() + size.y());
    
    return (inCrop) && (isMouseOverWidget(mouse));
  }
  
  public boolean isMouseOverWidget(IPoint relativeMouse)
  {
    return getArea().contains(relativeMouse);
  }
  
  private boolean enabled = true;
  private boolean visible = true;
  
  public final void enable()
  {
    this.enabled = true;
    callEvent(new EventWidget.Enable(this));
  }
  
  public final void disable()
  {
    this.enabled = false;
    callEvent(new EventWidget.Disable(this));
  }
  
  public final void show()
  {
    this.visible = true;
    callEvent(new EventWidget.Show(this));
  }
  
  public final void hide()
  {
    this.visible = false;
    callEvent(new EventWidget.Hide(this));
  }
  
  public boolean isEnabled()
  {
    return (this.enabled) && ((isTopLevel()) || ((getParent().isEnabled()) && (getParent().isChildEnabled(this))));
  }
  
  public final boolean isVisible()
  {
    return (this.visible) && ((isTopLevel()) || ((getParent().isVisible()) && (getParent().isChildVisible(this))));
  }
  
  public final boolean isFocused()
  {
    return getSuperParent().isFocused(this);
  }
  
  public final boolean isDragged()
  {
    return getSuperParent().isDragged(this);
  }
  
  public final boolean isMouseOver()
  {
    return getSuperParent().isMouseOver(this);
  }
  
  public boolean isChildVisible(IWidget child)
  {
    return true;
  }
  
  public boolean isChildEnabled(IWidget child)
  {
    return true;
  }
  
  public void onRender(RenderStage stage)
  {
    if (stage == RenderStage.PreChildren) {
      onRenderBackground();
    }
    if (stage == RenderStage.PostChildren) {
      onRenderForeground();
    }
    if (stage == RenderStage.PostSiblings) {
      onRenderOverlay();
    }
  }
  
  public void onRenderBackground() {}
  
  public void onRenderForeground() {}
  
  public void onRenderOverlay() {}
  
  public void onUpdateClient() {}
  
  public final void delete()
  {
    getSuperParent().widgetDeleted(this);
    onDelete();
  }
  
  public void onDelete() {}
  
  public <T> T getWidget(Class<T> x)
  {
    for (IWidget child : getWidgets())
    {
      if (x.isInstance(child)) {
        return child;
      }
      T found = child.getWidget(x);
      if (found != null) {
        return found;
      }
    }
    return null;
  }
  
  public final boolean contains(IPoint position)
  {
    return getArea().contains(position);
  }
  
  public void scheduleDeletion()
  {
    addAttribute(Attribute.NeedsDeletion);
  }
  
  public int getLevel()
  {
    int level = getParent() == null ? 0 : getParent().getLevel();
    int index = getParent() == null ? 0 : getParent().getWidgets().indexOf(this);
    return level + index;
  }
  
  public boolean isDescendant(IWidget widget)
  {
    IWidget clss = this;
    do
    {
      if (clss == widget) {
        return true;
      }
      clss = clss.getParent();
    } while (clss != null);
    return false;
  }
  
  public float x()
  {
    return pos().x();
  }
  
  public float y()
  {
    return pos().y();
  }
  
  public float w()
  {
    return size().x();
  }
  
  public float h()
  {
    return size().y();
  }
  
  public IWidget getWidget()
  {
    return this;
  }
}
