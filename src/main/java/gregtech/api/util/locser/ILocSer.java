package gregtech.api.util.locser;

import net.minecraft.network.PacketBuffer;

import io.netty.buffer.Unpooled;

/**
 * LOCalizable and SERializable.
 * This data structure can be serialized and then deserialized after transmission over the network,
 * allowing for customized display methods.
 * Every implementation should be registered in `ILocSerManager`
 *
 * @see ILocSerManager
 */
public interface ILocSer {

    default byte[] encodeToBytes() {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        encode(buffer);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        return data;
    }

    default String encodeToBase64() {
        byte[] data = encodeToBytes();
        return java.util.Base64.getEncoder()
            .encodeToString(data);
    }

    void encode(PacketBuffer out);

    void decode(PacketBuffer in);

    String localize();

    String getId();
}
