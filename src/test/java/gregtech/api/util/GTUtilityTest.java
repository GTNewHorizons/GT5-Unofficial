package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.GTValues;

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
}
