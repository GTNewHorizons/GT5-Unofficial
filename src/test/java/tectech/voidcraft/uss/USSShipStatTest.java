package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/** The ship STAT registry ids round-trip. */
final class USSShipStatTest {

    @Test
    void testByIdRoundTripForEveryStat() {
        for (USSShipStat stat : USSShipStat.values()) {
            assertNotNull(USSShipStat.byId(stat.getId()), "byId must find " + stat.name());
            assertEquals(stat, USSShipStat.byId(stat.getId()));
        }
    }

    @Test
    void testIdsAreDistinct() {
        for (USSShipStat a : USSShipStat.values()) {
            for (USSShipStat b : USSShipStat.values()) {
                if (a != b) {
                    assertNotSame(a.getId(), b.getId(), a.name() + " and " + b.name() + " must not share an id");
                }
            }
        }
    }

    @Test
    void testUnknownIdIsNull() {
        assertNull(USSShipStat.byId(99));
        assertNull(USSShipStat.byId(-1));
    }

    @Test
    void testRegistryCoversTheSpecList() {
        // the stat set: cargo, state, target, position, travel, work progress
        assertNotNull(USSShipStat.byId(0)); // CARGO_USED
        assertNotNull(USSShipStat.byId(1)); // CARGO_FREE
        assertNotNull(USSShipStat.byId(2)); // CARGO_FULL
        assertNotNull(USSShipStat.byId(3)); // STATE
        assertNotNull(USSShipStat.byId(4)); // TARGET
        assertNotNull(USSShipStat.byId(5)); // POSITION_X
        assertNotNull(USSShipStat.byId(6)); // POSITION_Y
        assertNotNull(USSShipStat.byId(7)); // POSITION_Z
        assertNotNull(USSShipStat.byId(8)); // DIST_TO_TARGET
        assertNotNull(USSShipStat.byId(9)); // SPEED
        assertNotNull(USSShipStat.byId(10)); // TICKS_IN_LEG
        assertNotNull(USSShipStat.byId(11)); // RIPPLES_UNSCANNED
        assertNotNull(USSShipStat.byId(12)); // LOGISTICS_POWER
    }
}
