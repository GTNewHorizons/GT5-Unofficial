package tectech.voidcraft.render;

/**
 * Pure math for the client-side ship effects (mining laser beam + thruster trail), kept out of the GL class so
 * it unit-tests in a bare JVM (no LWJGL, no {@code Minecraft} statics).
 *
 * <p>
 * <strong>Beam</strong>: during a work leg, a thin laser rod from the MIDDLE of the ship to the MIDDLE of the
 * body it works — on the MINE and SIPHON legs (a CONSTRUCT leg builds at the site, a SCAN leg scans). The rod
 * fades in over the first {@link #BEAM_FADE_RAMP} of the leg and out over the last, so the state transitions
 * (OUTBOUND→MINING→RETURNING) read as the beam engaging and releasing instead of popping.
 *
 * <p>
 * <strong>Trail</strong>: while a ship moves (the OUTBOUND / RETURNING legs — not while it hovers on a body),
 * a fading tube trail runs BEHIND it, opposite its direction of travel: {@value TRAIL_SECTIONS} sections,
 * alpha {@value #TRAIL_ALPHA_MAX} at the ship fading by STEP per section to 0 + STEP at the tail
 * ({@link #trailSectionAlpha}), total length scaled by the ship's rendered
 * speed ({@link #trailLength}), ramped from 0 to full length over the first and last {@value TRAIL_LENGTH_RAMP}
 * of the travel leg ({@link #trailLengthScale}).
 */
public final class VoidcraftShipFx {

    /**
     * Beam half-width in blocks — a thin laser rod that is still visible across the system. Pass 10 (user: "quite
     * wide — ~4× smaller is fine, very narrow is fine"): a quarter of the original 0.045 → 0.01125. Pass 26 (user:
     * "the mining beams are no longer rendering for miners that are mining — miners orbiting a planet don't have
     * any beam on them"): the 0.01125 rod was ~2 cm wide and, for a body-miner, mostly occluded inside the body
     * (only a short stub above the surface was visible) — so it effectively disappeared. Widened to a clearly
     * visible ~8 cm rod (0.04) so the mining laser reads again. Pass 28 (user: "the mining laser beam was
     * previously made thicker, but it was good previously — make it 4 times less thick"): a quarter of 0.04 →
     * 0.01 (~2 cm rod, close to the pass-10 width the user liked).
     */
    public static final double BEAM_HALF_WIDTH = 0.01;

    /**
     * Beam fade ramp as a FRACTION of the mining leg: fully on between the first and last {@value} of the leg.
     * Pass 26: shortened from 0.15 to 0.08 so the beam is at full strength for a wider middle of the leg (the
     * engage/release fade is kept, just gentler — the beam is visible for longer instead of a narrow window).
     */
    public static final double BEAM_FADE_RAMP = 0.08;

    private VoidcraftShipFx() {}

    /**
     * Beam visibility for the mining-leg progress in [0, 1]: 0 at either end, 1 in the middle, with a linear ramp
     * of {@link #BEAM_FADE_RAMP} at both ends (symmetric: {@code beamFade(p) == beamFade(1 - p)}).
     */
    public static double beamFade(double progress) {
        if (progress <= 0.0 || progress >= 1.0) {
            return 0.0;
        }
        double v = Math.min(progress / BEAM_FADE_RAMP, (1.0 - progress) / BEAM_FADE_RAMP);
        return Math.min(1.0, v);
    }

