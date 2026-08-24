package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Phase 3 in-flight ship state machine ({@link VoidcraftActiveShip}) — leg transitions,
 * leg durations from {@link USSConstants}, the completion signal, and the NBT round-trip.
 */
public class VoidcraftActiveShipTest {

    private static final double SPEED = 0.5;
    private static final long MINING_POWER = 1000L;

    private static long travelTicks() {
        return USSConstants.travelTicks(SPEED);
    }

    private static long mineTicks() {
        return USSConstants.mineTicks(MINING_POWER);
    }

    // region launch + leg transitions

    @Test
    public void testLaunchStartsOutbound() {
        NBTTagCompound payload = new NBTTagCompound();
        payload.setString("vc_uuid", "ship-1");

        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch("ship-1", "Test Ship", SPEED, MINING_POWER, true, payload, null, null);
        assertEquals(USSShipState.OUTBOUND, ship.getState());
        assertEquals(travelTicks(), ship.getTicksRemaining(), "OUTBOUND leg uses the travel duration");
        assertFalse(ship.isRecoverable() != true, "recoverable flag preserved");
        assertNull(ship.getCargo(), "no cargo until the MINING leg completes");
        assertEquals(payload, ship.getPayload(), "full ship item NBT captured at launch");
    }

    @Test
    public void testFullMissionLoopOutboundMiningReturning() {
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch("ship-1", "Test Ship", SPEED, MINING_POWER, true, new NBTTagCompound(), null, null);

        long travel = travelTicks();
        long mine = mineTicks();
        // Each of the first two legs costs duration + 1 (the +1 is the transition tick); the final leg completes on
        // its last tick (no extra transition after completion).
        int expectedTotal = (int) (travel + 1) + (int) (mine + 1) + (int) travel;

        boolean inFlight = true;
        int ticks = 0;
        boolean sawMining = false;
        boolean sawReturning = false;
        while (inFlight && ticks < expectedTotal + 10) {
            if (ship.getState() == USSShipState.MINING) {
                sawMining = true;
            }
            if (ship.getState() == USSShipState.RETURNING) {
                sawReturning = true;
            }
            inFlight = ship.tick();
            ticks++;
        }

        assertFalse(inFlight, "the mission must complete");
        assertEquals(expectedTotal, ticks, "mission length = travel + 1 + mine + 1 + travel (deterministic)");
        assertTrue(sawMining, "visited the MINING leg");
        assertTrue(sawReturning, "visited the RETURNING leg");
    }

    @Test
    public void testLegDurationsMatchConstants() {
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch("ship-1", "S", SPEED, MINING_POWER, false, new NBTTagCompound(), null, null);
        assertEquals(travelTicks(), ship.getTicksRemaining());

        // run to the MINING leg
        int guard = 0;
        while (ship.getState() != USSShipState.MINING && ship.tick() && guard++ < 10_000) {
            // spin
        }
        assertEquals(USSShipState.MINING, ship.getState());
        assertEquals(mineTicks(), ship.getTicksRemaining(), "MINING leg uses the mining duration");

        guard = 0;
        while (ship.getState() != USSShipState.RETURNING && ship.tick() && guard++ < 10_000) {
            // spin
        }
        assertEquals(USSShipState.RETURNING, ship.getState());
        assertEquals(travelTicks(), ship.getTicksRemaining(), "RETURNING leg uses the travel duration");
    }

    @Test
    public void testCompletionIsSticky() {
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch("ship-1", "S", 1.0, 1000L, false, new NBTTagCompound(), null, null);
        int guard = 0;
        boolean inFlight = true;
        while (inFlight && guard++ < 100_000) {
            inFlight = ship.tick();
        }
        assertFalse(inFlight);
        // completion must be sticky — the caller delivers once, the ship is gone afterwards
        assertFalse(ship.tick());
        assertFalse(ship.tick());
    }

    @Test
    public void testSpeedAndMiningPowerEdges() {
        // zero speed → the maximum travel duration (never 0)
        VoidcraftActiveShip slow = VoidcraftActiveShip
            .launch("slow", "Slow", 0.0, 0L, false, new NBTTagCompound(), null, null);
        assertEquals(USSConstants.TRAVEL_TICKS_MAX, slow.getTicksRemaining());
        assertTrue(slow.getTicksRemaining() > 0);

        // zero mining power → the maximum mining duration (never 0)
        VoidcraftActiveShip weak = VoidcraftActiveShip
            .launch("weak", "Weak", 0.5, 0L, false, new NBTTagCompound(), null, null);
        int guard = 0;
        while (weak.getState() != USSShipState.MINING && weak.tick() && guard++ < 10_000) {
            // spin
        }
        assertEquals(USSConstants.MINE_TICKS_MAX, weak.getTicksRemaining());
    }

