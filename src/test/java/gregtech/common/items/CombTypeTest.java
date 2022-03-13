package gregtech.common.items;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
            assertTrue(value.getId() >= 0, "Comb type ID must not be negative");
        }
    }
}
