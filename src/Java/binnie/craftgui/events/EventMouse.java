package binnie.craftgui.events;

import binnie.craftgui.core.IWidget;

public abstract class EventMouse
  extends Event
{
  public EventMouse(IWidget origin)
  {
    super(origin);
  }
  
  public static class Button
    extends EventMouse
  {
    int x;
    int y;
    int button;
    
    public int getX()
    {
      return this.x;
    }
    
    public int getY()
    {
      return this.y;
    }
    
    public int getButton()
    {
      return this.button;
    }
    
    public Button(IWidget currentMousedOverWidget, int x, int y, int button)
    {
      super();
      this.x = x;
      this.y = y;
      this.button = button;
    }
  }
  
  public static class Down
    extends EventMouse.Button
  {
    public Down(IWidget currentMousedOverWidget, int x, int y, int button)
    {
      super(x, y, button);
    }
    
    public static abstract class Handler
      extends EventHandler<EventMouse.Down>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class Up
    extends EventMouse.Button
  {
    public Up(IWidget currentMousedOverWidget, int x, int y, int button)
    {
      super(x, y, button);
    }
    
    public static abstract class Handler
      extends EventHandler<EventMouse.Up>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class Move
    extends EventMouse
  {
    float dx;
    float dy;
    
    public float getDx()
    {
      return this.dx;
    }
    
    public float getDy()
    {
      return this.dy;
    }
    
    public Move(IWidget origin, float dx, float dy)
    {
      super();
      this.dx = dx;
      this.dy = dy;
    }
    
    public static abstract class Handler
      extends EventHandler<EventMouse.Move>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class Drag
    extends EventMouse.Move
  {
    public Drag(IWidget draggedWidget, float dx, float dy)
    {
      super(dx, dy);
    }
    
    public static abstract class Handler
      extends EventHandler<EventMouse.Drag>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class Wheel
    extends EventMouse
  {
    int dWheel = 0;
    
    public Wheel(IWidget origin, int dWheel)
    {
      super();
      this.dWheel = (dWheel / 28);
    }
    
    public int getDWheel()
    {
      return this.dWheel;
    }
    
    public static abstract class Handler
      extends EventHandler<EventMouse.Wheel>
    {
      public Handler()
      {
        super();
      }
    }
  }
}
