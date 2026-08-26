package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * Unit tests for the Phase C pilot — the bridge between the program executor and the game world
 * ({@link USSShipPilot}): the leg/arrival flow, the EXACTLY-ONCE side-effect invariants (including across a
 * save/reload), the HOME = origin rule, holding, and the executor invariants through the real (fake-world) bridge.
 */
public class USSShipPilotTest {

    private static final USSPosition ORIGIN = USSPosition.of(2.0, -1.0, 3.0);
    private static final USSPosition PLANET = USSPosition.of(30.0, 5.0, -20.0);
    private static final int SEED = 12345;

    private static USSNode move(String target, int index) {
        NBTTagCompound a = new NBTTagCompound();
        a.setString(USSProgramDefaults.PARAM_TARGET, target);
        if (index >= 0) {
            a.setInteger(USSProgramDefaults.PARAM_INDEX, index);
        }
        return USSNode.command(USSCommand.MOVE, a);
    }

    private static USSNode work() {
        return USSNode.command(USSCommand.WORK, new NBTTagCompound());
    }

    /** A Miner-style program: MOVE NEAREST_PLANET → WORK → MOVE HOME. */
    private static USSProgram minerProgram() {
        return USSProgram.of(Arrays.asList(move(USSProgramDefaults.TARGET_NEAREST_PLANET, 0), work(), move("HOME", 0)));
    }

    private static FakePilotWorld worldWithNearestPlanet() {
        FakePilotWorld w = new FakePilotWorld();
        w.resolve(USSProgramDefaults.TARGET_NEAREST_PLANET, 0, PLANET, 2, false);
        return w;
    }

    /**
     * A ship whose payload carries the PROGRAM (the MTE's flow: the assembler writes {@code vc_program} into the
     * item NBT, which becomes the launch payload — the pilot's {@code attach} re-reads it there).
     */
    private static VoidcraftActiveShip ship(String uuid, USSProgram program) {
        NBTTagCompound payload = new NBTTagCompound();
        payload.setString("vc_uuid", uuid);
        if (program != null) {
            NBTTagList list = program.writeToNBT();
            if (list != null) {
                payload.setTag(VoidcraftNbt.TAG_PROGRAM, list);
            }
        }
        return VoidcraftActiveShip.launch(uuid, uuid, 0.5, 1000L, payload, null, null, 0, ORIGIN);
    }

    // region creation

    @Test
    public void testEmptyProgramHoldsImmediately() {
        FakePilotWorld w = new FakePilotWorld();
        USSShipPilot p = USSShipPilot.create(ship("empty-1", null), null, w, SEED);
        assertEquals(
            USSShipState.HOVERING,
            p.getShip()
                .getState(),
            "an empty program → the ship holds");
        assertTrue(p.isCompleted(), "the executor is done");
        assertFalse(p.tick(), "no work to do (the ship is not delivered — it holds)");
        assertEquals(0, w.workCalls);
    }

    // endregion

    // region the full chip loop

    @Test
    public void testChipProgramRunsLegsWorkDeliveryInOrder() {
        FakePilotWorld w = worldWithNearestPlanet();
        USSShipPilot p = USSShipPilot.create(ship("full-1", minerProgram()), minerProgram(), w, SEED);
        assertEquals(
            USSShipState.HOVERING,
            p.getShip()
                .getState(),
            "the first instruction has not started yet (20-tick pacing)");

        // The first MOVE starts within the first step (20-tick pacing) and arms an OUTBOUND leg.
        int budget = 100;
        while (p.getShip()
            .getState() == USSShipState.HOVERING) {
            assertTrue(budget-- > 0, "the first MOVE must start");
            p.tick();
        }
        assertEquals(
            USSShipState.OUTBOUND,
            p.getShip()
                .getState());
        assertEquals(
            w.travelTicks,
            p.getShip()
                .getLegTotal(),
            "the leg duration comes from the world seam");
        assertEquals(
            1,
            p.getShip()
                .getLegId(),
            "the first leg bumps the id to 1");
        assertEquals(
            PLANET,
            p.getShip()
                .getDestination(),
            "the OUTBOUND leg aims at the resolved target");

        // Drive to the HOME delivery (bounded).
        budget = 700;
        boolean delivered = false;
        while (!delivered) {
            assertTrue(budget-- > 0, "the program must end with the HOME delivery");
            delivered = p.tick();
        }

        assertTrue(delivered);
        assertEquals(1, w.workCalls, "the work leg's side-effect fired EXACTLY ONCE");
        assertEquals(Arrays.asList(USSProgramDefaults.TARGET_NEAREST_PLANET), w.workKinds);
        assertEquals(Arrays.asList(2), w.workIndices);
        assertEquals(
            ORIGIN,
            p.getShip()
                .getPosition(),
            "the HOME leg delivered the ship back at its origin");
        assertEquals(
            3,
            p.getShip()
                .getLegId(),
            "three legs ran (OUTBOUND, WORK, RETURNING)");
    }

