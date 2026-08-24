package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;

/**
 * Unit tests for the Phase 4 pass 2 infrastructure project catalog ({@link USSProject}) — the fixed build order and
 * the cost-table invariants the rest of the Constructor slice relies on.
 *
 * <p>
 * Bare-JVM safe: only the {@code Materials} name strings are touched (the same surface
 * {@code USSShipCargoTest} exercises), no Fluid/ItemStack construction.
 */
public class USSProjectTest {

    @Test
    public void testCatalogHasDistinctIdsInBuildOrder() {
        assertEquals(3, USSProject.CATALOG.size(), "the vertical-slice catalog has three projects");
        Set<Integer> ids = new HashSet<>();
        int expectedId = 0;
        for (USSProject project : USSProject.CATALOG) {
            assertEquals(expectedId++, project.id, "catalog ids are contiguous from 0 (build order = list order)");
            assertTrue(ids.add(project.id), "distinct id: " + project.id);
        }
    }

    @Test
    public void testByRoundTripById() {
        for (USSProject project : USSProject.CATALOG) {
            assertNotNull(USSProject.byId(project.id), "byId resolves catalog id " + project.id);
            assertEquals(project, USSProject.byId(project.id), "byId is identity for " + project.id);
        }
        assertNull(USSProject.byId(-1), "unknown id → null");
        assertNull(USSProject.byId(999), "unknown id → null");
    }

    @Test
    public void testEveryProjectHasCosts() {
        for (USSProject project : USSProject.CATALOG) {
            assertTrue(project.costs.size() >= 1, project.id + " has at least one cost");
            assertNotNull(project.langKey, project.id + " has a display lang key");
            for (USSProject.Cost cost : project.costs) {
                assertTrue(cost.amount > 0L, project.id + " cost " + cost.materialName + " is positive");
                assertNotNull(cost.kind, project.id + " cost " + cost.materialName + " has a kind");
                assertNotNull(cost.materialName, project.id + " cost has a material name");
            }
        }
    }

    @Test
    public void testCostMaterialNamesResolve() {
        // The whole slice (gateway loadout, USS apply, infodata display) resolves cost names through
        // Materials.get(String) — a bad name here would silently zero out the whole project.
        for (USSProject project : USSProject.CATALOG) {
            for (USSProject.Cost cost : project.costs) {
                Materials material = Materials.get(cost.materialName);
                assertNotEquals(Materials._NULL, material, project.id + " cost " + cost.materialName + " must resolve");
                assertEquals(
                    cost.materialName,
                    material.getName(),
                    project.id + " cost " + cost.materialName + " resolves to itself");
            }
        }
    }

    @Test
    public void testNoDuplicateMaterialsPerProject() {
        for (USSProject project : USSProject.CATALOG) {
            Set<String> names = new HashSet<>();
            for (USSProject.Cost cost : project.costs) {
                assertTrue(names.add(cost.materialName), project.id + " has a single entry for " + cost.materialName);
            }
        }
    }

    @Test
    public void testCostOfLooksUpByName() {
        USSProject project = USSProject.byId(0);
        for (USSProject.Cost cost : project.costs) {
            assertEquals(cost, project.costOf(cost.materialName), "costOf finds " + cost.materialName);
        }
        assertNull(project.costOf("definitely_not_a_cost"), "costOf of an unknown material → null");
    }

    @Test
    public void testCatalogOrderIsStable() {
        // firstIncomplete() and the infodata both iterate CATALOG in order — pin the order explicitly.
        assertEquals(3, USSProject.CATALOG.size());
        for (int i = 0; i < USSProject.CATALOG.size(); i++) {
            assertEquals(i, USSProject.CATALOG.get(i).id, "position " + i + " = id " + i);
        }
    }
}
