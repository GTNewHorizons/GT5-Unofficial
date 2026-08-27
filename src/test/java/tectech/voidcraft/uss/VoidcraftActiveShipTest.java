package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.ship.VoidcraftRole;

/**
 * Unit tests for the Phase C PASSIVE leg driver ({@link VoidcraftActiveShip}): launch (holding at the origin),
 * leg arming / countdown / completion latch (consumed exactly once), holding, and the NBT round-trip (including
 * the persisted completion latch that makes a leg's side-effect fire exactly once across a save/reload).
 */
public class VoidcraftActiveShipTest {

    private static final double SPEED = 0.5;
    private static final long MINING_POWER = 1000L;
    private static final double DISTANCE = 10.0;

    private static final USSPosition ORIGIN = USSPosition.of(2.0, -1.0, 3.0);
    private static final USSPosition DEST = USSPosition.of(12.0, 1.0, 13.0);

    private static VoidcraftActiveShip ship(String uuid) {
        NBTTagCompound payload = new NBTTagCompound();
        payload.setString("vc_uuid", uuid);
        return VoidcraftActiveShip.launch(uuid, uuid, SPEED, MINING_POWER, payload, null, null, 0, ORIGIN);
    }

    // region launch (Phase C: the ship starts HOLDING at its origin, legs unarmed)

    @Test
    public void testLaunchHoldsAtOrigin() {
        VoidcraftActiveShip s = ship("ship-1");
        assertEquals(
            USSShipState.HOVERING,
            s.getState(),
            "a fresh ship holds at the origin (the pilot runs its program)");
        assertEquals(ORIGIN, s.getPosition(), "position = the launch origin");
        assertEquals(ORIGIN, s.getLegFrom(), "leg start = the origin (the first leg departs from the gateway)");
        assertFalse(s.isLegActive(), "no leg is armed at launch");
        assertEquals(0, s.getLegId(), "no leg has run yet");
        assertNull(s.getDestination(), "no destination until a leg is armed");
        assertEquals(0, s.getTicksRemaining());
        assertFalse(s.isLegComplete());
        assertEquals(-1, s.getTargetPlanet(), "no hover body yet (the star / none)");
        assertNull(s.getCargo(), "no cargo until a WORK leg's completion sets it");
    }

    @Test
    public void testLaunchDefaultsOriginToZero() {
        VoidcraftActiveShip s = VoidcraftActiveShip
            .launch("z", "z", SPEED, MINING_POWER, new NBTTagCompound(), null, null, 0, null);
        assertEquals(USSPosition.zero(), s.getPosition(), "null origin → (0,0,0)");
    }

    // endregion

    // region leg arming + countdown + completion latch

    @Test
    public void testStartLegArmsTheLeg() {
        VoidcraftActiveShip s = ship("leg-1");
        s.startLeg(USSShipState.OUTBOUND, ORIGIN, DEST, 100, 14.14);
        assertEquals(USSShipState.OUTBOUND, s.getState());
        assertEquals(ORIGIN, s.getLegFrom());
        assertEquals(ORIGIN, s.getPosition(), "position stays at the leg's start until the leg completes");
        assertEquals(DEST, s.getDestination());
        assertEquals(14.14, s.getTravelDistance(), 1e-9);
        assertEquals(100, s.getTicksRemaining());
        assertEquals(100, s.getLegTotal());
        assertEquals(0, s.getTicksInLeg());
        assertTrue(s.isLegActive());
        assertEquals(1, s.getLegId(), "the first leg bumps the leg id to 1");
        assertFalse(s.isLegComplete());
    }

    @Test
    public void testCountdownLatchesAtZero() {
        VoidcraftActiveShip s = ship("leg-2");
        s.startLeg(USSShipState.OUTBOUND, ORIGIN, DEST, 3, DISTANCE);
        s.tickLeg();
        assertEquals(2, s.getTicksRemaining());
        assertEquals(1, s.getTicksInLeg(), "ticks elapsed = total − remaining");
        assertFalse(s.isLegComplete(), "not complete before the countdown runs out");
        s.tickLeg();
        s.tickLeg();
        assertEquals(0, s.getTicksRemaining());
        assertTrue(s.isLegComplete(), "the finished leg latches");
        assertTrue(s.isLegActive(), "the latch stays armed until consumed");
    }

