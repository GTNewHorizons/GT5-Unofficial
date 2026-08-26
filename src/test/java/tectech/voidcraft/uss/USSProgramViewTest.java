package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the pass-33 program ROW VIEW ({@link USSProgramView}) â€” the flat Scratch-style block list the
 * Controller GUI renders: depth / path / label per node, the argument slots each block shows (including a WRITE
 * value held as a USS reference), the flat visual order (children immediately after their parent), and the row
 * wire form (list sync).
 */
public class USSProgramViewTest {

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

    private static USSNode moveIndexed(String target, int index) {
        NBTTagCompound a = new NBTTagCompound();
        a.setString(USSProgramDefaults.PARAM_TARGET, target);
        a.setInteger(USSProgramDefaults.PARAM_INDEX, index);
        return USSNode.command(USSCommand.MOVE, a);
    }

    private static USSNode write(int slot, String value) {
        NBTTagCompound a = new NBTTagCompound();
        a.setInteger(USSCommandWrite.PARAM_SLOT, slot);
        a.setString(USSCommandWrite.PARAM_VALUE, value);
        return USSNode.command(USSCommand.WRITE, a);
    }

    /** A WRITE whose value is a USS VARIABLE reference (the slot-assignment feature, pass 33 UI). */
    private static USSNode writeVar(int slot, int varSlot) {
        NBTTagCompound a = new NBTTagCompound();
        a.setInteger(USSCommandWrite.PARAM_SLOT, slot);
        a.setTag(
            USSCommandWrite.PARAM_VALUE,
            USSValue.variable(varSlot)
                .writeToNBT());
        return USSNode.command(USSCommand.WRITE, a);
    }

    private static USSNode ifNode(List<USSNode> body) {
        return USSNode.ifNode(USSCondition.of(USSValue.literal("1"), USSConditionOp.EQ, USSValue.variable(3)), body);
    }

    private static USSProgram program(USSNode... nodes) {
        return USSProgram.of(new ArrayList<USSNode>(Arrays.asList(nodes)));
    }

    // region rows

    @Test
    public void testRootOrderDepthPathAndLabel() {
        USSProgram p = program(move("HOME"), work(), write(4, "hello"));
        List<USSProgramView.Row> rows = USSProgramView.rows(p);
        assertEquals(3, rows.size());

        assertEquals(1, rows.get(0).depth);
        assertArrayEquals(path(0), rows.get(0).path);
        assertEquals("MOVE", rows.get(0).label);

        assertArrayEquals(path(1), rows.get(1).path);
        assertEquals("WORK", rows.get(1).label);
        assertTrue(rows.get(1).slots.isEmpty());
        assertFalse(rows.get(1).hasBody);

        assertArrayEquals(path(2), rows.get(2).path);
        assertEquals("WRITE", rows.get(2).label);
    }

    @Test
    public void testChildrenFollowTheirParentIndented() {
        USSProgram p = program(ifNode(Arrays.asList(work(), work())), move("HOME"));
        List<USSProgramView.Row> rows = USSProgramView.rows(p);
        assertEquals(4, rows.size());

        assertEquals("IF", rows.get(0).label);
        assertTrue(rows.get(0).hasBody);
        assertEquals(1, rows.get(0).depth);

        assertEquals(2, rows.get(1).depth);
        assertArrayEquals(path(0, 0), rows.get(1).path);
        assertEquals("WORK", rows.get(1).label);

        assertEquals(2, rows.get(2).depth);
        assertArrayEquals(path(0, 1), rows.get(2).path);

        // the next root comes after the whole IF subtree
        assertEquals(1, rows.get(3).depth);
        assertArrayEquals(path(1), rows.get(3).path);
    }

    @Test
    public void testDeepNestingPaths() {
        USSNode inner = USSNode.repeat(2, Arrays.asList(work()));
        USSProgram p = program(ifNode(Arrays.asList(ifNode(Arrays.asList(inner)))));
        List<USSProgramView.Row> rows = USSProgramView.rows(p);
        assertEquals(4, rows.size());
        assertArrayEquals(path(0), rows.get(0).path); // IF
        assertArrayEquals(path(0, 0), rows.get(1).path); // IF
        assertArrayEquals(path(0, 0, 0), rows.get(2).path); // REPEAT
        assertArrayEquals(path(0, 0, 0, 0), rows.get(3).path); // WORK
        assertEquals(4, rows.get(3).depth);
    }

    @Test
    public void testNullAndEmptyPrograms() {
        assertTrue(
            USSProgramView.rows(null)
                .isEmpty());
        assertTrue(
            USSProgramView.rows(USSProgram.empty())
                .isEmpty());
        assertTrue(
            USSProgramView.rowsJsonList(null)
                .isEmpty());
    }

    // endregion

    // region slots

    @Test
    public void testMoveSlots() {
        List<USSProgramView.Row> rows = USSProgramView.rows(program(move("HOME")));
        List<USSProgramView.Slot> slots = rows.get(0).slots;
        assertEquals(1, slots.size());
        assertEquals("target", slots.get(0).label);
        assertEquals("HOME", slots.get(0).display);
        assertFalse(slots.get(0).isOp);

        rows = USSProgramView.rows(program(moveIndexed("PLANET", 2)));
        assertEquals(2, rows.get(0).slots.size());
        assertEquals("index", rows.get(0).slots.get(1).label);
        assertEquals("2", rows.get(0).slots.get(1).display);
    }