    /**
     * Beam basis for the rod between two (world) endpoints: the normalized direction {@code d} plus two
     * orthonormal perpendiculars {@code p1}/{@code p2} (a right-handed frame, {@code p1 x p2 = d}).
     *
     * @param start the ship center (world coordinates)
     * @param end   the body center (world coordinates)
     * @return {@code {dx, dy, dz, p1x, p1y, p1z, p2x, p2y, p2z}}, or null when the endpoints coincide
     *         (degenerate geometry — the renderer then draws nothing)
     */
    public static double[] beamBasis(double[] start, double[] end) {
        double dx = end[0] - start[0];
        double dy = end[1] - start[1];
        double dz = end[2] - start[2];
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len < 1e-9) {
            return null;
        }
        dx /= len;
        dy /= len;
        dz /= len;
        // Reference vector NOT parallel to d (d is usually vertical — a straight-down mining pose — so prefer the
        // X axis in that case; the Y axis only when d is far from vertical).
        double rx = 0.0;
        double ry = 1.0;
        double rz = 0.0;
        if (Math.abs(dy) > 0.9) {
            rx = 1.0;
            ry = 0.0;
        }
        // p1 = normalize(d x r); p2 = d x p1 (unit by construction, right-handed).
        double p1x = dy * rz - dz * ry;
        double p1y = dz * rx - dx * rz;
        double p1z = dx * ry - dy * rx;
        double p1len = Math.sqrt(p1x * p1x + p1y * p1y + p1z * p1z);
        p1x /= p1len;
        p1y /= p1len;
        p1z /= p1len;
        double p2x = dy * p1z - dz * p1y;
        double p2y = dz * p1x - dx * p1z;
        double p2z = dx * p1y - dy * p1x;
        return new double[] { dx, dy, dz, p1x, p1y, p1z, p2x, p2y, p2z };
    }

    /** Number of trail sections behind a moving ship. */
    public static final int TRAIL_SECTIONS = 9;
    /** The trail's alpha at the ship (section 0). */
    public static final double TRAIL_ALPHA_MAX = 0.7;
    /** Trail length (blocks) per 1 block/tick of rendered ship speed. */
    public static final double TRAIL_LENGTH_PER_SPEED = 15.0;
    /** Trail length floor (blocks) — a slow ship still leaves a short trail. */
    public static final double TRAIL_LENGTH_MIN = 0.25;
    /** Trail length ceiling (blocks). */
    public static final double TRAIL_LENGTH_MAX = 8.0;
    /**
     * The fraction of a travel leg over which the trail's length ramps 0 → 1 at the start and 1 → 0 at the
     * end (the ship "speeds up" out of the gateway and "slows down" into it).
     */
    public static final double TRAIL_LENGTH_RAMP = 0.02;

    /**
     * The trail's total length in blocks for a rendered speed in blocks/tick: proportional to the speed,
     * clamped to {@link #TRAIL_LENGTH_MIN}–{@link #TRAIL_LENGTH_MAX}.
     *
     * @param blocksPerTick the ship's rendered speed (leg distance over the leg's tick duration); corrupt
     *                      (negative) values degrade to the floor
     */
    public static double trailLength(double blocksPerTick) {
        double v = Math.max(0.0, blocksPerTick) * TRAIL_LENGTH_PER_SPEED;
        return Math.max(TRAIL_LENGTH_MIN, Math.min(TRAIL_LENGTH_MAX, v));
    }

    /**
     * The trail's length scale at a travel leg's progress in [0, 1]: 0 at the leg's start, smooth-stepping up to
     * 1 over the first {@value TRAIL_LENGTH_RAMP} of the leg, 1 across the middle, and smooth-stepping back to 0
     * over the last — so the trail grows out of the gateway and shrinks into it. Out-of-range progress clamps to
     * the endpoints.
     */
    public static double trailLengthScale(double progress) {
        double p = Math.min(1.0, Math.max(0.0, progress));
        double v = Math.min(p, 1.0 - p) / TRAIL_LENGTH_RAMP;
        if (v <= 0.0) {
            return 0.0;
        }
        if (v >= 1.0) {
            return 1.0;
        }
        return v * v * (3.0 - 2.0 * v);
    }

    /**
     * The alpha of trail section {@code section} (0 = the section at the ship): a linear ramp from
     * {@link #TRAIL_ALPHA_MAX} at the ship, down by STEP = {@link #TRAIL_ALPHA_MAX} / {@value TRAIL_SECTIONS}
     * per section, so the last section sits at 0 + STEP — still visible (a 0-alpha section renders nothing).
     * Out-of-range sections clamp to the ramp's ends.
     */
    public static double trailSectionAlpha(int section) {
        if (section < 0) {
            section = 0;
        }
        if (section >= TRAIL_SECTIONS) {
            return 0.0;
        }
        return TRAIL_ALPHA_MAX * (1.0 - section / (double) TRAIL_SECTIONS);
    }
}
