package tectech.voidcraft.uss;

/**
 * The ship STAT registry (programming framework, Phase B).
 *
 * <p>
 * User spec: "The executor of the instructions has access to the Voidcraft stats (Cargo, position, etc...)".
 * A stat is a named, always-STRING reading of the ship/USS at the moment it is resolved (the values of this
 * framework are always strings — see {@link USSValue}). Conditions compare stats as strings; numeric ones use
 * the LT/GT numeric rule.
 *
 * <p>
 * Adding a stat = adding one entry here (the registry is the modularity seam; the program format never
 * changes). The game-side context (Phase C) resolves the ids to live values; tests use a fake.
 */
public enum USSShipStat {

    /** Cargo units in use (stringified int). */
    CARGO_USED(0),
    /** Cargo units free (stringified int). */
    CARGO_FREE(1),
    /** Whether the hold is full (stringified boolean: "1"/"0"). */
    CARGO_FULL(2),
    /** The ship state name (OUTBOUND / MINING / RETURNING / DOCKED / HOVERING). */
    STATE(3),
    /** The current/last target name (e.g. "NEAREST_PLANET", "STAR", "HOME"). */
    TARGET(4),
    /** Current position X (stringified number). */
    POSITION_X(5),
    /** Current position Y (stringified number). */
    POSITION_Y(6),
    /** Current position Z (stringified number). */
    POSITION_Z(7),
    /** Blocks to the current leg's destination (stringified number). */
    DIST_TO_TARGET(8),
    /** The ship speed (stringified number). */
    SPEED(9),
    /** Ticks elapsed in the current leg (stringified int). */
    TICKS_IN_LEG(10),
    /** Still-unscanned ripple points in this USS (stringified int). */
    RIPPLES_UNSCANNED(11);

    private final int id;

    USSShipStat(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static USSShipStat byId(int id) {
        for (USSShipStat stat : values()) {
            if (stat.id == id) return stat;
        }
        return null;
    }
}
