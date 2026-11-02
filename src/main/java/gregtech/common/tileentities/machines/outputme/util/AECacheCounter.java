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
            cacheMap.removeLong(key);
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
        var it = cacheMap.object2LongEntrySet()
            .iterator();
        while (it.hasNext()) {
            var entry = it.next();
            long old = entry.getLongValue();
            T key = entry.getKey();
            long remaining = filter.apply(key, old);
            if (remaining <= 0) {
                total -= old;
                it.remove();
            } else if (remaining != old) {
                total -= old - remaining;
                entry.setValue(remaining);
            }
        }
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
