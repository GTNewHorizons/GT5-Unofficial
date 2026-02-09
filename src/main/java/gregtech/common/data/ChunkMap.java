package gregtech.common.data;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;

@SuppressWarnings("unused")
public class ChunkMap<V> extends Long2ObjectOpenHashMap<V> {

    public V get(int chunkX, int chunkZ) {
        return super.get(pack(chunkX, chunkZ));
    }

    public V remove(int chunkX, int chunkZ) {
        return super.remove(pack(chunkX, chunkZ));
    }

    public boolean containsKey(int chunkX, int chunkZ) {
        return super.containsKey(pack(chunkX, chunkZ));
    }

    public V put(int chunkX, int chunkZ, V v) {
        return super.put(pack(chunkX, chunkZ), v);
    }

    public interface ChunkMapComputeFn<V> {

        V apply(int chunkX, int chunkZ);
    }

    public V computeIfAbsent(int chunkX, int chunkZ, @NotNull ChunkMapComputeFn<V> mappingFunction) {
        V v;

        long key = pack(chunkX, chunkZ);

        if ((v = get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(chunkX, chunkZ)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return v;
    }

    public interface FastEntrySet<V> extends ObjectSet<ChunkEntry<V>> {

        /**
         * Returns a fast iterator over this entry set; the iterator might return always the same entry
         * instance, suitably mutated.
         *
         * @return a fast iterator over this entry set; the iterator might return always the same
         *         {@link java.util.Map.Entry} instance, suitably mutated.
         */
        ObjectIterator<ChunkEntry<V>> fastIterator();

        /**
         * Iterates quickly over this entry set; the iteration might happen always on the same entry
         * instance, suitably mutated.
         *
         * <p>
         *
         * This default implementation just delegates to {@link #forEach(Consumer)}.
         *
         * @param consumer a consumer that will by applied to the entries of this set; the entries might be
         *                 represented by the same entry instance, suitably mutated.
         * @since 8.1.0
         */
        default void fastForEach(final Consumer<? super ChunkEntry<V>> consumer) {
            forEach(consumer);
        }
    }

    private FastChunkEntrySet entrySet;

    public FastEntrySet<V> chunkEntrySet() {
        if (entrySet == null) entrySet = new FastChunkEntrySet();

        return entrySet;
    }

    public Iterable<ChunkEntry<V>> fastEntryIterable() {
        return () -> chunkEntrySet().fastIterator();
    }

    public Stream<ChunkEntry<V>> fastEntryStream() {
        return StreamSupport.stream(
            Spliterators.spliterator(
                fastEntryIterable().iterator(),
                size(),
                Spliterator.SIZED | Spliterator.NONNULL | Spliterator.DISTINCT),
            false);
    }

    private class FastChunkEntrySet extends AbstractObjectSet<ChunkEntry<V>> implements FastEntrySet<V> {

        @Override
        public ObjectIterator<ChunkEntry<V>> fastIterator() {
            ChunkEntry<V> entry = new ChunkEntry<>();

            var iter = ChunkMap.this.long2ObjectEntrySet()
                .fastIterator();

            return new ObjectIterator<>() {

                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public ChunkEntry<V> next() {
                    var e = iter.next();

                    entry.setKey(e.getLongKey());
                    entry.setValue(e.getValue());

                    return entry;
                }
            };
        }

        @Override
        public @NotNull ObjectIterator<ChunkEntry<V>> iterator() {
            var iter = ChunkMap.this.long2ObjectEntrySet()
                .fastIterator();

            return new ObjectIterator<>() {

                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public ChunkEntry<V> next() {
                    var e = iter.next();

                    return new ChunkEntry<>(e.getLongKey(), e.getValue());
                }
            };
        }

        @Override
        public int size() {
            return 0;
        }
    }

    public static class ChunkEntry<T> extends BasicEntry<T> {

        public ChunkEntry() {}

        public ChunkEntry(Long key, T value) {
            super(key, value);
        }

        public ChunkEntry(long key, T value) {
            super(key, value);
        }

        public ChunkEntry(int chunkX, int chunkZ, T value) {
            super(pack(chunkX, chunkZ), value);
        }

        void setKey(long key) {
            super.key = key;
        }

        @Override
        public T setValue(T value) {
            T old = super.value;
            super.value = value;
            return old;
        }

        public final int getChunkX() {
            return unpackX(getLongKey());
        }

        public final int getChunkZ() {
            return unpackZ(getLongKey());
        }
    }

    private static final long INT = 0xFFFFFFFFL;

    public static long pack(int x, int z) {
        return (z & INT) << 32 | (long) x & INT;
    }

    public static int unpackX(long l) {
        return (int) l;
    }

    public static int unpackZ(long l) {
        return (int) (l >> 32);
    }
}
