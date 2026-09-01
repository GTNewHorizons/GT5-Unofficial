package tectech.voidcraft.ship;

import java.util.Optional;

/**
 * A single Voidcraft <i>cover</i> component — a compact part that mounts on any face of a Voidcraft component block
 * (and, like a GT cover, replaces that face's texture) and contributes its own stats to the ship.
 *
 * <p>
 * Covers are the dense alternative to full component blocks: every block has six faces, so one hull block can carry
 * up to six parts. That lets far more complex voidcraft fit into a smaller footprint.
 *
 * <p>
 * Thrust covers (the engine family: {@link #THRUSTER_NOZZLE}, {@link #ION_THRUSTER}, {@link #FUSION_TORCH},
 * {@link #ANTIMATTER_ENGINE}) count toward the ship's single thrust value only when mounted on the ship's BACK face
 * (−Z, the assembler side, {@link VoidcraftBlueprint#BACK_FACE}) — exhaust out the rear pushes the ship forward
 * (the nose is the far end, grid +Z); an engine mounted anywhere else is dead weight (see
 * {@link VoidcraftBlueprint#computeStats()}). A ship may mount exactly ONE engine type
 * ({@code voidcraft_engine_mismatch}); the engine types that burn fuel ({@code requiresFuel}) draw the ship's fuel
 * tank while travelling.
 *
 * <p>
 * Frame tier rule: a cover mounted on a face of a frame block is only legal when its tier is at or below that
 * frame's tier (a tier-3 cover needs a tier-4 frame). The tier of the controller does not gate covers.
 *
 * <p>
 * Reactor covers ({@link #FUSION_REACTOR}, {@link #ANTIMATTER_REACTOR}) generate large flat energy in flight but
 * cost a launch fee of reactor fuel (their {@code launchFuel}, in mB) at the Gateway, scaled by the number of
 * reactors of each type.
 *
 * <p>
 * PASS 23 (user spec): covers are the PRIMARY components — ALL ship functionality (thrust, cargo, mining, scanning,
 * construction, starlifting, energy) is delivered by covers mounted on the placeable full blocks: the Voidcraft
 * Controller and the frame tiers (the mostly-transparent framebox hull blocks whose purpose is to accept exactly
 * these covers). The old full-block engines/cargos/... are no longer placeable — they survive only as the cover
 * definitions' mirror targets.
 *
 * <p>
 * There is intentionally no controller cover: the ship's brain must be a full block.
 *
 * <p>
 * Grid encoding in {@link VoidcraftBlueprint#coverGrid}: {@code id + 1} (0 = no cover on that face).
 */
public enum VoidcraftCoverComponent {

    THRUSTER_NOZZLE(0, "Voidcraft Thruster Nozzle", "tt.voidcraft.cover.thruster_nozzle", VoidcraftComponent.ENGINE, 0,
        3, 120, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, VoidcraftEngineType.STANDARD, 0, 0, 40, 0),

    ARMOR_PLATE(1, "Voidcraft Armor Plate", "tt.voidcraft.cover.armor_plate", VoidcraftComponent.FRAME, 0, 2, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 500, VoidcraftEngineType.NONE, 0, 0, 0, 0),

    CARGO_POD(2, "Voidcraft Cargo Pod", "tt.voidcraft.cover.cargo_pod", VoidcraftComponent.CARGO_BAY, 0, 6, 0, 200, 0,
        0, 0, 0, 0, 0, 0, 0, 0, VoidcraftEngineType.NONE, 0, 0, 0, 0),

    MINING_ARRAY(3, "Voidcraft Mining Array", "tt.voidcraft.cover.mining_array", VoidcraftComponent.MINING_CENTRE, 1, 8,
        0, 0, 40, 0, 0, 0, 0, 8, 0, 0, 0, VoidcraftEngineType.NONE, 0, 0, 0, 0),

    STAR_SIPHON(4, "Voidcraft Star Siphon", "tt.voidcraft.cover.star_siphon", VoidcraftComponent.STARLIFTER_ARRAY, 2,
        16, 0, 0, 0, 0, 0, 40, 0, 16, 0, 0, 0, VoidcraftEngineType.NONE, 0, 0, 0, 0),

    SCANNER_DISH(5, "Voidcraft Scanner Dish", "tt.voidcraft.cover.scanner_dish", VoidcraftComponent.SPACETIME_SCANNER,
        2, 6, 0, 0, 0, 40, 0, 0, 0, 8, 0, 0, 0, VoidcraftEngineType.NONE, 0, 0, 0, 0),

