package gregtech.common.hydrodam;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// The client's copy of each volume's {@link DamLevel}, per dimension. Written by {@link GTPacketDamLevels} on the
/// client thread and read by the renderer, which may run on other threads.
public final class ClientDamLevels {

    private static final ConcurrentHashMap<Integer, AtomicReferenceArray<DamLevel>> LEVELS = new ConcurrentHashMap<>();

    private ClientDamLevels() {}

    @Nullable
    public static DamLevel get(int dimension, int volumeId) {
        AtomicReferenceArray<DamLevel> levels = LEVELS.get(dimension);

        return levels == null ? null : levels.get(volumeId);
    }

    /// How full the given volume's cell at the given y is, in [0, 1]. Unknown volumes are treated as full.
    public static float getFill(int dimension, int volumeId, int y) {
        DamLevel level = get(dimension, volumeId);

        return level == null ? 1 : level.getFill(y);
    }

    /// @return the previous level, if any
    @Nullable
    static DamLevel set(int dimension, int volumeId, @NotNull DamLevel level) {
        return LEVELS.computeIfAbsent(dimension, ignored -> new AtomicReferenceArray<>(DamWaterMeta.MAX_ID + 1))
            .getAndSet(volumeId, level);
    }

    static void clear(int dimension) {
        LEVELS.remove(dimension);
    }

    static void clearAll() {
        LEVELS.clear();
    }
}
