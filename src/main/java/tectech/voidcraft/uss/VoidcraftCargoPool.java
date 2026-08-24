package tectech.voidcraft.uss;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * The Voidcraft Storage Bay's shared cargo pool: 16 slots (a 4×4 face) that mining ships deliver into and that
 * hatches move out of.
 *
 * <p>
 * The pool is deliberately <em>not</em> an {@code IInventory}: it is a plain stack array with merge-aware
 * insert/remove, so the bay MTE drives all bus traffic (input bus → pool, pool → output bus) in
 * {@code onPostTick} and the pool itself stays pure Java + NBT (unit-testable).
 *
 * <p>
 * Insertion is stack-aware: items merge into an existing compatible slot before taking an empty one (same item id,
 * same damage, no more than 64 per slot). Overflow that does not fit is simply not inserted — the caller keeps
 * the remainder (the bay re-pulls it from the input bus next tick).
 */
public final class VoidcraftCargoPool {

    /** Slot count (4×4 face). */
    public static final int SLOTS = 16;

    /** Max items per slot (vanilla stack size). */
    public static final int MAX_PER_SLOT = 64;

    private static final String TAG_SLOTS = "vc_slots";
    private static final String TAG_COUNT = "vc_count";

    private final ItemStack[] slots = new ItemStack[SLOTS];
    private int count;

    public VoidcraftCargoPool() {
        for (int i = 0; i < SLOTS; i++) {
            this.slots[i] = null;
        }
    }

    /**
     * Read a pool from NBT (tag written by {@link #writeToNBT()}).
     *
     * @param nbt the bay NBT (may be null)
     * @return the pool (an empty pool for missing/corrupt data — never null)
     */
    public static VoidcraftCargoPool readFromNBT(NBTTagCompound nbt) {
        VoidcraftCargoPool pool = new VoidcraftCargoPool();
        if (nbt == null || !nbt.hasKey(TAG_SLOTS)) {
            return pool;
        }
        NBTTagList list = nbt.getTagList(TAG_SLOTS, 10);
        for (int i = 0; i < list.tagCount() && i < SLOTS; i++) {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            if (entry == null || !entry.hasKey(TAG_COUNT) || entry.getInteger(TAG_COUNT) <= 0) {
                continue;
            }
            ItemStack stack = ItemStack.loadItemStackFromNBT(entry);
            if (stack == null) {
                continue;
            }
            if (stack.stackSize > MAX_PER_SLOT) {
                stack.stackSize = MAX_PER_SLOT;
            }
            pool.slots[i] = stack;
            pool.count++;
        }
        return pool;
    }

    /**
     * @param slot slot index 0..15
     * @return the stack in that slot (null if empty)
     */
    public ItemStack get(int slot) {
        if (slot < 0 || slot >= SLOTS) {
            return null;
        }
        return slots[slot];
    }

    /**
     * @return number of occupied slots
     */
    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * True when every slot is full (no free space and no compatible stack left to merge into).
     */
    public boolean isFull(ItemStack candidate) {
        if (candidate == null) {
            return true;
        }
        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = slots[i];
            if (stack == null) {
                return false; // an empty slot always has room
            }
            if (stack.stackSize < MAX_PER_SLOT && canStack(stack, candidate)) {
                return false; // a mergeable partial slot has room
            }
            // occupied (full or foreign) slot: no room for this candidate
        }
        return true;
    }

    /**
     * Insert as much of {@code stack} as fits (merge-aware).
     *
     * @param stack the items to insert (mutated: {@code stackSize} reduced by the inserted amount)
     * @return the number of items actually inserted
     */
    public int insert(ItemStack stack) {
        if (stack == null || stack.stackSize <= 0) {
            return 0;
        }
        int inserted = 0;
        // Pass 1: merge into existing compatible slots.
        for (int i = 0; i < SLOTS && stack.stackSize > 0; i++) {
            ItemStack slot = slots[i];
            if (slot == null || !canStack(slot, stack)) {
                continue;
            }
            int space = MAX_PER_SLOT - slot.stackSize;
            int take = Math.min(space, stack.stackSize);
            slot.stackSize += take;
            stack.stackSize -= take;
            inserted += take;
        }
        // Pass 2: empty slots.
        for (int i = 0; i < SLOTS && stack.stackSize > 0; i++) {
            if (slots[i] != null) {
                continue;
            }
            int take = Math.min(MAX_PER_SLOT, stack.stackSize);
            slots[i] = stack.copy();
            slots[i].stackSize = take;
            stack.stackSize -= take;
            inserted += take;
            count++;
        }
        return inserted;
    }

    /**
     * Remove up to {@code amount} items from one slot (merge-agnostic: the caller picks the slot).
     *
     * @param slot   slot index 0..15
     * @param amount items to remove (>= 1)
     * @return the removed stack (null if the slot was empty), with a stackSize of at most {@code amount}
     */
    public ItemStack remove(int slot, int amount) {
        if (slot < 0 || slot >= SLOTS || amount <= 0) {
            return null;
        }
        ItemStack stack = slots[slot];
        if (stack == null) {
            return null;
        }
        int take = Math.min(amount, stack.stackSize);
        ItemStack removed = stack.copy();
        removed.stackSize = take;
        stack.stackSize -= take;
        if (stack.stackSize <= 0) {
            slots[slot] = null;
            count--;
        }
        return removed;
    }

    /**
     * Remove every item of a given type across the pool (used when the bay is torn down: the stacks are dropped).
     *
     * @return a single merged list of everything removed (empty list if the pool was empty)
     */
    public NBTTagList drainAll() {
        NBTTagList out = new NBTTagList();
        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = slots[i];
            if (stack == null) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            stack.writeToNBT(entry);
            entry.setInteger("Slot", i);
            out.appendTag(entry);
            slots[i] = null;
            count--;
        }
        return out;
    }

    /**
     * Serialize the pool (tag for the bay's NBT).
     */
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = slots[i];
            if (stack == null) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            stack.writeToNBT(entry);
            entry.setInteger(TAG_COUNT, stack.stackSize);
            list.appendTag(entry);
        }
        nbt.setTag(TAG_SLOTS, list);
    }

    private static boolean canStack(ItemStack a, ItemStack b) {
        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage();
    }
}
