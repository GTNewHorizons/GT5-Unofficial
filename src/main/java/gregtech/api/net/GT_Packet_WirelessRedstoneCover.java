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
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import io.netty.buffer.ByteBuf;

public class GT_Packet_WirelessRedstoneCover extends GT_Packet_TileEntityCover {

    private static final int PRIVATE_MASK = 0xFFFE0000;
    private static final int PUBLIC_MASK = 0x0000FFFF;
    private static final int CHECKBOX_MASK = 0x00010000;

    private EntityPlayerMP mPlayer;
    private int mPublicChannel;
    private int mCheckBoxValue;

    public GT_Packet_WirelessRedstoneCover() {
        super();
    }

    public GT_Packet_WirelessRedstoneCover(int mX, short mY, int mZ, ForgeDirection coverSide, int coverID, int dimID,
        int publicChannel, int checkBoxValue) {
        super(mX, mY, mZ, coverSide, coverID, 0, dimID);
        mPublicChannel = publicChannel;
        mCheckBoxValue = checkBoxValue;
    }

    public GT_Packet_WirelessRedstoneCover(ForgeDirection coverSide, int coverID, ICoverable tile, int publicChannel,
        int checkBoxValue) {
        super(coverSide, coverID, 0, tile);
        mPublicChannel = publicChannel;
        mCheckBoxValue = checkBoxValue;
    }

    @Override
    public byte getPacketID() {
        return 10;
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

        aOut.writeInt(dimID);

        aOut.writeInt(mPublicChannel);
        aOut.writeInt(mCheckBoxValue);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_WirelessRedstoneCover(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            ForgeDirection.getOrientation(aData.readByte()),
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        World world = DimensionManager.getWorld(dimID);
        if (world != null && world.blockExists(mX, mY, mZ)) {
            TileEntity tile = world.getTileEntity(mX, mY, mZ);
            if (tile instanceof IGregTechTileEntity && !((IGregTechTileEntity) tile).isDead()) {
                int tPrivateChannel = (mCheckBoxValue > 0) ? mPlayer.getUniqueID()
                    .hashCode() & PRIVATE_MASK : 0;
                int tCoverData = tPrivateChannel | (mCheckBoxValue & CHECKBOX_MASK) | (mPublicChannel & PUBLIC_MASK);
                ((IGregTechTileEntity) tile).receiveCoverData(side, coverID, tCoverData);
            }
        }
    }
}
