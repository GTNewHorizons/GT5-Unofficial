package binnie.core.network.packet;

public class IndexInPayload
{
  public IndexInPayload(int intIndex, int floatIndex, int stringIndex)
  {
    this.intIndex = intIndex;
    this.floatIndex = floatIndex;
    this.stringIndex = stringIndex;
  }
  
  public int intIndex = 0;
  public int floatIndex = 0;
  public int stringIndex = 0;
}
