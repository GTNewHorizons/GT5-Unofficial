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
 * lifecycles, failure-skip, the invisible-while wrap (a finished program runs again; only STOP ends it), and
 * the NBT cursor (resume mid-program).
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

    /** Tick a fixed number of times (for programs that never end on their own — the wrap tests). */
    private static void runTicks(USSProgramExecutor executor, FakeUSSContext ctx, int ticks) {
        for (int i = 0; i < ticks; i++) {
            executor.tick(ctx);
        }
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
    void testSequencingRunsNodesInOrderThenWraps() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor.start(program(writeVar(0, "a"), writeVar(1, "b")));
        runTicks(executor, ctx, 40); // first loop: both nodes
        assertEquals("a", ctx.vars.get(0));
        assertEquals("b", ctx.vars.get(1));
        assertEquals(2, ctx.writeVarCalls);
        assertFalse(executor.isCompleted(), "a finished program does not end — it wraps");
        runTicks(executor, ctx, 40); // second loop
        assertEquals(4, ctx.writeVarCalls, "the program restarted at its first node");
    }

    @Test
    void testOneNodeStepPer20Ticks() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(writeVar(0, "a"), writeVar(1, "b"), writeVar(2, "c")));
        runTicks(executor, ctx, 60);
        assertEquals(3, ctx.writeVarCalls, "decision #6: exactly one node step per 20 ticks (3 nodes = 60)");
        runTicks(executor, ctx, 60);
        assertEquals(6, ctx.writeVarCalls, "the wrap restarts the same one-step-per-second rhythm");
        assertFalse(executor.isCompleted());
    }

    @Test
    void testFinishedProgramWrapsWithNoImplicitLegs() {
        // the wrap: a finished program restarts at its first node — and starts no implicit leg
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor.start(program(writeVar(0, "done")));
        runTicks(executor, ctx, 40);
        assertEquals(2, ctx.writeVarCalls, "the single node ran twice (two loops — one node step per 20 ticks)");
        assertFalse(executor.isCompleted());
        assertEquals(0, ctx.travelLegs, "no implicit MOVE HOME at the wrap");
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
        runTicks(executor, ctx, 80);
        assertEquals("in-body", ctx.vars.get(0));
        assertEquals("after", ctx.vars.get(1));
        assertEquals(2, ctx.writeVarCalls, "one full loop (IF body + after)");
        assertFalse(executor.isCompleted(), "the program wraps, it does not end");
    }

    @Test
    void testIfFalseSkipsBodyButContinues() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSNode ifFalse = USSNode.ifNode(
            USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.literal("2")),
            Arrays.asList(writeVar(0, "in-body")));
        USSProgramExecutor executor = USSProgramExecutor.start(program(ifFalse, writeVar(1, "after")));
        runTicks(executor, ctx, 80);
        assertFalse(ctx.vars.isWritten(0), "the body must not run (across two loops)");
        assertEquals("after", ctx.vars.get(1));
        assertEquals(2, ctx.writeVarCalls, "only the trailing write ran, once per loop");
        assertFalse(executor.isCompleted());
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
        runTicks(executor, ctx, 400);
        assertEquals(5, ctx.writeVarCalls, "one body-run per positive cargo-free reading");
        assertEquals(0, ctx.cargoFree);
        assertFalse(executor.isCompleted(), "after the last body the program wraps (it never ends on its own)");
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
    void testRepeatRunsBodyExactlyNtimesPerPass() {
        // a finished REPEAT wraps (the whole program restarts) — two passes of 3 iterations in 280 ticks
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.repeat(3, Arrays.asList(writeVar(0, "r")))));
        runTicks(executor, ctx, 280);
        assertEquals(6, ctx.writeVarCalls, "two full passes × 3 iterations (the wrap restarts the REPEAT)");
        assertFalse(executor.isCompleted());
    }

    @Test
    void testRepeatZeroNeverRunsTheBody() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.repeat(0, Arrays.asList(writeVar(0, "r"))), writeVar(1, "after")));
        runTicks(executor, ctx, 80);
        assertFalse(ctx.vars.isWritten(0), "REPEAT 0 — the body must never run (Phase A contract, across two loops)");
        assertEquals("after", ctx.vars.get(1), "the node after the REPEAT runs (every loop)");
        assertEquals(2, ctx.writeVarCalls, "only the trailing write ran (once per loop)");
        assertFalse(executor.isCompleted());
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
        runTicks(executor, ctx, 500);
        assertEquals(3, ctx.writeVarCalls, "the nested body ran exactly 3 times (the stat ended the WHILE)");
        assertFalse(executor.isCompleted(), "the program wraps after the loop ends");
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
        runTicks(executor, ctx, 100);
        assertEquals("yes", ctx.vars.get(1));
        assertFalse(ctx.vars.isWritten(2));
        assertFalse(executor.isCompleted(), "the program wraps — it never ends on its own");
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
        while (!ctx.vars.isWritten(0) && ticks < 1000) {
            if (ticks >= 39) {
                ctx.legComplete = true; // the leg (real time on the game side) finishes at tick 40
            }
            executor.tick(ctx);
            ticks++;
        }
        assertEquals(1, ctx.travelLegs);
        assertEquals(10.0, ctx.lastLegDist, 0.0001);
        assertEquals("arrived", ctx.vars.get(0), "the instruction after MOVE runs on arrival");
        assertFalse(executor.isCompleted(), "the program wraps instead of ending");
        runTicks(executor, ctx, 200);
        assertEquals(
            6,
            ctx.travelLegs,
            "the wrapped pass re-arms the same MOVE leg on every loop (5 more passes in 200 ticks)");
    }

    @Test
    void testWorkRunsAtPositionThenContinues() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.command(USSCommand.MINE, new NBTTagCompound()), writeVar(0, "worked")));
        int ticks = 0;
        while (!ctx.vars.isWritten(0) && ticks < 1000) {
            if (ticks >= 30) {
                ctx.legComplete = true; // the work leg finishes after 30 ticks
            }
            executor.tick(ctx);
            ticks++;
        }
        assertEquals(1, ctx.workLegs);
        assertEquals("worked", ctx.vars.get(0));
        assertFalse(executor.isCompleted(), "the program wraps instead of ending");
        runTicks(executor, ctx, 200);
        assertEquals(
            6,
            ctx.workLegs,
            "the wrapped pass re-arms the WORK leg on every loop (5 more passes in 200 ticks)");
    }

    // endregion

    // region failure → skip (decision #3)

    @Test
    void testMoveUnresolvableTargetSkipsToNextNode() {
        FakeUSSContext ctx = new FakeUSSContext(); // no STAR target registered
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(moveTo(USSProgramDefaults.TARGET_STAR), writeVar(0, "after")));
        runTicks(executor, ctx, 60);
        assertEquals(0, ctx.travelLegs);
        assertEquals("after", ctx.vars.get(0), "the failure is skipped, the program keeps going");
        assertTrue(ctx.loggedContains("unresolvable"));
        assertFalse(executor.isCompleted(), "the program wraps — the skip never ends it");
    }

    @Test
    void testUnknownCommandIdSkipsToNextNode() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.command(99, new NBTTagCompound()), writeVar(0, "after")));
        runTicks(executor, ctx, 60);
        assertEquals("after", ctx.vars.get(0));
        assertTrue(ctx.loggedContains("unknown command 99"));
        assertFalse(executor.isCompleted());
    }

    @Test
    void testWorkFailureSkips() {
        // a context that refuses legs — the instruction is skipped, not a crash, not a halt
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.startRefused = true;
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.command(USSCommand.MINE, new NBTTagCompound()), writeVar(0, "after")));
        runTicks(executor, ctx, 60);
        assertEquals("after", ctx.vars.get(0));
        assertFalse(executor.isCompleted());
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
        runTicks(executor, ctx, 60);
        assertEquals("hello", ctx.vars.get(5));
        assertEquals("hello", ctx.vars.get(9));
        assertFalse(executor.isCompleted());
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

    // region the invisible while (the wrap)

    @Test
    void testLoneCommandWrapsForever() {
        // the user's one-command base program: a lone WORK runs, the program restarts at it, forever
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.legComplete = true; // legs finish instantly
        USSProgramExecutor executor = USSProgramExecutor
            .start(program(USSNode.command(USSCommand.MINE, new NBTTagCompound())));
        runTicks(executor, ctx, 200);
        assertTrue(ctx.workLegs >= 2, "the WORK re-armed at least twice (loops: " + ctx.workLegs + ")");
        assertFalse(executor.isCompleted(), "a lone command never ends the program");
    }

    @Test
    void testStopInsideWhileEndsTheProgram() {
        // a STOP anywhere in the tree ends the whole program (the wrap is transparent to it)
        FakeUSSContext ctx = new FakeUSSContext();
        USSNode loop = USSNode.whileNode(
            USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.literal("1")),
            Arrays.asList(USSNode.command(USSCommand.STOP, new NBTTagCompound())));
        USSProgramExecutor executor = USSProgramExecutor.start(program(loop));
        int ticks = 0;
        while (!executor.isCompleted() && ticks < 200) {
            executor.tick(ctx);
            ticks++;
        }
        assertTrue(executor.isCompleted(), "a STOP inside the loop ends the whole program");
        assertTrue(ticks < 100);
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

        int more = 0;
        while (ctx.writeVarCalls < 5 && more < 10000) { // the loop runs to its stat end (then the program wraps)
            restored.tick(ctx);
            more++;
        }
        assertEquals(5, ctx.writeVarCalls, "the loop continued exactly where it left off (no re-run, no loss)");
        assertTrue(more > 0);
        assertFalse(restored.isCompleted(), "after the stat ends the program wraps (never ends on its own)");
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

        int more = 0;
        while (!ctx.vars.isWritten(0) && more < 10000) {
            restored.tick(ctx);
            more++;
        }
        assertEquals("after-wait", ctx.vars.get(0));
        assertEquals(25, more, "the wait was not restarted by the round trip");
        assertFalse(restored.isCompleted(), "the program wraps after the write");
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
        int more = 0;
        while (!ctx.vars.isWritten(0) && more < 10000) {
            restored.tick(ctx);
            more++;
        }
        assertEquals(1, ctx.travelLegs, "the leg must not restart after the round trip");
        assertEquals("arrived", ctx.vars.get(0));
        assertFalse(restored.isCompleted(), "the program wraps after the write");
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
