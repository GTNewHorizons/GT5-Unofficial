package gregtech.api.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

/**
 * Used to transfer Block Events in a much better fashion
 */
public class Packet_BlockEvent extends Packet_New {

    private int mX, mZ;
    private short mY;
    private byte mID, mValue;

    public Packet_BlockEvent() {
        super(true);
    }

    public Packet_BlockEvent(int aX, short aY, int aZ, byte aID, byte aValue) {
        super(false);
        mX = aX;
        mY = aY;
        mZ = aZ;
        mID = aID;
        mValue = aValue;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);
        aOut.writeByte(mID);
        aOut.writeByte(mValue);
    }

    @Override
    public Packet_New decode(ByteArrayDataInput aData) {
        return new Packet_BlockEvent(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            aData.readByte(),
            aData.readByte());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld != null) {
            final TileEntity tTileEntity = aWorld.getTileEntity(mX, mY, mZ);
            if (tTileEntity != null) tTileEntity.receiveClientEvent(mID, mValue);
        }
    }

    @Override
    public byte getPacketID() {
        return PacketTypes.BLOCK_EVENT.id;
    }
}
