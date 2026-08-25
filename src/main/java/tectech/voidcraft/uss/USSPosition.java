package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A point in the Voidcraft solar system (the stateful-position pass): the 3D position of a star, planet, or ship.
 *
 * <p>
 * <strong>Coordinate system.</strong> FLEET-ANCHOR coordinates (see {@link USSFleetOrbit}) — the same space the EoH
 * renderer and the ship-tracked orbit math already use, where the star center sits at {@code (0, STAR_CENTER_Y, 0)}
 * = {@code (0, -2, 0)}. Everything is in <em>blocks</em>, so the Euclidean distance between two positions is the
 * physical separation the ships fly across — "the notion of distance within the solar system."
 *
 * <p>
 * <strong>Bare-JVM safe.</strong> Only three {@code double} components — no Forge objects — so the vector math,
 * the distance model, and the travel-time math (distance / speed) are unit-testable without a world or a live
 * renderer. NBT read/write is provided for persistence on the ship.
 *
 * <p>
 * The class is IMMUTABLE: every operation returns a new instance, so a ship's stored position is never mutated in
 * place (the state machine hands out a fresh position each tick).
 */
public final class USSPosition {

    private static final String TAG_X = "vc_pos_x";
    private static final String TAG_Y = "vc_pos_y";
    private static final String TAG_Z = "vc_pos_z";

    private final double x;
    private final double y;
    private final double z;

    public USSPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @param x the X coordinate (anchor blocks)
     * @param y the Y coordinate (anchor blocks)
     * @param z the Z coordinate (anchor blocks)
     * @return a new position
     */
    public static USSPosition of(double x, double y, double z) {
        return new USSPosition(x, y, z);
    }

    /**
     * @return the origin {@code (0, 0, 0)} (the anchor point, not the star center)
     */
    public static USSPosition zero() {
        return new USSPosition(0.0, 0.0, 0.0);
    }

    /**
     * @return the star's center in anchor coordinates — {@code (0, STAR_CENTER_Y, 0)} = {@code (0, -2, 0)} (the
     *         fixed position of the star; see {@link USSFleetOrbit#STAR_CENTER_Y})
     */
    public static USSPosition starCenter() {
        return new USSPosition(0.0, USSFleetOrbit.STAR_CENTER_Y, 0.0);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    // region vector math

    /**
     * The straight-line (Euclidean) distance to another position — "distance within the solar system" (in blocks).
     *
     * @param other the other position (null → 0, defensive)
     * @return {@code sqrt((x-ox)² + (y-oy)² + (z-oz)²)} (always &ge; 0)
     */
    public double distanceTo(USSPosition other) {
        if (other == null) {
            return 0.0;
        }
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * The distance from the origin {@code (0, 0, 0)} (the anchor point).
     *
     * @return {@code sqrt(x² + y² + z²)} (always &ge; 0)
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Linear interpolation toward another position.
     *
     * @param other the target position
     * @param t     the blend factor: {@code 0.0} → this, {@code 1.0} → other (not clamped — callers may overshoot)
     * @return {@code this + (other - this) · t}
     */
    public USSPosition lerp(USSPosition other, double t) {
        if (other == null) {
            return this;
        }
        return new USSPosition(
            this.x + (other.x - this.x) * t,
            this.y + (other.y - this.y) * t,
            this.z + (other.z - this.z) * t);
    }

    /**
     * Translate by a delta.
     *
     * @param dx the X delta
     * @param dy the Y delta
     * @param dz the Z delta
     * @return {@code (x + dx, y + dy, z + dz)}
     */
    public USSPosition add(double dx, double dy, double dz) {
        return new USSPosition(this.x + dx, this.y + dy, this.z + dz);
    }

    /**
     * Translate by another position's components (treated as a delta).
     *
     * @param delta the delta (null → this)
     * @return {@code this + delta}
     */
    public USSPosition add(USSPosition delta) {
        if (delta == null) {
            return this;
        }
        return new USSPosition(this.x + delta.x, this.y + delta.y, this.z + delta.z);
    }

    /**
     * Subtract another position (component-wise).
     *
     * @param other the position to subtract (null → this)
     * @return {@code (x - ox, y - oy, z - oz)}
     */
    public USSPosition subtract(USSPosition other) {
        if (other == null) {
            return this;
        }
        return new USSPosition(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Scale (multiply) every component.
     *
     * @param s the scale factor
     * @return {@code (x·s, y·s, z·s)}
     */
    public USSPosition scale(double s) {
        return new USSPosition(this.x * s, this.y * s, this.z * s);
    }

    /**
     * The dot product with another position (treated as a vector).
     *
     * @param other the other vector (null → 0, defensive)
     * @return {@code x·ox + y·oy + z·oz}
     */
    public double dot(USSPosition other) {
        if (other == null) {
            return 0.0;
        }
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * The cross product with another position (treated as vectors) — a vector perpendicular to both.
     *
     * @param other the other vector (null → zero, defensive)
     * @return {@code (y·oz - z·oy, z·ox - x·oz, x·oy - y·ox)}
     */
    public USSPosition cross(USSPosition other) {
        if (other == null) {
            return USSPosition.zero();
        }
        return new USSPosition(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x);
    }

    /**
     * A unit-length copy of this position (zero-safe: a zero vector returns itself).
     *
     * @return {@code this / |this|} (or this when |this| = 0)
     */
    public USSPosition normalize() {
        double len = length();
        if (len <= 1.0e-9) {
            return this;
        }
        return scale(1.0 / len);
    }

    // endregion

    // region NBT

    /**
     * Serialize the position.
     *
     * @param nbt the compound to write into (null → no-op)
     */
    public void writeToNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return;
        }
        nbt.setDouble(TAG_X, x);
        nbt.setDouble(TAG_Y, y);
        nbt.setDouble(TAG_Z, z);
    }

    /**
     * Restore a position from NBT.
     *
     * @param nbt the tag written by {@link #writeToNBT(NBTTagCompound)} (null → the origin)
     * @return the restored position (never null)
     */
    public static USSPosition readFromNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return USSPosition.zero();
        }
        return new USSPosition(nbt.getDouble(TAG_X), nbt.getDouble(TAG_Y), nbt.getDouble(TAG_Z));
    }

    // endregion

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof USSPosition)) {
            return false;
        }
        USSPosition that = (USSPosition) other;
        return Double.compare(this.x, that.x) == 0 && Double.compare(this.y, that.y) == 0
            && Double.compare(this.z, that.z) == 0;
    }

    @Override
    public int hashCode() {
        int h = Double.hashCode(x);
        h = 31 * h + Double.hashCode(y);
        h = 31 * h + Double.hashCode(z);
        return h;
    }

    @Override
    public String toString() {
        return "USSPosition[" + x + ", " + y + ", " + z + "]";
    }
}
