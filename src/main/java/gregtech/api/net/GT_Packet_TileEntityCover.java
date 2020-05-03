package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Client -> Server: Update cover data
 */

public class GT_Packet_TileEntityCover extends GT_Packet {
    protected int mX;
    protected short mY;
    protected int mZ;

    protected byte side;
    protected int coverID, coverData, dimID;

    public GT_Packet_TileEntityCover() {
        super(true);
    }

    public GT_Packet_TileEntityCover(int mX, short mY, int mZ, byte coverSide, int coverID, int coverData, int dimID) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = coverData;

        this.dimID = dimID;
    }
    public GT_Packet_TileEntityCover(byte coverSide, int coverID, int coverData, ICoverable tile) {
        super(false);
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = coverData;

        this.dimID = tile.getWorld().provider.dimensionId;
    }

    @Override
    public byte getPacketID() {
        return 6;
    }

    @Override
    public byte[] encode() {
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput(4+2+4+1+4+4+4);
        tOut.writeInt(mX);
        tOut.writeShort(mY);
        tOut.writeInt(mZ);

        tOut.writeByte(side);
        tOut.writeInt(coverID);
        tOut.writeInt(coverData);

        tOut.writeInt(dimID);

        return tOut.toByteArray();
    }

    @Override
    public GT_Packet decode(ByteArrayDataInput aData) {
        return new GT_Packet_TileEntityCover(
                aData.readInt(),
                aData.readShort(),
                aData.readInt(),

                aData.readByte(),
                aData.readInt(),
                aData.readInt(),

                aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        World world = DimensionManager.getWorld(dimID);
        if (world != null) {
            TileEntity tile = world.getTileEntity(mX, mY, mZ);
            if (tile instanceof IGregTechTileEntity && !((IGregTechTileEntity) tile).isDead()) {
                ((IGregTechTileEntity) tile).receiveCoverData(side, coverID, coverData);
            }
        }
    }
}