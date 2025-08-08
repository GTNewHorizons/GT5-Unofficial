package gtPlusPlus.core.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;

public class MovingAverageLong {

    private final long[] storage;
    private int ptr;

    public MovingAverageLong(int sampleSize) {
        storage = new long[sampleSize];
    }

    public void set(long average) {
        Arrays.fill(storage, average);
    }

    public void sample(long data) {
        storage[ptr] = data;
        ptr = (ptr + 1) % storage.length;
    }

    public long get() {
        BigInteger result = BigInteger.ZERO;
        for (long l : storage) {
            result = result.add(BigInteger.valueOf(l));
        }
        return result.divide(BigInteger.valueOf(storage.length))
            .longValue();
    }

    public void write(NBTTagCompound tagCompound, String key) {
        ByteBuffer buf = ByteBuffer.allocate(storage.length * Long.BYTES)
            .order(ByteOrder.nativeOrder());
        buf.asLongBuffer()
            .put(storage);
        tagCompound.setByteArray(key, buf.array());
    }

    /**
     * if read failed, the internal states would not be changed.
     * 
     * @return true if successful, false otherwise.
     */
    public boolean read(NBTTagCompound tagCompound, String key) {
        ByteBuffer buf = ByteBuffer.wrap(tagCompound.getByteArray(key));
        if (buf.remaining() != storage.length * Long.BYTES)
            // not very good...
            return false;
        buf.asLongBuffer()
            .get(storage);
        return true;
    }
}