    // endregion

    // region cargo handoff

    @Test
    public void testSetCargo() {
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch("ship-1", "S", SPEED, MINING_POWER, true, new NBTTagCompound(), null, null);
        assertNull(ship.getCargo());

        NBTTagCompound cargo = new NBTTagCompound();
        cargo.setString("marker", "xyz");
        ship.setCargo(cargo);
        assertNotNull(ship.getCargo());
        assertEquals(
            "xyz",
            ship.getCargo()
                .getString("marker"));
    }

    // endregion

    // region NBT round-trip

    @Test
    public void testNbtRoundTrip() {
        NBTTagCompound payload = new NBTTagCompound();
        payload.setString("vc_uuid", "rt-1");
        payload.setInteger("vc_format", 1);

        int[] gateway = { 10, 64, -20 };
        int[] bay = { 12, 64, -18 };
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch("rt-1", "Round Trip", SPEED, MINING_POWER, true, payload, gateway, bay);
        ship.tick();
        ship.tick();
        NBTTagCompound cargo = new NBTTagCompound();
        cargo.setString("marker", "cargo");
        ship.setCargo(cargo);

        NBTTagCompound tag = ship.writeToNBT();
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(tag);

        assertNotNull(restored);
        assertEquals("rt-1", restored.getUuid());
        assertEquals("Round Trip", restored.getName());
        assertEquals(SPEED, restored.getSpeed(), 0.0);
        assertEquals(MINING_POWER, restored.getMiningPower());
        assertTrue(restored.isRecoverable());
        assertEquals(ship.getState(), restored.getState());
        assertEquals(ship.getTicksRemaining(), restored.getTicksRemaining());
        assertNotNull(restored.getCargo());
        assertEquals(
            "cargo",
            restored.getCargo()
                .getString("marker"));
        assertEquals(payload, restored.getPayload());
        assertArrayEquals(gateway, restored.getGatewayPos(), "the gateway return target round-trips");
        assertArrayEquals(bay, restored.getBayPos(), "the bay delivery target round-trips");
    }

    @Test
    public void testNbtRoundTripWithoutTargets() {
        // null targets stay null across the round-trip (a drop-at-USS fallback mission)
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch("rt-2", "No Targets", SPEED, MINING_POWER, false, new NBTTagCompound(), null, null);
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(ship.writeToNBT());
        assertNotNull(restored);
        assertNull(restored.getGatewayPos());
        assertNull(restored.getBayPos());
    }

    @Test
    public void testPerLaunchSeedRoundTrips() {
        // Pass 5.1: the per-launch identity seed (unique per flight even for duplicated ship items) must survive
        // the mission NBT round-trip — the client keys this ship's animation phase + swarm spot on it.
        int seed = 0x12345678;
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch("dup-1", "Duplicate", SPEED, MINING_POWER, true, new NBTTagCompound(), null, null, seed);
        assertEquals(seed, ship.getSeed());
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(ship.writeToNBT());
        assertNotNull(restored);
        assertEquals(seed, restored.getSeed(), "the per-launch seed survives the NBT round-trip");
    }

    @Test
    public void testLegacyShipsReadZeroSeed() {
        // Pre-pass-5.1 saves carry no vc_seed tag: they must restore with seed 0 (the client's item-UUID fallback)
        // instead of failing.
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("vc_uuid", "legacy-1");
        tag.setInteger("vc_state", USSShipState.OUTBOUND.ordinal());
        tag.setInteger("vc_ticks", 10);
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(tag);
        assertNotNull(restored);
        assertEquals(0, restored.getSeed(), "missing vc_seed reads as 0 (legacy)");

        // and the 8-arg launch (no seed) keeps the legacy behavior
        VoidcraftActiveShip legacy = VoidcraftActiveShip
            .launch("legacy-2", "Legacy", SPEED, MINING_POWER, true, new NBTTagCompound(), null, null);
        assertEquals(0, legacy.getSeed());
    }

