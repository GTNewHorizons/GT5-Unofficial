package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.GT_Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Server -> Client: Show GUI
 */

public class GT_Packet_TileEntityCoverGUI extends GT_Packet {
    protected int mX;
    protected short mY;
    protected int mZ;

    protected byte side;
    protected int coverID, coverData, dimID, playerID;

    public GT_Packet_TileEntityCoverGUI() {
        super(true);
    }

    public GT_Packet_TileEntityCoverGUI(int mX, short mY, int mZ, byte coverSide, int coverID, int coverData, int dimID, int playerID) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.side = coverSide;
        this.coverID = coverID;
        this.coverData = coverData;

        this.dimID = dimID;
        this.playerID = playerID;
    }


    public GT_Packet_TileEntityCoverGUI(byte side, int coverID, int coverData, ICoverable tile, EntityPlayerMP aPlayer) {
        super(false);

        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = side;
        this.coverID = coverID;
        this.coverData = coverData;

        this.dimID = tile.getWorld().provider.dimensionId;
        this.playerID = aPlayer.getEntityId();
    }

    public GT_Packet_TileEntityCoverGUI(byte coverSide, int coverID, int coverData, IGregTechTileEntity tile) {
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
        return 7;
    }

    @Override
    public byte[] encode() {
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput(4+2+4+1+4+4+4+4);
        tOut.writeInt(mX);
        tOut.writeShort(mY);
        tOut.writeInt(mZ);

        tOut.writeByte(side);
        tOut.writeInt(coverID);
        tOut.writeInt(coverData);

        tOut.writeInt(dimID);
        tOut.writeInt(playerID);

        return tOut.toByteArray();
    }

    @Override
    public GT_Packet decode(ByteArrayDataInput aData) {
        return new GT_Packet_TileEntityCoverGUI(
                aData.readInt(),
                aData.readShort(),
                aData.readInt(),

                aData.readByte(),
                aData.readInt(),
                aData.readInt(),

                aData.readInt(),
                aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld instanceof World) {
            EntityClientPlayerMP a = Minecraft.getMinecraft().thePlayer;
            TileEntity tile = aWorld.getTileEntity(mX, mY, mZ);
            if (tile instanceof IGregTechTileEntity && !((IGregTechTileEntity) tile).isDead()) {

                ((IGregTechTileEntity) tile).setCoverDataAtSide(side, coverData); //Set it client side to read later.
                a.openGui(GT_Values.GT, GT_Proxy.GUI_ID_COVER_SIDE_BASE + side, a.worldObj, mX, mY, mZ);
            }
        }
    }
}