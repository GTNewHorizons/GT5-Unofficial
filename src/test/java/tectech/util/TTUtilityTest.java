package tectech.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TTUtilityTest {

    @Test
    public void testToExponentForm() {
        Assertions.assertEquals("1.00e2", TTUtility.toExponentForm(100));
        Assertions.assertEquals("3.01e2", TTUtility.toExponentForm(301));
        Assertions.assertEquals("8.41e2", TTUtility.toExponentForm(841));
        Assertions.assertEquals("1.00e3", TTUtility.toExponentForm(1000));
        Assertions.assertEquals("5.01e3", TTUtility.toExponentForm(5010));
        Assertions.assertEquals("9.22e18", TTUtility.toExponentForm(Long.MAX_VALUE));
    }

}
