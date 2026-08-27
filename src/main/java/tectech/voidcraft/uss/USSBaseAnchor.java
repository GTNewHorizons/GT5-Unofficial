package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A Voidbase anchor: the in-USS body the station is built around. Any target a Voidcraft can reach - the star,
 * a system planet by index, or a spacetime ripple point by index (the same descriptors the pilot MOVE
 * resolution uses). The base sits at that body ship hover point and follows it (a planet anchor orbits with
 * the planet).
 *
 * <p>
 * Bare-JVM safe (NBT + primitives) for unit tests.
 */
public final class USSBaseAnchor {

    public static final int KIND_STAR = 0;
    public static final int KIND_PLANET = 1;
    public static final int KIND_RIPPLE = 2;

    private static final String TAG_KIND = "vc_base_anchor_kind";
    private static final String TAG_INDEX = "vc_base_anchor_index";

    private final int kind;
    private final int index;

    private USSBaseAnchor(int kind, int index) {
        this.kind = kind;
        this.index = index;
    }

    /** Anchor at the star (no index). */
    public static USSBaseAnchor star() {
        return new USSBaseAnchor(KIND_STAR, -1);
    }

    /** Anchor at the given planet (by system index). */
    public static USSBaseAnchor planet(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Planet index must be >= 0: " + index);
        }
        return new USSBaseAnchor(KIND_PLANET, index);
    }

    /** Anchor at the given spacetime ripple point (by index). */
    public static USSBaseAnchor ripple(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Ripple index must be >= 0: " + index);
        }
        return new USSBaseAnchor(KIND_RIPPLE, index);
    }

    /**
     * Convert a resolved MOVE target (the pilot hover: the target kind string + the resolved body index) into a
     * Voidbase anchor - a CONSTRUCT always builds at the executor hover body.
     *
     * @param kind  the MOVE target string (a {@link USSProgramDefaults} {@code TARGET_*} value)
     * @param index the RESOLVED body index (planet i / ripple j; -1 for the star)
     * @return the hover body anchor, or null when the target carries no build anchor ({@code SHIP} rendezvous,
     *         {@code HOME}, or a hover that never resolved - a CONSTRUCT in that state SKIPs)
     */
    public static USSBaseAnchor fromMoveTarget(String kind, int index) {
        if (kind == null || kind.isEmpty()) {
            return null;
        }
        if (USSProgramDefaults.TARGET_STAR.equals(kind)) {
            return star();
        }
        if (index < 0) {
            return null;
        }
        if (USSProgramDefaults.TARGET_PLANET.equals(kind) || USSProgramDefaults.TARGET_NEAREST_PLANET.equals(kind)
            || USSProgramDefaults.TARGET_RANDOM_PLANET.equals(kind)) {
            return planet(index);
        }
        if (USSProgramDefaults.TARGET_RIPPLE.equals(kind) || USSProgramDefaults.TARGET_RIPPLE_UNSCANNED.equals(kind)) {
            return ripple(index);
        }
        return null; // SHIP / HOME / anything else has no build anchor
    }

    public int kind() {
        return kind;
    }

    /** The body index (PLANET / RIPPLE); -1 for STAR. */
    public int index() {
        return index;
    }

    public boolean isStar() {
        return kind == KIND_STAR;
    }

    public boolean isPlanet() {
        return kind == KIND_PLANET;
    }

    public boolean isRipple() {
        return kind == KIND_RIPPLE;
    }

    /**
     * Write the anchor into a compound tag.
     */
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger(TAG_KIND, kind);
        nbt.setInteger(TAG_INDEX, index);
    }

    /**
     * Read an anchor from a compound tag.
     *
     * @param nbt source tag (null / missing keys default to the star)
     * @return the anchor
     */
    public static USSBaseAnchor readFromNBT(NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey(TAG_KIND)) {
            return star();
        }
        int kind = nbt.getInteger(TAG_KIND);
        int index = nbt.hasKey(TAG_INDEX) ? nbt.getInteger(TAG_INDEX) : -1;
        switch (kind) {
            case KIND_PLANET:
                return planet(Math.max(0, index));
            case KIND_RIPPLE:
                return ripple(Math.max(0, index));
            default:
                return star();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof USSBaseAnchor)) return false;
        USSBaseAnchor that = (USSBaseAnchor) o;
        return kind == that.kind && index == that.index;
    }

    @Override
    public int hashCode() {
        int result = kind;
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        switch (kind) {
            case KIND_PLANET:
                return "PLANET " + index;
            case KIND_RIPPLE:
                return "RIPPLE " + index;
            default:
                return "STAR";
        }
    }
}
