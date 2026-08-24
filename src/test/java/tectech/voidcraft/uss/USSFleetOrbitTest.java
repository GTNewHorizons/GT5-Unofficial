package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the deterministic fleet swarm spread (Phase 4 pass 5): same ship → same hover offset for the
 * life of the world; different ships → different spots; every offset inside the magnitude bound that keeps the whole
 * fleet inside the EoH space shell.
 */
public class USSFleetOrbitTest {

    @Test
    public void testDeterministicPerUuid() {
        double[] a = USSFleetOrbit.offsetFor("123e4567-e89b-12d3-a456-426614174000");
        double[] b = USSFleetOrbit.offsetFor("123e4567-e89b-12d3-a456-426614174000");
        assertArrayEquals(a, b); // pure function: bit-identical results for the same UUID
        // and NOT the same as a different ship's offset
        double[] c = USSFleetOrbit.offsetFor("00000000-0000-0000-0000-000000000001");
        assertTrue(
            Math.abs(a[0] - c[0]) + Math.abs(a[1] - c[1]) + Math.abs(a[2] - c[2]) > 1e-9,
            "different ships must not hover at the identical spot");
    }

    @Test
    public void testBoundsKeepFleetInsideShell() {
        for (int i = 0; i < 1000; i++) {
            double[] o = USSFleetOrbit.offsetFor("ship-" + i);
            double mag = Math.sqrt(o[0] * o[0] + o[1] * o[1] + o[2] * o[2]);
            assertTrue(
                mag <= USSFleetOrbit.MAX_RADIUS + 1e-9,
                "offset magnitude " + mag + " exceeds the shell-safe bound");
            for (double v : o) {
                assertTrue(Math.abs(v) <= USSFleetOrbit.MAX_RADIUS + 1e-9);
            }
        }
    }

    @Test
    public void testFleetSpreadsOut() {
        // A realistic fleet must not collapse to one or two points: among 50 ships there must be at least 10
        // distinct hover spots (guards against a hash avalanche onto identical slots).
        int distinct = 0;
        double[][] seen = new double[50][3];
        for (int i = 0; i < 50; i++) {
            double[] o = USSFleetOrbit.offsetFor("fleet-ship-" + i);
            seen[i] = o;
        }
        outer: for (int i = 0; i < seen.length; i++) {
            for (int j = 0; j < i; j++) {
                if (Math.abs(seen[i][0] - seen[j][0]) + Math.abs(seen[i][1] - seen[j][1])
                    + Math.abs(seen[i][2] - seen[j][2]) > 1e-6) {
                    distinct++;
                    continue outer;
                }
            }
        }
        assertTrue(distinct >= 10, "only " + distinct + " distinct hover spots among 50 ships");
    }

    @Test
    public void testNullAndEmptyUuidSafe() {
        assertArrayEquals(new double[] { 0, 0, 0 }, USSFleetOrbit.offsetFor(null));
        assertArrayEquals(new double[] { 0, 0, 0 }, USSFleetOrbit.offsetFor(""));
    }

    @Test
    public void testCapacityIsFleetScale() {
        // The fleet must host dozens–hundreds of ships (user request), not a single slot.
        assertTrue(USSConstants.MAX_SHIPS_PER_USS >= 100, "fleet capacity must be large");
    }

    // region per-launch seed key (pass 5.1) — duplicated ship items share the item UUID, so the fleet must key
    // per-ship identity on the USS-assigned per-launch seed instead

    @Test
    public void testSeedOffsetsAreDeterministicAndBounded() {
        for (int i = 0; i < 1000; i++) {
            double[] a = USSFleetOrbit.offsetFor(i);
            assertArrayEquals(a, USSFleetOrbit.offsetFor(i), "pure function: bit-identical for the same seed");
            double mag = Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
            assertTrue(
                mag <= USSFleetOrbit.MAX_RADIUS + 1e-9,
                "seed " + i + " offset magnitude " + mag + " exceeds the shell-safe bound");
        }
    }

