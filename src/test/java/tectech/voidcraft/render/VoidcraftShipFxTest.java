package tectech.voidcraft.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.ship.VoidcraftRole;

/**
 * Pure-math tests for the pass 8 ship effects (mining laser beam + exhaust gate) — the GL layer itself is not
 * reachable in a bare JVM, so the geometry, fade curve, and spawn gate live in {@link VoidcraftShipFx} and are
 * tested here.
 */
public class VoidcraftShipFxTest {

    @Test
    public void testBeamRoleGate() {
        NBTTagCompound miner = new NBTTagCompound();
        miner.setInteger(VoidcraftNbt.TAG_ROLES, VoidcraftRole.MINER.getBit());
        assertTrue(VoidcraftShipFx.minesWithBeam(miner), "a Miner fires the beam");

        NBTTagCompound starlifter = new NBTTagCompound();
        starlifter.setInteger(VoidcraftNbt.TAG_ROLES, VoidcraftRole.STARLIFTER.getBit());
        assertTrue(VoidcraftShipFx.minesWithBeam(starlifter), "a Starlifter fires the beam");

        NBTTagCompound constructor = new NBTTagCompound();
        constructor.setInteger(VoidcraftNbt.TAG_ROLES, VoidcraftRole.CONSTRUCTOR.getBit());
        assertFalse(VoidcraftShipFx.minesWithBeam(constructor), "a Constructor builds, it does not fire");

        NBTTagCompound hybrid = new NBTTagCompound();
        hybrid.setInteger(VoidcraftNbt.TAG_ROLES, VoidcraftRole.MINER.getBit() | VoidcraftRole.CONSTRUCTOR.getBit());
        assertTrue(VoidcraftShipFx.minesWithBeam(hybrid), "a mining hybrid still fires the beam");

        assertFalse(VoidcraftShipFx.minesWithBeam(new NBTTagCompound()), "no roles -> no beam");
        assertFalse(VoidcraftShipFx.minesWithBeam(null), "null payload -> no beam");
    }

    @Test
    public void testBeamFade() {
        assertEquals(0.0, VoidcraftShipFx.beamFade(0.0), 1e-12, "off at leg start");
        assertEquals(0.0, VoidcraftShipFx.beamFade(1.0), 1e-12, "off at leg end");
        assertEquals(1.0, VoidcraftShipFx.beamFade(0.5), 1e-12, "fully on mid-leg");
        assertTrue(VoidcraftShipFx.beamFade(0.08) < 1.0, "still ramping in the first 15%");
        assertTrue(VoidcraftShipFx.beamFade(0.92) < 1.0, "still ramping in the last 15%");

        // Monotone rising over the first half.
        double prev = 0.0;
        for (double p = 0.0; p <= 0.5 + 1e-9; p += 0.01) {
            double v = VoidcraftShipFx.beamFade(p);
            assertTrue(v >= prev - 1e-12, "rising ramp monotone at p=" + p);
            prev = v;
        }
        // Symmetric: fade(p) == fade(1 - p) — the beam engages and releases identically.
        for (double p = 0.0; p <= 1.0 + 1e-9; p += 0.025) {
            assertEquals(VoidcraftShipFx.beamFade(p), VoidcraftShipFx.beamFade(1.0 - p), 1e-12, "symmetric at p=" + p);
        }
    }

    @Test
    public void testBeamBasisIsOrthonormal() {
        double[][] cases = {
            // start, end — first case is the straight-down mining pose (the common case)
            { 0.0, 0.0, 0.0, 0.0, -3.0, 0.0 }, { 1.0, 2.0, 3.0, 4.0, 5.0, 7.0 }, { 0.0, 1.0, 0.0, 0.5, -0.2, 2.0 }, };
        for (double[] c : cases) {
            double[] b = VoidcraftShipFx
                .beamBasis(new double[] { c[0], c[1], c[2] }, new double[] { c[3], c[4], c[5] });
            assertNotNull(b, "non-degenerate endpoints produce a basis: " + c[0] + "," + c[1] + "," + c[2]);
            double dx = b[0];
            double dy = b[1];
            double dz = b[2];
            double p1x = b[3];
            double p1y = b[4];
            double p1z = b[5];
            double p2x = b[6];
            double p2y = b[7];
            double p2z = b[8];
            assertEquals(1.0, norm(dx, dy, dz), 1e-9, "direction is unit length");
            assertEquals(1.0, norm(p1x, p1y, p1z), 1e-9, "p1 is unit length");
            assertEquals(1.0, norm(p2x, p2y, p2z), 1e-9, "p2 is unit length");
            assertEquals(0.0, dot(dx, dy, dz, p1x, p1y, p1z), 1e-9, "p1 perpendicular to direction");
            assertEquals(0.0, dot(dx, dy, dz, p2x, p2y, p2z), 1e-9, "p2 perpendicular to direction");
            assertEquals(0.0, dot(p1x, p1y, p1z, p2x, p2y, p2z), 1e-9, "p1 perpendicular to p2");
            // Right-handed: p1 x p2 = d (the beam corners form a proper box around the axis).
            double cx = p1y * p2z - p1z * p2y;
            double cy = p1z * p2x - p1x * p2z;
            double cz = p1x * p2y - p1y * p2x;
            assertEquals(dx, cx, 1e-9, "right-handed x");
            assertEquals(dy, cy, 1e-9, "right-handed y");
            assertEquals(dz, cz, 1e-9, "right-handed z");
        }
        assertNull(
            VoidcraftShipFx.beamBasis(new double[] { 1.0, 2.0, 3.0 }, new double[] { 1.0, 2.0, 3.0 }),
            "coincident endpoints are degenerate -> null");
    }

    @Test
    public void testExhaustGateDutyCycle() {
        // Exactly 3 of every 8 consecutive ticks fire, for any seed (a fleet should puff, not stream).
        for (int seed = 0; seed < 4; seed++) {
            for (int windowStart = 0; windowStart < 8; windowStart++) {
                int fired = 0;
                for (int t = windowStart; t < windowStart + 8; t++) {
                    if (VoidcraftShipFx.exhaustGate(t, seed)) {
                        fired++;
                    }
                }
                assertEquals(3, fired, "duty cycle 3/8 at seed=" + seed + " window=" + windowStart);
            }
        }
        // Different seeds offset the puffing (a fleet does not exhale in lockstep).
        int samePhase = 0;
        for (int t = 0; t < 64; t++) {
            if (VoidcraftShipFx.exhaustGate(t, 0) == VoidcraftShipFx.exhaustGate(t, 1)) {
                samePhase++;
            }
        }
        assertTrue(samePhase < 64, "seed 0 and 1 do not fire on identical frames");
        // Corrupt (negative) world time must not throw.
        VoidcraftShipFx.exhaustGate(-100, 0);
        assertTrue(true, "negative tick is clamped");
    }

    private static double norm(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    private static double dot(double ax, double ay, double az, double bx, double by, double bz) {
        return ax * bx + ay * by + az * bz;
    }
}
