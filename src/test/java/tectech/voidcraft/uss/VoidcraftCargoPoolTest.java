package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the bay cargo pool ({@link VoidcraftCargoPool}) — merge-aware insert, slot capacity,
 * remove/drain, and the NBT round-trip.
 *
 * <p>
 * Bare-JVM note: vanilla {@code Items.*} fields are all {@code null} here (they resolve through the
 * FML-controlled item registry, which is empty without a game). The test therefore uses two fresh
 * {@link Item} instances registered via {@link BareJvmItemRegistry} — distinct item instances stand in for two
 * distinct item ids, and the registry entry makes the {@code ItemStack} NBT id round-trip work.
 */
public class VoidcraftCargoPoolTest {

    /** First registered test item ("kind A"). Ids must fit in a signed short (NBT). */
    private static final Item ITEM_A = BareJvmItemRegistry.register(new Item(), 30001);

    /** Second registered test item ("kind B"). */
    private static final Item ITEM_B = BareJvmItemRegistry.register(new Item(), 30002);

    @BeforeAll
    public static void sanity() {
        // Fail loudly (with a clear message) if the bare-JVM registry shim stops working in this environment.
        assertEquals(30001, Item.getIdFromItem(ITEM_A));
        assertEquals(ITEM_A, Item.getItemById(30001));
        assertEquals(ITEM_B, Item.getItemById(30002));
    }

    private static ItemStack dustA(int size) {
        return new ItemStack(ITEM_A, size);
    }

    private static ItemStack dustB(int size) {
        return new ItemStack(ITEM_B, size);
    }

    // region insert

    @Test
    public void testInsertIntoEmptyPool() {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        ItemStack stack = dustA(10);
        int inserted = pool.insert(stack);
        assertEquals(10, inserted);
        assertEquals(0, stack.stackSize, "consumed from the caller's stack");
        assertEquals(1, pool.size());
        assertEquals(10, pool.get(0).stackSize);
        assertEquals(
            ITEM_A,
            pool.get(0)
                .getItem());
    }

    @Test
    public void testInsertMergesIntoExistingSlot() {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        pool.insert(dustA(10));
        assertEquals(1, pool.size());

        ItemStack stack = dustA(10);
        int inserted = pool.insert(stack);
        assertEquals(10, inserted);
        assertEquals(1, pool.size(), "merged into the existing slot, no new slot taken");
        assertEquals(20, pool.get(0).stackSize);
    }

    @Test
    public void testInsertSplitsAcrossSlots() {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        ItemStack stack = dustA(100);
        int inserted = pool.insert(stack);
        assertEquals(100, inserted, "two empty slots hold all 100");
        assertEquals(0, stack.stackSize, "fully consumed");
        assertEquals(2, pool.size());
        assertEquals(64, pool.get(0).stackSize, "first slot capped at 64");
        assertEquals(36, pool.get(1).stackSize, "remainder in the second slot");
    }

    @Test
    public void testInsertDoesNotMixItemKinds() {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        pool.insert(dustA(64));
        ItemStack stack = dustB(10);
        int inserted = pool.insert(stack);
        assertEquals(10, inserted);
        assertEquals(2, pool.size(), "different item → new slot");
        assertEquals(64, pool.get(0).stackSize);
        assertEquals(
            ITEM_A,
            pool.get(0)
                .getItem());
        assertEquals(10, pool.get(1).stackSize);
        assertEquals(
            ITEM_B,
            pool.get(1)
                .getItem());
    }

    // endregion

    // region capacity

    @Test
    public void testInsertOverflowLeavesRemainder() {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        for (int i = 0; i < VoidcraftCargoPool.SLOTS; i++) {
            pool.insert(dustA(64));
        }
        assertEquals(VoidcraftCargoPool.SLOTS, pool.size());

        ItemStack stack = dustA(5);
        int inserted = pool.insert(stack);
        assertEquals(0, inserted, "full pool, nothing to merge into → nothing inserted");
        assertEquals(5, stack.stackSize, "caller keeps everything");
    }

