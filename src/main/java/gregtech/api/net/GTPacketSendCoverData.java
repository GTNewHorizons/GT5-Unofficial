package gregtech.api.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverInfo;
import io.netty.buffer.ByteBuf;

/**
 * Server -> Client : Update cover data
 */
public class GTPacketSendCoverData extends GTPacketNew {

    protected int mX;
    protected short mY;
    protected int mZ;

    protected ForgeDirection side;
    protected int coverID;
    protected ISerializableObject coverData;

    public GTPacketSendCoverData() {
        super(true);
    }

    public GTPacketSendCoverData(int mX, short mY, int mZ, ForgeDirection coverSide, int coverID,
        ISerializableObject coverData) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = coverData;
    }

    public GTPacketSendCoverData(CoverInfo info, ICoverable tile) {
        super(false);
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = info.getSide();
        this.coverID = info.getCoverID();
        this.coverData = info.getCoverData();
    }

    public GTPacketSendCoverData(ForgeDirection coverSide, int coverID, ISerializableObject coverData,
        ICoverable tile) {
        super(false);
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = coverData;
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

        aOut.writeByte(side.ordinal());
        aOut.writeInt(coverID);
        coverData.writeToByteBuf(aOut);
    }

    @Override
    public GTPacketNew decode(ByteArrayDataInput aData) {
        final int coverId;
        return new GTPacketSendCoverData(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            ForgeDirection.getOrientation(aData.readByte()),
            coverId = aData.readInt(),
            GregTechAPI.getCoverBehaviorNew(coverId)
                .createDataObject()
                .readFromPacket(aData, null));
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld != null) {
            final TileEntity tile = aWorld.getTileEntity(mX, mY, mZ);
            if (tile instanceof CoverableTileEntity coverable && !coverable.isDead()) {
                coverable.receiveCoverData(side, coverID, coverData, null);
            }
        }
    }
}
