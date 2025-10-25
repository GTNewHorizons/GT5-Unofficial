package gregtech.api.net;

import java.nio.charset.StandardCharsets;

import net.minecraft.network.INetHandler;
import net.minecraft.world.IBlockAccess;

import org.apache.commons.lang3.Validate;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;

public abstract class GTPacket {

    public GTPacket() {}

    /**
     * Unique ID of this packet.
     */
    public abstract byte getPacketID();

    /**
     * Encode the data into given byte buffer.
     */
    public abstract void encode(ByteBuf buffer);

    /**
     * Decode byte buffer into packet object.
     */
    public abstract GTPacket decode(ByteArrayDataInput buffer);

    /**
     * Process the received packet.
     *
     * @param world null if message is received on server side, the client world if message is received on client side
     */
    public abstract void process(IBlockAccess world);

    /**
     * This will be called just before {@link #process(IBlockAccess)} to inform the handler about the source and type of
     * connection.
     */
    public void setINetHandler(INetHandler handler) {}

    /**
     * Read a varint from the supplied byte array data input.
     * 
     * @param buf     The data input to read from
     * @param maxSize The maximum length of bytes to read
     * @return The integer
     * @see ByteBufUtils#readVarInt(ByteBuf, int)
     */
    public static int readVarInt(ByteArrayDataInput buf, int maxSize) {
        Validate.isTrue(maxSize < 6 && maxSize > 0, "Varint length is between 1 and 5, not %d", maxSize);
        int i = 0;
        int j = 0;
        byte b0;

        do {
            b0 = buf.readByte();
            i |= (b0 & 127) << j++ * 7;

            if (j > maxSize) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    /**
     * An extended length short. Used by custom payload packets to extend size.
     *
     * @param buf The data input to read from
     * @return The short
     * @see ByteBufUtils#readVarShort(ByteBuf)
     */
    public static int readVarShort(ByteArrayDataInput buf) {
        int low = buf.readUnsignedShort();
        int high = 0;
        if ((low & 0x8000) != 0) {
            low = low & 0x7FFF;
            high = buf.readUnsignedByte();
        }
        return ((high & 0xFF) << 15) | low;
    }

    /**
     * Read a UTF8 string from the byte array data input.
     * It is encoded as &lt;varint length&gt;[&lt;UTF8 char bytes&gt;]
     *
     * @param from The data input to read from
     * @return The string
     * @see ByteBufUtils#readUTF8String(ByteBuf)
     */
    public static String readUTF8String(ByteArrayDataInput from) {
        byte[] utf8Bytes = new byte[readVarInt(from, 2)];
        from.readFully(utf8Bytes);
        return new String(utf8Bytes, StandardCharsets.UTF_8);
    }
}
