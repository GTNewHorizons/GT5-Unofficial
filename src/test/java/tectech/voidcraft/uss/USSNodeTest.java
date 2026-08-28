package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the program NODE ({@link USSNode}): the four node kinds, factory clamping/null-safety,
 * depth/subtree counting, and the NBT round-trip (including the structural-corruption → null path).
 */
public class USSNodeTest {

    private static USSCondition guard() {
        return USSCondition.of(USSValue.literal("a"), USSConditionOp.EQ, USSValue.literal("b"));
    }

    @Test
    public void testCommandNode() {
        NBTTagCompound params = new NBTTagCompound();
        params.setString(USSProgramDefaults.PARAM_TARGET, USSProgramDefaults.TARGET_HOME);
        USSNode node = USSNode.command(USSCommand.MOVE, params);
        assertEquals(USSNodeType.COMMAND, node.type());
        assertTrue(node.isCommand());
        assertFalse(node.hasBody());
        assertEquals(USSCommand.MOVE, node.cmdId());
        assertEquals(
            USSProgramDefaults.TARGET_HOME,
            node.params()
                .getString(USSProgramDefaults.PARAM_TARGET));
        assertTrue(
            node.body()
                .isEmpty());
        assertNull(node.condition());
    }

    @Test
    public void testCommandNullParamsGivesEmptyCompound() {
        USSNode node = USSNode.command(USSCommand.MINE, null);
        assertTrue(
            node.params()
                .hasNoTags());
    }

    @Test
    public void testCommandNegativeIdClampsToZero() {
        assertEquals(
            0,
            USSNode.command(-7, null)
                .cmdId());
    }

    @Test
    public void testCommandParamsAreDefensivelyCopied() {
        NBTTagCompound params = new NBTTagCompound();
        params.setString("a", "1");
        USSNode node = USSNode.command(USSCommand.WAIT, params);
        params.setString("a", "MUTATED");
        assertEquals(
            "1",
            node.params()
                .getString("a"),
            "mutating the caller's compound must not leak in");
    }

    @Test
    public void testIfNodeHoldsConditionAndBody() {
        USSNode bodyNode = USSNode.command(USSCommand.STOP, null);
        USSNode node = USSNode.ifNode(guard(), Collections.singletonList(bodyNode));
        assertEquals(USSNodeType.IF, node.type());
        assertFalse(node.isCommand());
        assertTrue(node.hasBody());
        assertEquals(guard(), node.condition());
        assertEquals(
            1,
            node.body()
                .size());
        assertEquals(
            bodyNode,
            node.body()
                .get(0));
    }

    @Test
    public void testIfNodeNullsGiveSafeDefaults() {
        USSNode node = USSNode.ifNode(null, null);
        assertEquals(
            USSConditionOp.EQ,
            node.condition()
                .op());
        assertTrue(
            node.body()
                .isEmpty());
    }

    @Test
    public void testWhileNode() {
        USSNode node = USSNode.whileNode(guard(), Arrays.asList(USSNode.command(USSCommand.MINE, null)));
        assertEquals(USSNodeType.WHILE, node.type());
        assertEquals(guard(), node.condition());
        assertEquals(
            1,
            node.body()
                .size());
    }

    @Test
    public void testRepeatClampsTheCount() {
        assertEquals(
            0,
            USSNode.repeat(-3, null)
                .count(),
            "negative counts clamp to 0");
        assertEquals(
            5,
            USSNode.repeat(5, null)
                .count());
        assertEquals(
            USSNode.MAX_REPEAT_COUNT,
            USSNode.repeat(1_000_000_000, null)
                .count(),
            "huge counts clamp to 65535");
        assertEquals(
            USSNodeType.REPEAT,
            USSNode.repeat(5, null)
                .type());
    }

    @Test
    public void testBodyRejectsNullEntries() {
        USSNode node = USSNode.whileNode(guard(), Arrays.asList(USSNode.command(USSCommand.STOP, null), null));
        assertEquals(
            1,
            node.body()
                .size());
    }

    @Test
    public void testBodyIsUnmodifiable() {
        USSNode node = USSNode.whileNode(guard(), Collections.singletonList(USSNode.command(USSCommand.STOP, null)));
        try {
            node.body()
                .clear();
            throw new AssertionError("the body list must be unmodifiable");
        } catch (UnsupportedOperationException expected) {
            // that is the point
        }
    }

