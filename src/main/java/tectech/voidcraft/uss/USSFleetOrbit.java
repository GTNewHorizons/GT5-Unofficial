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
 * Bounds: |offset| ≤ {@link #MAX_RADIUS} (2.0 blocks). Pass 13/14 re-based the shell bound: planets orbit from
 * 3 blocks out to 4 blocks INSIDE the Voidcraft dome (USSPlanets.MAX_DISTANCE = SPACE_SHELL_RADIUS − 4 = 23.1,
 * dome 27.1 — pass 12's 25.9 + 1.5, −0.3 in pass 15), so the planet center worst case is 0.2 + 23.1 + 0.28 =
 * 23.58 from the star center — and since the 4-block margin exceeds hover 0.5 + cube half 0.375 + spread 2.0 =
 * 2.875, even the worst-case ship (≈ 26.46) stays INSIDE the dome (pass 14, user: ships must not exit the dome).
 * (The legacy
 * 12.95 dome and the old 12.355 worst case belonged to the pre-pass-13 range; the legacy EoH machine keeps its
 * 12.95 dome via its own domeRadius default.)
 *
 * <p>
 * Pure function — unit-testable in the bare JVM.
 */
public final class USSFleetOrbit {

    /**
     * Maximum spread magnitude in blocks (pass 11 removed the bob term). Pass 14: with the 4-block orbit margin
     * (USSPlanets.MAX_DISTANCE = dome − 4), spread 2.0 + hover 0.5 + planet half ≤ 0.375 = 2.875 < 4.0, so even
     * the outermost hovering ship stays inside the Voidcraft dome.
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
     * Pass 30 (user: "the orbital speed of planets is not very realistic. Planets at the outer edges move a lot
     * faster than planets closer to the sun. It should be calculated to roughly approximate real orbital
     * mechanics, where far out planets move very slowly while closer planets move faster. For pinning the speed to
     * some numbers, a planet at X blocks should take X minutes to complete one full rotation"): the orbit angle is
     * a pure function of the orbit RADIUS — inner planets orbit fast, outer planets very slowly (the qualitative
     * shape of real orbital mechanics). Pass 37 (user: "the planets feel very fast. Make their orbital speed 10x
     * slower"): the pass-30 pace (a planet at X blocks took X·1200 ticks to orbit, 0.3/X deg per tick) is 10x too
     * quick, so the constant is 0.03/X deg per tick — a planet at X blocks now takes 10·X minutes (10·X·1200 ticks)
     * to complete one orbit. The star renderer draws the planets with THIS constant (single source of truth, see
     * {@code EOHRenderingUtils}), so ships, rendered planets and the server all share one law and never drift
     * apart.
     */
    public static final float ORBIT_DEG_PER_TICK_PER_BLOCK = 0.03f;

    private USSFleetOrbit() {
        throw new AssertionError("Constants holder");
    }

    /**
     * Pass 7 — the live position of a planet in FLEET-ANCHOR coordinates at the given world time. This is
     * EXACTLY the orbit math {@code EOHRenderingUtils.renderUSSOrbits} uses to draw the planet (radius
     * {@code 0.2 + distance + 0.2·starSize}; angle {@code (0.03·time)/radius} — the pass-30 radius law, slowed 10x in
     * pass 37; tilts
     * {@code xAngle}/{@code zAngle})
     * plus the anchor's 2-block offset over the star — so a ship hovering
     * {@link USSConstants#HOVER_ABOVE_PLANET} above this point sits precisely above the rendered body.
     *
     * @param distance   the planet's orbit radius in star-center blocks (the spec's distance)
     * @param orbitSpeed the planet's legacy orbit-speed field — retained in the signature; since pass 30 the orbit
     *                   angle is a pure function of the orbit radius (the old random 0.5–1.5 speed made far
     *                   planets orbit as fast as near ones)
     * @param xAngle     the orbit tilt about X (degrees)
     * @param zAngle     the orbit tilt about Z (degrees)
     * @param starSize   the star's rendered size (orbit radius = 0.2 + distance + 0.2·starSize)
     * @param time       world time + partial ticks, in TICKS (the shared render clock — getTotalWorldTime is ticks)
     * @return {@code double[3]} position in fleet-anchor coordinates (pure function — bare-JVM testable)
     */
    public static double[] planetAnchorPosition(float distance, float orbitSpeed, float xAngle, float zAngle,
        float starSize, float time) {
        final float radius = 0.2f + distance + 0.2f * starSize;
        // Pass 30 (user: "a planet at X blocks should take X minutes to complete one full rotation") + pass 37
        // (10x slower): angular speed 0.03/X degrees per tick — inner fast, outer slow. (The old
        // orbitSpeed·0.1·time was independent of radius, so outer planets swept the sky as fast as inner ones.)
        final float orbitAngle = (ORBIT_DEG_PER_TICK_PER_BLOCK * time / radius) % 360f;
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
     * Gateway render pass (user: "make a gateway render (just a simple gray circle) at the edge of the solar
     * system, closest to the side the gateway is on" — and use it as the start/end point for the ship
     * animations): the point ON the space-dome surface closest to the given gateway position — the gateway
     * projected onto the dome along the star-center → gateway direction. The dome is the EoH space shell
     * (centered on the star center {@code (0, STAR_CENTER_Y, 0)}, radius
     * {@link USSConstants#SPACE_SHELL_RADIUS}); the actual gateway block sits OUTSIDE the dome and must not be
     * the animation's visual anchor — ships spawn at (and return to) this dome-edge point instead, so they
     * spawn inside the shell and leave from a better-looking spot.
     *
     * <p>
     * Pure function (bare-JVM testable): the client draws its gray gateway circle here and starts/ends every
     * ship animation here.
     *
     * @param gateway the ACTUAL gateway position in fleet-anchor coordinates (the entry's {@code vc_gw_rel};
     *                length 3, never null)
     * @return {@code double[3]} the dome-surface point in the gateway's direction (a FRESH array; never null)
     */
    public static double[] gatewayEdgePoint(double[] gateway) {
        double cx = gateway[0];
        double cy = gateway[1] - STAR_CENTER_Y;
        double cz = gateway[2];
        double len = Math.sqrt(cx * cx + cy * cy + cz * cz);
        if (len < 1e-9) {
            // Degenerate: the gateway sits AT the star center — there is no direction, keep the gateway itself.
            return new double[] { gateway[0], gateway[1], gateway[2] };
        }
        double f = USSConstants.SPACE_SHELL_RADIUS / len;
        return new double[] { cx * f, STAR_CENTER_Y + cy * f, cz * f };
    }

    // region stateful-position pass (USSPosition — the "distance within the solar system" model)

    /**
     * The star's position in the solar system (the stateful-position pass) — a FIXED point: the star center
     * {@code (0, STAR_CENTER_Y, 0)} = {@code (0, -2, 0)} in anchor coordinates.
     *
     * @return the star's position (never null)
     */
    public static USSPosition starPosition() {
        return USSPosition.starCenter();
    }

    /**
     * A planet's LIVE position in the solar system at the given world time (the stateful-position pass) — the exact
     * orbit math {@link #planetAnchorPosition} uses to draw the planet, returned as a {@link USSPosition}.
     *
     * @param planet   the planet (its orbit params)
     * @param starSize the star's rendered size (orbit radius = 0.2 + distance + 0.2·starSize)
     * @param time     world time in TICKS (the shared render clock — the SAME time base
     *                 {@link #planetAnchorPosition} expects; both sides of the server/client split evaluate this
     *                 function at the synced world time, so the server's planet position and the rendered one can
     *                 never drift apart)
     * @return the planet's position in anchor coordinates (never null)
     */
    public static USSPosition planetPosition(USSPlanets.USSPlanet planet, float starSize, float time) {
        double[] p = planetAnchorPosition(
            (float) planet.distance,
            (float) planet.orbitSpeed,
            (float) planet.xAngle,
            (float) planet.zAngle,
            starSize,
            time);
        return USSPosition.of(p[0], p[1], p[2]);
    }

    /**
     * A deterministic, seed-random point on a SPHERICAL SHELL around a center (the stateful-position pass): used
     * for a Miner's hover point — "a random target position around the orbit of the planet, within a spherical
     * shell (so not only above it, but on any side)."
     *
     * <p>
     * The point is at exactly {@code radius} from the center, on a random direction (uniform over the sphere via
     * the two-angle method). Deterministic in {@code seed} (the ship's per-launch seed), so server and client agree
     * and the same ship always hovers at the same spot (no flicker).
     *
     * @param center the shell center (the planet's live position; null → origin)
     * @param radius the shell radius in blocks (the hover distance; &lt;= 0 → the center itself)
     * @param seed   the ship's per-launch seed (any long; only its low 32 bits matter)
     * @return a point at exactly {@code radius} from the center (never null)
     */
    public static USSPosition shellPoint(USSPosition center, double radius, long seed) {
        USSPosition origin = (center == null) ? USSPosition.zero() : center;
        if (radius <= 0.0) {
            return origin;
        }
        java.util.Random rng = new java.util.Random(seed ^ 0x5550535348454C4DL); // salt: "USSSHELL"
        // Uniform direction on the unit sphere (Marsaglia's two-angle method — no clustering at the poles).
        double theta = rng.nextDouble() * 2.0 * Math.PI; // azimuth
        double z = 1.0 - rng.nextDouble() * 2.0; // cos(phi) in [-1, 1]
        double rxy = Math.sqrt(Math.max(0.0, 1.0 - z * z)); // sin(phi)
        double dx = rxy * Math.cos(theta);
        double dy = z;
        double dz = rxy * Math.sin(theta);
        return origin.add(dx * radius, dy * radius, dz * radius);
    }

    /**
     * Half-width of the station equatorial band (degrees): a planet-anchored Voidbase stays within this angle of
     * the planet's orbital plane (its equator) instead of floating directly above the planet.
     */
    public static final float ORBITAL_BAND_DEG = 30.0f;

    /**
     * A deterministic point in a planet's EQUATORIAL BAND (the station hover law): on the shell of radius
     * {@code radius} around the planet center, at an elevation of at most {@link #ORBITAL_BAND_DEG} from the
     * planet's orbital plane (the plane through the planet center, parallel to its orbit) and a seeded azimuth
     * within that plane.
     *
     * <p>
     * The orbital plane's normal follows {@link #planetAnchorPosition} (Ry(θ)·(r,0,0), then Rz(zAngle), then
     * Rx(xAngle)) — the normal is Rx(xAngle)·Rz(zAngle)·(0,1,0). Deterministic in {@code seed} (the planet
     * index — one stable point per planet); server and client evaluate this at the synced world time, so they
     * never drift apart.
     *
     * @param center the planet's live position (null → origin)
     * @param radius the shell radius in blocks (the hover distance; &lt;= 0 → the center itself)
     * @param seed   the deterministic seed (the planet index)
     * @param xAngle the orbit tilt about X (degrees)
     * @param zAngle the orbit tilt about Z (degrees)
     * @return the band point (never null), exactly {@code radius} from the center
     */
    public static USSPosition orbitalBandPoint(USSPosition center, double radius, long seed, float xAngle,
        float zAngle) {
        USSPosition origin = (center == null) ? USSPosition.zero() : center;
        if (radius <= 0.0) {
            return origin;
        }
        java.util.Random rng = new java.util.Random(seed ^ 0x5550535342414E44L); // salt: "USSBAND"
        double alpha = rng.nextDouble() * 2.0 * Math.PI; // azimuth within the orbital plane
        double delta = Math.toRadians((rng.nextDouble() * 2.0 - 1.0) * ORBITAL_BAND_DEG); // elevation, [-30°, +30°]
        double cx = Math.cos(Math.toRadians(xAngle));
        double sx = Math.sin(Math.toRadians(xAngle));
        double cz = Math.cos(Math.toRadians(zAngle));
        double sz = Math.sin(Math.toRadians(zAngle));
        // The orbital plane normal: Rx(xAngle)·Rz(zAngle)·(0,1,0).
        double nx = -sz;
        double ny = cz * cx;
        double nz = cz * sx;
        // Orthonormal basis of the plane: m1 = (0, -sin x, cos x) (unit, perpendicular to n); m2 = n × m1.
        double m1x = 0.0, m1y = -sx, m1z = cx;
        double m2x = ny * m1z - nz * m1y;
        double m2y = nz * m1x - nx * m1z;
        double m2z = nx * m1y - ny * m1x;
        double inPlane = Math.cos(delta);
        double outPlane = Math.sin(delta);
        double dx = inPlane * (Math.cos(alpha) * m1x + Math.sin(alpha) * m2x) + outPlane * nx;
        double dy = inPlane * (Math.cos(alpha) * m1y + Math.sin(alpha) * m2y) + outPlane * ny;
        double dz = inPlane * (Math.cos(alpha) * m1z + Math.sin(alpha) * m2z) + outPlane * nz;
        return origin.add(dx * radius, dy * radius, dz * radius);
    }

    /**
     * A deterministic, seed-random NUDGE around a center (the stateful-position pass): used when a ship targets
     * ANOTHER ship — "the voidcraft that has the target should visually nudge itself randomly to create 'clouds'
     * of Voidcraft instead of overlapping them all."
     *
     * <p>
     * The nudge is a small offset (magnitude &le; {@code maxRadius}) so the targeting ship hovers NEAR the target
     * ship but not exactly on top of it. Deterministic in {@code seed} (the targeting ship's per-launch seed), so
     * each ship gets its own stable spot and the fleet reads as a "cloud" rather than a single stacked point.
     *
     * @param center    the target ship's position (null → origin)
     * @param maxRadius the maximum nudge magnitude in blocks (&le; 0 → the center itself)
     * @param seed      the targeting ship's per-launch seed (any long; only its low 32 bits matter)
     * @return the nudged position (never null; within {@code maxRadius} of the center)
     */
    public static USSPosition nudge(USSPosition center, double maxRadius, long seed) {
        USSPosition origin = (center == null) ? USSPosition.zero() : center;
        if (maxRadius <= 0.0) {
            return origin;
        }
        // Reuse the swarm-spread offset (a deterministic, well-spread point within MAX_RADIUS=2.0), scaled to
        // maxRadius — so a cloud of ships around a target stays compact and stable.
        double[] o = offsetFor(seed ^ 0x555053534E554447L); // salt: "USSNUDG"
        double scale = maxRadius / MAX_RADIUS;
        return origin.add(o[0] * scale, o[1] * scale, o[2] * scale);
    }

    // endregion

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
