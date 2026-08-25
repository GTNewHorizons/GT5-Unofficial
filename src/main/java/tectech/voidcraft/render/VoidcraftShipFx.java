package tectech.voidcraft.render;

import net.minecraft.nbt.NBTTagCompound;

import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.ship.VoidcraftRole;

/**
 * Pass 8 — pure math for the client-side ship effects (mining laser beam + exhaust), kept out of the GL class so
 * it unit-tests in a bare JVM (no LWJGL, no {@code Minecraft} statics).
 *
 * <p>
 * <strong>Beam</strong> (user spec): during the MINING leg, a thin laser rod from the MIDDLE of the ship to the
 * MIDDLE of the body it works — for MINERS and STARLIFTERS (a Constructor builds, it does not fire). The rod
 * fades in over the first {@link #BEAM_FADE_RAMP} of the leg and out over the last, so the state transitions
 * (OUTBOUND→MINING→RETURNING) read as the beam engaging and releasing instead of popping.
 *
 * <p>
 * <strong>Exhaust</strong> (user spec): particles emitted BEHIND the ship — the opposite of its direction of
 * travel — on the legs it actually moves (OUTBOUND / RETURNING), not while it hovers.
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
     * Whether the mining laser fires for the given ship (user spec: MINER and STARLIFTER — a Constructor builds
     * during its leg and does not fire; a pure transport has no mining leg at all).
     *
     * @param payload the ship payload NBT (carries {@link VoidcraftNbt#TAG_ROLES}); null-safe
     */
    public static boolean minesWithBeam(NBTTagCompound payload) {
        if (payload == null) {
            return false;
        }
        int roles = VoidcraftNbt.readInt(payload, VoidcraftNbt.TAG_ROLES);
        return VoidcraftRole.MINER.isActive(roles) || VoidcraftRole.STARLIFTER.isActive(roles);
    }

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

    /**
     * Exhaust spawn gate: deterministic (no RNG state on the render thread) — fires on exactly 3 of every 8
     * consecutive world ticks, offset by the ship's per-launch seed so a fleet does not puff in lockstep.
     *
     * @param tick the world time in ticks (clamped at 0 for corrupt values)
     * @param seed the ship's per-launch seed
     */
    public static boolean exhaustGate(long tick, int seed) {
        long t = tick < 0 ? 0L : tick;
        return (t + (long) seed * 7L) % 8L < 3L;
    }
}