    @Test
    public void testRandomPlanetTargetRunsLikeMiner() {
        // pass-33 UI target: MOVE RANDOM_PLANET → the world picks the planet (here: index 1) → WORK mines it → HOME.
        FakePilotWorld w = new FakePilotWorld();
        w.resolve(USSProgramDefaults.TARGET_RANDOM_PLANET, 0, PLANET, 1, false);
        USSProgram program = USSProgram
            .of(Arrays.asList(move(USSProgramDefaults.TARGET_RANDOM_PLANET, -1), work(), move("HOME", 0)));
        USSShipPilot p = USSShipPilot.create(ship("rand-1", program), program, w, SEED);

        int budget = 100;
        while (p.getShip()
            .getState() == USSShipState.HOVERING) {
            assertTrue(budget-- > 0, "the first MOVE must start");
            p.tick();
        }
        assertEquals(
            USSShipState.OUTBOUND,
            p.getShip()
                .getState());
        assertEquals(
            PLANET,
            p.getShip()
                .getDestination(),
            "the OUTBOUND leg aims at the resolved random planet");
        assertEquals(
            1,
            p.getShip()
                .getTargetPlanet(),
            "the resolved body index drives the client hover target");

        budget = 700;
        boolean delivered = false;
        while (!delivered) {
            assertTrue(budget-- > 0, "the program must end with the HOME delivery");
            delivered = p.tick();
        }
        assertTrue(delivered);
        assertEquals(1, w.workCalls, "the work leg's side-effect fired EXACTLY ONCE");
        assertEquals(
            Arrays.asList(USSProgramDefaults.TARGET_RANDOM_PLANET),
            w.workKinds,
            "the WORK leg mines the random planet");
        assertEquals(Arrays.asList(1), w.workIndices);
        assertEquals(
            ORIGIN,
            p.getShip()
                .getPosition(),
            "delivered back at the origin");
        assertEquals(
            3,
            p.getShip()
                .getLegId(),
            "three legs ran (OUTBOUND, WORK, RETURNING)");
    }

    @Test
    public void testWorkLegIsARealLegWithWorkTicks() {
        FakePilotWorld w = worldWithNearestPlanet();
        USSShipPilot p = USSShipPilot.create(ship("work-1", minerProgram()), minerProgram(), w, SEED);
        // Let the OUTBOUND leg finish (its completion report drives the executor on the same tick).
        int budget = 400;
        while (p.getShip()
            .getState() != USSShipState.MINING) {
            assertTrue(budget-- > 0, "must reach the work leg");
            assertFalse(p.tick(), "no delivery before the work leg");
        }
        assertEquals(
            w.workTicks,
            p.getShip()
                .getLegTotal(),
            "the WORK leg uses the world's work duration");
        assertEquals(
            2,
            p.getShip()
                .getLegId());
        assertEquals(0, w.workCalls, "the side-effect fires only when the WORK leg COMPLETES");
    }

    @Test
    public void testRepeatLoopsTheWorkLeg() {
        // REPEAT 3 { WORK }: three distinct work legs → the side-effect fires EXACTLY three times (per leg, not
        // per program) — the "exactly once PER WORK LEG" contract.
        FakePilotWorld w = worldWithNearestPlanet();
        USSProgram program = USSProgram.of(
            Arrays.asList(
                move(USSProgramDefaults.TARGET_NEAREST_PLANET, 0),
                USSNode.repeat(3, Arrays.asList(work())),
                move("HOME", 0)));
        USSShipPilot p = USSShipPilot.create(ship("rep-1", program), program, w, SEED);
        int budget = 800;
        boolean delivered = false;
        while (!delivered) {
            assertTrue(budget-- > 0, "the repeat loop must terminate");
            delivered = p.tick();
        }
        assertTrue(delivered);
        assertEquals(3, w.workCalls, "REPEAT 3 → the work leg's side-effect fired exactly 3 times");
    }

    // endregion

    // region failure = SKIP (user decision #3)

