package gregtech.api.net;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.util.GT_MusicSystem;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class GT_Packet_MusicSystemData extends GT_Packet_New {

    ByteBuf storedData;

    public GT_Packet_MusicSystemData() {
        super(true);
    }

    public GT_Packet_MusicSystemData(ByteBuf data) {
        super(false);
        this.storedData = data;
    }

    @Override
    public byte getPacketID() {
        return GT_PacketTypes.MUSIC_SYSTEM_DATA.id;
    }

    @Override
    public void encode(ByteBuf aOut) {
        if (storedData == null) {
            return;
        }
        storedData.markReaderIndex();
        final int len = storedData.readableBytes();
        aOut.writeInt(len);
        aOut.writeBytes(storedData);
        storedData.resetReaderIndex();
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        final int len = aData.readInt();
        final byte[] fullData = new byte[len];
        aData.readFully(fullData);
        return new GT_Packet_MusicSystemData(Unpooled.wrappedBuffer(fullData));
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld == null || storedData == null) {
            return;
        }
        storedData.markReaderIndex();
        GT_MusicSystem.ClientSystem.loadUpdatedSources(storedData);
        storedData.resetReaderIndex();
    }
}
