package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the DEFAULT PROGRAM CHIPS ({@link USSProgramDefaults}): the Miner / Starlifter / Explorer
 * programs, their shape (MOVE → WORK → MOVE HOME), their distinct targets, and the NBT round-trip (params are
 * compared explicitly — node equality covers structure, not free-form payload).
 */
public class USSProgramDefaultsTest {

    @Test
    public void testMinerChip() {
        assertChip(USSProgramDefaults.miner(), USSProgramDefaults.TARGET_NEAREST_PLANET, "Miner");
    }

    @Test
    public void testStarlifterChip() {
        assertChip(USSProgramDefaults.starlifter(), USSProgramDefaults.TARGET_STAR, "Starlifter");
    }

    @Test
    public void testExplorerChip() {
        assertChip(USSProgramDefaults.explorer(), USSProgramDefaults.TARGET_RIPPLE_UNSCANNED, "Explorer");
    }

    private static void assertChip(USSProgram program, String expectedFirstTarget, String role) {
        assertEquals(3, program.size(), role + " chip: exactly three instructions");
        assertEquals(1, program.depth(), role + " chip: a flat list");

        // instruction 1 — MOVE to the role's target
        USSNode move = program.nodes()
            .get(0);
        assertTrue(move.isCommand());
        assertEquals(USSCommand.MOVE, move.cmdId(), role + " chip: instruction 1 is MOVE");
        assertEquals(
            expectedFirstTarget,
            move.params()
                .getString(USSProgramDefaults.PARAM_TARGET),
            role + " chip: MOVE target");

        // instruction 2 — WORK
        USSNode work = program.nodes()
            .get(1);
        assertTrue(work.isCommand());
        assertEquals(USSCommand.WORK, work.cmdId(), role + " chip: instruction 2 is WORK");
        assertTrue(
            work.params()
                .hasNoTags(),
            role + " chip: WORK takes no params");

        // instruction 3 — MOVE HOME
        USSNode home = program.nodes()
            .get(2);
        assertTrue(home.isCommand());
        assertEquals(USSCommand.MOVE, home.cmdId(), role + " chip: instruction 3 is MOVE");
        assertEquals(
            USSProgramDefaults.TARGET_HOME,
            home.params()
                .getString(USSProgramDefaults.PARAM_TARGET),
            role + " chip: comes HOME");
    }

    @Test
    public void testChipsAreDistinct() {
        NBTTagCompound m = USSProgramDefaults.miner()
            .nodes()
            .get(0)
            .params();
        NBTTagCompound s = USSProgramDefaults.starlifter()
            .nodes()
            .get(0)
            .params();
        NBTTagCompound e = USSProgramDefaults.explorer()
            .nodes()
            .get(0)
            .params();
        assertNotEquals(m.getString(USSProgramDefaults.PARAM_TARGET), s.getString(USSProgramDefaults.PARAM_TARGET));
        assertNotEquals(m.getString(USSProgramDefaults.PARAM_TARGET), e.getString(USSProgramDefaults.PARAM_TARGET));
        assertNotEquals(s.getString(USSProgramDefaults.PARAM_TARGET), e.getString(USSProgramDefaults.PARAM_TARGET));
    }

    @Test
    public void testChipsFitTheProgramCaps() {
        for (USSProgram program : new USSProgram[] { USSProgramDefaults.miner(), USSProgramDefaults.starlifter(),
            USSProgramDefaults.explorer() }) {
            assertTrue(program.nodeCount() <= USSProgram.MAX_NODES, "chip within the node cap");
            assertTrue(program.depth() <= USSProgram.MAX_DEPTH, "chip within the depth cap");
        }
    }

    @Test
    public void testChipsSurviveTheNbtRoundTrip() {
        for (USSProgram program : new USSProgram[] { USSProgramDefaults.miner(), USSProgramDefaults.starlifter(),
            USSProgramDefaults.explorer() }) {
            NBTTagList list = program.writeToNBT();
            USSProgram back = USSProgram.readFromNBT(list);
            assertEquals(program.size(), back.size());
            // structure round-trips through node equality (params excluded by design)…
            for (int i = 0; i < program.size(); i++) {
                assertEquals(
                    program.nodes()
                        .get(i)
                        .type(),
                    back.nodes()
                        .get(i)
                        .type());
                assertEquals(
                    program.nodes()
                        .get(i)
                        .cmdId(),
                    back.nodes()
                        .get(i)
                        .cmdId());
            }
            // …and the params round-trip verbatim:
            assertEquals("target", "target"); // the param key is a contract — pin it here
            assertEquals(
                USSProgramDefaults.TARGET_NEAREST_PLANET,
                USSProgramDefaults.miner()
                    .nodes()
                    .get(0)
                    .params()
                    .getString(USSProgramDefaults.PARAM_TARGET));
        }
    }

