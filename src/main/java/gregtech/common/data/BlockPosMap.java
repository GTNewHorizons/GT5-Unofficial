package gregtech.common.data;

import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.pack;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackX;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackY;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker.unpackZ;

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
public class BlockPosMap<V> extends Long2ObjectOpenHashMap<V> {

    public V get(int blockX, int blockY, int blockZ) {
        return super.get(pack(blockX, blockY, blockZ));
    }

    public V remove(int blockX, int blockY, int blockZ) {
        return super.remove(pack(blockX, blockY, blockZ));
    }

    public boolean containsKey(int blockX, int blockY, int blockZ) {
        return super.containsKey(pack(blockX, blockY, blockZ));
    }

    public V put(int blockX, int blockY, int blockZ, V v) {
        return super.put(pack(blockX, blockY, blockZ), v);
    }

    public interface BlockPosMapComputeFn<V> {

        V apply(int blockX, int blockY, int blockZ);
    }

    public V computeIfAbsent(int blockX, int blockY, int blockZ, @NotNull BlockPosMapComputeFn<V> mappingFunction) {
        V v;

        long key = pack(blockX, blockY, blockZ);

        if ((v = get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(blockX, blockY, blockZ)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return v;
    }

    public interface FastEntrySet<V> extends ObjectSet<BlockPosEntry<V>> {

        /**
         * Returns a fast iterator over this entry set; the iterator might return always the same entry
         * instance, suitably mutated.
         *
         * @return a fast iterator over this entry set; the iterator might return always the same
         *         {@link java.util.Map.Entry} instance, suitably mutated.
         */
        ObjectIterator<BlockPosEntry<V>> fastIterator();

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
        default void fastForEach(final Consumer<? super BlockPosEntry<V>> consumer) {
            forEach(consumer);
        }
    }

    private FastChunkEntrySet entrySet;

    public FastEntrySet<V> fastEntrySet() {
        if (entrySet == null) entrySet = new FastChunkEntrySet();

        return entrySet;
    }

    public Iterable<BlockPosEntry<V>> fastEntryIterable() {
        return () -> fastEntrySet().fastIterator();
    }

    public Stream<BlockPosEntry<V>> fastEntryStream() {
        return StreamSupport.stream(
            Spliterators.spliterator(
                fastEntryIterable().iterator(),
                size(),
                Spliterator.SIZED | Spliterator.NONNULL | Spliterator.DISTINCT),
            false);
    }

    private class FastChunkEntrySet extends AbstractObjectSet<BlockPosEntry<V>> implements FastEntrySet<V> {

        @Override
        public ObjectIterator<BlockPosEntry<V>> fastIterator() {
            BlockPosEntry<V> entry = new BlockPosEntry<>();

            var iter = BlockPosMap.this.long2ObjectEntrySet()
                .fastIterator();

            return new ObjectIterator<>() {

                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public BlockPosEntry<V> next() {
                    var e = iter.next();

                    entry.setKeyImpl(e.getLongKey());
                    entry.setValueImpl(e.getValue());

                    return entry;
                }
            };
        }

        @Override
        public @NotNull ObjectIterator<BlockPosEntry<V>> iterator() {
            var iter = BlockPosMap.this.long2ObjectEntrySet()
                .fastIterator();

            return new ObjectIterator<>() {

                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public BlockPosEntry<V> next() {
                    var e = iter.next();

                    return new BlockPosEntry<>(e.getLongKey(), e.getValue());
                }
            };
        }

        @Override
        public int size() {
            return BlockPosMap.this.size();
        }
    }

    public static class BlockPosEntry<T> extends BasicEntry<T> {

        public BlockPosEntry() {}

        public BlockPosEntry(Long key, T value) {
            super(key, value);
        }

        public BlockPosEntry(long key, T value) {
            super(key, value);
        }

        public BlockPosEntry(int blockX, int blockY, int blockZ, T value) {
            super(pack(blockX, blockY, blockZ), value);
        }

        private void setKeyImpl(long key) {
            super.key = key;
        }

        private void setValueImpl(T value) {
            super.value = value;
        }

        public final int getBlockX() {
            return unpackX(getLongKey());
        }

        public final int getBlockY() {
            return unpackY(getLongKey());
        }

        public final int getBlockZ() {
            return unpackZ(getLongKey());
        }
    }
}
