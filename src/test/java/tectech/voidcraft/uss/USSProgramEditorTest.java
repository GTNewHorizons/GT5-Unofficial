package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the pass-33 program editor ({@link USSProgramEditor}) — the bare-JVM core of the Controller's
 * program editor: structural edits (insert / remove / move) at every nesting level, content edits (params with
 * the executor's NBT types, REPEAT count, conditions), the whole-program gate (presets / clear), cap
 * enforcement, and the "the input program is never mutated" purity rule.
 */
public class USSProgramEditorTest {

    // region builders (test readability)

    private static int[] path(int... p) {
        return p;
    }

    private static USSNode work() {
        return USSNode.command(USSCommand.WORK, new NBTTagCompound());
    }

    private static USSNode move(String target) {
        NBTTagCompound a = new NBTTagCompound();
        a.setString(USSProgramDefaults.PARAM_TARGET, target);
        return USSNode.command(USSCommand.MOVE, a);
    }

    private static USSNode write(int slot, String value) {
        NBTTagCompound a = new NBTTagCompound();
        a.setInteger(USSCommandWrite.PARAM_SLOT, slot);
        a.setString(USSCommandWrite.PARAM_VALUE, value);
        return USSNode.command(USSCommand.WRITE, a);
    }

    private static USSNode read(int from, int to) {
        NBTTagCompound a = new NBTTagCompound();
        a.setInteger(USSCommandRead.PARAM_FROM, from);
        a.setInteger(USSCommandRead.PARAM_TO, to);
        return USSNode.command(USSCommand.READ, a);
    }

    private static USSNode waitNode(long ticks) {
        NBTTagCompound a = new NBTTagCompound();
        a.setLong(USSCommandWait.PARAM_TICKS, ticks);
        return USSNode.command(USSCommand.WAIT, a);
    }

    private static USSNode ifNode(List<USSNode> body) {
        return USSNode.ifNode(USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.literal("1")), body);
    }

    private static USSNode repeat(int count, List<USSNode> body) {
        return USSNode.repeat(count, body);
    }

    /** A program of N WORK nodes (root level). */
    private static USSProgram works(int n) {
        List<USSNode> nodes = new ArrayList<USSNode>();
        for (int i = 0; i < n; i++) {
            nodes.add(work());
        }
        return USSProgram.of(nodes);
    }

    /** A chain of IFs {@code depth} levels deep (each body holds the next). */
    private static USSNode nestedIfs(int depth) {
        if (depth == 1) {
            return ifNode(Collections.singletonList(work()));
        }
        return ifNode(Collections.singletonList(nestedIfs(depth - 1)));
    }

    private static USSProgram accepted(USSProgramEditor.Result r) {
        assertNotNull(r);
        assertTrue(r.accepted(), "expected acceptance, got: " + r.error());
        return r.program();
    }

    private static String rejected(USSProgramEditor.Result r) {
        assertNotNull(r);
        assertFalse(r.accepted(), "expected rejection");
        assertNotNull(r.error());
        assertNull(r.program());
        return r.error();
    }

    // endregion

    // region insert

    @Test
    public void testInsertAtRootPositions() {
        USSProgram p = USSProgram.of(Arrays.asList(move("HOME"), work()));
        USSProgram afterStart = accepted(USSProgramEditor.insert(p, path(), 0, move("STAR")));
        assertEquals(
            "STAR",
            afterStart.nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            "HOME",
            afterStart.nodes()
                .get(1)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        USSProgram afterMiddle = accepted(USSProgramEditor.insert(p, path(), 1, waitNode(60L)));
        assertEquals(
            USSCommand.WAIT,
            afterMiddle.nodes()
                .get(1)
                .cmdId());
        USSProgram afterEnd = accepted(USSProgramEditor.insert(p, path(), 2, work()));
        assertEquals(3, afterEnd.size());
        assertEquals(
            USSCommand.WORK,
            afterEnd.nodes()
                .get(2)
                .cmdId());
    }

    @Test
    public void testInsertIntoIfBody() {
        USSProgram p = USSProgram.of(Arrays.asList(ifNode(Arrays.asList(work())), move("HOME")));
        USSProgram next = accepted(USSProgramEditor.insert(p, path(0), 0, waitNode(10L)));
        assertEquals(
            2,
            next.nodes()
                .get(0)
                .body()
                .size());
        assertEquals(
            USSCommand.WAIT,
            next.nodes()
                .get(0)
                .body()
                .get(0)
                .cmdId());
        // the second root node is untouched
        assertEquals(
            "HOME",
            next.nodes()
                .get(1)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
    }

    @Test
    public void testInsertIntoDeepBody() {
        USSProgram p = USSProgram.of(Arrays.asList(ifNode(Arrays.asList(repeat(2, Arrays.asList(work()))))));
        USSProgram next = accepted(USSProgramEditor.insert(p, path(0, 0), 1, work()));
        assertEquals(
            2,
            next.nodes()
                .get(0)
                .body()
                .get(0)
                .body()
                .size());
        assertEquals(4, next.nodeCount(), "root IF + REPEAT + 2 WORKs");
    }

    @Test
    public void testInsertRejectsBadInputs() {
        USSProgram p = USSProgram.of(Arrays.asList(work()));
        assertNotNull(rejected(USSProgramEditor.insert(null, path(), 0, work())));
        assertNotNull(rejected(USSProgramEditor.insert(p, path(), 0, null)));
        assertNotNull(rejected(USSProgramEditor.insert(p, path(), 5, work())));
        assertNotNull(rejected(USSProgramEditor.insert(p, path(-1), 0, work())));
        assertNotNull(rejected(USSProgramEditor.insert(p, path(7), 0, work())), "owner not found");
    }

    @Test
    public void testInsertRejectsCommandAsBodyOwner() {
        USSProgram p = USSProgram.of(Arrays.asList(work()));
        String error = rejected(USSProgramEditor.insert(p, path(0), 0, work()));
        assertTrue(error.contains("body"), "a command has no body: " + error);
    }

    @Test
    public void testInsertRejectsNodeCap() {
        USSProgram full = works(USSProgram.MAX_NODES);
        assertEquals(USSProgram.MAX_NODES, full.nodeCount());
        String error = rejected(USSProgramEditor.insert(full, path(), 0, work()));
        assertTrue(error.contains("node cap"), error);
    }

    @Test
    public void testInsertRejectsDepthCap() {
        // nestedIfs(n) has depth n+1 (n nested IFs + the leaf) — at cap = 7 IFs, leaf at depth 8.
        USSProgram atCap = USSProgram.of(Arrays.asList(nestedIfs(USSProgram.MAX_DEPTH - 1)));
        assertEquals(USSProgram.MAX_DEPTH, atCap.depth());
        // the innermost IF is at address [0,0,0,0,0,0,0] (depth 7); its body holds the leaf at depth 8.
        // inserting a FLOW node there would put its body at depth 9 → over the cap
        String error = rejected(
            USSProgramEditor.insert(atCap, path(0, 0, 0, 0, 0, 0, 0), 0, ifNode(Arrays.asList(work()))));
        assertTrue(error.contains("nesting"), error);
        // a plain leaf still fits (depth 8) and is accepted
        accepted(USSProgramEditor.insert(atCap, path(0, 0, 0, 0, 0, 0, 0), 0, work()));
    }

    // endregion

    // region remove

    @Test
    public void testRemoveFromRoot() {
        USSProgram p = USSProgram.of(Arrays.asList(move("STAR"), work(), move("HOME")));
        USSProgram next = accepted(USSProgramEditor.remove(p, path(1)));
        assertEquals(2, next.size());
        assertEquals(
            "STAR",
            next.nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            "HOME",
            next.nodes()
                .get(1)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
    }

    @Test
    public void testRemoveFromBody() {
        USSProgram p = USSProgram.of(Arrays.asList(ifNode(Arrays.asList(work(), waitNode(5L)))));
        USSProgram next = accepted(USSProgramEditor.remove(p, path(0, 1)));
        assertEquals(
            1,
            next.nodes()
                .get(0)
                .body()
                .size());
        assertEquals(
            USSCommand.WORK,
            next.nodes()
                .get(0)
                .body()
                .get(0)
                .cmdId());
    }

    @Test
    public void testRemoveTakesTheWholeSubtree() {
        USSProgram p = USSProgram.of(Arrays.asList(work(), ifNode(Arrays.asList(work(), work())), work()));
        assertEquals(5, p.nodeCount());
        USSProgram next = accepted(USSProgramEditor.remove(p, path(1)));
        assertEquals(2, next.size(), "the IF and its body are gone");
        assertEquals(2, next.nodeCount());
    }

    @Test
    public void testRemoveRejectsBadPaths() {
        USSProgram p = USSProgram.of(Arrays.asList(work()));
        assertNotNull(rejected(USSProgramEditor.remove(p, path())));
        assertNotNull(rejected(USSProgramEditor.remove(p, path(3))));
        assertNotNull(rejected(USSProgramEditor.remove(p, path(0, 0))), "a command has no body");
        assertNotNull(rejected(USSProgramEditor.remove(null, path(0))));
    }

    // endregion

    // region move

    @Test
    public void testMoveUpAndDownAtRoot() {
        USSProgram p = USSProgram.of(Arrays.asList(move("STAR"), work(), move("HOME")));
        USSProgram swapped = accepted(USSProgramEditor.move(p, path(1), false)); // work DOWN after HOME
        assertEquals(
            "HOME",
            swapped.nodes()
                .get(1)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            USSCommand.WORK,
            swapped.nodes()
                .get(2)
                .cmdId());
        USSProgram back = accepted(USSProgramEditor.move(p, path(1), true)); // work UP before STAR
        assertEquals(
            USSCommand.WORK,
            back.nodes()
                .get(0)
                .cmdId());
        assertEquals(
            "STAR",
            back.nodes()
                .get(1)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
    }

    @Test
    public void testMoveInBody() {
        USSProgram p = USSProgram.of(Arrays.asList(ifNode(Arrays.asList(work(), waitNode(5L)))));
        USSProgram next = accepted(USSProgramEditor.move(p, path(0, 0), false));
        assertEquals(
            USSCommand.WAIT,
            next.nodes()
                .get(0)
                .body()
                .get(0)
                .cmdId());
        assertEquals(
            USSCommand.WORK,
            next.nodes()
                .get(0)
                .body()
                .get(1)
                .cmdId());
    }

    @Test
    public void testMoveRejectsBoundariesAndMissingNodes() {
        USSProgram p = USSProgram.of(Arrays.asList(work(), work()));
        assertNotNull(rejected(USSProgramEditor.move(p, path(0), true)), "already first");
        assertNotNull(rejected(USSProgramEditor.move(p, path(1), false)), "already last");
        assertNotNull(rejected(USSProgramEditor.move(p, path(9), true)));
        assertNotNull(rejected(USSProgramEditor.move(p, path(), true)));
    }

    // endregion

    // region copy

    @Test
    public void testCopyRootCommandInsertsAfterOriginal() {
        USSProgram p = USSProgram.of(Arrays.asList(move("HOME"), work()));
        USSProgram next = accepted(USSProgramEditor.copy(p, path(0)));
        assertEquals(3, next.size());
        assertEquals(
            "HOME",
            next.nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            "HOME",
            next.nodes()
                .get(1)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            USSCommand.WORK,
            next.nodes()
                .get(2)
                .cmdId());
        // the copy is an INDEPENDENT node: editing the original's target does not touch the copy
        USSProgram edited = accepted(USSProgramEditor.setParam(next, path(0), USSProgramDefaults.PARAM_TARGET, "STAR"));
        assertEquals(
            "STAR",
            edited.nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            "HOME",
            edited.nodes()
                .get(1)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
    }

    @Test
    public void testCopyBodyNode() {
        USSProgram p = USSProgram.of(Arrays.asList(ifNode(Arrays.asList(work(), waitNode(5L)))));
        USSProgram next = accepted(USSProgramEditor.copy(p, path(0, 1)));
        assertEquals(
            3,
            next.nodes()
                .get(0)
                .body()
                .size());
        assertEquals(
            USSCommand.WAIT,
            next.nodes()
                .get(0)
                .body()
                .get(1)
                .cmdId());
        assertEquals(
            USSCommand.WAIT,
            next.nodes()
                .get(0)
                .body()
                .get(2)
                .cmdId());
        assertEquals(
            USSCommand.WORK,
            next.nodes()
                .get(0)
                .body()
                .get(0)
                .cmdId());
    }

    @Test
    public void testCopyIfWithBodyAndConditionDeepCopies() {
        USSNode inner = ifNode(Arrays.asList(work()));
        USSProgram p = USSProgram.of(Arrays.asList(ifNode(Arrays.asList(inner, work()))));
        USSProgram next = accepted(USSProgramEditor.copy(p, path(0, 0)));
        USSNode copy = next.nodes()
            .get(0)
            .body()
            .get(1);
        assertEquals(USSNodeType.IF, copy.type());
        assertNotNull(copy.condition());
        assertEquals(
            1,
            copy.body()
                .size());
        assertEquals(
            USSCommand.WORK,
            copy.body()
                .get(0)
                .cmdId());
        // editing the copy's body does not touch the original
        USSProgram edited = accepted(USSProgramEditor.remove(next, path(0, 1, 0)));
        assertEquals(
            1,
            edited.nodes()
                .get(0)
                .body()
                .get(0)
                .body()
                .size(),
            "original body intact");
        assertEquals(
            0,
            edited.nodes()
                .get(0)
                .body()
                .get(1)
                .body()
                .size(),
            "copy body emptied");
    }

    @Test
    public void testCopyRespectsNodeCap() {
        // 255 nodes already (cap) — a copy would be 256 → rejected
        USSProgram p = works(USSProgram.MAX_NODES);
        assertEquals(USSProgram.MAX_NODES, p.nodeCount());
        USSProgramEditor.Result r = USSProgramEditor.copy(p, path(0));
        assertFalse(r.accepted());
        assertNull(r.program());
        assertNotNull(r.error());
    }

    @Test
    public void testCopyRejectsBadPaths() {
        USSProgram p = USSProgram.of(Arrays.asList(work()));
        assertNotNull(rejected(USSProgramEditor.copy(p, path(9))), "out of range");
        assertNotNull(rejected(USSProgramEditor.copy(p, path())), "empty path");
        assertNotNull(rejected(USSProgramEditor.copy(null, path(0))), "null program");
    }

    // endregion

    // region setParam (with the executor's NBT types)

    @Test
    public void testSetMoveTarget() {
        USSProgram p = USSProgram.of(Arrays.asList(move("HOME")));
        USSProgram next = accepted(
            USSProgramEditor
                .setParam(p, path(0), USSProgramDefaults.PARAM_TARGET, USSProgramDefaults.TARGET_RANDOM_PLANET));
        assertEquals(
            USSProgramDefaults.TARGET_RANDOM_PLANET,
            next.nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        // every known target is accepted by the schema
        String[] all = { USSProgramDefaults.TARGET_STAR, USSProgramDefaults.TARGET_PLANET,
            USSProgramDefaults.TARGET_NEAREST_PLANET, USSProgramDefaults.TARGET_RANDOM_PLANET,
            USSProgramDefaults.TARGET_RIPPLE, USSProgramDefaults.TARGET_RIPPLE_UNSCANNED,
            USSProgramDefaults.TARGET_SHIP, USSProgramDefaults.TARGET_HOME };
        for (String target : all) {
            accepted(USSProgramEditor.setParam(p, path(0), USSProgramDefaults.PARAM_TARGET, target));
        }
    }

    @Test
    public void testSetMoveTargetRejectsUnknown() {
        USSProgram p = USSProgram.of(Arrays.asList(move("HOME")));
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(0), USSProgramDefaults.PARAM_TARGET, "MARS")));
    }

    @Test
    public void testSetMoveIndex() {
        USSProgram p = USSProgram.of(Arrays.asList(move("PLANET")));
        USSProgram next = accepted(USSProgramEditor.setParam(p, path(0), USSProgramDefaults.PARAM_INDEX, "12"));
        assertEquals(
            12,
            next.nodes()
                .get(0)
                .params()
                .getInteger(USSProgramDefaults.PARAM_INDEX));
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(0), USSProgramDefaults.PARAM_INDEX, "abc")));
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(0), USSProgramDefaults.PARAM_INDEX, "-1")));
    }

    @Test
    public void testSetWriteValueAndSlot() {
        USSProgram p = USSProgram.of(Arrays.asList(write(0, "")));
        USSProgram next = accepted(USSProgramEditor.setParam(p, path(0), USSCommandWrite.PARAM_VALUE, "hello"));
        assertEquals(
            "hello",
            next.nodes()
                .get(0)
                .params()
                .getString(USSCommandWrite.PARAM_VALUE));
        next = accepted(USSProgramEditor.setParam(p, path(0), USSCommandWrite.PARAM_SLOT, "42"));
        assertEquals(
            42,
            next.nodes()
                .get(0)
                .params()
                .getInteger(USSCommandWrite.PARAM_SLOT));
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(0), USSCommandWrite.PARAM_SLOT, "256")));
        StringBuilder tooLong = new StringBuilder();
        for (int i = 0; i < USSProgram.MAX_LITERAL_LENGTH + 1; i++) {
            tooLong.append('x');
        }
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(0), USSCommandWrite.PARAM_VALUE, tooLong.toString())));
    }

    @Test
    public void testSetReadSlots() {
        USSProgram p = USSProgram.of(Arrays.asList(read(0, 0)));
        USSProgram next = accepted(USSProgramEditor.setParam(p, path(0), USSCommandRead.PARAM_FROM, "7"));
        assertEquals(
            7,
            next.nodes()
                .get(0)
                .params()
                .getInteger(USSCommandRead.PARAM_FROM));
        next = accepted(USSProgramEditor.setParam(p, path(0), USSCommandRead.PARAM_TO, "255"));
        assertEquals(
            255,
            next.nodes()
                .get(0)
                .params()
                .getInteger(USSCommandRead.PARAM_TO));
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(0), USSCommandRead.PARAM_FROM, "256")));
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(0), USSCommandRead.PARAM_TO, "-2")));
    }

    @Test
    public void testSetWaitTicks() {
        USSProgram p = USSProgram.of(Arrays.asList(waitNode(0L)));
        USSProgram next = accepted(USSProgramEditor.setParam(p, path(0), USSCommandWait.PARAM_TICKS, "600"));
        assertEquals(
            600L,
            next.nodes()
                .get(0)
                .params()
                .getLong(USSCommandWait.PARAM_TICKS));
        accepted(USSProgramEditor.setParam(p, path(0), USSCommandWait.PARAM_TICKS, "0"));
        accepted(
            USSProgramEditor
                .setParam(p, path(0), USSCommandWait.PARAM_TICKS, String.valueOf(USSCommandWait.MAX_WAIT_TICKS)));
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(0), USSCommandWait.PARAM_TICKS, "-1")));
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(0), USSCommandWait.PARAM_TICKS, "zz")));
    }

    @Test
    public void testSetParamRejectsWrongShapes() {
        USSProgram p = USSProgram.of(Arrays.asList(work(), ifNode(Arrays.asList(work()))));
        assertNotNull(
            rejected(USSProgramEditor.setParam(p, path(0), USSCommandWait.PARAM_TICKS, "5")),
            "WORK takes no params");
        assertNotNull(
            rejected(USSProgramEditor.setParam(p, path(1), USSProgramDefaults.PARAM_TARGET, "STAR")),
            "IF takes no params");
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(0), "unknown", "5")));
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(9), USSCommandWait.PARAM_TICKS, "5")));
        assertNotNull(rejected(USSProgramEditor.setParam(p, path(), USSCommandWait.PARAM_TICKS, "5")));
    }

    // endregion

    // region setCount

    @Test
    public void testSetCount() {
        USSProgram p = USSProgram.of(Arrays.asList(repeat(1, Arrays.asList(work()))));
        USSProgram next = accepted(USSProgramEditor.setCount(p, path(0), 9));
        assertEquals(
            9,
            next.nodes()
                .get(0)
                .count());
        assertEquals(
            1,
            next.nodes()
                .get(0)
                .body()
                .size(),
            "the body survives the count edit");
        assertEquals(
            USSCommand.WORK,
            next.nodes()
                .get(0)
                .body()
                .get(0)
                .cmdId());
        accepted(USSProgramEditor.setCount(p, path(0), 0));
        accepted(USSProgramEditor.setCount(p, path(0), USSNode.MAX_REPEAT_COUNT));
    }

    @Test
    public void testSetCountRejects() {
        USSProgram p = USSProgram.of(Arrays.asList(work(), repeat(1, Arrays.asList(work()))));
        assertNotNull(rejected(USSProgramEditor.setCount(p, path(0), 3)), "not a REPEAT");
        assertNotNull(rejected(USSProgramEditor.setCount(p, path(1), USSNode.MAX_REPEAT_COUNT + 1)));
        assertNotNull(rejected(USSProgramEditor.setCount(p, path(1), -1)));
        assertNotNull(rejected(USSProgramEditor.setCount(p, path(9), 3)));
    }

    // endregion

    // region condition

    @Test
    public void testSetConditionSides() {
        USSProgram p = USSProgram.of(Arrays.asList(ifNode(Arrays.asList(work()))));
        USSProgram next = accepted(USSProgramEditor.setConditionSide(p, path(0), true, USSValue.variable(9)));
        assertEquals(
            USSValue.Kind.VAR,
            next.nodes()
                .get(0)
                .condition()
                .left()
                .kind());
        assertEquals(
            9,
            next.nodes()
                .get(0)
                .condition()
                .left()
                .slot());
        // the right side and the operator survived
        assertEquals(
            USSConditionOp.EQ,
            next.nodes()
                .get(0)
                .condition()
                .op());
        assertEquals(
            "1",
            next.nodes()
                .get(0)
                .condition()
                .right()
                .literal());
        next = accepted(
            USSProgramEditor.setConditionSide(p, path(0), false, USSValue.stat(USSShipStat.CARGO_FULL.getId())));
        assertEquals(
            USSValue.Kind.STAT,
            next.nodes()
                .get(0)
                .condition()
                .right()
                .kind());
        // a WHILE node is edited the same way
        USSProgram w = USSProgram.of(
            Arrays.asList(
                USSNode.whileNode(
                    USSCondition.of(USSValue.literal(""), USSConditionOp.EQ, USSValue.literal("")),
                    Arrays.asList(work()))));
        next = accepted(USSProgramEditor.setConditionSide(w, path(0), true, USSValue.literal("x")));
        assertEquals(
            "x",
            next.nodes()
                .get(0)
                .condition()
                .left()
                .literal());
    }

    @Test
    public void testSetConditionRejectsNonFlow() {
        USSProgram p = USSProgram.of(Arrays.asList(work(), repeat(2, Arrays.asList(work()))));
        assertNotNull(rejected(USSProgramEditor.setConditionSide(p, path(0), true, USSValue.literal("x"))));
        assertNotNull(
            rejected(USSProgramEditor.setConditionSide(p, path(1), false, USSValue.literal("x"))),
            "REPEAT has no condition");
        assertNotNull(rejected(USSProgramEditor.setConditionSide(p, path(9), true, USSValue.literal("x"))));
    }

    @Test
    public void testSetOp() {
        USSProgram p = USSProgram.of(Arrays.asList(ifNode(Arrays.asList(work()))));
        USSProgram next = accepted(USSProgramEditor.setOp(p, path(0), USSConditionOp.GT));
        assertEquals(
            USSConditionOp.GT,
            next.nodes()
                .get(0)
                .condition()
                .op());
        // the sides survived
        assertEquals(
            "1",
            next.nodes()
                .get(0)
                .condition()
                .left()
                .literal());
        for (USSConditionOp op : USSConditionOp.values()) {
            accepted(USSProgramEditor.setOp(p, path(0), op));
        }
        assertNotNull(rejected(USSProgramEditor.setOp(p, path(0), null)));
        USSProgram w = USSProgram.of(Arrays.asList(work()));
        assertNotNull(rejected(USSProgramEditor.setOp(w, path(0), USSConditionOp.EQ)));
    }

    // endregion

    // region apply (preset chips / clear / the whole-program gate)

    @Test
    public void testApplyAcceptsTheChips() {
        accepted(USSProgramEditor.apply(USSProgramDefaults.miner()));
        accepted(USSProgramEditor.apply(USSProgramDefaults.starlifter()));
        accepted(USSProgramEditor.apply(USSProgramDefaults.explorer()));
        accepted(USSProgramEditor.apply(USSProgram.empty())); // the Clear button
    }

    @Test
    public void testApplyRejectsBadPrograms() {
        assertNotNull(rejected(USSProgramEditor.apply(null)));
        assertNotNull(rejected(USSProgramEditor.apply(works(USSProgram.MAX_NODES + 1))), "over the node cap");
        assertNotNull(
            rejected(USSProgramEditor.apply(USSProgram.of(Arrays.asList(nestedIfs(USSProgram.MAX_DEPTH + 1))))),
            "over the depth cap");

        NBTTagCompound badTarget = new NBTTagCompound();
        badTarget.setString(USSProgramDefaults.PARAM_TARGET, "MARS");
        assertNotNull(
            rejected(USSProgramEditor.apply(USSProgram.of(Arrays.asList(USSNode.command(USSCommand.MOVE, badTarget))))),
            "unknown target");

        NBTTagCompound noTarget = new NBTTagCompound();
        assertNotNull(
            rejected(USSProgramEditor.apply(USSProgram.of(Arrays.asList(USSNode.command(USSCommand.MOVE, noTarget))))),
            "missing target");

        NBTTagCompound badSlot = new NBTTagCompound();
        badSlot.setInteger(USSCommandWrite.PARAM_SLOT, 300);
        assertNotNull(
            rejected(USSProgramEditor.apply(USSProgram.of(Arrays.asList(USSNode.command(USSCommand.WRITE, badSlot))))),
            "slot out of range");

        NBTTagCompound badTicks = new NBTTagCompound();
        badTicks.setLong(USSCommandWait.PARAM_TICKS, -5L);
        assertNotNull(
            rejected(USSProgramEditor.apply(USSProgram.of(Arrays.asList(USSNode.command(USSCommand.WAIT, badTicks))))),
            "negative ticks");

        // a bad target DEEP in the body is caught by the walk
        NBTTagCompound deep = new NBTTagCompound();
        deep.setString(USSProgramDefaults.PARAM_TARGET, "NOPE");
        USSNode nested = ifNode(Arrays.asList(USSNode.command(USSCommand.MOVE, deep)));
        assertNotNull(rejected(USSProgramEditor.apply(USSProgram.of(Arrays.asList(nested)))));
    }

    // endregion

    // region purity + round-trip

    @Test
    public void testInputProgramIsNeverMutated() {
        USSProgram p = USSProgram
            .of(Arrays.asList(ifNode(Arrays.asList(work(), work())), repeat(1, Arrays.asList(work())), move("HOME")));
        int beforeSize = p.size();
        int beforeCount = p.nodeCount();
        // a battery of edits — every one applies to a DIFFERENT valid shape of the (unmodified) program
        accepted(USSProgramEditor.insert(p, path(), 0, waitNode(1L)));
        accepted(USSProgramEditor.insert(p, path(0), 0, work()));
        accepted(USSProgramEditor.insert(p, path(1), 0, work()));
        USSProgram withWait = accepted(USSProgramEditor.insert(p, path(0), 0, waitNode(1L)));
        accepted(USSProgramEditor.setParam(withWait, path(0, 0), USSCommandWait.PARAM_TICKS, "99"));
        accepted(USSProgramEditor.remove(p, path(0, 1)));
        accepted(USSProgramEditor.move(p, path(1), false));
        accepted(USSProgramEditor.setParam(p, path(2), USSProgramDefaults.PARAM_TARGET, "STAR"));
        accepted(USSProgramEditor.setCount(p, path(1), 5));
        accepted(USSProgramEditor.setConditionSide(p, path(0), true, USSValue.literal("2")));
        assertEquals(beforeSize, p.size(), "the root list is unchanged");
        assertEquals(beforeCount, p.nodeCount(), "the subtree is unchanged");
        assertEquals(
            USSCommand.WORK,
            p.nodes()
                .get(0)
                .body()
                .get(1)
                .cmdId(),
            "the IF body is unchanged");
        assertEquals(
            1,
            p.nodes()
                .get(1)
                .count(),
            "the REPEAT count is unchanged");
        assertEquals(
            "HOME",
            p.nodes()
                .get(2)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET),
            "the MOVE is unchanged");
    }

    @Test
    public void testEditedProgramRoundTripsThroughNbt() {
        USSProgram p = USSProgram.of(Arrays.asList(ifNode(Arrays.asList(work())), move("HOME")));
        // edited = [REPEAT(write), IF(work), MOVE HOME]
        USSProgram edited = accepted(USSProgramEditor.insert(p, path(), 0, repeat(3, Arrays.asList(write(1, "done")))));
        edited = accepted(
            USSProgramEditor
                .setParam(edited, path(2), USSProgramDefaults.PARAM_TARGET, USSProgramDefaults.TARGET_RANDOM_PLANET));
        edited = accepted(USSProgramEditor.setConditionSide(edited, path(1), false, USSValue.variable(3)));
        USSProgram roundTrip = USSProgram.readFromNBT(edited.writeToNBT());
        assertEquals(edited, roundTrip, "the edited program survives an NBT round-trip unchanged");
        // and it passes the whole-program gate
        accepted(USSProgramEditor.apply(roundTrip));
    }

    // endregion
}
