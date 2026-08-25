package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the USS variable space ({@link USSVariableSpace}): the 256 slots, the unwritten-vs-written
 * distinction, immutability, the sparse NBT round-trip, and garbage tolerance.
 */
public class USSVariableSpaceTest {

    @Test
    public void testFreshSpaceReadsEmptyAndUnwritten() {
        USSVariableSpace space = USSVariableSpace.fresh();
        assertEquals(0, space.writtenCount());
        for (int slot : new int[] { 0, 1, 127, 254, 255 }) {
            assertEquals("", space.get(slot));
            assertFalse(space.isWritten(slot));
        }
    }

    @Test
    public void testGetOutOfRangeIsEmpty() {
        USSVariableSpace space = USSVariableSpace.fresh()
            .set(5, "abc");
        assertEquals("", space.get(-1));
        assertEquals("", space.get(256));
        assertFalse(space.isWritten(-1));
        assertFalse(space.isWritten(256));
    }

    @Test
    public void testSetWritesANewInstance() {
        USSVariableSpace original = USSVariableSpace.fresh();
        USSVariableSpace updated = original.set(5, "abc");
        assertNotEquals(original, updated);
        assertEquals("abc", updated.get(5));
        assertEquals("", original.get(5), "the original space is immutable");
        assertTrue(updated.isWritten(5));
    }

    @Test
    public void testSetOverwritesTheSlot() {
        USSVariableSpace space = USSVariableSpace.fresh()
            .set(7, "first")
            .set(7, "second");
        assertEquals("second", space.get(7));
        assertEquals(1, space.writtenCount(), "a slot written twice counts once");
    }

    @Test
    public void testSetNullValueWritesEmptyString() {
        USSVariableSpace space = USSVariableSpace.fresh()
            .set(9, null);
        assertEquals("", space.get(9));
        assertTrue(space.isWritten(9), "an explicit empty write is still a WRITTEN slot");
    }

    @Test
    public void testSetOutOfRangeIsANoOp() {
        USSVariableSpace original = USSVariableSpace.fresh();
        assertSame(original, original.set(-1, "x"), "negative slot → unchanged instance");
        assertSame(original, original.set(256, "x"), "slot 256 → unchanged instance");
    }

    @Test
    public void testWrittenCountTracksDistinctSlots() {
        USSVariableSpace space = USSVariableSpace.fresh()
            .set(0, "a")
            .set(255, "b")
            .set(0, "c");
        assertEquals(2, space.writtenCount());
    }

    @Test
    public void testBoundarySlots() {
        USSVariableSpace space = USSVariableSpace.fresh()
            .set(0, "lo")
            .set(255, "hi");
        assertEquals("lo", space.get(0));
        assertEquals("hi", space.get(255));
    }

    @Test
    public void testNbtRoundTripSparse() {
        USSVariableSpace space = USSVariableSpace.fresh()
            .set(3, "three")
            .set(200, "two hundred")
            .set(41, "");
        NBTTagList list = space.writeToNBT();
        assertEquals(3, list.tagCount(), "only the three WRITTEN slots are serialized (sparse)");
        assertEquals(space, USSVariableSpace.readFromNBT(list));
        assertEquals(
            3,
            USSVariableSpace.readFromNBT(list)
                .writtenCount());
    }

    @Test
    public void testNbtListIsSparseAndWellFormed() {
        NBTTagList list = USSVariableSpace.fresh()
            .set(7, "seven")
            .writeToNBT();
        NBTTagCompound tag = (NBTTagCompound) list.tagList.get(0); // 1.7.10: raw element via the public tagList field
        assertEquals(7, tag.getInteger("i"));
        assertEquals("seven", tag.getString("s"));
    }

    @Test
    public void testReadNullGivesFresh() {
        assertEquals(USSVariableSpace.fresh(), USSVariableSpace.readFromNBT(null));
    }

    @Test
    public void testReadDropsOutOfRangeSlotsAndNonCompounds() {
        NBTTagList list = new NBTTagList();
        NBTTagCompound inRange = new NBTTagCompound();
        inRange.setInteger("i", 10);
        inRange.setString("s", "ten");
        NBTTagCompound tooBig = new NBTTagCompound();
        tooBig.setInteger("i", 1000);
        tooBig.setString("s", "nope");
        NBTTagCompound negative = new NBTTagCompound();
        negative.setInteger("i", -4);
        negative.setString("s", "nope");
        List<Object> entries = new ArrayList<Object>();
        entries.add(inRange);
        entries.add(tooBig);
        entries.add(negative);
        entries.add(new net.minecraft.nbt.NBTTagByte((byte) 1)); // non-compound entry
        for (Object entry : entries) {
            list.appendTag((net.minecraft.nbt.NBTBase) entry);
        }
        USSVariableSpace space = USSVariableSpace.readFromNBT(list);
        assertEquals("ten", space.get(10));
        assertEquals(1, space.writtenCount(), "only the in-range slot survives");
    }

    @Test
    public void testEquality() {
        assertEquals(USSVariableSpace.fresh(), USSVariableSpace.fresh());
        assertEquals(
            USSVariableSpace.fresh()
                .set(1, "x"),
            USSVariableSpace.fresh()
                .set(1, "x"));
        assertNotEquals(
            USSVariableSpace.fresh()
                .set(1, "x"),
            USSVariableSpace.fresh()
                .set(1, "y"));
        assertNotEquals(
            USSVariableSpace.fresh()
                .set(1, "x"),
            USSVariableSpace.fresh()
                .set(2, "x"));
        assertEquals(
            USSVariableSpace.fresh()
                .set(1, "x")
                .hashCode(),
            USSVariableSpace.fresh()
                .set(1, "x")
                .hashCode());
    }
}
