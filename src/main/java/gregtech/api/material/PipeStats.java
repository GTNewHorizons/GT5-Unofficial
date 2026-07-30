package gregtech.api.material;

/// Pure size transforms deriving per-size wire/cable, fluid pipe, and item pipe stats from the base values
/// held in [PipeProperties]. Wire and cable sizes are indexed 0 to 5
/// (1x, 2x, 4x, 8x, 12x, 16x); pipe sizes 0 to 4 (tiny, small, medium, large, huge). All arithmetic is exact
/// integer arithmetic, truncating division included.
public final class PipeStats {

    /// Fluid channel count of the quadruple fluid pipe.
    public static final int QUADRUPLE_PIPE_AMOUNT = 4;
    /// Fluid channel count of the nonuple fluid pipe.
    public static final int NONUPLE_PIPE_AMOUNT = 9;

    private static final int[] WIRE_AMPERAGE_MULTIPLIERS = { 1, 2, 4, 8, 12, 16 };
    private static final int[] ITEM_PIPE_STEP_DIVIDENDS = { 524288, 262144, 131072, 65536, 32768 };
    private static final int[] ITEM_PIPE_TICK_DIVIDENDS = { 16, 8, 4, 2 };

    private PipeStats() {}

    /// Amperage of the wire or cable at the given size for a material with the given base amperage.
    public static long wireAmperage(int baseAmperage, int size) {
        return (long) WIRE_AMPERAGE_MULTIPLIERS[size] * baseAmperage;
    }

    /// Voltage loss per meter of a bare wire: the explicit override where present, else twice the cable loss.
    public static int wireLoss(Integer wireLossOverride, int baseCableLoss) {
        return wireLossOverride != null ? wireLossOverride : 2 * baseCableLoss;
    }

    /// Voltage loss per meter of an insulated cable, identical at every size.
    public static int cableLoss(int baseCableLoss) {
        return baseCableLoss;
    }

    /// Fluid capacity of the single-channel fluid pipe at the given size for a material with the given
    /// medium-size capacity.
    public static int fluidPipeCapacity(int baseCapacity, int size) {
        return switch (size) {
            case 0 -> baseCapacity / 6;
            case 1 -> baseCapacity / 3;
            case 2 -> baseCapacity;
            case 3 -> baseCapacity * 2;
            case 4 -> baseCapacity * 4;
            default -> throw new IllegalArgumentException("pipe size must be 0 to 4: " + size);
        };
    }

    /// Per-channel fluid capacity of the quadruple fluid pipe.
    public static int quadrupleFluidPipeCapacity(int baseCapacity) {
        return baseCapacity;
    }

    /// Per-channel fluid capacity of the nonuple fluid pipe.
    public static int nonupleFluidPipeCapacity(int baseCapacity) {
        return baseCapacity / 3;
    }

    /// Inventory slot count of the item pipe at the given size for a material whose huge pipe holds the given
    /// slot count.
    public static int itemPipeSlots(int hugeSlots, int size) {
        return switch (size) {
            case 0 -> Math.max(hugeSlots / 16, 1);
            case 1 -> Math.max(hugeSlots / 8, 1);
            case 2 -> Math.max(hugeSlots / 4, 1);
            case 3 -> Math.max(hugeSlots / 2, 1);
            case 4 -> hugeSlots;
            default -> throw new IllegalArgumentException("pipe size must be 0 to 4: " + size);
        };
    }

    /// Routing step size of the item pipe at the given size; a restrictive pipe's step is 100 times the
    /// normal pipe's.
    public static int itemPipeStepSize(int hugeSlots, int size, boolean restrictive) {
        int dividend = ITEM_PIPE_STEP_DIVIDENDS[size];
        return (restrictive ? dividend * 100 : dividend) / hugeSlots;
    }

    /// Ticks per transfer step of the item pipe at the given size; the huge pipe always takes 20.
    public static int itemPipeTickTime(int hugeSlots, int size) {
        if (size == 4) return 20;
        return Math.max(ITEM_PIPE_TICK_DIVIDENDS[size] / hugeSlots, 1) * 20;
    }
}
