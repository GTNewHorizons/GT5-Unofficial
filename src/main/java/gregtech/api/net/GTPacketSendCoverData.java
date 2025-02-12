package gregtech.api.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.common.covers.CoverInfo;
import io.netty.buffer.ByteBuf;

/**
 * Server -> Client : Update cover data
 */
public class GTPacketSendCoverData extends GTPacket {

    protected int mX;
    protected short mY;
    protected int mZ;
    protected int coverID;

    protected CoverInfo info;

    public GTPacketSendCoverData() {
        super();
    }

    public GTPacketSendCoverData(CoverInfo info, ICoverable tile) {
        super();
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();
        this.coverID = info.getCoverID();

        this.info = info;
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

        info.writeToByteBuf(aOut);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        return new GTPacketDecodeAtProcess(aData, GTPacketSendCoverData::decodeAndProcess);
    }

    public static void decodeAndProcess(ByteArrayDataInput data, IBlockAccess world) {
        if (world == null) return;
        final TileEntity tile = world.getTileEntity(data.readInt(), data.readShort(), data.readInt());
        if (tile instanceof CoverableTileEntity coverable && !coverable.isDead()) {
            int coverId = data.readInt();
            CoverInfo cover = CoverRegistry.getRegistration(coverId)
                .buildCover(coverable, data);
            coverable.updateCover(cover, cover.getSide());
        }
    }

    @Override
    public void process(IBlockAccess aWorld) {
        // Unused, processing happens with decodeAndProcess
    }
}
