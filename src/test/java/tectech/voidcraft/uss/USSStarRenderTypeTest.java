package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link USSStarRenderType} — the ordinal resolution contract (the render TE's NBT carries an
 * ordinal; an unknown value must resolve to the standard treatment, not fail).
 *
 * <p>
 * Bare-JVM: enum data only.
 */
public class USSStarRenderTypeTest {

    @Test
    public void testFromOrdinalRoundTripsEveryType() {
        for (USSStarRenderType type : USSStarRenderType.values()) {
            assertEquals(type, USSStarRenderType.fromOrdinal(type.ordinal()), type + " round-trips");
        }
    }

    @Test
    public void testFromOrdinalFallsBackToStandardForUnknown() {
        assertEquals(USSStarRenderType.STANDARD, USSStarRenderType.fromOrdinal(-1));
        assertEquals(USSStarRenderType.STANDARD, USSStarRenderType.fromOrdinal(USSStarRenderType.values().length + 7));
    }
}
