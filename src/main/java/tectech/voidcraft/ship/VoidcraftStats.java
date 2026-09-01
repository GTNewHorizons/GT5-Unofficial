package tectech.voidcraft.ship;

/**
 * Immutable stat block of a digitized Voidcraft.
 *
 * <p>
 * Produced by {@link VoidcraftBlueprint} from the component and cover grid.
 *
 * <p>
 * <b>Thrust model (pass 18, pass 23 covers-only, pass 24 flip).</b> Thrust is a <em>single value</em>: the sum of
 * the thrust of every ENGINE cover (all four engine families) that faces the ship's BACK (exhaust out the rear, −Z =
 * the assembler side; the nose is the far end, grid +Z). Engines aimed anywhere else contribute nothing; there is no
 * net vector and no cancellation.
 *
 * <p>
 * {@code speed} is derived, not additive: {@code max(0, thrust / mass)} — the thrust-to-mass ratio, unclamped.
 * Travel time to a destination is an actual measure of DISTANCE divided by this ratio:
 * {@code USSConstants.travelTicks(distance, speed)} = {@code distance · 20 / speed} (clamped to [20, 600] ticks),
 * so a high thrust/mass ratio (or a closer destination) means a short leg.
 *
 * <p>
 * <b>Integrity model (frame rework).</b> Integrity is no longer a flat per-block sum: each frame block contributes
 * its tier's base integrity plus a per-FACE term — a frame side facing another frame side adds
 * {@code FRAME_FACE_INTEGRITY_BONUS}, a side exposed to air removes {@code FRAME_FACE_INTEGRITY_PENALTY}, a side
 * facing the controller or a multiblock block contributes nothing. The ship may not be digitized with total
 * integrity &lt;= 0 ({@code voidcraft_integrity_too_low}).
 *
 * <p>
 * <b>Fuel model.</b> {@code engineType} is the single engine family the ship may carry (validation enforces it);
 * {@code thrusterCount} is the number of engine covers of that family (drives the per-thruster fuel and energy
 * draws of travel legs); {@code fuelCapacity} (mB) is the total Fuel Storage capacity.
 */
public final class VoidcraftStats {

    public final long mass;
    /** Single thrust value: sum of thrust of all back-facing engine covers (all four families). */
    public final long thrust;
    /** Derived: max(0, thrust / mass) — the thrust-to-mass ratio, unclamped. */
    public final double speed;
    public final long cargoSlots;
    public final long miningPower;
    public final long scanPower;
    public final long constructionPower;
    public final long starlifterPower;
    /** Logistics power (the Cargo Drone Bay covers): 1 power = 1 cargo unit transferred per second (SEND / TAKE). */
    public final long logisticsPower;
    public final long energyBuffer;
    public final long energyDraw;
    /** Energy generated per game tick (solar panels, reactors; 0 for a plain ship). */
    public final long energyGen;
    public final long integrity;
    /** Fuel tank capacity in mB (sum of the Fuel Storage covers). */
    public final long fuelCapacity;
    /** The ship's single engine family ({@code VoidcraftEngineType.id}; {@code NONE} id when there are no engines). */
    public final int engineType;
    /** Number of engine covers of the ship's engine family (drives the per-thruster travel draws). */
    public final long thrusterCount;
    /** The frame tier of the ship's frames (all frames one tier — validation); 0 when the ship has no frame. */
    public final int frameTier;

    public VoidcraftStats(long mass, long thrust, long cargoSlots, long miningPower, long scanPower,
        long constructionPower, long starlifterPower, long logisticsPower, long energyBuffer, long energyDraw,
        long energyGen, long integrity, long fuelCapacity, int engineType, long thrusterCount, int frameTier) {
        this.mass = mass;
        this.thrust = thrust;
        this.speed = speedFor(thrust, mass);
        this.cargoSlots = cargoSlots;
        this.miningPower = miningPower;
        this.scanPower = scanPower;
        this.constructionPower = constructionPower;
        this.starlifterPower = starlifterPower;
        this.logisticsPower = logisticsPower;
        this.energyBuffer = energyBuffer;
        this.energyDraw = energyDraw;
        this.energyGen = energyGen;
        this.integrity = integrity;
        this.fuelCapacity = fuelCapacity;
        this.engineType = engineType;
        this.thrusterCount = thrusterCount;
        this.frameTier = frameTier;
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
            + ", logistics="
            + logisticsPower
            + ", buffer="
            + energyBuffer
            + ", draw="
            + energyDraw
            + ", gen="
            + energyGen
            + ", integrity="
            + integrity
            + ", fuel="
            + fuelCapacity
            + ", engine="
            + engineType
            + ", thrusters="
            + thrusterCount
            + ", frameTier="
            + frameTier
            + "]";
    }
}
