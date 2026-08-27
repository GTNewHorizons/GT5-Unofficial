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

    /**
     * Fly to a target and hover there (the ONE "Go to" — user spec). Params: {@code target} = STAR / PLANET /
     * NEAREST_PLANET / RANDOM_PLANET / RIPPLE / RIPPLE_UNSCANNED / SHIP / HOME (see {@link USSProgramDefaults})
     * plus an optional {@code index} for PLANET / RIPPLE / SHIP.
     */
    public static final int MOVE = 0;
    /** Work at the current target (mine / starlift / Explorer-scan) until the work leg completes. */
    public static final int WORK = 1;
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
     * carrying a Voidbase blueprint + parts loadout reaches its target and builds/fills the construction site
     * there. Params: {@code target} (the anchor body — STAR / PLANET / RIPPLE, with {@code index} for PLANET /
     * RIPPLE). On a ship this is the CONSTRUCT leg of the Constructor mission; a completed site spawns the base.
     */
    public static final int CONSTRUCT = 6;
    /**
     * REPAIR: restore the station's integrity over time, drawing its own energy buffer (repair work command).
     * A Voidbase runs this at its anchor (or a Voidcraft at any hover point) to top up the integrity time limit.
     * Requires a {@link tectech.voidcraft.ship.VoidcraftCoverComponent#REPAIR_BAY} cover; each second of repair
     * restores one integrity at the bay's energy draw. Params: none.
     */
    public static final int REPAIR = 7;

    private USSCommand() {}
}
