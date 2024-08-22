package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

@SuppressWarnings("deprecation")
public abstract class Packet_New extends GT_Packet {

    public Packet_New(boolean aIsReference) {
        super(aIsReference);
    }

    @Override
    @Deprecated
    public final byte[] encode() {
        final ByteBuf tOut = Unpooled.buffer();
        encode(tOut);
        final byte[] bytes = new byte[tOut.readableBytes()];
        tOut.readBytes(bytes);
        return bytes;
    }

    @Override
    public abstract void encode(ByteBuf aOut);

    @Override
    public abstract Packet_New decode(ByteArrayDataInput aData);
}
