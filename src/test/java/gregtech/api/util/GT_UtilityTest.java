package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class GT_UtilityTest {

    @Test
    void formatNumbers() {

        assertEquals("0.01", GT_Utility.formatNumbers(0.01));
        assertEquals("0.001", GT_Utility.formatNumbers(0.001));
        assertEquals("0.005", GT_Utility.formatNumbers(0.005));
        assertEquals("0.006", GT_Utility.formatNumbers(0.0059));
        assertEquals("-0.01", GT_Utility.formatNumbers(-0.01));
        assertEquals("-0.005", GT_Utility.formatNumbers(-0.005));
        assertEquals("-0.006", GT_Utility.formatNumbers(-0.0059));

        assertEquals("-1", GT_Utility.formatNumbers(-1));
        assertEquals("-10", GT_Utility.formatNumbers(-10));
        assertEquals("-1,000", GT_Utility.formatNumbers(-1_000));
        assertEquals("-10,000", GT_Utility.formatNumbers(-10_000));

        assertEquals("0", GT_Utility.formatNumbers(0));
        assertEquals("10", GT_Utility.formatNumbers(10));
        assertEquals("1,000", GT_Utility.formatNumbers(1_000));
        assertEquals("10,000", GT_Utility.formatNumbers(10_000));
        assertEquals("139,491,491", GT_Utility.formatNumbers(139_491_491));

        assertEquals("1.00 x 10^12", GT_Utility.formatNumbers(1_000_000_000_000L));
        assertEquals("1.00 x 10^12", GT_Utility.formatNumbers(1_000_000_000_000L));
        assertEquals("1.20 x 10^12", GT_Utility.formatNumbers(1_200_000_601_000L));
        assertEquals("1.34 x 10^12", GT_Utility.formatNumbers(1_340_000_000_000L));
        assertEquals("1.35 x 10^12", GT_Utility.formatNumbers(1_346_000_000_491L));
        assertEquals("1.34 x 10^12", GT_Utility.formatNumbers(1_341_000_000_000L));
        assertEquals("1.03 x 10^16", GT_Utility.formatNumbers(10_341_000_592_000_000L));
        assertEquals("7.72 x 10^86", GT_Utility.formatNumbers(BigInteger.valueOf(13).pow(78)));

    }
}
