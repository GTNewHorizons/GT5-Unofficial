package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.common.GT_Proxy;
import io.netty.buffer.ByteBuf;

/**
 * Client -> Server: Request that the server opens a Gregtech GUI for us after providing us with the required data.
 */
public class GT_Packet_GtTileEntityGuiRequest extends GT_Packet_New {

    protected int mX;
    protected short mY;
    protected int mZ;

    protected int guiId;
    protected int dimId, playerId;

    protected int parentGuiId;

    public GT_Packet_GtTileEntityGuiRequest() {
        super(true);
    }

    public GT_Packet_GtTileEntityGuiRequest(int mX, short mY, int mZ, int guiId, int dimID, int playerID,
        int parentGuiId) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.guiId = guiId;

        this.dimId = dimID;
        this.playerId = playerID;

        this.parentGuiId = parentGuiId;
    }

    public GT_Packet_GtTileEntityGuiRequest(int mX, short mY, int mZ, int guiId, int dimID, int playerID) {
        this(mX, mY, mZ, guiId, dimID, playerID, -1);
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);

        aOut.writeInt(guiId);

        aOut.writeInt(dimId);
        aOut.writeInt(playerId);

        aOut.writeInt(parentGuiId);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_GtTileEntityGuiRequest(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt());
    }

    @Override
    public byte getPacketID() {
        return 15;
    }

    @Override
    public void process(IBlockAccess aWorld) {
        final World world = DimensionManager.getWorld(this.dimId);
        if (world == null) return;
        final TileEntity tile = world.getTileEntity(this.mX, this.mY, this.mZ);
        if (!(tile instanceof BaseTileEntity baseTile) || baseTile.isDead()) return;

        final EntityPlayerMP player = (EntityPlayerMP) world.getEntityByID(playerId);
        final CoverableTileEntity coverableTile = (baseTile instanceof CoverableTileEntity)
            ? (CoverableTileEntity) baseTile
            : null;
        // If the requested Gui ID corresponds to a cover, send the cover data to the client so they can open it.
        if (GT_Proxy.GUI_ID_COVER_SIDE_BASE <= guiId && guiId < GT_Proxy.GUI_ID_COVER_SIDE_BASE + 6
            && coverableTile != null) {
            final ForgeDirection coverSide = ForgeDirection
                .getOrientation((byte) (guiId - GT_Proxy.GUI_ID_COVER_SIDE_BASE));
            final GT_Packet_TileEntityCoverGUI packet = new GT_Packet_TileEntityCoverGUI(
                this.mX,
                this.mY,
                this.mZ,
                coverSide,
                coverableTile.getCoverIDAtSide(coverSide),
                coverableTile.getComplexCoverDataAtSide(coverSide),
                this.dimId,
                this.playerId,
                parentGuiId);
            GT_Values.NW.sendToPlayer(packet, player);
        } else if (guiId == 0) {
            if (baseTile.useModularUI()) {
                GT_UIInfos.openGTTileEntityUI(baseTile, player);
            } else {
                baseTile.openGUI(player);
            }
        }
    }
}