    @Test
    public void testDuplicatedShipsSpreadBySeed() {
        // The user's test loop: ONE ship item duplicated N times (same vc_uuid) launched as a fleet. Every launch
        // gets a fresh per-launch seed — those seeds must produce distinct hover spots, or the whole fleet stacks
        // at one point (the reported "no variance" bug).
        int distinct = 0;
        double[][] seen = new double[50][3];
        for (int i = 0; i < 50; i++) {
            seen[i] = USSFleetOrbit.offsetFor(i * 7919 + 1); // sparse sequential seeds, as random ints would be
        }
        outer: for (int i = 0; i < seen.length; i++) {
            for (int j = 0; j < i; j++) {
                if (Math.abs(seen[i][0] - seen[j][0]) + Math.abs(seen[i][1] - seen[j][1])
                    + Math.abs(seen[i][2] - seen[j][2]) > 1e-6) {
                    distinct++;
                    continue outer;
                }
            }
        }
        assertTrue(distinct >= 10, "only " + distinct + " distinct hover spots among 50 per-launch seeds");
    }

    // endregion

    // region planet position + dynamic hover (pass 7) — the ships must hover above the RENDERED planets, so this
    // math must agree with EOHRenderingUtils.renderUSSOrbits exactly

    @Test
    public void testPlanetPositionAtTimeZero() {
        // t = 0 → orbit angle 0 → the planet sits on +X at its orbit radius (star-center frame); the anchor sits
        // exactly 2 above the star block, so in anchor coordinates the point is (radius, -2, 0).
        // The radius is FLOAT math (exactly like renderUSSOrbits) — expectations use the same float expression.
        double[] p = USSFleetOrbit.planetAnchorPosition(5.0f, 1.0f, 0f, 0f, 1.0f, 0.0f);
        double radius = 0.2f + 5.0f + 0.2f * 1.0f; // the render formula: 0.2 + distance + 0.2*starSize
        assertEquals(radius, p[0], 1e-5, "orbit radius formula matches the star renderer");
        assertEquals(-2.0, p[1], 1e-9, "anchor sits 2 above the star block");
        assertEquals(0.0, p[2], 1e-9);
    }

    @Test
    public void testPlanetStaysOnItsOrbit() {
        // at any time the planet stays at its orbit radius from the star center (it orbits, it does not drift)
        for (float t = 0.0f; t < 1440.0f; t += 137.0f) {
            double[] p = USSFleetOrbit.planetAnchorPosition(6.5f, 1.3f, 20f, -12f, 1.4f, t);
            double dx = p[0];
            double dy = p[1] + 2.0;
            double dz = p[2];
            double radius = 0.2f + 6.5f + 0.2f * 1.4f; // float, like the renderer
            assertEquals(radius, Math.sqrt(dx * dx + dy * dy + dz * dz), 1e-5, "radius preserved at t=" + t);
        }
    }

    @Test
    public void testTiltedOrbitAtTimeZero() {
        // angle 0: (r,0,0) under Rz(zAngle) then Rx(xAngle) → (r·cp, r·sp·cs, r·sp·ss) — checked against explicit
        // values, not just the shortcut algebra. (radius = float, like the renderer)
        double[] p = USSFleetOrbit.planetAnchorPosition(5.0f, 1.0f, 30f, 90f, 1.0f, 0.0f);
        double radius = 0.2f + 5.0f + 0.2f * 1.0f;
        assertEquals(0.0, p[0], 1e-9);
        assertEquals(radius * Math.cos(Math.toRadians(30f)), p[1] + 2.0, 1e-5, "xAngle tilt about the X axis");
        assertEquals(radius * Math.sin(Math.toRadians(30f)), p[2], 1e-5);
    }

