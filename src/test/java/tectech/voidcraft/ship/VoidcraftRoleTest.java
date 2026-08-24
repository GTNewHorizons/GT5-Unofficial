package tectech.voidcraft.ship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class VoidcraftRoleTest {

    @Test
    public void testRoleBitsAreDistinct() {
        for (VoidcraftRole a : VoidcraftRole.values()) {
            for (VoidcraftRole b : VoidcraftRole.values()) {
                if (a == b) {
                    assertTrue(a.isActive(a.getBit()));
                } else {
                    assertFalse(a.isActive(b.getBit()));
                }
            }
        }
    }

    @Test
    public void testComputeRoles() {
        assertEquals(0, VoidcraftRole.computeRoles(0, 0, 0, 0));
        assertEquals(VoidcraftRole.MINER.getBit(), VoidcraftRole.computeRoles(1, 0, 0, 0));
        assertEquals(VoidcraftRole.EXPLORER.getBit(), VoidcraftRole.computeRoles(0, 5, 0, 0));
        assertEquals(VoidcraftRole.CONSTRUCTOR.getBit(), VoidcraftRole.computeRoles(0, 0, 9, 0));
        assertEquals(VoidcraftRole.STARLIFTER.getBit(), VoidcraftRole.computeRoles(0, 0, 0, 3));

        int all = VoidcraftRole.computeRoles(1, 1, 1, 1);
        assertEquals(VoidcraftRole.ALL_ROLES, all);
        assertEquals(4, VoidcraftRole.countRoles(all));
    }

    @Test
    public void testEfficiencyMultiplier() {
        assertEquals(1.0, VoidcraftRole.efficiencyMultiplier(0), 1e-9);
        assertEquals(1.0, VoidcraftRole.efficiencyMultiplier(1), 1e-9);
        assertEquals(VoidcraftConstants.HYBRID_ROLE_PENALTY, VoidcraftRole.efficiencyMultiplier(2), 1e-9);
        assertEquals(Math.pow(VoidcraftConstants.HYBRID_ROLE_PENALTY, 2), VoidcraftRole.efficiencyMultiplier(3), 1e-9);
        assertEquals(Math.pow(VoidcraftConstants.HYBRID_ROLE_PENALTY, 3), VoidcraftRole.efficiencyMultiplier(4), 1e-9);
    }

    @Test
    public void testActiveRolesList() {
        int two = VoidcraftRole.MINER.getBit() | VoidcraftRole.STARLIFTER.getBit();
        assertEquals(
            2,
            VoidcraftRole.activeRoles(two)
                .size());
        assertTrue(
            VoidcraftRole.activeRoles(two)
                .contains(VoidcraftRole.MINER));
        assertTrue(
            VoidcraftRole.activeRoles(two)
                .contains(VoidcraftRole.STARLIFTER));
        assertFalse(
            VoidcraftRole.activeRoles(two)
                .contains(VoidcraftRole.EXPLORER));
        assertTrue(
            VoidcraftRole.activeRoles(0)
                .isEmpty());
    }
}
