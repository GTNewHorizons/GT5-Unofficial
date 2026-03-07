package gregtech.api.objects;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

@EventBusSubscriber
public class TimedLRUCache<K, V> {

    private static class CacheEntry<V> {

        public int expTime;
        public final V value;

        private CacheEntry(int expTime, V value) {
            this.expTime = expTime;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (CacheEntry<?>) obj;
            return this.expTime == that.expTime && Objects.equals(this.value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(expTime, value);
        }

        @Override
        public String toString() {
            return "CacheEntry[" + "expTime=" + expTime + ", " + "value=" + value + ']';
        }
    }

    private final Object2ObjectLinkedOpenHashMap<K, CacheEntry<V>> cache = new Object2ObjectLinkedOpenHashMap<>();
    private final Function<K, V> operation;
    private final int timeout;
    private final int capacity;

    private static int TIME;
    private static final Deque<WeakReference<TimedLRUCache<?, ?>>> CACHES = new ArrayDeque<>();

    public TimedLRUCache(Function<K, V> operation, int timeout, int capacity) {
        this.operation = operation;
        this.timeout = timeout;
        this.capacity = capacity;
        CACHES.add(new WeakReference<>(this));
    }

    public V get(K key) {
        var entry = cache.getAndMoveToLast(key);

        if (entry != null) {
            entry.expTime = TIME + timeout;
            return entry.value;
        }

        V value = operation.apply(key);

        cache.putAndMoveToLast(key, new CacheEntry<>(TIME + timeout, value));

        while (cache.size() > capacity) {
            cache.removeFirst();
        }

        return value;
    }

    public void clear() {
        cache.clear();
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.side != Side.SERVER) return;

        TIME++;

        Iterator<WeakReference<TimedLRUCache<?, ?>>> cacheIter = CACHES.iterator();

        while (cacheIter.hasNext()) {
            TimedLRUCache<?, ?> cache = cacheIter.next()
                .get();

            if (cache == null) {
                cacheIter.remove();
                continue;
            }

            var entryIter = cache.cache.object2ObjectEntrySet()
                .fastIterator();

            while (entryIter.hasNext()) {
                var entry = entryIter.next();

                if (entry.getValue().expTime < TIME) {
                    entryIter.remove();
                } else {
                    break;
                }
            }
        }
    }
}
