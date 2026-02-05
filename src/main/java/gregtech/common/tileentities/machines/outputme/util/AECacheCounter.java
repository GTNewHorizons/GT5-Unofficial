package gregtech.common.tileentities.machines.outputme.util;

import java.util.function.ObjLongConsumer;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

public class AECacheCounter<T> {

    private final Object2LongOpenHashMap<T> cacheMap = new Object2LongOpenHashMap<>(16);
    private long total = 0;

    public AECacheCounter() {
        cacheMap.defaultReturnValue(0L);
    }

    /**
     * <b>Note:</b> The caller <b>MUST NOT</b> modify this key after insertion.
     */
    public void insert(T key, long added) {
        if (added <= 0) return;
        total += added;
        cacheMap.addTo(key, added);
    }

    public void extract(T key, long removed) {
        if (removed <= 0) return;
        long old = cacheMap.getLong(key);
        if (old == 0) return;
        if (removed >= old) {
            total -= old;
            cacheMap.put(key, 0L);
        } else {
            total -= removed;
            cacheMap.put(key, old - removed);
        }
    }

    public long getTotal() {
        return total;
    }

    @FunctionalInterface
    public interface EntryProcessor<T> {

        long apply(T key, long currentAmount);
    }

    public void updateAll(EntryProcessor<T> filter) {
        cacheMap.object2LongEntrySet()
            .fastForEach(entry -> {
                long old = entry.getLongValue();
                if (old <= 0) return;
                T key = entry.getKey();
                long remaining = filter.apply(key, old);
                if (remaining <= 0) {
                    total -= old;
                } else if (remaining != old) {
                    total -= old - remaining;
                }
                entry.setValue(Math.max(0, remaining));
            });
    }

    public boolean isEmpty() {
        return total == 0;
    }

    public long get(T key) {
        return cacheMap.getLong(key);
    }

    public void iterateAll(ObjLongConsumer<T> consumer) {
        cacheMap.object2LongEntrySet()
            .fastForEach(entry -> {
                long value = entry.getLongValue();
                if (value > 0) consumer.accept(entry.getKey(), value);
            });
    }
}