    @Test
    public void testCompletionIsConsumedExactlyOnce() {
        VoidcraftActiveShip s = ship("leg-3");
        s.startLeg(USSShipState.MINING, ORIGIN, DEST, 2, 0.0);
        s.tickLeg();
        s.tickLeg();
        assertTrue(s.isLegComplete());
        s.clearLegComplete();
        assertEquals(DEST, s.getPosition(), "arrival: the position becomes the leg's endpoint");
        assertFalse(s.isLegComplete(), "the latch is consumed (the side-effect fires exactly once)");
        assertFalse(s.isLegActive(), "the leg is deactivated");
        s.tickLeg(); // ticking after consumption is a no-op
        assertFalse(s.isLegComplete(), "a consumed leg never re-latches");
    }

    @Test
    public void testZeroLengthLegCompletesOnTheNextTick() {
        VoidcraftActiveShip s = ship("leg-4");
        s.startLeg(USSShipState.OUTBOUND, ORIGIN, DEST, 0, 0.0);
        assertFalse(s.isLegComplete(), "a zero-length leg is armed but not yet complete");
        s.tickLeg();
        assertTrue(s.isLegComplete());
        s.clearLegComplete();
        assertEquals(DEST, s.getPosition());
    }

    @Test
    public void testHoldParksTheShip() {
        VoidcraftActiveShip s = ship("hold-1");
        s.startLeg(USSShipState.OUTBOUND, ORIGIN, DEST, 50, DISTANCE);
        s.tickLeg();
        assertEquals(USSShipState.OUTBOUND, s.getState());
        s.hold();
        assertEquals(USSShipState.HOVERING, s.getState(), "hold() parks the ship");
        assertFalse(s.isLegActive(), "hold() abandons the in-flight leg (the countdown stops)");
        s.tickLeg();
        assertFalse(s.isLegComplete(), "an abandoned leg cannot complete");
        assertEquals(ORIGIN, s.getPosition(), "the ship stays where it was (mid-leg)");
    }

    @Test
    public void testLegIdIncrementsPerLeg() {
        VoidcraftActiveShip s = ship("id-1");
        assertEquals(0, s.getLegId());
        s.startLeg(USSShipState.OUTBOUND, ORIGIN, DEST, 10, DISTANCE);
        s.tickLeg();
        s.tickLeg();
        s.tickLeg();
        s.clearLegComplete();
        assertEquals(1, s.getLegId(), "leg 1 done");
        s.startLeg(USSShipState.MINING, ORIGIN, DEST, 10, 0.0);
        assertEquals(
            2,
            s.getLegId(),
            "consecutive legs bump the id even with the same state (the client's MOVE→MOVE fix)");
        s.startLeg(USSShipState.RETURNING, ORIGIN, ORIGIN, 10, 0.0);
        assertEquals(3, s.getLegId());
    }

    // endregion

    // region cargo handoff

    @Test
    public void testSetCargo() {
        VoidcraftActiveShip s = ship("cargo-1");
        assertNull(s.getCargo());
        NBTTagCompound cargo = new NBTTagCompound();
        cargo.setString("marker", "xyz");
        s.setCargo(cargo);
        assertNotNull(s.getCargo());
        assertEquals(
            "xyz",
            s.getCargo()
                .getString("marker"));
    }

