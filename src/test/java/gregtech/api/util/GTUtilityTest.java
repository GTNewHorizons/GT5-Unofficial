package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.GTValues;

public class GTUtilityTest {

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
