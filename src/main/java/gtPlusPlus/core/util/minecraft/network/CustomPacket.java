package gtPlusPlus.core.util.minecraft.network;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import mods.railcraft.common.util.misc.Game;

public abstract class CustomPacket {
	public static final String CHANNEL_NAME = "GTPP";

	public enum PacketType {
        TILE_ENTITY,
	}
	
	public FMLProxyPacket getPacket() {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		try {
			data.writeByte(this.getID());
			this.writeData(data);
		} catch (IOException var4) {
			Game.logThrowable("Error constructing packet: {0}", var4, new Object[]{this.getClass()});
		}
		return new FMLProxyPacket(Unpooled.wrappedBuffer(bytes.toByteArray()), "GTPP");
		}

	public abstract void writeData(DataOutputStream var1) throws IOException;

	public abstract void readData(DataInputStream var1) throws IOException;

	public abstract int getID();

	public String toString() {
		return this.getClass().getSimpleName();
	}
}