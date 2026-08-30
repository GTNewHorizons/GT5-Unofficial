package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the USS planet tint: every registered planet definition resolves to an
 * opaque, deterministic color from its ore materials, and the catalog's planets are visually distinguishable (not
 * one shared default).
 */
public class USSPlanetColorTest {

    @BeforeEach
    public void setUp() {
        USSPlanetRegistry.clear();
        USSPlanetCatalog.resetForTests();
        USSPlanetCatalog.registerAll();
    }

    @Test
    public void testEveryPlanetHasOpaqueColor() {
        for (USSPlanetDefinition def : USSPlanetRegistry.all()) {
            int color = USSPlanetColor.colorFor(def);
            assertEquals(0xFF000000, color & 0xFF000000, def.getId() + " color must be opaque");
        }
    }

    @Test
    public void testColorIsDeterministic() {
        for (USSPlanetDefinition def : USSPlanetRegistry.all()) {
            assertEquals(
                USSPlanetColor.colorFor(def),
                USSPlanetColor.colorFor(def),
                def.getId() + " color must be stable");
        }
    }

    @Test
    public void testPlanetsAreVisuallyDistinguishable() {
        // The planet catalog exists so "different planets determine what can be mined" is visible too: the
        // catalog's tinted planets must not all collapse to one color.
        List<USSPlanetDefinition> defs = USSPlanetRegistry.all();
        int distinct = 0;
        int[] colors = new int[defs.size()];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = USSPlanetColor.colorFor(defs.get(i));
        }
        outer: for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < i; j++) {
                if (colors[i] != colors[j]) {
                    distinct++;
                    continue outer;
                }
            }
        }
        assertTrue(distinct >= 4, "only " + distinct + " distinct planet colors among " + defs.size() + " planets");
    }

    @Test
    public void testNullDefinitionFallsBackToWhite() {
        assertEquals(0xFFFFFFFF, USSPlanetColor.colorFor((USSPlanetDefinition) null));
    }
}
