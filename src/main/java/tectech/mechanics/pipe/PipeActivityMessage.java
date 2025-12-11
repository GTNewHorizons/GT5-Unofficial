package tectech.mechanics.pipe;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import io.netty.buffer.ByteBuf;

public class PipeActivityMessage implements IMessage {

    int mPosX;
    int mPosY;
    int mPosZ;
    int mPosD;
    int mActive;

    public PipeActivityMessage() {}

    private PipeActivityMessage(IActivePipe metaTile) {
        IGregTechTileEntity base = metaTile.getBaseMetaTileEntity();
        mPosX = base.getXCoord();
        mPosY = base.getYCoord();
        mPosZ = base.getZCoord();
        mPosD = base.getWorld().provider.dimensionId;
        mActive = metaTile.getActive() ? 1 : 0;
    }

    private PipeActivityMessage(World world, int x, int y, int z, boolean active) {
        mPosX = x;
        mPosY = y;
        mPosZ = z;
        mPosD = world.provider.dimensionId;
        mActive = active ? 1 : 0;
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        mPosX = buffer.readInt();
        mPosY = buffer.readInt();
        mPosZ = buffer.readInt();
        mPosD = buffer.readInt();
        mActive = buffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(mPosX);
        buffer.writeInt(mPosY);
        buffer.writeInt(mPosZ);
        buffer.writeInt(mPosD);
        buffer.writeInt(mActive);
    }

    public static class PipeActivityQuery extends PipeActivityMessage {

        public PipeActivityQuery() {}

        public PipeActivityQuery(IActivePipe metaTile) {
            super(metaTile);
        }

        public PipeActivityQuery(World world, int x, int y, int z, boolean active) {
            super(world, x, y, z, active);
        }
    }

    public static class PipeActivityData extends PipeActivityMessage {

        public PipeActivityData() {}

        private PipeActivityData(PipeActivityQuery query) {
            mPosX = query.mPosX;
            mPosY = query.mPosY;
            mPosZ = query.mPosZ;
            mPosD = query.mPosD;
            mActive = query.mActive;
        }

        public PipeActivityData(IActivePipe metaTile) {
            super(metaTile);
        }

        public PipeActivityData(World world, int x, int y, int z, boolean active) {
            super(world, x, y, z, active);
        }
    }

    public static class ClientHandler implements IMessageHandler<PipeActivityData, IMessage> {

        @Override
        public IMessage onMessage(PipeActivityData message, MessageContext ctx) {
            var world = Minecraft.getMinecraft().theWorld;

            if (world.provider.dimensionId == message.mPosD) {
                TileEntity te = world.getTileEntity(message.mPosX, message.mPosY, message.mPosZ);
                if (te instanceof IGregTechTileEntity gregTile) {
                    IMetaTileEntity meta = gregTile.getMetaTileEntity();
                    if (meta instanceof IActivePipe activePipe) {
                        activePipe.setActive(message.mActive == 1);
                    }
                }
            }
            return null;
        }
    }

    public static class ServerHandler implements IMessageHandler<PipeActivityQuery, IMessage> {

        @Override
        public IMessage onMessage(PipeActivityQuery message, MessageContext ctx) {
            World world = DimensionManager.getWorld(message.mPosD);
            if (world != null) {
                TileEntity te = world.getTileEntity(message.mPosX, message.mPosY, message.mPosZ);
                if (te instanceof IGregTechTileEntity gregTile) {
                    IMetaTileEntity meta = gregTile.getMetaTileEntity();
                    if (meta instanceof IActivePipe activePipe) {
                        message.mActive = activePipe.getActive() ? 1 : 0;
                        return new PipeActivityData(message);
                    }
                }
            }
            return null;
        }
    }
}
