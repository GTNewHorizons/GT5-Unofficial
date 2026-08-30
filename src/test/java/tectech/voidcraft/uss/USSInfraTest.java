package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Dyson Swarm's pure tuning: capacity/decay calibration against the largest star and the loadout→cargo key
 * routing contract.
 */
public class USSInfraTest {

    private static final double EPS = 1e-9;

    @BeforeEach
    public void setUp() {
        USSStarRegistry.clear();
        USSStarCatalog.resetForTests();
    }

    @AfterEach
    public void tearDown() {
        USSStarRegistry.clear();
        USSStarCatalog.resetForTests();
    }

    @Test
    public void testCapacityPins() {
        // A white dwarf at its minimum (render (2/3)·√1): 100.
        assertEquals(100L, USSInfra.starSatelliteCapacity(USSPlanets.starRenderSize(1.0)));
        // A yellow dwarf at size 3 (render (2/3)·√3): 300.
        assertEquals(300L, USSInfra.starSatelliteCapacity(USSPlanets.starRenderSize(3.0)));
        // The catalog's largest star (a giant at 10, render (2/3)·√10): 1000.
        assertEquals(1000L, USSInfra.starSatelliteCapacity(USSPlanets.starRenderSize(10.0)));
        // A degenerate size still hosts one satellite.
        assertEquals(1L, USSInfra.starSatelliteCapacity(0.001));
    }

    @Test
    public void testCapacityIsMonotonicInRenderSize() {
        long previous = 0L;
        for (int i = 1; i <= 400; i++) {
            double render = i / 40.0; // 0.025 .. 10.0 blocks
            long capacity = USSInfra.starSatelliteCapacity(render);
            assertTrue(capacity >= previous, "capacity must not shrink as the star grows (render " + render + ")");
            previous = capacity;
        }
    }

    @Test
    public void testDecayIsCalibratedToTheLargestStar() {
        // Bare-JVM catalog (none registered) → the giant maximum (size 10) calibrates the decay:
        // DYSON_SATELLITE_DECAY_FRACTION of one whole satellite per launcher interval at full saturation.
        double maxRender = USSInfra.largestStarRenderSize();
        assertEquals(USSPlanets.starRenderSize(10.0), maxRender, EPS, "no catalog → the giant maximum");
        long capacity = USSInfra.starSatelliteCapacity(maxRender);
        assertEquals(1000L, capacity);
        double rate = USSInfra.decayPerUnitPerTick();
        assertEquals(
            USSConstants.DYSON_SATELLITE_DECAY_FRACTION / (10.0 * 1000.0),
            rate,
            0.0,
            "FRACTION / (interval × largest capacity)");

        double lossPerInterval = capacity * rate * USSConstants.DYSON_SATELLITE_LAUNCH_INTERVAL;
        assertEquals(
            USSConstants.DYSON_SATELLITE_DECAY_FRACTION,
            lossPerInterval,
            1e-9,
            "calibrated to exactly FRACTION of a satellite per interval");

        // Behavior: at full saturation, the accumulator-driven decay loses ≈ FRACTION × 10 satellites over 10
        // intervals. The exact 100-tick horizon sits a float ulp from the integer boundary, so the band (never
        // stalls, never outruns the calibrated rate) is the contract — the analytic pin above is the exact one.
        USSInfrastructure swarm = USSInfrastructure.empty()
            .addUnits(USSInfrastructure.DYSON_STAR_KEY, capacity);
        long lost = 0L;
        for (long tick = 0; tick < USSConstants.DYSON_SATELLITE_LAUNCH_INTERVAL * 10L; tick++) {
            USSInfrastructure.DecayStep step = swarm.applyDecay(USSInfrastructure.DYSON_STAR_KEY, rate);
            lost += step.lost;
            swarm = step.infrastructure;
        }
        long expected = Math.round(USSConstants.DYSON_SATELLITE_DECAY_FRACTION * 10L);
        assertTrue(
            lost >= expected - 1L && lost <= expected + 1L,
            "10 intervals at saturation lose ~" + expected + " satellites (FRACTION per launch), got " + lost);
    }

    @Test
    public void testLargestTracksTheCatalog() {
        // The registered catalog's largest star (a giant at 10) is what the capacity and the decay calibrate to.
        USSStarCatalog.registerAll();
        double maxRender = USSInfra.largestStarRenderSize();
        assertEquals(USSPlanets.starRenderSize(10.0), maxRender, EPS);
        long capacity = USSInfra.starSatelliteCapacity(maxRender);
        assertEquals(1000L, capacity, "round(225 · ((2/3)·√10)²)");
        double lossPerInterval = capacity * USSInfra.decayPerUnitPerTick()
            * USSConstants.DYSON_SATELLITE_LAUNCH_INTERVAL;
        assertEquals(
            USSConstants.DYSON_SATELLITE_DECAY_FRACTION,
            lossPerInterval,
            1e-9,
            "the decay always re-calibrates to FRACTION of a satellite per interval");
    }

    @Test
    public void testLoadoutKeyRoutesToSiteCargo() {
        // The item. prefix is the routing contract between the gateway's loadout and the site's cargo map.
        assertEquals("power_satellite", USSInfra.KEY_POWER_SATELLITE);
        assertEquals("item.power_satellite", USSInfra.LOADOUT_KEY_SATELLITE);
        assertTrue(USSBaseSite.isCargoKey(USSInfra.LOADOUT_KEY_SATELLITE), "the satellite loadout key is a cargo key");
    }
}
