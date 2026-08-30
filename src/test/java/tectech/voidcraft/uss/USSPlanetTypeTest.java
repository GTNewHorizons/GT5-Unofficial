package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;

/**
 * Unit tests for the fixed planet-type catalog ({@link USSPlanetType}) — star type → planet pool →
 * mineable ore set.
 *
 * <p>
 * Bare-JVM: only the {@link gregtech.api.enums.Materials} registry is involved (no blocks, no fluids, no world).
 */
public class USSPlanetTypeTest {

    @Test
    public void testCatalogHasTwelveOrderedTypes() {
        List<USSPlanetType> all = USSPlanetType.all();
        assertEquals(12, all.size(), "the catalog has 12 planet types");
        for (int i = 0; i < all.size(); i++) {
            assertEquals(i, all.get(i).id, "id " + i + " is stable and in catalog order");
            assertEquals(all.get(i), USSPlanetType.byId(i), "byId round-trips");
        }
        assertEquals(null, USSPlanetType.byId(-1), "below range → null");
        assertEquals(null, USSPlanetType.byId(12), "above range → null");
    }

    @Test
    public void testEachTypeCarriesThreeDistinctResolvableMaterials() {
        for (USSPlanetType type : USSPlanetType.values()) {
            Materials[] materials = type.getMaterials();
            assertEquals(3, materials.length, type + " carries three ores");
            Set<Materials> distinct = new HashSet<>();
            for (Materials material : materials) {
                assertNotNull(material, type + " material");
                assertNotEquals(Materials._NULL, material, type + " material is not the sentinel");
                // name → material round-trip (the cargo entries and infodata rely on name identity)
                Materials resolved = Materials.get(material.getName());
                assertNotEquals(Materials._NULL, resolved, type + " material name resolves");
                assertEquals(material, resolved, type + " material name resolves back to itself");
                assertTrue(distinct.add(material), type + " ore set is distinct (" + material + ")");
            }
        }
    }

    @Test
    public void testCatalogMaterialsAreGloballyDistinct() {
        Set<Materials> seen = new HashSet<>();
        for (USSPlanetType type : USSPlanetType.values()) {
            for (Materials material : type.getMaterials()) {
                assertTrue(seen.add(material), "material " + material.getName() + " is unique in the catalog");
            }
        }
        assertEquals(36, seen.size(), "12 types × 3 ores, no sharing");
    }

    @Test
    public void testLangKeysAreUniqueAndNamespaced() {
        Set<String> keys = new HashSet<>();
        for (USSPlanetType type : USSPlanetType.values()) {
            String key = type.getLangKey();
            assertTrue(key.startsWith("tt.voidcraft_uss.planet."), type + " lang key is namespaced: " + key);
            assertTrue(keys.add(key), "lang key unique: " + key);
        }
    }

    @Test
    public void testVisualsAreNonEmpty() {
        Set<String> visuals = new HashSet<>();
        for (USSPlanetType type : USSPlanetType.values()) {
            String visual = type.getVisual();
            assertNotNull(visual, type + " has a hologram dimension key");
            assertFalse(visual.isEmpty(), type + " hologram dimension key is non-empty");
            assertTrue(visuals.add(visual), "hologram dimension key unique (" + visual + ")");
        }
    }

    @Test
    public void testPoolIsNullSafe() {
        assertEquals(
            USSPlanetType.pool(USSStarType.YELLOW_DWARF),
            USSPlanetType.pool(null),
            "null star type falls back to yellow dwarf");
    }
}
