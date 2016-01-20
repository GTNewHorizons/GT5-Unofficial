package binnie.core.network.packet;

import io.netty.buffer.ByteBuf;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class MessageBase
{
  private int id;
  
  public MessageBase(int id)
  {
    this.id = id;
  }
  
  public MessageBase(MessageBinnie message)
  {
    try
    {
      readData(message.data);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public MessageBinnie GetMessage()
  {
    return new MessageBinnie(this.id, this);
  }
  
  protected NBTTagCompound readNBTTagCompound(ByteBuf data)
    throws IOException
  {
    short length = data.readShort();
    if (length < 0) {
      return null;
    }
    byte[] compressed = new byte[length];
    data.readBytes(compressed);
    return CompressedStreamTools.readCompressed(new ByteArrayInputStream(compressed));
  }
  
  protected void writeNBTTagCompound(NBTTagCompound nbttagcompound, ByteBuf data)
    throws IOException
  {
    if (nbttagcompound == null)
    {
      data.writeShort(-1);
    }
    else
    {
      byte[] compressed = CompressedStreamTools.compress(nbttagcompound);
      data.writeShort((short)compressed.length);
      data.writeBytes(compressed);
    }
  }
  
  public void writeData(ByteBuf data)
    throws IOException
  {}
  
  public void readData(ByteBuf data)
    throws IOException
  {}
}
