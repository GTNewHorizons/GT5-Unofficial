package gregtech.api.net;

import java.util.Arrays;

import bartworks.common.net.PacketBWMetaBlock;
import bartworks.common.net.PacketBioVatRenderer;
import bartworks.common.net.PacketEIC;
import bartworks.common.net.PacketOreDictCache;
import bartworks.common.net.PacketServerJoined;
import gregtech.api.net.cape.GTPacketBroadcastCapes;
import gregtech.api.net.cape.GTPacketListCapes;
import gregtech.api.net.cape.GTPacketSetCape;
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
    // Free ID 18
    SEND_OREGEN_PATTERN(19, new GTPacketSendOregenPattern()),
    // Free ID 20
    MUSIC_SYSTEM_DATA(21, new GTPacketMusicSystemData()),
    INFINITE_SPRAYCAN(22, new GTPacketInfiniteSpraycan()),
    BIO_VAT_RENDERER(23, new PacketBioVatRenderer()),
    BW_META_BLOCK(24, new PacketBWMetaBlock()),
    ORE_DICT_CACHE(25, new PacketOreDictCache()),
    SERVER_JOINED(26, new PacketServerJoined()),
    EIC(27, new PacketEIC()),
    CREATE_TILE_ENTITY(28, new GTPacketCreateTE()),
    NODE_INFO(29, new GTPacketNodeInfo()),
    COIL_STATUS(30, new GTCoilStatus()),
    DEBUG_COVER(31, new PacketDebugRedstoneCover()),
    LMA_CRAFTING_FX(32, new GTPacketLMACraftingFX()),
    SEND_WIRELESS_EU(33, new GTPacketUpdatePowerGoggles()),
    OVERWRITE_POWER_GOGGLES_MEASUREMENTS(34, new GTPacketOverwritePowerGogglesMeasurements()),
    TETHER(35, new GTPacketTether()),
    UPDATE_POWERFAILS(36, new GTPacketUpdatePowerfails()),
    ON_POWERFAIL(37, new GTPacketOnPowerfail()),
    CLEAR_POWERFAIL(38, new GTPacketClearPowerfail()),
    CHAT(39, new GTPacketChat()),
    BROADCAST_CAPES(40, new GTPacketBroadcastCapes()),
    LIST_CAPES(41, new GTPacketListCapes()),
    SET_CAPE(42, new GTPacketSetCape()),
    // merge conflict prevention comment, keep a trailing comma above
    ;

    static {
        // Validate no duplicate IDs
        final GTPacketTypes[] types = values();
        final Int2ObjectOpenHashMap<GTPacket> foundIds = new Int2ObjectOpenHashMap<>(types.length);
        for (GTPacketTypes type : types) {
            final GTPacket previous = foundIds.get(type.id);
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
    public final GTPacket referencePacket;

    GTPacketTypes(int id, GTPacket referencePacket) {
        if (((int) (byte) id) != id) {
            throw new IllegalArgumentException("Value outside of byte normal range: " + id);
        }
        this.id = (byte) id;
        this.referencePacket = referencePacket;
    }

    public static GTPacket[] referencePackets() {
        return Arrays.stream(values())
            .map(p -> p.referencePacket)
            .toArray(GTPacket[]::new);
    }
}
