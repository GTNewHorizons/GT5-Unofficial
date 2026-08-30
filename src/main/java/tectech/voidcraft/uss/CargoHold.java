package tectech.voidcraft.uss;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import gregtech.api.enums.Materials;

/**
 * The internal cargo of a Voidcraft (the cargo-capacity pass): a bounded hold that the ship's mining, starlifting
 * and construction fill, and that limits what the ship can carry.
 *
 * <p>
 * <strong>Capacity model.</strong> The hold has a fixed capacity in <em>cargo units</em>, where
 * {@code 1 unit = 1 item = 100 mB of fluid} (user spec). Items consume their count in units; fluids consume
 * {@code mB / 100} units. Adding cargo is clamped by the remaining capacity — a full hold simply stops accepting
 * more (the ship "cannot mine if it is full").
 *
 * <p>
 * <strong>Generic framework.</strong> The hold is the single source of truth for a Voidcraft's cargo, with a small
 * immutable API: {@link #addItems}/{@link #addFluids} (fill), {@link #removeItems}/{@link #removeFluids} (empty),
 * and {@link #transferTo} (the generic ship-to-ship transfer primitive — the gameplay around it is not implemented
 * yet, but this is the cargo-level operation it will use). All operations return a NEW instance (the hold is
 * immutable), so the ship's current hold is never mutated in place.
 *
 * <p>
 * Bare-JVM safe: only {@link Materials} data + plain long amounts (no Forge {@code Fluid}/{@code FluidStack}/
 * {@code ItemStack} objects), so the capacity math is unit-testable without a live ore dictionary or fluid registry.
 * The item/fluid resolution happens at the delivery boundary (the bay), like {@link USSShipCargo}.
 */
public final class CargoHold {

    /** 1 cargo unit = 100 mB of fluid (user spec: "1 cargo unit = 1 item = 100L fluid"). */
    public static final long MB_PER_UNIT = 100L;

    // NBT tags (voidcraft "vc_" naming convention).
    public static final String TAG_CAPACITY = "vc_hold_capacity";
    public static final String TAG_ITEMS = "vc_hold_items";
    public static final String TAG_FLUIDS = "vc_hold_fluids";
    public static final String TAG_SPECIAL = "vc_hold_special";
    public static final String ENTRY_MATERIAL = "m";
    public static final String ENTRY_AMOUNT = "a";
    public static final String ENTRY_KEY = "k";

    /** The hold's capacity in cargo units (1 unit = 1 item = 100 mB). */
    private final long capacity;

    /** Items on board: GT material → item count. */
    private final Map<Materials, Long> items;

    /** Fluids on board: GT material → mB count. */
    private final Map<Materials, Long> fluids;

    /**
     * Special (non-GT-material) cargo on board: stable string key → item count. This axis holds infrastructure
     * payloads (e.g. the Power Satellite) that are not GT {@link Materials} and so cannot ride the {@code items}
     * axis. Each special item consumes 1 cargo unit, like a normal item.
     */
    private final Map<String, Long> special;

    private CargoHold(long capacity, Map<Materials, Long> items, Map<Materials, Long> fluids,
        Map<String, Long> special) {
        this.capacity = Math.max(0L, capacity);
        this.items = new LinkedHashMap<>(items);
        this.fluids = new LinkedHashMap<>(fluids);
        this.special = new LinkedHashMap<>(special);
    }

    /**
     * @param capacity the hold's capacity in cargo units (1 unit = 1 item = 100 mB; negative clamped to 0)
     * @return a fresh, empty hold with the given capacity
     */
    public static CargoHold of(long capacity) {
        return new CargoHold(
            capacity,
            new LinkedHashMap<Materials, Long>(),
            new LinkedHashMap<Materials, Long>(),
            new LinkedHashMap<String, Long>());
    }

    /**
     * @return an empty hold with ZERO capacity (the defensive default — holds nothing)
     */
    public static CargoHold empty() {
        return new CargoHold(
            0L,
            new LinkedHashMap<Materials, Long>(),
            new LinkedHashMap<Materials, Long>(),
            new LinkedHashMap<String, Long>());
    }

    // region queries

    /**
     * @return the hold's capacity in cargo units
     */
    public long getCapacity() {
        return capacity;
    }

    /**
     * @return the used capacity in cargo units — the item count plus {@code mB / 100} for each fluid, plus the
     *         special-item count
     */
    public long usedUnits() {
        long units = 0L;
        for (Long count : items.values()) {
            units += count;
        }
        for (Long mB : fluids.values()) {
            units += mB / MB_PER_UNIT;
        }
        for (Long count : special.values()) {
            units += count;
        }
        return units;
    }

