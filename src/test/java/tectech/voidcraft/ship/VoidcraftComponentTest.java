package tectech.voidcraft.ship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class VoidcraftComponentTest {

    @Test
    public void testMetaMappingIsContiguous() {
        for (int i = 0; i < VoidcraftComponent.ALL.length; i++) {
            assertEquals(i, VoidcraftComponent.ALL[i].getMeta(), "meta mismatch at index " + i);
            assertEquals(
                i,
                VoidcraftComponent.fromMeta(i)
                    .orElseThrow()
                    .getMeta());
        }
        assertTrue(
            VoidcraftComponent.fromMeta(-1)
                .isEmpty());
        assertTrue(
            VoidcraftComponent.fromMeta(VoidcraftComponent.ALL.length)
                .isEmpty());
    }

    @Test
    public void testGridValueRoundTrip() {
        for (VoidcraftComponent component : VoidcraftComponent.ALL) {
            byte value = (byte) component.toGridValue();
            assertEquals(
                component,
                VoidcraftComponent.fromGridValue(value)
                    .orElseThrow());
        }
        // 0 and out-of-range values are not components
        assertTrue(
            VoidcraftComponent.fromGridValue(0)
                .isEmpty());
        assertTrue(
            VoidcraftComponent.fromGridValue(-1)
                .isEmpty());
        assertTrue(
            VoidcraftComponent.fromGridValue(VoidcraftComponent.ALL.length + 1)
                .isEmpty());
    }

    @Test
    public void testExactlyOneControllerEngineAndUtility() {
        assertEquals(
            1,
            countComponents(
                component -> component.name()
                    .contains("CONTROLLER")));
        assertEquals(1, countComponents(c -> c == VoidcraftComponent.ENGINE));
        assertEquals(1, countComponents(c -> c == VoidcraftComponent.FRAME));
    }

    @Test
    public void testPlaceableSet() {
        // Covers are the primary components — the only CLASSIC placeable full blocks are the controller and the
        // frame; the multiblock component blocks are the second kind of placeable block.
        assertEquals(
            8,
            VoidcraftComponent.PLACEABLE.size(),
            "controller + frame + the six multiblock component blocks");
        assertTrue(VoidcraftComponent.PLACEABLE.contains(VoidcraftComponent.CONTROLLER));
        assertTrue(VoidcraftComponent.PLACEABLE.contains(VoidcraftComponent.FRAME));
        for (VoidcraftComponent component : VoidcraftComponent.ALL) {
            if (component == VoidcraftComponent.CONTROLLER || component == VoidcraftComponent.FRAME
                || component.isMultiblock()) {
                assertTrue(component.isPlaceable(), component + " must be placeable");
                assertFalse(component.isCoverOnly());
            } else {
                assertFalse(component.isPlaceable(), component + " must be cover-only");
                assertTrue(component.isCoverOnly());
            }
        }
    }

    @Test
    public void testMultiblockEntries() {
        // The Mining Array multiblock: one stats-carrying controller entry + two zero-stat casing entries.
        assertTrue(VoidcraftComponent.MINING_ARRAY.isMultiblock(), "the mining array controller is a multiblock block");
        assertTrue(VoidcraftComponent.MINING_ARRAY.isPlaceable(), "a multiblock controller is a placeable full block");
        assertTrue(VoidcraftComponent.MINING_ARRAY_CASING.isMultiblock());
        assertTrue(VoidcraftComponent.MINING_ARRAY_PANEL.isMultiblock());
        assertEquals(
            6,
            countComponents(VoidcraftComponent::isMultiblock),
            "the Mining Array and the Satellite Launcher block triples");
        // The controller entry carries the component's stats
        assertEquals(2, VoidcraftComponent.MINING_ARRAY.getTier());
        assertEquals(1000L, VoidcraftComponent.MINING_ARRAY.getMiningPower());
        assertEquals(200L, VoidcraftComponent.MINING_ARRAY.getEnergyDraw());
        assertEquals(25L, VoidcraftComponent.MINING_ARRAY.getMass());
        // The casings contribute mass only
        for (VoidcraftComponent casing : new VoidcraftComponent[] { VoidcraftComponent.MINING_ARRAY_CASING,
            VoidcraftComponent.MINING_ARRAY_PANEL }) {
            assertEquals(5L, casing.getMass(), casing + " contributes mass only");
            assertEquals(0, casing.getTier());
            assertEquals(0L, casing.getMiningPower());
            assertEquals(0L, casing.getEnergyDraw());
            assertEquals(0L, casing.getThrust());
            assertEquals(0L, casing.getCargoSlots());
            assertEquals(0L, casing.getScanPower());
            assertEquals(0L, casing.getConstructionPower());
            assertEquals(0L, casing.getStarlifterPower());
            assertEquals(0L, casing.getEnergyBuffer());
            assertEquals(0L, casing.getIntegrity());
        }
        // Meta / grid values: 11, 12, 13 → grid 12, 13, 14
        assertEquals(11, VoidcraftComponent.MINING_ARRAY.getMeta());
        assertEquals(12, VoidcraftComponent.MINING_ARRAY_CASING.getMeta());
        assertEquals(13, VoidcraftComponent.MINING_ARRAY_PANEL.getMeta());
        assertEquals(12, VoidcraftComponent.MINING_ARRAY.toGridValue());
        assertEquals(13, VoidcraftComponent.MINING_ARRAY_CASING.toGridValue());
        assertEquals(14, VoidcraftComponent.MINING_ARRAY_PANEL.toGridValue());
        // The Satellite Launcher multiblock: one stats-carrying controller entry (tier 2, mass only) + two
        // zero-stat casing entries.
        assertTrue(VoidcraftComponent.SATELLITE_LAUNCHER.isMultiblock());
        assertTrue(VoidcraftComponent.SATELLITE_LAUNCHER.isPlaceable());
        assertTrue(VoidcraftComponent.SATELLITE_LAUNCHER_CASING.isMultiblock());
        assertTrue(VoidcraftComponent.SATELLITE_LAUNCHER_PANEL.isMultiblock());
        assertEquals(2, VoidcraftComponent.SATELLITE_LAUNCHER.getTier());
        assertEquals(60L, VoidcraftComponent.SATELLITE_LAUNCHER.getMass());
        for (VoidcraftComponent casing : new VoidcraftComponent[] { VoidcraftComponent.SATELLITE_LAUNCHER_CASING,
            VoidcraftComponent.SATELLITE_LAUNCHER_PANEL }) {
            assertEquals(5L, casing.getMass(), casing + " contributes mass only");
            assertEquals(0, casing.getTier());
        }
        // Meta / grid values: 15, 16, 17 → grid 16, 17, 18
        assertEquals(15, VoidcraftComponent.SATELLITE_LAUNCHER.getMeta());
        assertEquals(16, VoidcraftComponent.SATELLITE_LAUNCHER_CASING.getMeta());
        assertEquals(17, VoidcraftComponent.SATELLITE_LAUNCHER_PANEL.getMeta());
        assertEquals(16, VoidcraftComponent.SATELLITE_LAUNCHER.toGridValue());
        assertEquals(17, VoidcraftComponent.SATELLITE_LAUNCHER_CASING.toGridValue());
        assertEquals(18, VoidcraftComponent.SATELLITE_LAUNCHER_PANEL.toGridValue());
    }

    @Test
    public void testMultiblockMteIdsFollowRendererContract() {
        // In-flight model contract (ShipModelBuilder): the MTE id of every placeable block is 32058 + catalog
        // meta — the renderer derives the block's texture meta straight from the blueprint's grid values.
        final int base = 32058;
        assertEquals(
            base + VoidcraftComponent.CONTROLLER.getMeta(),
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftComponent_Controller.ID);
        assertEquals(
            base + VoidcraftComponent.FRAME.getMeta(),
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftComponent_Frame.ID);
        assertEquals(
            base + VoidcraftComponent.MINING_ARRAY.getMeta(),
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftMiningArrayController.ID);
        assertEquals(
            base + VoidcraftComponent.MINING_ARRAY_CASING.getMeta(),
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftMiningArrayCasing.ID);
        assertEquals(
            base + VoidcraftComponent.MINING_ARRAY_PANEL.getMeta(),
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftMiningArrayPanel.ID);
        assertEquals(
            base + VoidcraftComponent.SATELLITE_LAUNCHER.getMeta(),
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftSatelliteLauncherController.ID);
        assertEquals(
            base + VoidcraftComponent.SATELLITE_LAUNCHER_CASING.getMeta(),
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftSatelliteLauncherCasing.ID);
        assertEquals(
            base + VoidcraftComponent.SATELLITE_LAUNCHER_PANEL.getMeta(),
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftSatelliteLauncherPanel.ID);
    }

    @Test
    public void testVoidbaseCoversAreCoverOnly() {
        // The repair bay and solar panel function definitions behind the new covers: cover-only, tier 2
        assertTrue(VoidcraftComponent.REPAIR_BAY.isCoverOnly(), "the repair bay is a cover, not a block");
        assertTrue(VoidcraftComponent.SOLAR_PANEL.isCoverOnly(), "the solar panel is a cover, not a block");
        assertEquals(2, VoidcraftComponent.REPAIR_BAY.getTier());
        assertEquals(2, VoidcraftComponent.SOLAR_PANEL.getTier());
        // The solar panel is the first energy-generating component definition
        assertEquals(2000L, VoidcraftComponent.SOLAR_PANEL.getEnergyGen(), "flat generation rate per panel");
        for (VoidcraftComponent component : VoidcraftComponent.ALL) {
            if (component == VoidcraftComponent.SOLAR_PANEL) {
                continue;
            }
            assertEquals(0L, component.getEnergyGen(), component + " must not generate energy");
        }
        assertEquals(2000L, VoidcraftComponent.REPAIR_BAY.getEnergyDraw(), "repair bay draw while active");
        // Grid values: meta + 1 (10 and 11)
        assertEquals(10, VoidcraftComponent.REPAIR_BAY.toGridValue());
        assertEquals(11, VoidcraftComponent.SOLAR_PANEL.toGridValue());
    }

    private static int countComponents(java.util.function.Predicate<VoidcraftComponent> predicate) {
        int count = 0;
        for (VoidcraftComponent component : VoidcraftComponent.ALL) {
            if (predicate.test(component)) count++;
        }
        return count;
    }

    @Test
    public void testRegistryCountsAndTiers() {
        assertEquals(VoidcraftComponent.ALL.length, VoidcraftComponentRegistry.COUNT);
        int maxTier = 0;
        for (VoidcraftComponent component : VoidcraftComponent.ALL) {
            maxTier = Math.max(maxTier, component.getTier());
            assertTrue(component.getTier() >= 0 && component.getTier() <= VoidcraftComponentRegistry.MAX_TIER);
        }
        assertEquals(maxTier, VoidcraftComponentRegistry.MAX_TIER);
    }

    @Test
    public void testCircuitTierGate() {
        assertEquals(0, VoidcraftComponentRegistry.maxTierForCircuit(0));
        assertEquals(0, VoidcraftComponentRegistry.maxTierForCircuit(2));
        assertEquals(1, VoidcraftComponentRegistry.maxTierForCircuit(3));
        assertEquals(1, VoidcraftComponentRegistry.maxTierForCircuit(5));
        assertEquals(2, VoidcraftComponentRegistry.maxTierForCircuit(6));
        // clamped to the highest registered tier
        assertEquals(2, VoidcraftComponentRegistry.maxTierForCircuit(24));
        // clamped to 0 for garbage damage values
        assertEquals(0, VoidcraftComponentRegistry.maxTierForCircuit(-5));
    }

    @Test
    public void testStatsAreNonNegative() {
        for (VoidcraftComponent component : VoidcraftComponent.ALL) {
            assertTrue(component.getMass() >= 0);
            assertTrue(component.getThrust() >= 0);
            assertTrue(component.getCargoSlots() >= 0);
            assertTrue(component.getMiningPower() >= 0);
            assertTrue(component.getScanPower() >= 0);
            assertTrue(component.getConstructionPower() >= 0);
            assertTrue(component.getStarlifterPower() >= 0);
            assertTrue(component.getEnergyBuffer() >= 0);
            assertTrue(component.getEnergyDraw() >= 0);
            assertTrue(component.getIntegrity() >= 0);
            assertFalse(
                component.getDisplayName()
                    .isEmpty());
        }
    }
}
