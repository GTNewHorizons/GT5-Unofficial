package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.VoltageIndex;

public class GTUtilityTest {

    @Test
    void testPowIntPositive() {
        assertEquals(GTUtility.powInt(0.95, 1), Math.pow(0.95, 1));
        assertEquals(GTUtility.powInt(0.95, 2), Math.pow(0.95, 2));
        assertEquals(GTUtility.powInt(0.95, 3), Math.pow(0.95, 3));
        assertEquals(GTUtility.powInt(-0.95, 1), Math.pow(-0.95, 1));
        assertEquals(GTUtility.powInt(-0.95, 2), Math.pow(-0.95, 2));
        assertEquals(GTUtility.powInt(-0.95, 3), Math.pow(-0.95, 3));
    }

    @Test
    void testPowIntNegative() {
        assertEquals(GTUtility.powInt(0.95, -1), Math.pow(0.95, -1));
        assertEquals(GTUtility.powInt(0.95, -2), Math.pow(0.95, -2));
        assertEquals(GTUtility.powInt(0.95, -3), Math.pow(0.95, -3));
        assertEquals(GTUtility.powInt(-0.95, -1), Math.pow(-0.95, -1));
        assertEquals(GTUtility.powInt(-0.95, -2), Math.pow(-0.95, -2));
        assertEquals(GTUtility.powInt(-0.95, -3), Math.pow(-0.95, -3));
    }

    @Test
    void testPowIntZero() {
        assertEquals(GTUtility.powInt(0.95, 0), Math.pow(0.95, 0));
        assertEquals(GTUtility.powInt(-0.95, 0), Math.pow(-0.95, 0));
    }

    @Test
    void testPowIntZeroZero() {
        assertEquals(GTUtility.powInt(0.00, 0), Math.pow(0.00, 0));
        assertEquals(GTUtility.powInt(-0.00, 0), Math.pow(-0.00, 0));
    }

    @Test
    void testPowIntDomain() {
        for (int i = 0; i <= 1024; i++) assertEquals(Math.pow(2, i), GTUtility.powInt(2, i));
        for (int i = 0; i <= 512; i++) assertEquals(Math.pow(4, i), GTUtility.powInt(4, i));
    }

    @SuppressWarnings("ConstantValue")
    @Test
    void testLog2() {
        for (int i = 2; i > 0 && i <= (1 << 16); i <<= 1) {
            assertEquals((int) (Math.log(i - 1) / Math.log(2)), GTUtility.log2(i - 1));
            assertEquals((int) (Math.log(i) / Math.log(2)), GTUtility.log2(i));
            assertEquals((int) (Math.log(i + 1) / Math.log(2)), GTUtility.log2(i + 1));
        }
        for (long i = 2; i > 0 && i <= (1 << 16); i <<= 1) {
            assertEquals((int) (Math.log(i - 1) / Math.log(2)), GTUtility.log2(i - 1));
            assertEquals((int) (Math.log(i) / Math.log(2)), GTUtility.log2(i));
            assertEquals((int) (Math.log(i + 1) / Math.log(2)), GTUtility.log2(i + 1));
        }
    }

    @SuppressWarnings("ConstantValue")
    @Test
    void testLog2Ceil() {
        for (int i = 2; i > 0 && i <= (1 << 16); i <<= 1) {
            assertEquals((int) Math.ceil(Math.log(i - 1) / Math.log(2)), GTUtility.log2ceil(i - 1));
            assertEquals((int) Math.ceil(Math.log(i) / Math.log(2)), GTUtility.log2ceil(i));
            assertEquals((int) Math.ceil(Math.log(i + 1) / Math.log(2)), GTUtility.log2ceil(i + 1));
        }
        for (long i = 2; i > 0 && i <= (1 << 16); i <<= 1) {
            assertEquals((int) Math.ceil(Math.log(i - 1) / Math.log(2)), GTUtility.log2ceil(i - 1));
            assertEquals((int) Math.ceil(Math.log(i) / Math.log(2)), GTUtility.log2ceil(i));
            assertEquals((int) Math.ceil(Math.log(i + 1) / Math.log(2)), GTUtility.log2ceil(i + 1));
        }
    }

    @Test
    void testLog2Zero() {
        assertEquals(0, GTUtility.log2(0));
    }

    @SuppressWarnings("ConstantValue")
    @Test
    void testLog4() {
        for (int i = 2; i > 0 && i <= (1 << 16); i <<= 1) {
            assertEquals((int) (Math.log(i - 1) / Math.log(4)), GTUtility.log4(i - 1));
            assertEquals((int) (Math.log(i) / Math.log(4)), GTUtility.log4(i));
            assertEquals((int) (Math.log(i + 1) / Math.log(4)), GTUtility.log4(i + 1));
        }
        for (long i = 2; i > 0 && i <= (1 << 16); i <<= 1) {
            assertEquals((int) (Math.log(i - 1) / Math.log(4)), GTUtility.log4(i - 1));
            assertEquals((int) (Math.log(i) / Math.log(4)), GTUtility.log4(i));
            assertEquals((int) (Math.log(i + 1) / Math.log(4)), GTUtility.log4(i + 1));
        }
    }

