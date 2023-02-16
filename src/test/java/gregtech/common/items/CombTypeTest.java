package gregtech.common.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class CombTypeTest {

    @Test
    void noDuplicateID() {
        Set<Integer> seen = new HashSet<>();
        for (CombType value : CombType.values()) {
            assertTrue(seen.add(value.getId()), "Comb type must not have duplicate ID");
        }
    }

    @Test
    void noNegativeID() {
        for (CombType value : CombType.values()) {
            if (value == CombType._NULL) assertTrue(value.getId() <= 0, "Comb type ID must be negative for _NULL");
            else assertTrue(value.getId() >= 0, "Comb type ID must not be negative");
        }
    }

    @Test
    void invalidIDNotNull() {
        assertEquals(CombType.valueOf(-2), CombType._NULL, "Invalid ID Lookup should result in _NULL");
        assertEquals(CombType.valueOf(Integer.MAX_VALUE), CombType._NULL, "Invalid ID Lookup should result in _NULL");
    }

    @Test
    void validIDCorrectComb() {
        for (CombType value : CombType.values()) {
            if (value != CombType._NULL)
                assertEquals(CombType.valueOf(value.getId()), value, "Valid ID Lookup should result in correct output");
        }
    }
}
