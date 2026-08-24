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
        assertTrue(countComponents(c -> c == VoidcraftComponent.UTILITY) >= 1);
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
