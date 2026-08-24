package tectech.voidcraft.ship;

import java.util.Arrays;
import java.util.Optional;

/**
 * A single Voidcraft component — one block type that can be placed inside the Voidcraft Assembler's build volume.
 *
 * <p>
 * Each component contributes fixed stats (see the per-field javadoc) to the ship it is part of. The {@code tier}
 * gates which assembler circuit tier may digitize the component (higher tech = more options, per the rework
 * proposal). The grid encoding used in {@link VoidcraftBlueprint} is {@code meta + 1} (0 = empty cell).
 */
public enum VoidcraftComponent {

    /**
     * Required, exactly one per ship. The Voidcraft's "brain": without it the build is rejected. Contributes base
     * mass and a little integrity.
     */
    CONTROLLER(0, "Voidcraft Controller", "tt.voidcraft.component.controller", 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 10),

    /** Thrust source. Every practical ship needs at least one. */
    ENGINE(1, "Voidcraft Engine", "tt.voidcraft.component.engine", 0, 8, 100, 0, 0, 0, 0, 0, 0, 5, 0),

    /** Structural bulk: pure mass plus integrity (the recoverable-vs-expendable stat). */
    UTILITY(2, "Voidcraft Utility Block", "tt.voidcraft.component.utility", 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 10),

    /** Storage for the resources a ship collects inside the USS. */
    CARGO_BAY(3, "Voidcraft Cargo Bay", "tt.voidcraft.component.cargo_bay", 0, 15, 0, 50, 0, 0, 0, 0, 0, 0, 0),

    /** Mining drone command centre — planetary mining (MINER role). */
    MINING_CENTRE(4, "Mining Drone Command Centre", "tt.voidcraft.component.mining_centre", 1, 20, 0, 0, 100, 0, 0, 0,
        0, 20, 0),

    /** Starlifter array — mines the central star (STARLIFTER role). */
    STARLIFTER_ARRAY(5, "Starlifter Array", "tt.voidcraft.component.starlifter_array", 2, 40, 0, 0, 0, 0, 100, 0, 0, 40,
        0),

    /** Spacetime fabric scanner — surveys ripples (EXPLORER role). */
    SPACETIME_SCANNER(6, "Spacetime Fabric Scanner", "tt.voidcraft.component.spacetime_scanner", 2, 15, 0, 0, 0, 100, 0,
        0, 0, 20, 0),

    /** Construction arm / fabricator — builds USS infrastructure (CONSTRUCTOR role). */
    CONSTRUCTION_ARM(7, "Construction Fabricator", "tt.voidcraft.component.construction_arm", 2, 25, 0, 0, 0, 0, 100, 0,
        0, 20, 0),

    /** Onboard reactor: large energy buffer, no draw. */
    REACTOR(8, "Voidcraft Reactor", "tt.voidcraft.component.reactor", 1, 10, 0, 0, 0, 0, 0, 1_000_000L, 0, 0, 0);

    /** All components in meta order (index == meta). */
    public static final VoidcraftComponent[] ALL = values();

    private final int meta;
    private final String displayName;
    private final String langKey;
    private final int tier;
    private final long mass;
    private final long thrust;
    private final long cargoSlots;
    private final long miningPower;
    private final long scanPower;
    private final long constructionPower;
    private final long starlifterPower;
    private final long energyBuffer;
    private final long energyDraw;
    private final long integrity;

    VoidcraftComponent(int meta, String displayName, String langKey, int tier, long mass, long thrust, long cargoSlots,
        long miningPower, long scanPower, long constructionPower, long starlifterPower, long energyBuffer,
        long energyDraw, long integrity) {
        this.meta = meta;
        this.displayName = displayName;
        this.langKey = langKey;
        this.tier = tier;
        this.mass = mass;
        this.thrust = thrust;
        this.cargoSlots = cargoSlots;
        this.miningPower = miningPower;
        this.scanPower = scanPower;
        this.constructionPower = constructionPower;
        this.starlifterPower = starlifterPower;
        this.energyBuffer = energyBuffer;
        this.energyDraw = energyDraw;
        this.integrity = integrity;
    }

    /** Block meta value; also the index into {@link #ALL}. */
    public int getMeta() {
        return meta;
    }

    /** English display name (registered as the block name at pre-load). */
    public String getDisplayName() {
        return displayName;
    }

    /** Lang key prefix for display names and tooltips (block names use {@code <key>.name}). */
    public String getLangKey() {
        return langKey;
    }

    /**
     * Technology tier. Gated by the assembler circuit: circuit damage 0-2 = tier 0, 3-5 = tier 1, 6+ = tier 2.
     */
    public int getTier() {
        return tier;
    }

    public long getMass() {
        return mass;
    }

    public long getThrust() {
        return thrust;
    }

    public long getCargoSlots() {
        return cargoSlots;
    }

    public long getMiningPower() {
        return miningPower;
    }

    public long getScanPower() {
        return scanPower;
    }

    public long getConstructionPower() {
        return constructionPower;
    }

    public long getStarlifterPower() {
        return starlifterPower;
    }

    public long getEnergyBuffer() {
        return energyBuffer;
    }

    public long getEnergyDraw() {
        return energyDraw;
    }

    public long getIntegrity() {
        return integrity;
    }

    /**
     * Grid value for this component (meta + 1). Zero is reserved for empty cells.
     */
    public int toGridValue() {
        return meta + 1;
    }

    /**
     * @param gridValue value as stored in the blueprint grid
     * @return the component, or empty for an unknown/empty value
     */
    public static Optional<VoidcraftComponent> fromGridValue(int gridValue) {
        int meta = gridValue - 1;
        if (meta < 0 || meta >= ALL.length) {
            return Optional.empty();
        }
        return Optional.of(ALL[meta]);
    }

    /**
     * @param meta block meta value
     * @return the component for that meta, or empty for an invalid meta
     */
    public static Optional<VoidcraftComponent> fromMeta(int meta) {
        if (meta < 0 || meta >= ALL.length) {
            return Optional.empty();
        }
        return Optional.of(ALL[meta]);
    }

    @Override
    public String toString() {
        return "VoidcraftComponent[" + name() + " meta=" + meta + "]";
    }

    static {
        // Keep the enum declaration and the meta values in sync.
        for (int i = 0; i < ALL.length; i++) {
            if (ALL[i].meta != i) {
                throw new IllegalStateException("VoidcraftComponent meta mismatch at index " + i);
            }
        }
        if (Arrays.stream(ALL)
            .anyMatch(
                c -> c.mass < 0 || c.thrust < 0
                    || c.cargoSlots < 0
                    || c.miningPower < 0
                    || c.scanPower < 0
                    || c.constructionPower < 0
                    || c.starlifterPower < 0
                    || c.energyBuffer < 0
                    || c.energyDraw < 0
                    || c.integrity < 0)) {
            throw new IllegalStateException("Negative component stat");
        }
    }
}