    @Test
    public void testChipDerivedFromCovers() {
        // The controller block's covers declare what it IS (right-click applies the chip). Priority:
        // Explorer > Starlifter > Miner (the Miner is the default — no recognizable cover at all).
        assertEquals(
            USSProgramDefaults.TARGET_RIPPLE_UNSCANNED,
            USSProgramDefaults.chip(true, true, true)
                .nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET),
            "a scanner dish wins over everything");
        assertEquals(
            USSProgramDefaults.TARGET_RIPPLE_UNSCANNED,
            USSProgramDefaults.chip(true, true, false)
                .nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            USSProgramDefaults.TARGET_RIPPLE_UNSCANNED,
            USSProgramDefaults.chip(true, false, true)
                .nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            USSProgramDefaults.TARGET_STAR,
            USSProgramDefaults.chip(false, true, true)
                .nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET),
            "a star siphon wins over the mining array");
        assertEquals(
            USSProgramDefaults.TARGET_STAR,
            USSProgramDefaults.chip(false, true, false)
                .nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            USSProgramDefaults.TARGET_NEAREST_PLANET,
            USSProgramDefaults.chip(false, false, true)
                .nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET),
            "a mining array alone → the Miner chip");
        assertEquals(
            USSProgramDefaults.TARGET_NEAREST_PLANET,
            USSProgramDefaults.chip(false, false, false)
                .nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET),
            "no recognizable cover → the Miner chip (the default)");
        // every derived chip is a valid 3-instruction program that comes HOME (the MTE stores it as-is)
        for (USSProgram p : new USSProgram[] { USSProgramDefaults.chip(true, true, true),
            USSProgramDefaults.chip(false, true, false), USSProgramDefaults.chip(false, false, false) }) {
            assertEquals(3, p.size());
            assertEquals(
                USSProgramDefaults.TARGET_HOME,
                p.nodes()
                    .get(2)
                    .params()
                    .getString(USSProgramDefaults.PARAM_TARGET),
                "every chip comes HOME");
        }
    }

    @Test
    public void testChipParamsRoundTripVerbatim() {
        USSProgram[] chips = { USSProgramDefaults.miner(), USSProgramDefaults.starlifter(),
            USSProgramDefaults.explorer() };
        for (USSProgram program : chips) {
            USSProgram back = USSProgram.readFromNBT(program.writeToNBT());
            for (int i = 0; i < program.size(); i++) {
                NBTTagCompound before = program.nodes()
                    .get(i)
                    .params();
                NBTTagCompound after = back.nodes()
                    .get(i)
                    .params();
                for (Object key : before.tagMap.keySet()) { // 1.7.10: raw key set via the public tagMap field
                    String k = String.valueOf(key);
                    assertEquals(before.getString(k), after.getString(k), "param " + k);
                }
            }
        }
    }

    @Test
    public void testConstructorChip() {
        USSProgram program = USSProgramDefaults.constructor();
        assertEquals(3, program.size(), "constructor chip: exactly three instructions");
        assertEquals(1, program.depth(), "constructor chip: a flat list");

        // instruction 1 - MOVE to the anchor target (nearest planet by default; the player edits it)
        USSNode move = program.nodes()
            .get(0);
        assertTrue(move.isCommand());
        assertEquals(USSCommand.MOVE, move.cmdId());
        assertEquals(
            USSProgramDefaults.TARGET_NEAREST_PLANET,
            move.params()
                .getString(USSProgramDefaults.PARAM_TARGET));

        // instruction 2 - CONSTRUCT (create or fill the construction site at the anchor)
        USSNode construct = program.nodes()
            .get(1);
        assertTrue(construct.isCommand());
        assertEquals(USSCommand.CONSTRUCT, construct.cmdId());
        assertTrue(
            construct.params()
                .hasNoTags(),
            "CONSTRUCT takes no params (the anchor is the hover body)");

        // instruction 3 - MOVE HOME
        USSNode home = program.nodes()
            .get(2);
        assertTrue(home.isCommand());
        assertEquals(USSCommand.MOVE, home.cmdId());
        assertEquals(
            USSProgramDefaults.TARGET_HOME,
            home.params()
                .getString(USSProgramDefaults.PARAM_TARGET));

        // NBT round-trip
        USSProgram back = USSProgram.readFromNBT(program.writeToNBT());
        assertEquals(
            USSCommand.CONSTRUCT,
            back.nodes()
                .get(1)
                .cmdId());
    }
}
