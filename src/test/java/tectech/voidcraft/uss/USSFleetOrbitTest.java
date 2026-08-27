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
            // Pass 30: the orbit angle is the RADIUS law (0.3·time/radius) — the random orbitSpeed no longer
            // drives it (it is only passed through for signature stability).
            double th = Math.toRadians((USSFleetOrbit.ORBIT_DEG_PER_TICK_PER_BLOCK * time / radius) % 360f);
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
    public void testPlanetAndStarLifterBoundsRespectTheVoidcraftShell() {
        // Pass 13/14 (user: "the planets are still very close to the star — orbital distance randomized between
        // 3 blocks from the star all the way to the edges of the system"; then "ships exiting the dome isn't
        // good — make the distance from the edge 4 blocks"):
        // MAX_DISTANCE = USSConstants.SPACE_SHELL_RADIUS − 4 BY CONSTRUCTION, so the planet center always stays
        // inside the Voidcraft dome (27.1) with the user's 4-block margin. (The legacy 12.95 bound is gone —
        // the dome is per-machine; the legacy EoH machine keeps its own 12.95 default.)
        float starSize = 0.4f + 8f / 8.0f;
        double planetWorstCenter = 0.2 + USSPlanets.MAX_DISTANCE + 0.2 * starSize;
        assertTrue(
            USSPlanets.MAX_DISTANCE <= USSConstants.SPACE_SHELL_RADIUS - 4.0 + 1e-6,
            "outermost orbit " + USSPlanets.MAX_DISTANCE
                + " must be 4 blocks inside the dome "
                + USSConstants.SPACE_SHELL_RADIUS);
        assertTrue(
            planetWorstCenter < USSConstants.SPACE_SHELL_RADIUS,
            "worst-case planet center " + planetWorstCenter
                + " must stay inside the dome "
                + USSConstants.SPACE_SHELL_RADIUS);

        // The 4-block margin covers hover 0.5 + cube half ≤ 0.33 (MAX_SCALE 0.66 / 2) + spread 2.0 = 2.83 < 4.0 —
        // even the outermost hovering ship stays INSIDE the dome (≈ 23.68 + 2.83 = 26.51 vs 27.1), so ships
        // render inside the bubble.
        double minerWorst = planetWorstCenter + USSConstants.HOVER_ABOVE_PLANET
            + 0.5 * USSPlanets.MAX_SCALE
            + USSFleetOrbit.MAX_RADIUS;
        assertTrue(
            minerWorst < USSConstants.SPACE_SHELL_RADIUS,
            "worst-case miner hover " + minerWorst + " must stay inside the dome " + USSConstants.SPACE_SHELL_RADIUS);
        // Starlifter over the star: 2.5 + spread (the star center is 2 below the anchor — well inside).
        double starWorst = Math.abs(USSFleetOrbit.STAR_CENTER_Y) + USSConstants.HOVER_ABOVE_STAR
            + USSFleetOrbit.MAX_RADIUS;
        assertTrue(
            starWorst < USSConstants.SPACE_SHELL_RADIUS,
            "worst-case Starlifter hover " + starWorst
                + " must stay inside the dome "
                + USSConstants.SPACE_SHELL_RADIUS);
        // and clears the largest star surface (radius ≤ 1.9)
        assertTrue(USSConstants.HOVER_ABOVE_STAR > 1.9, "Starlifter hover must clear the star surface");
    }

    // region stateful-position pass (USSPosition) — the "distance within the solar system" model

    @Test
    public void testStarPositionIsTheStarCenter() {
        USSPosition star = USSFleetOrbit.starPosition();
        assertEquals(0.0, star.x(), 1e-9);
        assertEquals(USSFleetOrbit.STAR_CENTER_Y, star.y(), 1e-9, "star center at (0,-2,0) in anchor coords");
        assertEquals(0.0, star.z(), 1e-9);
        assertEquals(USSPosition.starCenter(), star, "starPosition == USSPosition.starCenter");
    }

    @Test
    public void testPlanetPositionMatchesAnchorMath() {
        // planetPosition is the USSPosition wrapper over planetAnchorPosition — they must agree exactly.
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(null, 6.0, 1.4, 1.3, 0.5, 20.0, -12.0, false, -1);
        for (float t = 0.0f; t < 300.0f; t += 97.0f) {
            USSPosition p = USSFleetOrbit.planetPosition(planet, 1.0f, t);
            double[] a = USSFleetOrbit.planetAnchorPosition(6.0f, 1.3f, 20f, -12f, 1.0f, t);
            assertEquals(a[0], p.x(), 1e-9, "X at t=" + t);
            assertEquals(a[1], p.y(), 1e-9, "Y at t=" + t);
            assertEquals(a[2], p.z(), 1e-9, "Z at t=" + t);
        }
    }

    @Test
    public void testShellPointIsDeterministicAndOnTheSphere() {
        USSPosition center = USSPosition.of(3.0, -2.0, 5.0);
        double radius = 1.5;
        for (int i = 0; i < 200; i++) {
            long seed = i * 7919 + 1;
            USSPosition a = USSFleetOrbit.shellPoint(center, radius, seed);
            USSPosition b = USSFleetOrbit.shellPoint(center, radius, seed);
            assertEquals(a, b, "same seed → same point (bit-identical, server/client agree)");
            assertEquals(
                radius,
                center.distanceTo(a),
                1e-9,
                "point is EXACTLY radius from the center (seed=" + seed + ")");
        }
    }

    @Test
    public void testShellPointSpreadsAroundTheSphere() {
        // "not only above it, but on any side" — a fleet of shells must occupy points on BOTH the +Y and -Y hemispheres
        // (and off-axis), not stack on one pole.
        USSPosition center = USSPosition.zero();
        boolean sawAbove = false;
        boolean sawBelow = false;
        boolean sawOffAxis = false;
        for (int i = 0; i < 64; i++) {
            USSPosition p = USSFleetOrbit.shellPoint(center, 1.0, i * 7919 + 1);
            if (p.y() > 0.1) {
                sawAbove = true;
            }
            if (p.y() < -0.1) {
                sawBelow = true;
            }
            if (Math.abs(p.x()) > 0.1 || Math.abs(p.z()) > 0.1) {
                sawOffAxis = true;
            }
        }
        assertTrue(sawAbove, "some shell points sit above the center (the +Y hemisphere is used)");
        assertTrue(sawBelow, "some shell points sit below the center (the -Y hemisphere is used)");
        assertTrue(sawOffAxis, "some shell points are off the vertical axis (the equator is used)");
    }

    @Test
    public void testShellPointRadiusZeroReturnsTheCenter() {
        USSPosition center = USSPosition.of(1.0, 2.0, 3.0);
        assertEquals(center, USSFleetOrbit.shellPoint(center, 0.0, 42), "radius 0 → the center itself");
        assertEquals(center, USSFleetOrbit.shellPoint(center, -1.0, 42), "negative radius → the center itself");
        // null center defaults to the origin — the point is radius from the origin.
        assertEquals(
            1.0,
            USSFleetOrbit.shellPoint(null, 1.0, 42)
                .length(),
            1e-9,
            "null center → the origin");
    }

    @Test
    public void testNudgeIsDeterministicAndBounded() {
        USSPosition center = USSPosition.of(2.0, -2.0, -3.0);
        double maxRadius = 1.2;
        for (int i = 0; i < 200; i++) {
            long seed = i * 7919 + 1;
            USSPosition a = USSFleetOrbit.nudge(center, maxRadius, seed);
            USSPosition b = USSFleetOrbit.nudge(center, maxRadius, seed);
            assertEquals(a, b, "same seed → same nudge (stable cloud, no flicker)");
            assertTrue(
                center.distanceTo(a) <= maxRadius + 1e-9,
                "nudge stays within maxRadius (seed=" + seed + ", got " + center.distanceTo(a) + ")");
        }
    }

    @Test
    public void testNudgeCreatesCloudsNotOverlaps() {
        // The user's "clouds of Voidcraft instead of overlapping them all": a swarm targeting one ship must occupy
        // DISTINCT spots (at least 10 distinct among 50), all within the nudge bound.
        USSPosition center = USSPosition.zero();
        int distinct = 0;
        USSPosition[] seen = new USSPosition[50];
        for (int i = 0; i < 50; i++) {
            seen[i] = USSFleetOrbit.nudge(center, USSFleetOrbit.MAX_RADIUS, i * 7919 + 1);
        }
        outer: for (int i = 0; i < seen.length; i++) {
            for (int j = 0; j < i; j++) {
                if (seen[i].distanceTo(seen[j]) > 1e-6) {
                    distinct++;
                    continue outer;
                }
            }
        }
        assertTrue(distinct >= 10, "only " + distinct + " distinct cloud spots among 50 targeting ships");
    }

    @Test
    public void testNudgeRadiusZeroReturnsTheCenter() {
        USSPosition center = USSPosition.of(-1.0, 0.5, 2.0);
        assertEquals(center, USSFleetOrbit.nudge(center, 0.0, 42), "maxRadius 0 → the center itself");
        assertEquals(center, USSFleetOrbit.nudge(center, -5.0, 42), "negative maxRadius → the center itself");
    }

    // endregion

    // region gateway render pass (the dome-edge point in the gateway's direction)

    @Test
    public void testGatewayEdgeSitsOnTheDomeSurface() {
        double[][] gates = { { 16, 0, 0 }, { -20, 5, 0 }, { 0, 0, 32 }, { 8, -14, -9 }, { 0.5, 3.25, -1.5 } };
        for (double[] gw : gates) {
            double[] edge = USSFleetOrbit.gatewayEdgePoint(gw);
            double dx = edge[0];
            double dy = edge[1] - USSFleetOrbit.STAR_CENTER_Y;
            double dz = edge[2];
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            assertEquals(
                USSConstants.SPACE_SHELL_RADIUS,
                dist,
                1e-9,
                "the gateway edge must sit exactly on the dome surface (gateway " + gw[0]
                    + ","
                    + gw[1]
                    + ","
                    + gw[2]
                    + ")");
        }
    }

    @Test
    public void testGatewayEdgeKeepsTheGatewayDirection() {
        // The edge point is the dome point CLOSEST to the gateway: it stays on the star-center → gateway ray,
        // only the radius changes.
        double[] gw = { 24.0, 3.0, -11.0 };
        double[] edge = USSFleetOrbit.gatewayEdgePoint(gw);
        double ax = gw[0], ay = gw[1] - USSFleetOrbit.STAR_CENTER_Y, az = gw[2];
        double bx = edge[0], by = edge[1] - USSFleetOrbit.STAR_CENTER_Y, bz = edge[2];
        double cross = Math
            .sqrt(Math.pow(ay * bz - az * by, 2) + Math.pow(az * bx - ax * bz, 2) + Math.pow(ax * by - ay * bx, 2));
        assertEquals(0.0, cross, 1e-9, "the edge point must stay on the star-center → gateway ray");
        assertTrue(
            ax * bx + ay * by + az * bz > 0.0,
            "the edge point must be on the SAME side of the star as the gateway");
    }

    @Test
    public void testGatewayEdgeIsIdempotentOnTheSurface() {
        double[] once = USSFleetOrbit.gatewayEdgePoint(new double[] { 16.0, 1.0, 4.0 });
        assertArrayEquals(once, USSFleetOrbit.gatewayEdgePoint(once), 1e-9, "projecting a surface point keeps it");
    }

    @Test
    public void testGatewayEdgeDegenerateAtStarCenter() {
        double[] gw = { 0.0, USSFleetOrbit.STAR_CENTER_Y, 0.0 };
        assertArrayEquals(gw, USSFleetOrbit.gatewayEdgePoint(gw), 1e-12, "no direction → the gateway itself");
    }

    @Test
    public void testGatewayEdgeSanity() {
        // 16 blocks out at +X (2 above the star center): the edge point is further along the same ray, at
        // exactly the dome radius — this is where the ships now spawn/return.
        double[] edge = USSFleetOrbit.gatewayEdgePoint(new double[] { 16.0, 0.0, 0.0 });
        double len = Math.sqrt(16.0 * 16.0 + 2.0 * 2.0);
        double f = USSConstants.SPACE_SHELL_RADIUS / len;
        assertArrayEquals(new double[] { 16.0 * f, USSFleetOrbit.STAR_CENTER_Y + 2.0 * f, 0.0 }, edge, 1e-12);
    }

    // endregion

    // region the station equatorial band (the Voidbase hover law) — a planet-anchored base sits within ±30° of
    // the planet's orbital plane, on the shell at the ship hover radius, seeded by the planet index

    @Test
    public void testBandPointSitsExactlyOnTheShell() {
        for (int seed = 0; seed < 200; seed++) {
            float x = (seed % 41) - 20f;
            float z = (seed % 29) - 14f;
            USSPosition c = USSPosition.of(3.0, -2.0, 7.0);
            USSPosition p = USSFleetOrbit.orbitalBandPoint(c, 1.5, seed, x, z);
            double d = c.distanceTo(p);
            assertEquals(1.5, d, 1e-9, "seed " + seed + ": the band point must be exactly on the shell");
        }
    }

    @Test
    public void testBandPointStaysWithinThirtyDegreesOfTheOrbitalPlane() {
        // The orbital plane through the center has normal Rx(x)·Rz(z)·(0,1,0) (the same chain the orbit law
        // uses); a point within 30° of the plane has |dot(dir, n)| ≤ cos(60°) = 0.5.
        for (int seed = 0; seed < 500; seed++) {
            float x = (seed % 41) - 20f;
            float z = (seed % 29) - 14f;
            double[] n = rotX(Math.toRadians(x), rotZ(Math.toRadians(z), new double[] { 0.0, 1.0, 0.0 }));
            USSPosition c = USSPosition.of(1.0, -2.0, 2.0);
            USSPosition p = USSFleetOrbit.orbitalBandPoint(c, 1.0, seed, x, z);
            double[] dir = new double[] { p.x() - c.x(), p.y() - c.y(), p.z() - c.z() };
            double dot = dir[0] * n[0] + dir[1] * n[1] + dir[2] * n[2];
            assertTrue(
                Math.abs(dot) <= 0.5 + 1e-9,
                "seed " + seed + ": elevation " + Math.toDegrees(Math.asin(dot)) + "° exceeds the 30° band");
        }
    }

    @Test
    public void testBandPointIsDeterministicPerPlanetIndex() {
        USSPosition c = USSPosition.of(0.0, -2.0, 0.0);
        USSPosition a = USSFleetOrbit.orbitalBandPoint(c, 1.0, 3, 12f, -5f);
        USSPosition b = USSFleetOrbit.orbitalBandPoint(c, 1.0, 3, 12f, -5f);
        assertEquals(a, b, "same planet index → same band point (server and client agree)");
        USSPosition other = USSFleetOrbit.orbitalBandPoint(c, 1.0, 4, 12f, -5f);
        assertTrue(
            c.distanceTo(a) > 0 && a.distanceTo(other) > 1e-9,
            "different planet indices must not share the same band point");
    }

    @Test
    public void testBandPointZeroTiltBandAroundXZ() {
        // No tilt: the orbital plane is the XZ plane through the center — the band point stays within ±30° of
        // it (|dy| ≤ radius·sin(30°)).
        for (int seed = 0; seed < 100; seed++) {
            USSPosition c = USSPosition.of(5.0, -2.0, 0.0);
            USSPosition p = USSFleetOrbit.orbitalBandPoint(c, 2.0, seed, 0f, 0f);
            assertEquals(2.0, c.distanceTo(p), 1e-9);
            assertTrue(
                Math.abs(p.y() - c.y()) <= 2.0 * Math.sin(Math.toRadians(30.0)) + 1e-9,
                "seed " + seed + ": dy " + (p.y() - c.y()) + " outside the zero-tilt band");
        }
    }

    @Test
    public void testBandPointTracksThePlanetAcrossOrbitTimes() {
        // The band point is anchored to the planet center: as the planet orbits, the point follows (always at
        // the hover radius from the LIVE center, always inside the band).
        double[] n = rotX(Math.toRadians(10.0), rotZ(Math.toRadians(-7.0), new double[] { 0.0, 1.0, 0.0 }));
        USSPosition c0 = USSFleetOrbit.planetPosition(planet(5.0f, 10.0f, -7.0f), 1.0f, 0.0f);
        USSPosition c1 = USSFleetOrbit.planetPosition(planet(5.0f, 10.0f, -7.0f), 1.0f, 2000.0f);
        USSPosition p0 = USSFleetOrbit.orbitalBandPoint(c0, 1.0, 0, 10.0f, -7.0f);
        USSPosition p1 = USSFleetOrbit.orbitalBandPoint(c1, 1.0, 0, 10.0f, -7.0f);
        assertEquals(1.0, c0.distanceTo(p0), 1e-9);
        assertEquals(1.0, c1.distanceTo(p1), 1e-9);
        for (int i = 0; i < 2; i++) {
            USSPosition c = i == 0 ? c0 : c1;
            USSPosition p = i == 0 ? p0 : p1;
            double[] dir = new double[] { p.x() - c.x(), p.y() - c.y(), p.z() - c.z() };
            double dot = dir[0] * n[0] + dir[1] * n[1] + dir[2] * n[2];
            assertTrue(Math.abs(dot) <= 0.5 + 1e-9, "the band point follows the orbital plane");
        }
        assertTrue(c0.distanceTo(c1) > 0.1, "the planet moved (the two samples are distinct)");
    }

    @Test
    public void testBandPointRadiusZeroIsTheCenter() {
        USSPosition c = USSPosition.of(1.0, 2.0, 3.0);
        assertEquals(c, USSFleetOrbit.orbitalBandPoint(c, 0.0, 5, 10f, 10f));
    }

    // endregion

    private static USSPlanets.USSPlanet planet(float distance, float xAngle, float zAngle) {
        // definition unused by the orbit math (only distance / speed / tilts matter)
        return new USSPlanets.USSPlanet(null, distance, 0.5, 1.0, 1.0, xAngle, zAngle, false, -1);
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