    @Test
    public void testDepthAndSubtreeSize() {
        assertEquals(
            1,
            USSNode.command(USSCommand.STOP, null)
                .depth());
        assertEquals(
            1,
            USSNode.command(USSCommand.STOP, null)
                .subtreeSize());

        USSNode nested = USSNode.whileNode(
            guard(),
            Arrays.asList(
                USSNode.ifNode(guard(), Collections.singletonList(USSNode.command(USSCommand.STOP, null))),
                USSNode.command(USSCommand.MINE, null)));
        assertEquals(3, nested.depth(), "while{if{cmd}, cmd} is three levels deep");
        assertEquals(4, nested.subtreeSize(), "four nodes in the subtree");
    }

    @Test
    public void testNbtRoundTripCommand() {
        NBTTagCompound params = new NBTTagCompound();
        params.setString(USSProgramDefaults.PARAM_TARGET, USSProgramDefaults.TARGET_STAR);
        params.setInteger(USSProgramDefaults.PARAM_INDEX, 3);
        USSNode node = USSNode.command(USSCommand.MOVE, params);
        assertEquals(node, USSNode.readFromNBT(node.writeToNBT(), 1, new int[] { 0 }));
        NBTTagCompound back = USSNode.readFromNBT(node.writeToNBT(), 1, new int[] { 0 })
            .params();
        assertEquals(USSProgramDefaults.TARGET_STAR, back.getString(USSProgramDefaults.PARAM_TARGET));
        assertEquals(3, back.getInteger(USSProgramDefaults.PARAM_INDEX));
    }

    @Test
    public void testNbtRoundTripNestedBodies() {
        USSCondition guard = guard();
        USSNode node = USSNode.whileNode(
            guard,
            Arrays.asList(
                USSNode.ifNode(guard, Collections.singletonList(USSNode.command(USSCommand.STOP, null))),
                USSNode.repeat(9, Collections.singletonList(USSNode.command(USSCommand.WAIT, null)))));
        USSNode back = USSNode.readFromNBT(node.writeToNBT(), 1, new int[] { 0 });
        assertEquals(node, back);
        assertEquals(
            9,
            back.body()
                .get(1)
                .count());
    }

    @Test
    public void testReadNullGivesNull() {
        assertNull(USSNode.readFromNBT(null, 1, new int[] { 0 }));
    }

    @Test
    public void testReadUnknownTypeGivesNull() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(USSNode.TAG_TYPE, 99);
        assertNull(USSNode.readFromNBT(nbt, 1, new int[] { 0 }), "unknown node types are structural corruption");
    }

    @Test
    public void testReadExceedingTheNodeBudgetGivesNull() {
        int[] used = { USSProgram.MAX_NODES }; // the budget is already exhausted
        assertNull(
            USSNode.readFromNBT(
                USSNode.command(USSCommand.STOP, null)
                    .writeToNBT(),
                1,
                used));
    }

    @Test
    public void testReadExceedingTheDepthGivesNull() {
        NBTTagCompound nbt = USSNode.command(USSCommand.STOP, null)
            .writeToNBT();
        assertNull(
            USSNode.readFromNBT(nbt, USSProgram.MAX_DEPTH + 1, new int[] { 0 }),
            "a node past the max depth is corrupt");
    }

    @Test
    public void testReadNonCompoundBodyEntryGivesNull() {
        NBTTagCompound node = new NBTTagCompound();
        node.setInteger(USSNode.TAG_TYPE, USSNodeType.WHILE.getId());
        node.setTag(
            USSNode.TAG_CONDITION,
            USSCondition.of(USSValue.literal("a"), USSConditionOp.EQ, USSValue.literal("b"))
                .writeToNBT());
        net.minecraft.nbt.NBTTagList list = new net.minecraft.nbt.NBTTagList();
        list.appendTag(new net.minecraft.nbt.NBTTagByte((byte) 1));
        node.setTag(USSNode.TAG_BODY, list);
        assertNull(USSNode.readFromNBT(node, 1, new int[] { 0 }), "a non-compound body entry is structural corruption");
    }

    @Test
    public void testReadMissingTypeGivesNull() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(USSNode.TAG_CMD, USSCommand.STOP); // a payload with NO type key
        assertNull(
            USSNode.readFromNBT(nbt, 1, new int[] { 0 }),
            "a node without a type key is corruption (it must not read as COMMAND)");
    }
}
