package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the GUI action handler ({@link USSProgramSync}) — the single server-side entry that
 * turns one GUI ACTION JSON into an {@link USSProgramEditor} call: every op (insert / remove / move / param /
 * count / cond / condop / apply), the USS-slot reference assignment (param-var / cond-var), rejection reasons,
 * and the "bad JSON never throws" rule.
 */
public class USSProgramSyncTest {

    private static int[] path(int... p) {
        return p;
    }

    private static USSNode work() {
        return USSNode.command(USSCommand.MINE, new NBTTagCompound());
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

    private static USSNode ifNode() {
        return USSNode.ifNode(
            USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.literal("1")),
            Arrays.asList(work()));
    }

    private static USSProgram program(USSNode... nodes) {
        return USSProgram.of(Arrays.asList(nodes));
    }

    private static USSProgramSync.Outcome ok(USSProgram p, String action) {
        return ok(p, action, null);
    }

    private static USSProgramSync.Outcome ok(USSProgram p, String action, USSCapabilities caps) {
        USSProgramSync.Outcome out = USSProgramSync.handle(p, action, caps);
        assertTrue(out.ok, "expected accepted, got: " + out.message);
        assertNotNull(out.program);
        return out;
    }

    private static USSProgramSync.Outcome rejected(USSProgram p, String action, String messagePart) {
        return rejected(p, action, null, messagePart);
    }

    private static USSProgramSync.Outcome rejected(USSProgram p, String action, USSCapabilities caps,
        String messagePart) {
        USSProgramSync.Outcome out = USSProgramSync.handle(p, action, caps);
        assertFalse(out.ok, "expected rejection");
        assertNull(out.program);
        assertNotNull(out.message);
        if (messagePart != null) {
            assertTrue(out.message.contains(messagePart), out.message + " !~ " + messagePart);
        }
        return out;
    }

    // region insert

