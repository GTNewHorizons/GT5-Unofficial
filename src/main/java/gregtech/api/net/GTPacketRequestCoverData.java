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

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.common.covers.CoverInfo;
import io.netty.buffer.ByteBuf;

/**
 * Client -> Server : ask for cover data
 */
public class GTPacketRequestCoverData extends GTPacketNew {

    protected int mX;
    protected short mY;
    protected int mZ;

    protected ForgeDirection side;
    protected int coverID;

    protected EntityPlayerMP mPlayer;

    public GTPacketRequestCoverData() {
        super(true);
    }

    public GTPacketRequestCoverData(CoverInfo info, ICoverable tile) {
        super(false);
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = info.getSide();
        this.coverID = info.getCoverID();
    }

    public GTPacketRequestCoverData(int mX, short mY, int mZ, ForgeDirection coverSide, int coverID) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.side = coverSide;
        this.coverID = coverID;
    }

    public GTPacketRequestCoverData(ForgeDirection coverSide, int coverID, ICoverable tile) {
        super(false);
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.side = coverSide;
        this.coverID = coverID;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.REQUEST_COVER_DATA.id;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);

        aOut.writeByte(side.ordinal());
        aOut.writeInt(coverID);
    }

    @Override
    public GTPacketNew decode(ByteArrayDataInput aData) {
        return new GTPacketRequestCoverData(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            ForgeDirection.getOrientation(aData.readByte()),
            aData.readInt());
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            mPlayer = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }

    @Override
    public void process(IBlockAccess aWorld) {
        // impossible, but who knows
        if (mPlayer == null) return;
        final World world = DimensionManager.getWorld(mPlayer.dimension);
        if (world != null) {
            final TileEntity tile = world.getTileEntity(mX, mY, mZ);
            if (tile instanceof CoverableTileEntity te) {
                if (!te.isDead() && te.getCoverIDAtSide(side) == coverID) {
                    te.issueCoverUpdate(side);
                }
            }
        }
    }
}
