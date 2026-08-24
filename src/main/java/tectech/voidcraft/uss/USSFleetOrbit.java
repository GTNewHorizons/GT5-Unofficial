package tectech.voidcraft.uss;

/**
 * Phase 4 pass 5: deterministic per-ship "swarm spread" for a USS fleet.
 *
 * <p>
 * With dozens–hundreds of ships (see {@link USSConstants#MAX_SHIPS_PER_USS}), the fleet must not stack at one
 * point: each ship hovers at a stable, deterministic offset around its role's shared hover point, derived from a
 * per-ship key — no extra NBT, and the same offset on every client for the life of the world.
 *
 * <p>
 * The key is the USS-assigned <strong>per-launch fleet seed</strong> (see {@link #offsetFor(long)}); the item
 * UUID is only a fallback. Ships are ITEMS — a player can duplicate one in creative, and every copy shares the
 * item's {@code vc_uuid}. Keying the spread (and the client's per-ship animation phases) on the item UUID alone
 * made all duplicates hover at one spot and corrupt each other's leg progress. The seed is assigned once per
 * launch by the USS and persisted on the ship's mission, so it is unique per flight even for identical items.
 *
 * <p>
 * Bounds: |offset| ≤ {@link #MAX_RADIUS} (2.0 blocks). The outermost shared hover point is 10.355 blocks from
 * the star center (outermost planet 9.48 + hover 0.5 + the planet cube's half-height 0.375) and — pass 11: the
 * vertical bob is gone — so even the worst-case collinear sum (10.355 + 2.0 = 12.355) stays INSIDE the 12.95-block
 * EoH space shell (user rule: ships render inside the bubble).
 *
 * <p>
 * Pure function — unit-testable in the bare JVM.
 */
public final class USSFleetOrbit {

    /**
     * Maximum spread magnitude in blocks (kept so the outermost miner hover + spread stays inside 12.95; pass 11
     * removed the bob term).
     */
    public static final double MAX_RADIUS = 2.0;

    /** Maximum vertical spread component in blocks (before the magnitude clamp below). */
    public static final double MAX_HEIGHT = 1.0;

    /**
     * Pass 7 — the fleet anchor sits exactly 2 blocks ABOVE the star block (see the USS's anchor layout), so the
     * star center is at (0, -2, 0) in fleet-anchor coordinates. Shared by the hover math below and the client.
     */
    public static final double STAR_CENTER_Y = -2.0;

    /**
     * Pass 7 — orbit angular speed scale, mirroring {@code EOHRenderingUtils.USS_ORBIT_SPEED_SCALE} (0.1): the
     * planet angle in degrees is {@code orbitSpeed · 0.1 · time(seconds)} — the SAME formula the star renderer
     * draws the planets with, so ships and rendered planets never drift apart.
     */
    public static final float ORBIT_SPEED_SCALE = 0.1f;

    private USSFleetOrbit() {
        throw new AssertionError("Constants holder");
    }

    /**
     * Pass 7 — the live position of a planet in FLEET-ANCHOR coordinates at the given world time. This is
     * EXACTLY the orbit math {@code EOHRenderingUtils.renderUSSOrbits} uses to draw the planet (radius
     * {@code 0.2 + distance + 0.2·starSize}; angle {@code orbitSpeed·0.1·time}; tilts {@code xAngle}/{@code zAngle})
     * plus the anchor's 2-block offset over the star — so a ship hovering
     * {@link USSConstants#HOVER_ABOVE_PLANET} above this point sits precisely above the rendered body.
     *
     * @param distance   the planet's orbit radius in star-center blocks (the spec's distance)
     * @param orbitSpeed the planet's orbit speed (the spec's orbitSpeed)
     * @param xAngle     the orbit tilt about X (degrees)
     * @param zAngle     the orbit tilt about Z (degrees)
     * @param starSize   the star's rendered size (orbit radius = 0.2 + distance + 0.2·starSize)
     * @param time       world time + partial ticks, in seconds (the shared render clock)
     * @return {@code double[3]} position in fleet-anchor coordinates (pure function — bare-JVM testable)
     */
    public static double[] planetAnchorPosition(float distance, float orbitSpeed, float xAngle, float zAngle,
        float starSize, float time) {
        final float orbitAngle = (orbitSpeed * ORBIT_SPEED_SCALE * time) % 360f;
        final float radius = 0.2f + distance + 0.2f * starSize;
        // rotX(xAngle) · rotZ(zAngle) · rotY(orbitAngle) applied to (radius, 0, 0) — joml's chain order in
        // renderUSSOrbits (Rx, then Rz, then Ry, then translate).
        final double ct = Math.cos(Math.toRadians(orbitAngle));
        final double st = Math.sin(Math.toRadians(orbitAngle));
        final double cp = Math.cos(Math.toRadians(zAngle));
        final double sp = Math.sin(Math.toRadians(zAngle));
        final double cs = Math.cos(Math.toRadians(xAngle));
        final double ss = Math.sin(Math.toRadians(xAngle));
        return new double[] { radius * ct * cp, radius * (ct * sp * cs + st * ss) + STAR_CENTER_Y,
            radius * (ct * sp * ss - st * cs) };
    }