    FABRICATOR_UNIT(6, "Voidcraft Fabricator Unit", "tt.voidcraft.cover.fabricator_unit",
        VoidcraftComponent.CONSTRUCTION_ARM, 2, 10, 0, 0, 0, 0, 40, 0, 0, 8, 0, 0, 0, VoidcraftEngineType.NONE, 0, 0, 0,
        0),

    POWER_CELL(7, "Voidcraft Power Cell", "tt.voidcraft.cover.power_cell", VoidcraftComponent.REACTOR, 1, 4, 0, 0, 0, 0,
        0, 0, 0, 0, 400_000, 0, 0, VoidcraftEngineType.NONE, 0, 0, 0, 0),

    /**
     * The repair bay (Voidbase construction framework): the repair work command restores the station's integrity
     * over time while drawing this cover's energy. A base without a repair bay cannot repair itself.
     */
    REPAIR_BAY(8, "Voidcraft Repair Bay", "tt.voidcraft.cover.repair_bay", VoidcraftComponent.REPAIR_BAY, 2, 12, 0, 0,
        0, 0, 0, 0, 0, 2_000, 0, 0, 0, VoidcraftEngineType.NONE, 0, 0, 0, 0),

    /**
     * The solar panel — the first energy-generating component: a flat energy generation rate per game tick into
     * the station's energy buffer (star-independent in this version).
     */
    SOLAR_PANEL(9, "Voidcraft Solar Panel", "tt.voidcraft.cover.solar_panel", VoidcraftComponent.SOLAR_PANEL, 2, 8, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 2_000, 0, VoidcraftEngineType.NONE, 0, 0, 0, 0),

    /**
     * The Cargo Drone Bay (ship-to-ship cargo transfer): the SEND / TAKE commands move cargo between two ships that
     * share a location, at the ship's logistics power (1 power = 1 cargo unit per second).
     */
    CARGO_DRONE_BAY(10, "Voidcraft Cargo Drone Bay", "tt.voidcraft.cover.cargo_drone_bay",
        VoidcraftComponent.CARGO_DRONE_BAY, 2, 8, 0, 0, 0, 0, 0, 0, 40, 0, 0, 0, 0, VoidcraftEngineType.NONE, 0, 0, 0,
        0),

    /**
     * The Fusion Reactor: high-output flat energy generation (no fuel burned in flight). A ship carrying it must pay
     * its {@code launchFuel} (Deuterium, mB per reactor) at the Gateway before it may launch.
     */
    FUSION_REACTOR(11, "Voidcraft Fusion Reactor", "tt.voidcraft.cover.fusion_reactor",
        VoidcraftComponent.FUSION_REACTOR, 2, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5_000, 0, VoidcraftEngineType.NONE, 0,
        5_000, 0, 0),

    /**
     * The Antimatter Reactor: the highest-output energy source in the catalog (no fuel burned in flight). A ship
     * carrying it must pay its {@code launchFuel} (Semi-Stable Antimatter, mB per reactor) at the Gateway.
     */
    ANTIMATTER_REACTOR(12, "Voidcraft Antimatter Reactor", "tt.voidcraft.cover.antimatter_reactor",
        VoidcraftComponent.ANTIMATTER_REACTOR, 3, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25_000, 0, VoidcraftEngineType.NONE, 0,
        2_500, 0, 0),

    /**
     * Ion Thruster: the fuel-burning engine family's baseline. Higher thrust than the baseline nozzles but burns
     * Xenon (liquid) while travelling. A ship may carry exactly one engine type.
     */
    ION_THRUSTER(13, "Voidcraft Ion Thruster", "tt.voidcraft.cover.ion_thruster", VoidcraftComponent.ION_THRUSTER, 1, 3,
        150, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, VoidcraftEngineType.ION, 0, 0, 40, 10),

    /**
     * Fusion Torch: high-thrust fuel-burning engine (burns Water while travelling). A ship may carry exactly one
     * engine type.
     */
    FUSION_TORCH(14, "Voidcraft Fusion Torch", "tt.voidcraft.cover.fusion_torch", VoidcraftComponent.FUSION_TORCH, 2, 6,
        400, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, VoidcraftEngineType.FUSION, 0, 0, 60, 50),

    /**
     * Antimatter Engine: the highest-thrust engine in the catalog (burns Semi-Stable Antimatter while travelling). A
     * ship may carry exactly one engine type.
     */
    ANTIMATTER_ENGINE(15, "Voidcraft Antimatter Engine", "tt.voidcraft.cover.antimatter_engine",
        VoidcraftComponent.ANTIMATTER_ENGINE, 3, 10, 900, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, VoidcraftEngineType.ANTIMATTER,
        0, 0, 100, 200),

