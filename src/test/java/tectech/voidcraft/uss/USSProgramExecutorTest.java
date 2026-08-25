package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

/**
 * Phase B: the program executor against the fake context — sequencing, control flow, pacing, the command
 * lifecycles, failure-skip, program-end-holds, STOP, and the NBT cursor (resume mid-program).
 */
final class USSProgramExecutorTest {

    // region helpers

    private static USSNode writeVar(int slot, String value) {
        NBTTagCompound p = new NBTTagCompound();
        p.setString(USSCommandWrite.PARAM_VALUE, value);
        p.setInteger(USSCommandWrite.PARAM_SLOT, slot);
        return USSNode.command(USSCommand.WRITE, p);
    }

    private static USSNode moveTo(String target) {
        NBTTagCompound p = new NBTTagCompound();
        p.setString(USSProgramDefaults.PARAM_TARGET, target);
        return USSNode.command(USSCommand.MOVE, p);
    }

    private static USSProgram program(USSNode... nodes) {
        return USSProgram.of(Arrays.asList(nodes));
    }

    /**
     * Tick until completed (or maxTicks) — returns the number of ticks consumed.
     */
    private static int runToCompletion(USSProgramExecutor executor, FakeUSSContext ctx, int maxTicks) {
        int t = 0;
        while (!executor.isCompleted() && t < maxTicks) {
            executor.tick(ctx);
            t++;
        }
        return t;
    }

    // endregion

    // region program end / basics

    @Test
    void testNullProgramCompletesImmediately() {
        USSProgramExecutor executor = USSProgramExecutor.start(null);
        assertTrue(executor.isCompleted());
    }

    @Test
    void testEmptyProgramCompletesImmediately() {
        USSProgramExecutor executor = USSProgramExecutor.start(USSProgram.empty());
        assertTrue(executor.isCompleted());
    }

