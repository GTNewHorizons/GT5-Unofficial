package tectech.voidcraft.uss;

/**
 * One in-flight STABILIZE leg's pacing (the Hyperdimensional Stabilization Matrix's activation command) — the
 * pure half: the energy gate (stall-on-shortfall, travel semantics), the fixed-duration countdown, and the paced
 * Field Generator consumption (one per interval, the UXV tier over the UMV tier; the leg's weight = the tier of
 * the last Field Generator consumed).
 *
 * <p>
 * The game side owns the eligibility gates (base + matrix + revealed anchor ripple + built stabilizer + a Field
 * Generator on board) and maps the base's energy buffer and hold Field Generator counts into the {@link #tick}
 * call — this class only moves the session forward.
 *
 * <p>
 * Bare JVM (primitives + the session record — no NBT, no entity dependencies).
 */
public final class USSStabilize {

    /**
     * One in-flight STABILIZE leg's bookkeeping (persisted with the system's in-flight sessions).
     */
    public static final class Session {

        /** Remaining duration ticks (the fixed window length, counting down). */
        public long ticks;

        /** Countdown to the next Field Generator consumption (armed to the interval, re-armed after each). */
        public long fieldGeneratorTicks;

        /**
         * The tier of the last Field Generator consumed (0 = none yet; UMV = 1, UXV = 2 — see {@link USSConstants}).
         */
        public int weight;
    }

    /** The outcome of one {@link #tick} call. */
    public static final class TickResult {

        /** The window still has duration left (false = the window ran out on this tick). */
        public final boolean running;
        /** The tick consumed one UXV Field Generator. */
        public final boolean consumeUxv;
        /** The tick consumed one UMV Field Generator. */
        public final boolean consumeUmv;

        public TickResult(boolean running, boolean consumeUxv, boolean consumeUmv) {
            this.running = running;
            this.consumeUxv = consumeUxv;
            this.consumeUmv = consumeUmv;
        }
    }

    private USSStabilize() {}

    /**
     * Whether a leg may start: at least one Field Generator on board (either tier) — a leg with none cannot
     * start.
     */
    public static boolean hasFieldGenerators(long umv, long uxv) {
        return umv > 0L || uxv > 0L;
    }

    /**
     * One machine tick of an in-flight leg.
     *
     * <p>
     * The energy gate comes first: a tick the base cannot pay for is a STALL — the duration, the consumption
     * countdown and the consumption all freeze (the leg retries next tick). A paid tick counts the duration
     * down one and arms the consumption countdown; at zero it consumes ONE Field Generator (the UXV tier over
     * the UMV tier) and re-arms the countdown regardless — a countdown that finds none leaves the weight
     * untouched and simply re-arms.
     *
     * @param session    the in-flight session (mutated)
     * @param energyPaid whether the base's buffer covered this tick's draw
     * @param umv        UMV Field Generators on board (before the tick)
     * @param uxv        UXV Field Generators on board (before the tick)
     * @return the tick outcome (never null; null session = not running)
     */
    public static TickResult tick(Session session, boolean energyPaid, long umv, long uxv) {
        if (session == null) {
            return new TickResult(false, false, false);
        }
        if (!energyPaid) {
            return new TickResult(true, false, false); // stalled — everything freezes
        }
        session.ticks--;
        session.fieldGeneratorTicks--;
        boolean consumeUxv = false;
        boolean consumeUmv = false;
        if (session.fieldGeneratorTicks <= 0L) {
            session.fieldGeneratorTicks = USSConstants.STABILIZE_FIELD_GENERATOR_INTERVAL_TICKS;
            if (uxv > 0L) {
                consumeUxv = true;
                session.weight = USSConstants.MATRIX_WEIGHT_UXV;
            } else if (umv > 0L) {
                consumeUmv = true;
                session.weight = USSConstants.MATRIX_WEIGHT_UMV;
            }
        }
        return new TickResult(session.ticks > 0L, consumeUxv, consumeUmv);
    }
}
