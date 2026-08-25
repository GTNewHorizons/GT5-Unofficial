package tectech.voidcraft.uss;

/**
 * The pilot's game seam (programming framework, Phase C).
 *
 * <p>
 * The {@link USSShipPilot} is bare-JVM (executor + ship + this interface), so it is unit-testable like the
 * Phase B executor. The game side (the {@code MTEUnstableSolarSystem}) implements this seam: it knows the USS
 * (variable space, ripple field, ore reserves, star type), the fleet (other ships for {@code SHIP} rendezvous),
 * and the world (logging, fleet resync, cargo build).
 *
 * <p>
 * Contract (same spirit as {@link USSExecutionContext}): methods must not throw — corrupt input degrades (null
 * / {@code 0} / no-op), never crashes the ship's tick.
 */
public interface USSPilotWorld {

    // region USS variable space (the ship's in/out channel — shared by the whole fleet)

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
     * @return the number of ripple points still UNREVEALED (the {@code RIPPLES_UNSCANNED} stat); 0 when the
     *         system has no ripple field (COLD star) or every point is revealed
     */
    int unscannedRipples();

    // endregion

    // region leg + target resolution (game-side: USS system, fleet, world)

    /**
     * Resolve a MOVE target to a destination (the game-side version of
     * {@link USSExecutionContext#resolveMoveTarget}).
     *
     * @param target the target string (one of the {@link USSProgramDefaults} {@code TARGET_*} values)
     * @param index  the target index (0 for index-less targets)
     * @param ship   the ship (its position — for {@code NEAREST_PLANET} / {@code SHIP} — and its roles)
     * @return the resolution, or null when unresolvable (the pilot then SKIPs the instruction)
     */
    USSTargetResult resolveTarget(String target, int index, VoidcraftActiveShip ship);

    /**
     * The duration of a leg for THIS ship (the game-side version of the Phase B leg start): the same tables the
     * client uses ({@link USSConstants}) so server and client animate the same duration.
     *
     * @param work     true = a WORK leg (role-aware: Explorer scans, everything else mines)
     * @param ship     the ship (speed / mining power / scan power / roles)
     * @param distance the leg's distance in blocks (travel legs only; ignored for work legs)
     * @return the leg duration in ticks (always &gt; 0)
     */
    long legTicks(boolean work, VoidcraftActiveShip ship, double distance);

    // endregion

    // region side-effects (the WORK leg's "done" path — the old tickShips cargo/reveal gate)

    /**
     * A WORK leg just completed. The game side applies the leg's yield EXACTLY ONCE: an Explorer reveal (the
     * ripple point was scanned), a Starlifter cargo build, or a Miner cargo build (clamped by the ship's hold).
     *
     * @param ship        the ship (the cargo is set on its hold / cargo)
     * @param targetKind  the kind of body the ship worked — the MOVE {@code target} string that preceded the
     *                    WORK (e.g. {@code "STAR"}, {@code "PLANET"}); may be null (a WORK with no preceding
     *                    MOVE — no world-side yield)
     * @param targetIndex the RESOLVED body index (planet i / ripple i; -1 star; -2 ship; -3 home)
     */
    void onWorkComplete(VoidcraftActiveShip ship, String targetKind, int targetIndex);

    /**
     * A framework log line (the game side: LOGGER, pass-26 style; tests: a capturing list).
     *
     * @param ship    the ship (for the log's identity)
     * @param message the line (never null)
     */
    void log(VoidcraftActiveShip ship, String message);

    // endregion
}
