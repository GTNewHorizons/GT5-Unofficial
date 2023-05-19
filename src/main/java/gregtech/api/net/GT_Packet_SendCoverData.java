package gregtech.api.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverInfo;
import io.netty.buffer.ByteBuf;

/**
 * Server -> Client : Update cover data
 */
public class GT_Packet_SendCoverData extends GT_Packet_New {

    protected int mX;
    protected short mY;
    protected int mZ;

    protected ForgeDirection side;
    protected int coverID;
    protected ISerializableObject coverData;

    public GT_Packet_SendCoverData() {
        super(true);
    }

    public GT_Packet_SendCoverData(int mX, short mY, int mZ, ForgeDirection coverSide, int coverID,
        ISerializableObject coverData) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = coverData;
    }

    public GT_Packet_SendCoverData(CoverInfo info, ICoverable tile) {
        super(false);
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = info.getSide();
        this.coverID = info.getCoverID();
        this.coverData = info.getCoverData();
    }

    public GT_Packet_SendCoverData(ForgeDirection coverSide, int coverID, ISerializableObject coverData,
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
        return 16;
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
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        final int coverId;
        return new GT_Packet_SendCoverData(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            ForgeDirection.getOrientation(aData.readByte()),
            coverId = aData.readInt(),
            GregTech_API.getCoverBehaviorNew(coverId)
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
