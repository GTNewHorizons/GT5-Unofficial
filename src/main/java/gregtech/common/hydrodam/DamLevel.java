package gregtech.common.hydrodam;

import org.jetbrains.annotations.Contract;

/// A volume's water level as seen by clients: the y of its top layer, and how full that layer is (quantized to
/// {@link #FILL_STEPS} steps). Immutable.
public final class DamLevel {

    public static final int FILL_STEPS = 16;

    public final int topY;
    /// In [0, 1], a multiple of 1 / {@link #FILL_STEPS}.
    public final float topLayerFill;

    public DamLevel(int topY, float topLayerFill) {
        this.topY = topY;
        this.topLayerFill = topLayerFill;
    }

    /// Assumption: the volume is not empty.
    @Contract(pure = true)
    public static DamLevel of(WaterVolume volume) {
        return new DamLevel(volume.getTopY(), quantize(volume.getTopLayerFill()));
    }

    /// Rounds a fill in [0, 1] to the nearest step.
    @Contract(pure = true)
    public static float quantize(double fill) {
        return Math.round(fill * FILL_STEPS) / (float) FILL_STEPS;
    }

    /// How full the volume's cell at the given y is, in [0, 1].
    @Contract(pure = true)
    public float getFill(int y) {
        return y == topY ? topLayerFill : 1;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DamLevel other && topY == other.topY && topLayerFill == other.topLayerFill;
    }

    @Override
    public int hashCode() {
        return 31 * topY + Float.hashCode(topLayerFill);
    }
}
