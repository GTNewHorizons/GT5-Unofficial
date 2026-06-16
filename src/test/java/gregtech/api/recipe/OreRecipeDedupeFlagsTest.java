package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OreRecipeDedupeFlagsTest {

    @Test
    void masterDisabledTurnsOffGuardsAndLeavesConcreteInputsByDefault() {
        restoreAll(() -> {
            System.setProperty(OreRecipeDedupeFlags.ENABLED_PROPERTY, "false");
            assertFalse(OreRecipeDedupeFlags.isMasterEnabled());
            assertFalse(OreRecipeDedupeFlags.guardProcessEnabled());
            assertFalse(OreRecipeDedupeFlags.guardReverseEnabled());
            assertFalse(OreRecipeDedupeFlags.canonicalInputsEnabled());
        });
    }

    @Test
    void canonicalInputsCanBeEnabledWhenMasterEnabled() {
        restoreAll(() -> {
            System.clearProperty(OreRecipeDedupeFlags.ENABLED_PROPERTY);
            System.setProperty(OreRecipeDedupeFlags.CANONICAL_INPUTS_PROPERTY, "true");
            assertTrue(OreRecipeDedupeFlags.isMasterEnabled());
            assertTrue(OreRecipeDedupeFlags.canonicalInputsEnabled());
        });
    }

    @Test
    void masterDisabledOverridesCanonicalInputsFlag() {
        restoreAll(() -> {
            System.setProperty(OreRecipeDedupeFlags.ENABLED_PROPERTY, "false");
            System.setProperty(OreRecipeDedupeFlags.CANONICAL_INPUTS_PROPERTY, "true");
            assertFalse(OreRecipeDedupeFlags.isMasterEnabled());
            assertFalse(OreRecipeDedupeFlags.canonicalInputsEnabled());
        });
    }

    @Test
    void subFlagsCanBeDisabledIndependently() {
        restoreAll(() -> {
            System.clearProperty(OreRecipeDedupeFlags.ENABLED_PROPERTY);
            System.setProperty(OreRecipeDedupeFlags.GUARD_PROCESS_PROPERTY, "false");
            System.setProperty(OreRecipeDedupeFlags.GUARD_REVERSE_PROPERTY, "true");
            System.setProperty(OreRecipeDedupeFlags.CANONICAL_INPUTS_PROPERTY, "false");
            assertTrue(OreRecipeDedupeFlags.isMasterEnabled());
            assertFalse(OreRecipeDedupeFlags.guardProcessEnabled());
            assertTrue(OreRecipeDedupeFlags.guardReverseEnabled());
            assertFalse(OreRecipeDedupeFlags.canonicalInputsEnabled());
        });
    }

    private static void restoreAll(Runnable test) {
        String enabled = System.getProperty(OreRecipeDedupeFlags.ENABLED_PROPERTY);
        String guardProcess = System.getProperty(OreRecipeDedupeFlags.GUARD_PROCESS_PROPERTY);
        String guardReverse = System.getProperty(OreRecipeDedupeFlags.GUARD_REVERSE_PROPERTY);
        String canonical = System.getProperty(OreRecipeDedupeFlags.CANONICAL_INPUTS_PROPERTY);
        try {
            System.clearProperty(OreRecipeDedupeFlags.ENABLED_PROPERTY);
            System.clearProperty(OreRecipeDedupeFlags.GUARD_PROCESS_PROPERTY);
            System.clearProperty(OreRecipeDedupeFlags.GUARD_REVERSE_PROPERTY);
            System.clearProperty(OreRecipeDedupeFlags.CANONICAL_INPUTS_PROPERTY);
            test.run();
        } finally {
            restore(OreRecipeDedupeFlags.ENABLED_PROPERTY, enabled);
            restore(OreRecipeDedupeFlags.GUARD_PROCESS_PROPERTY, guardProcess);
            restore(OreRecipeDedupeFlags.GUARD_REVERSE_PROPERTY, guardReverse);
            restore(OreRecipeDedupeFlags.CANONICAL_INPUTS_PROPERTY, canonical);
        }
    }

    private static void restore(String property, String previous) {
        if (previous == null) {
            System.clearProperty(property);
        } else {
            System.setProperty(property, previous);
        }
    }
}