    @Test
    public void testUnresolvableMoveIsSkippedAndTheProgramContinues() {
        // MOVE BOGUS (unresolvable) → SKIP (no leg, no crash) → WORK → the work leg still runs → HOME → delivery.
        FakePilotWorld w = new FakePilotWorld(); // no scripted targets at all
        USSProgram program = USSProgram.of(Arrays.asList(move("BOGUS", 99), work(), move("HOME", 0)));
        USSShipPilot p = USSShipPilot.create(ship("skip-1", program), program, w, SEED);
        int budget = 600;
        boolean delivered = false;
        while (!delivered) {
            assertTrue(budget-- > 0, "the skipped MOVE must not deadlock the program");
            delivered = p.tick();
        }
        assertTrue(delivered, "the program still completes after a skipped MOVE");
        assertEquals(1, w.workCalls, "the WORK leg ran (the bad MOVE was skipped, not fatal)");
        assertEquals(
            2,
            p.getShip()
                .getLegId(),
            "legs: WORK + RETURNING only (the failed MOVE armed none)");
        assertTrue(
            w.logs.stream()
                .anyMatch(l -> l.contains("unresolvable")),
            "the skip was logged");
    }

    @Test
    public void testRefusedLegStartIsSkipped() {
        // The world refuses the leg (legTicks ≤ 0) → the MOVE is SKIPPED (never stuck RUNNING), the program ends,
        // and the ship HOLDS.
        FakePilotWorld w = new FakePilotWorld();
        w.refuseLegStarts = true;
        USSProgram program = USSProgram.of(Arrays.asList(move("HOME", 0)));
        USSShipPilot p = USSShipPilot.create(ship("refuse-1", program), program, w, SEED);
        int budget = 100;
        while (!p.isCompleted()) {
            assertTrue(budget-- > 0, "a refused leg skips the instruction and ends the program");
            p.tick();
        }
        assertTrue(p.isCompleted());
        assertEquals(
            USSShipState.HOVERING,
            p.getShip()
                .getState(),
            "the ship holds (no stuck leg)");
    }

    // endregion

    // region exactly-once across a save/reload (THE Phase C invariant)

    @Test
    public void testWorkSideEffectFiresOnceAcrossReloadMidWorkLeg() {
        FakePilotWorld w = worldWithNearestPlanet();
        VoidcraftActiveShip ship = ship("reload-1", minerProgram());
        USSShipPilot p1 = USSShipPilot.create(ship, minerProgram(), w, SEED);
        // Run until the WORK leg is armed and one tick from finishing (the side-effect has NOT fired yet).
        int budget = 600;
        while (p1.getShip()
            .getState() != USSShipState.MINING) {
            assertTrue(budget-- > 0, "must reach the work leg");
            p1.tick();
        }
        budget = 600;
        while (p1.getShip()
            .getTicksRemaining() > 1) {
            assertTrue(budget-- > 0, "must get within 1 tick of the work leg's end");
            p1.tick();
        }
        assertEquals(0, w.workCalls, "the side-effect has not fired yet");

        // SAVE: the world saves here (the leg finishes on the NEXT tick, post-reload).
        NBTTagCompound saved = p1.writeToNBT();
        NBTTagCompound shipTag = ship.writeToNBT();
        shipTag.setTag(USSShipPilot.TAG_PILOT, saved);

        // RELOAD: fresh ship + fresh pilot (attach) — the leg state must come back with it.
        VoidcraftActiveShip ship2 = VoidcraftActiveShip.readFromNBT(shipTag);
        assertNotNull(ship2);
        assertEquals(USSShipState.MINING, ship2.getState(), "the work leg state survives the reload");
        USSShipPilot p2 = USSShipPilot.attach(ship2, w, shipTag);
        assertEquals(0, w.workCalls, "no side-effect yet after the attach either");

        // Finish the leg post-reload — the side-effect fires EXACTLY ONCE, then the program runs to delivery.
        budget = 600;
        boolean delivered = false;
        while (!delivered) {
            assertTrue(budget-- > 0, "must deliver");
            delivered = p2.tick();
        }
        assertTrue(delivered);
        assertEquals(1, w.workCalls, "the work side-effect fired EXACTLY ONCE across the save/reload boundary");
    }

    // endregion

    // region HOME + holding

    @Test
    public void testHomeResolvesToTheOrigin() {
        FakePilotWorld w = new FakePilotWorld();
        USSShipPilot p = USSShipPilot.create(ship("home-1", null), null, w, SEED);
        assertEquals(ORIGIN, p.resolveMoveTarget("HOME", 0), "HOME = the pilot's origin (the gateway)");
    }

