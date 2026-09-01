package tectech.voidcraft.uss;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import gregtech.api.enums.Materials;

/**
 * A ship's cargo hold — a bounded, immutable container of cargo in the USS (the cargo-capacity pass).
 *
 * <p>
 * The capacity model (user spec): {@code 1 cargo unit = 1 item = 100 mB of fluid}. Items consume their count in
 * units; fluids consume {@code mB / 100} units. Adding is clamped by the remaining capacity ("they cannot mine if
 * it is full"); removing is clamped by what is on board. A full hold simply stops accepting more.
 *
 * <p>
 * Two axes: items (key → count) and fluids (material → mB). The item axis is keyed by the CARGO ITEM KEY
 * (item identity, see {@link USSItemCargo#keyOf}: a GT material's name for ores, the blueprint parts-list key
 * ({@code block.<component>} / {@code cover.<cover>}) for hull parts, a dedicated key for the infrastructure
 * payloads — Power Satellite, the builder components, the UMV / UXV Field Generators — and the item's unlocalized
 * name + meta for anything else). The hold does not check what the keys mean: the gateway loads whatever the user
 * put on the input side, and the consumers (CONSTRUCT, the infrastructure builders, the Dyson launcher, the
 * STABILIZE window) draw their own keys from it.
 *
 * <p>
 * Bare JVM (NBT + primitives) for unit tests — item resolution (key ↔ stack) lives at the boundaries
 * ({@link USSItemCargo}), never here.
 */
public final class CargoHold {

    /** Fluids: mB per cargo unit (100 mB = 1 unit). */
    public static final long MB_PER_UNIT = 100L;

    // NBT keys (the vc_ naming convention) — the serialized hold format.
    private static final String TAG_CAPACITY = "vc_hold_capacity";
    private static final String TAG_ITEMS = "vc_hold_items";
    private static final String TAG_FLUIDS = "vc_hold_fluids";
    private static final String ENTRY_MATERIAL = "m";
    private static final String ENTRY_AMOUNT = "a";
    private static final String ENTRY_KEY = "k";

    private final long capacity;
    /** Cargo item key → count on board (LinkedHashMap = stable consumption order). */
    private final Map<String, Long> items;
    /** GT material → mB on board (LinkedHashMap = stable consumption order). */
    private final Map<Materials, Long> fluids;

    private CargoHold(long capacity, Map<String, Long> items, Map<Materials, Long> fluids) {
        this.capacity = Math.max(0L, capacity);
        this.items = items;
        this.fluids = fluids;
    }

    /**
     * @param capacity the capacity in cargo units (negative clamped to 0)
     * @return an empty hold with the given capacity
     */
    public static CargoHold of(long capacity) {
        return new CargoHold(capacity, new LinkedHashMap<String, Long>(), new LinkedHashMap<Materials, Long>());
    }

    /**
     * @return an empty hold with ZERO capacity (the defensive default — holds nothing)
     */
    public static CargoHold empty() {
        return new CargoHold(0L, new LinkedHashMap<String, Long>(), new LinkedHashMap<Materials, Long>());
    }

    // region queries

    /**
     * @return the hold's capacity in cargo units
     */
    public long getCapacity() {
        return capacity;
    }

