package gregtech.api.net;

import java.util.Arrays;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * Centralized place to keep all the GT packet ID constants
 */
public enum PacketTypes {

    TILE_ENTITY(0, new Packet_TileEntity()),
    SOUND(1, new Packet_Sound()),
    BLOCK_EVENT(2, new Packet_BlockEvent()),
    ORES(3, new Packet_Ores()),
    POLLUTION(4, new Packet_Pollution()),
    CLIENT_PREFERENCE(9, new Packet_ClientPreference()),
    SET_CONFIGURATION_CIRCUIT(12, new Packet_SetConfigurationCircuit()),
    UPDATE_ITEM(13, new Packet_UpdateItem()),
    SEND_COVER_DATA(16, new Packet_SendCoverData()),
    REQUEST_COVER_DATA(17, new Packet_RequestCoverData()),
    MULTI_TILE_ENTITY(18, new Packet_MultiTileEntity(true)),
    SEND_OREGEN_PATTERN(19, new Packet_SendOregenPattern()),
    TOOL_SWITCH_MODE(20, new Packet_ToolSwitchMode()),
    MUSIC_SYSTEM_DATA(21, new Packet_MusicSystemData()),
    // merge conflict prevention comment, keep a trailing comma above
    ;

    static {
        // Validate no duplicate IDs
        final PacketTypes[] types = values();
        final Int2ObjectOpenHashMap<Packet_New> foundIds = new Int2ObjectOpenHashMap<>(types.length);
        for (PacketTypes type : types) {
            final Packet_New previous = foundIds.get(type.id);
            if (previous != null) {
                throw new IllegalStateException(
                    "Duplicate packet IDs defined: " + type.id
                        + " for "
                        + type.getClass()
                        + " and "
                        + previous.getClass());
            }
            foundIds.put(type.id, type.referencePacket);
        }
    }

    public final byte id;
    public final Packet_New referencePacket;

    PacketTypes(int id, Packet_New referencePacket) {
        if (((int) (byte) id) != id) {
            throw new IllegalArgumentException("Value outside of byte normal range: " + id);
        }
        this.id = (byte) id;
        this.referencePacket = referencePacket;
    }

    public static Packet_New[] referencePackets() {
        return Arrays.stream(values())
            .map(p -> p.referencePacket)
            .toArray(Packet_New[]::new);
    }
}
