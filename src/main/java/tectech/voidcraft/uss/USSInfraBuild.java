package tectech.voidcraft.uss;

import java.util.Collection;
import java.util.Map;

import tectech.voidcraft.ship.VoidcraftComponent;

/**
 * The constructor-built infrastructure (beyond the Dyson Swarm): the Stellar Injector, the Spacetime Continuum
 * Stabilizer and the Stellar Gravitational Lens — structures built AROUND A TARGET (the star or a spacetime
 * ripple point) by the Voidbase standing at that target: the base's infrastructure-builder components feed the
 * infrastructure cargo the constructor delivered, one structure unit per build interval, until the target's shell
 * capacity (its triangle count — see {@link USSInfraShell}) is reached. The Hyperdimensional Stabilization
 * Matrix is the one infrastructure that does NOT build a structure — it exists as a voidbase component and its
 * cell count is the stabilization weight.
 *
 * <p>
 * One structure per target: the star hosts at most one of Dyson Swarm / Injector / Lens (mutually exclusive), a
 * ripple at most one of Stabilizer / Injector. A target's occupancy is the built COUNT (a partially built shell
 * already occupies the target).
 *
 * <p>
 * Bare JVM (strings + longs only — no MC types).
 */
public final class USSInfraBuild {

    /** The Stellar Injector (built around the star — or a ripple). */
    public static final int INJECTOR = 0;
    /** The Spacetime Continuum Stabilizer (built around a ripple). */
    public static final int STABILIZER = 1;
    /** The Stellar Gravitational Lens (built around the star). */
    public static final int LENS = 2;

    /** Target kind: the USS's own star (no index). */
    public static final int TARGET_STAR = 0;
    /** Target kind: a spacetime ripple point by field index (0..342). */
    public static final int TARGET_RIPPLE = 1;

    private static final String[] NAMES = { "injector", "stabilizer", "lens" };

    private USSInfraBuild() {
        throw new AssertionError("Utility holder");
    }

    /** @return true for a valid infrastructure type (INJECTOR / STABILIZER / LENS) */
    public static boolean isValidType(int type) {
        return type >= INJECTOR && type <= LENS;
    }

    /** @return the type's block label ("INJECTOR" / "STABILIZER" / "LENS") */
    public static String name(int type) {
        if (!isValidType(type)) {
            return "INFRA" + type;
        }
        return NAMES[type].toUpperCase();
    }

    /** @return true when the type may be built around the given target kind */
    public static boolean isValidTarget(int type, int targetKind) {
        switch (type) {
            case INJECTOR:
                return targetKind == TARGET_STAR || targetKind == TARGET_RIPPLE;
            case STABILIZER:
                return targetKind == TARGET_RIPPLE;
            case LENS:
                return targetKind == TARGET_STAR;
            default:
                return false;
        }
    }

    /**
     * The infrastructure PROGRESS key of (type, target): {@code <name>:star} or {@code <name>:ripple:<index>} —
     * the {@link USSInfrastructure} key the structure built on that target counts under.
     */
    public static String key(int type, int targetKind, int index) {
        if (type < 0 || type >= NAMES.length) {
            throw new IllegalArgumentException("Unknown infrastructure type: " + type);
        }
        if (targetKind == TARGET_STAR) {
            return NAMES[type] + ":star";
        }
        return NAMES[type] + ":ripple:" + Math.max(0, index);
    }

    /**
     * The key PREFIX of a type's progress keys (both target kinds share it) — the scan handle
     * {@code "stabilizer:"} / {@code "injector:"} / {@code "lens:"}.
     */
    public static String prefix(int type) {
        if (type < 0 || type >= NAMES.length) {
            throw new IllegalArgumentException("Unknown infrastructure type: " + type);
        }
        return NAMES[type] + ":";
    }

    /**
     * The star-scale build capacity of the type: the triangle count of its shell at the star's render size
     * (0 for a non-star-scale type).
     */
    public static long starCapacity(int type, double starRenderSize) {
        switch (type) {
            case INJECTOR:
                return USSInfraShell.triangleCount(
                    (float) starRenderSize + USSConstants.INJECTOR_SHELL_RADIUS_MARGIN,
                    USSConstants.INJECTOR_TRIANGLE_EDGE);
            case LENS:
                return USSInfraShell.triangleCount(
                    (float) starRenderSize + USSConstants.LENS_SHELL_RADIUS_MARGIN,
                    USSConstants.LENS_TRIANGLE_EDGE);
            default:
                return 0L;
        }
    }

    /** The ripple-scale build capacity — a fixed small shell, shared by every ripple target. */
    public static long rippleCapacity() {
        return USSInfraShell.triangleCount(USSConstants.STABILIZER_SHELL_RADIUS, USSConstants.STABILIZER_TRIANGLE_EDGE);
    }

    /** The build capacity of (type, target) at the given star render size. */
    public static long capacity(int type, int targetKind, double starRenderSize) {
        return targetKind == TARGET_STAR ? starCapacity(type, starRenderSize) : rippleCapacity();
    }

