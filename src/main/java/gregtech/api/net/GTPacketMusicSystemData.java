package gregtech.api.net;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.util.GTMusicSystem;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class GTPacketMusicSystemData extends GTPacketNew {

    ByteBuf storedData;

    public GTPacketMusicSystemData() {
        super(true);
    }

    public GTPacketMusicSystemData(ByteBuf data) {
        super(false);
        this.storedData = data;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.MUSIC_SYSTEM_DATA.id;
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
    public GTPacketNew decode(ByteArrayDataInput aData) {
        final int len = aData.readInt();
        final byte[] fullData = new byte[len];
        aData.readFully(fullData);
        return new GTPacketMusicSystemData(Unpooled.wrappedBuffer(fullData));
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld == null || storedData == null) {
            return;
        }
        storedData.markReaderIndex();
        GTMusicSystem.ClientSystem.loadUpdatedSources(storedData);
        storedData.resetReaderIndex();
    }
}
