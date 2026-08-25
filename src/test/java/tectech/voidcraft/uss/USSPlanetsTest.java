package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;

/**
 * Unit tests for deterministic planet generation ({@link USSPlanets}) — the registration-based mechanics pass: the
 * system's planets are a pure function of (star type, seed), the count from the star's planet range, the types from
 * the star's registered planet pool, the size from each planet's size range, and the orbital parameters inside the
 * legacy EoH ranges (so the shared orbit renderer can draw them) with 4 blocks of margin from the shell edge.
 *
 * <p>
 * Bare-JVM: no blocks or world involved — the hologram block resolution happens at the render TE boundary.
 */
public class USSPlanetsTest {

    @BeforeEach
    public void setUp() {
        USSPlanetRegistry.clear();
        USSStarRegistry.clear();
        USSPlanetCatalog.resetForTests();
        USSStarCatalog.resetForTests();
        USSPlanetCatalog.registerAll();
        USSStarCatalog.registerAll();
    }

    @Test
    public void testSystemHasAFewPlanets() {
        // The count is RANDOM but bounded by the star's planet range — and a wide seed sweep must actually produce
        // more than one count.
        for (USSStarType starType : USSStarType.values()) {
            USSStarDefinition star = USSStarRegistry.byType(starType);
            int min = star != null ? star.getPlanetMin() : USSPlanets.MIN_PLANETS_PER_SYSTEM;
            int max = star != null ? star.getPlanetMax() : USSPlanets.MAX_PLANETS_PER_SYSTEM;
            List<USSPlanets.USSPlanet> system = USSPlanets.generate(starType, 123L);
            assertTrue(
                system.size() >= min && system.size() <= max,
                starType + " — count within " + min + "–" + max + ", got " + system.size());
            for (USSPlanets.USSPlanet planet : system) {
                assertTrue(
                    planet.definition.allowsStarType(starType),
                    starType + " — every planet allows the star type (from the star's own pool)");
            }
        }
        Set<Integer> counts = new HashSet<>();
        for (long seed = 1; seed <= 64; seed++) {
            counts.add(
                USSPlanets.generate(USSStarType.MAIN_SEQUENCE, seed)
                    .size());
        }
        assertTrue(counts.size() > 1, "a sweep of 64 seeds must produce more than one planet count: " + counts);
    }

    @Test
    public void testSystemPlanetsHaveDistinctDefinitions() {
        // Distinct worlds until the star's registered pool is exhausted — then it wraps (duplicates unavoidable,
        // but no definition may repeat before the first cycle is complete).
        for (USSStarType starType : USSStarType.values()) {
            final int poolSize = USSPlanetRegistry.pool(starType)
                .size();
            for (long seed = 1; seed <= 16; seed++) {
                List<USSPlanets.USSPlanet> system = USSPlanets.generate(starType, seed);
                Set<USSPlanetDefinition> defs = new HashSet<>();
                for (USSPlanets.USSPlanet planet : system) {
                    defs.add(planet.definition);
                }
                assertEquals(
                    Math.min(system.size(), poolSize),
                    defs.size(),
                    starType + " seed "
                        + seed
                        + " — distinct definitions up to the pool size ("
                        + poolSize
                        + "), then wraps; count was "
                        + system.size());
                // stronger: the FIRST min(count, pool) planets are pairwise distinct (no early duplicates)
                final int firstCycle = Math.min(system.size(), poolSize);
                for (int i = 0; i < firstCycle; i++) {
                    for (int j = i + 1; j < firstCycle; j++) {
                        assertNotEquals(
                            system.get(i).definition,
                            system.get(j).definition,
                            starType + " seed "
                                + seed
                                + " — planets "
                                + i
                                + " and "
                                + j
                                + " (first cycle) must differ");
                    }
                }
            }
        }
    }

    @Test
    public void testGenerationIsDeterministic() {
        for (USSStarType starType : USSStarType.values()) {
            List<USSPlanets.USSPlanet> a = USSPlanets.generate(starType, 999L);
            List<USSPlanets.USSPlanet> b = USSPlanets.generate(starType, 999L);
            assertEquals(a, b, starType + " — same (star type, seed) → identical system");
        }
    }

    @Test
    public void testDifferentSeedsGiveDifferentSystems() {
        boolean anyDifference = false;
        for (long seed = 1; seed <= 16; seed++) {
            List<USSPlanets.USSPlanet> a = USSPlanets.generate(USSStarType.MAIN_SEQUENCE, seed);
            List<USSPlanets.USSPlanet> b = USSPlanets.generate(USSStarType.MAIN_SEQUENCE, seed + 100_000L);
            if (!a.equals(b)) {
                anyDifference = true;
                break;
            }
        }
        assertTrue(anyDifference, "near-identical planets are an astronomical coincidence, not a design");
    }

    @Test
    public void testDifferentStarTypesGiveDifferentPlanetFamilies() {
        for (long seed = 1; seed <= 4; seed++) {
            Set<String> main = new HashSet<>();
            Set<String> white = new HashSet<>();
            Set<String> supermassive = new HashSet<>();
            for (USSPlanets.USSPlanet planet : USSPlanets.generate(USSStarType.MAIN_SEQUENCE, seed)) {
                main.add(planet.definition.getId());
            }
            for (USSPlanets.USSPlanet planet : USSPlanets.generate(USSStarType.WHITE_DWARF, seed)) {
                white.add(planet.definition.getId());
            }
            for (USSPlanets.USSPlanet planet : USSPlanets.generate(USSStarType.SUPERMASSIVE, seed)) {
                supermassive.add(planet.definition.getId());
            }
            assertTrue(isDisjoint(main, white), "seed " + seed + " — main sequence vs white dwarf families");
            assertTrue(isDisjoint(main, supermassive), "seed " + seed + " — main sequence vs supermassive families");
            assertTrue(isDisjoint(white, supermassive), "seed " + seed + " — white dwarf vs supermassive families");
        }
    }

