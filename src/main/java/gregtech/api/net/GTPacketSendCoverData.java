package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import appeng.api.util.WorldCoord;
import gregtech.GTMod;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.objects.blockupdate.BlockUpdateHandler;
import gregtech.common.covers.Cover;
import io.netty.buffer.ByteBuf;

/**
 * Server -> Client : Update cover data
 */
public class GTPacketSendCoverData extends GTPacket {

    protected int mX;
    protected short mY;
    protected int mZ;
    protected ForgeDirection side;
    protected int coverID;

    protected Cover cover;

    public GTPacketSendCoverData() {
        super();
    }

    public GTPacketSendCoverData(Cover cover, ICoverable tile, ForgeDirection side) {
        super();
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();
        this.coverID = cover.getCoverID();

        this.side = side;

        this.cover = cover;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SEND_COVER_DATA.id;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);
        aOut.writeInt(coverID);
        aOut.writeByte(side.ordinal());

        cover.writeToByteBuf(aOut);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        return new GTPacketDecodeAtProcess(aData, GTPacketSendCoverData::decodeAndProcess);
    }

    public static void decodeAndProcess(ByteArrayDataInput data, IBlockAccess world) {
        if (world == null) return;
        int x = data.readInt();
        int y = data.readShort();
        int z = data.readInt();
        final TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof CoverableTileEntity coverable && !coverable.isDead()) {
            coverable.updateAttachedCover(data);
            if (coverable instanceof IGregTechTileEntity base) {
                base.issueTextureUpdate();
            } else {
                EntityPlayer player = GTMod.GT.getThePlayer();
                if (player == null) return;
                World realWorld = player.getEntityWorld();
                if (GTMod.proxy.mUseBlockUpdateHandler) {
                    BlockUpdateHandler.Instance.enqueueBlockUpdate(realWorld, new WorldCoord(x, y, z));
                } else {
                    realWorld.markBlockForUpdate(x, y, z);
                }
            }
        }
    }

    @Override
    public void process(IBlockAccess aWorld) {
        // Unused, processing happens with decodeAndProcess
    }
}
