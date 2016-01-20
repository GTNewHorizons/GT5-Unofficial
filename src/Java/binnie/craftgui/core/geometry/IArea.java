package binnie.craftgui.core.geometry;

public class IArea
{
  private IPoint pos;
  private IPoint size;
  
  public IArea(IArea area)
  {
    this(area.pos().x(), area.pos().y(), area.size().x(), area.size().y());
  }
  
  public IArea(IPoint pos, IPoint size)
  {
    this(pos.x(), pos.y(), size.x(), size.y());
  }
  
  public IArea(float xywh)
  {
    this(xywh, xywh, xywh, xywh);
  }
  
  public IArea(float xy, float wh)
  {
    this(xy, xy, wh, wh);
  }
  
  public IArea(float x, float y, float wh)
  {
    this(x, y, wh, wh);
  }
  
  public IArea(float x, float y, float w, float h)
  {
    setPosition(new IPoint(x, y));
    setSize(new IPoint(w, h));
  }
  
  public IPoint pos()
  {
    return this.pos;
  }
  
  public IPoint getPosition()
  {
    return this.pos;
  }
  
  public void setPosition(IPoint position)
  {
    this.pos = position.copy();
  }
  
  public IPoint size()
  {
    return this.size;
  }
  
  public IPoint getSize()
  {
    return this.size;
  }
  
  public void setSize(IPoint size)
  {
    this.size = size.copy();
  }
  
  public boolean contains(IPoint position)
  {
    return (position.x() >= pos().x()) && (position.y() >= this.pos.y()) && (position.x() <= pos().x() + size().x()) && (position.y() <= pos().y() + size().y());
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
  
  public float x(float n)
  {
    return this.pos.x(n);
  }
  
  public float y(float n)
  {
    return this.pos.y(n);
  }
  
  public float w(float n)
  {
    return this.size.x(n);
  }
  
  public float h(float n)
  {
    return this.size.y(n);
  }
  
  public IArea inset(IBorder border)
  {
    return new IArea(x() + border.l(), y() + border.t(), w() - border.l() - border.r(), h() - border.t() - border.b());
  }
  
  public IArea outset(int outset)
  {
    return outset(new IBorder(outset));
  }
  
  public IArea outset(IBorder border)
  {
    return new IArea(x() - border.l(), y() - border.t(), w() + border.l() + border.r(), h() + border.t() + border.b());
  }
  
  public IArea inset(int inset)
  {
    return inset(new IBorder(inset));
  }
  
  public String toString()
  {
    return w() + "x" + h() + "@" + x() + "," + y();
  }
  
  public IArea shift(float dx, float f)
  {
    return new IArea(x() + dx, y() + f, w(), h());
  }
}
