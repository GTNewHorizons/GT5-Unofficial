package tectech.voidcraft.uss;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

/**
 * The stellar-evolution mechanic's pure math (bare-JVM safe, unit-tested without a world):
 *
 * <ul>
 * <li>the EXPIRY OUTCOME — what a star becomes when its lifespan runs out ({@link #resolve}): the cooling dwarf
 * chain / main sequence evolve to their catalog target on a chance that grows as the star's primary reserve is
 * depleted (0% at full, 100% at empty); the massive blue giants explode (to their catalog target) only at the
 * injector's max size, on a chance that is 100% above 75% primary and fades to 0% at empty; the red supergiant
 * goes to supernova deterministically; a supernova leaves a neutron star only if the star was FED (its size
 * boosted above the original by the Stellar Injector), else a black hole; a hypernova leaves a neutron star; a
 * neutron star becomes a gravastar under a Stellar Gravitational Lens (priority), a magnetar when fully depleted
 * at max size, a quark star when fully depleted, and dies otherwise. Terminal classes (black dwarf, black hole,
 * quasi-star, magnetar, gravastar, quark star) never evolve.
 * <li>the EXPIRY OUTPUTS — the Spacetime yield and the Universium conversion (PER RIPPLE: each active scanned
 * ripple converts at its own rate and Spacetime:Universium ratio — 10 000 000 / 100000:1 base, 200 000 / 1000:1
 * with a finished Stabilizer on that ripple — plus the Fibonacci matrix multiplier): {@link #universiumLiters},
 * {@link #spacetimeConsumedMB}, {@link #universiumOutputMB}, {@link #matrixMultiplier}.
 * <li>the STELLAR ACCELERATION — the lifespan reduction per consumed tachyon fluid (sqrt, minimum 1) and the
 * proportional orbit-clock advance, smoothed at the start and end of the acceleration (the first second ramps the
 * orbit clock up, the last ramps it down): {@link #lifespanReductionPerSecond}, {@link #orbitAdvancePerTick},
 * {@link #orbitAdvanceSmoothed}.
 * <li>the STELLAR INJECTOR — the 1.5x size cap and the size-scaled cargo cost: {@link #sizeCap},
 * {@link #cargoUnitsForSizeDelta}.
 * <li>the RIPPLE helpers — counting the scanned-actual ripples (the conversion's R) and the nearest-ripple
 * binding for the Continuum Stabilizer: {@link #activeScannedRipples}, {@link #nearestRippleIndex}.
 * </ul>
 *
 * <p>
 * The chanced rows read their NOMINAL target from the star catalog (the registry's
 * {@link USSStarRegistry#evolutionTargetOf} — the "dwarf chain / main sequence" and the explosion targets), so
 * the catalog's {@code evolutionTarget} field stays the single source of the nominal chain. Chance outcomes take
 * an explicit {@link Random} (seed it for deterministic tests).
 */
public final class USSStellarEvolution {

    /** Epsilon for the double size-factor comparisons (a star boosted by the injector above its original size). */
    private static final double SIZE_EPSILON = 1e-9;

    /**
     * Primary-fraction boundary above which the explosion chance is 100% (the D7 ramp: 100% above 75% full,
     * linear down to 0% at empty; at exactly 0.75 the linear part already reads 1.0).
     */
    private static final double EXPLOSION_CHANCE_FULL = 0.75;

    /**
     * The orbit-clock phase of one acceleration window (one machine second): which part of the acceleration's
     * start/end smoothing is active.
     */
    public enum AccelerationPhase {
        /** No tachyon drained this window — the orbit clock runs at the normal rate. */
        IDLE,
        /** The first acceleration window — the orbit clock ramps up from the normal rate to the full rate. */
        RAMP_UP,
        /** A middle acceleration window — the orbit clock holds the full rate. */
        FULL,
        /**
         * The last acceleration window (the star's lifespan runs out within it) — the orbit clock ramps down
         * from the full rate to the normal rate.
         */
        RAMP_DOWN
    }

    private USSStellarEvolution() {
        throw new AssertionError("Math holder");
    }

    // region expiry outcome

