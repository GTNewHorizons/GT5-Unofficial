package gregtech.api.metatileentity.implementations;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GTUtilityTest {
    @Test
    public void testGetTier() {
        Assertions.assertEquals(0, GTUtility.getTier(1));
        Assertions.assertEquals(0, GTUtility.getTier(GTValues.V[0]));
        for(int i = 1; i < 16; i++) {
            Assertions.assertEquals(i, GTUtility.getTier(GTValues.V[i - 1] + 1));
            Assertions.assertEquals(i, GTUtility.getTier(GTValues.V[i]));
        }
    }
}
