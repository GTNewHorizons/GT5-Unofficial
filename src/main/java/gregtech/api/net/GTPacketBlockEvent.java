package gregtech.api.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

/**
 * Used to transfer Block Events in a much better fashion
 */
public class GTPacketBlockEvent extends GTPacketNew {

    private int mX, mZ;
    private short mY;
    private byte mID, mValue;

    public GTPacketBlockEvent() {
        super(true);
    }

    public GTPacketBlockEvent(int aX, short aY, int aZ, byte aID, byte aValue) {
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
    public GTPacketNew decode(ByteArrayDataInput aData) {
        return new GTPacketBlockEvent(
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
        return GTPacketTypes.BLOCK_EVENT.id;
    }
}
