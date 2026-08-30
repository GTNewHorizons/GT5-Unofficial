package tectech.voidcraft.ship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Sanity tests for the cover component table (pure Java).
 */
public class VoidcraftCoverComponentTest {

    @Test
    public void testTableShape() {
        assertEquals(11, VoidcraftCoverComponent.ALL.length);
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            assertEquals(cover.ordinal(), cover.getId(), "id must match ordinal");
            assertNotEquals(
                VoidcraftComponent.CONTROLLER,
                cover.getMirroredComponent(),
                "no cover may mirror the controller");
        }
    }

    @Test
    public void testMirroredMapping() {
        assertEquals(VoidcraftComponent.ENGINE, VoidcraftCoverComponent.THRUSTER_NOZZLE.getMirroredComponent());
        assertEquals(VoidcraftComponent.FRAME, VoidcraftCoverComponent.ARMOR_PLATE.getMirroredComponent());
        assertEquals(VoidcraftComponent.CARGO_BAY, VoidcraftCoverComponent.CARGO_POD.getMirroredComponent());
        assertEquals(VoidcraftComponent.MINING_CENTRE, VoidcraftCoverComponent.MINING_ARRAY.getMirroredComponent());
        assertEquals(VoidcraftComponent.STARLIFTER_ARRAY, VoidcraftCoverComponent.STAR_SIPHON.getMirroredComponent());
        assertEquals(VoidcraftComponent.SPACETIME_SCANNER, VoidcraftCoverComponent.SCANNER_DISH.getMirroredComponent());
        assertEquals(
            VoidcraftComponent.CONSTRUCTION_ARM,
            VoidcraftCoverComponent.FABRICATOR_UNIT.getMirroredComponent());
        assertEquals(VoidcraftComponent.REACTOR, VoidcraftCoverComponent.POWER_CELL.getMirroredComponent());
        assertEquals(VoidcraftComponent.REPAIR_BAY, VoidcraftCoverComponent.REPAIR_BAY.getMirroredComponent());
        assertEquals(VoidcraftComponent.SOLAR_PANEL, VoidcraftCoverComponent.SOLAR_PANEL.getMirroredComponent());
        assertEquals(
            VoidcraftComponent.CARGO_DRONE_BAY,
            VoidcraftCoverComponent.CARGO_DRONE_BAY.getMirroredComponent());
    }

    @Test
    public void testOnlyThrusterHasThrust() {
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            if (cover == VoidcraftCoverComponent.THRUSTER_NOZZLE) {
                assertTrue(cover.getThrust() > 0, "the thruster must actually thrust");
            } else {
                assertEquals(0, cover.getThrust(), cover + " must not thrust");
            }
        }
    }

    @Test
    public void testTiers() {
        assertEquals(0, VoidcraftCoverComponent.THRUSTER_NOZZLE.getTier());
        assertEquals(0, VoidcraftCoverComponent.ARMOR_PLATE.getTier());
        assertEquals(0, VoidcraftCoverComponent.CARGO_POD.getTier());
        assertEquals(1, VoidcraftCoverComponent.MINING_ARRAY.getTier());
        assertEquals(1, VoidcraftCoverComponent.POWER_CELL.getTier());
        assertEquals(2, VoidcraftCoverComponent.STAR_SIPHON.getTier());
        assertEquals(2, VoidcraftCoverComponent.SCANNER_DISH.getTier());
        assertEquals(2, VoidcraftCoverComponent.FABRICATOR_UNIT.getTier());
        assertEquals(2, VoidcraftCoverComponent.REPAIR_BAY.getTier());
        assertEquals(2, VoidcraftCoverComponent.SOLAR_PANEL.getTier());
        assertEquals(2, VoidcraftCoverComponent.CARGO_DRONE_BAY.getTier());
    }

    @Test
    public void testEnergyGenerationAndRepairDraw() {
        // The solar panel is the first energy-generating component; the repair bay draws while repairing
        assertEquals(2000L, VoidcraftCoverComponent.SOLAR_PANEL.getEnergyGen(), "flat generation rate per panel");
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            if (cover == VoidcraftCoverComponent.SOLAR_PANEL) {
                continue;
            }
            assertEquals(0L, cover.getEnergyGen(), cover + " must not generate energy");
        }
        assertEquals(2000L, VoidcraftCoverComponent.REPAIR_BAY.getEnergyDraw(), "repair bay draw while active");
        assertEquals(0L, VoidcraftCoverComponent.SOLAR_PANEL.getEnergyDraw(), "a panel draws nothing");
        // Grid values: id + 1 (11 for the latest cover)
        assertEquals(9, VoidcraftCoverComponent.REPAIR_BAY.toGridValue());
        assertEquals(10, VoidcraftCoverComponent.SOLAR_PANEL.toGridValue());
        assertEquals(11, VoidcraftCoverComponent.CARGO_DRONE_BAY.toGridValue());
        assertEquals(
            VoidcraftCoverComponent.REPAIR_BAY,
            VoidcraftCoverComponent.fromGridValue(9)
                .orElse(null));
        assertEquals(
            VoidcraftCoverComponent.SOLAR_PANEL,
            VoidcraftCoverComponent.fromGridValue(10)
                .orElse(null));
        assertEquals(
            VoidcraftCoverComponent.CARGO_DRONE_BAY,
            VoidcraftCoverComponent.fromGridValue(11)
                .orElse(null));
        // the drone bay is the only logistics cover (1 power = 1 cargo unit per second)
        assertEquals(40L, VoidcraftCoverComponent.CARGO_DRONE_BAY.getLogisticsPower());
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            if (cover == VoidcraftCoverComponent.CARGO_DRONE_BAY) {
                continue;
            }
            assertEquals(0L, cover.getLogisticsPower(), cover + " must not carry logistics power");
        }
    }

    @Test
    public void testGridValueRoundTrip() {
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            assertEquals(
                cover,
                VoidcraftCoverComponent.fromGridValue(cover.toGridValue())
                    .orElse(null));
        }
        assertTrue(
            VoidcraftCoverComponent.fromGridValue(0)
                .isEmpty(),
            "0 = no cover");
        assertTrue(
            VoidcraftCoverComponent.fromGridValue(12)
                .isEmpty(),
            "12 is beyond the 11 covers");
        assertTrue(
            VoidcraftCoverComponent.fromGridValue(-5)
                .isEmpty());
    }

    @Test
    public void testStatsShape() {
        assertEquals(400_000L, VoidcraftCoverComponent.POWER_CELL.getEnergyBuffer());
        assertEquals(0, VoidcraftCoverComponent.POWER_CELL.getThrust());
        assertTrue(VoidcraftCoverComponent.ARMOR_PLATE.getIntegrity() > 0);
        assertTrue(VoidcraftCoverComponent.CARGO_POD.getCargoSlots() > 0);
        assertTrue(VoidcraftCoverComponent.MINING_ARRAY.getMiningPower() > 0);
        assertTrue(VoidcraftCoverComponent.STAR_SIPHON.getStarlifterPower() > 0);
        assertTrue(VoidcraftCoverComponent.SCANNER_DISH.getScanPower() > 0);
        assertTrue(VoidcraftCoverComponent.FABRICATOR_UNIT.getConstructionPower() > 0);
        assertTrue(VoidcraftCoverComponent.CARGO_DRONE_BAY.getLogisticsPower() > 0);
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            assertTrue(cover.getMass() > 0, cover + " must have mass");
        }
    }
}
