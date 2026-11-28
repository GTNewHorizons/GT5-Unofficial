package gregtech.common.hydrodam;

import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.unpackChunkX;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.unpackChunkZ;

import java.util.ArrayList;

import javax.annotation.Nonnegative;

import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.datastructs.space.HashSet2D;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;

/// Adds rainwater to the volumes in one dimension. Each top-layer cell that rain can reach adds a fixed amount of water
/// per tick while it is raining.
final class DamRain {

    public static final double LITRES_PER_CELL_PER_TICK = 0.1;

    /// How often the number of cells that rain can reach is recounted, in ticks.
    private static final long RECOUNT_INTERVAL = 100;

    /// Volume id -> the number of its top-layer cells that rain can reach. Recounted periodically, so it may be stale.
    private final Int2IntOpenHashMap exposedCells = new Int2IntOpenHashMap();
    private long nextRecount;

    void tick(@NotNull World world, @NotNull DamVolumes volumes) {
        if (!world.isRaining()) return;

        long now = world.getTotalWorldTime();

        if (now >= nextRecount) {
            recount(world, volumes);
            nextRecount = now + RECOUNT_INTERVAL;
        }

        for (WaterVolume volume : new ArrayList<>(volumes.getVolumes())) {
            // Adding water can merge volumes, which removes the absorbed one.
            if (volumes.getVolume(volume.getId()) != volume) continue;

            int exposed = exposedCells.get(volume.getId());

            if (exposed > 0) volumes.addLitres(volume, exposed * LITRES_PER_CELL_PER_TICK);
        }
    }

    private void recount(World world, DamVolumes volumes) {
        exposedCells.clear();

        for (WaterVolume volume : volumes.getVolumes()) {
            exposedCells.put(volume.getId(), countExposedCells(world, volume));
        }
    }

    /// Assumption: the volume is not empty.
    @Nonnegative
    private static int countExposedCells(World world, WaterVolume volume) {
        int topY = volume.getTopY();
        HashSet2D top = volume.getLayer(topY);

        int count = 0;

        LongIterator iter = top.iterator();

        while (iter.hasNext()) {
            long xz = iter.nextLong();

            int x = unpackChunkX(xz);
            int z = unpackChunkZ(xz);

            if (!world.blockExists(x, topY, z)) continue;
            if (world.getPrecipitationHeight(x, z) > topY + 1) continue;
            if (!world.getBiomeGenForCoords(x, z)
                .canSpawnLightningBolt()) continue;

            count++;
        }

        return count;
    }
}
