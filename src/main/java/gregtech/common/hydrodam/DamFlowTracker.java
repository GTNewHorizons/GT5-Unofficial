package gregtech.common.hydrodam;

import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import it.unimi.dsi.fastutil.objects.Reference2DoubleOpenHashMap;

/// Measures how fast each volume in one dimension is gaining or losing water, from every source (machines, leaks,
/// rain). Rates are averaged over {@link #SAMPLE_INTERVAL} ticks because the per-tick change is noisy.
///
/// Volumes are tracked by identity rather than id, since ids are reused after a volume is deleted.
final class DamFlowTracker {

    public static final int SAMPLE_INTERVAL = 20;

    /// Volume -> its litres at the last sample.
    private Reference2DoubleOpenHashMap<WaterVolume> sampledLitres = new Reference2DoubleOpenHashMap<>();
    private long lastSampleTime;

    /// Volume -> its net change between the last two samples, in litres per tick.
    private final Reference2DoubleOpenHashMap<WaterVolume> netLitresPerTick = new Reference2DoubleOpenHashMap<>();

    void tick(@NotNull World world, @NotNull DamVolumes volumes) {
        long now = world.getTotalWorldTime();
        long elapsed = now - lastSampleTime;

        if (elapsed < SAMPLE_INTERVAL) return;

        Reference2DoubleOpenHashMap<WaterVolume> samples = new Reference2DoubleOpenHashMap<>();

        netLitresPerTick.clear();

        for (WaterVolume volume : volumes.getVolumes()) {
            samples.put(volume, volume.getLitres());

            // A volume that was just created or split off has no previous sample.
            if (sampledLitres.containsKey(volume)) {
                netLitresPerTick.put(volume, (volume.getLitres() - sampledLitres.getDouble(volume)) / elapsed);
            }
        }

        sampledLitres = samples;
        lastSampleTime = now;
    }

    /// The volume's net change in litres per tick, averaged over the last {@link #SAMPLE_INTERVAL} ticks. Positive
    /// when it is filling.
    double getNetLitresPerTick(@NotNull WaterVolume volume) {
        return netLitresPerTick.getDouble(volume);
    }
}
