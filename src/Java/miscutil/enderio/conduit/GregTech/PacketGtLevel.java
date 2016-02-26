package miscutil.enderio.conduit.GregTech;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import crazypants.enderio.conduit.gas.IGasConduit;
import crazypants.enderio.network.MessageTileEntity;

public class PacketGtLevel extends MessageTileEntity<TileEntity> implements IMessageHandler<PacketGtLevel, IMessage> {

  public NBTTagCompound tc;

  public PacketGtLevel() {
  }

  public PacketGtLevel(IGasConduit conduit) {
    super(conduit.getBundle().getEntity());
    tc = new NBTTagCompound();
    conduit.writeToNBT(tc);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    ByteBufUtils.writeTag(buf, tc);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    tc = ByteBufUtils.readTag(buf);
  }

  @Override
  public IMessage onMessage(PacketGtLevel message, MessageContext ctx) {
    // TODO ClientUtil.doGasLevelUpdate(message.x, message.y, message.z, message);
    return null;
  }
}
