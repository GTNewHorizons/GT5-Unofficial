package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the COMMAND CAPABILITY SET ({@link USSCapabilities}): the bits, the command gating
 * (always-available commands need no bit), the preset requirements, and the universal / empty sentinels.
 */
public class USSCapabilitiesTest {

    @Test
    public void testBits() {
        assertEquals(1, USSCapabilities.MOVE);
        assertEquals(2, USSCapabilities.MINE);
        assertEquals(4, USSCapabilities.SCAN);
        assertEquals(8, USSCapabilities.SIPHON);
        assertEquals(16, USSCapabilities.CONSTRUCT);
        assertEquals(32, USSCapabilities.REPAIR);
        assertEquals(63, USSCapabilities.ALL);
        assertEquals(
            0,
            USSCapabilities.empty()
                .bits());
        assertEquals(
            USSCapabilities.ALL,
            USSCapabilities.universal()
                .bits());
        // out-of-range bits are masked away (a corrupt set can only shrink, never add)
        assertEquals(
            1,
            USSCapabilities.of(USSCapabilities.MOVE | 0x10000)
                .bits());
    }

    @Test
    public void testAllowsCommand() {
        USSCapabilities minerOnly = USSCapabilities.of(USSCapabilities.MOVE | USSCapabilities.MINE);
        assertTrue(minerOnly.allowsCommand(USSCommand.MOVE));
        assertTrue(minerOnly.allowsCommand(USSCommand.MINE));
        assertFalse(minerOnly.allowsCommand(USSCommand.SCAN));
        assertFalse(minerOnly.allowsCommand(USSCommand.SIPHON));
        assertFalse(minerOnly.allowsCommand(USSCommand.CONSTRUCT));
        assertFalse(minerOnly.allowsCommand(USSCommand.REPAIR));
        // the always-available commands need no bit (the empty set still allows them)
        for (int id : new int[] { USSCommand.WRITE, USSCommand.READ, USSCommand.WAIT, USSCommand.STOP }) {
            assertTrue(
                USSCapabilities.empty()
                    .allowsCommand(id),
                "command " + USSCommand.label(id));
            assertTrue(minerOnly.allowsCommand(id));
        }
        // an unknown id is allowed at authoring time (the executor SKIPs it at run time)
        assertTrue(minerOnly.allowsCommand(99));
        // the universal set allows everything
        for (int id = 0; id <= USSCommand.MAX_ID; id++) {
            assertTrue(
                USSCapabilities.universal()
                    .allowsCommand(id),
                "command " + USSCommand.label(id));
        }
    }

    @Test
    public void testPresetRequirements() {
        assertEquals(USSCapabilities.MINE, USSCapabilities.presetRequirement("miner"));
        assertEquals(USSCapabilities.SIPHON, USSCapabilities.presetRequirement("starlifter"));
        assertEquals(USSCapabilities.SCAN, USSCapabilities.presetRequirement("explorer"));
        assertEquals(USSCapabilities.CONSTRUCT, USSCapabilities.presetRequirement("constructor"));
        assertEquals(0, USSCapabilities.presetRequirement("clear"));
        assertEquals(0, USSCapabilities.presetRequirement(null));
        assertEquals(0, USSCapabilities.presetRequirement("unknown"));

        USSCapabilities minerOnly = USSCapabilities.of(USSCapabilities.MOVE | USSCapabilities.MINE);
        assertTrue(minerOnly.allowsPreset("miner"));
        assertTrue(minerOnly.allowsPreset("clear"), "clear needs no capability");
        assertFalse(minerOnly.allowsPreset("starlifter"));
        assertFalse(minerOnly.allowsPreset("explorer"));
        assertFalse(minerOnly.allowsPreset("constructor"));
        // even the EMPTY set allows the capability-free preset
        assertTrue(
            USSCapabilities.empty()
                .allowsPreset("clear"));
        assertFalse(
            USSCapabilities.empty()
                .allowsPreset("miner"));
    }