    /**
     * Pass 7 — the center of the body a ship works, in fleet-anchor coordinates: the planet at {@code target}
     * (a system planet index) or the star center ({@code target < 0} / Starlifter / no planets).
     *
     * @return {@code double[3]} — the body's center (pure function — bare-JVM testable)
     */
    public static double[] targetBodyCenter(int target, float distance, float orbitSpeed, float xAngle, float zAngle,
        float starSize, float time) {
        if (target >= 0) {
            return planetAnchorPosition(distance, orbitSpeed, xAngle, zAngle, starSize, time);
        }
        return new double[] { 0.0, STAR_CENTER_Y, 0.0 };
    }

    /**
     * Deterministic per-ship hover offset (the swarm spread).
     *
     * @param uuid the ship's UUID (null/empty → zero offset: a safe fallback that leaves the shared point unchanged)
     * @return {@code double[3]} offset in world blocks — horizontal XZ spread around the role hover point plus a
     *         vertical component; guaranteed |offset| ≤ {@link #MAX_RADIUS}
     */
    /**
     * Deterministic hover offset from a per-launch fleet seed (the PRIMARY key — see class javadoc).
     *
     * @param seed the USS-assigned per-launch seed (any int value; only the low 32 bits matter)
     * @return {@code double[3]} — same contract and bounds as {@link #offsetFor(String)}
     */
    public static double[] offsetFor(long seed) {
        return offsetFromBits(finalizer32((int) seed));
    }

    public static double[] offsetFor(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            return new double[] { 0.0, 0.0, 0.0 };
        }
        return offsetFromBits(uuid.hashCode() & 0xffffffffL);
    }

    private static double[] offsetFromBits(long h) {
        double theta = frac(h) * 2.0 * Math.PI;
        double radius = 0.5 + frac(h >>> 1) * 1.5; // 0.5..2.0 — never dead-center, never out of range
        double height = (frac(h >>> 2) * 2.0 - 1.0) * MAX_HEIGHT; // -1..1
        double maxHeight = Math.sqrt(Math.max(0.0, MAX_RADIUS * MAX_RADIUS - radius * radius));
        if (height > maxHeight) {
            height = maxHeight;
        } else if (height < -maxHeight) {
            height = -maxHeight;
        }
        return new double[] { Math.cos(theta) * radius, height, Math.sin(theta) * radius };
    }

    /** 32-bit finalizer mix (Murmur3-style) so sequential/small seeds get well-spread, uncorrelated slices. */
    private static long finalizer32(int x) {
        x = ((x >>> 16) ^ x) * 0x45d9f3b;
        x = ((x >>> 16) ^ x) * 0x45d9f3b;
        x = (x >>> 16) ^ x;
        return x & 0xffffffffL;
    }

    /** 24-bit slice of a 32-bit hash as a value in [0, 1). */
    private static double frac(long bits) {
        return ((bits >>> 8) & 0xffffffL) / 16777216.0;
    }
}
