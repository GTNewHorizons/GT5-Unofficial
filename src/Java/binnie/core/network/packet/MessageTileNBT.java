package binnie.core.network.packet;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MessageTileNBT
  extends MessageNBT
  implements IPacketLocation
{
  private int posX;
  private int posY;
  private int posZ;
  
  public MessageTileNBT(MessageBinnie message)
  {
    super(message);
  }
  
  public MessageTileNBT(int id, TileEntity tile, NBTTagCompound nbt)
  {
    super(id);
    
    this.posX = tile.xCoord;
    this.posY = tile.yCoord;
    this.posZ = tile.zCoord;
    
    this.nbt = nbt;
  }
  
  public void writeData(ByteBuf data)
    throws IOException
  {
    data.writeInt(this.posX);
    data.writeInt(this.posY);
    data.writeInt(this.posZ);
    
    super.writeData(data);
  }
  
  public void readData(ByteBuf data)
    throws IOException
  {
    this.posX = data.readInt();
    this.posY = data.readInt();
    this.posZ = data.readInt();
    
    super.readData(data);
  }
  
  public TileEntity getTarget(World world)
  {
    return world.getTileEntity(this.posX, this.posY, this.posZ);
  }
  
  public int getX()
  {
    return this.posX;
  }
  
  public int getY()
  {
    return this.posY;
  }
  
  public int getZ()
  {
    return this.posZ;
  }
  
  public NBTTagCompound getTagCompound()
  {
    return this.nbt;
  }
  
  void setTagCompound(NBTTagCompound nbt)
  {
    this.nbt = nbt;
  }
}
