package gregtech.api.render;

import java.util.concurrent.atomic.AtomicReferenceArray;

import org.jetbrains.annotations.Nullable;

import gregtech.api.interfaces.ITexture;

public final class BoundedTextureCache {

    private static final int CACHE_SIZE = 512;
    private static final int BUCKET_SIZE = 8;
    private static final int BUCKET_COUNT = CACHE_SIZE / BUCKET_SIZE;

    private final AtomicReferenceArray<Entry> entries = new AtomicReferenceArray<>(CACHE_SIZE);
    private final int[] nextEviction = new int[BUCKET_COUNT];

    @Nullable
    public ITexture[][] get(int metadata) {
        int bucketStart = bucketStart(metadata);
        for (int i = 0; i < BUCKET_SIZE; i++) {
            Entry entry = entries.get(bucketStart + i);
            if (entry != null && entry.metadata == metadata) return entry.textures;
        }
        return null;
    }

    public synchronized void put(int metadata, ITexture[][] textures) {
        int bucketStart = bucketStart(metadata);
        for (int i = 0; i < BUCKET_SIZE; i++) {
            int index = bucketStart + i;
            Entry entry = entries.get(index);
            if (entry != null && entry.metadata == metadata) return;
            if (entry == null) {
                entries.set(index, new Entry(metadata, textures));
                return;
            }
        }

        int bucket = bucketStart / BUCKET_SIZE;
        entries.set(bucketStart + nextEviction[bucket], new Entry(metadata, textures));
        nextEviction[bucket] = (nextEviction[bucket] + 1) % BUCKET_SIZE;
    }

    static int bucketStart(int metadata) {
        return (metadata * 0x9E3779B9 >>> 26) * BUCKET_SIZE;
    }

    private static final class Entry {

        private final int metadata;
        private final ITexture[][] textures;

        private Entry(int metadata, ITexture[][] textures) {
            this.metadata = metadata;
            this.textures = textures;
        }
    }
}
