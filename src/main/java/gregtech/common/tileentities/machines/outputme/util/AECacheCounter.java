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
     * <b>Note:</b> The caller <b>MUST NOT</b> modify this stack after insertion.
     */
    public void insert(T stack, long added) {
        if (added <= 0) return;
        total += added;
        cacheMap.addTo(stack, added);
    }

    public void extract(T stack, long removed) {
        if (removed <= 0) return;
        long old = cacheMap.getLong(stack);
        if (old == 0) return;
        if (removed >= old) {
            total -= old;
            cacheMap.put(stack, 0L);
        } else {
            total -= removed;
            cacheMap.put(stack, old - removed);
        }
    }

    public long getTotal() {
        return total;
    }

    @FunctionalInterface
    public interface EntryProcessor<T> {

        long apply(T stack, long currentAmount);
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

    public long get(T stack) {
        return cacheMap.getLong(stack);
    }

    public void iterateAll(ObjLongConsumer<T> consumer) {
        cacheMap.object2LongEntrySet()
            .fastForEach(entry -> {
                long value = entry.getLongValue();
                if (value > 0) consumer.accept(entry.getKey(), value);
            });
    }
}
