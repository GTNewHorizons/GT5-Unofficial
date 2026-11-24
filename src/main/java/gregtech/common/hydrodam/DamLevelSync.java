package gregtech.common.hydrodam;

import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.unpackChunkX;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.unpackChunkZ;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.GTValues;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;

/// Propagates changes to the quantized levels of the volumes in one dimension: relights the server's world, and sends
/// the new levels to the players in the dimension.
final class DamLevelSync {

    /// Volume id -> the level the players in this dimension last received.
    private final Int2ObjectOpenHashMap<DamLevel> sentLevels = new Int2ObjectOpenHashMap<>();

    void tick(@NotNull World world, @NotNull DamVolumes volumes) {
        sentLevels.keySet()
            .removeIf(id -> volumes.getVolume(id) == null);

        List<GTPacketDamLevels.Entry> changed = new ArrayList<>();

        for (WaterVolume volume : volumes.getVolumes()) {
            DamLevel level = DamLevel.of(volume);

            if (level.equals(sentLevels.get(volume.getId()))) continue;

            GTPacketDamLevels.Entry entry = createEntry(volume, level);

            DamLighting.relightChangedLayers(world, entry, sentLevels.put(volume.getId(), level));
            changed.add(entry);
        }

        if (changed.isEmpty()) return;

        GTValues.NW.sendToWorld(world, new GTPacketDamLevels(world.provider.dimensionId, false, changed));
    }

    /// Sends every volume's level to a player who just entered the dimension.
    void sendAll(@NotNull World world, @NotNull DamVolumes volumes, @NotNull EntityPlayerMP player) {
        List<GTPacketDamLevels.Entry> entries = new ArrayList<>();

        for (WaterVolume volume : volumes.getVolumes()) {
            entries.add(createEntry(volume, DamLevel.of(volume)));
        }

        GTValues.NW.sendToPlayer(new GTPacketDamLevels(world.provider.dimensionId, true, entries), player);
    }

    /// Assumption: the volume is not empty.
    private static GTPacketDamLevels.Entry createEntry(WaterVolume volume, DamLevel level) {
        int minX = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        LongIterator iter = volume.getLayer(volume.getTopY())
            .iterator();

        while (iter.hasNext()) {
            long xz = iter.nextLong();

            int x = unpackChunkX(xz);
            int z = unpackChunkZ(xz);

            minX = Math.min(minX, x);
            minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x);
            maxZ = Math.max(maxZ, z);
        }

        return new GTPacketDamLevels.Entry(volume.getId(), level, minX, minZ, maxX, maxZ);
    }
}
