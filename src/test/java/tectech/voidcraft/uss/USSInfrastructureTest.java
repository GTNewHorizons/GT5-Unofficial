package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Phase 4 pass 2 per-USS infrastructure progress ({@link USSInfrastructure}) — incremental
 * building across multiple Constructor missions, first-incomplete selection, overflow clamping, and the NBT
 * round-trip (including the corrupt → fresh behavior the project mandates).
 */
public class USSInfrastructureTest {

    private static Map<String, Long> materials(String... nameAmountPairs) {
        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = 0; i < nameAmountPairs.length; i += 2) {
            map.put(nameAmountPairs[i], Long.parseLong(nameAmountPairs[i + 1]));
        }
        return map;
    }

    private static String name(USSProject.Cost cost) {
        return cost.materialName;
    }

    private static long amount(USSProject.Cost cost) {
        return cost.amount;
    }

    @Test
    public void testFreshInfrastructureHasNoProgress() {
        USSInfrastructure infrastructure = new USSInfrastructure();
        assertEquals(0L, infrastructure.consumed(0, "whatever"), "unknown material → 0");
        assertEquals(0L, infrastructure.consumed(99, "whatever"), "unknown project → 0");
        assertFalse(infrastructure.isComplete(0), "nothing built → not complete");
        assertEquals(0L, infrastructure.apply(0, materials("anything", "1000")), "no project → nothing applied");
    }

    @Test
    public void testFirstIncompleteIsTheBuildOrderHead() {
        USSInfrastructure infrastructure = new USSInfrastructure();
        USSProject first = USSProject.CATALOG.get(0);

        assertEquals(first, infrastructure.firstIncomplete(), "fresh → the first catalog project");

        // Complete project 0 in one shot → project 1 becomes the head.
        Map<String, Long> full0 = new HashMap<>();
        for (USSProject.Cost cost : first.costs) {
            full0.put(cost.materialName, cost.amount);
        }
        assertEquals(sumOf(full0), infrastructure.apply(first.id, full0), "full apply credits everything");
        assertEquals(
            USSProject.CATALOG.get(1),
            infrastructure.firstIncomplete(),
            "project 0 done → project 1 is the head");

        // ...and so on until the catalog is exhausted (every remaining project fully built).
        for (int i = 1; i < USSProject.CATALOG.size(); i++) {
            USSProject project = USSProject.CATALOG.get(i);
            Map<String, Long> full = new HashMap<>();
            for (USSProject.Cost cost : project.costs) {
                full.put(cost.materialName, cost.amount);
            }
            infrastructure.apply(project.id, full);
        }
        assertNull(infrastructure.firstIncomplete(), "whole catalog complete → nothing to build");
    }

    @Test
    public void testApplyIsIncrementalAndClamped() {
        USSProject project = USSProject.byId(0);
        USSInfrastructure infrastructure = new USSInfrastructure();
        USSProject.Cost firstCost = project.costs.get(0);

        long cost = amount(firstCost);
        assertTrue(cost > 0L);

        // Half of the first material, nothing else.
        long half = cost / 2;
        assertEquals(
            half,
            infrastructure.apply(project.id, materials(name(firstCost), String.valueOf(half))),
            "partial apply credits exactly what was carried");
        assertEquals(half, infrastructure.consumed(project.id, name(firstCost)));
        assertFalse(infrastructure.isComplete(project.id), "one material done → project not complete");
        assertEquals(
            cost - half,
            infrastructure.remaining(project.id, name(firstCost)),
            "remaining is clamped by the cost");

        // Over-apply: carry MORE than is left — only the remainder is credited (no corruption past the cost).
        long over = cost + 1_000_000L;
        long applied = infrastructure.apply(project.id, materials(name(firstCost), String.valueOf(over)));
        assertEquals(cost - half, applied, "overflow beyond the cost is NOT applied");
        assertEquals(cost, infrastructure.consumed(project.id, name(firstCost)), "progress stops exactly at the cost");

        // Finish the project (each cost's own amount; the first cost only needs the remainder).
        Map<String, Long> full = new HashMap<>();
        for (USSProject.Cost c : project.costs) {
            full.put(c.materialName, c.amount - (c == firstCost ? half : 0L));
        }
        infrastructure.apply(project.id, full);
        assertTrue(infrastructure.isComplete(project.id), "all costs full → complete");
        assertEquals(0L, infrastructure.remaining(project.id, name(firstCost)), "no remainder once complete");

        // A stale payload (extra material on a complete project) applies nothing.
        assertEquals(
            0L,
            infrastructure.apply(project.id, materials(name(firstCost), "500")),
            "complete project → a late loadout applies nothing");
    }

    @Test
    public void testApplyIgnoresUnknownMaterialsAndNonPositive() {
        USSProject project = USSProject.byId(0);
        USSInfrastructure infrastructure = new USSInfrastructure();
        Map<String, Long> garbage = new HashMap<>();
        garbage.put("not_a_project_material", 1000L);
        garbage.put(name(project.costs.get(0)), 0L);
        garbage.put(name(project.costs.get(0)), -5L);
        long applied = infrastructure.apply(project.id, garbage);
        assertEquals(0L, applied, "unknown / non-positive entries apply nothing");
        assertFalse(infrastructure.isComplete(project.id));
    }

    @Test
    public void testApplyNullAmountsIsSafe() {
        USSInfrastructure infrastructure = new USSInfrastructure();
        assertEquals(0L, infrastructure.apply(0, null), "null map → 0, no exception");
    }

    // region NBT round-trip

    @Test
    public void testNbtRoundTrip() {
        USSInfrastructure infrastructure = new USSInfrastructure();
        // Two projects, partially built (project 0 half of each material, project 1 fully built).
        USSProject p0 = USSProject.byId(0);
        Map<String, Long> partial = new HashMap<>();
        for (USSProject.Cost cost : p0.costs) {
            partial.put(cost.materialName, cost.amount / 2);
        }
        infrastructure.apply(p0.id, partial);
        USSProject p1 = USSProject.byId(1);
        Map<String, Long> full1 = new HashMap<>();
        for (USSProject.Cost cost : p1.costs) {
            full1.put(cost.materialName, cost.amount);
        }
        infrastructure.apply(p1.id, full1);

        NBTTagCompound nbt = new NBTTagCompound();
        infrastructure.writeToNBT(nbt);
        USSInfrastructure loaded = USSInfrastructure.readFromNBT(nbt);

        for (USSProject project : USSProject.CATALOG) {
            for (USSProject.Cost cost : project.costs) {
                long expected = (project.id == p0.id) ? cost.amount / 2 : (project.id == p1.id ? cost.amount : 0L);
                assertEquals(
                    expected,
                    loaded.consumed(project.id, cost.materialName),
                    "project " + project.id + " material " + cost.materialName);
            }
        }
        assertEquals(infrastructure.firstIncomplete(), loaded.firstIncomplete(), "selection survives the round trip");
        assertEquals(true, loaded.isComplete(p1.id), "project 1 stays complete after reload");
    }

    @Test
    public void testNbtRoundTripEmptyProgress() {
        USSInfrastructure infrastructure = new USSInfrastructure();
        NBTTagCompound nbt = new NBTTagCompound();
        infrastructure.writeToNBT(nbt);
        assertFalse(
            nbt.hasKey(USSInfrastructure.TAG_PROJECTS),
            "empty progress writes no tags (a fresh USS stays lean)");
        USSInfrastructure loaded = USSInfrastructure.readFromNBT(nbt);
        assertEquals(infrastructure.firstIncomplete(), loaded.firstIncomplete());
    }

    @Test
    public void testNbtCorruptOrUnknownYieldsFreshState() {
        // Unknown project id → dropped (the catalog may change; no migration path).
        USSProject p0 = USSProject.byId(0);
        NBTTagCompound nbt = new NBTTagCompound();
        net.minecraft.nbt.NBTTagList projects = new net.minecraft.nbt.NBTTagList();
        NBTTagCompound badProject = new NBTTagCompound();
        badProject.setInteger("id", 424242); // not in the catalog
        net.minecraft.nbt.NBTTagList badCosts = new net.minecraft.nbt.NBTTagList();
        NBTTagCompound badCost = new NBTTagCompound();
        badCost.setString("name", p0.costs.get(0).materialName);
        badCost.setLong("amount", 1234L);
        badCosts.appendTag(badCost);
        badProject.setTag(USSInfrastructure.TAG_COSTS, badCosts);
        projects.appendTag(badProject);
        nbt.setTag(USSInfrastructure.TAG_PROJECTS, projects);

        USSInfrastructure loaded = USSInfrastructure.readFromNBT(nbt);
        assertEquals(0L, loaded.consumed(424242, p0.costs.get(0).materialName), "unknown project id → dropped");
        assertEquals(p0, loaded.firstIncomplete(), "selection unaffected by the dropped entry");

        // Unknown material inside a known project → dropped.
        NBTTagCompound nbt2 = new NBTTagCompound();
        net.minecraft.nbt.NBTTagList projects2 = new net.minecraft.nbt.NBTTagList();
        NBTTagCompound project = new NBTTagCompound();
        project.setInteger("id", p0.id);
        net.minecraft.nbt.NBTTagList costs = new net.minecraft.nbt.NBTTagList();
        NBTTagCompound known = new NBTTagCompound();
        known.setString("name", p0.costs.get(0).materialName);
        known.setLong("amount", 55L);
        costs.appendTag(known);
        NBTTagCompound unknownMaterial = new NBTTagCompound();
        unknownMaterial.setString("name", "not_part_of_this_project");
        unknownMaterial.setLong("amount", 999L);
        costs.appendTag(unknownMaterial);
        project.setTag(USSInfrastructure.TAG_COSTS, costs);
        projects2.appendTag(project);
        nbt2.setTag(USSInfrastructure.TAG_PROJECTS, projects2);

        USSInfrastructure loaded2 = USSInfrastructure.readFromNBT(nbt2);
        assertEquals(55L, loaded2.consumed(p0.id, p0.costs.get(0).materialName), "known entry survives");
        assertEquals(0L, loaded2.consumed(p0.id, "not_part_of_this_project"), "unknown material dropped");
    }

    @Test
    public void testReadFromNbtNullIsFresh() {
        USSInfrastructure loaded = USSInfrastructure.readFromNBT(null);
        assertNotNull(loaded, "never null");
        assertEquals(USSProject.CATALOG.get(0), loaded.firstIncomplete());
    }

    // endregion

    private static long sumOf(Map<String, Long> map) {
        long sum = 0L;
        for (long value : map.values()) {
            sum += value;
        }
        return sum;
    }
}
