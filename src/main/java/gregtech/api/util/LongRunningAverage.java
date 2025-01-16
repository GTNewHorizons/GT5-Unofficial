package gregtech.api.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

public class LongRunningAverage implements LongData {

    private final long[] data;
    private long sumHigh, sumLow;
    private int ptr;
    private boolean filled;
    private final Collection<View> views = Collections.newSetFromMap(new WeakHashMap<>());

    public LongRunningAverage(final int size) {
        if (size <= 1) {
            throw new IllegalArgumentException("Size must be greater than 1");
        }
        data = new long[size];
    }

    public void update(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must not be negative");
        }
        long high = sumHigh, low = sumLow;
        low += value;
        high += low < 0 ? 1 : 0;
        low = low & 0x7FFFFFFFFFFFFFFFL;
        if (filled) {
            low -= data[ptr];
            high -= low < 0 ? 1 : 0;
            low = low & 0x7FFFFFFFFFFFFFFFL;
        }
        sumHigh = high;
        sumLow = low;
        for (View view : views) {
            view.update(value);
        }
        data[ptr++] = value;
        if (ptr == data.length) {
            ptr = 0;
            filled = true;
        }
    }

    @Override
    public int size() {
        int length;
        if (filled) {
            length = data.length;
        } else {
            length = ptr;
        }
        return length;
    }

    @Override
    public BigInteger sum() {
        long high = sumHigh, low = sumLow;
        if ((high & 1) == 1) {
            low |= 0x8000000000000000L;
        }
        high >>>= 1;
        byte[] byteArray = new byte[16];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        byteBuffer.putLong(high);
        byteBuffer.putLong(low);
        return new BigInteger(byteArray);
    }

    public LongData view(int size) {
        if (size <= 1) {
            throw new IllegalArgumentException("Size must be greater than 1");
        }
        if (size > data.length) {
            throw new IllegalArgumentException("Size must be less than or equal to limit of current ring buffer");
        }
        if (size == data.length) {
            return this;
        }
        return new View(size);
    }

    private class View implements LongData {

        private final int size;
        private long sumHigh, sumLow;

        public View(int size) {
            this.size = size;
            views.add(this);
            if (ptr > 0 || filled) {
                ByteBuffer buf = ByteBuffer.allocate(16);
                buf.put(sumSlow().toByteArray());
                buf.flip();
                sumLow = buf.getLong();
                sumHigh = buf.getLong();
            }
        }

        @Override
        public int size() {
            return Math.min(LongRunningAverage.this.size(), size);
        }

        @Override
        public BigInteger sum() {
            long high = sumHigh, low = sumLow;
            if ((high & 1) == 1) {
                low |= 0x8000000000000000L;
            }
            high >>>= 1;
            byte[] byteArray = new byte[16];
            ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
            byteBuffer.putLong(high);
            byteBuffer.putLong(low);
            return new BigInteger(byteArray);
        }

        public BigInteger sumSlow() {
            int start = getStart();
            long high = 0, low = data[start];
            int end = ptr;
            for (int i = start + 1; i != end; i++) {
                if (i == data.length) {
                    i = 0;
                    if (end == 0) break;
                }
                long num = data[i];
                low += num;
                high += low < 0 ? 1 : 0;
                low = low & 0x7FFFFFFFFFFFFFFFL;
            }
            if ((high & 1) == 1) {
                low |= 0x8000000000000000L;
            }
            high >>>= 1;
            byte[] byteArray = new byte[16];
            ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
            byteBuffer.putLong(high);
            byteBuffer.putLong(low);
            return new BigInteger(byteArray);
        }

        private int getStart() {
            int start = ptr - size;
            if (start < 0) {
                if (filled) {
                    start += data.length;
                } else {
                    start = 0;
                }
            }
            return start;
        }

        public void update(long value) {
            long high = sumHigh, low = sumLow;
            low += value;
            high += low < 0 ? 1 : 0;
            low = low & 0x7FFFFFFFFFFFFFFFL;
            int start = getStart();
            if (size() == size) {
                low -= data[start];
                high -= low < 0 ? 1 : 0;
                low = low & 0x7FFFFFFFFFFFFFFFL;
            }
            sumHigh = high;
            sumLow = low;
        }
    }
}
