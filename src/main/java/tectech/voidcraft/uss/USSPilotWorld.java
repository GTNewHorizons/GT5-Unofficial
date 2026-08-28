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
     * The duration of a leg for THIS ship (the game-side version of the leg start): the same tables the
     * client uses ({@link USSConstants}) so server and client animate the same duration.
     *
     * @param workKind the leg's work kind (see {@link USSWorkKind}) — TRAVEL = a travel leg (the distance is
     *                 used); a work kind = a work leg (its table: mining / scan / siphon power)
     * @param ship     the ship (speed / mining / scan / siphon power)
     * @param distance the leg's distance in blocks (travel legs only; ignored for work legs)
     * @return the leg duration in ticks (always &gt; 0)
     */
    long legTicks(int workKind, VoidcraftActiveShip ship, double distance);

    // endregion

    // region side-effects (the WORK leg's "done" path — the old tickShips cargo/reveal gate)

    /**
     * A WORK leg just completed. The game side applies the leg's yield EXACTLY ONCE, keyed by the leg's WORK
     * KIND (owned by the work command — not by the target): a MINE leg delivers the planet cargo, a SCAN leg
     * reveals the ripple point, a SIPHON leg delivers the star cargo. A kind/target mismatch (e.g. a MINE leg at
     * the star) delivers nothing but logs the reason.
     *
     * @param ship        the ship (the cargo is set on its hold / cargo)
     * @param workKind    the completed leg's work kind (see {@link USSWorkKind})
     * @param targetKind  the kind of body the ship worked — the MOVE {@code target} string that preceded the
     *                    work command (e.g. {@code "STAR"}, {@code "PLANET"}); may be null (a work command with
     *                    no preceding MOVE — no world-side yield)
     * @param targetIndex the RESOLVED body index (planet i / ripple i; -1 star; -2 ship; -3 home)
     */
    void onWorkComplete(VoidcraftActiveShip ship, int workKind, String targetKind, int targetIndex);

    /**
     * A framework log line (the game side: LOGGER, pass-26 style; tests: a capturing list).
     *
     * @param ship    the ship (for the log's identity)
     * @param message the line (never null)
     */
    void log(VoidcraftActiveShip ship, String message);

    /**
     * A CONSTRUCT command begins at the ship hover anchor: create or reuse the construction site there (the
     * Voidbase blueprint + parts loadout from the ship payload) and arm its timed part transfer (one part per
     * second per 100 construction power of the ship); a site with nothing left to take settles on the first tick.
     *
     * @param ship        the executing ship (its payload carries the build blueprint + parts loadout)
     * @param targetKind  the ship hover target kind (the MOVE target string; see {@link #resolveTarget})
     * @param targetIndex the RESOLVED hover body index (-1 star)
     * @return true when the work started or has nothing to transfer (the command goes RUNNING and polls
     *         {@link #constructTick}) / false when there is nothing to construct (no hover anchor, no blueprint,
     *         or a base already stands there - the command reports FAILED and SKIPs)
     */
    boolean constructStart(VoidcraftActiveShip ship, String targetKind, int targetIndex);

    /**
     * One machine tick of the ship's CONSTRUCT leg (polled while its CONSTRUCT command is in flight): advances
     * the site's pacing countdown, deposits one part per ticksPerItem from the ship's loadout (the site takes
     * only what it still needs), and spawns the Voidbase when the site completes.
     *
     * @param ship        the executing ship
     * @param targetKind  the ship hover target kind (the MOVE target string; see {@link #resolveTarget})
     * @param targetIndex the RESOLVED hover body index (-1 star)
     * @return true when construction is still running / false when the leg is over (the command reports DONE)
     */
    boolean constructTick(VoidcraftActiveShip ship, String targetKind, int targetIndex);

    /**
     * Probe whether a base can start repairing (the repair work command at a station): true when the station has
     * integrity to restore and energy to spend (the command goes RUNNING); false - the command SKIPs.
     *
     * @param base the station being repaired (its own program runs REPAIR at its anchor)
     * @return true when repair can run
     */
    boolean baseRepairStart(VoidcraftActiveBase base);

    /**
     * One REPAIR tick on a station (polled while its REPAIR command is in flight): spend energy buffer at the
     * repair draw, accrue one integrity per second of repair.
     *
     * @param base the station
     * @return true when integrity is still below the maximum (keep polling) / false when full (the command DONEs)
     */
    boolean baseRepairTick(VoidcraftActiveBase base);

    /**
     * A framework log line for a base (its own program log).
     *
     * @param base    the base (for the log identity)
     * @param message the line (never null)
     */
    void logBase(VoidcraftActiveBase base, String message);

    // endregion
}
