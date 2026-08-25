package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Spacetime-ripple field generation (the Explorer mechanic).
 *
 * <p>
 * Every solar system carries a uniformly spaced {@link #GRID_SIZE}×{@link #GRID_SIZE}×{@link #GRID_SIZE} point grid
 * (343 points). The field is a PURE function of (star type, seed) — the same inputs always yield the identical
 * field — so server, client, and GUI agree. The ripple COUNT is sampled inclusively from the star's
 * {@code spacetimeRipples} range ({@link USSStarDefinition#getRippleMin()}…{@link USSStarDefinition#getRippleMax()},
 * 0–128), and the ripple POINTS are chosen deterministically (seeded).
 *
 * <p>
 * <strong>Shell mapping.</strong> "Visually, the spots should be mapped to 3 different sphere shells with uniform
 * spacing." Each grid point is assigned to one of {@link #NUM_SHELLS} shells by its radius from the grid center
 * (three equal radial bands), and then placed on that shell's sphere — the shells sit at evenly-spaced radii
 * ({@link #shellRadius(int)}: {@code RIPPLE_SHELL_RADIUS/3, 2·/3, RIPPLE_SHELL_RADIUS}), all relative to the star
 * center ({@link USSPosition#starCenter()}), so every spot is a real position within the solar system.
 *
 * <p>
 * Bare-JVM safe + deterministic (a {@link Random} seeded by {@code seed} and a fixed salt — the
 * {@code java.util.Random(long)}
 * contract guarantees identical sequences on server and client).
 *
 * @see USSRippleField
 * @see USSRipplePoint
 */
public final class USSRipples {

    /** The grid edge length: a 7×7×7 field (user spec). */
    public static final int GRID_SIZE = 7;
    /** The number of visual shells the grid is mapped onto (user spec: 3). */
    public static final int NUM_SHELLS = 3;
    /** Total points in the field (7³ = 343). */
    public static final int POINT_COUNT = GRID_SIZE * GRID_SIZE * GRID_SIZE;
    /**
     * The OUTERMOST shell radius in blocks (the inner shells are at ⅓ and ⅔ of this, evenly spaced). Chosen to sit
     * comfortably inside the Voidcraft dome ({@code USSConstants.SPACE_SHELL_RADIUS} = 27.1) and across the planet
     * orbit band (3..23.1).
     */
    public static final double RIPPLE_SHELL_RADIUS = 24.0;

    /** The star center in fleet-anchor coordinates — the origin of every ripple position. */
    private static final USSPosition STAR_CENTER = USSPosition.starCenter();

    /** The maximum grid radius (a corner of the 7×7×7 cube, 3 from the center on each axis). */
    private static final double MAX_GRID_RADIUS = Math.sqrt(3.0) * (GRID_SIZE / 2.0);

    /** Seed salt (the ASCII "USSRIPPE") so the field draws are independent of the planet/star size salts. */
    private static final long SALT = 0x5553535249505045L;

    private USSRipples() {
        throw new AssertionError("Static holder");
    }

    /**
     * The render radius (in blocks, relative to the star center) of the given shell: the shells are at
     * {@code RIPPLE_SHELL_RADIUS/3, 2·RIPPLE_SHELL_RADIUS/3, RIPPLE_SHELL_RADIUS} (evenly spaced).
     *
     * @param shell a shell index (0 = innermost … {@code NUM_SHELLS-1} = outermost)
     * @return the shell radius in blocks (always &gt; 0 for a valid shell)
     */
    public static double shellRadius(int shell) {
        int s = Math.max(0, Math.min(NUM_SHELLS - 1, shell));
        return RIPPLE_SHELL_RADIUS * (s + 1) / NUM_SHELLS;
    }

    /**
     * Generate a star system's spacetime-ripple field (the Explorer mechanic).
     *
     * <p>
     * A pure function of (star type, seed): the ripple COUNT is sampled inclusively from the star's
     * {@code spacetimeRipples} range, and the ripple POINTS are chosen by a seeded shuffle of the 343 grid indices —
     * so (starType, seed) yields exactly ONE fixed field and server/client agree.
     *
     * @param starType the star's type (null → {@link USSStarType#MAIN_SEQUENCE}, defensive)
     * @param seed     any stable long (the USS ignition timestamp in practice)
     * @return the field (never null; always the full 343-point grid, with {@code rippleCount} ripples)
     */
    public static USSRippleField generate(USSStarType starType, long seed) {
        if (starType == null) {
            starType = USSStarType.MAIN_SEQUENCE;
        }
        USSStarDefinition star = USSStarRegistry.byType(starType);
        int rippleMin = star != null ? star.getRippleMin() : USSStarDefinition.MIN_RIPPLES;
        int rippleMax = star != null ? star.getRippleMax() : USSStarDefinition.MAX_RIPPLES;

        Random rng = new Random(seed ^ SALT);
        // The ripple count is the FIRST draw — rippleMin…rippleMax inclusive (Random(long) contract → deterministic).
        int rippleCount = rippleMin + rng.nextInt(rippleMax - rippleMin + 1);

        // Build the full 343-point grid (positions are pure — no RNG).
        List<USSRipplePoint> points = new ArrayList<>(POINT_COUNT);
        for (int z = 0; z < GRID_SIZE; z++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                for (int x = 0; x < GRID_SIZE; x++) {
                    int index = x + y * GRID_SIZE + z * GRID_SIZE * GRID_SIZE;
                    double dx = x - GRID_SIZE / 2.0;
                    double dy = y - GRID_SIZE / 2.0;
                    double dz = z - GRID_SIZE / 2.0;
                    double r = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    // Assign to a shell by the radial band (three equal bands), then place on that shell's sphere.
                    int shell = (int) Math.min(NUM_SHELLS - 1, r * NUM_SHELLS / MAX_GRID_RADIUS);
                    double inv = (r > 0.0) ? 1.0 / r : 0.0;
                    // The grid center (r=0) has no direction — give it the +Y axis so it still lands on a shell.
                    double dirX = dx * inv;
                    double dirY = (r > 0.0) ? dy * inv : 1.0;
                    double dirZ = dz * inv;
                    double radius = shellRadius(shell);
                    USSPosition position = STAR_CENTER.add(dirX * radius, dirY * radius, dirZ * radius);
                    points.add(new USSRipplePoint(index, x, y, z, position, false, shell));
                }
            }
        }

        // Choose the ripple points: a seeded shuffle of the 343 indices, take the first `rippleCount`.
        List<Integer> order = new ArrayList<>(POINT_COUNT);
        for (int i = 0; i < POINT_COUNT; i++) {
            order.add(i);
        }
        for (int i = order.size() - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            Integer tmp = order.get(i);
            order.set(i, order.get(j));
            order.set(j, tmp);
        }

        // Mark the chosen points as ripples.
        boolean[] isRipple = new boolean[POINT_COUNT];
        for (int i = 0; i < rippleCount && i < POINT_COUNT; i++) {
            isRipple[order.get(i)] = true;
        }
        List<USSRipplePoint> finalPoints = new ArrayList<>(POINT_COUNT);
        for (int i = 0; i < POINT_COUNT; i++) {
            USSRipplePoint p = points.get(i);
            finalPoints.add(
                isRipple[i]
                    ? new USSRipplePoint(
                        p.getIndex(),
                        p.getGridX(),
                        p.getGridY(),
                        p.getGridZ(),
                        p.getPosition(),
                        true,
                        p.getShell())
                    : p);
        }

        return new USSRippleField(GRID_SIZE, rippleCount, finalPoints);
    }
}
