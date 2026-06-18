package gtPlusPlus.core.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.network.handler.AbstractServerMessageHandler;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;
import io.netty.buffer.ByteBuf;

public class PacketVolumetricFlaskGui extends AbstractServerMessageHandler<PacketVolumetricFlaskGui>
    implements IMessage {

    private int x;
    private int y;
    private int z;
    private int flaskValue;

    public PacketVolumetricFlaskGui() {}

    public PacketVolumetricFlaskGui(TileEntityVolumetricFlaskSetter tile, int aCustomValue) {
        x = tile.xCoord;
        y = tile.yCoord;
        z = tile.zCoord;
        flaskValue = aCustomValue;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(flaskValue);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        flaskValue = buf.readInt();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getCustomValue() {
        return flaskValue;
    }

    protected TileEntityVolumetricFlaskSetter getTileEntity(PacketVolumetricFlaskGui message, MessageContext ctx) {
        World worldObj = getWorld(ctx);
        if (worldObj == null) {
            return null;
        }
        TileEntity te = worldObj.getTileEntity(message.getX(), message.getY(), message.getZ());
        if (te == null) {
            return null;
        }
        if (te instanceof TileEntityVolumetricFlaskSetter) {
            return (TileEntityVolumetricFlaskSetter) te;
        }
        return null;
    }

    protected World getWorld(MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            return ctx.getServerHandler().playerEntity.worldObj;
        } else {
            return GTplusplus.proxy.getClientWorld();
        }
    }

    @Override
    public IMessage handleServerMessage(EntityPlayer player, PacketVolumetricFlaskGui message, MessageContext ctx) {
        TileEntityVolumetricFlaskSetter te = getTileEntity(message, ctx);
        if (te != null) {
            te.setCustomValue(message.getCustomValue());
            // return new Packet_VolumetricFlaskGui2(te, message.getCustomValue());
        }
        return null;
    }

}
