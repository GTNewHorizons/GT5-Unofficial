package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;

/**
 * Unit tests for deterministic planet generation ({@link USSPlanets}) — Phase 4 pass 3: the system's planets are a
 * pure function of (star type, seed), with orbital parameters inside the legacy EoH ranges (so the shared orbit
 * renderer can draw them) and inside the space shell.
 *
 * <p>
 * Bare-JVM: no blocks or world involved — the hologram block resolution happens at the render TE boundary.
 */
public class USSPlanetsTest {

    @Test
    public void testSystemHasAFewPlanets() {
        for (USSStarType starType : USSStarType.values()) {
            List<USSPlanets.USSPlanet> system = USSPlanets.generate(starType, 123L);
            assertEquals(USSPlanets.PLANETS_PER_SYSTEM, system.size(), starType + " — exactly the fixed count");
            for (USSPlanets.USSPlanet planet : system) {
                assertEquals(
                    starType,
                    planet.type.getStarType(),
                    starType + " — every planet comes from the star's own pool");
            }
        }
    }

    @Test
    public void testSystemPlanetsHaveDistinctTypes() {
        // Pass 7: 3 DIFFERENT worlds (and therefore different colors). The pass-3 with-replacement draw put
        // duplicate types in 62.5% of systems — users read that as "all planets are the same color" and the
        // infodata as "one planet" (three identical entries).
        for (USSStarType starType : USSStarType.values()) {
            for (long seed = 1; seed <= 16; seed++) {
                Set<USSPlanetType> types = new java.util.HashSet<>();
                for (USSPlanets.USSPlanet planet : USSPlanets.generate(starType, seed)) {
                    types.add(planet.type);
                }
                assertEquals(
                    USSPlanets.PLANETS_PER_SYSTEM,
                    types.size(),
                    starType + " seed " + seed + " — every planet must be a different type");
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
            Set<USSPlanetType> main = new java.util.HashSet<>();
            Set<USSPlanetType> white = new java.util.HashSet<>();
            Set<USSPlanetType> supermassive = new java.util.HashSet<>();
            for (USSPlanets.USSPlanet planet : USSPlanets.generate(USSStarType.MAIN_SEQUENCE, seed)) {
                main.add(planet.type);
            }
            for (USSPlanets.USSPlanet planet : USSPlanets.generate(USSStarType.WHITE_DWARF, seed)) {
                white.add(planet.type);
            }
            for (USSPlanets.USSPlanet planet : USSPlanets.generate(USSStarType.SUPERMASSIVE, seed)) {
                supermassive.add(planet.type);
            }
            assertTrue(isDisjoint(main, white), "seed " + seed + " — main sequence vs white dwarf families");
            assertTrue(isDisjoint(main, supermassive), "seed " + seed + " — main sequence vs supermassive families");
            assertTrue(isDisjoint(white, supermassive), "seed " + seed + " — white dwarf vs supermassive families");
        }
    }

    private static boolean isDisjoint(Set<USSPlanetType> a, Set<USSPlanetType> b) {
        for (USSPlanetType type : a) {
            if (b.contains(type)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testOrbitalParametersStayInLegacyRanges() {
        for (long seed = 1; seed <= 32; seed++) {
            List<USSPlanets.USSPlanet> system = USSPlanets.generate(USSStarType.WHITE_DWARF, seed);
            double band = (USSPlanets.MAX_DISTANCE - USSPlanets.MIN_DISTANCE) / USSPlanets.PLANETS_PER_SYSTEM;
            for (int i = 0; i < system.size(); i++) {
                USSPlanets.USSPlanet planet = system.get(i);
                // distance: inside its orbital band (bands are disjoint, inner → outer), clear of the star (≤ 1.4)
                // and inside the 12.95-block space shell
                assertTrue(
                    planet.distance >= USSPlanets.MIN_DISTANCE + band * i
                        && planet.distance <= USSPlanets.MIN_DISTANCE + band * (i + 1),
                    "seed " + seed + " planet " + i + " distance " + planet.distance + " in band " + i);
                assertTrue(planet.distance > 1.4, "seed " + seed + " — clear of the star surface");
                assertTrue(planet.distance < 12.95, "seed " + seed + " — inside the space shell");
                assertTrue(
                    planet.scale >= USSPlanets.MIN_SCALE && planet.scale <= USSPlanets.MAX_SCALE,
                    "seed " + seed + " — scale in range");
                assertTrue(
                    planet.orbitSpeed >= USSPlanets.MIN_SPEED && planet.orbitSpeed <= USSPlanets.MAX_SPEED,
                    "seed " + seed + " — orbit speed in range");
                assertTrue(
                    planet.rotationSpeed >= USSPlanets.MIN_SPEED && planet.rotationSpeed <= USSPlanets.MAX_SPEED,
                    "seed " + seed + " — spin speed in range");
                // Pass 11: coplanar orbits — each tilt component stays within the small 0–5° inclination (the
                // components of one inclination can each reach the full inclination, so the bound holds per axis).
                assertTrue(
                    Math.abs(planet.xAngle) <= USSPlanets.MAX_INCLINATION_DEG
                        && Math.abs(planet.zAngle) <= USSPlanets.MAX_INCLINATION_DEG,
                    "seed " + seed + " — orbit inclinations in range (0–5°)");
            }
        }
    }

    @Test
    public void testMaterialsOfDeduplicatesAndOrdersByFirstSeen() {
        USSPlanetType rocky = USSPlanetType.ROCKY_WORLD;
        USSPlanetType ocean = USSPlanetType.OCEAN_WORLD;

        List<USSPlanets.USSPlanet> system = new java.util.ArrayList<>();
        system.add(new USSPlanets.USSPlanet(rocky, 5.0, 0.5, 1.0, 1.0, 0, 0));
        system.add(new USSPlanets.USSPlanet(ocean, 7.0, 0.5, 1.0, 1.0, 0, 0));

        List<Materials> materials = USSPlanets.materialsOf(system);
        assertEquals(6, materials.size(), "two distinct planets → 6 ores");
        assertEquals(rocky.getMaterials()[0], materials.get(0), "first-seen order starts with planet 1");
        assertEquals(ocean.getMaterials()[0], materials.get(3), "then planet 2's first ore");

        // a repeated type adds nothing new
        system.add(new USSPlanets.USSPlanet(rocky, 9.0, 0.5, 1.0, 1.0, 0, 0));
        assertEquals(
            6,
            USSPlanets.materialsOf(system)
                .size(),
            "repeated planet type → same union");
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