    /**
     * Fuel Storage: a dedicated tank (separate from the cargo hold) holding the fuel required by the ship's engine
     * type. The Gateway only launches the ship when the tank is full; the fuel is consumed while travelling.
     */
    FUEL_STORAGE(16, "Voidcraft Fuel Storage", "tt.voidcraft.cover.fuel_storage", VoidcraftComponent.FUEL_STORAGE, 0, 8,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, VoidcraftEngineType.NONE, 10_000, 0, 0, 0);

    /** All covers, in id order. */
    public static final VoidcraftCoverComponent[] ALL = values();

    private final int id;
    private final String displayName;
    private final String langKey;
    private final VoidcraftComponent mirroredComponent;
    private final int tier;
    private final long mass;
    private final long thrust;
    private final long cargoSlots;
    private final long miningPower;
    private final long scanPower;
    private final long constructionPower;
    private final long starlifterPower;
    /** Logistics power (the Cargo Drone Bay): 1 power = 1 cargo unit transferred per second (SEND / TAKE). */
    private final long logisticsPower;
    private final long energyDraw;
    private final long energyBuffer;
    /** Energy generated per game tick (solar panel, reactors; 0 for everything else). */
    private final long energyGen;
    private final long integrity;
    /** The engine family this cover belongs to (engine covers); {@link VoidcraftEngineType#NONE} otherwise. */
    private final VoidcraftEngineType engineType;
    /** Fuel tank capacity contributed, in mB (the Fuel Storage; 0 for everything else). */
    private final long fuelCapacity;
    /** Reactor launch fuel per mounted unit, in mB (the reactor covers; 0 for everything else). */
    private final long launchFuel;
    /** Travel energy draw per game tick of one mounted unit (EU/tick; 0 for the non-engine covers). */
    private final long travelEnergyPerTick;
    /** Travel fuel draw per game tick of one mounted unit (mB/tick; 0 for the fuel-less families). */
    private final long travelFuelPerTick;