    @Test
    public void testProgramEndHoldsTheShip() {
        // A program with NO HOME: it ends, and the ship HOLDS where it is (the user-accepted "slot stays occupied").
        FakePilotWorld w = worldWithNearestPlanet();
        USSProgram program = USSProgram.of(Arrays.asList(move(USSProgramDefaults.TARGET_NEAREST_PLANET, 0), work()));
        USSShipPilot p = USSShipPilot.create(ship("end-1", program), program, w, SEED);
        int budget = 600;
        while (!p.isCompleted()) {
            assertTrue(budget-- > 0, "the program must finish");
            p.tick();
        }
        assertTrue(p.isCompleted());
        assertEquals(
            USSShipState.HOVERING,
            p.getShip()
                .getState(),
            "program end → the ship HOLDS (no implicit return)");
        assertEquals(
            PLANET,
            p.getShip()
                .getPosition(),
            "…at the last body (the program ended there)");
        assertFalse(p.tick(), "a finished pilot never delivers");
        assertEquals(1, w.workCalls);
    }

    @Test
    public void testStopEndsTheProgramAndHolds() {
        FakePilotWorld w = worldWithNearestPlanet();
        USSProgram program = USSProgram.of(
            Arrays.asList(
                move(USSProgramDefaults.TARGET_NEAREST_PLANET, 0),
                USSNode.command(USSCommand.STOP, new NBTTagCompound())));
        USSShipPilot p = USSShipPilot.create(ship("stop-1", program), program, w, SEED);
        int budget = 600;
        while (!p.isCompleted()) {
            assertTrue(budget-- > 0, "STOP must end the program");
            assertFalse(p.tick(), "STOP is not a delivery (only MOVE HOME delivers)");
        }
        assertTrue(p.isCompleted());
        assertEquals(
            USSShipState.HOVERING,
            p.getShip()
                .getState(),
            "the ship holds where it was sent");
        assertEquals(0, w.workCalls, "STOP does no work (no side-effect)");
    }

    // endregion

    // region the context surface

    @Test
    public void testVariablesAndStatsFlowThroughTheWorld() {
        FakePilotWorld w = worldWithNearestPlanet();
        // A ship with a 10-slot cargo pool (capacity 10 × 100 = 1000 units, pass 27).
        NBTTagCompound payload = new NBTTagCompound();
        payload.setString("vc_uuid", "ctx-1");
        payload.setLong("vc_cargo", 10L);
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch("ctx-1", "ctx-1", 0.5, 1000L, payload, null, null, 0, ORIGIN);
        USSShipPilot p = USSShipPilot.create(ship, null, w, SEED);

        p.writeVar(5, "hello");
        assertEquals("hello", w.variables.get(5), "WRITE reached the world's variable space");
        assertEquals("hello", p.readVar(5));
        assertEquals("", p.readVar(255), "an unwritten slot reads as the empty string");

        assertEquals("1000", p.stat(USSShipStat.CARGO_FREE), "CARGO_FREE = capacity × 100 (pass 27)");
        assertEquals("0", p.stat(USSShipStat.CARGO_USED), "nothing has been mined yet");
        assertEquals("0", p.stat(USSShipStat.CARGO_FULL), "the hold is not full");
        assertEquals("0", p.stat(USSShipStat.DIST_TO_TARGET), "no destination yet");
        assertEquals("2.0", p.stat(USSShipStat.POSITION_X), "position stats are live (doubles print with .0)");
        assertEquals("7", p.stat(USSShipStat.RIPPLES_UNSCANNED), "the unscanned ripple count flows from the world");
        assertEquals("", p.stat(USSShipStat.byId(999)), "unknown stat ids degrade to the empty string");

        // nextInt is seeded and bounded.
        int a = p.nextInt(10);
        int b = p.nextInt(10);
        USSShipPilot q = USSShipPilot.create(ship("ctx-2", null), null, w, SEED);
        assertEquals(a, q.nextInt(10), "same seed → same first draw");
        assertEquals(b, q.nextInt(10), "same seed → same second draw");
        for (int i = 0; i < 20; i++) {
            int v = q.nextInt(7);
            assertTrue(v >= 0 && v < 7, "nextInt stays in [0, bound): " + v);
        }
        assertEquals(0, p.nextInt(0), "bound ≤ 0 → 0");
    }

    @Test
    public void testResolveFunnelsLiteralsAndVariables() {
        FakePilotWorld w = worldWithNearestPlanet();
        USSShipPilot p = USSShipPilot.create(ship("res-1", null), null, w, SEED);
        w.writeVar(3, "from-var");
        assertEquals("literal", p.resolve(USSValue.literal("literal")));
        assertEquals("from-var", p.resolve(USSValue.variable(3)), "VAR resolves through the world");
        assertEquals("", p.resolve(null), "corrupt values degrade");
    }