    @Test
    public void testCargoStaysNullUntilTheCallerProducesIt() {
        // Phase C: the pilot's onWorkComplete is the SOLE producer of the cargo — the ship's leg driver must not
        // pre-fill anything (that is how a work leg's yield fires exactly once).
        VoidcraftActiveShip s = ship("cargo-2");
        s.startLeg(USSShipState.MINING, ORIGIN, DEST, 2, 0.0);
        s.tickLeg();
        s.tickLeg();
        s.clearLegComplete();
        assertNull(s.getCargo(), "cargo stays NULL until the pilot's world seam produces it");
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
        VoidcraftActiveShip s = VoidcraftActiveShip
            .launch("rt-1", "Round Trip", SPEED, MINING_POWER, payload, gateway, bay, 42, ORIGIN);
        s.startLeg(USSShipState.OUTBOUND, ORIGIN, DEST, 80, DISTANCE);
        s.tickLeg();
        NBTTagCompound cargo = new NBTTagCompound();
        cargo.setString("marker", "cargo");
        s.setCargo(cargo);

        NBTTagCompound tag = s.writeToNBT();
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(tag);

        assertNotNull(restored);
        assertEquals("rt-1", restored.getUuid());
        assertEquals("Round Trip", restored.getName());
        assertEquals(SPEED, restored.getSpeed(), 0.0);
        assertEquals(MINING_POWER, restored.getMiningPower());
        assertEquals(0L, restored.getIntegrity(), "no integrity in the payload → 0, and it round-trips");
        assertEquals(s.getState(), restored.getState());
        assertEquals(s.getTicksRemaining(), restored.getTicksRemaining());
        assertEquals(s.getLegTotal(), restored.getLegTotal());
        assertTrue(restored.isLegActive());
        assertEquals(1, restored.getLegId());
        assertEquals(ORIGIN, restored.getLegFrom(), "the leg's start point round-trips");
        assertEquals(DEST, restored.getDestination(), "the leg's endpoint round-trips");
        assertEquals(DISTANCE, restored.getTravelDistance(), 1e-12);
        assertEquals(ORIGIN, restored.getPosition(), "the ship's current position round-trips");
        assertEquals(42, restored.getSeed());
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
        VoidcraftActiveShip s = VoidcraftActiveShip
            .launch("rt-2", "No Targets", SPEED, MINING_POWER, new NBTTagCompound(), null, null, 0, ORIGIN);
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(s.writeToNBT());
        assertNotNull(restored);
        assertNull(restored.getGatewayPos());
        assertNull(restored.getBayPos());
    }

    @Test
    public void testNbtRoundTripPreservesDestinationAndDistance() {
        VoidcraftActiveShip s = VoidcraftActiveShip
            .launch("ship-pos6", "Pos Ship", SPEED, MINING_POWER, new NBTTagCompound(), null, null, 7, ORIGIN);
        s.startLeg(USSShipState.OUTBOUND, ORIGIN, USSPosition.of(4.0, -2.0, 7.0), 60, 9.0);
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(s.writeToNBT());
        assertNotNull(restored);
        assertEquals(USSPosition.of(4.0, -2.0, 7.0), restored.getDestination(), "destination survives the round-trip");
        assertEquals(9.0, restored.getTravelDistance(), 1e-12, "distance survives the round-trip");
    }

    @Test
    public void testNbtRoundTripPreservesLegDoneLatch() {
        // THE Phase C invariant: a leg that finished but whose side-effect has not fired yet (latched complete)
        // must survive a save/reload and be consumable exactly once AFTER the reload.
        VoidcraftActiveShip s = ship("latch-1");
        s.startLeg(USSShipState.MINING, ORIGIN, DEST, 2, 0.0);
        s.tickLeg();
        s.tickLeg();
        assertTrue(s.isLegComplete());
        // save WITHOUT consuming (the world saved between the latch and the pilot's consumption)
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(s.writeToNBT());
        assertNotNull(restored);
        assertTrue(restored.isLegActive(), "the leg is still armed");
        assertTrue(restored.isLegComplete(), "the completion latch survives the round-trip");
        assertNull(restored.getCargo(), "no side-effect yet (it fires on consumption, after the reload)");
        restored.clearLegComplete();
        assertEquals(DEST, restored.getPosition());
        assertFalse(restored.isLegComplete(), "consumed exactly once");
    }

    @Test
    public void testNbtRoundTripBodyStaticAndTarget() {
        VoidcraftActiveShip s = ship("body-1");
        s.setTargetPlanet(5);
        s.setBodyStatic(true);
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(s.writeToNBT());
        assertNotNull(restored);
        assertEquals(5, restored.getTargetPlanet(), "the hover body descriptor round-trips");
        assertTrue(restored.isBodyStatic(), "the static-body flag round-trips");
    }

    @Test
    public void testPerLaunchSeedRoundTrips() {
        // Pass 5.1: the per-launch identity seed (unique per flight even for duplicated ship items) must survive
        // the mission NBT round-trip — the client keys this ship's animation phase + swarm spot on it.
        int seed = 0x12345678;
        VoidcraftActiveShip s = VoidcraftActiveShip
            .launch("dup-1", "Duplicate", SPEED, MINING_POWER, new NBTTagCompound(), null, null, seed, ORIGIN);
        assertEquals(seed, s.getSeed());
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(s.writeToNBT());
        assertNotNull(restored);
        assertEquals(seed, restored.getSeed(), "the per-launch seed survives the NBT round-trip");
    }

