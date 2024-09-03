package tectech.mechanics.pipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import eu.usrv.yamcore.network.client.AbstractClientMessageHandler;
import eu.usrv.yamcore.network.server.AbstractServerMessageHandler;
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
    public void fromBytes(ByteBuf pBuffer) {
        NBTTagCompound tTag = ByteBufUtils.readTag(pBuffer);
        mPosX = tTag.getInteger("posx");
        mPosY = tTag.getInteger("posy");
        mPosZ = tTag.getInteger("posz");
        mPosD = tTag.getInteger("posd");
        mActive = tTag.getInteger("active");
    }

    @Override
    public void toBytes(ByteBuf pBuffer) {
        NBTTagCompound tFXTag = new NBTTagCompound();
        tFXTag.setInteger("posx", mPosX);
        tFXTag.setInteger("posy", mPosY);
        tFXTag.setInteger("posz", mPosZ);
        tFXTag.setInteger("posd", mPosD);
        tFXTag.setInteger("active", mActive);

        ByteBufUtils.writeTag(pBuffer, tFXTag);
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

    public static class ClientHandler extends AbstractClientMessageHandler<PipeActivityData> {

        @Override
        public IMessage handleClientMessage(EntityPlayer pPlayer, PipeActivityData pMessage, MessageContext pCtx) {
            if (pPlayer.worldObj.provider.dimensionId == pMessage.mPosD) {
                TileEntity te = pPlayer.worldObj.getTileEntity(pMessage.mPosX, pMessage.mPosY, pMessage.mPosZ);
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (meta instanceof IActivePipe) {
                        ((IActivePipe) meta).setActive(pMessage.mActive == 1);
                    }
                }
            }
            return null;
        }
    }

    public static class ServerHandler extends AbstractServerMessageHandler<PipeActivityQuery> {

        @Override
        public IMessage handleServerMessage(EntityPlayer pPlayer, PipeActivityQuery pMessage, MessageContext pCtx) {
            World world = DimensionManager.getWorld(pMessage.mPosD);
            if (world != null) {
                TileEntity te = world.getTileEntity(pMessage.mPosX, pMessage.mPosY, pMessage.mPosZ);
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (meta instanceof IActivePipe) {
                        pMessage.mActive = ((IActivePipe) meta).getActive() ? 1 : 0;
                        return new PipeActivityData(pMessage);
                    }
                }
            }
            return null;
        }
    }
}
