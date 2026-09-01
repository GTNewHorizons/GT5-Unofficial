package tectech.voidcraft.ship;

/**
 * Balance and format constants for the Voidcraft system.
 *
 * <p>
 * All tunable numbers live here so balance changes never require touching the math classes. Values follow
 * docs/Voidcraft_Implementation_Plan.md: ships range from a 1×1×3 minimal craft (controller + engine + utility)
 * up to a 5×5×10 bounding volume.
 */
public final class VoidcraftConstants {

    /**
     * Minimum number of parts a digitizable Voidcraft needs (blocks + covers; pass 23: controller + frame + at
     * least one cover).
     */
    public static final int MIN_COMPONENT_COUNT = 3;

    /** Maximum grid dimensions of a Voidcraft (width × height × depth, any orientation of the 5×5×10 volume). */
    public static final int MAX_DIM_X = 5;
    public static final int MAX_DIM_Y = 5;
    public static final int MAX_DIM_Z = 10;

    public static final int MAX_CELLS = MAX_DIM_X * MAX_DIM_Y * MAX_DIM_Z;

    /** Maximum grid dimension of a Voidbase (width = height = depth — a 15×15×15 volume, any orientation). */
    public static final int MAX_BASE_DIM = 15;

    /** Maximum cell count of a Voidbase (15³ = 3375). */
    public static final int MAX_BASE_CELLS = MAX_BASE_DIM * MAX_BASE_DIM * MAX_BASE_DIM;

    // region Frame integrity (per-face model)

    /**
     * Integrity gained PER FRAME SIDE that faces another frame side (any frame tier). Solid frame clusters —
     * where every frame hugs its neighbours — are the strongest hulls.
     */
    public static final long FRAME_FACE_INTEGRITY_BONUS = 4L;

    /**
     * Integrity lost PER FRAME SIDE that is exposed to air (outside the bounding volume or facing an empty cell).
     * Hollow frames bleed integrity on every exposed side.
     */
    public static final long FRAME_FACE_INTEGRITY_PENALTY = 2L;

    // endregion

    // region Assembler digitization cost

    /** EU consumed per digitized component block. */
    public static final long DIGITIZE_EU_PER_CELL = 500_000L;

    /** Ticks per component block for the digitization process (progress duration). */
    public static final int DIGITIZE_TICKS_PER_CELL = 8;

    /** Minimum digitization duration in ticks. */
    public static final int DIGITIZE_MIN_TICKS = 400;

    // endregion

    /**
     * NBT payload format version, bumped on any incompatible ItemVoidcraft NBT change.
     *
     * <ul>
     * <li>1: grid of component blocks only.</li>
     * <li>2: + per-cell facing grid, per-face cover grid, and net-thrust vector tags.</li>
     * <li>3: + fuel capacity, engine type, thruster count, and frame tier denormalized stat tags (the reactor /
     * fuel / frame-tier mechanics pass).</li>
     * </ul>
     */
    public static final int NBT_FORMAT_VERSION = 3;

    private VoidcraftConstants() {
        throw new AssertionError("Constants class");
    }
}
