package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for spacetime-ripple field generation ({@link USSRipples}) — the Explorer mechanic: every solar system
 * carries a 7×7×7 point grid (343 points), the ripple COUNT is sampled inclusively from the star's
 * {@code spacetimeRipples} range, and the ripple POINTS are chosen deterministically (seeded). The points sit on 3
 * evenly-spaced shells around the star center.
 *
 * <p>
 * Bare-JVM: no world or renderer — the positions are pure fleet-anchor doubles.
 */
public class USSRipplesTest {

    @BeforeEach
    public void setUp() {
        USSStarRegistry.clear();
        USSStarCatalog.resetForTests();
        USSStarCatalog.registerAll();
    }

    @Test
    public void testFieldHasTheFullGrid() {
        USSRippleField field = USSRipples.generate(USSStarType.MAIN_SEQUENCE, 123L);
        assertEquals(USSRipples.GRID_SIZE, field.getGridSize(), "grid edge is 7");
        assertEquals(USSRipples.POINT_COUNT, field.size(), "a 7×7×7 field has 343 points");
        assertEquals(343, USSRipples.POINT_COUNT, "343 = 7³");
    }

    @Test
    public void testRippleCountWithinStarRange() {
        for (USSStarType starType : USSStarType.values()) {
            USSStarDefinition star = USSStarRegistry.byType(starType);
            int min = star != null ? star.getRippleMin() : USSStarDefinition.MIN_RIPPLES;
            int max = star != null ? star.getRippleMax() : USSStarDefinition.MAX_RIPPLES;
            USSRippleField field = USSRipples.generate(starType, 456L);
            assertTrue(
                field.getRippleCount() >= min && field.getRippleCount() <= max,
                starType + " — ripple count " + field.getRippleCount() + " within " + min + "–" + max);
        }
    }

    @Test
    public void testRippleCountVariesWithSeed() {
        // A sweep of seeds must actually produce more than one ripple count (the count is a real draw, not a constant).
        Set<Integer> counts = new HashSet<>();
        for (long seed = 1; seed <= 64; seed++) {
            counts.add(
                USSRipples.generate(USSStarType.MAIN_SEQUENCE, seed)
                    .getRippleCount());
        }
        assertTrue(counts.size() > 1, "a sweep of 64 seeds must produce more than one ripple count: " + counts);
    }

    @Test
    public void testGenerationIsDeterministic() {
        for (USSStarType starType : USSStarType.values()) {
            USSRippleField a = USSRipples.generate(starType, 999L);
            USSRippleField b = USSRipples.generate(starType, 999L);
            assertEquals(a.rippleIndices(), b.rippleIndices(), starType + " — same (star, seed) → same ripple set");
            for (int i = 0; i < a.size(); i++) {
                assertEquals(
                    a.pointAt(i)
                        .getPosition(),
                    b.pointAt(i)
                        .getPosition(),
                    starType + " point " + i + " — position deterministic");
            }
        }
    }

    @Test
    public void testDifferentSeedsGiveDifferentRippleSets() {
        boolean anyDifference = false;
        for (long seed = 1; seed <= 16; seed++) {
            USSRippleField a = USSRipples.generate(USSStarType.MAIN_SEQUENCE, seed);
            USSRippleField b = USSRipples.generate(USSStarType.MAIN_SEQUENCE, seed + 100_000L);
            if (!a.rippleIndices()
                .equals(b.rippleIndices())) {
                anyDifference = true;
                break;
            }
        }
        assertTrue(anyDifference, "identical ripple sets for different seeds is a coincidence, not a design");
    }

    @Test
    public void testRippleSetSizeMatchesCount() {
        for (long seed = 1; seed <= 16; seed++) {
            USSRippleField field = USSRipples.generate(USSStarType.WHITE_DWARF, seed);
            assertEquals(
                field.getRippleCount(),
                field.rippleIndices()
                    .size(),
                "seed " + seed + " — the ripple index list has exactly rippleCount entries");
            // every listed index really is a ripple, and every ripple is listed
            for (int idx : field.rippleIndices()) {
                assertTrue(field.isRipple(idx), "seed " + seed + " index " + idx + " is a ripple");
            }
        }
    }

    @Test
    public void testEveryPointSitsOnOneOfTheThreeShells() {
        USSPosition center = USSPosition.starCenter();
        Set<Double> radii = new HashSet<>();
        USSRippleField field = USSRipples.generate(USSStarType.MAIN_SEQUENCE, 42L);
        for (int i = 0; i < field.size(); i++) {
            double r = field.pointAt(i)
                .getPosition()
                .distanceTo(center);
            radii.add(round(r));
        }
        // Every point lands on one of the three evenly-spaced shells — exactly those three radii.
        assertEquals(3, radii.size(), "exactly three distinct shell radii, got " + radii);
        assertTrue(
            radii.contains(round(USSRipples.shellRadius(0))),
            "inner shell (" + USSRipples.shellRadius(0) + ") present");
        assertTrue(
            radii.contains(round(USSRipples.shellRadius(1))),
            "middle shell (" + USSRipples.shellRadius(1) + ") present");
        assertTrue(
            radii.contains(round(USSRipples.shellRadius(2))),
            "outer shell (" + USSRipples.shellRadius(2) + ") present");
    }

    @Test
    public void testShellRadiiAreEvenlySpaced() {
        double r0 = USSRipples.shellRadius(0);
        double r1 = USSRipples.shellRadius(1);
        double r2 = USSRipples.shellRadius(2);
        assertEquals(r0 + (r1 - r0), r1, 1e-9, "shell 1");
        assertEquals(r1 + (r2 - r1), r2, 1e-9, "shell 2");
        double gap = r1 - r0;
        assertEquals(gap, r2 - r1, 1e-9, "even spacing between shells");
        assertTrue(r0 > 0 && r0 < r1 && r1 < r2, "radii strictly increasing: " + r0 + "," + r1 + "," + r2);
    }

    @Test
    public void testShellRadiusClampsToValidRange() {
        assertEquals(USSRipples.shellRadius(0), USSRipples.shellRadius(-5), 1e-9, "below-range clamps to shell 0");
        assertEquals(USSRipples.shellRadius(2), USSRipples.shellRadius(99), 1e-9, "above-range clamps to shell 2");
    }

    @Test
    public void testNullStarTypeFallsBackToMainSequence() {
        USSRippleField nullType = USSRipples.generate(null, 42L);
        USSRippleField mainSequence = USSRipples.generate(USSStarType.MAIN_SEQUENCE, 42L);
        assertEquals(mainSequence.rippleIndices(), nullType.rippleIndices(), "null star type → main sequence field");
        assertNotEquals(
            USSRipples.generate(USSStarType.SUPERMASSIVE, 42L)
                .rippleIndices(),
            mainSequence.rippleIndices(),
            "different star types draw different ripple ranges (same seed)");
    }

    private static double round(double v) {
        return Math.round(v * 1e6) / 1e6;
    }
}
