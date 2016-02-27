package crazypants.enderio.conduit.gas;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import crazypants.enderio.conduit.IConduitBundle;
import crazypants.enderio.network.MessageTileEntity;
import crazypants.util.ClientUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class PacketGasLevel
  extends MessageTileEntity<TileEntity>
  implements IMessageHandler<PacketGasLevel, IMessage>
{
  public NBTTagCompound tc;
  
  public PacketGasLevel() {}
  
  public PacketGasLevel(IGasConduit conduit)
  {
    super(conduit.getBundle().getEntity());
    this.tc = new NBTTagCompound();
    conduit.writeToNBT(this.tc);
  }
  
  public void toBytes(ByteBuf buf)
  {
    super.toBytes(buf);
    ByteBufUtils.writeTag(buf, this.tc);
  }
  
  public void fromBytes(ByteBuf buf)
  {
    super.fromBytes(buf);
    this.tc = ByteBufUtils.readTag(buf);
  }
  
  public IMessage onMessage(PacketGasLevel message, MessageContext ctx)
  {
    ClientUtil.doGasLevelUpdate(message.x, message.y, message.z, message);
    return null;
  }
}