    /**
     * @return the remaining capacity in cargo units (never negative)
     */
    public long remainingUnits() {
        return Math.max(0L, capacity - usedUnits());
    }

    /**
     * @return true when the hold is at (or beyond) its capacity — the ship cannot take on more cargo
     */
    public boolean isFull() {
        return remainingUnits() <= 0L;
    }

    /**
     * @return true when the hold carries no items and no fluids
     */
    public boolean isEmpty() {
        return usedUnits() == 0L;
    }

    /**
     * @param material the ore material (null → 0)
     * @return the item count on board for the material (0 when absent)
     */
    public long itemsOf(Materials material) {
        if (material == null) {
            return 0L;
        }
        Long v = items.get(material);
        return v == null ? 0L : v;
    }

    /**
     * @param material the fluid material (null → 0)
     * @return the mB count on board for the material (0 when absent)
     */
    public long fluidsOf(Materials material) {
        if (material == null) {
            return 0L;
        }
        Long v = fluids.get(material);
        return v == null ? 0L : v;
    }

    /**
     * @return an unmodifiable view of the item counts (material → count)
     */
    public Map<Materials, Long> getItems() {
        return Collections.unmodifiableMap(items);
    }

    /**
     * @return an unmodifiable view of the fluid amounts (material → mB)
     */
    public Map<Materials, Long> getFluids() {
        return Collections.unmodifiableMap(fluids);
    }

    // endregion

    // region modifications (immutable — each returns a new hold)

    /**
     * Add items to the hold, clamped by the remaining capacity (1 item = 1 unit).
     *
     * @param material the item material (null / _NULL → this hold unchanged)
     * @param count    the item count to add (≤ 0 → this hold unchanged)
     * @return a new hold with the (clamped) items added
     */
    public CargoHold addItems(Materials material, long count) {
        if (material == null || material == Materials._NULL || count <= 0L) {
            return this;
        }
        long toAdd = Math.min(count, remainingUnits());
        if (toAdd <= 0L) {
            return this;
        }
        Map<Materials, Long> nextItems = new LinkedHashMap<>(items);
        nextItems.put(material, itemsOf(material) + toAdd);
        return new CargoHold(capacity, nextItems, new LinkedHashMap<>(fluids), new LinkedHashMap<>(special));
    }

    /**
     * Add fluids to the hold, clamped by the remaining capacity (100 mB = 1 unit).
     *
     * @param material the fluid material (null / _NULL → this hold unchanged)
     * @param mB       the mB amount to add (≤ 0 → this hold unchanged)
     * @return a new hold with the (clamped) fluids added
     */
    public CargoHold addFluids(Materials material, long mB) {
        if (material == null || material == Materials._NULL || mB <= 0L) {
            return this;
        }
        long toAdd = Math.min(mB, remainingUnits() * MB_PER_UNIT);
        if (toAdd <= 0L) {
            return this;
        }
        Map<Materials, Long> nextFluids = new LinkedHashMap<>(fluids);
        nextFluids.put(material, fluidsOf(material) + toAdd);
        return new CargoHold(capacity, new LinkedHashMap<>(items), nextFluids, new LinkedHashMap<>(special));
    }

    /**
     * Remove items from the hold (clamped by what is on board).
     *
     * @param material the item material (null / _NULL → this hold unchanged)
     * @param count    the item count to remove (≤ 0 → this hold unchanged)
     * @return a new hold with the (clamped) items removed
     */
    public CargoHold removeItems(Materials material, long count) {
        if (material == null || material == Materials._NULL || count <= 0L) {
            return this;
        }
        long toRemove = Math.min(count, itemsOf(material));
        if (toRemove <= 0L) {
            return this;
        }
        Map<Materials, Long> nextItems = new LinkedHashMap<>(items);
        long remaining = itemsOf(material) - toRemove;
        if (remaining <= 0L) {
            nextItems.remove(material);
        } else {
            nextItems.put(material, remaining);
        }
        return new CargoHold(capacity, nextItems, new LinkedHashMap<>(fluids), new LinkedHashMap<>(special));
    }

