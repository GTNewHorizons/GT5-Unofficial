package tectech.voidcraft.uss;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * The USS's infrastructure progress — per TARGET the star-scale infrastructure built on it (the Dyson Swarm pass
 * is the first entry; the same shape extends to other targets / infra types: one progress key per target).
 *
 * <p>
 * Each key tracks a satellite count plus a DECAY ACCUMULATOR: satellites decay proportionally to the count (the
 * swarm loses a fraction of itself per unit per tick), and the fractional loss is carried in the accumulator until
 * it crosses one whole unit. Immutable — every mutation returns a new instance (the {@link VoidcraftUSS} holds one
 * and swaps it through its factory).
 */
public final class USSInfrastructure {

    /** The Dyson Swarm on the USS's own star (the first infrastructure target). */
    public static final String DYSON_STAR_KEY = "dyson_swarm:star";

    // NBT (voidcraft "vc_" naming convention).
    public static final String TAG_ENTRIES = "vc_infra_entries";
    public static final String ENTRY_KEY = "k";
    public static final String ENTRY_COUNT = "c";
    public static final String ENTRY_ACC = "a";

    /** Progress per target: key → entry (count + decay accumulator). */
    private final Map<String, Entry> entries;

    private static final class Entry {

        final long count;
        final double acc;

        Entry(long count, double acc) {
            this.count = count;
            this.acc = acc;
        }
    }

    private USSInfrastructure(Map<String, Entry> entries) {
        this.entries = new LinkedHashMap<>(entries);
    }

    /**
     * @return an empty infrastructure (no progress on any target)
     */
    public static USSInfrastructure empty() {
        return new USSInfrastructure(new LinkedHashMap<String, Entry>());
    }

    /**
     * @param key the target key (null / empty → 0)
     * @return the satellite count on that target (0 when absent)
     */
    public long count(String key) {
        Entry e = entries.get(key);
        return e == null ? 0L : e.count;
    }

