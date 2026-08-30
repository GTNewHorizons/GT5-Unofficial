package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * The built-in command handlers against the fake context.
 */
final class USSCommandTest {

    private static USSNode command(int id, NBTTagCompound params) {
        return USSNode.command(id, params);
    }

    private static NBTTagCompound writeParams(String value, int slot) {
        NBTTagCompound p = new NBTTagCompound();
        p.setString(USSCommandWrite.PARAM_VALUE, value);
        p.setInteger(USSCommandWrite.PARAM_SLOT, slot);
        return p;
    }

    // region MOVE

    @Test
    void testMoveWithoutTargetFails() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSCommandMove move = new USSCommandMove();
        assertEquals(
            USSCommandStatus.FAILED,
            move.begin(ctx, command(USSCommand.MOVE, new NBTTagCompound()), new NBTTagCompound()));
        assertTrue(ctx.loggedContains("missing target"));
    }

    @Test
    void testMoveUnresolvableTargetFails() {
        FakeUSSContext ctx = new FakeUSSContext();
        NBTTagCompound p = new NBTTagCompound();
        p.setString(USSProgramDefaults.PARAM_TARGET, USSProgramDefaults.TARGET_STAR);
        USSCommandMove move = new USSCommandMove();
        assertEquals(USSCommandStatus.FAILED, move.begin(ctx, command(USSCommand.MOVE, p), new NBTTagCompound()));
        assertTrue(ctx.loggedContains("unresolvable"));
        assertEquals(0, ctx.travelLegs);
    }

    @Test
    void testMoveStartsTravelLegThenDoneOnArrival() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.setTarget(USSProgramDefaults.TARGET_NEAREST_PLANET, 0, USSPosition.of(3, 4, 0));
        NBTTagCompound p = new NBTTagCompound();
        p.setString(USSProgramDefaults.PARAM_TARGET, USSProgramDefaults.TARGET_NEAREST_PLANET);
        NBTTagCompound state = new NBTTagCompound();
        USSCommandMove move = new USSCommandMove();

        assertEquals(USSCommandStatus.RUNNING, move.begin(ctx, command(USSCommand.MOVE, p), state));
        assertEquals(1, ctx.travelLegs);
        assertEquals(USSPosition.of(3, 4, 0), ctx.lastLegDest);
        assertEquals(5.0, ctx.lastLegDist, 0.0001); // 3-4-5 triangle
        assertEquals(USSWorkKind.TRAVEL, ctx.lastLegWorkKind);

        assertEquals(USSCommandStatus.RUNNING, move.tick(ctx, null, state)); // still flying
        ctx.legComplete = true;
        assertEquals(USSCommandStatus.DONE, move.tick(ctx, null, state));
    }

    @Test
    void testMoveWithIndexUsesIt() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.setTarget(USSProgramDefaults.TARGET_PLANET, 2, USSPosition.of(10, 0, 0));
        NBTTagCompound p = new NBTTagCompound();
        p.setString(USSProgramDefaults.PARAM_TARGET, USSProgramDefaults.TARGET_PLANET);
        p.setInteger(USSProgramDefaults.PARAM_INDEX, 2);
        USSCommandMove move = new USSCommandMove();
        assertEquals(USSCommandStatus.RUNNING, move.begin(ctx, command(USSCommand.MOVE, p), new NBTTagCompound()));
        assertEquals(1, ctx.travelLegs);
    }

    // endregion

    // region MINE / SCAN / SIPHON (the work commands)

    @Test
    void testMineStartsWorkLegAtPositionThenDone() {
        assertWorkCommand(new USSCommandMine(), USSCommand.MINE, USSWorkKind.MINE);
    }

    @Test
    void testScanStartsWorkLegAtPositionThenDone() {
        assertWorkCommand(new USSCommandScan(), USSCommand.SCAN, USSWorkKind.SCAN);
    }

    @Test
    void testSiphonStartsWorkLegAtPositionThenDone() {
        assertWorkCommand(new USSCommandSiphon(), USSCommand.SIPHON, USSWorkKind.SIPHON);
    }

    /** A work command starts its KIND's leg at the ship's position (distance 0) and completes when the leg ends. */
    private static void assertWorkCommand(USSCommandHandler work, int commandId, int workKind) {
        FakeUSSContext ctx = new FakeUSSContext();
        assertEquals(
            USSCommandStatus.RUNNING,
            work.begin(ctx, command(commandId, new NBTTagCompound()), new NBTTagCompound()));
        assertEquals(1, ctx.workLegs);
        assertEquals(workKind, ctx.lastLegWorkKind);
        assertEquals(USSPosition.zero(), ctx.lastLegDest);
        assertEquals(0.0, ctx.lastLegDist, 0.0001);
        assertEquals(USSCommandStatus.RUNNING, work.tick(ctx, null, new NBTTagCompound()));
        ctx.legComplete = true;
        assertEquals(USSCommandStatus.DONE, work.tick(ctx, null, new NBTTagCompound()));
    }

    // endregion

    // region WRITE / READ

    @Test
    void testWritePlainStringValue() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSCommandWrite write = new USSCommandWrite();
        assertEquals(
            USSCommandStatus.DONE,
            write.begin(ctx, command(USSCommand.WRITE, writeParams("hello", 7)), new NBTTagCompound()));
        assertEquals("hello", ctx.vars.get(7));
    }

    @Test
    void testWriteValueAsVariableReference() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.vars = ctx.vars.set(3, "sourced");
        NBTTagCompound p = new NBTTagCompound();
        p.setTag(
            USSCommandWrite.PARAM_VALUE,
            USSValue.variable(3)
                .writeToNBT());
        p.setInteger(USSCommandWrite.PARAM_SLOT, 9);
        USSCommandWrite write = new USSCommandWrite();
        assertEquals(USSCommandStatus.DONE, write.begin(ctx, command(USSCommand.WRITE, p), new NBTTagCompound()));
        assertEquals("sourced", ctx.vars.get(9));
    }

    @Test
    void testWriteLocationResolvesToTheCurrentPosition() {
        // the LOCATION value is resolved at execution time to the ship's current position (the coordinate
        // string) — not the leg's destination, not a stored literal
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.position = USSPosition.of(12.5, -2.0, 40.0);
        NBTTagCompound p = new NBTTagCompound();
        p.setTag(
            USSCommandWrite.PARAM_VALUE,
            USSValue.location()
                .writeToNBT());
        p.setInteger(USSCommandWrite.PARAM_SLOT, 4);
        USSCommandWrite write = new USSCommandWrite();
        assertEquals(USSCommandStatus.DONE, write.begin(ctx, command(USSCommand.WRITE, p), new NBTTagCompound()));
        assertEquals("12.5;-2.0;40.0", ctx.vars.get(4));
        ctx.position = USSPosition.of(99.0, 1.0, 99.0);
        assertEquals(USSCommandStatus.DONE, write.begin(ctx, command(USSCommand.WRITE, p), new NBTTagCompound()));
        assertEquals("99.0;1.0;99.0", ctx.vars.get(4), "a later run broadcasts the position of that run");
    }

    @Test
    void testWriteMissingSlotDefaultsToZero() {
        FakeUSSContext ctx = new FakeUSSContext();
        NBTTagCompound p = new NBTTagCompound();
        p.setString(USSCommandWrite.PARAM_VALUE, "slotless");
        USSCommandWrite write = new USSCommandWrite();
        write.begin(ctx, command(USSCommand.WRITE, p), new NBTTagCompound());
        assertEquals("slotless", ctx.vars.get(0));
    }

    @Test
    void testReadCopiesSlotToSlot() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.vars = ctx.vars.set(5, "data");
        NBTTagCompound p = new NBTTagCompound();
        p.setInteger(USSCommandRead.PARAM_FROM, 5);
        p.setInteger(USSCommandRead.PARAM_TO, 11);
        USSCommandRead read = new USSCommandRead();
        assertEquals(USSCommandStatus.DONE, read.begin(ctx, command(USSCommand.READ, p), new NBTTagCompound()));
        assertEquals("data", ctx.vars.get(11));
        assertEquals("data", ctx.vars.get(5)); // source untouched
    }

    // endregion

    // region WAIT

    @Test
    void testWaitZeroIsImmediate() {
        FakeUSSContext ctx = new FakeUSSContext();
        NBTTagCompound p = new NBTTagCompound();
        p.setLong(USSCommandWait.PARAM_TICKS, 0L);
        assertEquals(
            USSCommandStatus.DONE,
            new USSCommandWait().begin(ctx, command(USSCommand.WAIT, p), new NBTTagCompound()));
    }

    @Test
    void testWaitCountsDownOnePerTick() {
        FakeUSSContext ctx = new FakeUSSContext();
        NBTTagCompound p = new NBTTagCompound();
        p.setLong(USSCommandWait.PARAM_TICKS, 5L);
        USSCommandWait wait = new USSCommandWait();
        NBTTagCompound state = new NBTTagCompound();
        assertEquals(USSCommandStatus.RUNNING, wait.begin(ctx, command(USSCommand.WAIT, p), state));
        assertEquals(5L, state.getLong(USSCommandWait.STATE_REMAINING));
        for (int i = 0; i < 4; i++) {
            assertEquals(USSCommandStatus.RUNNING, wait.tick(ctx, null, state), "tick " + (i + 1) + " of 5");
        }
        assertEquals(USSCommandStatus.DONE, wait.tick(ctx, null, state), "the 5th tick completes the wait");
    }

    @Test
    void testWaitClampsGarbageDuration() {
        FakeUSSContext ctx = new FakeUSSContext();
        NBTTagCompound p = new NBTTagCompound();
        p.setLong(USSCommandWait.PARAM_TICKS, Long.MAX_VALUE);
        USSCommandWait wait = new USSCommandWait();
        NBTTagCompound state = new NBTTagCompound();
        assertEquals(USSCommandStatus.RUNNING, wait.begin(ctx, command(USSCommand.WAIT, p), state));
        assertEquals(USSCommandWait.MAX_WAIT_TICKS, state.getLong(USSCommandWait.STATE_REMAINING));
    }

    // region CONSTRUCT / REPAIR (the Voidbase construction framework)

    @Test
    void testConstructRunsUntilTheWorldReportsDone() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.constructStartResult = true;
        ctx.constructTickResult = true;
        USSCommandConstruct construct = new USSCommandConstruct();
        assertEquals(
            USSCommandStatus.RUNNING,
            construct.begin(ctx, command(USSCommand.CONSTRUCT, new NBTTagCompound()), new NBTTagCompound()));
        assertEquals(1, ctx.constructStartCalls);
        assertEquals(USSCommandStatus.RUNNING, construct.tick(ctx, null, new NBTTagCompound()));
        assertEquals(1, ctx.constructTickCalls);
        ctx.constructTickResult = false;
        assertEquals(USSCommandStatus.DONE, construct.tick(ctx, null, new NBTTagCompound()));
        assertEquals(2, ctx.constructTickCalls);
    }

    @Test
    void testConstructFailsWhenTheWorldRefuses() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.constructStartResult = false;
        USSCommandConstruct construct = new USSCommandConstruct();
        assertEquals(
            USSCommandStatus.FAILED,
            construct.begin(ctx, command(USSCommand.CONSTRUCT, new NBTTagCompound()), new NBTTagCompound()));
        assertEquals(1, ctx.constructStartCalls);
        assertTrue(ctx.loggedContains("CONSTRUCT"));
        assertEquals(0, ctx.constructTickCalls);
    }

    @Test
    void testRepairRunsUntilTheWorldReportsFull() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.repairStartResult = true;
        ctx.repairTickResult = true;
        USSCommandRepair repair = new USSCommandRepair();
        assertEquals(
            USSCommandStatus.RUNNING,
            repair.begin(ctx, command(USSCommand.REPAIR, new NBTTagCompound()), new NBTTagCompound()));
        assertEquals(1, ctx.repairStartCalls);
        assertEquals(USSCommandStatus.RUNNING, repair.tick(ctx, null, new NBTTagCompound()));
        assertEquals(1, ctx.repairTickCalls);
        ctx.repairTickResult = false;
        assertEquals(USSCommandStatus.DONE, repair.tick(ctx, null, new NBTTagCompound()));
        assertEquals(2, ctx.repairTickCalls);
    }

    @Test
    void testRepairFailsWhenNothingIsRepairable() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.repairStartResult = false;
        USSCommandRepair repair = new USSCommandRepair();
        assertEquals(
            USSCommandStatus.FAILED,
            repair.begin(ctx, command(USSCommand.REPAIR, new NBTTagCompound()), new NBTTagCompound()));
        assertTrue(ctx.loggedContains("REPAIR"));
        assertEquals(0, ctx.repairTickCalls);
    }

    @Test
    void testRepairPassesTheTargetParamThrough() {
        // The command never resolves the target — the raw param goes to the world (empty = SELF, the world's
        // default; a name / index is resolved with the SEND / TAKE rules).
        FakeUSSContext ctx = new FakeUSSContext();
        USSCommandRepair repair = new USSCommandRepair();
        assertEquals(
            USSCommandStatus.RUNNING,
            repair.begin(ctx, command(USSCommand.REPAIR, new NBTTagCompound()), new NBTTagCompound()));
        assertEquals("", ctx.repairStartTarget, "no param → empty (the world reads that as SELF)");

        NBTTagCompound self = new NBTTagCompound();
        self.setString(USSProgramDefaults.PARAM_TARGET, USSCommandRepair.TARGET_SELF);
        assertEquals(
            USSCommandStatus.RUNNING,
            repair.begin(ctx, command(USSCommand.REPAIR, self), new NBTTagCompound()));
        assertEquals(USSCommandRepair.TARGET_SELF, ctx.repairStartTarget, "SELF passes through untouched");

        NBTTagCompound fleet = new NBTTagCompound();
        fleet.setString(USSProgramDefaults.PARAM_TARGET, "Siphon-1");
        repair.begin(ctx, command(USSCommand.REPAIR, fleet), new NBTTagCompound());
        assertEquals(
            "Siphon-1",
            ctx.repairStartTarget,
            "a fleet member name / index passes through (the world resolves it)");
    }

    // endregion

    // region SEND / TAKE (the cargo transfer framework)

    @Test
    void testTransferPassesTheRawParamsThrough() {
        // The command never resolves the target — the raw param (a fleet index, a ship name, or NEARBY) goes to
        // the world verbatim (the MTE resolves it, including the NEARBY candidate scan).
        assertTransferPassThrough(new USSCommandSend(), USSCommand.SEND);
        assertTransferPassThrough(new USSCommandTake(), USSCommand.TAKE);
    }

    /** begin hands the world the raw target / amount / filter and polls tick until it reports the transfer over. */
    private static void assertTransferPassThrough(USSCommandHandler handler, int commandId) {
        FakeUSSContext ctx = new FakeUSSContext();
        NBTTagCompound p = new NBTTagCompound();
        p.setString(USSProgramDefaults.PARAM_TARGET, USSProgramDefaults.TARGET_NEARBY);
        p.setLong(USSProgramDefaults.PARAM_AMOUNT, 10L);
        p.setString(USSProgramDefaults.PARAM_FILTER, "iron");
        assertEquals(USSCommandStatus.RUNNING, handler.begin(ctx, command(commandId, p), new NBTTagCompound()));
        assertEquals(1, ctx.transferStartCalls);
        assertEquals(commandId, ctx.transferStartCommandId);
        assertEquals(USSProgramDefaults.TARGET_NEARBY, ctx.transferStartTarget, "the target passes through verbatim");
        assertEquals(10L, ctx.transferStartAmount);
        assertEquals("iron", ctx.transferStartFilter);
        assertEquals(USSCommandStatus.RUNNING, handler.tick(ctx, null, new NBTTagCompound()));
        assertEquals(1, ctx.transferTickCalls);
        ctx.transferTickResult = false;
        assertEquals(USSCommandStatus.DONE, handler.tick(ctx, null, new NBTTagCompound()));
        assertEquals(2, ctx.transferTickCalls);
    }

    @Test
    void testTransferFailsWhenTheWorldRefuses() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.transferStartResult = false;
        assertEquals(
            USSCommandStatus.FAILED,
            new USSCommandSend().begin(ctx, command(USSCommand.SEND, transferParams()), new NBTTagCompound()));
        assertEquals(1, ctx.transferStartCalls);
        assertTrue(ctx.loggedContains("SEND"));
        assertEquals(0, ctx.transferTickCalls);
    }

    @Test
    void testTransferMissingTargetFailsWithoutAskingTheWorld() {
        for (int commandId : new int[] { USSCommand.SEND, USSCommand.TAKE }) {
            USSCommandHandler handler = commandId == USSCommand.SEND ? new USSCommandSend() : new USSCommandTake();
            FakeUSSContext ctx = new FakeUSSContext();
            assertEquals(
                USSCommandStatus.FAILED,
                handler.begin(ctx, command(commandId, new NBTTagCompound()), new NBTTagCompound()));
            assertEquals(0, ctx.transferStartCalls);
            assertTrue(ctx.loggedContains("missing target"));
        }
    }

    private static NBTTagCompound transferParams() {
        NBTTagCompound p = new NBTTagCompound();
        p.setString(USSProgramDefaults.PARAM_TARGET, USSProgramDefaults.TARGET_NEARBY);
        return p;
    }

    // endregion

    // region STOP / registry

    @Test
    void testStopTerminates() {
        assertEquals(
            USSCommandStatus.STOP,
            new USSCommandStop()
                .begin(new FakeUSSContext(), command(USSCommand.STOP, new NBTTagCompound()), new NBTTagCompound()));
    }

    @Test
    void testRegistryHasAllBuiltIns() {
        assertTrue(USSCommandRegistry.has(USSCommand.MOVE));
        assertTrue(USSCommandRegistry.has(USSCommand.MINE));
        assertTrue(USSCommandRegistry.has(USSCommand.WRITE));
        assertTrue(USSCommandRegistry.has(USSCommand.READ));
        assertTrue(USSCommandRegistry.has(USSCommand.WAIT));
        assertTrue(USSCommandRegistry.has(USSCommand.STOP));
        assertTrue(USSCommandRegistry.has(USSCommand.CONSTRUCT));
        assertTrue(USSCommandRegistry.has(USSCommand.REPAIR));
        assertTrue(USSCommandRegistry.has(USSCommand.SCAN));
        assertTrue(USSCommandRegistry.has(USSCommand.SIPHON));
        assertSame(
            USSCommand.MOVE,
            USSCommandRegistry.handler(USSCommand.MOVE)
                .commandId());
        assertNull(USSCommandRegistry.handler(99));
        assertFalse(USSCommandRegistry.has(99));
    }

    // endregion
}
