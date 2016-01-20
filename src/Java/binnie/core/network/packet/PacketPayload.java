package binnie.core.network.packet;

import binnie.core.network.INetworkedEntity;
import java.util.ArrayList;
import java.util.List;

public class PacketPayload
{
  public List<Integer> intPayload = new ArrayList();
  public List<Float> floatPayload = new ArrayList();
  public List<String> stringPayload = new ArrayList();
  
  public PacketPayload()
  {
    this.intPayload.clear();
    this.floatPayload.clear();
    this.stringPayload.clear();
  }
  
  public PacketPayload(INetworkedEntity tile)
  {
    this();
    tile.writeToPacket(this);
  }
  
  public void addInteger(int a)
  {
    this.intPayload.add(Integer.valueOf(a));
  }
  
  public void addFloat(float a)
  {
    this.floatPayload.add(Float.valueOf(a));
  }
  
  public void addString(String a)
  {
    this.stringPayload.add(a);
  }
  
  public int getInteger()
  {
    return ((Integer)this.intPayload.remove(0)).intValue();
  }
  
  public float getFloat()
  {
    return ((Float)this.floatPayload.remove(0)).floatValue();
  }
  
  public String getString()
  {
    return (String)this.stringPayload.remove(0);
  }
  
  public void append(PacketPayload other)
  {
    if (other == null) {
      return;
    }
    this.intPayload.addAll(other.intPayload);
    this.floatPayload.addAll(other.floatPayload);
    this.stringPayload.addAll(other.stringPayload);
  }
  
  public boolean isEmpty()
  {
    return (this.intPayload.isEmpty()) && (this.floatPayload.isEmpty()) && (this.stringPayload.isEmpty());
  }
}