    /**
     * Remove fluids from the hold (clamped by what is on board).
     *
     * @param material the fluid material (null / _NULL → this hold unchanged)
     * @param mB       the mB amount to remove (≤ 0 → this hold unchanged)
     * @return a new hold with the (clamped) fluids removed
     */
    public CargoHold removeFluids(Materials material, long mB) {
        if (material == null || material == Materials._NULL || mB <= 0L) {
            return this;
        }
        long toRemove = Math.min(mB, fluidsOf(material));
        if (toRemove <= 0L) {
            return this;
        }
        Map<Materials, Long> nextFluids = new LinkedHashMap<>(fluids);
        long remaining = fluidsOf(material) - toRemove;
        if (remaining <= 0L) {
            nextFluids.remove(material);
        } else {
            nextFluids.put(material, remaining);
        }
        return new CargoHold(capacity, new LinkedHashMap<>(items), nextFluids, new LinkedHashMap<>(special));
    }

    /**
     * @param key the special-item key (null / empty → 0)
     * @return the special-item count on board for the key (0 when absent)
     */
    public long specialOf(String key) {
        if (key == null || key.isEmpty()) {
            return 0L;
        }
        Long v = special.get(key);
        return v == null ? 0L : v;
    }

    /**
     * @return an unmodifiable view of the special-item counts (key → count)
     */
    public Map<String, Long> getSpecial() {
        return Collections.unmodifiableMap(special);
    }

    /**
     * Add special (infrastructure) items to the hold, clamped by the remaining capacity (1 item = 1 unit).
     *
     * @param key   the special-item key (null / empty → this hold unchanged)
     * @param count the item count to add (≤ 0 → this hold unchanged)
     * @return a new hold with the (clamped) items added
     */
    public CargoHold addSpecial(String key, long count) {
        if (key == null || key.isEmpty() || count <= 0L) {
            return this;
        }
        long toAdd = Math.min(count, remainingUnits());
        if (toAdd <= 0L) {
            return this;
        }
        Map<String, Long> next = new LinkedHashMap<>(special);
        next.put(key, specialOf(key) + toAdd);
        return new CargoHold(capacity, new LinkedHashMap<>(items), new LinkedHashMap<>(fluids), next);
    }

    /**
     * Remove special (infrastructure) items from the hold (clamped by what is on board).
     *
     * @param key   the special-item key (null / empty → this hold unchanged)
     * @param count the item count to remove (≤ 0 → this hold unchanged)
     * @return a new hold with the (clamped) items removed
     */
    public CargoHold removeSpecial(String key, long count) {
        if (key == null || key.isEmpty() || count <= 0L) {
            return this;
        }
        long toRemove = Math.min(count, specialOf(key));
        if (toRemove <= 0L) {
            return this;
        }
        Map<String, Long> next = new LinkedHashMap<>(special);
        long remaining = specialOf(key) - toRemove;
        if (remaining <= 0L) {
            next.remove(key);
        } else {
            next.put(key, remaining);
        }
        return new CargoHold(capacity, new LinkedHashMap<>(items), new LinkedHashMap<>(fluids), next);
    }

    // endregion

    // region transfer (the generic ship-to-ship primitive)

    /**
     * The result of a transfer: the (updated) source and target holds.
     */
    public static final class TransferResult {

        /** The source hold after the transfer (cargo removed). */
        public final CargoHold source;

        /** The target hold after the transfer (cargo added, clamped by its capacity). */
        public final CargoHold target;

        TransferResult(CargoHold source, CargoHold target) {
            this.source = source;
            this.target = target;
        }
    }

    /**
     * Transfer as much cargo as possible from this hold to another hold — the generic Voidcraft-to-Voidcraft
     * transfer primitive. Items transfer 1-for-1; fluids transfer in whole 100 mB units. The transfer is clamped by
     * the TARGET's remaining capacity (the target's capacity is the binding constraint), so a full target simply
     * stops accepting more (the source keeps the remainder).
     *
     * <p>
     * The gameplay around this (which ship transfers to which, when, and the transfer's tick cost) is NOT
     * implemented — this is only the cargo-level operation it will build on.
     *
     * @param target the hold to transfer into (null → a no-op result: this hold as source, no target)
     * @return the (updated) source + target holds
     */
    public TransferResult transferTo(CargoHold target) {
        if (target == null) {
            return new TransferResult(this, null);
        }
        CargoHold nextSource = this;
        CargoHold nextTarget = target;
        // Items: transfer each material's count, clamped by the target's remaining capacity (1 item = 1 unit).
        for (Materials material : new LinkedHashMap<Materials, Long>(items).keySet()) {
            long count = itemsOf(material);
            if (count <= 0L) {
                continue;
            }
            long toMove = Math.min(count, nextTarget.remainingUnits());
            if (toMove <= 0L) {
                break;
            }
            nextSource = nextSource.removeItems(material, toMove);
            nextTarget = nextTarget.addItems(material, toMove);
        }
        // Fluids: transfer in whole 100 mB units, clamped by the target's remaining capacity (100 mB = 1 unit).
        for (Materials material : new LinkedHashMap<Materials, Long>(fluids).keySet()) {
            long mB = fluidsOf(material);
            if (mB <= 0L) {
                continue;
            }
            long toMove = Math.min((mB / MB_PER_UNIT) * MB_PER_UNIT, nextTarget.remainingUnits() * MB_PER_UNIT);
            if (toMove <= 0L) {
                break;
            }
            nextSource = nextSource.removeFluids(material, toMove);
            nextTarget = nextTarget.addFluids(material, toMove);
        }
        // Special (infrastructure) items: transfer 1-for-1, clamped by the target's remaining capacity.
        for (String key : new LinkedHashMap<>(special).keySet()) {
            long count = specialOf(key);
            if (count <= 0L) {
                continue;
            }
            long toMove = Math.min(count, nextTarget.remainingUnits());
            if (toMove <= 0L) {
                break;
            }
            nextSource = nextSource.removeSpecial(key, toMove);
            nextTarget = nextTarget.addSpecial(key, toMove);
        }
        return new TransferResult(nextSource, nextTarget);
    }