    @SuppressWarnings("ConstantValue")
    @Test
    void testLog4Ceil() {
        for (int i = 2; i > 0 && i <= (1 << 16); i <<= 1) {
            assertEquals((int) Math.ceil(Math.log(i - 1) / Math.log(4)), GTUtility.log4ceil(i - 1));
            assertEquals((int) Math.ceil(Math.log(i) / Math.log(4)), GTUtility.log4ceil(i));
            assertEquals((int) Math.ceil(Math.log(i + 1) / Math.log(4)), GTUtility.log4ceil(i + 1));
        }
        for (long i = 2; i > 0 && i <= (1 << 16); i <<= 1) {
            assertEquals((int) Math.ceil(Math.log(i - 1) / Math.log(4)), GTUtility.log4ceil(i - 1));
            assertEquals((int) Math.ceil(Math.log(i) / Math.log(4)), GTUtility.log4ceil(i));
            assertEquals((int) Math.ceil(Math.log(i + 1) / Math.log(4)), GTUtility.log4ceil(i + 1));
        }
    }

    @Test
    void testLog4Zero() {
        assertEquals(0, GTUtility.log4(0));
    }

    @Test
    public void testGetTier() {
        assertEquals(0, GTUtility.getTier(-1));
        assertEquals(0, GTUtility.getTier(1));
        assertEquals(0, GTUtility.getTier(GTValues.V[0]));
        for (int i = 1; i < 16; i++) {
            assertEquals(
                i,
                GTUtility.getTier(GTValues.V[i - 1] + 1),
                String.format("%d, %d", i, GTValues.V[i - 1] + 1));
            assertEquals(i, GTUtility.getTier(GTValues.V[i]));
        }
    }

    @Test
    void testCeilDiv() {
        assertEquals(2, GTUtility.ceilDiv(15, 8));
        assertEquals(-3, GTUtility.ceilDiv(-7, 2));
        assertEquals(-1, GTUtility.ceilDiv(-15, 8));
        assertEquals(-3, GTUtility.ceilDiv(7, -2));
        assertEquals(4, GTUtility.ceilDiv(-7, -2));
        assertEquals(0, GTUtility.ceilDiv(0, 5));

        assertEquals(2L, GTUtility.ceilDiv(15L, 8L));
        assertEquals(-3L, GTUtility.ceilDiv(-7L, 2L));
        assertEquals(-1L, GTUtility.ceilDiv(-15L, 8L));
        assertEquals(-3L, GTUtility.ceilDiv(7L, -2L));
        assertEquals(4L, GTUtility.ceilDiv(-7L, -2L));
        assertEquals(0L, GTUtility.ceilDiv(0L, 5L));
    }

    @Test
    void testCeilDiv2() {
        assertEquals(2, GTUtility.ceilDiv2(15, 8));
        assertEquals(-4, GTUtility.ceilDiv2(-7, 2));
        assertEquals(-2, GTUtility.ceilDiv2(-15, 8));
        assertEquals(-4, GTUtility.ceilDiv2(7, -2));
        assertEquals(4, GTUtility.ceilDiv2(-7, -2));
        assertEquals(0, GTUtility.ceilDiv2(0, 5));

        assertEquals(2L, GTUtility.ceilDiv2(15L, 8L));
        assertEquals(-4L, GTUtility.ceilDiv2(-7L, 2L));
        assertEquals(-2L, GTUtility.ceilDiv2(-15L, 8L));
        assertEquals(-4L, GTUtility.ceilDiv2(7L, -2L));
        assertEquals(4L, GTUtility.ceilDiv2(-7L, -2L));
        assertEquals(0L, GTUtility.ceilDiv2(0L, 5L));
    }

    @Test
    void testDivMul() {
        assertEquals(300_000, GTUtility.fastDivMul(300_000, 10_000, 10_000));
        assertEquals(30, GTUtility.fastDivMul(300_000, 10_000, 1));
        assertEquals(300_000, GTUtility.fastDivMul(30, 1, 10_000));

        assertEquals(1, GTUtility.fastDivMul(1, 1, 1));
        assertEquals(2, GTUtility.fastDivMul(7, 3, 1));
        assertEquals(7, GTUtility.fastDivMul(7, 3, 3));

        assertEquals(Long.MAX_VALUE, GTUtility.fastDivMul(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE));
        assertEquals(Long.MIN_VALUE, GTUtility.fastDivMul(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE));
    }

    @Test
    public void testGetAmperageForTier() {
        assertEquals(1L, GTUtility.getAmperageForTier(TierEU.UIV, (byte) VoltageIndex.UIV));
        assertEquals(274877906944L, GTUtility.getAmperageForTier(Long.MAX_VALUE, (byte) VoltageIndex.UIV));
    }
}
