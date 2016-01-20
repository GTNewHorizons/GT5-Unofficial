package binnie.craftgui.core.geometry;

public enum Position
{
  Top(0, -1),  Bottom(0, 1),  Left(-1, 0),  Right(1, 0);
  
  int x;
  int y;
  
  private Position(int x, int y)
  {
    this.x = x;
    this.y = y;
  }
  
  public int x()
  {
    return this.x;
  }
  
  public int y()
  {
    return this.y;
  }
  
  public Position opposite()
  {
    switch (1.$SwitchMap$binnie$craftgui$core$geometry$Position[ordinal()])
    {
    case 1: 
      return Top;
    case 2: 
      return Right;
    case 3: 
      return Left;
    case 4: 
      return Bottom;
    }
    return null;
  }
}
