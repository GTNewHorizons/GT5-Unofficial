package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.GregTech_API;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Server -> Client: Show GUI
 */
public class GT_Packet_TileEntityCoverGUI extends GT_Packet_New {
    protected int mX;
    protected short mY;
    protected int mZ;

    protected byte side;
    protected int coverID, dimID, playerID;
    protected ISerializableObject coverData;

    protected int parentGuiId;

    public GT_Packet_TileEntityCoverGUI() {
        super(true);
    }

    public GT_Packet_TileEntityCoverGUI(int mX, short mY, int mZ, byte coverSide, int coverID, int coverData, int dimID,
            int playerID) {
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

    public GT_Packet_TileEntityCoverGUI(int mX, short mY, int mZ, byte coverSide, int coverID,
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

    public GT_Packet_TileEntityCoverGUI(int mX, short mY, int mZ, byte coverSide, int coverID,
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


    public GT_Packet_TileEntityCoverGUI(byte side, int coverID, int coverData, ICoverable tile, EntityPlayerMP aPlayer) {
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

    public GT_Packet_TileEntityCoverGUI(byte coverSide, int coverID, int coverData, IGregTechTileEntity tile) {
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

    public GT_Packet_TileEntityCoverGUI(byte side, int coverID, ISerializableObject coverData, ICoverable tile,
            EntityPlayerMP aPlayer) {
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

        aOut.writeByte(side);
        aOut.writeInt(coverID);
        coverData.writeToByteBuf(aOut);

        aOut.writeInt(dimID);
        aOut.writeInt(playerID);

        aOut.writeInt(parentGuiId);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        int coverID;
        return new GT_Packet_TileEntityCoverGUI(
                aData.readInt(),
                aData.readShort(),
                aData.readInt(),

                aData.readByte(),
                coverID = aData.readInt(),
                GregTech_API.getCoverBehaviorNew(coverID).createDataObject().readFromPacket(aData, null),

                aData.readInt(),
                aData.readInt(),

                aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld instanceof World) {
            // Using EntityPlayer instead of EntityClientPlayerMP so both client and server can load this
            EntityPlayer thePlayer = ((EntityPlayer) ((World)aWorld).getEntityByID(playerID));
            TileEntity tile = aWorld.getTileEntity(mX, mY, mZ);
            if (tile instanceof IGregTechTileEntity && !((IGregTechTileEntity) tile).isDead()) {
                IGregTechTileEntity gtTile = ((IGregTechTileEntity) tile);
                gtTile.setCoverDataAtSide(side, coverData); //Set it client side to read later.
                
                GuiScreen gui = (GuiScreen) getCoverGUI(side, thePlayer, thePlayer.worldObj, gtTile);
                // If it's one of this mod's covers, tell it to exit to the GUI with the specified ID (-1 is ignored)
                if (gui instanceof GT_GUICover) {
                    ((GT_GUICover) gui).setParentGuiId(parentGuiId);
                }
                Minecraft.getMinecraft().displayGuiScreen(gui);
            }
        }
    }

    /**
     * Gets the specified cover's GUI object, if one exists
     * @param aSide Block side (0 through 5)
     * @param aPlayer Current player
     * @param aWorld Current world
     * @param aGtTile IGregTechTileEntity instance
     * @return The specified cover's GUI, if one exists
     */
    private Object getCoverGUI(byte aSide, EntityPlayer aPlayer, World aWorld, IGregTechTileEntity aGtTile) {
        GT_CoverBehaviorBase<?> cover = aGtTile.getCoverBehaviorAtSideNew(aSide);
        if (cover.hasCoverGUI()) {
            return cover.getClientGUI(
                aSide, aGtTile.getCoverIDAtSide(aSide), aGtTile.getComplexCoverDataAtSide(aSide), aGtTile, aPlayer, aWorld);
        }
        return null;
    }
}