    /**
     * Resolve what a star becomes when its lifespan expires.
     *
     * @param starType        the expiring star class (null → no outcome).
     * @param primaryFraction the remaining fraction of the star's primary (fluid reserve) material, 0..1
     *                        (an unsiphoned reserve = 1.0; clamped into range).
     * @param sizeFactor      the star's current size as a multiple of its ORIGINAL sampled size (&ge; 1.0 — the
     *                        Stellar Injector only grows the star, up to 1.5x; values below 1.0 clamp to 1.0).
     * @param lensPresent     a formed Stellar Gravitational Lens infrastructure stands around the star.
     * @param rng             the RNG for the chanced rows (seeded for deterministic tests).
     * @return the outcome star class; empty = the star dies (the system terminates with no evolution).
     */
    public static Optional<USSStarType> resolve(USSStarType starType, double primaryFraction, double sizeFactor,
        boolean lensPresent, Random rng) {
        if (starType == null || rng == null) {
            return Optional.empty();
        }
        double f = Math.max(0.0, Math.min(1.0, primaryFraction));
        double sf = Math.max(1.0, sizeFactor);
        boolean atMaxSize = sf >= USSConstants.INJECTOR_MAX_SIZE_FACTOR - SIZE_EPSILON;
        boolean fed = sf > 1.0 + SIZE_EPSILON;
        boolean depleted = f <= 0.0;

        switch (starType) {
            case RED_DWARF:
            case YELLOW_DWARF:
            case RED_GIANT:
            case WHITE_DWARF:
                // Cooling (the dwarf chain / main sequence): the nominal catalog target, on a depletion chance.
                if (rng.nextDouble() < 1.0 - f) {
                    return nominalTarget(starType);
                }
                return Optional.empty();
            case BLUE_GIANT:
            case BLUE_SUPERGIANT:
                // Explosion: at the injector's max size only, on the primary-fraction ramp (explosionChance).
                if (atMaxSize && rng.nextDouble() < explosionChance(f)) {
                    return nominalTarget(starType);
                }
                return Optional.empty();
            case RED_SUPERGIANT:
                // Deterministic: the red supergiant goes to supernova.
                return nominalTarget(starType);
            case SUPERNOVA:
                // A fed supernova leaves a neutron star; left alone it collapses to a black hole.
                return Optional.of(fed ? USSStarType.NEUTRON_STAR : USSStarType.BLACK_HOLE);
            case HYPERNOVA:
                return Optional.of(USSStarType.NEUTRON_STAR);
            case NEUTRON_STAR:
                if (lensPresent) {
                    // The Stellar Gravitational Lens overrides the depletion rules (priority).
                    return Optional.of(USSStarType.GRAVASTAR);
                }
                if (depleted && atMaxSize) {
                    return Optional.of(USSStarType.MAGNETAR);
                }
                if (depleted) {
                    return Optional.of(USSStarType.QUARK_STAR);
                }
                return Optional.empty();
            default:
                // Terminal classes (black dwarf, black hole, quasi-star, magnetar, gravastar, quark star).
                return Optional.empty();
        }
    }

    /**
     * The explosion chance for a given primary fraction: 100% above {@link #EXPLOSION_CHANCE_FULL}, linear down
     * to 0% at empty (continuous at the boundary — 0.75/0.75 = 1.0).
     *
     * @param primaryFraction the remaining primary fraction (0..1; clamped into range).
     * @return the chance in [0..1].
     */
    static double explosionChance(double primaryFraction) {
        double f = Math.max(0.0, Math.min(1.0, primaryFraction));
        return f > EXPLOSION_CHANCE_FULL ? 1.0 : f / EXPLOSION_CHANCE_FULL;
    }

