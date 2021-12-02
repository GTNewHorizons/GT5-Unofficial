package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

@SuppressWarnings("deprecation")
public abstract class GT_Packet_New extends GT_Packet {
	public GT_Packet_New(boolean aIsReference) {
		super(aIsReference);
	}

	@Override
	@Deprecated
	public final byte[] encode() {
		ByteBuf tOut = Unpooled.buffer();
		encode(tOut);
		byte[] bytes = new byte[tOut.readableBytes()];
		tOut.readBytes(bytes);
		return bytes;
	}

	@Override
	public abstract void encode(ByteBuf aOut);

	@Override
	public abstract GT_Packet_New decode(ByteArrayDataInput aData);
}
