package binnie.craftgui.core;

import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.events.EventMouse.Drag;
import binnie.craftgui.events.EventMouse.Move;
import binnie.craftgui.events.EventMouse.Up;
import binnie.craftgui.events.EventMouse.Up.Handler;
import binnie.craftgui.events.EventWidget.EndDrag;
import binnie.craftgui.events.EventWidget.EndMouseOver;
import binnie.craftgui.events.EventWidget.GainFocus;
import binnie.craftgui.events.EventWidget.LoseFocus;
import binnie.craftgui.events.EventWidget.StartDrag;
import binnie.craftgui.events.EventWidget.StartDrag.Handler;
import binnie.craftgui.events.EventWidget.StartMouseOver;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.ListIterator;
import org.lwjgl.input.Mouse;

public abstract class TopLevelWidget
  extends Widget
  implements ITopLevelWidget
{
  public TopLevelWidget()
  {
    super(null);
    
    addEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        TopLevelWidget.this.setDraggedWidget(TopLevelWidget.this.mousedOverWidget, event.getButton());
        TopLevelWidget.this.setFocusedWidget(TopLevelWidget.this.mousedOverWidget);
      }
    });
    addEventHandler(new EventMouse.Up.Handler()
    {
      public void onEvent(EventMouse.Up event)
      {
        TopLevelWidget.this.setDraggedWidget(null);
      }
    });
    addEventHandler(new EventWidget.StartDrag.Handler()
    {
      public void onEvent(EventWidget.StartDrag event)
      {
        TopLevelWidget.this.dragStart = TopLevelWidget.this.getRelativeMousePosition();
      }
    });
  }
  
  IWidget mousedOverWidget = null;
  IWidget draggedWidget = null;
  IWidget focusedWidget = null;
  
  public void setMousedOverWidget(IWidget widget)
  {
    if (this.mousedOverWidget == widget) {
      return;
    }
    if (this.mousedOverWidget != null) {
      callEvent(new EventWidget.EndMouseOver(this.mousedOverWidget));
    }
    this.mousedOverWidget = widget;
    if (this.mousedOverWidget != null) {
      callEvent(new EventWidget.StartMouseOver(this.mousedOverWidget));
    }
  }
  
  public void setDraggedWidget(IWidget widget)
  {
    setDraggedWidget(widget, -1);
  }
  
  public void setDraggedWidget(IWidget widget, int button)
  {
    if (this.draggedWidget == widget) {
      return;
    }
    if (this.draggedWidget != null) {
      callEvent(new EventWidget.EndDrag(this.draggedWidget));
    }
    this.draggedWidget = widget;
    if (this.draggedWidget != null) {
      callEvent(new EventWidget.StartDrag(this.draggedWidget, button));
    }
  }
  
  public void setFocusedWidget(IWidget widget)
  {
    IWidget newWidget = widget;
    if (this.focusedWidget == newWidget) {
      return;
    }
    if ((newWidget != null) && (!newWidget.canFocus())) {
      newWidget = null;
    }
    if (this.focusedWidget != null) {
      callEvent(new EventWidget.LoseFocus(this.focusedWidget));
    }
    this.focusedWidget = newWidget;
    if (this.focusedWidget != null) {
      callEvent(new EventWidget.GainFocus(this.focusedWidget));
    }
  }
  
  public IWidget getMousedOverWidget()
  {
    return this.mousedOverWidget;
  }
  
  public IWidget getDraggedWidget()
  {
    return this.draggedWidget;
  }
  
  public IWidget getFocusedWidget()
  {
    return this.focusedWidget;
  }
  
  public boolean isMouseOver(IWidget widget)
  {
    return getMousedOverWidget() == widget;
  }
  
  public boolean isDragged(IWidget widget)
  {
    return getDraggedWidget() == widget;
  }
  
  public boolean isFocused(IWidget widget)
  {
    return getFocusedWidget() == widget;
  }
  
  public void updateTopLevel()
  {
    setMousedOverWidget(calculateMousedOverWidget());
    if ((getFocusedWidget() != null) && ((!getFocusedWidget().isVisible()) || (!getFocusedWidget().isEnabled()))) {
      setFocusedWidget(null);
    }
    if (!Mouse.isButtonDown(0)) {
      if (this.draggedWidget != null) {
        setDraggedWidget(null);
      }
    }
  }
  
  private IWidget calculateMousedOverWidget()
  {
    Deque<IWidget> queue = calculateMousedOverWidgets();
    while (!queue.isEmpty())
    {
      IWidget widget = (IWidget)queue.removeFirst();
      if ((widget.isEnabled()) && (widget.isVisible()) && (widget.canMouseOver())) {
        if ((widget.isEnabled()) && (widget.isVisible()) && (widget.canMouseOver()) && (widget.calculateIsMouseOver())) {
          return widget;
        }
      }
    }
    return null;
  }
  
  public Deque<IWidget> calculateMousedOverWidgets()
  {
    Deque<IWidget> list = new ArrayDeque();
    for (IWidget widget : getQueuedWidgets(this)) {
      if (widget.calculateIsMouseOver()) {
        list.addLast(widget);
      }
    }
    return list;
  }
  
  private Collection<IWidget> getQueuedWidgets(IWidget widget)
  {
    List<IWidget> widgets = new ArrayList();
    
    boolean addChildren = true;
    if (widget.isCroppedWidet()) {
      addChildren = widget.getCroppedZone().contains(widget.getCropWidget().getRelativeMousePosition());
    }
    if (addChildren)
    {
      ListIterator<IWidget> li = widget.getWidgets().listIterator(widget.getWidgets().size());
      while (li.hasPrevious())
      {
        IWidget child = (IWidget)li.previous();
        widgets.addAll(getQueuedWidgets(child));
      }
    }
    widgets.add(widget);
    
    return widgets;
  }
  
  protected IPoint mousePosition = new IPoint(0.0F, 0.0F);
  
  public void setMousePosition(int x, int y)
  {
    float dx = x - this.mousePosition.x();
    float dy = y - this.mousePosition.y();
    if ((dx != 0.0F) || (dy != 0.0F)) {
      if (getDraggedWidget() != null) {
        callEvent(new EventMouse.Drag(getDraggedWidget(), dx, dy));
      } else {
        callEvent(new EventMouse.Move(this, dx, dy));
      }
    }
    if ((this.mousePosition.x() != x) || (this.mousePosition.y() != y))
    {
      this.mousePosition = new IPoint(x, y);
      setMousedOverWidget(calculateMousedOverWidget());
    }
  }
  
  public IPoint getAbsoluteMousePosition()
  {
    return this.mousePosition;
  }
  
  public void widgetDeleted(IWidget widget)
  {
    if (isMouseOver(widget)) {
      setMousedOverWidget(null);
    }
    if (isDragged(widget)) {
      setDraggedWidget(null);
    }
    if (isFocused(widget)) {
      setFocusedWidget(null);
    }
  }
  
  IPoint dragStart = IPoint.ZERO;
  
  public IPoint getDragDistance()
  {
    return getRelativeMousePosition().sub(this.dragStart);
  }
}
