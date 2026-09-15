package gregtech.api.util;

import java.util.Optional;
import java.util.function.Function;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

public class LRUCache<K, V> {

    public Object2ObjectLinkedOpenHashMap<K, Optional<V>> map = new Object2ObjectLinkedOpenHashMap<>();
    public int capacity;
    public Function<K, V> lookup;

    public LRUCache(int capacity, Function<K, V> lookup) {
        this.capacity = capacity;
        this.lookup = lookup;
    }

    public V get(K key) {
        Optional<V> v = map.getAndMoveToFirst(key);

        if (v == null) {
            v = Optional.ofNullable(lookup.apply(key));
            map.putAndMoveToFirst(key, v);

            while (map.size() > capacity) map.removeLast();
        }

        return v.orElse(null);
    }

    public void clear() {
        map.clear();
    }
}
