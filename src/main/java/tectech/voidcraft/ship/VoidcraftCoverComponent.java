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
 * Thrust covers ({@link #THRUSTER_NOZZLE}) count toward the ship's single thrust value only when mounted on the
 * ship's BACK face (−Z, the assembler side, {@link VoidcraftBlueprint#BACK_FACE}) — exhaust out the rear pushes
 * the ship forward (the nose is the far end, grid +Z); a nozzle mounted anywhere else is dead weight (see
 * {@link VoidcraftBlueprint#computeStats()}).
 *
 * <p>
 * PASS 23 (user spec): covers are the PRIMARY components — ALL ship functionality (thrust, cargo, mining, scanning,
 * construction, starlifting, energy) is delivered by covers mounted on the two placeable full blocks: the
 * Voidcraft Controller and the Voidcraft Frame (the renamed Utility Block, a mostly-transparent framebox whose
 * purpose is to accept exactly these covers). The old full-block engines/cargos/... are no longer placeable —
 * they survive only as the cover definitions' mirror targets.
 *
 * <p>
 * There is intentionally no controller cover: the ship's brain must be a full block.
 *
 * <p>
 * Grid encoding in {@link VoidcraftBlueprint#coverGrid}: {@code id + 1} (0 = no cover on that face).
 */
public enum VoidcraftCoverComponent {

    THRUSTER_NOZZLE(0, "Voidcraft Thruster Nozzle", "tt.voidcraft.cover.thruster_nozzle", VoidcraftComponent.ENGINE, 0,
        3, 40, 0, 0, 0, 0, 0, 2, 0, 0),

    ARMOR_PLATE(1, "Voidcraft Armor Plate", "tt.voidcraft.cover.armor_plate", VoidcraftComponent.FRAME, 0, 2, 0, 0, 0,
        0, 0, 0, 0, 0, 5),

    CARGO_POD(2, "Voidcraft Cargo Pod", "tt.voidcraft.cover.cargo_pod", VoidcraftComponent.CARGO_BAY, 0, 6, 0, 20, 0, 0,
        0, 0, 0, 0, 0),

    MINING_ARRAY(3, "Voidcraft Mining Array", "tt.voidcraft.cover.mining_array", VoidcraftComponent.MINING_CENTRE, 1, 8,
        0, 0, 40, 0, 0, 0, 8, 0, 0),

    STAR_SIPHON(4, "Voidcraft Star Siphon", "tt.voidcraft.cover.star_siphon", VoidcraftComponent.STARLIFTER_ARRAY, 2,
        16, 0, 0, 0, 0, 0, 40, 16, 0, 0),

    SCANNER_DISH(5, "Voidcraft Scanner Dish", "tt.voidcraft.cover.scanner_dish", VoidcraftComponent.SPACETIME_SCANNER,
        2, 6, 0, 0, 0, 40, 0, 0, 8, 0, 0),

    FABRICATOR_UNIT(6, "Voidcraft Fabricator Unit", "tt.voidcraft.cover.fabricator_unit",
        VoidcraftComponent.CONSTRUCTION_ARM, 2, 10, 0, 0, 0, 0, 40, 0, 8, 0, 0),

    POWER_CELL(7, "Voidcraft Power Cell", "tt.voidcraft.cover.power_cell", VoidcraftComponent.REACTOR, 1, 4, 0, 0, 0, 0,
        0, 0, 0, 400_000, 0);

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
    private final long energyDraw;
    private final long energyBuffer;
    private final long integrity;

    VoidcraftCoverComponent(int id, String displayName, String langKey, VoidcraftComponent mirroredComponent, int tier,
        long mass, long thrust, long cargoSlots, long miningPower, long scanPower, long constructionPower,
        long starlifterPower, long energyDraw, long energyBuffer, long integrity) {
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
        this.energyDraw = energyDraw;
        this.energyBuffer = energyBuffer;
        this.integrity = integrity;
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
     * Used for icon mapping (covers reuse the component's icon set) and for role/thrust semantics.
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

    public long getEnergyDraw() {
        return energyDraw;
    }

    public long getEnergyBuffer() {
        return energyBuffer;
    }

    public long getIntegrity() {
        return integrity;
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
                || component.energyDraw < 0
                || component.energyBuffer < 0
                || component.integrity < 0) {
                throw new IllegalStateException("Cover stats must not be negative: " + component);
            }
            if (component.tier < 0 || component.tier > VoidcraftComponentRegistry.MAX_TIER) {
                throw new IllegalStateException("Cover tier out of range: " + component);
            }
            if (component.getMirroredComponent() == VoidcraftComponent.CONTROLLER) {
                throw new IllegalStateException("No cover may mirror the controller: " + component);
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