    @Test
    public void testInsertAtRoot() {
        USSProgram out = ok(
            program(work()),
            "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":0,\"p\":{\"target\":\"HOME\"}}}").program;
        assertEquals(2, out.nodeCount());
        assertEquals(
            "HOME",
            out.nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
    }

    @Test
    public void testInsertBeforeExistingRoot() {
        USSProgram out = ok(
            program(work(), work()),
            "{\"op\":\"insert\",\"path\":[],\"index\":1,\"node\":{\"t\":0,\"c\":5}}").program;
        assertEquals(3, out.nodeCount());
        assertEquals(
            USSCommand.STOP,
            out.nodes()
                .get(1)
                .cmdId());
    }

    @Test
    public void testInsertIntoBody() {
        USSProgram out = ok(
            program(ifNode()),
            "{\"op\":\"insert\",\"path\":[0],\"index\":0,\"node\":{\"t\":0,\"c\":0,\"p\":{\"target\":\"STAR\"}}}").program;
        assertEquals(
            "STAR",
            out.nodes()
                .get(0)
                .body()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            2,
            out.nodes()
                .get(0)
                .body()
                .size());
    }

    @Test
    public void testInsertNodeWithBody() {
        USSProgram out = ok(
            program(),
            "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":3,\"n\":2,\"b\":[{\"t\":0,\"c\":1}]}}").program;
        assertEquals(2, out.nodeCount()); // REPEAT + its WORK child
        assertEquals(
            1,
            out.nodes()
                .size());
        assertEquals(
            2,
            out.nodes()
                .get(0)
                .count());
        assertEquals(
            1,
            out.nodes()
                .get(0)
                .body()
                .size());
        assertEquals(
            USSCommand.MINE,
            out.nodes()
                .get(0)
                .body()
                .get(0)
                .cmdId());
    }

    @Test
    public void testInsertRejections() {
        rejected(program(work()), "{\"op\":\"insert\",\"path\":[],\"index\":5,\"node\":{\"t\":0,\"c\":1}}", null);
        rejected(program(work()), "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":9}}", "bad node spec");
        rejected(program(work()), "{\"op\":\"insert\",\"path\":[],\"index\":0}", null);
        // the node cap is still enforced through the action path (255 roots + 1 → over cap)
        USSProgram full = USSProgram.of(
            java.util.Collections
                .nCopies(USSProgram.MAX_NODES, USSNode.command(USSCommand.MINE, new NBTTagCompound())));
        rejected(full, "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":1}}", null);
    }

    // endregion

    // region remove / move

    @Test
    public void testRemoveRootAndBody() {
        USSProgram p = program(ifNode(), work());
        USSProgram out = ok(p, "{\"op\":\"remove\",\"path\":[0]}").program;
        assertEquals(1, out.nodeCount());

        out = ok(p, "{\"op\":\"remove\",\"path\":[0,0]}").program;
        assertEquals(2, out.nodeCount()); // IF (now empty body) + WORK
        assertEquals(
            2,
            out.nodes()
                .size());
        assertTrue(
            out.nodes()
                .get(0)
                .body()
                .isEmpty());
    }

    @Test
    public void testRemoveRejections() {
        rejected(program(work()), "{\"op\":\"remove\",\"path\":[3]}", null);
        rejected(program(work()), "{\"op\":\"remove\",\"path\":[0,0]}", null);
        rejected(program(work()), "{\"op\":\"remove\"}", null);
    }

    @Test
    public void testMoveUpAndDown() {
        USSProgram p = program(move("HOME"), work(), write(1, "x"));
        USSProgram out = ok(p, "{\"op\":\"move\",\"path\":[1],\"up\":true}").program;
        assertEquals(
            USSCommand.MINE,
            out.nodes()
                .get(0)
                .cmdId());

        // first row cannot move up → rejection
        rejected(p, "{\"op\":\"move\",\"path\":[0],\"up\":true}", null);
        // last row cannot move down → rejection
        rejected(p, "{\"op\":\"move\",\"path\":[2],\"up\":false}", null);
        // middle moves both ways
        out = ok(p, "{\"op\":\"move\",\"path\":[1],\"up\":false}").program;
        assertEquals(
            USSCommand.MINE,
            out.nodes()
                .get(2)
                .cmdId());
        out = ok(p, "{\"op\":\"move\",\"path\":[2],\"up\":true}").program;
        assertEquals(
            USSCommand.WRITE,
            out.nodes()
                .get(1)
                .cmdId());
    }

    @Test
    public void testMoveBody() {
        USSProgram p = USSProgram.of(
            Arrays.asList(
                USSNode.ifNode(
                    USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.literal("1")),
                    Arrays.asList(work(), work()))));
        USSProgram out = ok(p, "{\"op\":\"move\",\"path\":[0,1],\"up\":true}").program;
        // both bodies are WORK — verify by order marker: move the second up, then the list is unchanged in content
        assertEquals(
            2,
            out.nodes()
                .get(0)
                .body()
                .size());
    }

    @Test
    public void testCopy() {
        USSProgram p = program(move("HOME"), work());
        USSProgram out = ok(p, "{\"op\":\"copy\",\"path\":[0]}").program;
        assertEquals(3, out.size());
        assertEquals(
            "HOME",
            out.nodes()
                .get(1)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(
            USSCommand.MINE,
            out.nodes()
                .get(2)
                .cmdId());
    }

    @Test
    public void testCopyBodyAndRejections() {
        USSProgram p = USSProgram.of(
            Arrays.asList(
                USSNode.ifNode(
                    USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.literal("1")),
                    Arrays.asList(work(), work()))));
        USSProgram out = ok(p, "{\"op\":\"copy\",\"path\":[0,0]}").program;
        assertEquals(
            3,
            out.nodes()
                .get(0)
                .body()
                .size());

        rejected(p, "{\"op\":\"copy\",\"path\":[9]}", null);
        rejected(p, "{\"op\":\"copy\",\"path\":[]}", null);
        rejected(p, "{\"op\":\"copy\"}", null);
    }

    // endregion

    // region param (literal + USS reference)

    @Test
    public void testParamLiteral() {
        USSProgram out = ok(
            program(move("HOME")),
            "{\"op\":\"param\",\"path\":[0],\"key\":\"target\",\"value\":\"STAR\"}").program;
        assertEquals(
            "STAR",
            out.nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
    }

    @Test
    public void testParamIntParams() {
        USSProgram out = ok(
            program(write(0, "")),
            "{\"op\":\"param\",\"path\":[0],\"key\":\"slot\",\"value\":\"42\"}").program;
        assertEquals(
            42,
            out.nodes()
                .get(0)
                .params()
                .getInteger(USSCommandWrite.PARAM_SLOT));

        out = ok(
            program(USSNode.command(USSCommand.WAIT, new NBTTagCompound())),
            "{\"op\":\"param\",\"path\":[0],\"key\":\"ticks\",\"value\":\"300\"}").program;
        assertEquals(
            300L,
            out.nodes()
                .get(0)
                .params()
                .getLong(USSCommandWait.PARAM_TICKS));
    }

    @Test
    public void testParamRejections() {
        rejected(program(move("HOME")), "{\"op\":\"param\",\"path\":[0],\"key\":\"nope\",\"value\":\"x\"}", null);
        rejected(program(work()), "{\"op\":\"param\",\"path\":[0],\"key\":\"target\",\"value\":\"x\"}", null);
        // MOVE target out of the whitelist
        rejected(
            program(move("HOME")),
            "{\"op\":\"param\",\"path\":[0],\"key\":\"target\",\"value\":\"NOWHERE\"}",
            null);
    }

    @Test
    public void testParamVarAssignsWriteValue() {
        USSProgram out = ok(
            program(write(9, "literal")),
            "{\"op\":\"param\",\"path\":[0],\"key\":\"value\",\"var\":17}").program;
        NBTBase tag = out.nodes()
            .get(0)
            .params()
            .getTag(USSCommandWrite.PARAM_VALUE);
        assertTrue(tag instanceof NBTTagCompound);
        USSValue uv = USSValue.readFromNBT((NBTTagCompound) tag);
        assertEquals(USSValue.Kind.VAR, uv.kind());
        assertEquals(17, uv.slot());
    }

    @Test
    public void testParamVarRejections() {
        // only a WRITE value accepts a reference
        rejected(program(move("HOME")), "{\"op\":\"param\",\"path\":[0],\"key\":\"target\",\"var\":3}", null);
        rejected(program(write(1, "x")), "{\"op\":\"param\",\"path\":[0],\"key\":\"slot\",\"var\":3}", null);
        rejected(program(ifNode()), "{\"op\":\"param\",\"path\":[0],\"key\":\"value\",\"var\":3}", null);
    }

    @Test
    public void testParamLocAssignsWriteValue() {
        // the "loc" marker assigns the LOCATION value (the ship's current position at execution time)
        USSProgram out = ok(
            program(write(9, "literal")),
            "{\"op\":\"param\",\"path\":[0],\"key\":\"value\",\"loc\":true}").program;
        NBTBase tag = out.nodes()
            .get(0)
            .params()
            .getTag(USSCommandWrite.PARAM_VALUE);
        assertTrue(tag instanceof NBTTagCompound);
        assertEquals(USSValue.location(), USSValue.readFromNBT((NBTTagCompound) tag));
    }

    @Test
    public void testParamLocRejections() {
        // like a var reference: only a WRITE value accepts the LOCATION value
        rejected(program(move("HOME")), "{\"op\":\"param\",\"path\":[0],\"key\":\"target\",\"loc\":true}", null);
        rejected(program(write(1, "x")), "{\"op\":\"param\",\"path\":[0],\"key\":\"slot\",\"loc\":true}", null);
    }

    // endregion

    // region count / cond / condop

    @Test
    public void testCount() {
        USSProgram p = USSProgram.of(Arrays.asList(USSNode.repeat(1, Arrays.asList(work()))));
        USSProgram out = ok(p, "{\"op\":\"count\",\"path\":[0],\"value\":9}").program;
        assertEquals(
            9,
            out.nodes()
                .get(0)
                .count());

        out = ok(p, "{\"op\":\"count\",\"path\":[0],\"value\":0}").program;
        assertEquals(
            0,
            out.nodes()
                .get(0)
                .count());

        rejected(p, "{\"op\":\"count\",\"path\":[0],\"value\":70000}", null);
        rejected(p, "{\"op\":\"count\",\"path\":[3],\"value\":1}", null);
    }

    @Test
    public void testCondLiteralVarStat() {
        USSProgram p = program(ifNode());
        USSProgram out = ok(p, "{\"op\":\"cond\",\"path\":[0],\"side\":0,\"lit\":\"fuel\"}").program;
        assertEquals(
            "fuel",
            out.nodes()
                .get(0)
                .condition()
                .left()
                .literal());

        out = ok(p, "{\"op\":\"cond\",\"path\":[0],\"side\":1,\"var\":11}").program;
        assertEquals(
            USSValue.Kind.VAR,
            out.nodes()
                .get(0)
                .condition()
                .right()
                .kind());
        assertEquals(
            11,
            out.nodes()
                .get(0)
                .condition()
                .right()
                .slot());

        out = ok(p, "{\"op\":\"cond\",\"path\":[0],\"side\":0,\"stat\":7}").program;
        assertEquals(
            USSValue.Kind.STAT,
            out.nodes()
                .get(0)
                .condition()
                .left()
                .kind());
        assertEquals(
            7,
            out.nodes()
                .get(0)
                .condition()
                .left()
                .statId());

        out = ok(p, "{\"op\":\"cond\",\"path\":[0],\"side\":1,\"loc\":true}").program;
        assertEquals(
            USSValue.Kind.LOCATION,
            out.nodes()
                .get(0)
                .condition()
                .right()
                .kind());
    }

    @Test
    public void testCondRejections() {
        rejected(program(work()), "{\"op\":\"cond\",\"path\":[0],\"side\":0,\"lit\":\"x\"}", null);
        rejected(program(ifNode()), "{\"op\":\"cond\",\"path\":[0],\"side\":2,\"lit\":\"x\"}", null);
        rejected(program(ifNode()), "{\"op\":\"cond\",\"path\":[0],\"side\":0}", null);
        rejected(program(ifNode()), "{\"op\":\"cond\",\"path\":[5],\"side\":0,\"lit\":\"x\"}", null);
    }

    @Test
    public void testCondop() {
        USSProgram p = program(ifNode());
        USSProgram out = ok(p, "{\"op\":\"condop\",\"path\":[0],\"operator\":\"GT\"}").program;
        assertEquals(
            USSConditionOp.GT,
            out.nodes()
                .get(0)
                .condition()
                .op());

        out = ok(p, "{\"op\":\"condop\",\"path\":[0],\"operator\":1}").program; // int form
        assertEquals(
            USSConditionOp.NEQ,
            out.nodes()
                .get(0)
                .condition()
                .op());

        rejected(p, "{\"op\":\"condop\",\"path\":[0],\"operator\":\"FOO\"}", null);
        rejected(program(work()), "{\"op\":\"condop\",\"path\":[0],\"operator\":\"EQ\"}", null);
    }

    // endregion

    // region apply (presets)

    @Test
    public void testApplyPresets() {
        USSProgram out = ok(program(work()), "{\"op\":\"apply\",\"preset\":\"miner\"}").program;
        assertEquals(
            USSProgramDefaults.miner()
                .nodeCount(),
            out.nodeCount());

        out = ok(program(work()), "{\"op\":\"apply\",\"preset\":\"starlifter\"}").program;
        assertEquals(
            USSProgramDefaults.starlifter()
                .nodeCount(),
            out.nodeCount());

        out = ok(program(work()), "{\"op\":\"apply\",\"preset\":\"explorer\"}").program;
        assertEquals(
            USSProgramDefaults.explorer()
                .nodeCount(),
            out.nodeCount());

        out = ok(program(work()), "{\"op\":\"apply\",\"preset\":\"clear\"}").program;
        assertEquals(0, out.nodeCount());

        rejected(program(work()), "{\"op\":\"apply\",\"preset\":\"warp\"}", "unknown preset");
    }

    // endregion

    // region capability gating (the capability system)

    @Test
    public void testInsertRejectedOutsideTheCaps() {
        // a mining-only ship: the palette offers no SCAN / SIPHON / CONSTRUCT rows, and the server rejects an
        // insert of one (the GUI's Add buttons never reach it — the server is authoritative)
        USSCapabilities miner = USSCapabilities.of(USSCapabilities.MOVE | USSCapabilities.MINE);
        rejected(
            program(work()),
            "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":8}}",
            miner,
            "SCAN");
        rejected(
            program(work()),
            "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":9}}",
            miner,
            "SIPHON");
        rejected(
            program(work()),
            "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":6}}",
            miner,
            "CONSTRUCT");
        // MINE / MOVE / the always-available commands are accepted
        ok(program(work()), "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":1}}", miner);
        ok(
            program(work()),
            "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":0,\"p\":{\"target\":\"HOME\"}}}",
            miner);
        ok(program(work()), "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":5}}", miner);
        // a null caps = no check at all (legacy call path)
        ok(program(work()), "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":8}}", null);
    }

    @Test
    public void testCopyRejectedOutsideTheCaps() {
        // copying a SCAN row on a mining-only ship is rejected (the copy is a re-insert)
        USSCapabilities miner = USSCapabilities.of(USSCapabilities.MOVE | USSCapabilities.MINE);
        USSProgram withScan = ok(
            program(work()),
            "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":8}}",
            USSCapabilities.universal()).program;
        rejected(withScan, "{\"op\":\"copy\",\"path\":[0]}", miner, "SCAN");
    }

    @Test
    public void testApplyPresetRejectedWithoutTheCapability() {
        // the preset gate: a mining-only ship cannot apply the Starlifter / Explorer / Constructor presets
        USSCapabilities miner = USSCapabilities.of(USSCapabilities.MOVE | USSCapabilities.MINE);
        rejected(program(work()), "{\"op\":\"apply\",\"preset\":\"starlifter\"}", miner, "starlifter");
        rejected(program(work()), "{\"op\":\"apply\",\"preset\":\"explorer\"}", miner, "explorer");
        rejected(program(work()), "{\"op\":\"apply\",\"preset\":\"constructor\"}", miner, "constructor");
        // its own preset and the capability-free clear are fine
        ok(program(work()), "{\"op\":\"apply\",\"preset\":\"miner\"}", miner);
        ok(program(work()), "{\"op\":\"apply\",\"preset\":\"clear\"}", USSCapabilities.empty());
        // the editor walk applies too: a starlifter program needs MOVE + SIPHON for the editor's own check
        rejected(
            program(work()),
            "{\"op\":\"apply\",\"preset\":\"starlifter\"}",
            USSCapabilities.of(USSCapabilities.SIPHON),
            "MOVE");
        ok(
            program(work()),
            "{\"op\":\"apply\",\"preset\":\"starlifter\"}",
            USSCapabilities.of(USSCapabilities.MOVE | USSCapabilities.SIPHON));
    }

    // endregion

    // region robustness

    @Test
    public void testBadActionsNeverThrow() {
        USSProgram p = program(work());
        rejected(p, "not json", null);
        rejected(p, "{\"op\":\"teleport\"}", "unknown op");
        rejected(p, "{}", "unknown op");
        rejected(p, null, null);
    }

    @Test
    public void testRejectedActionsKeepTheProgram() {
        USSProgram p = program(move("HOME"), work());
        USSProgramSync.Outcome out = rejected(p, "{\"op\":\"remove\",\"path\":[9]}", null);
        assertNull(out.program);
        // the input program is untouched (purity — the editor is pure)
        assertEquals(2, p.nodeCount());
        assertEquals(
            "HOME",
            p.nodes()
                .get(0)
                .params()
                .getString(USSProgramDefaults.PARAM_TARGET));
    }

    @Test
    public void testValueSpecPinsTheWireForm() {
        // the value spec ({"k":…}) is a stored + sent format — the kind ids stay pinned
        com.google.gson.JsonObject v = new com.google.gson.JsonObject();
        v.addProperty("k", 0);
        v.addProperty("s", "lit");
        assertEquals(USSValue.literal("lit"), USSProgramSync.readValue(v));
        v = new com.google.gson.JsonObject();
        v.addProperty("k", 1);
        v.addProperty("v", 17);
        assertEquals(USSValue.variable(17), USSProgramSync.readValue(v));
        v = new com.google.gson.JsonObject();
        v.addProperty("k", 2);
        v.addProperty("st", 5);
        assertEquals(USSValue.stat(5), USSProgramSync.readValue(v));
        v = new com.google.gson.JsonObject();
        v.addProperty("k", 3);
        assertEquals(USSValue.location(), USSProgramSync.readValue(v));
        v = new com.google.gson.JsonObject();
        v.addProperty("k", 4);
        assertNull(USSProgramSync.readValue(v), "unknown kinds are not values");
    }

    // endregion
}
