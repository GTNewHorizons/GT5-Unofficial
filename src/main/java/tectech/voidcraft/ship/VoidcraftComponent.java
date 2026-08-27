package tectech.voidcraft.ship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The Voidcraft component catalog.
 *
 * <p>
 * PASS 23 (user spec): <b>covers are the primary components â€” all ship functionality comes from the covers.</b>
 * Only TWO components are placeable full blocks:
 *
 * <ul>
 * <li>{@link #CONTROLLER} â€” the ship's brain (required, exactly one per ship).</li>
 * <li>{@link #FRAME} â€” the "Voidcraft Frame": a mostly-transparent framebox hull block whose purpose is to
 * accept the Voidcraft component covers on its faces (renamed from the old Utility Block).</li>
 * </ul>
 *
 * <p>
 * The other entries ({@link #ENGINE}, {@link #CARGO_BAY}, ...) are <em>cover-only</em>: they are no longer
 * placeable blocks, they survive in the catalog purely as the function definitions behind their mirror covers
 * (icon/texture mapping and the stat shapes the covers copy). They are marked {@code placeable=false} â€” the
 * assembler refuses to digitize a build volume that still contains them ({@code voidcraft_cover_only_component}).
 *
 * <p>
 * Each entry contributes fixed stats (see the per-field javadoc). The {@code tier} gates which assembler circuit
 * tier may digitize it (higher tech = more options, per the rework proposal). The grid encoding used in
 * {@link VoidcraftBlueprint} is {@code meta + 1} (0 = empty cell).
 */
public enum VoidcraftComponent {

    /**
     * Required, exactly one per ship. The Voidcraft's "brain": without it the build is rejected. Contributes base
     * mass and a little integrity; every function of the ship comes from its covers.
     */
    CONTROLLER(0, "Voidcraft Controller", "tt.voidcraft.component.controller", true, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        10),

    /**
     * COVER-ONLY: thrust is delivered by the {@link VoidcraftCoverComponent#THRUSTER_NOZZLE} cover.
     * Kept as the function definition behind that cover (icon + stat shape); not a placeable block.
     */
    ENGINE(1, "Voidcraft Engine", "tt.voidcraft.component.engine", false, 0, 8, 100, 0, 0, 0, 0, 0, 5, 0, 0, 0),

    /**
     * The "Voidcraft Frame" (renamed from the old "Voidcraft Utility Block"): the mostly-transparent
     * framebox hull block. Structural mass + integrity, no function of its own â€” its purpose is to accept
     * the Voidcraft component covers on its faces; all ship functionality comes from those covers.
     */
    FRAME(2, "Voidcraft Frame", "tt.voidcraft.component.frame", true, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10),

    /**
     * COVER-ONLY: cargo is delivered by the {@link VoidcraftCoverComponent#CARGO_POD} cover. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    CARGO_BAY(3, "Voidcraft Cargo Bay", "tt.voidcraft.component.cargo_bay", false, 0, 15, 0, 50, 0, 0, 0, 0, 0, 0, 0,
        0),

    /**
     * COVER-ONLY: mining is delivered by the {@link VoidcraftCoverComponent#MINING_ARRAY} cover. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    MINING_CENTRE(4, "Mining Drone Command Centre", "tt.voidcraft.component.mining_centre", false, 1, 20, 0, 0, 100, 0,
        0, 0, 0, 20, 0, 0),

    /**
     * COVER-ONLY: starlifting is delivered by the {@link VoidcraftCoverComponent#STAR_SIPHON} cover. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    STARLIFTER_ARRAY(5, "Starlifter Array", "tt.voidcraft.component.starlifter_array", false, 2, 40, 0, 0, 0, 0, 100, 0,
        0, 40, 0, 0),

    /**
     * COVER-ONLY: scanning is delivered by the {@link VoidcraftCoverComponent#SCANNER_DISH} cover. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    SPACETIME_SCANNER(6, "Spacetime Fabric Scanner", "tt.voidcraft.component.spacetime_scanner", false, 2, 15, 0, 0, 0,
        100, 0, 0, 0, 20, 0, 0),

    /**
     * COVER-ONLY: construction is delivered by the {@link VoidcraftCoverComponent#FABRICATOR_UNIT} cover. Kept as
     * the function definition behind that cover (icon + stat shape); not a placeable block.
     */
    CONSTRUCTION_ARM(7, "Construction Fabricator", "tt.voidcraft.component.construction_arm", false, 2, 25, 0, 0, 0, 0,
        100, 0, 0, 20, 0, 0),

    /**
     * COVER-ONLY: energy storage is delivered by the {@link VoidcraftCoverComponent#POWER_CELL} cover. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    REACTOR(8, "Voidcraft Reactor", "tt.voidcraft.component.reactor", false, 1, 10, 0, 0, 0, 0, 0, 1_000_000L, 0, 0, 0,
        0),

    /**
     * COVER-ONLY: hull repair is delivered by the {@link VoidcraftCoverComponent#REPAIR_BAY} cover (the repair work
     * command restores the station's integrity, consuming the energy draw while active). Kept as the function
     * definition behind that cover; not a placeable block.
     */
    REPAIR_BAY(9, "Voidcraft Repair Bay", "tt.voidcraft.component.repair_bay", false, 2, 12, 0, 0, 0, 0, 0, 0, 0, 2_000,
        0, 0),

    /**
     * COVER-ONLY: energy generation is delivered by the {@link VoidcraftCoverComponent#SOLAR_PANEL} cover (the first
     * energy-generating component: a flat generation rate per panel per game tick, independent of the star â€”
     * star-dependent generation is a later pass). Kept as the function definition behind that cover; not a
     * placeable block.
     */
    SOLAR_PANEL(10, "Voidcraft Solar Panel", "tt.voidcraft.component.solar_panel", false, 2, 8, 0, 0, 0, 0, 0, 0, 0, 0,
        2_000, 0);

    /** All components in meta order (index == meta). */
    public static final VoidcraftComponent[] ALL = values();

    /**
     * PASS 23: the ONLY placeable full-block components (controller + frame). Everything else is cover-only.
     */
    public static final List<VoidcraftComponent> PLACEABLE = placeableList();

    private final int meta;
    private final String displayName;
    private final String langKey;
    /** PASS 23: true only for the two full blocks (controller, frame); false = cover-only function definition. */
    private final boolean placeable;
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
    /** Energy generated per game tick (the solar panel; 0 for everything else). */
    private final long energyGen;
    private final long integrity;

    VoidcraftComponent(int meta, String displayName, String langKey, boolean placeable, int tier, long mass,
        long thrust, long cargoSlots, long miningPower, long scanPower, long constructionPower, long starlifterPower,
        long energyBuffer, long energyDraw, long energyGen, long integrity) {
        this.meta = meta;
        this.displayName = displayName;
        this.langKey = langKey;
        this.placeable = placeable;
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
        this.energyGen = energyGen;
        this.integrity = integrity;
    }

    private static List<VoidcraftComponent> placeableList() {
        List<VoidcraftComponent> list = new ArrayList<>();
        for (VoidcraftComponent component : ALL) {
            if (component.placeable) {
                list.add(component);
            }
        }
        return java.util.Collections.unmodifiableList(list);
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
     * PASS 23: whether this component is a placeable full block (only {@link #CONTROLLER} and {@link #FRAME}).
     * Cover-only definitions are never placed in the world â€” their function ships as a cover.
     */
    public boolean isPlaceable() {
        return placeable;
    }

    /** PASS 23: {@code true} when this component only exists as a cover (not a placeable block). */
    public boolean isCoverOnly() {
        return !placeable;
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

    public long getEnergyGen() {
        return energyGen;
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
        return "VoidcraftComponent[" + name() + " meta=" + meta + " placeable=" + placeable + "]";
    }

    static {
        // Keep the enum declaration and the meta values in sync.
        for (int i = 0; i < ALL.length; i++) {
            if (ALL[i].meta != i) {
                throw new IllegalStateException("VoidcraftComponent meta mismatch at index " + i);
            }
        }
        // Pass 23: exactly the controller and the frame are placeable full blocks.
        if (!CONTROLLER.isPlaceable() || !FRAME.isPlaceable()) {
            throw new IllegalStateException("Controller and Frame must be placeable full blocks");
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
                    || c.energyGen < 0
                    || c.integrity < 0)) {
            throw new IllegalStateException("Negative component stat");
        }
    }
}
