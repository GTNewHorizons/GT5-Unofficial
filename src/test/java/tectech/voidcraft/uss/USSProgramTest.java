package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the PROGRAM ({@link USSProgram}): the in-code factory, size/depth/nodeCount, the NBT
 * round-trip, and — the point of the caps — oversized / corrupt programs reading back as the EMPTY program.
 */
public class USSProgramTest {

    private static USSCondition guard() {
        return USSCondition.of(USSValue.literal("a"), USSConditionOp.EQ, USSValue.literal("b"));
    }

    private static USSNode cmd() {
        return USSNode.command(USSCommand.STOP, null);
    }

    @Test
    public void testEmptyProgram() {
        USSProgram p = USSProgram.empty();
        assertTrue(p.isEmpty());
        assertEquals(0, p.size());
        assertEquals(0, p.depth());
        assertEquals(0, p.nodeCount());
    }

    @Test
    public void testOfNullGivesEmptyAndDropsNullEntries() {
        assertTrue(
            USSProgram.of(null)
                .isEmpty());
        List<USSNode> nodes = new ArrayList<USSNode>();
        nodes.add(cmd());
        nodes.add(null);
        assertEquals(
            1,
            USSProgram.of(nodes)
                .size());
    }

    @Test
    public void testSizeDepthAndNodeCount() {
        USSNode nested = USSNode
            .whileNode(guard(), Arrays.asList(USSNode.ifNode(guard(), Collections.singletonList(cmd())), cmd()));
        USSProgram p = USSProgram.of(Arrays.asList(cmd(), nested));
        assertEquals(2, p.size(), "two ROOT nodes");
        assertEquals(3, p.depth(), "while{if{cmd}, cmd} → three levels");
        assertEquals(5, p.nodeCount(), "cmd + (while + if + cmd + cmd)");
    }

    @Test
    public void testNodeListIsUnmodifiable() {
        USSProgram p = USSProgram.of(Collections.singletonList(cmd()));
        try {
            p.nodes()
                .clear();
            throw new AssertionError("the node list must be unmodifiable");
        } catch (UnsupportedOperationException expected) {
            // that is the point
        }
    }

    @Test
    public void testNbtRoundTrip() {
        USSProgram p = USSProgram.of(
            Arrays.asList(
                USSNode.command(USSCommand.MOVE, new NBTTagCompound()),
                USSNode.repeat(3, Collections.singletonList(cmd())),
                USSNode.whileNode(guard(), Collections.singletonList(cmd()))));
        USSProgram back = USSProgram.readFromNBT(p.writeToNBT());
        assertEquals(p, back);
        assertEquals(p.nodeCount(), back.nodeCount(), "nodeCount is stable across the round-trip");
    }

    @Test
    public void testNbtRoundTripEmpty() {
        assertEquals(
            USSProgram.empty(),
            USSProgram.readFromNBT(
                USSProgram.empty()
                    .writeToNBT()));
    }

    @Test
    public void testReadNullListGivesEmpty() {
        assertTrue(
            USSProgram.readFromNBT(null)
                .isEmpty());
    }

    @Test
    public void testProgramOverTheNodeCapReadsBackEmpty() {
        List<USSNode> nodes = new ArrayList<USSNode>();
        for (int i = 0; i < USSProgram.MAX_NODES + 1; i++) {
            nodes.add(cmd());
        }
        USSProgram p = USSProgram.of(nodes);
        assertEquals(USSProgram.MAX_NODES + 1, p.nodeCount());
        assertTrue(
            USSProgram.readFromNBT(p.writeToNBT())
                .isEmpty(),
            "256 nodes breaches the 255 cap → the whole program is dropped");
    }

    @Test
    public void testProgramAtTheNodeCapReadsBackIntact() {
        List<USSNode> nodes = new ArrayList<USSNode>();
        for (int i = 0; i < USSProgram.MAX_NODES; i++) {
            nodes.add(cmd());
        }
        USSProgram p = USSProgram.of(nodes);
        assertEquals(p, USSProgram.readFromNBT(p.writeToNBT()), "exactly 255 nodes is allowed");
    }

    @Test
    public void testProgramOverTheDepthCapReadsBackEmpty() {
        USSNode node = cmd();
        for (int i = 0; i < USSProgram.MAX_DEPTH; i++) {
            node = USSNode.whileNode(guard(), Collections.singletonList(node));
        }
        USSProgram p = USSProgram.of(Collections.singletonList(node));
        assertEquals(USSProgram.MAX_DEPTH + 1, p.depth());
        assertTrue(
            USSProgram.readFromNBT(p.writeToNBT())
                .isEmpty(),
            "9 nesting levels breaches the 8 cap → the whole program is dropped");
    }

    @Test
    public void testProgramAtTheDepthCapReadsBackIntact() {
        USSNode node = cmd();
        for (int i = 0; i < USSProgram.MAX_DEPTH - 1; i++) {
            node = USSNode.whileNode(guard(), Collections.singletonList(node));
        }
        USSProgram p = USSProgram.of(Collections.singletonList(node));
        assertEquals(USSProgram.MAX_DEPTH, p.depth());
        assertEquals(p, USSProgram.readFromNBT(p.writeToNBT()), "exactly 8 nesting levels is allowed");
    }

    @Test
    public void testNestedCorruptionDropsTheWholeProgram() {
        NBTTagCompound badChild = new NBTTagCompound();
        badChild.setInteger(USSNode.TAG_TYPE, 42); // an unknown node type
        NBTTagList badBody = new NBTTagList();
        badBody.appendTag(badChild);
        NBTTagCompound brokenNode = USSNode.whileNode(guard(), Collections.singletonList(cmd()))
            .writeToNBT();
        brokenNode.setTag(USSNode.TAG_BODY, badBody);
        assertTrue(
            USSProgram.readFromNBT(listOf(brokenNode))
                .isEmpty(),
            "a corrupt child deep in the body → the whole program is dropped");
    }

    @Test
    public void testNonCompoundRootEntryDropsTheProgram() {
        NBTTagList list = new NBTTagList();
        list.appendTag(new net.minecraft.nbt.NBTTagByte((byte) 7));
        assertTrue(
            USSProgram.readFromNBT(list)
                .isEmpty());
    }

    @Test
    public void testLiteralOverTheCapIsTruncatedOnWrite() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            sb.append('z');
        }
        USSValue v = USSValue.literal(sb.toString());
        USSProgram p = USSProgram
            .of(Collections.singletonList(USSNode.command(USSCommand.WRITE, paramsOf("value", v.literal()))));
        USSProgram back = USSProgram.readFromNBT(p.writeToNBT());
        assertEquals(
            v.literal(),
            back.nodes()
                .get(0)
                .params()
                .getString("value"),
            "the 255-char literal survives the round-trip (it was truncated at construction)");
    }

    private static NBTTagCompound paramsOf(String key, String value) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString(key, value);
        return nbt;
    }

    private static NBTTagList listOf(NBTTagCompound... nodes) {
        NBTTagList list = new NBTTagList();
        for (NBTTagCompound n : nodes) {
            list.appendTag(n);
        }
        return list;
    }
}