    @Test
    public void testPlanetPositionMatchesExplicitMatrixChain() {
        // Cross-check the trig shortcut against an INDEPENDENT explicit right-handed rotation composition
        // (Rx(xAngle) · Rz(zAngle) · Ry(orbitAngle) · (radius, 0, 0) — the same chain order renderUSSOrbits
        // applies to its matrix), across a spread of parameters and times.
        for (int i = 0; i < 48; i++) {
            float distance = 4.0f + 0.125f * i;
            float orbitSpeed = 0.5f + 0.03125f * i;
            float xAngle = (i % 7) * 7.0f - 21.0f; // -21..+21
            float zAngle = (i % 5) * 5.0f - 10.0f; // -10..+10
            float starSize = 0.4f + (i % 8) / 8.0f;
            float time = i * 137.0f;

            double[] actual = USSFleetOrbit.planetAnchorPosition(distance, orbitSpeed, xAngle, zAngle, starSize, time);

            float radius = 0.2f + distance + 0.2f * starSize; // float, exactly like the renderer
            double th = Math.toRadians((orbitSpeed * USSFleetOrbit.ORBIT_SPEED_SCALE * time) % 360f);
            double ph = Math.toRadians(zAngle);
            double ps = Math.toRadians(xAngle);
            double[] v = rotY(th, new double[] { radius, 0.0, 0.0 });
            v = rotZ(ph, v);
            v = rotX(ps, v);

            assertEquals(v[0], actual[0], 1e-5, "X (i=" + i + ")");
            assertEquals(v[1] + USSFleetOrbit.STAR_CENTER_Y, actual[1], 1e-5, "Y (i=" + i + ")");
            assertEquals(v[2], actual[2], 1e-5, "Z (i=" + i + ")");
        }
    }

    @Test
    public void testTargetBodyCenterFallsBackToTheStar() {
        double[] star = USSFleetOrbit.targetBodyCenter(-1, 5.0f, 1.0f, 0f, 0f, 1.0f, 12.0f);
        assertArrayEquals(new double[] { 0.0, -2.0, 0.0 }, star, "target -1 → the star center");
        double[] planet = USSFleetOrbit.targetBodyCenter(0, 5.0f, 1.0f, 0f, 0f, 1.0f, 0.0f);
        assertArrayEquals(
            USSFleetOrbit.planetAnchorPosition(5.0f, 1.0f, 0f, 0f, 1.0f, 0.0f),
            planet,
            "target 0 → the planet's live position");
    }

    @Test
    public void testHoverStaysInsideTheSpaceShell() {
        // Shell bound (user rule: everything renders inside the 12.95-block bubble around the star center):
        // outermost planet (distance 9.0) + the biggest star (tier 8 → size 1.4) + hover + planet half-height +
        // worst-case swarm spread. Pass 11: the vertical bob is gone, so it no longer enters the bound.
        float starSize = 0.4f + 8f / 8.0f;
        double radius = 0.2 + USSPlanets.MAX_DISTANCE + 0.2 * starSize;
        // Pass 8/9: the miner hovers 0.5 above the planet SURFACE → add the cube's half-height (≤ 0.5·MAX_SCALE —
        // the rendered planet is a unit cube of size spec.scale, surface at 0.5·scale). Worst case now
        // 9.48 + 0.5 + 0.375 + 2.0 = 12.355 < 12.95.
        double minerWorst = radius + USSConstants.HOVER_ABOVE_PLANET
            + 0.5 * USSPlanets.MAX_SCALE
            + USSFleetOrbit.MAX_RADIUS;
        assertTrue(minerWorst < 12.95, "worst-case miner hover " + minerWorst + " must stay inside 12.95");

        // Starlifter over the star: 2.5 + spread (the star center is 2 below the anchor — well inside).
        double starWorst = Math.abs(USSFleetOrbit.STAR_CENTER_Y) + USSConstants.HOVER_ABOVE_STAR
            + USSFleetOrbit.MAX_RADIUS;
        assertTrue(starWorst < 12.95, "worst-case Starlifter hover " + starWorst + " must stay inside 12.95");
        // and clears the largest star surface (radius ≤ 1.4)
        assertTrue(USSConstants.HOVER_ABOVE_STAR > 1.4, "Starlifter hover must clear the star surface");
    }

    private static double[] rotX(double a, double[] v) {
        double c = Math.cos(a);
        double s = Math.sin(a);
        return new double[] { v[0], c * v[1] - s * v[2], s * v[1] + c * v[2] };
    }

    private static double[] rotY(double a, double[] v) {
        double c = Math.cos(a);
        double s = Math.sin(a);
        return new double[] { c * v[0] + s * v[2], v[1], -s * v[0] + c * v[2] };
    }

    private static double[] rotZ(double a, double[] v) {
        double c = Math.cos(a);
        double s = Math.sin(a);
        return new double[] { c * v[0] - s * v[1], s * v[0] + c * v[1], v[2] };
    }

    // endregion
}