    @Test
    public void testDistanceToIsLive() {
        FakePilotWorld w = worldWithNearestPlanet();
        USSShipPilot p = USSShipPilot.create(ship("dist-1", null), null, w, SEED);
        double expected = Math.sqrt(28.0 * 28.0 + 6.0 * 6.0 + 23.0 * 23.0);
        assertEquals(expected, p.distanceTo(PLANET), 1e-9);
        assertEquals(0.0, p.distanceTo(null), "null → 0");
    }

    // endregion

    // region NBT + attach

    @Test
    public void testWriteToNbtNestsTheExecutorAndPilotState() {
        FakePilotWorld w = worldWithNearestPlanet();
        USSShipPilot p = USSShipPilot.create(ship("nbt-1", minerProgram()), minerProgram(), w, SEED);
        NBTTagCompound nbt = p.writeToNBT();
        assertNotNull(nbt.getTag("vc_exec"), "the executor's cursor is nested");
        assertEquals(false, nbt.getBoolean("vc_leg_work"), "no work leg yet");
        assertEquals(false, nbt.getBoolean("vc_leg_home"), "no HOME leg yet");
        assertEquals("", nbt.getString("vc_last_kind"), "no target resolved yet");
        assertNotNull(nbt.getTag("vc_origin"), "the origin persists (HOME = origin)");
    }

    @Test
    public void testAttachWithNoPilotTagIsFresh() {
        FakePilotWorld w = worldWithNearestPlanet();
        NBTTagCompound shipTag = ship("fresh-1", minerProgram()).writeToNBT(); // no vc_pilot (first save)
        VoidcraftActiveShip ship2 = VoidcraftActiveShip.readFromNBT(shipTag);
        assertNotNull(ship2);
        USSShipPilot p = USSShipPilot.attach(ship2, w, shipTag);
        assertNotNull(p);
        assertFalse(p.isCompleted(), "a fresh attach re-runs the program (the pilot is empty, not broken)");
        assertEquals(USSShipState.HOVERING, ship2.getState(), "the ship still holds (its first leg is not armed yet)");
    }

    @Test
    public void testAttachWithCorruptPilotTagFailsSafe() {
        FakePilotWorld w = worldWithNearestPlanet();
        NBTTagCompound shipTag = ship("corrupt-1", minerProgram()).writeToNBT();
        NBTTagCompound garbage = new NBTTagCompound();
        garbage.setString("vc_exec", "not a compound"); // wrongly-typed nested tag — must not crash the load
        shipTag.setTag(USSShipPilot.TAG_PILOT, garbage);
        VoidcraftActiveShip ship2 = VoidcraftActiveShip.readFromNBT(shipTag);
        assertNotNull(ship2);
        USSShipPilot p = USSShipPilot.attach(ship2, w, shipTag);
        assertNotNull(p, "attach never crashes — it degrades");
        assertTrue(p.isCompleted(), "a corrupt pilot NBT fails SAFE to COMPLETED (the ship holds, never a half-run)");
        assertEquals(USSShipState.HOVERING, ship2.getState());
    }

    @Test
    public void testAttachRestoresLegBookkeeping() {
        FakePilotWorld w = worldWithNearestPlanet();
        VoidcraftActiveShip ship = ship("book-1", minerProgram());
        USSShipPilot p1 = USSShipPilot.create(ship, minerProgram(), w, SEED);
        int budget = 400;
        while (p1.getShip()
            .getState() != USSShipState.MINING) {
            assertTrue(budget-- > 0);
            p1.tick();
        }
        // Mid-work-leg: attach a fresh pilot from the saved state — the target bookkeeping must come back.
        NBTTagCompound saved = p1.writeToNBT();
        NBTTagCompound shipTag = ship.writeToNBT();
        shipTag.setTag(USSShipPilot.TAG_PILOT, saved);
        VoidcraftActiveShip ship2 = VoidcraftActiveShip.readFromNBT(shipTag);
        assertNotNull(ship2);
        USSShipPilot p2 = USSShipPilot.attach(ship2, w, shipTag);
        assertFalse(p2.isCompleted(), "the program is still running after the attach");
        assertEquals(USSProgramDefaults.TARGET_NEAREST_PLANET, p2.getLastKind(), "the last kind was recorded");
        assertEquals(2, p2.getLastIndex(), "the last index was recorded");
        assertEquals(ORIGIN, p2.getOrigin(), "the origin was recorded (HOME = origin survives the attach)");
    }

    // endregion
}
