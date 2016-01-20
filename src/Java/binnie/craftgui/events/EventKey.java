package binnie.craftgui.events;

import binnie.craftgui.core.IWidget;

public abstract class EventKey
  extends Event
{
  char character;
  int key;
  
  public EventKey(IWidget origin, char character, int key)
  {
    super(origin);
    this.character = character;
    this.key = key;
  }
  
  public char getCharacter()
  {
    return this.character;
  }
  
  public int getKey()
  {
    return this.key;
  }
  
  public static class Down
    extends EventKey
  {
    public Down(IWidget origin, char character, int key)
    {
      super(character, key);
    }
    
    public static abstract class Handler
      extends EventHandler<EventKey.Down>
    {
      public Handler()
      {
        super();
      }
    }
  }
  
  public static class Up
    extends EventKey
  {
    public Up(IWidget origin, char character, int key)
    {
      super(character, key);
    }
    
    public static abstract class Handler
      extends EventHandler<EventKey.Up>
    {
      public Handler()
      {
        super();
      }
    }
  }
}