    /**
     * @return the used capacity in cargo units — the item count plus {@code mB / 100} for each fluid
     */
    public long usedUnits() {
        long units = 0L;
        for (Long count : items.values()) {
            units += count;
        }
        for (Long mB : fluids.values()) {
            units += mB / MB_PER_UNIT;
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
     * @param key the cargo item key (null / empty → 0)
     * @return the item count on board for the key (0 when absent)
     */
    public long itemsOf(String key) {
        if (key == null || key.isEmpty()) {
            return 0L;
        }
        Long v = items.get(key);
        return v == null ? 0L : v;
    }

    /**
     * @param material the ore material (null → 0)
     * @return the item count on board for the material's name key (0 when absent)
     */
    public long itemsOf(Materials material) {
        if (material == null) {
            return 0L;
        }
        return itemsOf(material.getName());
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
     * @return an unmodifiable view of the item counts (cargo item key → count)
     */
    public Map<String, Long> getItems() {
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
     * @param key   the cargo item key (null / empty → this hold unchanged)
     * @param count the item count to add (≤ 0 → this hold unchanged)
     * @return a new hold with the (clamped) items added
     */
    public CargoHold addItem(String key, long count) {
        if (key == null || key.isEmpty() || count <= 0L) {
            return this;
        }
        long toAdd = Math.min(count, remainingUnits());
        if (toAdd <= 0L) {
            return this;
        }
        Map<String, Long> nextItems = new LinkedHashMap<>(items);
        nextItems.put(key, itemsOf(key) + toAdd);
        return new CargoHold(capacity, nextItems, new LinkedHashMap<>(fluids));
    }

    /**
     * Add items of a GT material to the hold (the material's name key), clamped by the remaining capacity.
     *
     * @param material the item material (null / _NULL → this hold unchanged)
     * @param count    the item count to add (≤ 0 → this hold unchanged)
     * @return a new hold with the (clamped) items added
     */
    public CargoHold addItems(Materials material, long count) {
        if (material == null || material == Materials._NULL) {
            return this;
        }
        return addItem(material.getName(), count);
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
        return new CargoHold(capacity, new LinkedHashMap<>(items), nextFluids);
    }

    /**
     * Remove items from the hold (clamped by what is on board).
     *
     * @param key   the cargo item key (null / empty → this hold unchanged)
     * @param count the item count to remove (≤ 0 → this hold unchanged)
     * @return a new hold with the (clamped) items removed
     */
    public CargoHold removeItem(String key, long count) {
        if (key == null || key.isEmpty() || count <= 0L) {
            return this;
        }
        long toRemove = Math.min(count, itemsOf(key));
        if (toRemove <= 0L) {
            return this;
        }
        Map<String, Long> nextItems = new LinkedHashMap<>(items);
        long remaining = itemsOf(key) - toRemove;
        if (remaining <= 0L) {
            nextItems.remove(key);
        } else {
            nextItems.put(key, remaining);
        }
        return new CargoHold(capacity, nextItems, new LinkedHashMap<>(fluids));
    }

    /**
     * Remove items of a GT material from the hold (the material's name key, clamped by what is on board).
     *
     * @param material the item material (null / _NULL → this hold unchanged)
     * @param count    the item count to remove (≤ 0 → this hold unchanged)
     * @return a new hold with the (clamped) items removed
     */
    public CargoHold removeItems(Materials material, long count) {
        if (material == null || material == Materials._NULL) {
            return this;
        }
        return removeItem(material.getName(), count);
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
        return new CargoHold(capacity, new LinkedHashMap<>(items), nextFluids);
    }

    /**
     * Remove up to {@code units} cargo units from the hold, as ONE amount: items first (insertion order —
     * deterministic), then full 100 mB fluid units, clamped by what is on board — the Stellar Injector's buffer
     * drain (a size step's cost leaves the buffer whole, never partially per material).
     *
     * @param units the units to remove (negative clamped to 0)
     * @return a new hold with the units removed (this hold when nothing was removed)
     */
    public CargoHold removeUnits(long units) {
        if (units <= 0L) {
            return this;
        }
        CargoHold out = this;
        long left = units;
        for (Map.Entry<String, Long> entry : items.entrySet()) {
            if (left <= 0L) {
                break;
            }
            Long count = entry.getValue();
            if (count == null || count <= 0L) {
                continue;
            }
            long take = Math.min(count, left);
            out = out.removeItem(entry.getKey(), take);
            left -= take;
        }
        if (left > 0L) {
            for (Map.Entry<Materials, Long> entry : fluids.entrySet()) {
                if (left <= 0L) {
                    break;
                }
                Long mB = entry.getValue();
                if (mB == null || mB < MB_PER_UNIT) {
                    continue;
                }
                long take = Math.min(mB / MB_PER_UNIT, left) * MB_PER_UNIT;
                if (take <= 0L) {
                    continue;
                }
                out = out.removeFluids(entry.getKey(), take);
                left -= take / MB_PER_UNIT;
            }
        }
        return out;
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
     * @param target the hold to transfer into (null → a no-op result: this hold as source, no target)
     * @return the (updated) source + target holds
     */
    public TransferResult transferTo(CargoHold target) {
        if (target == null) {
            return new TransferResult(this, null);
        }
        CargoHold nextSource = this;
        CargoHold nextTarget = target;
        // Items: transfer each key's count, clamped by the target's remaining capacity (1 item = 1 unit).
        for (String key : new LinkedHashMap<>(items).keySet()) {
            long count = itemsOf(key);
            if (count <= 0L) {
                continue;
            }
            long toMove = Math.min(count, nextTarget.remainingUnits());
            if (toMove <= 0L) {
                break;
            }
            nextSource = nextSource.removeItem(key, toMove);
            nextTarget = nextTarget.addItem(key, toMove);
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
        return new TransferResult(nextSource, nextTarget);
    }

    // endregion

    // region NBT

    /**
     * Serialize the hold (capacity + items + fluids).
     *
     * @param nbt the compound to write into (null → no-op)
     */
    public void writeToNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return;
        }
        nbt.setLong(TAG_CAPACITY, capacity);
        nbt.setTag(TAG_ITEMS, keyEntryList(items));
        nbt.setTag(TAG_FLUIDS, materialEntryList(fluids));
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
            readKeyEntries(nbt != null ? nbt.getTagList(TAG_ITEMS, 10) : null),
            readMaterialEntries(nbt != null ? nbt.getTagList(TAG_FLUIDS, 10) : null));
    }

    private static NBTTagList keyEntryList(Map<String, Long> map) {
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

    private static Map<String, Long> readKeyEntries(NBTTagList list) {
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

    private static NBTTagList materialEntryList(Map<Materials, Long> map) {
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

    private static Map<Materials, Long> readMaterialEntries(NBTTagList list) {
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
