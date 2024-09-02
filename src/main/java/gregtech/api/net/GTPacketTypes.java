package gregtech.api.net;

import java.util.Arrays;

import gregtech.common.blocks.PacketOres;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * Centralized place to keep all the GT packet ID constants
 */
public enum GTPacketTypes {

    TILE_ENTITY(0, new GTPacketTileEntity()),
    SOUND(1, new GTPacketSound()),
    BLOCK_EVENT(2, new GTPacketBlockEvent()),
    ORES(3, new PacketOres()),
    POLLUTION(4, new GTPacketPollution()),
    CLIENT_PREFERENCE(9, new GTPacketClientPreference()),
    SET_CONFIGURATION_CIRCUIT(12, new GTPacketSetConfigurationCircuit()),
    UPDATE_ITEM(13, new GTPacketUpdateItem()),
    SEND_COVER_DATA(16, new GTPacketSendCoverData()),
    REQUEST_COVER_DATA(17, new GTPacketRequestCoverData()),
    MULTI_TILE_ENTITY(18, new GTPacketMultiTileEntity(true)),
    SEND_OREGEN_PATTERN(19, new GTPacketSendOregenPattern()),
    TOOL_SWITCH_MODE(20, new GTPacketToolSwitchMode()),
    MUSIC_SYSTEM_DATA(21, new GTPacketMusicSystemData()),
    // merge conflict prevention comment, keep a trailing comma above
    ;

    static {
        // Validate no duplicate IDs
        final GTPacketTypes[] types = values();
        final Int2ObjectOpenHashMap<GTPacketNew> foundIds = new Int2ObjectOpenHashMap<>(types.length);
        for (GTPacketTypes type : types) {
            final GTPacketNew previous = foundIds.get(type.id);
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
    public final GTPacketNew referencePacket;

    GTPacketTypes(int id, GTPacketNew referencePacket) {
        if (((int) (byte) id) != id) {
            throw new IllegalArgumentException("Value outside of byte normal range: " + id);
        }
        this.id = (byte) id;
        this.referencePacket = referencePacket;
    }

    public static GTPacketNew[] referencePackets() {
        return Arrays.stream(values())
            .map(p -> p.referencePacket)
            .toArray(GTPacketNew[]::new);
    }
}
