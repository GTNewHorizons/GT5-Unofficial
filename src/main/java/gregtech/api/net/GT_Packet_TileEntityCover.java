package gregtech.api.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import io.netty.buffer.ByteBuf;

/**
 * Client -> Server: Update cover data. use this only if you are using the legacy data storage
 */
public class GT_Packet_TileEntityCover extends GT_Packet_New {

    protected int mX;
    protected short mY;
    protected int mZ;

    protected ForgeDirection side;
    protected int coverID, coverData, dimID;

    public GT_Packet_TileEntityCover() {
        super(true);
    }

    public GT_Packet_TileEntityCover(int mX, short mY, int mZ, ForgeDirection coverSide, int coverID, int coverData,
        int dimID) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = coverData;

        this.dimID = dimID;
    }

    public GT_Packet_TileEntityCover(ForgeDirection coverSide, int coverID, int coverData, ICoverable tile) {
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
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);

        aOut.writeByte(side.ordinal());
        aOut.writeInt(coverID);
        aOut.writeInt(coverData);

        aOut.writeInt(dimID);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_TileEntityCover(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            ForgeDirection.getOrientation(aData.readByte()),
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