    /**
     * @return true when no target carries any satellite
     */
    public boolean isEmpty() {
        for (Entry e : entries.values()) {
            if (e.count > 0L) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return an unmodifiable view of the tracked target keys
     */
    public Map<String, Long> counts() {
        Map<String, Long> out = new LinkedHashMap<>();
        for (Map.Entry<String, Entry> e : entries.entrySet()) {
            out.put(e.getKey(), e.getValue().count);
        }
        return Collections.unmodifiableMap(out);
    }

    /**
     * The result of a decay step: the (updated) infrastructure plus the whole units lost this tick.
     */
    public static final class DecayStep {

        /** The infrastructure after the decay (unchanged when nothing decayed). */
        public final USSInfrastructure infrastructure;

        /** Whole satellite units lost on the key this tick (0 when none). */
        public final long lost;

        DecayStep(USSInfrastructure infrastructure, long lost) {
            this.infrastructure = infrastructure;
            this.lost = lost;
        }
    }

    /**
     * Add satellites to a target (no clamp — the caller enforces the target's capacity).
     *
     * @param key    the target key (null / empty → this instance unchanged)
     * @param amount the units to add (≤ 0 → this instance unchanged)
     * @return a new infrastructure with the satellites added
     */
    public USSInfrastructure addUnits(String key, long amount) {
        if (key == null || key.isEmpty() || amount <= 0L) {
            return this;
        }
        Map<String, Entry> next = new LinkedHashMap<>(entries);
        Entry e = entries.get(key);
        next.put(key, new Entry((e == null ? 0L : e.count) + amount, e == null ? 0.0 : e.acc));
        return new USSInfrastructure(next);
    }

    /**
     * Remove satellites from a target (clamped by the count; the decay accumulator is KEPT — a partially decayed
     * swarm keeps its fraction).
     *
     * @param key    the target key (null / empty → this instance unchanged)
     * @param amount the units to remove (≤ 0 → this instance unchanged)
     * @return a new infrastructure with the (clamped) units removed
     */
    public USSInfrastructure removeUnits(String key, long amount) {
        if (key == null || key.isEmpty() || amount <= 0L) {
            return this;
        }
        Entry e = entries.get(key);
        if (e == null) {
            return this;
        }
        long toRemove = Math.min(amount, e.count);
        if (toRemove <= 0L) {
            return this;
        }
        Map<String, Entry> next = new LinkedHashMap<>(entries);
        long remaining = e.count - toRemove;
        if (remaining <= 0L) {
            next.remove(key);
        } else {
            next.put(key, new Entry(remaining, e.acc));
        }
        return new USSInfrastructure(next);
    }

    /**
     * Apply one tick of proportional decay to a target: the target loses {@code count · perUnitPerTick} satellites
     * per tick (star-size independent per unit). The fractional loss accumulates across ticks until it crosses a
     * whole unit.
     *
     * @param key            the target key (null / empty / no satellites → an unchanged step)
     * @param perUnitPerTick the decay per satellite per tick (&le; 0 → an unchanged step)
     * @return the (possibly updated) infrastructure plus the whole units lost
     */
    public DecayStep applyDecay(String key, double perUnitPerTick) {
        Entry e = entries.get(key);
        if (key == null || key.isEmpty() || e == null || e.count <= 0L || perUnitPerTick <= 0.0) {
            return new DecayStep(this, 0L);
        }
        double loss = e.count * perUnitPerTick + e.acc;
        // The whole units lost cannot exceed the satellites on target (a spike rate must not "lose" more than exist).
        long lost = (long) Math.min(Math.floor(loss), e.count);
        Map<String, Entry> next = new LinkedHashMap<>(entries);
        long remaining = e.count - lost;
        if (remaining <= 0L) {
            next.remove(key);
        } else {
            // The fractional loss (lost > 0: the remainder past the whole units; lost = 0: the whole sub-unit
            // loss) carries into the next tick's accumulator.
            next.put(key, new Entry(remaining, loss - lost));
        }
        if (lost <= 0L) {
            return new DecayStep(new USSInfrastructure(next), 0L);
        }
        return new DecayStep(new USSInfrastructure(next), lost);
    }

    /**
     * @return true when this instance is equivalent to the given one (same keys and satellite counts — the decay
     *         accumulator fractions are NOT compared: a fraction-only step is not a visible change)
     */
    public boolean hasSameProgress(USSInfrastructure other) {
        if (other == null) {
            return entries.isEmpty();
        }
        if (entries.size() != other.entries.size()) {
            return false;
        }
        for (Map.Entry<String, Entry> e : entries.entrySet()) {
            Entry o = other.entries.get(e.getKey());
            if (o == null || o.count != e.getValue().count) {
                return false;
            }
        }
        return true;
    }

    // region NBT

    /**
     * Serialize the infrastructure.
     *
     * @param nbt the compound to write into (null → no-op)
     */
    public void writeToNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return;
        }
        NBTTagList list = new NBTTagList();
        for (Map.Entry<String, Entry> e : entries.entrySet()) {
            if (e.getValue().count <= 0L) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString(ENTRY_KEY, e.getKey());
            entry.setLong(ENTRY_COUNT, e.getValue().count);
            entry.setDouble(ENTRY_ACC, e.getValue().acc);
            list.appendTag(entry);
        }
        nbt.setTag(TAG_ENTRIES, list);
    }

    /**
     * Restore an infrastructure from NBT.
     *
     * @param nbt the compound written by {@link #writeToNBT(NBTTagCompound)} (null → an empty infrastructure)
     * @return the restored infrastructure (never null)
     */
    public static USSInfrastructure readFromNBT(NBTTagCompound nbt) {
        Map<String, Entry> map = new LinkedHashMap<>();
        NBTTagList list = nbt != null ? nbt.getTagList(TAG_ENTRIES, 10) : null;
        if (list != null) {
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound entry = list.getCompoundTagAt(i);
                if (entry == null) {
                    continue;
                }
                String key = entry.getString(ENTRY_KEY);
                if (key == null || key.isEmpty()) {
                    continue;
                }
                long count = Math.max(0L, entry.getLong(ENTRY_COUNT));
                if (count <= 0L) {
                    continue;
                }
                map.put(key, new Entry(count, Math.max(0.0, entry.getDouble(ENTRY_ACC))));
            }
        }
        return new USSInfrastructure(map);
    }

    // endregion
}
