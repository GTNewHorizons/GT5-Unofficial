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
        assertEquals(8, VoidcraftCoverComponent.ALL.length);
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
        assertEquals(VoidcraftComponent.UTILITY, VoidcraftCoverComponent.ARMOR_PLATE.getMirroredComponent());
        assertEquals(VoidcraftComponent.CARGO_BAY, VoidcraftCoverComponent.CARGO_POD.getMirroredComponent());
        assertEquals(VoidcraftComponent.MINING_CENTRE, VoidcraftCoverComponent.MINING_ARRAY.getMirroredComponent());
        assertEquals(VoidcraftComponent.STARLIFTER_ARRAY, VoidcraftCoverComponent.STAR_SIPHON.getMirroredComponent());
        assertEquals(VoidcraftComponent.SPACETIME_SCANNER, VoidcraftCoverComponent.SCANNER_DISH.getMirroredComponent());
        assertEquals(
            VoidcraftComponent.CONSTRUCTION_ARM,
            VoidcraftCoverComponent.FABRICATOR_UNIT.getMirroredComponent());
        assertEquals(VoidcraftComponent.REACTOR, VoidcraftCoverComponent.POWER_CELL.getMirroredComponent());
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
            VoidcraftCoverComponent.fromGridValue(9)
                .isEmpty(),
            "9 is beyond the 8 covers");
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
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            assertTrue(cover.getMass() > 0, cover + " must have mass");
        }
    }
}
