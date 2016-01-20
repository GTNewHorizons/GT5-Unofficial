package binnie.core.network.packet;

import binnie.core.network.BinnieCorePacketID;
import io.netty.buffer.ByteBuf;
import java.io.IOException;

public class MessageMetadata
  extends MessageCoordinates
{
  public int meta;
  
  public MessageMetadata(int posX, int posY, int posZ, int meta)
  {
    super(BinnieCorePacketID.TileMetadata.ordinal(), posX, posY, posZ);
    this.meta = meta;
  }
  
  public MessageMetadata(MessageBinnie message)
  {
    super(message);
  }
  
  public void writeData(ByteBuf data)
    throws IOException
  {
    super.writeData(data);
    data.writeInt(this.meta);
  }
  
  public void readData(ByteBuf data)
    throws IOException
  {
    super.readData(data);
    this.meta = data.readInt();
  }
}
