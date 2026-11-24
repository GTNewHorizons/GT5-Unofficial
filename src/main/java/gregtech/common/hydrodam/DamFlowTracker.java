package gregtech.common.hydrodam;

import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;

/// Measures how fast each volume in one dimension is gaining or losing water, from every source (machines, leaks,
/// rain). Rates are averaged over {@link #SAMPLE_INTERVAL} ticks because the per-tick change is noisy.
final class DamFlowTracker {

    public static final int SAMPLE_INTERVAL = 20;

    /// Volume id -> its litres at the last sample.
    private Int2DoubleOpenHashMap sampledLitres = new Int2DoubleOpenHashMap();
    private long lastSampleTime;

    /// Volume id -> its net change between the last two samples, in litres per tick.
    private final Int2DoubleOpenHashMap netLitresPerTick = new Int2DoubleOpenHashMap();

    void tick(@NotNull World world, @NotNull DamVolumes volumes) {
        long now = world.getTotalWorldTime();
        long elapsed = now - lastSampleTime;

        if (elapsed < SAMPLE_INTERVAL) return;

        Int2DoubleOpenHashMap samples = new Int2DoubleOpenHashMap();

        netLitresPerTick.clear();

        for (WaterVolume volume : volumes.getVolumes()) {
            int id = volume.getId();

            samples.put(id, volume.getLitres());

            // A volume that was just created, merged into or split off has no meaningful previous sample.
            if (sampledLitres.containsKey(id)) {
                netLitresPerTick.put(id, (volume.getLitres() - sampledLitres.get(id)) / elapsed);
            }
        }

        sampledLitres = samples;
        lastSampleTime = now;
    }

    /// The volume's net change in litres per tick, averaged over the last {@link #SAMPLE_INTERVAL} ticks. Positive when
    /// it is filling.
    double getNetLitresPerTick(int volumeId) {
        return netLitresPerTick.get(volumeId);
    }
}