    @Test
    void testSequencingRunsNodesInOrderAndCompletes() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor.start(program(writeVar(0, "a"), writeVar(1, "b")));
        int ticks = runToCompletion(executor, ctx, 1000);
        assertTrue(executor.isCompleted());
        assertEquals("a", ctx.vars.get(0));
        assertEquals("b", ctx.vars.get(1));
        assertEquals(40, ticks, "two immediate nodes = 2 node-steps × 20 ticks");
    }

    @Test
    void testOneNodeStepPer20Ticks() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(writeVar(0, "a"), writeVar(1, "b"), writeVar(2, "c")));
        int ticks = runToCompletion(executor, ctx, 1000);
        assertEquals(60, ticks, "decision #6: exactly one node step per 20 ticks (3 nodes = 60)");
    }

    @Test
    void testProgramEndHoldsWithNoImplicitHome() {
        // decision #2: finishing a program must NOT start any leg — the ship just holds
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor.start(program(writeVar(0, "done")));
        runToCompletion(executor, ctx, 1000);
        assertTrue(executor.isCompleted());
        assertEquals(0, ctx.travelLegs, "no implicit MOVE HOME");
        assertEquals(0, ctx.workLegs);
    }

    @Test
    void testNullContextTickIsSafe() {
        USSProgramExecutor executor = USSProgramExecutor.start(program(writeVar(0, "a")));
        for (int i = 0; i < 100; i++) {
            executor.tick(null);
        }
        assertFalse(executor.isCompleted()); // nothing ran — the context is the seam
    }

    // endregion

    // region IF / WHILE / REPEAT

    @Test
    void testIfTrueExecutesBodyThenContinues() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSNode ifTrue = USSNode.ifNode(
            USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.literal("1")),
            Arrays.asList(writeVar(0, "in-body")));
        USSProgramExecutor executor = USSProgramExecutor.start(program(ifTrue, writeVar(1, "after")));
        runToCompletion(executor, ctx, 1000);
        assertTrue(executor.isCompleted());
        assertEquals("in-body", ctx.vars.get(0));
        assertEquals("after", ctx.vars.get(1));
    }

    @Test
    void testIfFalseSkipsBodyButContinues() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSNode ifFalse = USSNode.ifNode(
            USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.literal("2")),
            Arrays.asList(writeVar(0, "in-body")));
        USSProgramExecutor executor = USSProgramExecutor.start(program(ifFalse, writeVar(1, "after")));
        runToCompletion(executor, ctx, 1000);
        assertTrue(executor.isCompleted());
        assertFalse(ctx.vars.isWritten(0), "the body must not run");
        assertEquals("after", ctx.vars.get(1));
    }

    @Test
    void testWhileRunsUntilConditionFalse() {
        // the world changes: CARGO_FREE decrements on every poll — the loop must stop exactly when it hits 0
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.autoDecrementCargoFree = true;
        ctx.cargoFree = 5;
        USSNode loop = USSNode.whileNode(
            USSCondition.of(USSValue.stat(USSShipStat.CARGO_FREE.getId()), USSConditionOp.GT, USSValue.literal("0")),
            Arrays.asList(writeVar(0, "loop")));
        USSProgramExecutor executor = USSProgramExecutor.start(program(loop));
        int ticks = runToCompletion(executor, ctx, 10000);
        assertTrue(executor.isCompleted());
        assertEquals(5, ctx.writeVarCalls, "one body-run per positive cargo-free reading");
        assertEquals(0, ctx.cargoFree);
        assertEquals(240, ticks);
    }

    @Test
    void testWhileTrueIsPacedAtLeastOneStepPerIteration() {
        // a WHILE(true) with an EMPTY body must not busy-loop: at most one node-step (20 ticks) per iteration
        FakeUSSContext ctx = new FakeUSSContext();
        USSNode loop = USSNode
            .whileNode(USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.literal("1")), null);
        USSProgramExecutor executor = USSProgramExecutor.start(program(loop));
        for (int i = 0; i < 200; i++) {
            executor.tick(ctx);
        }
        assertFalse(executor.isCompleted());
        assertEquals(20, ctx.resolveCalls, "200 ticks ÷ 20 = 10 iterations × 2 resolves — no tick-budget runaway");
    }

    @Test
    void testRepeatRunsBodyExactlyNtimes() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.repeat(3, Arrays.asList(writeVar(0, "r")))));
        int ticks = runToCompletion(executor, ctx, 10000);
        assertTrue(executor.isCompleted());
        assertEquals(3, ctx.writeVarCalls);
        assertEquals(140, ticks);
    }

    @Test
    void testRepeatZeroNeverRunsTheBody() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.repeat(0, Arrays.asList(writeVar(0, "r"))), writeVar(1, "after")));
        int ticks = runToCompletion(executor, ctx, 10000);
        assertTrue(executor.isCompleted());
        assertFalse(ctx.vars.isWritten(0), "REPEAT 0 — the body must never run (Phase A contract)");
        assertTrue(ctx.vars.isWritten(1), "the node after the REPEAT must run");
        assertEquals(1, ctx.writeVarCalls, "only the trailing write ran (the body write did not)");
        assertEquals(40, ticks);
    }

    @Test
    void testNestedWhileAndIf() {
        // WHILE cargo { IF always { write } } — nested control, terminated by the stat
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.autoDecrementCargoFree = true;
        ctx.cargoFree = 3;
        USSNode innerIf = USSNode.ifNode(
            USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.literal("1")),
            Arrays.asList(writeVar(0, "nested")));
        USSNode outerWhile = USSNode.whileNode(
            USSCondition.of(USSValue.stat(USSShipStat.CARGO_FREE.getId()), USSConditionOp.GT, USSValue.literal("0")),
            Arrays.asList(innerIf));
        USSProgramExecutor executor = USSProgramExecutor.start(program(outerWhile));
        runToCompletion(executor, ctx, 10000);
        assertTrue(executor.isCompleted());
        assertEquals(3, ctx.writeVarCalls);
    }

    @Test
    void testConditionReadsEarlierInstructionWrites() {
        // IF VAR0 EQ "hello" — the condition resolves a value an earlier instruction wrote
        FakeUSSContext ctx = new FakeUSSContext();
        USSNode yes = USSNode.ifNode(
            USSCondition.of(USSValue.variable(0), USSConditionOp.EQ, USSValue.literal("hello")),
            Arrays.asList(writeVar(1, "yes")));
        USSNode no = USSNode.ifNode(
            USSCondition.of(USSValue.variable(0), USSConditionOp.EQ, USSValue.literal("world")),
            Arrays.asList(writeVar(2, "no")));
        USSProgramExecutor executor = USSProgramExecutor.start(program(writeVar(0, "hello"), yes, no));
        runToCompletion(executor, ctx, 10000);
        assertTrue(executor.isCompleted());
        assertEquals("yes", ctx.vars.get(1));
        assertFalse(ctx.vars.isWritten(2));
    }

    // endregion

    // region MOVE / WORK lifecycles

    @Test
    void testMoveLifecycleThenNextInstruction() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.setTarget(USSProgramDefaults.TARGET_NEAREST_PLANET, 0, USSPosition.of(10, 0, 0));
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(moveTo(USSProgramDefaults.TARGET_NEAREST_PLANET), writeVar(0, "arrived")));
        int ticks = 0;
        while (!executor.isCompleted() && ticks < 1000) {
            if (ticks >= 39) {
                ctx.legComplete = true; // the leg (real time on the game side) finishes at tick 40
            }
            executor.tick(ctx);
            ticks++;
        }
        assertTrue(executor.isCompleted());
        assertEquals(1, ctx.travelLegs);
        assertEquals(10.0, ctx.lastLegDist, 0.0001);
        assertEquals("arrived", ctx.vars.get(0), "the instruction after MOVE runs on arrival");
        assertEquals(60, ticks);
    }

    @Test
    void testWorkRunsAtPositionThenContinues() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.command(USSCommand.WORK, new NBTTagCompound()), writeVar(0, "worked")));
        int ticks = 0;
        while (!executor.isCompleted() && ticks < 1000) {
            if (ticks >= 30) {
                ctx.legComplete = true; // the work leg finishes after 30 ticks
            }
            executor.tick(ctx);
            ticks++;
        }
        assertTrue(executor.isCompleted());
        assertEquals(1, ctx.workLegs);
        assertEquals("worked", ctx.vars.get(0));
    }

    // endregion

    // region failure → skip (decision #3)

    @Test
    void testMoveUnresolvableTargetSkipsToNextNode() {
        FakeUSSContext ctx = new FakeUSSContext(); // no STAR target registered
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(moveTo(USSProgramDefaults.TARGET_STAR), writeVar(0, "after")));
        runToCompletion(executor, ctx, 1000);
        assertTrue(executor.isCompleted());
        assertEquals(0, ctx.travelLegs);
        assertEquals("after", ctx.vars.get(0), "the failure is skipped, the program keeps going");
        assertTrue(ctx.loggedContains("unresolvable"));
    }

    @Test
    void testUnknownCommandIdSkipsToNextNode() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.command(99, new NBTTagCompound()), writeVar(0, "after")));
        runToCompletion(executor, ctx, 1000);
        assertTrue(executor.isCompleted());
        assertEquals("after", ctx.vars.get(0));
        assertTrue(ctx.loggedContains("unknown command 99"));
    }

    @Test
    void testWorkFailureSkips() {
        // a context that refuses legs — the instruction is skipped, not a crash, not a halt
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.startRefused = true;
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.command(USSCommand.WORK, new NBTTagCompound()), writeVar(0, "after")));
        runToCompletion(executor, ctx, 1000);
        assertTrue(executor.isCompleted());
        assertEquals("after", ctx.vars.get(0));
    }

    // endregion

    // region WRITE / READ / STOP

    @Test
    void testWriteThenReadBetweenSlots() {
        FakeUSSContext ctx = new FakeUSSContext();
        NBTTagCompound p = new NBTTagCompound();
        p.setInteger(USSCommandRead.PARAM_FROM, 5);
        p.setInteger(USSCommandRead.PARAM_TO, 9);
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(writeVar(5, "hello"), USSNode.command(USSCommand.READ, p)));
        runToCompletion(executor, ctx, 1000);
        assertTrue(executor.isCompleted());
        assertEquals("hello", ctx.vars.get(5));
        assertEquals("hello", ctx.vars.get(9));
    }

    @Test
    void testStopTerminatesTheRemainingProgram() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor.start(
            program(
                writeVar(0, "before"),
                USSNode.command(USSCommand.STOP, new NBTTagCompound()),
                writeVar(1, "never")));
        runToCompletion(executor, ctx, 1000);
        assertTrue(executor.isCompleted());
        assertEquals("before", ctx.vars.get(0));
        assertFalse(ctx.vars.isWritten(1), "nodes after STOP must not run");
    }

    // endregion

    // region NBT cursor (resume mid-program)

    @Test
    void testCursorSurvivesNbtRoundTripMidWhile() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.autoDecrementCargoFree = true;
        ctx.cargoFree = 5;
        USSNode loop = USSNode.whileNode(
            USSCondition.of(USSValue.stat(USSShipStat.CARGO_FREE.getId()), USSConditionOp.GT, USSValue.literal("0")),
            Arrays.asList(writeVar(0, "loop")));
        USSProgram program = program(loop);

        USSProgramExecutor executor = USSProgramExecutor.start(program);
        for (int i = 0; i < 80; i++) { // mid-loop: 2 of the 5 writes done
            executor.tick(ctx);
        }
        assertFalse(executor.isCompleted());
        assertEquals(2, ctx.writeVarCalls);

        // save / restore (a fresh executor from NBT — the game pilot's reload path)
        USSProgramExecutor restored = USSProgramExecutor.readFromNBT(executor.writeToNBT());
        assertFalse(restored.isCompleted());

        int more = runToCompletion(restored, ctx, 10000);
        assertTrue(restored.isCompleted());
        assertEquals(5, ctx.writeVarCalls, "the loop continued exactly where it left off (no re-run, no loss)");
        assertTrue(more > 0);
    }

    @Test
    void testCursorSurvivesNbtRoundTripMidWait() {
        FakeUSSContext ctx = new FakeUSSContext();
        NBTTagCompound p = new NBTTagCompound();
        p.setLong(USSCommandWait.PARAM_TICKS, 5L);
        USSProgram program = program(USSNode.command(USSCommand.WAIT, p), writeVar(0, "after-wait"));

        USSProgramExecutor executor = USSProgramExecutor.start(program);
        for (int i = 0; i < 20; i++) { // WAIT begin at tick 20, now in flight
            executor.tick(ctx);
        }
        assertTrue(executor.isActive());

        USSProgramExecutor restored = USSProgramExecutor.readFromNBT(executor.writeToNBT());
        assertTrue(restored.isActive(), "the in-flight WAIT must survive the round trip");

        int total = 20 + runToCompletion(restored, ctx, 10000);
        assertTrue(restored.isCompleted());
        assertEquals("after-wait", ctx.vars.get(0));
        assertEquals(45, total, "the wait was not restarted by the round trip");
    }

    @Test
    void testCursorSurvivesNbtRoundTripMidMove() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.setTarget(USSProgramDefaults.TARGET_NEAREST_PLANET, 0, USSPosition.of(10, 0, 0));
        USSProgram program = program(moveTo(USSProgramDefaults.TARGET_NEAREST_PLANET), writeVar(0, "arrived"));

        USSProgramExecutor executor = USSProgramExecutor.start(program);
        for (int i = 0; i < 20; i++) { // MOVE begin at tick 20, leg in flight
            executor.tick(ctx);
        }
        assertEquals(1, ctx.travelLegs);

        USSProgramExecutor restored = USSProgramExecutor.readFromNBT(executor.writeToNBT());
        assertTrue(restored.isActive());

        ctx.legComplete = true;
        runToCompletion(restored, ctx, 10000);
        assertTrue(restored.isCompleted());
        assertEquals(1, ctx.travelLegs, "the leg must not restart after the round trip");
        assertEquals("arrived", ctx.vars.get(0));
    }

    // endregion

    // region corrupt cursor → fail-safe

    @Test
    void testNullCursorReadsCompleted() {
        assertTrue(
            USSProgramExecutor.readFromNBT(null)
                .isCompleted());
    }

    @Test
    void testCorruptScopeEntryReadsCompleted() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("st", 0);
        NBTTagList scopes = new NBTTagList();
        scopes.appendTag(new net.minecraft.nbt.NBTTagByte((byte) 1)); // not a compound — corruption
        nbt.setTag("sc", scopes);
        assertTrue(
            USSProgramExecutor.readFromNBT(nbt)
                .isCompleted());
    }

    @Test
    void testUnknownScopeKindReadsCompleted() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("st", 0);
        NBTTagList scopes = new NBTTagList();
        NBTTagCompound scope = new NBTTagCompound();
        scope.setInteger("k", 9); // unknown kind
        scopes.appendTag(scope);
        nbt.setTag("sc", scopes);
        assertTrue(
            USSProgramExecutor.readFromNBT(nbt)
                .isCompleted());
    }

    @Test
    void testRunningWithoutScopesReadsCompleted() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("st", 0);
        assertTrue(
            USSProgramExecutor.readFromNBT(nbt)
                .isCompleted());
    }

    @Test
    void testCorruptActiveRecordReadsCompleted() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("st", 0);
        NBTTagList scopes = new NBTTagList();
        NBTTagCompound scope = new NBTTagCompound();
        scope.setInteger("k", 0); // ROOT with an empty body
        scopes.appendTag(scope);
        nbt.setTag("sc", scopes);
        NBTTagCompound active = new NBTTagCompound();
        active.setInteger("c", 4); // WAIT
        // but the node record is garbage (no type)
        NBTTagCompound node = new NBTTagCompound();
        node.setString("junk", "junk");
        active.setTag("n", node);
        nbt.setTag("act", active);
        assertTrue(
            USSProgramExecutor.readFromNBT(nbt)
                .isCompleted());
    }

    // endregion
}
