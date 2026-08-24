package tectech.voidcraft.ship;

/**
 * Immutable stat block of a digitized Voidcraft.
 *
 * <p>
 * Produced by {@link VoidcraftBlueprint} from the component and cover grid.
 *
 * <p>
 * Thrust is directional: each engine (block or cover) fires out of its mounting face, pushing the ship the other
 * way. {@link #thrustX}/{@link #thrustY}/{@link #thrustZ} are the signed net-thrust vector in ship-local space
 * (positive X = east, positive Y = up, positive Z = south, i.e. Minecraft axes). {@link #thrust} is the best thrust
 * along any single axis, {@code max(|thrustX|, |thrustY|, |thrustZ|)} — opposing thrusters cancel out in the vector
 * sum.
 *
 * <p>
 * {@code speed} is derived, not additive: {@code clamp(thrust / mass, 0, 1)} — engines raise thrust, everything
 * else adds mass, so heavy ships are slower by construction.
 */
public final class VoidcraftStats {

    public final long mass;
    /** Best thrust along any single axis = max(|thrustX|, |thrustY|, |thrustZ|). */
    public final long thrust;
    /** Signed net-thrust vector, ship-local (positive X = east, Y = up, Z = south). */
    public final long thrustX;
    public final long thrustY;
    public final long thrustZ;
    /** Derived: clamp(thrust / mass, 0, 1). */
    public final double speed;
    public final long cargoSlots;
    public final long miningPower;
    public final long scanPower;
    public final long constructionPower;
    public final long starlifterPower;
    public final long energyBuffer;
    public final long energyDraw;
    public final long integrity;

    /** Convenience constructor for directionless stats (net thrust vector all zero). */
    public VoidcraftStats(long mass, long thrust, long cargoSlots, long miningPower, long scanPower,
        long constructionPower, long starlifterPower, long energyBuffer, long energyDraw, long integrity) {
        this(
            mass,
            thrust,
            0,
            0,
            0,
            cargoSlots,
            miningPower,
            scanPower,
            constructionPower,
            starlifterPower,
            energyBuffer,
            energyDraw,
            integrity);
    }

    public VoidcraftStats(long mass, long thrust, long thrustX, long thrustY, long thrustZ, long cargoSlots,
        long miningPower, long scanPower, long constructionPower, long starlifterPower, long energyBuffer,
        long energyDraw, long integrity) {
        this.mass = mass;
        this.thrust = thrust;
        this.thrustX = thrustX;
        this.thrustY = thrustY;
        this.thrustZ = thrustZ;
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
     * Speed derivation shared by all stat math: thrust divided by mass, clamped to [0, 1].
     *
     * @param thrust total thrust
     * @param mass   total mass
     * @return speed in [0, 1]
     */
    public static double speedFor(long thrust, long mass) {
        if (mass <= 0) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, (double) thrust / (double) mass));
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
            + ", thrustXYZ=("
            + thrustX
            + ","
            + thrustY
            + ","
            + thrustZ
            + "], speed="
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