    @Test
    public void testMissionTargetRoundTrips() {
        // Pass 7: the mission target (a system planet index; -1 = the star itself) must survive the mission NBT
        // round-trip — the client resolves it against the fleet TE's planet system.
        VoidcraftActiveShip miner = VoidcraftActiveShip
            .launch("t-1", "Miner", SPEED, MINING_POWER, true, new NBTTagCompound(), null, null, 7, 2);
        assertEquals(2, miner.getTargetPlanet(), "the planet index is captured at launch");
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(miner.writeToNBT());
        assertNotNull(restored);
        assertEquals(2, restored.getTargetPlanet(), "the planet target survives the NBT round-trip");

        // Starlifter semantics: -1 = work the star (the client hovers 2.5 above the star center).
        VoidcraftActiveShip starlifter = VoidcraftActiveShip
            .launch("t-2", "Starlifter", SPEED, MINING_POWER, true, new NBTTagCompound(), null, null, 8, -1);
        assertEquals(-1, starlifter.getTargetPlanet(), "-1 = the star");
        assertEquals(
            -1,
            VoidcraftActiveShip.readFromNBT(starlifter.writeToNBT())
                .getTargetPlanet());
    }

    @Test
    public void testLegacyLaunchDefaultsToStarTarget() {
        // The legacy launch overloads (no target) default to -1 (work the star) instead of failing.
        assertEquals(
            -1,
            VoidcraftActiveShip.launch("t-3", "Legacy", SPEED, MINING_POWER, true, new NBTTagCompound(), null, null)
                .getTargetPlanet());
        assertEquals(
            -1,
            VoidcraftActiveShip
                .launch("t-4", "Legacy2", SPEED, MINING_POWER, true, new NBTTagCompound(), null, null, 11)
                .getTargetPlanet());
    }

    @Test
    public void testLegacyShipsReadStarTarget() {
        // Pre-pass-7 saves carry no vc_target tag: they restore with -1 (work the star) instead of failing.
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("vc_uuid", "legacy-3");
        tag.setInteger("vc_state", USSShipState.OUTBOUND.ordinal());
        tag.setInteger("vc_ticks", 10);
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(tag);
        assertNotNull(restored);
        assertEquals(-1, restored.getTargetPlanet(), "missing vc_target reads as -1 (the star)");
    }

    @Test
    public void testReadRejectsDockedShips() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("vc_uuid", "docked-1");
        tag.setInteger("vc_state", USSShipState.DOCKED.ordinal());
        tag.setInteger("vc_ticks", 10);
        // docked ships live in the gateway inventory, never in the USS — refuse to restore
        assertNull(VoidcraftActiveShip.readFromNBT(tag));
    }

    @Test
    public void testReadRejectsMissingOrUnknown() {
        assertNull(VoidcraftActiveShip.readFromNBT(null), "null tag");
        assertNull(VoidcraftActiveShip.readFromNBT(new NBTTagCompound()), "missing uuid");

        NBTTagCompound unknown = new NBTTagCompound();
        unknown.setString("vc_uuid", "x");
        unknown.setInteger("vc_state", 999);
        assertNull(VoidcraftActiveShip.readFromNBT(unknown), "unknown state id");
    }

    // endregion

    // region legTicks helper

    @Test
    public void testMiningLegIsAlwaysVisible() {
        // Pass 7: for EVERY mining power the MINING leg must be a visible hover. The user's "ships reach their
        // destination and turn right back without mining anything" was a 10-tick (0.5 s) hover for high-power
        // ships — invisible at 1/16 scale 10 blocks away. Now the window is 4.5–30 s, always a clear "mining" pose.
        for (long power = 0; power <= 64; power++) {
            long ticks = USSConstants.mineTicks(power);
            assertTrue(ticks >= USSConstants.MINE_TICKS_MIN, "power " + power + " → " + ticks + " ticks");
            assertTrue(ticks >= 90L, "power " + power + " — the mining hover must be at least 4.5 s");
            assertTrue(ticks <= USSConstants.MINE_TICKS_MAX, "power " + power + " — the hover stays bounded");
        }
        // more mining power → no LONGER mining (monotone non-increasing)
        for (long power = 1; power < 64; power++) {
            assertTrue(
                USSConstants.mineTicks(power) >= USSConstants.mineTicks(power + 1),
                "monotone non-increasing at power " + power);
        }
    }

    @Test
    public void testLegTicksTable() {
        assertEquals(
            USSConstants.travelTicks(SPEED),
            USSConstants.legTicks(USSShipState.OUTBOUND, SPEED, MINING_POWER));
        assertEquals(
            USSConstants.travelTicks(SPEED),
            USSConstants.legTicks(USSShipState.RETURNING, SPEED, MINING_POWER));
        assertEquals(
            USSConstants.mineTicks(MINING_POWER),
            USSConstants.legTicks(USSShipState.MINING, SPEED, MINING_POWER));
        assertEquals(0L, USSConstants.legTicks(USSShipState.DOCKED, SPEED, MINING_POWER), "docked ships have no leg");
        assertEquals(0L, USSConstants.legTicks(null, SPEED, MINING_POWER), "null state → 0");
    }

    // endregion
}
