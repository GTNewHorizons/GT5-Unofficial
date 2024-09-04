package gregtech.api.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import net.minecraft.block.Block;

import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;

public class GTBlockMap<V> {

    public static final byte WILDCARD = -1;
    private final ConcurrentHashMap<Block, TByteObjectMap<V>> backing = new ConcurrentHashMap<>();
    private int size = 0;

    private TByteObjectMap<V> getSubmap(Block block) {
        return backing.computeIfAbsent(block, b -> new TByteObjectHashMap<>());
    }

    /**
     * Associate a value with that union key
     *
     * @param block block
     * @param meta  meta
     * @return old mapping, or null if that doesn't exist
     */
    public V put(Block block, byte meta, V value) {
        V v = getSubmap(block).put(meta, value);
        if (v == null) size++;
        return v;
    }

    /**
     * Associate a value with that union key ONLY IF there isn't a prior EXACT mapping
     *
     * @param block block
     * @param meta  meta
     * @return old mapping, or null if that doesn't exist
     */
    public V putIfAbsent(Block block, byte meta, V value) {
        V v = getSubmap(block).putIfAbsent(meta, value);
        if (v == null) size++;
        return v;
    }

    /**
     * Associate a value with that union key ONLY IF there isn't a prior EXACT mapping
     *
     * @param block block
     * @param meta  meta
     * @return old mapping, or null if that doesn't exist
     */
    public V computeIfAbsent(Block block, byte meta, BiFunction<Block, Byte, V> function) {
        TByteObjectMap<V> submap = getSubmap(block);
        V v = submap.get(meta);
        if (v == null) {
            v = function.apply(block, meta);
            submap.put(meta, v);
            size++;
        }
        return v;
    }

    /**
     * Contains an associated value
     *
     * @param block block
     * @param meta  meta
     * @return current mapping OR wildcard of that mapping exists
     */
    public boolean containsKey(Block block, byte meta) {
        TByteObjectMap<V> submap = backing.get(block);
        if (submap == null) return false;
        return submap.containsKey(meta) || submap.containsKey(WILDCARD);
    }

    /**
     * Get the associated value
     *
     * @param block block
     * @param meta  meta
     * @return current mapping OR wildcard of that block. null if neither exists
     */
    public V get(Block block, byte meta) {
        TByteObjectMap<V> submap = backing.get(block);
        if (submap == null) return null;
        V v = submap.get(meta);
        if (v != null) return v;
        return submap.get(WILDCARD);
    }

    /**
     * Remove a mapping
     *
     * @param block block
     * @param meta  meta
     * @return old value, or null if none
     */
    public V remove(Block block, byte meta) {
        TByteObjectMap<V> submap = backing.get(block);
        if (submap == null) return null;
        V v = submap.remove(meta);
        if (v != null) {
            size--;
            if (submap.isEmpty()) backing.remove(block);
        }
        return v;
    }

    /**
     * Size of all mappings
     *
     * @return size
     */
    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GTBlockMap<?> that = (GTBlockMap<?>) o;

        return backing.equals(that.backing);
    }

    @Override
    public int hashCode() {
        return backing.hashCode();
    }
}
