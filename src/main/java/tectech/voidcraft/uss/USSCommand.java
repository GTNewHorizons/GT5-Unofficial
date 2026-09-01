package tectech.voidcraft.uss;

/**
 * Stable ids of the built-in Voidcraft commands (programming framework).
 *
 * <p>
 * Programs reference commands by these ids (see {@link USSNode#command(int, net.minecraft.nbt.NBTTagCompound)});
 * the command REGISTRY that maps ids to handlers is Phase B (the modularity seam — a new command is one new
 * handler plus an id here; the program format never changes).
 */
public final class USSCommand {

    /** Highest built-in command id (the GUI's stat-line arrays are sized to this + 1). */
    public static final int MAX_ID = 12;

    /**
     * Fly to a target and hover there (the ONE "Go to" — user spec). Params: {@code target} = STAR / PLANET /
     * NEAREST_PLANET / RANDOM_PLANET / RIPPLE / RIPPLE_UNSCANNED / SHIP / HOME (see {@link USSProgramDefaults})
     * plus an optional {@code index} for PLANET / RIPPLE / SHIP.
     */
    public static final int MOVE = 0;
    /**
     * MINE the current target planet until the work leg completes (the Miner's work command — a work leg of kind
     * {@link USSWorkKind#MINE}, mined at the ship's mining power).
     */
    public static final int MINE = 1;
    /** WRITE: write a string into the USS variable space. Params: {@code value} (string), {@code slot} (0..255). */
    public static final int WRITE = 2;
    /** READ: copy one USS variable slot into another. Params: {@code from} (0..255), {@code to} (0..255). */
    public static final int READ = 3;
    /** WAIT: hold for a number of ticks. Params: {@code ticks}. */
    public static final int WAIT = 4;
    /** STOP: terminate the program (the ship HOLDS — no implicit return, user decision). */
    public static final int STOP = 5;
    /**
     * CONSTRUCT: build a Voidbase at the current hover point (Voidbase construction framework). A constructor
     * carrying a Voidbase blueprint (the parts it carries are its cargo) reaches its target and builds/fills the
     * construction site there. Params: {@code target} (the anchor body — STAR / PLANET / RIPPLE, with {@code index} for
     * PLANET /
     * RIPPLE). On a ship this is the CONSTRUCT leg of the Constructor mission; a completed site spawns the base.
     */
    public static final int CONSTRUCT = 6;
    /**
     * REPAIR: restore integrity over time, drawing the executor's energy buffer (repair work command). A
     * Voidbase runs this at its anchor to top up its own integrity time limit, or to repair a fleet member
     * standing at the station; a Voidcraft SKIPs the command (a repair bay is a station capability). Requires a
     * {@link tectech.voidcraft.ship.VoidcraftCoverComponent#REPAIR_BAY} cover; each second of repair restores one
     * integrity at the repair draw. Params: {@code target} (optional — empty or {@code SELF} = the executing
     * entity itself; otherwise a fleet index or name, resolved with the same pattern and shared-location rule as
     * SEND / TAKE).
     */
    public static final int REPAIR = 7;
    /**
     * SCAN the current ripple point until the work leg completes (the Explorer's work command — a work leg of
     * kind {@link USSWorkKind#SCAN}, scanned at the ship's scan power; the yield is the ripple reveal, not cargo).
     */
    public static final int SCAN = 8;
    /**
     * SIPHON the star until the work leg completes (the Starlifter's work command — a work leg of kind
     * {@link USSWorkKind#SIPHON}, siphoned at the ship's starlifter power; the yield is the star cargo).
     */
    public static final int SIPHON = 9;
    /**
     * SEND cargo from this ship to the target ship (ship-to-ship cargo transfer — the transfer always succeeds:
     * there is no refusal from the target). Params: {@code amount} (units, default -1 = ALL), {@code filter}
     * (material name, default "*" = match all), {@code target} (the target ship — a fleet index or a ship name).
     * Requires the two ships to share a LOCATION (a planet orbit, the star, a ripple site, or one of the two
     * ships' own position) — and NEITHER to be mid-MOVE (a ship en route reads its destination's location but is
     * not settled there, so the transfer is blocked until it arrives); the rate is the ship's logistics power
     * (1 power = 1 cargo unit per second).
     */
    public static final int SEND = 10;
    /**
     * TAKE cargo from the target ship into this ship (the inverse of {@link #SEND}; the same params, the same
     * shared-location rule, the same logistics-power rate — always succeeds, no refusal from the target).
     */
    public static final int TAKE = 11;
    /**
     * STABILIZE: run a fixed-duration Hyperdimensional Stabilization Matrix window (a Voidbase-only station
     * command like {@link #REPAIR} — the base polls its own side-effect, no leg is started). Params: {@code
     * ticks} (long, the window length; 0 / absent = no-op). While in flight the base pays the per-tick matrix
     * draw from its energy buffer (a shortfall stalls the window, travel semantics) and consumes one Field
     * Generator (the GregTech tiered component) every interval from its hold (the UXV tier over the UMV tier);
     * the leg's expiry WEIGHT is the tier of the last Field Generator consumed (UMV = 1, UXV = 2). The command
     * is skipped when the executing base's blueprint carries no STABILIZATION_MATRIX, it is not anchored to a
     * revealed ripple, the Continuum Stabilizer on the anchor ripple is not fully built, or no Field Generator
     * is on board.
     */
    public static final int STABILIZE = 12;

    private USSCommand() {}

    /**
     * The block label a command id renders as in the program view ("MOVE" / "MINE" / …); {@code CMD<id>} for an
     * unknown id (the view's fallback, so a new command is never an empty block).
     */
    public static String label(int commandId) {
        switch (commandId) {
            case MOVE:
                return "MOVE";
            case MINE:
                return "MINE";
            case WRITE:
                return "WRITE";
            case READ:
                return "READ";
            case WAIT:
                return "WAIT";
            case STOP:
                return "STOP";
            case CONSTRUCT:
                return "CONSTRUCT";
            case REPAIR:
                return "REPAIR";
            case SCAN:
                return "SCAN";
            case SIPHON:
                return "SIPHON";
            case SEND:
                return "SEND";
            case TAKE:
                return "TAKE";
            case STABILIZE:
                return "STABILIZE";
            default:
                return "CMD" + commandId;
        }
    }
}
