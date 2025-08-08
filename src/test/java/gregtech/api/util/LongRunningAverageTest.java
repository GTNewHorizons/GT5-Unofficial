package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

// the main test subject is the internal 127-bit long arithmetic and ring buffer management
// so BigIntegers and Lists with manually maintained size limit is used as test reference
class LongRunningAverageTest {

    private static final long[] data;

    static {
        data = new long[200000];
        // some random number close to long max
        for (int i = 0; i < 100000; i++) {
            data[i] = Long.MAX_VALUE / (long) Math.sqrt(i + 1);
        }
        for (int i = 100000; i > 0; i--) {
            data[200000 - i] = Long.MAX_VALUE / (long) Math.sqrt(i + 1);
        }
    }

    @Test
    void avg() {
        BigInteger expectedSum = BigInteger.ZERO;
        LongRunningAverage average = new LongRunningAverage(data.length);
        for (int i = 0; i < data.length; i++) {
            long val = data[i];
            expectedSum = expectedSum.add(BigInteger.valueOf(val));
            average.update(val);
            int ii = i + 1;
            assertEquals(
                expectedSum.divide(BigInteger.valueOf(i + 1))
                    .doubleValue(),
                average.avg(),
                val / 1e8d,
                () -> "Iteration #" + ii);
        }
        average = new LongRunningAverage(10);
        LongList l = new LongArrayList();
        for (int i = 0; i < data.length; i++) {
            long val = data[i];
            average.update(val);
            l.add(val);
            if (l.size() > 10) {
                l.removeElements(0, 1);
            }
            BigInteger avg = l.longStream()
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger::add)
                .get()
                .divide(BigInteger.valueOf(Math.min(i + 1, 10)));
            int ii = i + 1;
            assertEquals(avg.doubleValue(), average.avg(), val / 1e8d, () -> "Iteration #" + ii);
        }
    }

    @Test
    void avgLong() {
        BigInteger expectedSum = BigInteger.ZERO;
        LongRunningAverage average = new LongRunningAverage(data.length);
        for (int i = 0; i < data.length; i++) {
            long val = data[i];
            expectedSum = expectedSum.add(BigInteger.valueOf(val));
            average.update(val);
            int ii = i + 1;
            assertEquals(
                expectedSum.divide(BigInteger.valueOf(i + 1))
                    .longValueExact(),
                average.avgLong(),
                () -> "Iteration #" + ii);
        }
        average = new LongRunningAverage(10);
        LongList l = new LongArrayList();
        for (int i = 0; i < data.length; i++) {
            long val = data[i];
            average.update(val);
            l.add(val);
            if (l.size() > 10) {
                l.removeElements(0, 1);
            }
            BigInteger avg = l.longStream()
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger::add)
                .get()
                .divide(BigInteger.valueOf(Math.min(i + 1, 10)));
            int ii = i + 1;
            assertEquals(avg.longValueExact(), average.avgLong(), () -> "Iteration #" + ii);
        }
    }

    @Test
    void size() {
        LongRunningAverage average = new LongRunningAverage(data.length);
        for (int i = 0; i < data.length; i++) {
            long val = data[i];
            average.update(val);
            int ii = i + 1;
            assertEquals(i + 1, average.size(), () -> "Iteration #" + ii);
        }
        average = new LongRunningAverage(10);
        for (int i = 0; i < data.length; i++) {
            long val = data[i];
            average.update(val);
            int ii = i + 1;
            assertEquals(Math.min(10, i + 1), average.size(), () -> "Iteration #" + ii);
        }
    }

    @Test
    void sum() {
        BigInteger expected = BigInteger.ZERO;
        LongRunningAverage average = new LongRunningAverage(data.length);
        for (int i = 0; i < data.length; i++) {
            long val = data[i];
            expected = expected.add(BigInteger.valueOf(val));
            average.update(val);
            BigInteger our = average.sum();
            int ii = i + 1;
            assertEquals(expected, our, () -> "Iteration #" + ii);
        }
        average = new LongRunningAverage(10);
        LongList l = new LongArrayList();
        for (int i = 0; i < data.length; i++) {
            long val = data[i];
            average.update(val);
            l.add(val);
            if (l.size() > 10) {
                l.removeElements(0, 1);
            }
            int ii = i + 1;
            assertEquals(
                l.longStream()
                    .mapToObj(BigInteger::valueOf)
                    .reduce(BigInteger::add)
                    .get(),
                average.sum(),
                () -> "Iteration #" + ii);
        }
    }

    @Test
    void view() {
        LongRunningAverage average = new LongRunningAverage(data.length / 10);
        LongData view = average.view(10);
        LongList l = new LongArrayList();
        for (int i = 0; i < data.length; i++) {
            long val = data[i];
            average.update(val);
            l.add(val);
            if (l.size() > 10) {
                l.removeElements(0, 1);
            }
            int ii = i + 1;
            assertEquals(l.size(), view.size(), () -> "Iteration #" + ii);
            assertEquals(
                l.longStream()
                    .mapToObj(BigInteger::valueOf)
                    .reduce(BigInteger::add)
                    .get(),
                view.sum(),
                () -> "Iteration #" + ii);
        }
    }
}
