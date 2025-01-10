package gregtech.api.objects;

/**
 * An implementation of Murmur3_32 that works well enough. No guarantees that it actually works properly though
 * :tootroll:.
 */
public class MurmurHash {

    private static final int MURMURHASH2A_R = 24;
    private static final int MURMURHASH2A_MULTIPLIER = 0x5bd1e995;
    private static final int MURMURHASH2A_SEED = 0x811c9dc5; // No seed suggested, so using FNV32_OFFSET_BASIS

    private int hash = MURMURHASH2A_SEED;

    public MurmurHash reset() {
        hash = MURMURHASH2A_SEED;

        return this;
    }

    public MurmurHash feed(byte value) {
        return feed((int) value);
    }

    public MurmurHash feed(short value) {
        return feed((int) value);
    }

    public MurmurHash feed(int value) {
        int hash = this.hash;

        int mmh2ak = value * MURMURHASH2A_MULTIPLIER;
        mmh2ak ^= mmh2ak >> MURMURHASH2A_R;
        mmh2ak *= MURMURHASH2A_MULTIPLIER;
        hash *= MURMURHASH2A_MULTIPLIER;
        hash ^= mmh2ak;

        this.hash = hash;

        return this;
    }

    public MurmurHash feed(long value) {
        feed((int) value);
        feed((int) (value >> 32));

        return this;
    }

    public MurmurHash feed(float value) {
        return feed(Float.floatToIntBits(value));
    }

    public MurmurHash feed(double value) {
        return feed(Double.doubleToLongBits(value));
    }

    public MurmurHash feed(String str) {
        return feed(str, 0, str.length());
    }

    public MurmurHash feed(String str, int offset, int length) {
        int hash = this.hash;

        for (int i = offset; i < length; i++) {
            int mmh2ak = str.charAt(i) * MURMURHASH2A_MULTIPLIER;
            mmh2ak ^= mmh2ak >> MURMURHASH2A_R;
            mmh2ak *= MURMURHASH2A_MULTIPLIER;
            hash *= MURMURHASH2A_MULTIPLIER;
            hash ^= mmh2ak;
        }

        this.hash = hash;

        return this;
    }

    public MurmurHash feed(byte[] data) {
        return feed(data, 0, data.length);
    }

    public MurmurHash feed(byte[] data, int offset, int length) {
        int hash = this.hash;

        int i = offset;

        for (; i + 4 < length; i += 4) {
            int value = data[i + 0] | data[i + 1] << 8 | data[i + 2] << 16 | data[i + 3] << 24;

            int mmh2ak = value * MURMURHASH2A_MULTIPLIER;
            mmh2ak ^= mmh2ak >> MURMURHASH2A_R;
            mmh2ak *= MURMURHASH2A_MULTIPLIER;
            hash *= MURMURHASH2A_MULTIPLIER;
            hash ^= mmh2ak;
        }

        for (; i < length; i++) {
            int mmh2ak = data[i] * MURMURHASH2A_MULTIPLIER;
            mmh2ak ^= mmh2ak >> MURMURHASH2A_R;
            mmh2ak *= MURMURHASH2A_MULTIPLIER;
            hash *= MURMURHASH2A_MULTIPLIER;
            hash ^= mmh2ak;
        }

        this.hash = hash;

        return this;
    }

    public MurmurHash feed(int[] data) {
        return feed(data, 0, data.length);
    }

    public MurmurHash feed(int[] data, int offset, int length) {
        int hash = this.hash;

        for (int i = offset; i < length; i++) {
            int mmh2ak = data[i] * MURMURHASH2A_MULTIPLIER;
            mmh2ak ^= mmh2ak >> MURMURHASH2A_R;
            mmh2ak *= MURMURHASH2A_MULTIPLIER;
            hash *= MURMURHASH2A_MULTIPLIER;
            hash ^= mmh2ak;
        }

        this.hash = hash;

        return this;
    }

    public MurmurHash feed(long[] data) {
        return feed(data, 0, data.length);
    }

    public MurmurHash feed(long[] data, int offset, int length) {
        int hash = this.hash;

        for (int i = offset; i < length; i++) {
            long value = data[i];

            int mmh2ak = ((int) value) * MURMURHASH2A_MULTIPLIER;
            mmh2ak ^= mmh2ak >> MURMURHASH2A_R;
            mmh2ak *= MURMURHASH2A_MULTIPLIER;
            hash *= MURMURHASH2A_MULTIPLIER;
            hash ^= mmh2ak;

            mmh2ak = ((int) (value >> 32)) * MURMURHASH2A_MULTIPLIER;
            mmh2ak ^= mmh2ak >> MURMURHASH2A_R;
            mmh2ak *= MURMURHASH2A_MULTIPLIER;
            hash *= MURMURHASH2A_MULTIPLIER;
            hash ^= mmh2ak;
        }

        this.hash = hash;

        return this;
    }

    public MurmurHash feed(Object obj) {
        return feed(obj == null ? 0 : obj.hashCode());
    }

    public int finish() {
        int hash = this.hash;

        hash ^= hash >> 13;
        hash *= MURMURHASH2A_MULTIPLIER;
        hash ^= hash >> 15;

        return hash;
    }

    @Override
    public int hashCode() {
        return finish();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MurmurHash other)) return false;

        return hash == other.hash;
    }

    @Override
    public String toString() {
        return "MurmurHash [hash=" + hash + "]";
    }
}