    @Test
    public void testWriteSlotsLiteralAndReference() {
        List<USSProgramView.Row> rows = USSProgramView.rows(program(write(4, "hello")));
        assertEquals(2, rows.get(0).slots.size());
        assertEquals("value", rows.get(0).slots.get(0).label);
        assertEquals("hello", rows.get(0).slots.get(0).display);
        assertEquals("slot", rows.get(0).slots.get(1).label);
        assertEquals("4", rows.get(0).slots.get(1).display);

        rows = USSProgramView.rows(program(writeVar(9, 17)));
        assertEquals("VAR 17", rows.get(0).slots.get(0).display);
    }

    @Test
    public void testReadWaitStopSlots() {
        USSProgram p = program(
            USSNode.command(USSCommand.READ, set2("from", 1, "to", 2)),
            USSNode.command(USSCommand.WAIT, setTicks(123)),
            USSNode.command(USSCommand.STOP, new NBTTagCompound()));
        List<USSProgramView.Row> rows = USSProgramView.rows(p);

        assertEquals("from", rows.get(0).slots.get(0).label);
        assertEquals("1", rows.get(0).slots.get(0).display);
        assertEquals("to", rows.get(0).slots.get(1).label);
        assertEquals("2", rows.get(0).slots.get(1).display);

        assertEquals("ticks", rows.get(1).slots.get(0).label);
        assertEquals("123", rows.get(1).slots.get(0).display);

        assertTrue(rows.get(2).slots.isEmpty());
    }

    private static NBTTagCompound set2(String k1, int v1, String k2, int v2) {
        NBTTagCompound a = new NBTTagCompound();
        a.setInteger(k1, v1);
        a.setInteger(k2, v2);
        return a;
    }

    private static NBTTagCompound setTicks(long ticks) {
        NBTTagCompound a = new NBTTagCompound();
        a.setLong(USSCommandWait.PARAM_TICKS, ticks);
        return a;
    }

    @Test
    public void testConditionSlots() {
        USSProgram p = program(ifNode(Arrays.asList(work())));
        List<USSProgramView.Slot> slots = USSProgramView.rows(p)
            .get(0).slots;
        assertEquals(3, slots.size());
        assertEquals("left", slots.get(0).label);
        assertEquals("1", slots.get(0).display);
        assertEquals("op", slots.get(1).label);
        assertEquals("EQ", slots.get(1).display);
        assertTrue(slots.get(1).isOp);
        assertEquals("right", slots.get(2).label);
        assertEquals("VAR 3", slots.get(2).display);
    }

    @Test
    public void testRepeatCountSlot() {
        USSProgram p = program(USSNode.repeat(7, Arrays.asList(work())));
        List<USSProgramView.Slot> slots = USSProgramView.rows(p)
            .get(0).slots;
        assertEquals(1, slots.size());
        assertEquals("count", slots.get(0).label);
        assertEquals("7", slots.get(0).display);
        assertFalse(slots.get(0).isOp);
    }

    @Test
    public void testValueDisplay() {
        assertEquals("abc", USSProgramView.valueDisplay(USSValue.literal("abc")));
        assertEquals("VAR 0", USSProgramView.valueDisplay(USSValue.variable(0)));
        assertEquals("VAR 255", USSProgramView.valueDisplay(USSValue.variable(255)));
        assertEquals("STAT 5", USSProgramView.valueDisplay(USSValue.stat(5)));
        assertEquals("", USSProgramView.valueDisplay(null));
    }

    // endregion

    // region wire form

    @Test
    public void testRowJsonRoundTrip() {
        USSProgram p = program(ifNode(Arrays.asList(work(), writeVar(1, 42))), move("HOME"));
        for (USSProgramView.Row row : USSProgramView.rows(p)) {
            String json = USSProgramView.rowToJson(row);
            USSProgramView.Row back = USSProgramView.rowFromJson(json);
            assertEquals(row.depth, back.depth);
            assertEquals(row.label, back.label);
            assertEquals(Arrays.toString(row.path), Arrays.toString(back.path));
            assertEquals(row.slots.size(), back.slots.size());
            for (int i = 0; i < row.slots.size(); i++) {
                assertEquals(row.slots.get(i).label, back.slots.get(i).label);
                assertEquals(row.slots.get(i).display, back.slots.get(i).display);
                assertEquals(row.slots.get(i).isOp, back.slots.get(i).isOp);
            }
        }
    }

    @Test
    public void testRowJsonMalformed() {
        assertNull(USSProgramView.rowFromJson("not json at all"));
        assertNull(USSProgramView.rowFromJson("{\"d\":1}"));
        assertNull(USSProgramView.rowFromJson(null));
    }

    @Test
    public void testRowsJsonListMatchesRows() {
        USSProgram p = program(ifNode(Arrays.asList(work())), move("HOME"));
        List<String> json = USSProgramView.rowsJsonList(p);
        assertEquals(
            USSProgramView.rows(p)
                .size(),
            json.size());
        for (String s : json) {
            assertNotNull(USSProgramView.rowFromJson(s));
        }
    }

    // endregion
}
