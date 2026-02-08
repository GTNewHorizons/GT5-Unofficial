package gregtech.api.net.cape;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketTypes;
import gregtech.client.GTCapesClientHandler;
import io.netty.buffer.ByteBuf;

/**
 * Sent from the server to the client, informing it about what cape one or multiple players selected.
 */
@ParametersAreNonnullByDefault
public class GTPacketBroadcastCapes extends GTPacket {

    private Map<UUID, String> capes;

    public GTPacketBroadcastCapes() {}

    public GTPacketBroadcastCapes(UUID uuid, String cape) {
        this(Collections.singletonMap(uuid, cape));
    }

    public GTPacketBroadcastCapes(Map<UUID, String> capes) {
        this.capes = capes;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.BROADCAST_CAPES.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(this.capes.size());
        for (Entry<UUID, String> p : this.capes.entrySet()) {
            UUID uuid = p.getKey();
            buffer.writeLong(uuid.getMostSignificantBits());
            buffer.writeLong(uuid.getLeastSignificantBits());
            ByteBufUtils.writeUTF8String(buffer, p.getValue());
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        int num = buffer.readInt();
        Map<UUID, String> capes = new HashMap<>();

        for (int i = 0; i < num; i++) {
            capes.put(new UUID(buffer.readLong(), buffer.readLong()), readUTF8String(buffer));
        }

        return new GTPacketBroadcastCapes(capes);
    }

    // executed on the client
    @Override
    public void process(IBlockAccess world) {
        GTCapesClientHandler.setCapes(this.capes);
    }
}
