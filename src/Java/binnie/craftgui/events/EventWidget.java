package binnie.craftgui.events;

import binnie.craftgui.core.IWidget;

public class EventWidget
  extends Event
{
  public EventWidget(IWidget origin)
  {
    super(origin);
  }
  
  public static class Enable
    extends EventWidget
  {
    public Enable(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.Enable>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class Disable
    extends EventWidget
  {
    public Disable(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.Disable>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class Show
    extends EventWidget
  {
    public Show(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.Show>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class Hide
    extends EventWidget
  {
    public Hide(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.Hide>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class ChangePosition
    extends EventWidget
  {
    public ChangePosition(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.ChangePosition>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class ChangeSize
    extends EventWidget
  {
    public ChangeSize(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.ChangeSize>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class ChangeOffset
    extends EventWidget
  {
    public ChangeOffset(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.ChangeOffset>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class ChangeColour
    extends EventWidget
  {
    public ChangeColour(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.ChangeColour>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class StartMouseOver
    extends EventWidget
  {
    public StartMouseOver(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.StartMouseOver>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class EndMouseOver
    extends EventWidget
  {
    public EndMouseOver(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.EndMouseOver>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class StartDrag
    extends EventWidget
  {
    int button;
    
    public StartDrag(IWidget origin, int button)
    {
      super();
      this.button = button;
    }
    
    public int getButton()
    {
      return this.button;
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.StartDrag>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class EndDrag
    extends EventWidget
  {
    public EndDrag(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.EndDrag>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class GainFocus
    extends EventWidget
  {
    public GainFocus(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.GainFocus>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class LoseFocus
    extends EventWidget
  {
    public LoseFocus(IWidget origin)
    {
      super();
    }
    
    public static abstract class Handler
      extends EventHandler<EventWidget.LoseFocus>
    {
      public Handler()
      {
        super();
      }
    }
  }
}
