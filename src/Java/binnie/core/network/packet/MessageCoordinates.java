package binnie.core.network.packet;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class MessageCoordinates
  extends MessageBase
{
  public int posX;
  public int posY;
  public int posZ;
  
  public MessageCoordinates(MessageBinnie message)
  {
    super(message);
  }
  
  public MessageCoordinates(int id, ChunkCoordinates coordinates)
  {
    this(id, coordinates.posX, coordinates.posY, coordinates.posZ);
  }
  
  public MessageCoordinates(int id, int posX, int posY, int posZ)
  {
    super(id);
    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
  }
  
  public void writeData(ByteBuf data)
    throws IOException
  {
    data.writeInt(this.posX);
    data.writeInt(this.posY);
    data.writeInt(this.posZ);
  }
  
  public void readData(ByteBuf data)
    throws IOException
  {
    this.posX = data.readInt();
    this.posY = data.readInt();
    this.posZ = data.readInt();
  }
  
  public ChunkCoordinates getCoordinates()
  {
    return new ChunkCoordinates(this.posX, this.posY, this.posZ);
  }
  
  public TileEntity getTileEntity(World world)
  {
    return world.getTileEntity(this.posX, this.posY, this.posZ);
  }
}
