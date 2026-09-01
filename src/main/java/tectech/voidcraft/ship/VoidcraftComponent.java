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
 * Only FOUR components are placeable hull blocks:
 *
 * <ul>
 * <li>{@link #CONTROLLER} â€” the ship's brain (required, exactly one per ship).</li>
 * <li>{@link #FRAME}, {@link #FRAME_2}, {@link #FRAME_3}, {@link #FRAME_4} â€” the frame tiers:
 * mostly-transparent framebox hull blocks whose purpose is to accept the Voidcraft component covers on their
 * faces. A ship may use frames of exactly ONE tier (no mix-and-match; multiblock components are exempt) and a
 * frame tier only accepts components of its own tier or lower (see
 * {@link #isFrame} / {@link #getTier}).</li>
 * </ul>
 *
 * <p>
 * The other entries ({@link #ENGINE}, {@link #CARGO_BAY}, ...) are <em>cover-only</em>: they are no longer
 * placeable blocks, they survive in the catalog purely as the function definitions behind their mirror covers
 * (icon/texture mapping and the stat shapes the covers copy). They are marked {@code placeable=false} â€” the
 * assembler refuses to digitize a build volume that still contains them ({@code voidcraft_cover_only_component}).
 *
 * <p>
 * <b>Multiblock components</b> (user spec) are a second kind of placeable block, marked {@code multiblock=true}:
 * a GT multiblock defined as a StructureLib structure (its own controller machine block plus zero-stat casing
 * blocks). The assemblers digitize the whole structure as a unit when the controller's own structure check
 * passes; the component's stats apply exactly once, through the controller entry's per-cell contribution (the
 * casing entries contribute mass only).
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
        0, 10, false),

    /**
     * COVER-ONLY: thrust is delivered by the {@link VoidcraftCoverComponent#THRUSTER_NOZZLE} cover.
     * Kept as the function definition behind that cover (icon + stat shape); not a placeable block.
     */
    ENGINE(1, "Voidcraft Engine", "tt.voidcraft.component.engine", false, 0, 8, 100, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0,
        false),

    /**
     * The "Voidcraft Frame" (tier 1, renamed from the old "Voidcraft Utility Block"): the mostly-transparent
     * framebox hull block. Structural mass + base integrity, no function of its own â€” its purpose is to accept
     * the Voidcraft component covers on its faces; all ship functionality comes from those covers. A frame side
     * facing another frame side adds integrity, a side exposed to air removes some (see
     * {@link VoidcraftBlueprint#computeStats}).
     */
    FRAME(2, "Voidcraft Frame", "tt.voidcraft.component.frame", true, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, false),

    /**
     * COVER-ONLY: cargo is delivered by the {@link VoidcraftCoverComponent#CARGO_POD} cover. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    CARGO_BAY(3, "Voidcraft Cargo Bay", "tt.voidcraft.component.cargo_bay", false, 0, 15, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0,
        0, false),

    /**
     * COVER-ONLY: mining is delivered by the {@link VoidcraftCoverComponent#MINING_ARRAY} cover. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    MINING_CENTRE(4, "Mining Drone Command Centre", "tt.voidcraft.component.mining_centre", false, 1, 20, 0, 0, 100, 0,
        0, 0, 0, 0, 20, 0, 0, false),

    /**
     * COVER-ONLY: starlifting is delivered by the {@link VoidcraftCoverComponent#STAR_SIPHON} cover. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    STARLIFTER_ARRAY(5, "Starlifter Array", "tt.voidcraft.component.starlifter_array", false, 2, 40, 0, 0, 0, 0, 100, 0,
        0, 0, 40, 0, 0, false),

    /**
     * COVER-ONLY: scanning is delivered by the {@link VoidcraftCoverComponent#SCANNER_DISH} cover. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    SPACETIME_SCANNER(6, "Spacetime Fabric Scanner", "tt.voidcraft.component.spacetime_scanner", false, 2, 15, 0, 0, 0,
        100, 0, 0, 0, 0, 20, 0, 0, false),

    /**
     * COVER-ONLY: construction is delivered by the {@link VoidcraftCoverComponent#FABRICATOR_UNIT} cover. Kept as
     * the function definition behind that cover (icon + stat shape); not a placeable block.
     */
    CONSTRUCTION_ARM(7, "Construction Fabricator", "tt.voidcraft.component.construction_arm", false, 2, 25, 0, 0, 0, 0,
        100, 0, 0, 0, 20, 0, 0, false),

    /**
     * COVER-ONLY: energy storage is delivered by the {@link VoidcraftCoverComponent#POWER_CELL} cover. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    REACTOR(8, "Voidcraft Reactor", "tt.voidcraft.component.reactor", false, 1, 10, 0, 0, 0, 0, 0, 0, 1_000_000L, 0, 0,
        0, 0, false),

    /**
     * COVER-ONLY: hull repair is delivered by the {@link VoidcraftCoverComponent#REPAIR_BAY} cover (the repair work
     * command restores the station's integrity, consuming the energy draw while active). Kept as the function
     * definition behind that cover; not a placeable block.
     */
    REPAIR_BAY(9, "Voidcraft Repair Bay", "tt.voidcraft.component.repair_bay", false, 2, 12, 0, 0, 0, 0, 0, 0, 0, 0,
        2_000, 0, 0, false),

    /**
     * COVER-ONLY: energy generation is delivered by the {@link VoidcraftCoverComponent#SOLAR_PANEL} cover (the first
     * energy-generating component: a flat generation rate per panel per game tick, independent of the star â€”
     * star-dependent generation is a later pass). Kept as the function definition behind that cover; not a
     * placeable block.
     */
    SOLAR_PANEL(10, "Voidcraft Solar Panel", "tt.voidcraft.component.solar_panel", false, 2, 8, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 2_000, 0, false),

    /**
     * MULTIBLOCK CONTROLLER: the Voidcraft Heavy Mining Array — a 3×3×2 GT multiblock (1 controller block + 17 casing
     * blocks). The assembler digitizes the whole structure as a unit when its controller's own structure check
     * passes; the component stats apply exactly once, carried by this entry (the casings contribute mass only).
     * "Heavy" distinguishes it from the small mining cover of the same name.
     */
    MINING_ARRAY(11, "Voidcraft Heavy Mining Array", "tt.voidcraft.component.mining_array", true, 2, 25, 0, 0, 1000, 0,
        0, 0, 0, 0, 200, 0, 0, true),

    /**
     * MULTIBLOCK CASING: the Mining Array's plain filler block — a "dumb" casing with no stats of its own beyond
     * mass (it takes no covers). Tolerated as inert mass / decoration when found outside a formed structure.
     */
    MINING_ARRAY_CASING(12, "Mining Array Casing", "tt.voidcraft.component.mining_array_casing", true, 0, 5, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, true),

    /**
     * MULTIBLOCK CASING: the Mining Array's accent panel around the controller's front face (no covers, no stats
     * beyond mass).
     */
    MINING_ARRAY_PANEL(13, "Mining Array Panel", "tt.voidcraft.component.mining_array_panel", true, 0, 5, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, true),

    /**
     * COVER-ONLY: logistics is delivered by the {@link VoidcraftCoverComponent#CARGO_DRONE_BAY} cover (the
     * ship-to-ship cargo transfer, SEND / TAKE: 1 power = 1 cargo unit per second). Kept as the function
     * definition behind that cover; not a placeable block.
     */
    CARGO_DRONE_BAY(14, "Voidcraft Cargo Drone Bay", "tt.voidcraft.component.cargo_drone_bay", false, 2, 10, 0, 0, 0, 0,
        0, 0, 10, 0, 0, 0, 0, false),

    /**
     * MULTIBLOCK CONTROLLER: the Satellite Rail Launcher — a 7×7×12 GT multiblock (1 controller block + 586 casing
     * blocks), the first PLANETARY/STAR-SCALE INFRASTRUCTURE component: a station built around a star launches Power
     * Satellites from its cargo hold onto the star's Dyson Swarm (the star's satellite capacity grows with the
     * star's size). Bases only — a ship build containing it is rejected at the assembler
     * ({@code voidcraft_launcher_station_only}).
     */
    SATELLITE_LAUNCHER(15, "Satellite Rail Launcher", "tt.voidcraft.component.satellite_launcher", true, 2, 60, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, true),

    /**
     * MULTIBLOCK CASING: the Satellite Launcher's plain filler block — a "dumb" casing with no stats of its own
     * beyond mass (it takes no covers). Tolerated as inert mass / decoration when found outside a formed structure.
     */
    SATELLITE_LAUNCHER_CASING(16, "Satellite Launcher Casing", "tt.voidcraft.component.satellite_launcher_casing", true,
        0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true),

    /**
     * MULTIBLOCK CASING: the Satellite Launcher's deck panel (the top launch-deck layers — no covers, no stats
     * beyond mass).
     */
    SATELLITE_LAUNCHER_PANEL(17, "Satellite Launcher Panel", "tt.voidcraft.component.satellite_launcher_panel", true, 0,
        5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true),

    /**
     * Frame tier 2: hosts covers of tier 1 or lower (all of tier 0, plus the tier-1 engines and reactor). Higher
     * base integrity than tier 1. All frames of a ship must be the same tier.
     */
    FRAME_2(18, "Voidcraft Frame (Tier 2)", "tt.voidcraft.component.frame_2", true, 1, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        15, false),

    /**
     * Frame tier 3: hosts covers of tier 2 or lower. Higher base integrity than tier 2. All frames of a ship must
     * be the same tier.
     */
    FRAME_3(19, "Voidcraft Frame (Tier 3)", "tt.voidcraft.component.frame_3", true, 2, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        20, false),

    /**
     * Frame tier 4: hosts covers of tier 3 or lower (the full catalog). Highest base integrity. All frames of a
     * ship must be the same tier.
     */
    FRAME_4(20, "Voidcraft Frame (Tier 4)", "tt.voidcraft.component.frame_4", true, 3, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        25, false),

    /**
     * COVER-ONLY: the Fusion Reactor is delivered by the {@link VoidcraftCoverComponent#FUSION_REACTOR} cover. A
     * high-output energy source (large flat energyGen, no fuel burned in flight); a ship carrying it must pay a
     * launch cost of reactor fuel (Deuterium) at the Gateway, scaled by the number of reactors. Kept as the
     * function definition behind that cover (icon + stat shape); not a placeable block.
     */
    FUSION_REACTOR(21, "Voidcraft Fusion Reactor", "tt.voidcraft.component.fusion_reactor", false, 2, 20, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 5_000, 0, false),

    /**
     * COVER-ONLY: the Antimatter Reactor is delivered by the {@link VoidcraftCoverComponent#ANTIMATTER_REACTOR}
     * cover. The highest-output energy source in the catalog; a ship carrying it must pay a launch cost of reactor
     * fuel (Semi-Stable Antimatter) at the Gateway, scaled by the number of reactors. Kept as the function
     * definition behind that cover (icon + stat shape); not a placeable block.
     */
    ANTIMATTER_REACTOR(22, "Voidcraft Antimatter Reactor", "tt.voidcraft.component.antimatter_reactor", false, 3, 30, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 25_000, 0, false),

    /**
     * COVER-ONLY: the Ion Thruster is delivered by the {@link VoidcraftCoverComponent#ION_THRUSTER} cover. The
     * fuel-burning engine family's baseline (burns Xenon while travelling); a ship may carry exactly one engine
     * type. Kept as the function definition behind that cover (icon + stat shape); not a placeable block.
     */
    ION_THRUSTER(23, "Voidcraft Ion Thruster", "tt.voidcraft.component.ion_thruster", false, 1, 3, 150, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, false),

    /**
     * COVER-ONLY: the Fusion Torch is delivered by the {@link VoidcraftCoverComponent#FUSION_TORCH} cover.
     * High-thrust fuel-burning engine (burns Water while travelling); a ship may carry exactly one engine type.
     * Kept as the function definition behind that cover (icon + stat shape); not a placeable block.
     */
    FUSION_TORCH(24, "Voidcraft Fusion Torch", "tt.voidcraft.component.fusion_torch", false, 2, 6, 400, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, false),

    /**
     * COVER-ONLY: the Antimatter Engine is delivered by the {@link VoidcraftCoverComponent#ANTIMATTER_ENGINE}
     * cover. The highest-thrust engine in the catalog (burns Semi-Stable Antimatter while travelling); a ship may
     * carry exactly one engine type. Kept as the function definition behind that cover (icon + stat shape); not a
     * placeable block.
     */
    ANTIMATTER_ENGINE(25, "Voidcraft Antimatter Engine", "tt.voidcraft.component.antimatter_engine", false, 3, 10, 900,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false),

    /**
     * COVER-ONLY: the Fuel Storage is delivered by the {@link VoidcraftCoverComponent#FUEL_STORAGE} cover. A
     * dedicated tank (separate from the cargo hold) holding the fuel required by the ship's engine type; the
     * Gateway only launches the ship when the tank is full, and the fuel is consumed while travelling. Kept as
     * the function definition behind that cover (icon + stat shape); not a placeable block.
     */
    FUEL_STORAGE(26, "Voidcraft Fuel Storage", "tt.voidcraft.component.fuel_storage", false, 0, 8, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, false),

    /**
     * MULTIBLOCK CONTROLLER: the Stellar Injector — a 7×7×12 STATION component like the Satellite Rail Launcher:
     * a ship build containing it is rejected ({@code voidcraft_launcher_station_only}); the Voidbase Assembler
     * digitizes it into a Voidbase blueprint. The star-feeding injector it provides is an internal of the
     * Unstable Solar System, contributed by the base that carries it.
     */
    STELLAR_INJECTOR(27, "Stellar Injector", "tt.voidcraft.component.stellar_injector", true, 2, 80, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, true),

    /**
     * MULTIBLOCK CASING: the Stellar Injector's plain filler block — a "dumb" casing with no stats of its own
     * beyond mass (it takes no covers).
     */
    STELLAR_INJECTOR_CASING(28, "Stellar Injector Casing", "tt.voidcraft.component.stellar_injector_casing", true, 0, 5,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true),

    /**
     * MULTIBLOCK CONTROLLER: the Continuum Stabilizer — a 5×5×7 STATION component like the Satellite Rail
     * Launcher: a ship build containing it is rejected ({@code voidcraft_launcher_station_only}); the Voidbase
     * Assembler digitizes it into a Voidbase blueprint. The ripple stabilization it provides is an internal of
     * the Unstable Solar System, contributed by the base that carries it.
     */
    CONTINUUM_STABILIZER(29, "Continuum Stabilizer", "tt.voidcraft.component.continuum_stabilizer", true, 2, 50, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, true),

    /**
     * MULTIBLOCK CASING: the Continuum Stabilizer's plain filler block (no stats beyond mass).
     */
    CONTINUUM_STABILIZER_CASING(30, "Continuum Stabilizer Casing", "tt.voidcraft.component.continuum_stabilizer_casing",
        true, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true),

    /**
     * MULTIBLOCK CONTROLLER: the Stellar Lens — a 7×7×12 STATION component like the Satellite Rail Launcher: a
     * ship build containing it is rejected ({@code voidcraft_launcher_station_only}); the Voidbase Assembler
     * digitizes it into a Voidbase blueprint. The visual it provides around the star is an internal of the
     * Unstable Solar System, contributed by the base that carries it.
     */
    STELLAR_LENS(31, "Stellar Lens", "tt.voidcraft.component.stellar_lens", true, 2, 80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, true),

    /**
     * MULTIBLOCK CASING: the Stellar Lens's plain filler block (no stats beyond mass).
     */
    STELLAR_LENS_CASING(32, "Stellar Lens Casing", "tt.voidcraft.component.stellar_lens_casing", true, 0, 5, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, true),

    /**
     * MULTIBLOCK CONTROLLER: the Stabilization Matrix — a 7×7×10 STATION component like the Satellite Rail
     * Launcher: a ship build containing it is rejected ({@code voidcraft_launcher_station_only}); the Voidbase
     * Assembler digitizes it into a Voidbase blueprint. The stabilization it provides is an internal of the
     * Unstable Solar System, contributed by the base that carries it.
     */
    STABILIZATION_MATRIX(33, "Stabilization Matrix", "tt.voidcraft.component.stabilization_matrix", true, 2, 60, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, true),

    /**
     * MULTIBLOCK CASING: the Stabilization Matrix's plain filler block (no stats beyond mass).
     */
    STABILIZATION_MATRIX_CASING(34, "Stabilization Matrix Casing", "tt.voidcraft.component.stabilization_matrix_casing",
        true, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);

    /** All components in meta order (index == meta). */
    public static final VoidcraftComponent[] ALL = values();

    /**
     * All placeable full-block components: the classic hull blocks (controller + frame) and the multiblock
     * component blocks (each multiblock's controller + its casing blocks). Everything else is cover-only.
     */
    public static final List<VoidcraftComponent> PLACEABLE = placeableList();

    private final int meta;
    private final String displayName;
    private final String langKey;
    /** PASS 23: true for the full blocks (controller, frame) and the multiblock component blocks. */
    private final boolean placeable;
    /**
     * True when this entry is a block of a GT multiblock component (its controller or one of its casing blocks) —
     * the assembler audits the owning structure's formation before digitizing it.
     */
    private final boolean multiblock;
    private final int tier;
    private final long mass;
    private final long thrust;
    private final long cargoSlots;
    private final long miningPower;
    private final long scanPower;
    private final long constructionPower;
    private final long starlifterPower;
    /** Logistics power (the Cargo Drone Bay mirror): 1 power = 1 cargo unit transferred per second (SEND / TAKE). */
    private final long logisticsPower;
    private final long energyBuffer;
    private final long energyDraw;
    /** Energy generated per game tick (the solar panel; 0 for everything else). */
    private final long energyGen;
    private final long integrity;

    VoidcraftComponent(int meta, String displayName, String langKey, boolean placeable, int tier, long mass,
        long thrust, long cargoSlots, long miningPower, long scanPower, long constructionPower, long starlifterPower,
        long logisticsPower, long energyBuffer, long energyDraw, long energyGen, long integrity, boolean multiblock) {
        this.meta = meta;
        this.displayName = displayName;
        this.langKey = langKey;
        this.placeable = placeable;
        this.multiblock = multiblock;
        this.tier = tier;
        this.mass = mass;
        this.thrust = thrust;
        this.cargoSlots = cargoSlots;
        this.miningPower = miningPower;
        this.scanPower = scanPower;
        this.constructionPower = constructionPower;
        this.starlifterPower = starlifterPower;
        this.logisticsPower = logisticsPower;
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
     * @return true when this entry is one of the frame tiers ({@link #FRAME} through {@link #FRAME_4}). The frame
     *         tier gates which covers may attach to its faces and contributes the face-based integrity math;
     *         all frames of a ship must be the same tier.
     */
    public boolean isFrame() {
        return this == FRAME || this == FRAME_2 || this == FRAME_3 || this == FRAME_4;
    }

    /**
     * {@code true} when this entry is a block of a GT multiblock component (the multiblock's controller or one of
     * its casing blocks). The assembler audits the owning structure before digitizing it.
     */
    public boolean isMultiblock() {
        return multiblock;
    }

    /**
     * @return true when this entry is a STATION-ONLY infrastructure controller (the Satellite Rail Launcher and
     *         the four star-infrastructure components): infrastructure that lives in a Voidbase, never in a
     *         flying ship.
     */
    public boolean isStationOnlyMultiblock() {
        return this == SATELLITE_LAUNCHER || this == STELLAR_INJECTOR
            || this == CONTINUUM_STABILIZER
            || this == STELLAR_LENS
            || this == STABILIZATION_MATRIX;
    }

    /**
     * Technology tier. Gated by the assembler circuit: circuit damage 0-2 = tier 0, 3-5 = tier 1, 6-8 = tier 2,
     * 9+ = tier 3. For the frame tiers this is also the highest cover tier the frame may host.
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

    public long getLogisticsPower() {
        return logisticsPower;
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
        // The classic hull blocks must be placeable.
        if (!CONTROLLER.isPlaceable() || !FRAME.isPlaceable()) {
            throw new IllegalStateException("Controller and Frame must be placeable full blocks");
        }
        // Every multiblock component block (controller or casing) is a placeable full block.
        if (Arrays.stream(ALL)
            .anyMatch(c -> c.multiblock && !c.placeable)) {
            throw new IllegalStateException("Multiblock component blocks must be placeable");
        }
        if (Arrays.stream(ALL)
            .anyMatch(
                c -> c.mass < 0 || c.thrust < 0
                    || c.cargoSlots < 0
                    || c.miningPower < 0
                    || c.scanPower < 0
                    || c.constructionPower < 0
                    || c.starlifterPower < 0
                    || c.logisticsPower < 0
                    || c.energyBuffer < 0
                    || c.energyDraw < 0
                    || c.energyGen < 0
                    || c.integrity < 0)) {
            throw new IllegalStateException("Negative component stat");
        }
    }
}
