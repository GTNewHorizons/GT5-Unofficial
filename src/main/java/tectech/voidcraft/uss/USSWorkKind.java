package tectech.voidcraft.uss;

/**
 * The KIND of a ship's active leg: a travel leg (MOVE) or one of the three work legs (MINE / SCAN / SIPHON).
 *
 * <p>
 * The work kind is owned by the WORK COMMAND that started the leg (the command tells the world side what the ship
 * is doing; the preceding MOVE only says WHERE it is). The pilot persists it with the leg bookkeeping so a
 * chunk-reloaded leg still delivers the right yield.
 *
 * <p>
 * Bare JVM (plain int constants — no NBT, no entity dependencies).
 */
public final class USSWorkKind {

    /** A travel leg (MOVE): OUTBOUND / RETURNING, no work side-effect. */
    public static final int TRAVEL = 0;
    /** Mine the current target planet (the Miner's work leg). */
    public static final int MINE = 1;
    /** Scan the current ripple point (the Explorer's work leg — the yield is the reveal). */
    public static final int SCAN = 2;
    /** Siphon the star (the Starlifter's work leg — the yield is the star cargo). */
    public static final int SIPHON = 3;
    /**
     * The matrix's stabilization leg (STABILIZE) — a Voidbase station side-effect polled by the command itself
     * (like CONSTRUCT / REPAIR), NOT a travel or work leg: {@link #isWork} stays false and {@link #fromCommand}
     * starts no leg for it.
     */
    public static final int STABILIZE = 4;

    private USSWorkKind() {}

    /** @return true for a work leg kind (MINE / SCAN / SIPHON); false for a travel leg. */
    public static boolean isWork(int kind) {
        return kind >= MINE && kind <= SIPHON;
    }

    /**
     * The work kind a command id starts (0 for every non-work command — MOVE / WRITE / READ / WAIT / STOP /
     * CONSTRUCT / REPAIR / STABILIZE are not work legs; CONSTRUCT, REPAIR and STABILIZE poll their own
     * side-effects).
     */
    public static int fromCommand(int commandId) {
        switch (commandId) {
            case USSCommand.MINE:
                return MINE;
            case USSCommand.SCAN:
                return SCAN;
            case USSCommand.SIPHON:
                return SIPHON;
            default:
                return TRAVEL;
        }
    }

    /** @return the kind's block label ("MINE" / "SCAN" / "SIPHON"; "WORK" for a travel leg / unknown kind). */
    public static String name(int kind) {
        switch (kind) {
            case MINE:
                return "MINE";
            case SCAN:
                return "SCAN";
            case SIPHON:
                return "SIPHON";
            default:
                return "WORK";
        }
    }
}
