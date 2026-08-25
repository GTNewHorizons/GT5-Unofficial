package tectech.voidcraft.uss;

/**
 * The executor's game seam (programming framework, Phase B).
 *
 * <p>
 * The executor and the command handlers are pure: they only ever talk to a context. The game-side
 * implementation (Phase C) bridges the ship + USS + world; tests use a fake. The contract:
 * <ul>
 * <li>methods must not throw — corrupt input degrades (null / {@code ""} / false), never crashes the tick loop;</li>
 * <li>{@link #resolve(USSValue)} is the single value-resolution funnel (LITERAL direct, VAR via the variable
 * space, STAT via the stat registry);</li>
 * <li>long-running work (travel legs, work legs) is started here and polled with {@link #legComplete()} — the
 * legs themselves tick in real time on the game side (server-authoritative), the executor only steps every
 * {@link USSProgramExecutor#STEP_TICKS} ticks;</li>
 * <li>implementations are expected to clamp slots (0..255) themselves (the variable space does).</li>
 * </ul>
 */
public interface USSExecutionContext {

    /**
     * Resolve a program value to its string.
     *
     * @param value the value (LITERAL / VAR / STAT)
     * @return the string (never null — corrupt input degrades to {@code ""})
     */
    String resolve(USSValue value);

    /**
     * @param slot the USS variable slot (the implementation clamps to 0..255)
     * @return the slot's string ({@code ""} when unwritten or out of range)
     */
    String readVar(int slot);

    /**
     * @param slot  the USS variable slot (the implementation clamps to 0..255)
     * @param value the string (null → {@code ""})
     */
    void writeVar(int slot, String value);

    /**
     * @param stat the ship stat (unknown ids → {@code ""})
     * @return the stat's string at this moment (never null)
     */
    String stat(USSShipStat stat);

    /** @return the ship's current position (never null) */
    USSPosition position();

    /**
     * @param dest the destination
     * @return the distance in blocks from {@link #position()}
     */
    double distanceTo(USSPosition dest);

    /**
     * Resolve a MOVE target to a destination (the target string is one of
     * {@link USSProgramDefaults}' {@code TARGET_*} values; e.g. "STAR", "PLANET" + index,
     * "NEAREST_PLANET", "RIPPLE" + index, "RIPPLE_UNSCANNED", "SHIP" + index, "HOME").
     *
     * @param target the target string
     * @param index  the target index (0 for index-less targets)
     * @return the destination, or null when unresolvable (the executor then SKIPS the instruction)
     */
    USSPosition resolveMoveTarget(String target, int index);

    /**
     * Start a leg.
     *
     * @param dest the destination (the leg's target position)
     * @param dist the distance in blocks
     * @param work true = a WORK leg (work in place at the target), false = a TRAVEL leg (MOVE)
     * @return true when the leg started (false → the executor SKIPS the instruction)
     */
    boolean startLeg(USSPosition dest, double dist, boolean work);

    /** @return true when the active leg (travel or work) has completed */
    boolean legComplete();

    /**
     * Deterministic pseudo-random (seeded per ship on the game side) for target picking.
     *
     * @param bound the bound (exclusive, like {@code java.util.Random})
     * @return a value in {@code [0, bound)} (0 when bound ≤ 0)
     */
    int nextInt(int bound);

    /**
     * A framework log line (the game side: LOGGER; tests: a capturing list).
     *
     * @param message the line (never null)
     */
    void log(String message);
}