    // endregion

    // region NBT

    /**
     * Serialize the hold (capacity + items + fluids + special items).
     *
     * @param nbt the compound to write into (null → no-op)
     */
    public void writeToNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return;
        }
        nbt.setLong(TAG_CAPACITY, capacity);
        nbt.setTag(TAG_ITEMS, entryList(items));
        nbt.setTag(TAG_FLUIDS, entryList(fluids));
        nbt.setTag(TAG_SPECIAL, specialEntryList(special));
    }

    private static NBTTagList specialEntryList(Map<String, Long> map) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<String, Long> e : map.entrySet()) {
            if (e.getKey() == null || e.getKey()
                .isEmpty() || e.getValue() == null || e.getValue() <= 0L) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString(ENTRY_KEY, e.getKey());
            entry.setLong(ENTRY_AMOUNT, e.getValue());
            list.appendTag(entry);
        }
        return list;
    }

    /**
     * Restore a hold from NBT.
     *
     * @param nbt the tag written by {@link #writeToNBT(NBTTagCompound)} (null → an empty zero-capacity hold)
     * @return the restored hold (never null)
     */
    public static CargoHold readFromNBT(NBTTagCompound nbt) {
        long capacity = nbt != null ? nbt.getLong(TAG_CAPACITY) : 0L;
        return new CargoHold(
            capacity,
            readEntries(nbt != null ? nbt.getTagList(TAG_ITEMS, 10) : null),
            readEntries(nbt != null ? nbt.getTagList(TAG_FLUIDS, 10) : null),
            readSpecialEntries(nbt != null ? nbt.getTagList(TAG_SPECIAL, 10) : null));
    }

    private static Map<String, Long> readSpecialEntries(NBTTagList list) {
        Map<String, Long> map = new LinkedHashMap<>();
        if (list == null) {
            return map;
        }
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            if (entry == null) {
                continue;
            }
            String key = entry.getString(ENTRY_KEY);
            if (key == null || key.isEmpty()) {
                continue;
            }
            long amount = Math.max(0L, entry.getLong(ENTRY_AMOUNT));
            if (amount <= 0L) {
                continue;
            }
            map.put(key, map.getOrDefault(key, 0L) + amount);
        }
        return map;
    }

    private static NBTTagList entryList(Map<Materials, Long> map) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<Materials, Long> e : map.entrySet()) {
            if (e.getKey() == null || e.getKey() == Materials._NULL || e.getValue() == null || e.getValue() <= 0L) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString(
                ENTRY_MATERIAL,
                e.getKey()
                    .getName());
            entry.setLong(ENTRY_AMOUNT, e.getValue());
            list.appendTag(entry);
        }
        return list;
    }

    private static Map<Materials, Long> readEntries(NBTTagList list) {
        Map<Materials, Long> map = new LinkedHashMap<>();
        if (list == null) {
            return map;
        }
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            if (entry == null) {
                continue;
            }
            Materials m = Materials.get(entry.getString(ENTRY_MATERIAL));
            if (m == null || m == Materials._NULL) {
                continue;
            }
            long amount = Math.max(0L, entry.getLong(ENTRY_AMOUNT));
            if (amount <= 0L) {
                continue;
            }
            map.put(m, map.getOrDefault(m, 0L) + amount);
        }
        return map;
    }

    // endregion

    @Override
    public String toString() {
        return "CargoHold[capacity=" + capacity
            + " used="
            + usedUnits()
            + " items="
            + items
            + " fluids="
            + fluids
            + "]";
    }
}