    VoidcraftCoverComponent(int id, String displayName, String langKey, VoidcraftComponent mirroredComponent, int tier,
        long mass, long thrust, long cargoSlots, long miningPower, long scanPower, long constructionPower,
        long starlifterPower, long logisticsPower, long energyDraw, long energyBuffer, long energyGen, long integrity,
        VoidcraftEngineType engineType, long fuelCapacity, long launchFuel, long travelEnergyPerTick,
        long travelFuelPerTick) {
        this.id = id;
        this.displayName = displayName;
        this.langKey = langKey;
        this.mirroredComponent = mirroredComponent;
        this.tier = tier;
        this.mass = mass;
        this.thrust = thrust;
        this.cargoSlots = cargoSlots;
        this.miningPower = miningPower;
        this.scanPower = scanPower;
        this.constructionPower = constructionPower;
        this.starlifterPower = starlifterPower;
        this.logisticsPower = logisticsPower;
        this.energyDraw = energyDraw;
        this.energyBuffer = energyBuffer;
        this.energyGen = energyGen;
        this.integrity = integrity;
        this.engineType = engineType;
        this.fuelCapacity = fuelCapacity;
        this.launchFuel = launchFuel;
        this.travelEnergyPerTick = travelEnergyPerTick;
        this.travelFuelPerTick = travelFuelPerTick;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Language key (tt.voidcraft.cover.*). */
    public String getLangKey() {
        return langKey;
    }

    /**
     * The full component block this cover is the compact version of.
     * Used for icon mapping (covers reuse the component's icon set) and for thrust semantics.
     */
    public VoidcraftComponent getMirroredComponent() {
        return mirroredComponent;
    }

    public int getTier() {
        return tier;
    }

    public long getMass() {
        return mass;
    }

    /**
     * Thrust magnitude (pass 18/24: counted toward the ship only when mounted on the back face, −Z = the assembler
     * side; 0 for non-thrusters).
     */
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

    public long getEnergyDraw() {
        return energyDraw;
    }

    public long getEnergyBuffer() {
        return energyBuffer;
    }

    public long getEnergyGen() {
        return energyGen;
    }

    public long getIntegrity() {
        return integrity;
    }

    /**
     * @return the engine family this cover belongs to (a cover with {@code engineType != NONE} is a thruster of that
     *         family); {@link VoidcraftEngineType#NONE} for non-engine covers.
     */
    public VoidcraftEngineType getEngineType() {
        return engineType;
    }

    /** @return true when this cover is an engine (thruster) cover of any family. */
    public boolean isEngine() {
        return engineType != VoidcraftEngineType.NONE;
    }

    /**
     * @return the fuel tank capacity this cover contributes, in mB (the Fuel Storage; 0 otherwise).
     */
    public long getFuelCapacity() {
        return fuelCapacity;
    }

    /**
     * @return true when this cover is a reactor that pays a launch fuel fee at the Gateway
     *         ({@code launchFuel > 0}).
     */
    public boolean isReactor() {
        return launchFuel > 0L;
    }

    /**
     * @return the reactor launch fuel per mounted unit, in mB (reactor covers; 0 otherwise).
     */
    public long getLaunchFuel() {
        return launchFuel;
    }

    /**
     * @return the travel energy draw per game tick of ONE mounted unit (EU/tick, engine covers; 0 for the
     *         non-engines) — the per-thruster half of the ship's static travel burn.
     */
    public long getTravelEnergyPerTick() {
        return travelEnergyPerTick;
    }

    /**
     * @return the travel fuel draw per game tick of ONE mounted unit (mB/tick; 0 for the fuel-less families and
     *         the non-engines).
     */
    public long getTravelFuelPerTick() {
        return travelFuelPerTick;
    }

    /** Grid value stored in the blueprint cover grid: {@code id + 1} (0 = empty face). */
    public int toGridValue() {
        return id + 1;
    }

    /** Recover the cover from a grid value (0 = empty face, out of range = invalid). */
    public static Optional<VoidcraftCoverComponent> fromGridValue(int value) {
        int id = value - 1;
        if (id < 0 || id >= ALL.length) {
            return Optional.empty();
        }
        return Optional.of(ALL[id]);
    }

    /**
     * The engine cover of a thruster family (each family has exactly one engine cover — the single-engine-family
     * rule makes the family's cover stats the ship's travel economics).
     *
     * @param engineTypeId the family id ({@link VoidcraftEngineType})
     * @return the family's engine cover, or empty for {@link VoidcraftEngineType#NONE}
     */
    public static Optional<VoidcraftCoverComponent> engineCoverOf(int engineTypeId) {
        VoidcraftEngineType type = VoidcraftEngineType.byId(engineTypeId);
        if (type == VoidcraftEngineType.NONE) {
            return Optional.empty();
        }
        for (VoidcraftCoverComponent cover : ALL) {
            if (cover.isEngine() && cover.getEngineType() == type) {
                return Optional.of(cover);
            }
        }
        return Optional.empty();
    }

    static {
        for (VoidcraftCoverComponent component : ALL) {
            if (component.id != ordinalOf(component)) {
                throw new IllegalStateException("Cover id mismatch: " + component);
            }
            if (component.mass < 0 || component.thrust < 0
                || component.cargoSlots < 0
                || component.miningPower < 0
                || component.scanPower < 0
                || component.constructionPower < 0
                || component.starlifterPower < 0
                || component.logisticsPower < 0
                || component.energyDraw < 0
                || component.energyBuffer < 0
                || component.energyGen < 0
                || component.integrity < 0
                || component.fuelCapacity < 0
                || component.launchFuel < 0
                || component.travelEnergyPerTick < 0
                || component.travelFuelPerTick < 0) {
                throw new IllegalStateException("Cover stats must not be negative: " + component);
            }
            if (component.tier < 0 || component.tier > VoidcraftComponentRegistry.MAX_TIER) {
                throw new IllegalStateException("Cover tier out of range: " + component);
            }
            if (component.getMirroredComponent() == VoidcraftComponent.CONTROLLER) {
                throw new IllegalStateException("No cover may mirror the controller: " + component);
            }
            // The single-engine-family rule and the per-family travel economics both rely on each family having
            // exactly ONE engine cover.
            if (component.isEngine()) {
                long familyCount = 0L;
                for (VoidcraftCoverComponent other : ALL) {
                    if (other.isEngine() && other.getEngineType() == component.getEngineType()) {
                        familyCount++;
                    }
                }
                if (familyCount != 1L) {
                    throw new IllegalStateException("Engine family must have exactly one cover: " + component);
                }
            }
        }
    }

    private static int ordinalOf(VoidcraftCoverComponent component) {
        for (int i = 0; i < ALL.length; i++) {
            if (ALL[i] == component) {
                return i;
            }
        }
        return -1;
    }
}
