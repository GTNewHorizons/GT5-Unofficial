package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * What a Voidcraft is flying toward (the stateful-position pass): a star, a planet, or ANOTHER ship.
 *
 * <p>
 * "Voidcraft should be able to fly to any of the three [star, planets, voidcraft]. Voidcraft hold a target, which
 * determines how they are moving." The target is the ship's destination:
 * <ul>
 * <li>{@link Kind#STAR} — the star center (Starlifters' natural work point).</li>
 * <li>{@link Kind#PLANET} — a system planet (index into the system's planet list); a Miner hovers at a random
 * spherical-shell point around it (see {@link USSFleetOrbit#shellPoint}).</li>
 * <li>{@link Kind#SHIP} — another ship in the fleet (identified by its per-launch seed); the targeting ship
 * nudges itself to hover near it without overlapping (see {@link USSFleetOrbit#nudge}).</li>
 * </ul>
 *
 * <p>
 * Bare-JVM safe (an enum + two ints) and NBT-serializable so it persists on the ship. The KIND is the routing
 * discriminator; the two payload ints are only meaningful for their matching kind (the other is ignored, defaulted
 * to 0) so the NBT shape stays flat.
 */
public final class USSTarget {

    /** The kind of body a ship can target. */
    public enum Kind {
        /** The star center. */
        STAR,
        /** A system planet (the index is {@link USSTarget#payloadA}). */
        PLANET,
        /** Another ship (the per-launch seed is {@link USSTarget#payloadA}). */
        SHIP
    }

    private static final String TAG_KIND = "vc_tgt_kind";
    private static final String TAG_A = "vc_tgt_a";

    private final Kind kind;
    private final int payloadA;
    private final int payloadB;

    private USSTarget(Kind kind, int payloadA, int payloadB) {
        this.kind = kind;
        this.payloadA = payloadA;
        this.payloadB = payloadB;
    }

    /**
     * @return a target at the star center
     */
    public static USSTarget star() {
        return new USSTarget(Kind.STAR, 0, 0);
    }

    /**
     * @param planetIndex the index into the system's planet list (the ship's planet target)
     * @return a target at the given planet
     */
    public static USSTarget planet(int planetIndex) {
        return new USSTarget(Kind.PLANET, planetIndex, 0);
    }

    /**
     * @param shipSeed the per-launch seed of the ship being targeted
     * @return a target at the given ship
     */
    public static USSTarget ship(int shipSeed) {
        return new USSTarget(Kind.SHIP, shipSeed, 0);
    }

    /**
     * @return the target kind (never null)
     */
    public Kind kind() {
        return kind;
    }

    /**
     * @return the planet index (for {@link Kind#PLANET}); 0 otherwise
     */
    public int planetIndex() {
        return (kind == Kind.PLANET) ? payloadA : 0;
    }

    /**
     * @return the targeted ship's per-launch seed (for {@link Kind#SHIP}); 0 otherwise
     */
    public int shipSeed() {
        return (kind == Kind.SHIP) ? payloadA : 0;
    }

    public int payloadA() {
        return payloadA;
    }

    public int payloadB() {
        return payloadB;
    }

    // region NBT

    /**
     * Serialize the target.
     *
     * @param nbt the compound to write into (null → no-op)
     */
    public void writeToNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return;
        }
        nbt.setString(TAG_KIND, kind.name());
        nbt.setInteger(TAG_A, payloadA);
        if (payloadB != 0) {
            nbt.setInteger("vc_tgt_b", payloadB);
        }
    }

    /**
     * Restore a target from NBT.
     *
     * @param nbt the tag written by {@link #writeToNBT(NBTTagCompound)} (null / unknown kind → the star target)
     * @return the restored target (never null)
     */
    public static USSTarget readFromNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return USSTarget.star();
        }
        Kind kind;
        try {
            kind = Kind.valueOf(nbt.getString(TAG_KIND));
        } catch (RuntimeException e) {
            return USSTarget.star();
        }
        int a = nbt.getInteger(TAG_A);
        int b = nbt.getInteger("vc_tgt_b");
        switch (kind) {
            case PLANET:
                return USSTarget.planet(a);
            case SHIP:
                return USSTarget.ship(a);
            case STAR:
            default:
                return USSTarget.star();
        }
    }

    // endregion

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof USSTarget)) {
            return false;
        }
        USSTarget that = (USSTarget) other;
        return this.kind == that.kind && this.payloadA == that.payloadA && this.payloadB == that.payloadB;
    }

    @Override
    public int hashCode() {
        int h = kind.hashCode();
        h = 31 * h + payloadA;
        h = 31 * h + payloadB;
        return h;
    }

    @Override
    public String toString() {
        switch (kind) {
            case PLANET:
                return "USSTarget[PLANET " + payloadA + "]";
            case SHIP:
                return "USSTarget[SHIP " + payloadA + "]";
            case STAR:
            default:
                return "USSTarget[STAR]";
        }
    }
}