    /**
     * The star's nominal (catalog) evolution target as a star type.
     *
     * @param starType the star (null → empty).
     * @return the registered target type; empty when the registry holds no definition or target for it (a bare
     *         JVM with the catalog unregistered).
     */
    static Optional<USSStarType> nominalTarget(USSStarType starType) {
        USSStarDefinition def = USSStarRegistry.byType(starType);
        if (def == null) {
            return Optional.empty();
        }
        USSStarDefinition target = USSStarRegistry.evolutionTargetOf(def);
        if (target == null || target.getId() == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(
                USSStarType.valueOf(
                    target.getId()
                        .toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    // endregion

    // region expiry outputs (spacetime yield + universium conversion)

    /**
     * The Universium litres an expiring system produces, computed PER RIPPLE: each of the R active scanned ripples
     * contributes on its own — a ripple carrying a finished Continuum Stabilizer contributes {@code yield / 200
     * 000} litres, every other ripple {@code yield / 10 000 000} — so the total is {@code stabilized x
     * (yield/200000) + (R - stabilized) x (yield/10000000)}. Capped by {@link #spacetimeConsumedMB}: when the
     * ripples' combined consumption would exceed the yield, the litres are scaled down proportionally so the
     * conversion never consumes more Spacetime than the star yields.
     *
     * @param yieldMB              the star's Spacetime yield in mB ({@link USSConstants#spacetimeYieldForType}).
     * @param stabilizedRipples    the active scanned ripples carrying a finished Stabilizer (the 50x rate).
     * @param activeScannedRipples R — the count of ripple points that are both scanned and actual ripples.
     * @return the Universium litres (&ge; 0).
     */
    public static long universiumLiters(long yieldMB, long stabilizedRipples, long activeScannedRipples) {
        if (yieldMB <= 0L || activeScannedRipples <= 0L) {
            return 0L;
        }
        long stabilized = clampStabilized(stabilizedRipples, activeScannedRipples);
        long plain = activeScannedRipples - stabilized;
        long litres = plain * (yieldMB / USSConstants.UNIVERSIUM_RATE_MB_PER_LITRE)
            + stabilized * (yieldMB / USSConstants.UNIVERSIUM_RATE_MB_PER_LITRE_STABILIZED);
        long rawConsumed = rawConsumedMB(yieldMB, stabilizedRipples, activeScannedRipples);
        if (rawConsumed > yieldMB) {
            litres = satMul(litres, yieldMB) / rawConsumed;
        }
        return litres;
    }

    /**
     * The Spacetime consumed by the Universium conversion, computed PER RIPPLE: each active scanned ripple
     * consumes its own litres at its own ratio — a ripple carrying a finished Stabilizer at 1000:1
     * ({@code (yield/200000) x 1000}), every other ripple at 100000:1 ({@code (yield/10000000) x 100000}) — capped
     * at the yield: the conversion never consumes more Spacetime than the star yields.
     *
     * @param yieldMB              the star's Spacetime yield in mB.
     * @param stabilizedRipples    the active scanned ripples carrying a finished Stabilizer (the 1000:1 ratio).
     * @param activeScannedRipples R — the count of ripple points that are both scanned and actual ripples.
     * @return the consumed Spacetime in mB (&ge; 0, &le; yieldMB).
     */
    public static long spacetimeConsumedMB(long yieldMB, long stabilizedRipples, long activeScannedRipples) {
        if (yieldMB <= 0L || activeScannedRipples <= 0L) {
            return 0L;
        }
        long raw = rawConsumedMB(yieldMB, stabilizedRipples, activeScannedRipples);
        return raw >= yieldMB ? yieldMB : raw;
    }

    private static long rawConsumedMB(long yieldMB, long stabilizedRipples, long activeScannedRipples) {
        long stabilized = clampStabilized(stabilizedRipples, activeScannedRipples);
        long plain = activeScannedRipples - stabilized;
        long base = (yieldMB / USSConstants.UNIVERSIUM_RATE_MB_PER_LITRE)
            * USSConstants.UNIVERSIUM_SPACETIME_MB_PER_LITRE;
        long stab = (yieldMB / USSConstants.UNIVERSIUM_RATE_MB_PER_LITRE_STABILIZED)
            * USSConstants.UNIVERSIUM_SPACETIME_MB_PER_LITRE_STABILIZED;
        return satAdd(satMul(plain, base), satMul(stabilized, stab));
    }

    private static long clampStabilized(long stabilized, long total) {
        return Math.max(0L, Math.min(stabilized, total));
    }

    private static long satMul(long count, long perRipple) {
        if (count <= 0L || perRipple <= 0L) {
            return 0L;
        }
        if (perRipple > Long.MAX_VALUE / count) {
            return Long.MAX_VALUE;
        }
        return count * perRipple;
    }

    private static long satAdd(long a, long b) {
        if (a < 0L || b < 0L || a > Long.MAX_VALUE - b) {
            return Long.MAX_VALUE;
        }
        return a + b;
    }

    /**
     * The Molten Universium output of an expiry (mB): litres x matrix multiplier, at 1000 mB per litre.
     *
     * @param universiumLitres the litres converted (&lt;= 0 → 0).
     * @param multiplier       the Fibonacci matrix multiplier ({@link #matrixMultiplier}; &lt; 1 → 1).
     * @return the Universium output in mB (saturates at {@link Long#MAX_VALUE} when the product would overflow).
     */
    public static long universiumOutputMB(long universiumLitres, long multiplier) {
        long litres = Math.max(0L, universiumLitres);
        if (litres == 0L) {
            return 0L;
        }
        long mult = Math.max(1L, multiplier);
        if (litres > Long.MAX_VALUE / mult) {
            return Long.MAX_VALUE;
        }
        long converted = litres * mult;
        if (converted > Long.MAX_VALUE / USSConstants.UNIVERSIUM_MB_PER_LITRE) {
            return Long.MAX_VALUE;
        }
        return converted * USSConstants.UNIVERSIUM_MB_PER_LITRE;
    }

    /**
     * The Fibonacci matrix multiplier for a total effective weight N of active stabilization matrices:
     * {@code fib(N + 2)} with {@code fib(1) = fib(2) = 1} — N = 0: x1, N = 1: x2 (F3), N = 2: x3, N = 3: x5,
     * N = 4: x8, ... (the spec counts from F3 = 2).
     *
     * @param weightSum N — the sum of the active matrices' weights (UMV = 1, UXV = 2; &lt;= 0 → x1).
     * @return the multiplier (&ge; 1; saturates at {@link Long#MAX_VALUE} once fib would overflow).
     */
    public static long matrixMultiplier(int weightSum) {
        if (weightSum <= 0) {
            return 1L;
        }
        int n = weightSum + 2;
        if (n > 92) { // fib(93) overflows a long
            return Long.MAX_VALUE;
        }
        return fibonacci(n);
    }

    /**
     * The n-th Fibonacci number with {@code fib(1) = fib(2) = 1} ({@code n <= 0} → 0).
     *
     * @param n the index (&ge; 1 for the meaningful values).
     * @return the Fibonacci number.
     */
    public static long fibonacci(int n) {
        if (n <= 0) {
            return 0L;
        }
        if (n <= 2) {
            return 1L;
        }
        long a = 1L;
        long b = 1L;
        for (int i = 3; i <= n; i++) {
            long c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    // endregion

    // region stellar acceleration

    /**
     * The star-lifespan reduction (machine ticks) applied at the end of an accelerating second: the square root of
     * the consumed tachyon fluid (mB of Tachyon Rich Temporal Fluid), minimum 1 ("the reduction cannot be zero").
     *
     * @param consumedMB the tachyon fluid consumed during the second (&lt;= 0 → the minimum 1).
     * @return the lifespan reduction in machine ticks (&ge; 1).
     */
    public static long lifespanReductionPerSecond(long consumedMB) {
        long mB = Math.max(0L, consumedMB);
        return Math.max(1L, (long) Math.sqrt(mB));
    }

    /**
     * The USS virtual orbit clock's advance per machine tick while an accelerating second is active:
     * {@code 1 + sqrt(consumedMB) / ORBIT_SPEEDUP_DIVISOR} — PROPORTIONAL to the consumption (a second that
     * consumes nothing advances the clock normally, by 1).
     *
     * @param consumedMB the tachyon fluid consumed during the second.
     * @return the orbit-clock advance per machine tick (&ge; 1).
     */
    public static long orbitAdvancePerTick(long consumedMB) {
        long mB = Math.max(0L, consumedMB);
        if (mB <= 0L) {
            return 1L;
        }
        return 1L + (long) (Math.sqrt(mB) / USSConstants.ORBIT_SPEEDUP_DIVISOR);
    }

    /**
     * The virtual orbit clock's advance (FRACTIONAL machine ticks) per machine tick, with the acceleration's
     * start/end smoothing: the first acceleration window ramps the clock up from the normal rate (1) to the full
     * rate, the last acceleration window ramps it back down, and the middle windows hold the full rate. The
     * caller accumulates these fractional advances into the whole-tick virtual orbit clock.
     *
     * @param fullRate  the window's full orbit rate ({@link #orbitAdvancePerTick} of the window's consumed mB,
     *                  &ge; 1).
     * @param phase     the window's phase.
     * @param windowPos the position within the window, 0.0 (first tick) .. ~1.0 (last tick).
     * @return the fractional advance per machine tick (&ge; 1; fractional during the ramps).
     */
    public static double orbitAdvanceSmoothed(double fullRate, AccelerationPhase phase, double windowPos) {
        double p = windowPos < 0.0 ? 0.0 : (windowPos > 1.0 ? 1.0 : windowPos);
        double s = p * p * (3.0 - 2.0 * p);
        switch (phase) {
            case RAMP_UP:
                return 1.0 + (fullRate - 1.0) * s;
            case RAMP_DOWN:
                return 1.0 + (fullRate - 1.0) * (1.0 - s);
            case FULL:
                return fullRate;
            case IDLE:
            default:
                return 1.0;
        }
    }

    // endregion

    // region stellar injector

    /**
     * The Stellar Injector's maximum star size: 1.5x the original sampled size (the size cap).
     *
     * @param originalSize the star's original sampled size (negative → 0).
     * @return the maximum size the injector can grow the star to.
     */
    public static double sizeCap(double originalSize) {
        return Math.max(0.0, originalSize) * USSConstants.INJECTOR_MAX_SIZE_FACTOR;
    }

    /**
     * The cargo units required to grow the star's size by {@code delta} from {@code currentSize}:
     * {@code INJECTOR_COST_PER_SIZE_UNIT x currentSize^2 x delta} — the material cost scales with the size of the
     * star (monotone in the current size), rounded UP (a fraction of a unit still costs a full unit).
     *
     * @param currentSize the star's current size (size units; &lt;= 0 → 0).
     * @param delta       the size growth (size units; &lt;= 0 → 0).
     * @return the cargo units required (&ge; 0; 1 unit = 1 item = 100 mB).
     */
    public static long cargoUnitsForSizeDelta(double currentSize, double delta) {
        if (currentSize <= 0.0 || delta <= 0.0) {
            return 0L;
        }
        double cost = USSConstants.INJECTOR_COST_PER_SIZE_UNIT * currentSize * currentSize * delta;
        return (long) Math.ceil(cost);
    }

    // endregion

    // region ripple helpers

    /**
     * R — the count of ripple points that are BOTH scanned (revealed by an Explorer) and actual ripples of the
     * field (the Universium conversion's ripple factor).
     *
     * @param field   the system's ripple field (null → 0).
     * @param scanned the scanned point indices (null/empty → 0).
     * @return the count (&ge; 0).
     */
    public static int activeScannedRipples(USSRippleField field, Collection<Integer> scanned) {
        if (field == null || scanned == null || scanned.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < field.size(); i++) {
            if (scanned.contains(i) && field.isRipple(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * The CONTINUUM STABILIZER binding: the index of the field's RIPPLE point nearest the given position (the
     * structure stands at a fixed world position, the binding is the ripple it wraps).
     *
     * @param field the system's ripple field (null → -1).
     * @param x     the position's x (fleet-anchor blocks).
     * @param y     the position's y.
     * @param z     the position's z.
     * @return the nearest ripple point's index, or -1 when the field has no ripples.
     */
    public static int nearestRippleIndex(USSRippleField field, double x, double y, double z) {
        if (field == null) {
            return -1;
        }
        USSPosition at = USSPosition.of(x, y, z);
        int best = -1;
        double bestDist = Double.MAX_VALUE;
        for (int i = 0; i < field.size(); i++) {
            if (!field.isRipple(i)) {
                continue;
            }
            double d = field.positionOf(i)
                .distanceTo(at);
            if (d < bestDist) {
                bestDist = d;
                best = i;
            }
        }
        return best;
    }

    // endregion
}
