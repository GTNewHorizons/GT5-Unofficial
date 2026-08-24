package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the USS planet tint (Phase 4 pass 5.1): every planet type resolves to an opaque,
 * deterministic color from its ore materials, and the 12 types are visually distinguishable (not one shared
 * default).
 */
public class USSPlanetColorTest {

    @Test
    public void testEveryPlanetTypeHasOpaqueColor() {
        for (USSPlanetType type : USSPlanetType.all()) {
            int color = USSPlanetColor.colorFor(type);
            assertEquals(0xFF000000, color & 0xFF000000, type + " color must be opaque");
        }
    }

    @Test
    public void testColorIsDeterministic() {
        for (USSPlanetType type : USSPlanetType.all()) {
            assertEquals(USSPlanetColor.colorFor(type), USSPlanetColor.colorFor(type), type + " color must be stable");
        }
    }

    @Test
    public void testTypesAreVisuallyDistinguishable() {
        // The planet catalog exists so "different planet types determine what can be mined" is visible too: the
        // 12 tinted spheres must not all collapse to one color.
        int distinct = 0;
        int[] colors = new int[USSPlanetType.all()
            .size()];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = USSPlanetColor.colorFor(
                USSPlanetType.all()
                    .get(i));
        }
        outer: for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < i; j++) {
                if (colors[i] != colors[j]) {
                    distinct++;
                    continue outer;
                }
            }
        }
        assertTrue(distinct >= 4, "only " + distinct + " distinct planet colors among 12 types");
    }

    @Test
    public void testNullTypeFallsBackToWhite() {
        assertEquals(0xFFFFFFFF, USSPlanetColor.colorFor(null));
    }
}
