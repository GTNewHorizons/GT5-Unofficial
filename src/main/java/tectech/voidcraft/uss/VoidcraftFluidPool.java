package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * The Voidcraft Storage Bay's shared FLUID cargo pool (Phase 4 pass 1): 16 slots that Starlifter missions deliver
 * into and that output hatches (incl. ME output hatches) move out of.
 *
 * <p>
 * Same design contract as {@link VoidcraftCargoPool}, with two deliberate differences:
 * <ul>
 * <li>Slots hold <em>abstract</em> entries — {@code {material name, amount (mB)}} — NOT {@code FluidStack}s. The
 * material name is the stable identity (the Forge fluid registry is a runtime detail; a pool entry must survive a
 * save/load and stay unit-testable in a bare JVM). The Fluid itself is resolved exactly once at the delivery
 * boundary (the bay's ejection/pump code, via {@code Materials.get(name).getFluid(mB)}).</li>
 * <li>The per-slot cap is generous (10_000 mB — one mission carries at most a few tens of thousands of mB), since
 * fluids are not stack-capped the way items are.</li>
 * </ul>
 *
 * <p>
 * Insertion is fluid-aware: the same material merges into an existing slot before taking an empty one.
 * Overflow that does not fit is simply not inserted — the caller keeps the remainder (the bay delivers the
 * remainder to the hatches on the next tick / at delivery time).
 */
public final class VoidcraftFluidPool {

    /** Slot count (mirrors the item pool's 4×4 face). */
    public static final int SLOTS = 16;

    /** Max mB per slot (10_000 L — far above a single mission's plasma; keeps 16 slots comfortably bounded). */
    public static final int MAX_PER_SLOT = 10_000_000;

    private static final String TAG_SLOTS = "vc_fluid_slots";
    private static final String TAG_MATERIAL = "vc_material";
    private static final String TAG_AMOUNT = "vc_amount";

    private final String[] materials = new String[SLOTS];
    private final long[] amounts = new long[SLOTS];
    private int count;

    public VoidcraftFluidPool() {}

    /**
     * Read a pool from NBT (tag written by {@link #writeToNBT()}).
     *
     * @param nbt the bay NBT (may be null)
     * @return the pool (an empty pool for missing/corrupt data — never null)
     */
    public static VoidcraftFluidPool readFromNBT(NBTTagCompound nbt) {
        VoidcraftFluidPool pool = new VoidcraftFluidPool();
        if (nbt == null || !nbt.hasKey(TAG_SLOTS)) {
            return pool;
        }
        NBTTagList list = nbt.getTagList(TAG_SLOTS, 10);
        for (int i = 0; i < list.tagCount() && i < SLOTS; i++) {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            if (entry == null) {
                continue;
            }
            String material = entry.getString(TAG_MATERIAL);
            long amount = entry.getLong(TAG_AMOUNT);
            if (material.isEmpty() || amount <= 0) {
                continue;
            }
            pool.materials[i] = material;
            pool.amounts[i] = Math.min(amount, MAX_PER_SLOT);
            pool.count++;
        }
        return pool;
    }

    /**
     * @param slot slot index 0..15
     * @return the material name in that slot (null if empty)
     */
    public String getMaterial(int slot) {
        if (slot < 0 || slot >= SLOTS) {
            return null;
        }
        return materials[slot];
    }

    /**
     * @param slot slot index 0..15
     * @return the amount in mB in that slot (0 if empty)
     */
    public long getAmount(int slot) {
        if (slot < 0 || slot >= SLOTS) {
            return 0;
        }
        return amounts[slot];
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
     * Insert as much of {@code amount} mB of {@code material} as fits, merging into a compatible slot first.
     *
     * @param material GT material name (the fluid's identity in the pool; empty/null rejects the whole amount)
     * @param amount   mB to insert (&lt;= 0 inserts nothing)
     * @return the amount accepted (the remainder is up to the caller — the pool never silently voids)
     */
    public long insert(String material, long amount) {
        if (material == null || material.isEmpty() || amount <= 0) {
            return 0;
        }
        long remaining = amount;

        // 1) merge into an existing slot of the same material
        for (int i = 0; i < SLOTS && remaining > 0; i++) {
            if (materials[i] == null || !materials[i].equals(material) || amounts[i] >= MAX_PER_SLOT) {
                continue;
            }
            long take = Math.min(remaining, MAX_PER_SLOT - amounts[i]);
            amounts[i] += take;
            remaining -= take;
        }

        // 2) take empty slots
        for (int i = 0; i < SLOTS && remaining > 0; i++) {
            if (materials[i] != null) {
                continue;
            }
            long take = Math.min(remaining, MAX_PER_SLOT);
            materials[i] = material;
            amounts[i] = take;
            count++;
            remaining -= take;
        }

        return amount - remaining;
    }

    /**
     * Remove up to {@code amount} mB from one slot.
     *
     * @param slot   slot index 0..15
     * @param amount mB to remove
     * @return the amount actually removed
     */
    public long remove(int slot, long amount) {
        if (slot < 0 || slot >= SLOTS || materials[slot] == null || amount <= 0) {
            return 0;
        }
        long take = Math.min(amount, amounts[slot]);
        amounts[slot] -= take;
        if (amounts[slot] <= 0) {
            materials[slot] = null;
            amounts[slot] = 0;
            count--;
        }
        return take;
    }

    /**
     * Serialize the pool (tag for the bay's NBT).
     */
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < SLOTS; i++) {
            if (materials[i] == null) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString(TAG_MATERIAL, materials[i]);
            entry.setLong(TAG_AMOUNT, amounts[i]);
            list.appendTag(entry);
        }
        nbt.setTag(TAG_SLOTS, list);
    }
}