    @Test
    public void testLegacyShipsReadDefaults() {
        // Pre-Phase-C saves carry no leg tags: they restore with the safe defaults (no armed leg) instead of failing.
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("vc_uuid", "legacy-1");
        tag.setInteger("vc_state", USSShipState.OUTBOUND.ordinal());
        tag.setInteger("vc_ticks", 10);
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(tag);
        assertNotNull(restored);
        assertEquals(0, restored.getSeed(), "missing vc_seed reads as 0 (legacy)");
        assertEquals(-1, restored.getTargetPlanet(), "missing vc_target reads as -1 (the star / none)");
        assertFalse(restored.isLegActive(), "a legacy save has no armed leg");
        assertEquals(0, restored.getLegId());
        assertNull(restored.getDestination());
        assertEquals(USSPosition.zero(), restored.getPosition(), "missing vc_pos → the origin");
    }

    @Test
    public void testReadAllowsHoldingShips() {
        // Phase C: a HOLDING ship (program finished / a fresh one) is a LEGITIMATE in-flight state — the USS must
        // restore it (it occupies its slot until the player deals with it).
        VoidcraftActiveShip s = ship("hold-rt");
        s.hold();
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(s.writeToNBT());
        assertNotNull(restored, "a holding ship restores");
        assertEquals(USSShipState.HOVERING, restored.getState());
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

    // region constructor loadout (durable ship state: a site consumes only its need, the remainder persists)

    private static NBTTagCompound constructorPayload(String uuid, String... keyAmounts) {
        NBTTagCompound payload = new NBTTagCompound();
        payload.setString("vc_uuid", uuid);
        NBTTagList loadout = new NBTTagList();
        for (int i = 0; i < keyAmounts.length; i += 2) {
            NBTTagCompound part = new NBTTagCompound();
            part.setString("key", keyAmounts[i]);
            part.setInteger("amount", Integer.parseInt(keyAmounts[i + 1]));
            loadout.appendTag(part);
        }
        payload.setTag(tectech.voidcraft.ship.VoidcraftNbt.TAG_BUILD_LOADOUT, loadout);
        return payload;
    }

    @Test
    public void testConstructorLoadoutInitializesFromPayload() {
        VoidcraftActiveShip s = VoidcraftActiveShip.launch(
            "ctor-1",
            "Ctor",
            SPEED,
            MINING_POWER,
            constructorPayload("ctor-1", "block.FRAME", "5", "block.CONTROLLER", "2"),
            null,
            null,
            0,
            ORIGIN);
        assertEquals(
            2,
            s.getBuildLoadout()
                .size(),
            "one key per loadout entry");
        assertEquals(
            5L,
            s.getBuildLoadout()
                .get("block.FRAME")
                .longValue());
        assertEquals(
            2L,
            s.getBuildLoadout()
                .get("block.CONTROLLER")
                .longValue());
        assertEquals(7L, s.buildLoadoutTotal());
        assertEquals(0L, ship("ctor-none").buildLoadoutTotal(), "a non-constructor ship carries no loadout");
    }

    @Test
    public void testConsumeBuildPartsClampsAndDropsEmptyKeys() {
        VoidcraftActiveShip s = VoidcraftActiveShip.launch(
            "ctor-2",
            "Ctor",
            SPEED,
            MINING_POWER,
            constructorPayload("ctor-2", "block.FRAME", "5"),
            null,
            null,
            0,
            ORIGIN);
        assertEquals(3L, s.consumeBuildParts("block.FRAME", 3), "take the requested count while it is on board");
        assertEquals(
            2L,
            s.getBuildLoadout()
                .get("block.FRAME")
                .longValue(),
            "the remainder stays on board");
        assertEquals(2L, s.consumeBuildParts("block.FRAME", 10), "clamped to what is on board");
        assertFalse(
            s.getBuildLoadout()
                .containsKey("block.FRAME"),
            "an emptied key is dropped");
        assertEquals(0L, s.buildLoadoutTotal());
        assertEquals(0L, s.consumeBuildParts("block.FRAME", 1), "nothing left to take");
        assertEquals(0L, s.consumeBuildParts("block.MISSING", 1), "an unknown key consumes nothing");
    }

    @Test
    public void testBuildLoadoutSurvivesNbtRoundTrip() {
        VoidcraftActiveShip s = VoidcraftActiveShip.launch(
            "ctor-3",
            "Ctor",
            SPEED,
            MINING_POWER,
            constructorPayload("ctor-3", "block.FRAME", "5", "block.CONTROLLER", "2"),
            null,
            null,
            0,
            ORIGIN);
        s.consumeBuildParts("block.FRAME", 2); // a CONSTRUCT leg took 2 of the 5 frames
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(s.writeToNBT());
        assertNotNull(restored);
        assertEquals(
            3L,
            restored.getBuildLoadout()
                .get("block.FRAME")
                .longValue(),
            "the persisted REMAINDER restores (the launch payload snapshot does not re-expand consumed parts)");
        assertEquals(
            2L,
            restored.getBuildLoadout()
                .get("block.CONTROLLER")
                .longValue());
        assertEquals(5L, restored.buildLoadoutTotal());
    }

    // endregion

    // region integrity (the time-limit pass: max on entry, -1 per second in the USS, 0 = lost)

    private static VoidcraftActiveShip shipWithIntegrity(long integrity) {
        NBTTagCompound payload = new NBTTagCompound();
        payload.setString("vc_uuid", "integ-ship");
        payload.setLong(tectech.voidcraft.ship.VoidcraftNbt.TAG_INTEGRITY, integrity);
        return VoidcraftActiveShip.launch("integ-ship", "Integ", SPEED, MINING_POWER, payload, null, null, 0, ORIGIN);
    }

    @Test
    public void testIntegrityStartsAtMaximum() {
        VoidcraftActiveShip s = shipWithIntegrity(300L);
        assertEquals(300L, s.getIntegrity(), "the ship enters the USS at its MAXIMUM (the blueprint's total)");
        assertEquals(300L, s.maxIntegrity());
        VoidcraftActiveShip none = ship("integ-none");
        assertEquals(0L, none.getIntegrity(), "no integrity in the payload → 0 (lost on the first tick)");
        assertEquals(0L, none.maxIntegrity());
    }

    @Test
    public void testIntegrityDropsOncePerSecond() {
        VoidcraftActiveShip s = shipWithIntegrity(300L);
        for (int i = 0; i < 19; i++) {
            assertFalse(s.tickIntegrity(), "tick " + (i + 1) + " of the first second — no drop yet");
            assertEquals(300L, s.getIntegrity());
        }
        assertFalse(s.tickIntegrity(), "tick 20 = one full second");
        assertEquals(299L, s.getIntegrity(), "exactly 1 drop per second");
        for (int i = 0; i < 40; i++) {
            s.tickIntegrity();
        }
        assertEquals(297L, s.getIntegrity(), "40 more ticks = 2 more seconds");
    }

    @Test
    public void testIntegrityZeroLosesTheShip() {
        VoidcraftActiveShip s = shipWithIntegrity(1L);
        for (int i = 0; i < 19; i++) {
            assertFalse(s.tickIntegrity());
        }
        assertTrue(s.tickIntegrity(), "the 20th tick drops the last point of integrity — the ship is lost");
        assertEquals(0L, s.getIntegrity());
        assertTrue(s.tickIntegrity(), "a lost ship stays lost");
    }

    @Test
    public void testZeroIntegrityLosesImmediately() {
        VoidcraftActiveShip s = ship("integ-zero");
        assertTrue(s.tickIntegrity(), "integrity 0 → lost on the first tick of the USS");
    }

    @Test
    public void testIntegrityRoundTripsMidCountdown() {
        VoidcraftActiveShip s = shipWithIntegrity(120L);
        for (int i = 0; i < 25; i++) {
            s.tickIntegrity(); // 1 full second + 5 ticks into the next
        }
        assertEquals(119L, s.getIntegrity());
        VoidcraftActiveShip restored = VoidcraftActiveShip.readFromNBT(s.writeToNBT());
        assertNotNull(restored);
        assertEquals(119L, restored.getIntegrity(), "the mid-countdown integrity survives the round-trip");
        // the partial second continues where it left off: 15 more ticks → one more drop
        for (int i = 0; i < 15; i++) {
            restored.tickIntegrity();
        }
        assertEquals(118L, restored.getIntegrity(), "the per-second counter resumes, it does not restart");
        for (int i = 0; i < 20; i++) {
            restored.tickIntegrity();
        }
        assertEquals(117L, restored.getIntegrity());
    }

    // endregion

    // region USSConstants leg-table checks (unchanged invariants the client's animation relies on)

    @Test
    public void testMiningLegIsAlwaysVisible() {
        // For EVERY mining power the MINING leg must be a visible hover (4.5–30 s) — a 10-tick hover would be
        // invisible at the fleet's scale.
        for (long power = 0; power <= 64; power++) {
            long ticks = USSConstants.mineTicks(power);
            assertTrue(ticks >= USSConstants.MINE_TICKS_MIN, "power " + power + " → " + ticks + " ticks");
            assertTrue(ticks >= 90L, "power " + power + " — the mining hover must be at least 4.5 s");
            assertTrue(ticks <= USSConstants.MINE_TICKS_MAX, "power " + power + " — the hover stays bounded");
        }
        for (long power = 1; power < 64; power++) {
            assertTrue(
                USSConstants.mineTicks(power) >= USSConstants.mineTicks(power + 1),
                "monotone non-increasing at power " + power);
        }
    }

    @Test
    public void testConstructTicksPerItem() {
        // One part per second per 100 construction power = 2000/power machine ticks per part.
        assertEquals(20L, USSConstants.constructTicksPerItem(100L), "100 power → one part per 20 ticks (1/s)");
        assertEquals(10L, USSConstants.constructTicksPerItem(200L), "200 power → two parts per second");
        assertEquals(1000L, USSConstants.constructTicksPerItem(2L), "2 power → one part per 50 s");
        assertEquals(1L, USSConstants.constructTicksPerItem(2_000_000L), "a part is never faster than one tick");
        // Power <= 0 degrades to the base rate (100 power): a blueprint without constructor components still
        // deposits at one part per second.
        assertEquals(20L, USSConstants.constructTicksPerItem(0L));
        assertEquals(20L, USSConstants.constructTicksPerItem(-5L));
        // Monotone non-increasing in power.
        for (long power = 1; power < 64; power++) {
            assertTrue(
                USSConstants.constructTicksPerItem(power) >= USSConstants.constructTicksPerItem(power + 1),
                "monotone non-increasing at power " + power);
        }
    }

    @Test
    public void testConstructionPowerReadsFromPayload() {
        // The construction power is denormalized into the payload at digitization (vc_construction) and read
        // LIVE by the CONSTRUCT pacing (like the scan power).
        NBTTagCompound payload = new NBTTagCompound();
        payload.setString("vc_uuid", "cp-1");
        VoidcraftActiveShip s = VoidcraftActiveShip
            .launch("cp-1", "cp-1", SPEED, MINING_POWER, payload, null, null, 0, ORIGIN);
        assertEquals(0L, s.getConstructionPower(), "no tag → 0 (the pacing degrades to the base rate)");
        payload.setLong(VoidcraftNbt.TAG_CONSTRUCTION, 240L);
        assertEquals(240L, s.getConstructionPower(), "the tag is read live (a payload write is visible)");
    }

    @Test
    public void testLegTicksTable() {
        assertEquals(
            USSConstants.travelTicks(DISTANCE, SPEED),
            USSConstants.legTicks(USSShipState.OUTBOUND, DISTANCE, SPEED, MINING_POWER));
        assertEquals(
            USSConstants.travelTicks(DISTANCE, SPEED),
            USSConstants.legTicks(USSShipState.RETURNING, DISTANCE, SPEED, MINING_POWER));
        assertEquals(
            USSConstants.mineTicks(MINING_POWER),
            USSConstants.legTicks(USSShipState.MINING, DISTANCE, SPEED, MINING_POWER));
        assertEquals(
            0L,
            USSConstants.legTicks(USSShipState.DOCKED, DISTANCE, SPEED, MINING_POWER),
            "docked ships have no leg");
        assertEquals(
            0L,
            USSConstants.legTicks(USSShipState.HOVERING, DISTANCE, SPEED, MINING_POWER),
            "a holding ship has no leg");
        assertEquals(0L, USSConstants.legTicks(null, DISTANCE, SPEED, MINING_POWER), "null state → 0");
    }

    @Test
    public void testScanTicksIsAlwaysVisibleAndBounded() {
        for (long power = 0; power <= 64; power++) {
            long ticks = USSConstants.scanTicks(power);
            assertTrue(ticks >= USSConstants.SCAN_TICKS_MIN, "power " + power + " → " + ticks + " ticks");
            assertTrue(ticks <= USSConstants.SCAN_TICKS_MAX, "power " + power + " — the scan stays bounded");
        }
        for (long power = 1; power < 64; power++) {
            assertTrue(
                USSConstants.scanTicks(power) >= USSConstants.scanTicks(power + 1),
                "monotone non-increasing at power " + power);
        }
        assertEquals(
            USSConstants.scanTicks(USSConstants.SCAN_POWER_SATURATION),
            USSConstants.scanTicks(1_000_000L),
            "above saturation the scan time stops shrinking");
    }

    @Test
    public void testWorkTicksIsRoleAware() {
        int explorer = VoidcraftRole.EXPLORER.getBit();
        int miner = VoidcraftRole.MINER.getBit();
        long scanPower = 8L;
        assertEquals(
            USSConstants.scanTicks(scanPower),
            USSConstants.workTicks(explorer, MINING_POWER, scanPower),
            "explorer work leg = scanTicks(scanPower)");
        assertEquals(
            USSConstants.mineTicks(MINING_POWER),
            USSConstants.workTicks(miner, MINING_POWER, scanPower),
            "miner work leg = mineTicks(miningPower)");
        assertEquals(
            USSConstants.mineTicks(MINING_POWER),
            USSConstants.workTicks(0, MINING_POWER, scanPower),
            "no role (legacy) work leg = mineTicks(miningPower)");
        assertEquals(
            USSConstants.scanTicks(scanPower),
            USSConstants.workTicks(explorer | miner, MINING_POWER, scanPower),
            "explorer+miner → explorer (scan)");
        assertEquals(
            USSConstants.scanTicks(scanPower),
            USSConstants.legTicks(USSShipState.MINING, DISTANCE, SPEED, MINING_POWER, explorer, scanPower),
            "legTicks(MINING, explorer) = scanTicks");
        assertEquals(
            USSConstants.mineTicks(MINING_POWER),
            USSConstants.legTicks(USSShipState.MINING, DISTANCE, SPEED, MINING_POWER, miner, scanPower),
            "legTicks(MINING, miner) = mineTicks");
        assertEquals(
            USSConstants.travelTicks(DISTANCE, SPEED),
            USSConstants.legTicks(USSShipState.OUTBOUND, DISTANCE, SPEED, MINING_POWER, explorer, scanPower),
            "OUTBOUND leg is distance-based (role-independent)");
        assertEquals(
            USSConstants.travelTicks(DISTANCE, SPEED),
            USSConstants.legTicks(USSShipState.RETURNING, DISTANCE, SPEED, MINING_POWER, explorer, scanPower),
            "RETURNING leg is distance-based (role-independent)");
    }

    @Test
    public void testTravelTimeGrowsWithDistance() {
        // "The travel time becomes an actual measure of distance divided by speed."
        long near = USSConstants.travelTicks(1.0, SPEED);
        long far = USSConstants.travelTicks(100.0, SPEED);
        assertTrue(far > near, "farther = longer (" + far + " > " + near + ")");
        assertTrue(near >= USSConstants.TRAVEL_TICKS_MIN && far <= USSConstants.TRAVEL_TICKS_MAX);
    }

    @Test
    public void testTravelSpeedMultiplier() {
        // The 5× speed test aid: legs divide by speed * SHIP_SPEED_MULTIPLIER — 15 blocks at speed 5 is
        // 15 * 200 / (5 * 5) = 120 ticks (a fifth of the 600 ticks the same leg takes without the multiplier).
        assertEquals(120L, USSConstants.travelTicks(15.0, 5.0), "the 5× speed multiplier applies to the travel time");
    }

    // endregion
}