    @Test
    public void testWorkKindHelpers() {
        // the work kinds (see USSWorkKind): TRAVEL is the non-work leg
        assertEquals(0, USSWorkKind.TRAVEL);
        assertEquals(1, USSWorkKind.MINE);
        assertEquals(2, USSWorkKind.SCAN);
        assertEquals(3, USSWorkKind.SIPHON);
        assertFalse(USSWorkKind.isWork(USSWorkKind.TRAVEL));
        assertTrue(USSWorkKind.isWork(USSWorkKind.MINE));
        assertTrue(USSWorkKind.isWork(USSWorkKind.SCAN));
        assertTrue(USSWorkKind.isWork(USSWorkKind.SIPHON));
        // the command → kind mapping (a work command owns its kind)
        assertEquals(USSWorkKind.MINE, USSWorkKind.fromCommand(USSCommand.MINE));
        assertEquals(USSWorkKind.SCAN, USSWorkKind.fromCommand(USSCommand.SCAN));
        assertEquals(USSWorkKind.SIPHON, USSWorkKind.fromCommand(USSCommand.SIPHON));
        assertEquals(USSWorkKind.TRAVEL, USSWorkKind.fromCommand(USSCommand.MOVE));
        assertEquals(USSWorkKind.TRAVEL, USSWorkKind.fromCommand(99));
        // the kind names (logs + base-pilot messages)
        assertEquals("MINE", USSWorkKind.name(USSWorkKind.MINE));
        assertEquals("SCAN", USSWorkKind.name(USSWorkKind.SCAN));
        assertEquals("SIPHON", USSWorkKind.name(USSWorkKind.SIPHON));
        assertEquals("WORK", USSWorkKind.name(USSWorkKind.TRAVEL));
    }

    @Test
    public void testCommandLabels() {
        // the palette labels: every built-in gets a readable name (the view's fallback is CMD<id>)
        assertEquals("MOVE", USSCommand.label(USSCommand.MOVE));
        assertEquals("MINE", USSCommand.label(USSCommand.MINE));
        assertEquals("WRITE", USSCommand.label(USSCommand.WRITE));
        assertEquals("READ", USSCommand.label(USSCommand.READ));
        assertEquals("WAIT", USSCommand.label(USSCommand.WAIT));
        assertEquals("STOP", USSCommand.label(USSCommand.STOP));
        assertEquals("CONSTRUCT", USSCommand.label(USSCommand.CONSTRUCT));
        assertEquals("REPAIR", USSCommand.label(USSCommand.REPAIR));
        assertEquals("SCAN", USSCommand.label(USSCommand.SCAN));
        assertEquals("SIPHON", USSCommand.label(USSCommand.SIPHON));
        assertEquals("CMD13", USSCommand.label(13));
    }

    @Test
    public void testStarliftYieldIsSiphonPowered() {
        // the starlifter yield scales with SIPHON power (not mining power — the command split): the plasma
        // amount = siphonPower * the plasma factor, capped
        assertEquals(
            USSConstants.STARLIFTER_PLASMA_FACTOR,
            USSConstants.starlifterPlasmaAmount(1L),
            "1 siphon power → one factor of plasma");
        assertEquals(
            40L * USSConstants.STARLIFTER_PLASMA_FACTOR,
            USSConstants.starlifterPlasmaAmount(40L),
            "one star siphon (40) → forty factors of plasma");
        assertEquals(
            USSConstants.STARLIFTER_PLASMA_CAP,
            USSConstants.starlifterPlasmaAmount(10_000_000L),
            "above the cap the plasma stays capped");
        assertEquals(
            USSConstants.STARLIFTER_PLASMA_FACTOR,
            USSConstants.starlifterPlasmaAmount(0L),
            "no siphon power still ships one factor (the power floors at 1)");
        // the matter (dwarf dust) amount rides the same siphon power
        assertEquals(USSConstants.minerOreAmount(40L), USSConstants.starlifterMatterAmount(40L));
    }
}
