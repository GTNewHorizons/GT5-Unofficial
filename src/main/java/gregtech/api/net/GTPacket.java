package gregtech.api.net;

import net.minecraft.network.INetHandler;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

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
}
