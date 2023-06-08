package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverInfo;
import io.netty.buffer.ByteBuf;

/**
 * Server -> Client: Show GUI
 */
public class GT_Packet_TileEntityCoverGUI extends GT_Packet_New {

    protected int mX;
    protected short mY;
    protected int mZ;

    protected ForgeDirection side;
    protected int coverID, dimID, playerID;
    protected ISerializableObject coverData;

    protected int parentGuiId;

    public GT_Packet_TileEntityCoverGUI() {
        super(true);
    }

    public GT_Packet_TileEntityCoverGUI(int mX, short mY, int mZ, ForgeDirection coverSide, int coverID, int coverData,
        int dimID, int playerID) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = new ISerializableObject.LegacyCoverData(coverData);

        this.dimID = dimID;
        this.playerID = playerID;
        this.parentGuiId = -1;
    }

    public GT_Packet_TileEntityCoverGUI(int mX, short mY, int mZ, ForgeDirection coverSide, int coverID,
        ISerializableObject coverData, int dimID, int playerID) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = coverData;
        this.dimID = dimID;
        this.playerID = playerID;
        this.parentGuiId = -1;
    }

    public GT_Packet_TileEntityCoverGUI(CoverInfo coverInfo, int dimID, int playerID, int parentGuiId) {
        super(false);
        final ICoverable tile = coverInfo.getTile();
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = coverInfo.getSide();
        this.coverID = coverInfo.getCoverID();
        this.coverData = coverInfo.getCoverData();

        this.dimID = dimID;
        this.playerID = playerID;
        this.parentGuiId = parentGuiId;
    }

    public GT_Packet_TileEntityCoverGUI(int mX, short mY, int mZ, ForgeDirection coverSide, int coverID,
        ISerializableObject coverData, int dimID, int playerID, int parentGuiId) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = coverData;
        this.dimID = dimID;
        this.playerID = playerID;
        this.parentGuiId = parentGuiId;
    }

    public GT_Packet_TileEntityCoverGUI(ForgeDirection side, int coverID, int coverData, ICoverable tile,
        EntityPlayerMP aPlayer) {
        super(false);

        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = side;
        this.coverID = coverID;
        this.coverData = new ISerializableObject.LegacyCoverData(coverData);

        this.dimID = tile.getWorld().provider.dimensionId;
        this.playerID = aPlayer.getEntityId();
        this.parentGuiId = -1;
    }

    public GT_Packet_TileEntityCoverGUI(ForgeDirection coverSide, int coverID, int coverData,
        IGregTechTileEntity tile) {
        super(false);
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = new ISerializableObject.LegacyCoverData(coverData);

        this.dimID = tile.getWorld().provider.dimensionId;
        this.parentGuiId = -1;
    }

    public GT_Packet_TileEntityCoverGUI(ForgeDirection side, int coverID, ISerializableObject coverData,
        ICoverable tile, EntityPlayerMP aPlayer) {
        super(false);
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = side;
        this.coverID = coverID;
        this.coverData = coverData.copy(); // make a copy so we don't get a race condition

        this.dimID = tile.getWorld().provider.dimensionId;
        this.playerID = aPlayer.getEntityId();
        this.parentGuiId = -1;
    }

    @Override
    public byte getPacketID() {
        return 7;
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
        aOut.writeInt(playerID);

        aOut.writeInt(parentGuiId);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        final int coverID;
        return new GT_Packet_TileEntityCoverGUI(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            ForgeDirection.getOrientation(aData.readByte()),
            coverID = aData.readInt(),
            GregTech_API.getCoverBehaviorNew(coverID)
                .createDataObject()
                .readFromPacket(aData, null),
            aData.readInt(),
            aData.readInt(),
            aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld instanceof World) {
            // Using EntityPlayer instead of EntityClientPlayerMP so both client and server can load this
            final EntityPlayer thePlayer = ((EntityPlayer) ((World) aWorld).getEntityByID(playerID));
            final TileEntity tile = aWorld.getTileEntity(mX, mY, mZ);
            if (tile instanceof IGregTechTileEntity gtTile && !gtTile.isDead()) {
                gtTile.setCoverDataAtSide(side, coverData); // Set it client side to read later.
            }
        }
    }
}
