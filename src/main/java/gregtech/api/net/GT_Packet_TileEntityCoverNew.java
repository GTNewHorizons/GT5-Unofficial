package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;

/**
 * Client -> Server: Update cover data
 */
public class GT_Packet_TileEntityCoverNew extends GT_Packet_New {

    protected int mX;
    protected short mY;
    protected int mZ;

    protected ForgeDirection side;
    protected int coverID, dimID;
    protected ISerializableObject coverData;

    protected EntityPlayerMP mPlayer;

    public GT_Packet_TileEntityCoverNew() {
        super(true);
    }

    public GT_Packet_TileEntityCoverNew(int mX, short mY, int mZ, ForgeDirection coverSide, int coverID,
        ISerializableObject coverData, int dimID) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = coverData;

        this.dimID = dimID;
    }

    public GT_Packet_TileEntityCoverNew(ForgeDirection coverSide, int coverID, ISerializableObject coverData,
        ICoverable tile) {
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
        return GT_PacketTypes.COVER_NEW.id;
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            mPlayer = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);

        aOut.writeByte(side.ordinal());
        aOut.writeInt(coverID);
        coverData.writeToByteBuf(aOut);

        aOut.writeInt(dimID);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        int coverId;
        return new GT_Packet_TileEntityCoverNew(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            ForgeDirection.getOrientation(aData.readByte()),
            coverId = aData.readInt(),
            GregTech_API.getCoverBehaviorNew(coverId)
                .createDataObject()
                .readFromPacket(aData, mPlayer),
            aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (mPlayer == null) // impossible, but who knows
            return;
        World world = DimensionManager.getWorld(dimID);
        if (world != null) {
            TileEntity tile = world.getTileEntity(mX, mY, mZ);
            if (tile instanceof IGregTechTileEntity && !((IGregTechTileEntity) tile).isDead()) {
                ((IGregTechTileEntity) tile).receiveCoverData(side, coverID, coverData, mPlayer);
            }
        }
    }
}