    @Test
    public void testIsFull() {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        assertFalse(pool.isFull(dustA(1)));

        for (int i = 0; i < VoidcraftCargoPool.SLOTS; i++) {
            pool.insert(dustA(64));
        }
        assertTrue(pool.isFull(dustA(1)), "all slots at 64 → full");
        assertTrue(pool.isFull(dustB(1)), "no free slot and no compatible slot for item B either → full");
        assertTrue(pool.isFull(null), "null candidate → full by definition");
    }

    // endregion

    // region remove + drain

    @Test
    public void testRemovePartialAndFull() {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        pool.insert(dustA(20));

        ItemStack out = pool.remove(0, 5);
        assertEquals(5, out.stackSize);
        assertEquals(15, pool.get(0).stackSize, "partial remove leaves the rest");

        out = pool.remove(0, 99);
        assertEquals(15, out.stackSize, "asked for more than held → only the remainder");
        assertNull(pool.get(0), "slot emptied");
        assertTrue(pool.isEmpty());
    }

    @Test
    public void testRemoveUnknownSlot() {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        pool.insert(dustA(20));
        assertNull(pool.remove(-1, 5));
        assertNull(pool.remove(16, 5));
        assertEquals(1, pool.size(), "out-of-range remove is a no-op");
    }

    @Test
    public void testDrainAll() {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        pool.insert(dustA(10));
        pool.insert(dustB(30));

        NBTTagList out = pool.drainAll();
        assertEquals(2, out.tagCount(), "one entry per occupied slot");
        assertTrue(pool.isEmpty(), "pool empty after drain");
        assertEquals(0, pool.size());
    }

    // endregion

    // region NBT round-trip

    @Test
    public void testNbtRoundTrip() {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        pool.insert(dustA(10));
        pool.insert(dustB(64));

        NBTTagCompound nbt = new NBTTagCompound();
        pool.writeToNBT(nbt);
        VoidcraftCargoPool restored = VoidcraftCargoPool.readFromNBT(nbt);

        assertEquals(2, restored.size());
        assertEquals(10, restored.get(0).stackSize);
        assertEquals(
            ITEM_A,
            restored.get(0)
                .getItem());
        assertEquals(64, restored.get(1).stackSize);
        assertEquals(
            ITEM_B,
            restored.get(1)
                .getItem());
    }

    @Test
    public void testReadFromCorruptData() {
        assertEquals(
            0,
            VoidcraftCargoPool.readFromNBT(null)
                .size(),
            "null → empty pool");
        assertEquals(
            0,
            VoidcraftCargoPool.readFromNBT(new NBTTagCompound())
                .size(),
            "missing list → empty pool");

        NBTTagCompound bad = new NBTTagCompound();
        bad.setTag("vc_slots", new NBTTagList());
        assertEquals(
            0,
            VoidcraftCargoPool.readFromNBT(bad)
                .size(),
            "empty list → empty pool");
    }

    @Test
    public void testReadClampsStackSize() {
        // Hand-craft an entry whose vc_count is above the slot max: readFromNBT must clamp to 64.
        // (The vanilla Count field is a byte, so the oversized count is only visible through vc_count.)
        NBTTagCompound entry = new NBTTagCompound();
        ItemStack stack = new ItemStack(ITEM_A);
        stack.stackSize = 64;
        stack.writeToNBT(entry);
        entry.setInteger("vc_count", 9999);

        NBTTagList list = new NBTTagList();
        list.appendTag(entry);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("vc_slots", list);

        VoidcraftCargoPool pool = VoidcraftCargoPool.readFromNBT(nbt);
        assertEquals(1, pool.size());
        assertEquals(64, pool.get(0).stackSize, "over-cap counts clamp to the slot max");
    }

    // endregion
}
