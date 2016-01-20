package binnie.craftgui.controls.scroll;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.events.EventMouse.Wheel;
import binnie.craftgui.events.EventMouse.Wheel.Handler;
import binnie.craftgui.events.EventWidget.ChangeSize;
import binnie.craftgui.events.EventWidget.ChangeSize.Handler;

public class ControlScrollableContent<T extends IWidget>
  extends Control
  implements IControlScrollable
{
  protected T controlChild;
  protected float scrollBarSize;
  
  public ControlScrollableContent(IWidget parent, float x, float y, float w, float h, float scrollBarSize)
  {
    super(parent, x, y, w, h);
    if (scrollBarSize != 0.0F) {
      new ControlScroll(this, getSize().x() - scrollBarSize, 0.0F, scrollBarSize, getSize().y(), this);
    }
    addEventHandler(new EventMouse.Wheel.Handler()
    {
      public void onEvent(EventMouse.Wheel event)
      {
        if ((ControlScrollableContent.this.getRelativeMousePosition().x() > 0.0F) && (ControlScrollableContent.this.getRelativeMousePosition().y() > 0.0F) && (ControlScrollableContent.this.getRelativeMousePosition().x() < ControlScrollableContent.this.getSize().x()) && (ControlScrollableContent.this.getRelativeMousePosition().y() < ControlScrollableContent.this.getSize().y()))
        {
          if (ControlScrollableContent.this.getMovementRange() == 0.0F) {
            return;
          }
          float percentageMove = 0.8F / ControlScrollableContent.this.getMovementRange();
          ControlScrollableContent.this.movePercentage(percentageMove * -event.getDWheel());
        }
      }
    });
    this.scrollBarSize = scrollBarSize;
  }
  
  public void setScrollableContent(T child)
  {
    this.controlChild = child;
    if (child == null) {
      return;
    }
    child.setCroppedZone(this, new IArea(1.0F, 1.0F, getSize().x() - 2.0F - this.scrollBarSize, getSize().y() - 2.0F));
    child.addSelfEventHandler(new EventWidget.ChangeSize.Handler()
    {
      public void onEvent(EventWidget.ChangeSize event)
      {
        ControlScrollableContent.this.controlChild.setOffset(new IPoint(0.0F, -ControlScrollableContent.this.percentageIndex * ControlScrollableContent.this.getMovementRange()));
        if (ControlScrollableContent.this.getMovementRange() == 0.0F) {
          ControlScrollableContent.this.percentageIndex = 0.0F;
        }
      }
    });
  }
  
  public T getContent()
  {
    return this.controlChild;
  }
  
  public float getPercentageShown()
  {
    if ((this.controlChild == null) || (this.controlChild.getSize().y() == 0.0F)) {
      return 1.0F;
    }
    float shown = getSize().y() / this.controlChild.getSize().y();
    return Math.min(shown, 1.0F);
  }
  
  float percentageIndex = 0.0F;
  
  public float getPercentageIndex()
  {
    return this.percentageIndex;
  }
  
  public void movePercentage(float percentage)
  {
    if (this.controlChild == null) {
      return;
    }
    this.percentageIndex += percentage;
    if (this.percentageIndex > 1.0F) {
      this.percentageIndex = 1.0F;
    } else if (this.percentageIndex < 0.0F) {
      this.percentageIndex = 0.0F;
    }
    if (getMovementRange() == 0.0F) {
      this.percentageIndex = 0.0F;
    }
    this.controlChild.setOffset(new IPoint(0.0F, -this.percentageIndex * getMovementRange()));
  }
  
  public void setPercentageIndex(float index)
  {
    movePercentage(index - this.percentageIndex);
  }
  
  public float getMovementRange()
  {
    if (this.controlChild == null) {
      return 0.0F;
    }
    float range = this.controlChild.getSize().y() - getSize().y();
    return Math.max(range, 0.0F);
  }
  
  public void onUpdateClient()
  {
    setPercentageIndex(getPercentageIndex());
  }
  
  public void ensureVisible(float minY, float maxY, float totalY)
  {
    minY /= totalY;
    maxY /= totalY;
    
    float shownPercentage = getPercentageShown();
    float percentageIndex = getPercentageIndex();
    float minPercent = (1.0F - shownPercentage) * percentageIndex;
    float maxPercent = minPercent + shownPercentage;
    if (maxY > maxPercent) {
      setPercentageIndex((maxY - shownPercentage) / (1.0F - shownPercentage));
    }
    if (minY < minPercent) {
      setPercentageIndex(minY / (1.0F - shownPercentage));
    }
  }
}
