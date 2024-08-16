package gregtech.api.net;

import java.util.Arrays;

import gregtech.common.blocks.GT_Packet_Ores;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * Centralized place to keep all the GT packet ID constants
 */
public enum GT_PacketTypes {

    TILE_ENTITY(0, new GT_Packet_TileEntity()),
    SOUND(1, new GT_Packet_Sound()),
    BLOCK_EVENT(2, new GT_Packet_Block_Event()),
    ORES(3, new GT_Packet_Ores()),
    POLLUTION(4, new GT_Packet_Pollution()),
    CLIENT_PREFERENCE(9, new GT_Packet_ClientPreference()),
    SET_CONFIGURATION_CIRCUIT(12, new GT_Packet_SetConfigurationCircuit()),
    UPDATE_ITEM(13, new GT_Packet_UpdateItem()),
    SEND_COVER_DATA(16, new GT_Packet_SendCoverData()),
    REQUEST_COVER_DATA(17, new GT_Packet_RequestCoverData()),
    MULTI_TILE_ENTITY(18, new GT_Packet_MultiTileEntity(true)),
    SEND_OREGEN_PATTERN(19, new GT_Packet_SendOregenPattern()),
    TOOL_SWITCH_MODE(20, new GT_Packet_ToolSwitchMode()),
    MUSIC_SYSTEM_DATA(21, new GT_Packet_MusicSystemData()),
    // merge conflict prevention comment, keep a trailing comma above
    ;

    static {
        // Validate no duplicate IDs
        final GT_PacketTypes[] types = values();
        final Int2ObjectOpenHashMap<GT_Packet_New> foundIds = new Int2ObjectOpenHashMap<>(types.length);
        for (GT_PacketTypes type : types) {
            final GT_Packet_New previous = foundIds.get(type.id);
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
    public final GT_Packet_New referencePacket;

    GT_PacketTypes(int id, GT_Packet_New referencePacket) {
        if (((int) (byte) id) != id) {
            throw new IllegalArgumentException("Value outside of byte normal range: " + id);
        }
        this.id = (byte) id;
        this.referencePacket = referencePacket;
    }

    public static GT_Packet_New[] referencePackets() {
        return Arrays.stream(values())
            .map(p -> p.referencePacket)
            .toArray(GT_Packet_New[]::new);
    }
}
