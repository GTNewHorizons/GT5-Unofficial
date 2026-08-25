package tectech.voidcraft.ship;

/**
 * Immutable stat block of a digitized Voidcraft.
 *
 * <p>
 * Produced by {@link VoidcraftBlueprint} from the component and cover grid.
 *
 * <p>
 * <b>Thrust model (pass 18, pass 23 covers-only, pass 24 flip).</b> Thrust is a <em>single value</em>: the sum of
 * the thrust of every {@code THRUSTER_NOZZLE} cover that faces the ship's BACK (exhaust out the rear, −Z = the
 * assembler side; the nose is the far end, grid +Z). Nozzles aimed anywhere else contribute nothing; there is no
 * net vector and no cancellation.
 *
 * <p>
 * {@code speed} is derived, not additive: {@code max(0, thrust / mass)} — the thrust-to-mass ratio, unclamped.
 * Travel time to a destination is an actual measure of DISTANCE divided by this ratio:
 * {@code USSConstants.travelTicks(distance, speed)} = {@code distance · 20 / speed} (clamped to [20, 600] ticks),
 * so a high thrust/mass ratio (or a closer destination) means a short leg.
 */
public final class VoidcraftStats {

    public final long mass;
    /** Single thrust value: sum of thrust of all back-facing thruster nozzles (pass 23: covers only). */
    public final long thrust;
    /** Derived: max(0, thrust / mass) — the thrust-to-mass ratio, unclamped. */
    public final double speed;
    public final long cargoSlots;
    public final long miningPower;
    public final long scanPower;
    public final long constructionPower;
    public final long starlifterPower;
    public final long energyBuffer;
    public final long energyDraw;
    public final long integrity;

    public VoidcraftStats(long mass, long thrust, long cargoSlots, long miningPower, long scanPower,
        long constructionPower, long starlifterPower, long energyBuffer, long energyDraw, long integrity) {
        this.mass = mass;
        this.thrust = thrust;
        this.speed = speedFor(thrust, mass);
        this.cargoSlots = cargoSlots;
        this.miningPower = miningPower;
        this.scanPower = scanPower;
        this.constructionPower = constructionPower;
        this.starlifterPower = starlifterPower;
        this.energyBuffer = energyBuffer;
        this.energyDraw = energyDraw;
        this.integrity = integrity;
    }

    /**
     * Speed derivation shared by all stat math: thrust divided by mass (the thrust-to-mass ratio), clamped at 0
     * but NOT at 1 — a strong engine load legitimately out-masses the old scale, and
     * {@link tectech.voidcraft.uss.USSConstants#travelTicks(double, double)} (the [20, 600] tick window) is what
     * bounds the resulting travel time.
     *
     * @param thrust total thrust (sum of back-facing engines)
     * @param mass   total mass
     * @return speed = max(0, thrust / mass)
     */
    public static double speedFor(long thrust, long mass) {
        if (mass <= 0) {
            return 0.0;
        }
        return Math.max(0.0, (double) thrust / (double) mass);
    }

    /**
     * @return true if this ship can be recovered (docked and pulled out) when its USS collapses
     */
    public boolean isRecoverable() {
        return integrity >= VoidcraftConstants.RECOVERABLE_INTEGRITY_THRESHOLD;
    }

    @Override
    public String toString() {
        return "VoidcraftStats[mass=" + mass
            + ", thrust="
            + thrust
            + ", speed="
            + speed
            + ", cargo="
            + cargoSlots
            + ", mining="
            + miningPower
            + ", scan="
            + scanPower
            + ", construction="
            + constructionPower
            + ", starlifter="
            + starlifterPower
            + ", buffer="
            + energyBuffer
            + ", draw="
            + energyDraw
            + ", integrity="
            + integrity
            + "]";
    }
}