    /**
     * Whether the target (kind + index) already carries a structure of ANOTHER type — one structure per target:
     * the star's Dyson Swarm / Injector / Lens are mutually exclusive, a ripple's Stabilizer / Injector as well.
     * The target's OWN type (a partially built shell) does not occupy it against itself.
     */
    public static boolean targetOccupiedByOther(USSInfrastructure infra, int type, int targetKind, int index) {
        if (infra == null || !isValidType(type)) {
            return false;
        }
        final String ownKey = key(type, targetKind, index);
        final String ownTargetPart = targetPart(targetKind, index);
        for (Map.Entry<String, Long> entry : infra.counts()
            .entrySet()) {
            if (entry.getValue() <= 0L) {
                continue;
            }
            String k = entry.getKey();
            if (k.equals(ownKey)) {
                continue;
            }
            if (k.equals(USSInfrastructure.DYSON_STAR_KEY)) {
                if (targetKind == TARGET_STAR) {
                    return true;
                }
                continue;
            }
            int colon = k.indexOf(':');
            if (colon <= 0) {
                continue;
            }
            if (ownTargetPart.equals(k.substring(colon + 1))) {
                return true;
            }
        }
        return false;
    }

    private static String targetPart(int targetKind, int index) {
        return targetKind == TARGET_STAR ? "star" : "ripple:" + Math.max(0, index);
    }

    /**
     * Whether the structure on (type, target) is FULLY BUILT (count reached the target's capacity) — a partial
     * build renders but has no effect until complete.
     */
    public static boolean isBuilt(USSInfrastructure infra, int type, int targetKind, int index, long capacity) {
        return capacity > 0L && infra != null && infra.count(key(type, targetKind, index)) >= capacity;
    }

    /**
     * Whether ANY target carries a FULLY BUILT structure of the given type (any ripple for a ripple-scale scan).
     */
    public static boolean anyBuilt(USSInfrastructure infra, int type) {
        if (infra == null || !isValidType(type)) {
            return false;
        }
        long capacity = rippleCapacity();
        if (capacity <= 0L) {
            return false;
        }
        String prefix = prefix(type);
        for (Map.Entry<String, Long> entry : infra.counts()
            .entrySet()) {
            if (entry.getKey()
                .startsWith(prefix) && entry.getValue() >= capacity) {
                return true;
            }
        }
        return false;
    }

    /**
     * Of the system's ACTIVE scanned ripples, the count that carry a FULLY BUILT structure of the given
     * ripple-scale type — the per-ripple stabilization count the expiry conversion reads. A ripple counts only
     * when it is an actual ripple of the field, has been scanned, AND its shell has reached the ripple capacity.
     *
     * @param field   the system's ripple field (null → 0).
     * @param scanned the scanned point indices (null/empty → 0).
     * @param infra   the system's infrastructure (null → 0).
     * @param type    the ripple-scale infrastructure type (STABILIZER).
     * @return the count (&ge; 0).
     */
    public static int builtOnActiveRipples(USSRippleField field, Collection<Integer> scanned, USSInfrastructure infra,
        int type) {
        if (field == null || scanned == null || scanned.isEmpty() || infra == null || !isValidType(type)) {
            return 0;
        }
        long capacity = rippleCapacity();
        if (capacity <= 0L) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < field.size(); i++) {
            if (scanned.contains(i) && field.isRipple(i) && infra.count(key(type, TARGET_RIPPLE, i)) >= capacity) {
                count++;
            }
        }
        return count;
    }

    /**
     * The CONTINUUM STABILIZER's next build target: the first ripple (in ascending index order) whose shell is not
     * FULLY BUILT — a saturated shell moves the target to the next ripple.
     *
     * @param infra         the system's infrastructure
     * @param rippleIndices the ripple point indices (ascending)
     * @return that ripple's index, -1 when every ripple's shell is full (or there are no ripples)
     */
    public static int firstIncompleteStabilizerRipple(USSInfrastructure infra, java.util.List<Integer> rippleIndices) {
        if (infra == null || rippleIndices == null) {
            return -1;
        }
        long capacity = rippleCapacity();
        if (capacity <= 0L) {
            return -1;
        }
        for (int index : rippleIndices) {
            if (infra.count(key(STABILIZER, TARGET_RIPPLE, index)) < capacity) {
                return index;
            }
        }
        return -1;
    }

    /** @return the voidbase component that BUILDS the given infrastructure type (the builder cell) */
    public static VoidcraftComponent builderComponent(int type) {
        switch (type) {
            case INJECTOR:
                return VoidcraftComponent.STELLAR_INJECTOR;
            case STABILIZER:
                return VoidcraftComponent.CONTINUUM_STABILIZER;
            case LENS:
                return VoidcraftComponent.STELLAR_LENS;
            default:
                return null;
        }
    }
}
