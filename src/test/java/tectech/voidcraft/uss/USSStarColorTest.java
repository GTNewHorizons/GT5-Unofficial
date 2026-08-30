package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link USSStarColor} — the halo-star rule (which star classes render their shell layers
 * outside-in as a glow ring).
 *
 * <p>
 * Bare-JVM: enum data only.
 */
public class USSStarColorTest {

    @Test
    public void testHaloStarRule() {
        // The near-black cores all get the halo treatment.
        assertTrue(USSStarColor.isHaloStar(USSStarType.BLACK_DWARF));
        assertTrue(USSStarColor.isHaloStar(USSStarType.BLACK_HOLE));
        assertTrue(USSStarColor.isHaloStar(USSStarType.GRAVASTAR));

        assertFalse(USSStarColor.isHaloStar(USSStarType.WHITE_DWARF));
        assertFalse(USSStarColor.isHaloStar(USSStarType.YELLOW_DWARF));
        assertFalse(USSStarColor.isHaloStar(null));
    }
}
