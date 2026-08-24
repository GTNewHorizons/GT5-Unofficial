package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Phase 4 pass 2 pure loadout computation ({@link USSLoadout}) — the take per cost entry is
 * {@code min(remaining, cap, available)}: the three inputs come from different worlds and must stay independent.
 */
public class USSLoadoutTest {

    private static Map<String, Long> map(String... nameAmountPairs) {
        Map<String, Long> map = new LinkedHashMap<>();
        for (int i = 0; i < nameAmountPairs.length; i += 2) {
            map.put(nameAmountPairs[i], Long.parseLong(nameAmountPairs[i + 1]));
        }
        return map;
    }

    private static String name(USSProject.Cost cost) {
        return cost.materialName;
    }

    @Test
    public void testFullLoadWhenEverythingIsAvailable() {
        USSProject project = USSProject.byId(0);
        Map<String, Long> available = new HashMap<>();
        for (USSProject.Cost cost : project.costs) {
            available.put(cost.materialName, cost.amount);
        }
        Map<String, Long> take = USSLoadout.compute(project, null, 10_000_000L, 10_000_000L, available);
        assertEquals(project.costs.size(), take.size(), "every cost entry loaded");
        for (USSProject.Cost cost : project.costs) {
            assertEquals(cost.amount, (long) take.get(cost.materialName), cost.materialName + " full amount");
        }
    }

    @Test
    public void testTakeIsCappedByRemaining() {
        USSProject project = USSProject.byId(0);
        USSProject.Cost cost = project.costs.get(0);
        // Half already applied → only the half remains, no matter how much the ship can carry or is available.
        Map<String, Long> consumed = map(name(cost), String.valueOf(cost.amount / 2));
        Map<String, Long> available = new HashMap<>();
        for (USSProject.Cost c : project.costs) {
            available.put(c.materialName, c.amount);
        }
        Map<String, Long> take = USSLoadout.compute(project, consumed, 10_000_000L, 10_000_000L, available);
        assertEquals(cost.amount / 2, (long) take.get(cost.materialName), "take stops at the remaining need");
        // The other costs are still loaded in full.
        for (USSProject.Cost c : project.costs) {
            if (c != cost) {
                assertEquals(c.amount, (long) take.get(c.materialName), c.materialName + " unaffected");
            }
        }
    }

    @Test
    public void testTakeIsCappedByMissionCapacity() {
        USSProject project = USSProject.byId(0);
        USSProject.Cost cost = project.costs.get(0);
        long cap = Math.max(1L, cost.amount / 4);
        Map<String, Long> available = map(name(cost), String.valueOf(cost.amount));
        Map<String, Long> take = USSLoadout.compute(project, null, cap, cap, available);
        assertEquals(cap, (long) take.get(cost.materialName), "take is limited by the ship's per-mission capacity");
    }

    @Test
    public void testTakeIsLimitedByAvailability() {
        USSProject project = USSProject.byId(0);
        USSProject.Cost cost = project.costs.get(0);
        long have = Math.max(1L, cost.amount / 8);
        Map<String, Long> available = map(name(cost), String.valueOf(have));
        Map<String, Long> take = USSLoadout.compute(project, null, 10_000_000L, 10_000_000L, available);
        assertEquals(have, (long) take.get(cost.materialName), "take is limited by what the gateway holds");
    }

    @Test
    public void testEmptyAvailabilityYieldsEmptyLoadout() {
        USSProject project = USSProject.byId(0);
        assertTrue(
            USSLoadout.compute(project, null, 10_000_000L, 10_000_000L, null)
                .isEmpty(),
            "null availability → empty loadout (the gateway reports no_materials)");
        assertTrue(
            USSLoadout.compute(project, null, 10_000_000L, 10_000_000L, new HashMap<>())
                .isEmpty(),
            "empty availability → empty loadout");
    }

    @Test
    public void testCompletedCostsAreSkipped() {
        USSProject project = USSProject.byId(0);
        // Everything already complete → the loadout is empty even with unlimited availability.
        Map<String, Long> consumed = new HashMap<>();
        for (USSProject.Cost cost : project.costs) {
            consumed.put(cost.materialName, cost.amount);
        }
        Map<String, Long> available = new HashMap<>(consumed);
        assertTrue(
            USSLoadout.compute(project, consumed, 10_000_000L, 10_000_000L, available)
                .isEmpty(),
            "a complete project loads nothing (stale payload protection)");
    }

    @Test
    public void testCapsBelowOneAreClampedToOne() {
        USSProject project = USSProject.byId(0);
        USSProject.Cost cost = project.costs.get(0);
        Map<String, Long> available = map(name(cost), String.valueOf(cost.amount));
        Map<String, Long> take = USSLoadout.compute(project, null, 0L, 0L, available);
        assertEquals(1L, (long) take.get(cost.materialName), "a degenerate cap degrades to one unit, not zero");
    }

    @Test
    public void testNullProjectIsSafe() {
        assertTrue(
            USSLoadout.compute(null, null, 100L, 100L, null)
                .isEmpty(),
            "null project → empty loadout");
    }

    @Test
    public void testNegativeInputsAreIgnored() {
        USSProject project = USSProject.byId(0);
        USSProject.Cost cost = project.costs.get(0);
        Map<String, Long> available = map(name(cost), "500");
        Map<String, Long> consumed = map(name(cost), "-100");
        Map<String, Long> take = USSLoadout.compute(project, consumed, 10_000_000L, 10_000_000L, available);
        assertEquals(
            500L,
            (long) take.get(cost.materialName),
            "negative consumed is treated as none; availability caps the take");
    }

    @Test
    public void testUnknownMaterialsInInputsAreIgnored() {
        USSProject project = USSProject.byId(0);
        Map<String, Long> available = map("not_part_of_this_project", "999999", name(project.costs.get(0)), "250");
        Map<String, Long> take = USSLoadout.compute(project, null, 10_000_000L, 10_000_000L, available);
        assertEquals(1, take.size(), "only the project's own materials are loaded");
        assertEquals(250L, (long) take.get(project.costs.get(0).materialName));
    }
}