    private static boolean isDisjoint(Set<String> a, Set<String> b) {
        for (String id : a) {
            if (b.contains(id)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testOrbitalParametersStayInRange() {
        for (long seed = 1; seed <= 32; seed++) {
            List<USSPlanets.USSPlanet> system = USSPlanets.generate(USSStarType.WHITE_DWARF, seed);
            double band = (USSPlanets.MAX_DISTANCE - USSPlanets.MIN_DISTANCE) / system.size();
            for (int i = 0; i < system.size(); i++) {
                USSPlanets.USSPlanet planet = system.get(i);
                // distance: inside its orbital band (bands are disjoint, inner → outer), clear of the star (≤ 1.4)
                // and 4 blocks inside the shell edge (MAX_DISTANCE = dome − 4 by construction).
                assertTrue(
                    planet.distance >= USSPlanets.MIN_DISTANCE + band * i
                        && planet.distance <= USSPlanets.MIN_DISTANCE + band * (i + 1),
                    "seed " + seed + " planet " + i + " distance " + planet.distance + " in band " + i);
                assertTrue(planet.distance > 1.4, "seed " + seed + " — clear of the star surface");
                assertTrue(
                    planet.distance <= USSConstants.SPACE_SHELL_RADIUS - 4.0 + 1e-6,
                    "seed " + seed + " — inside the shell edge with the 4-block margin");
                // scale: within the PLANET DEFINITION's size range (sampled from it).
                assertTrue(
                    planet.scale >= planet.definition.getSizeMin() - 1e-6
                        && planet.scale <= planet.definition.getSizeMax() + 1e-6,
                    "seed " + seed
                        + " — scale in the planet's size range ["
                        + planet.definition.getSizeMin()
                        + ","
                        + planet.definition.getSizeMax()
                        + "], got "
                        + planet.scale);
                assertTrue(
                    planet.orbitSpeed >= USSPlanets.MIN_SPEED && planet.orbitSpeed <= USSPlanets.MAX_SPEED,
                    "seed " + seed + " — orbit speed in range");
                assertTrue(
                    planet.rotationSpeed >= USSPlanets.MIN_SPEED && planet.rotationSpeed <= USSPlanets.MAX_SPEED,
                    "seed " + seed + " — spin speed in range");
                // coplanar orbits — each tilt component stays within the small 0–5° inclination.
                assertTrue(
                    Math.abs(planet.xAngle) <= USSPlanets.MAX_INCLINATION_DEG
                        && Math.abs(planet.zAngle) <= USSPlanets.MAX_INCLINATION_DEG,
                    "seed " + seed + " — orbit inclinations in range (0–5°)");
            }
        }
    }

    @Test
    public void testMaterialsOfCollectsThePlanetsOres() {
        // Two distinct planets → the union of their ores (first-seen order).
        USSPlanetDefinition rocky = USSPlanetRegistry.get("rocky_world");
        USSPlanetDefinition ocean = USSPlanetRegistry.get("ocean_world");
        assertNotNull(rocky, "rocky_world registered");
        assertNotNull(ocean, "ocean_world registered");

        List<USSPlanets.USSPlanet> system = new java.util.ArrayList<>();
        system.add(new USSPlanets.USSPlanet(rocky, 5.0, 0.5, 1.0, 1.0, 0, 0));
        system.add(new USSPlanets.USSPlanet(ocean, 7.0, 0.5, 1.0, 1.0, 0, 0));

        List<Materials> materials = USSPlanets.materialsOf(system);
        int expected = rocky.getOres()
            .size()
            + ocean.getOres()
                .size();
        assertEquals(expected, materials.size(), "two distinct planets → the union of their ores");
        assertEquals(
            rocky.getOres()
                .get(0)
                .getOreType(),
            materials.get(0),
            "first-seen order starts with planet 1");
        assertEquals(
            ocean.getOres()
                .get(0)
                .getOreType(),
            materials.get(
                rocky.getOres()
                    .size()),
            "then planet 2's first ore");
    }

    @Test
    public void testMaterialsOfIsNullSafe() {
        assertTrue(
            USSPlanets.materialsOf(null)
                .isEmpty());
        assertTrue(
            USSPlanets.materialsOf(Collections.<USSPlanets.USSPlanet>emptyList())
                .isEmpty());
    }

    @Test
    public void testNullStarTypeFallsBackToMainSequence() {
        List<USSPlanets.USSPlanet> nullType = USSPlanets.generate(null, 42L);
        List<USSPlanets.USSPlanet> mainSequence = USSPlanets.generate(USSStarType.MAIN_SEQUENCE, 42L);
        assertEquals(mainSequence, nullType, "null star type → main sequence system (identical draw)");
        assertNotEquals(
            USSPlanets.generate(USSStarType.SUPERMASSIVE, 42L),
            mainSequence,
            "different star types draw from different pools (same seed)");
    }
}
