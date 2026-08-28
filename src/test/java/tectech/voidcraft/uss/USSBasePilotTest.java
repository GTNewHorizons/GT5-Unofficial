package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * Unit tests for the Voidbase pilot (the base-mode bridge between the program executor and the game world):
 * the zero-length leg (a base sits at its anchor), the SKIP rules (other anchors / HOME / SHIP), the MINE
 * no-op, the REPAIR relay, program end = the base keeps standing, and the exactly-once / save-reload invariants
 * through the real (fake-world) bridge.
 */
public class USSBasePilotTest {

    private static final USSPosition BASE_POS = USSPosition.of(50.0, 0.0, 50.0);
    private static final int SEED = 999;

    private static USSNode move(String target, int index) {
        NBTTagCompound a = new NBTTagCompound();
        a.setString(USSProgramDefaults.PARAM_TARGET, target);
        if (index >= 0) {
            a.setInteger(USSProgramDefaults.PARAM_INDEX, index);
        }
        return USSNode.command(USSCommand.MOVE, a);
    }

    private static USSNode work() {
        return USSNode.command(USSCommand.MINE, new NBTTagCompound());
    }

    private static USSNode repair() {
        return USSNode.command(USSCommand.REPAIR, new NBTTagCompound());
    }

    /**
     * A base whose payload carries the PROGRAM (the MTE flow: the blueprint carries the digitized controller
     * program; the pilot attach re-reads it from the base payload).
     */
    /** A 1x1x2 base blueprint: controller + a frame (a valid base payload for the NBT round-trip). */
    private static VoidcraftBlueprint testBlueprint() {
        byte[] grid = { (byte) VoidcraftComponent.CONTROLLER.toGridValue(),
            (byte) VoidcraftComponent.FRAME.toGridValue(), };
        return VoidcraftBlueprint.ofBase(1, 1, 2, grid);
    }

    private static VoidcraftActiveBase base(String uuid, USSBaseAnchor anchor, USSProgram program) {
        NBTTagCompound payload = new NBTTagCompound();
        VoidcraftNbt.write(payload, testBlueprint(), uuid, uuid, 20L);
        if (program != null) {
            NBTTagList list = program.writeToNBT();
            if (list != null) {
                payload.setTag(VoidcraftNbt.TAG_PROGRAM, list);
            }
        }
        return VoidcraftActiveBase.launch(uuid, uuid, anchor, payload, SEED, BASE_POS);
    }

    /** How many times the base MINE ran (its log line is one per completed MINE leg). */
    private static int workLogCount(FakePilotWorld w) {
        int n = 0;
        for (String l : w.baseLogs) {
            if (l.contains("MINE")) {
                n++;
            }
        }
        return n;
    }

    // region creation + program end

    @Test
    public void testEmptyProgramHoldsImmediately() {
        FakePilotWorld w = new FakePilotWorld();
        USSBasePilot p = USSBasePilot.create(base("empty-1", USSBaseAnchor.planet(2), null), null, w);
        assertTrue(p.isCompleted(), "an empty program is done");
        p.tick(); // a finished pilot is a quiet no-op
        assertEquals(0, w.constructStartCalls);
    }

    @Test
    public void testLoneWorkProgramLoopsAndTheBaseStands() {
        // The user's one-command base program: no STOP anywhere — the invisible while re-runs the lone MINE
        // forever, and the base keeps standing (only integrity decay decommissions it).
        FakePilotWorld w = new FakePilotWorld();
        USSProgram program = USSProgram.of(Arrays.asList(work()));
        USSBasePilot p = USSBasePilot.create(base("end-1", USSBaseAnchor.planet(2), program), program, w);
        int budget = 1000;
        while (workLogCount(w) < 2) {
            assertTrue(budget-- > 0, "the lone MINE must loop (the wrap)");
            assertFalse(p.isCompleted(), "a program without STOP never ends");
            p.tick();
        }
        assertEquals(2, workLogCount(w), "the WORK ran, the program wrapped, and the WORK ran again");
        assertNotNull(p.getBase(), "the base still exists (the pilot never discards it)");
    }

    // endregion

    // region the zero-length leg (a base sits at its anchor)

    @Test
    public void testMoveToOwnAnchorIsAnInstantLeg() {
        // A base at PLANET 2 with MOVE PLANET 2: the target IS the base anchor → a zero-length leg that
        // completes on the next tick (the program continues).
        FakePilotWorld w = new FakePilotWorld();
        USSProgram program = USSProgram.of(Arrays.asList(move(USSProgramDefaults.TARGET_PLANET, 2), work()));
        USSBasePilot p = USSBasePilot.create(base("self-1", USSBaseAnchor.planet(2), program), program, w);
        int budget = 1000;
        while (workLogCount(w) < 1) {
            assertTrue(budget-- > 0, "the program must reach the WORK (past the instant leg)");
            assertFalse(p.isCompleted(), "a program without STOP never ends");
            p.tick();
        }
        assertTrue(
            w.baseLogs.stream()
                .anyMatch(l -> l.contains("MINE")),
            "the program continued past the instant leg");
        assertEquals(
            BASE_POS,
            p.getBase()
                .position(),
            "the base never moved");
    }

    @Test
    public void testMoveToAnotherAnchorIsSkipped() {
        // A base at PLANET 1 with MOVE PLANET 2: unresolvable (the base is immobile) → SKIP → the program
        // continues (the WORK still runs).
        FakePilotWorld w = new FakePilotWorld();
        USSProgram program = USSProgram.of(Arrays.asList(move(USSProgramDefaults.TARGET_PLANET, 2), work()));
        USSBasePilot p = USSBasePilot.create(base("other-1", USSBaseAnchor.planet(1), program), program, w);
        int budget = 1000;
        while (workLogCount(w) < 1) {
            assertTrue(budget-- > 0, "the WORK must run (after the skipped MOVE)");
            assertFalse(p.isCompleted(), "a program without STOP never ends");
            p.tick();
        }
        assertTrue(
            w.baseLogs.stream()
                .anyMatch(l -> l.contains("unresolvable")),
            "the skip was logged");
        assertTrue(
            w.baseLogs.stream()
                .anyMatch(l -> l.contains("MINE")),
            "the program continued after the skip");
        assertEquals(0, w.workCalls, "the base WORK never dispatches a ship-side work side-effect");
    }

    @Test
    public void testHomeAndShipAreSkipped() {
        FakePilotWorld w = new FakePilotWorld();
        USSProgram program = USSProgram
            .of(Arrays.asList(move("HOME", 0), move(USSProgramDefaults.TARGET_SHIP, 0), work()));
        USSBasePilot p = USSBasePilot.create(base("home-1", USSBaseAnchor.star(), program), program, w);
        int budget = 1000;
        while (workLogCount(w) < 1) {
            assertTrue(budget-- > 0, "the WORK must run (after both skipped MOVEs)");
            assertFalse(p.isCompleted(), "a program without STOP never ends");
            p.tick();
        }
        assertTrue(
            w.baseLogs.stream()
                .anyMatch(l -> l.contains("MINE")),
            "both SKIPs left the program running");
        assertEquals(0, w.workCalls);
    }

    @Test
    public void testResolveMoveTargetOnlyMatchesTheOwnAnchor() {
        FakePilotWorld w = new FakePilotWorld();
        USSBasePilot p = USSBasePilot.create(base("res-1", USSBaseAnchor.planet(2), null), null, w);
        assertEquals(
            BASE_POS,
            p.resolveMoveTarget(USSProgramDefaults.TARGET_PLANET, 2),
            "own anchor resolves in place");
        assertEquals(
            BASE_POS,
            p.resolveMoveTarget(USSProgramDefaults.TARGET_NEAREST_PLANET, 2),
            "a resolved kind of the own anchor");
        assertNull(p.resolveMoveTarget(USSProgramDefaults.TARGET_PLANET, 1), "another planet is unreachable");
        assertNull(p.resolveMoveTarget(USSProgramDefaults.TARGET_STAR, -1), "the star is unreachable");
        assertNull(p.resolveMoveTarget("HOME", 0), "HOME is unreachable on a base");
        assertNull(p.resolveMoveTarget(USSProgramDefaults.TARGET_SHIP, 0), "SHIP rendezvous is unreachable");
        assertNull(p.resolveMoveTarget("BOGUS", 0), "unknown targets are unreachable");
        USSBasePilot q = USSBasePilot.create(base("res-2", USSBaseAnchor.ripple(9), null), null, w);
        assertEquals(
            BASE_POS,
            q.resolveMoveTarget(USSProgramDefaults.TARGET_RIPPLE, 9),
            "a ripple anchor matches its own ripple");
        assertNull(q.resolveMoveTarget(USSProgramDefaults.TARGET_RIPPLE, 10));
    }

    // endregion

    // region REPAIR (the station repairs itself)

    @Test
    public void testRepairRunsUntilTheWorldSaysDone() {
        // The pilot RELAYS the repair to the world seam: begin → RUNNING, then the world is polled every tick
        // until it reports the integrity is full (DONE).
        FakePilotWorld w = new FakePilotWorld();
        w.repairTickTrueLeft = 3; // three more true ticks, then false → DONE
        USSProgram program = USSProgram.of(Arrays.asList(repair()));
        USSBasePilot p = USSBasePilot.create(base("rep-1", USSBaseAnchor.planet(0), program), program, w);
        int budget = 1000;
        while (w.baseRepairTicks < 5) {
            assertTrue(budget-- > 0, "the repair must poll to done (and the wrap must start it again)");
            assertFalse(p.isCompleted(), "a program without STOP never ends");
            p.tick();
        }
        assertEquals(
            5,
            w.baseRepairTicks,
            "the first repair polled to done (3 true + the final false), the wrap re-started it (one more poll)");
        assertTrue(w.baseRepairStarts >= 2, "the wrapped pass re-started the repair (the program loops)");
    }

    @Test
    public void testRefusedRepairStartIsSkipped() {
        // The world refuses the repair (nothing to restore / no energy) → the REPAIR instruction SKIPs and the
        // program continues.
        FakePilotWorld w = new FakePilotWorld();
        w.repairStartResult = false;
        USSProgram program = USSProgram.of(Arrays.asList(repair(), work()));
        USSBasePilot p = USSBasePilot.create(base("rep-2", USSBaseAnchor.planet(0), program), program, w);
        int budget = 1000;
        while (workLogCount(w) < 1) {
            assertTrue(budget-- > 0, "the WORK must run (after the skipped REPAIR)");
            assertFalse(p.isCompleted(), "a program without STOP never ends");
            p.tick();
        }
        assertEquals(1, w.baseRepairStarts);
        assertEquals(0, w.baseRepairTicks, "no polling after a refused start");
        assertTrue(
            w.baseLogs.stream()
                .anyMatch(l -> l.contains("REPAIR") && l.contains("skipping")),
            "the skip was logged");
        assertTrue(
            w.baseLogs.stream()
                .anyMatch(l -> l.contains("MINE")),
            "the program continued after the skip");
    }

    // endregion

    // region CONSTRUCT (a base never constructs)

    @Test
    public void testConstructIsAlwaysSkippedOnABase() {
        // A base carries no parts (a build loadout belongs to the Constructor in flight) — CONSTRUCT SKIPs even
        // when the world would accept it.
        FakePilotWorld w = new FakePilotWorld();
        w.constructStartResult = true; // the world would build — the base must not ask it to
        USSProgram program = USSProgram
            .of(Arrays.asList(USSNode.command(USSCommand.CONSTRUCT, new NBTTagCompound()), work()));
        USSBasePilot p = USSBasePilot.create(base("con-1", USSBaseAnchor.planet(0), program), program, w);
        int budget = 1000;
        while (workLogCount(w) < 1) {
            assertTrue(budget-- > 0, "the WORK must run (after the skipped CONSTRUCT)");
            assertFalse(p.isCompleted(), "a program without STOP never ends");
            p.tick();
        }
        assertEquals(0, w.constructStartCalls, "the base never forwards CONSTRUCT to the world (it carries no parts)");
        assertTrue(
            w.baseLogs.stream()
                .anyMatch(l -> l.contains("CONSTRUCT") && l.contains("skipping")),
            "the SKIP was logged");
        assertTrue(
            w.baseLogs.stream()
                .anyMatch(l -> l.contains("MINE")),
            "the SKIP left the program running");
    }

    // endregion

    // region the context surface

    @Test
    public void testBaseStats() {
        FakePilotWorld w = new FakePilotWorld();
        USSBasePilot p = USSBasePilot.create(base("stat-1", USSBaseAnchor.planet(2), null), null, w);
        assertEquals("BASE", p.stat(USSShipStat.STATE), "the base reports itself as a BASE");
        assertEquals("PLANET:2", p.stat(USSShipStat.TARGET), "the target stat is the base anchor");
        assertEquals("0", p.stat(USSShipStat.DIST_TO_TARGET), "a base sits at its anchor");
        assertEquals("0", p.stat(USSShipStat.SPEED), "a base does not fly");
        assertEquals("0", p.stat(USSShipStat.CARGO_FREE), "a station carries no cargo (v1)");
        assertEquals("0", p.stat(USSShipStat.CARGO_USED));
        assertEquals("0", p.stat(USSShipStat.CARGO_FULL));
        assertEquals("50.0", p.stat(USSShipStat.POSITION_X), "the position stats are live");
        assertEquals("0.0", p.stat(USSShipStat.POSITION_Y));
        assertEquals("50.0", p.stat(USSShipStat.POSITION_Z));
        assertEquals("7", p.stat(USSShipStat.RIPPLES_UNSCANNED), "the unscanned count flows from the world");
        USSBasePilot star = USSBasePilot.create(base("stat-2", USSBaseAnchor.star(), null), null, w);
        assertEquals("STAR", star.stat(USSShipStat.TARGET));
        USSBasePilot ripple = USSBasePilot.create(base("stat-3", USSBaseAnchor.ripple(3), null), null, w);
        assertEquals("RIPPLE:3", ripple.stat(USSShipStat.TARGET));
    }

    @Test
    public void testVariablesAndNextIntFlowThroughTheWorld() {
        FakePilotWorld w = new FakePilotWorld();
        USSBasePilot p = USSBasePilot.create(base("var-1", USSBaseAnchor.planet(0), null), null, w);
        p.writeVar(5, "hello");
        assertEquals("hello", w.variables.get(5), "WRITE reached the world variable space");
        assertEquals("hello", p.readVar(5));
        assertEquals("", p.readVar(255));
        int a = p.nextInt(10);
        int b = p.nextInt(10);
        USSBasePilot q = USSBasePilot.create(base("var-2", USSBaseAnchor.planet(0), null), null, w);
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
        FakePilotWorld w = new FakePilotWorld();
        USSBasePilot p = USSBasePilot.create(base("res2-1", USSBaseAnchor.planet(0), null), null, w);
        w.writeVar(3, "from-var");
        assertEquals("literal", p.resolve(USSValue.literal("literal")));
        assertEquals("from-var", p.resolve(USSValue.variable(3)));
        assertEquals("", p.resolve(null));
        assertEquals(0.0, p.distanceTo(null), "null destination → 0");
    }

    // endregion

    // region NBT + attach (the save/reload invariant)

    @Test
    public void testAttachRestoresTheCursorAndResumesTheProgram() {
        // Mid-program save → reload → the base pilot resumes EXACTLY where it left off (no re-run, no
        // double-fire) — then the invisible while carries it into its second pass.
        FakePilotWorld w = new FakePilotWorld();
        w.repairTickTrueLeft = 2;
        USSProgram program = USSProgram.of(Arrays.asList(move(USSProgramDefaults.TARGET_PLANET, 2), work(), repair()));
        // drive the pilot past the MOVE + WORK into the REPAIR
        USSBasePilot p1 = USSBasePilot.create(base("nbt-1", USSBaseAnchor.planet(2), program), program, w);
        int budget = 200;
        while (w.baseRepairStarts < 1) {
            assertTrue(budget-- > 0, "must reach the REPAIR");
            p1.tick();
        }
        assertEquals(0, w.baseRepairTicks, "the repair just began (no poll yet) — a clean save point");
        // SAVE + RELOAD.
        NBTTagCompound baseTag = p1.getBase()
            .writeToNBT();
        baseTag.setTag(USSBasePilot.TAG_PILOT, p1.writeToNBT());
        VoidcraftActiveBase base2 = VoidcraftActiveBase.readFromNBT(baseTag);
        assertNotNull(base2);
        USSBasePilot p2 = USSBasePilot.attach(base2, w, baseTag);
        assertFalse(p2.isCompleted(), "the program is still running after the attach");
        // Drive it: the first repair finishes (its 2 true + the final false polls happen after the reload),
        // then the program WRAPS (MOVE + WORK + the second REPAIR start).
        int budget2 = 1000;
        while (w.baseRepairStarts < 2) {
            assertTrue(budget2-- > 0, "the program must resume and wrap (the second REPAIR start)");
            p2.tick();
        }
        assertEquals(2, w.baseRepairStarts, "the repair began once per pass (no re-fire across the reload)");
        assertEquals(
            3,
            w.baseRepairTicks,
            "all the first-pass repair polls happened after the reload (2 true + the final false)");
    }

    @Test
    public void testAttachWithNoPilotTagIsFresh() {
        FakePilotWorld w = new FakePilotWorld();
        NBTTagCompound baseTag = base("fresh-1", USSBaseAnchor.planet(2), USSProgram.of(Arrays.asList(work())))
            .writeToNBT(); // no vc_pilot (first save)
        VoidcraftActiveBase base2 = VoidcraftActiveBase.readFromNBT(baseTag);
        assertNotNull(base2);
        USSBasePilot p = USSBasePilot.attach(base2, w, baseTag);
        assertNotNull(p);
        assertFalse(p.isCompleted(), "a fresh attach re-runs the program");
        int budget = 1000;
        while (workLogCount(w) < 1) {
            assertTrue(budget-- > 0, "the program must run (the WORK)");
            p.tick();
        }
        assertTrue(
            w.baseLogs.stream()
                .anyMatch(l -> l.contains("MINE")),
            "the re-run program reached the WORK");
    }

    @Test
    public void testAttachWithCorruptPilotTagFailsSafe() {
        FakePilotWorld w = new FakePilotWorld();
        NBTTagCompound baseTag = base("corrupt-1", USSBaseAnchor.planet(2), USSProgram.of(Arrays.asList(work())))
            .writeToNBT();
        NBTTagCompound garbage = new NBTTagCompound();
        garbage.setString("vc_exec", "not a compound"); // wrongly-typed nested tag — must not crash the load
        baseTag.setTag(USSBasePilot.TAG_PILOT, garbage);
        VoidcraftActiveBase base2 = VoidcraftActiveBase.readFromNBT(baseTag);
        assertNotNull(base2);
        USSBasePilot p = USSBasePilot.attach(base2, w, baseTag);
        assertNotNull(p, "attach never crashes — it degrades");
        assertTrue(p.isCompleted(), "a corrupt pilot NBT fails SAFE to COMPLETED (the base holds, never a half-run)");
    }

    // endregion

    // region mining leg (a MINE on a base with mining power runs a real mining leg)

    private static boolean logContains(List<String> logs, String s) {
        for (String l : logs) {
            if (l.contains(s)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testWorkWithMiningPowerRunsAMiningLeg() {
        FakePilotWorld w = new FakePilotWorld();
        USSProgram program = USSProgram.of(Arrays.asList(work()));
        VoidcraftActiveBase b = base("mine-1", USSBaseAnchor.planet(2), program);
        b.payload()
            .setLong(VoidcraftNbt.TAG_MINING, 4L); // a station with mining power
        USSBasePilot p = USSBasePilot.create(b, program, w);

        // The executor steps a node every STEP_TICKS (20) ticks: the MINE command starts the leg on its step,
        // with the full duration from the ship's table (mineTicks(4) = 150 ticks).
        int ticks = 0;
        while (p.miningLegId() < 1 && ++ticks < 100) {
            p.tick();
        }
        assertEquals(1, p.miningLegId(), "the first MINE started mining leg 1 (tick " + ticks + ")");
        assertEquals(
            USSConstants.mineTicks(4L),
            (long) p.miningTicks(),
            "the leg runs for the full mineTicks duration");
        assertEquals(String.valueOf(USSConstants.mineTicks(4L)), p.stat(USSShipStat.TICKS_IN_LEG));

        int total = ticks;
        while (total < 400 && !logContains(w.baseLogs, "mining leg complete")) {
            p.tick();
            total++;
        }
        assertTrue(logContains(w.baseLogs, "mining leg complete"), "the mining leg completes (tick " + total + ")");
        assertEquals(ticks + USSConstants.mineTicks(4L), (long) total, "the leg completes after its full duration");
        assertEquals(0, p.miningTicks(), "the finished leg is cleared");
        assertEquals(0, p.miningTotal());
    }

    @Test
    public void testMiningLegWrapsForTheNextWork() {
        FakePilotWorld w = new FakePilotWorld();
        USSProgram program = USSProgram.of(Arrays.asList(work()));
        VoidcraftActiveBase b = base("mine-2", USSBaseAnchor.planet(2), program);
        b.payload()
            .setLong(VoidcraftNbt.TAG_MINING, 4L);
        USSBasePilot p = USSBasePilot.create(b, program, w);
        int ticks = 0;
        while (p.miningLegId() < 2 && ticks < 400) {
            p.tick();
            ticks++;
        }
        assertEquals(2, p.miningLegId(), "the invisible-while re-run of MINE starts mining leg 2 (tick " + ticks + ")");
        assertEquals(USSConstants.mineTicks(4L), (long) p.miningTicks(), "the second leg runs again at full duration");
    }

    @Test
    public void testWorkWithoutMiningPowerStaysAnInstantNoOp() {
        FakePilotWorld w = new FakePilotWorld();
        USSProgram program = USSProgram.of(Arrays.asList(work(), work()));
        USSBasePilot p = USSBasePilot.create(base("nomin-1", USSBaseAnchor.planet(2), program), program, w);
        int budget = 1000;
        while (workLogCount(w) < 2 && budget-- > 0) {
            p.tick();
        }
        assertTrue(workLogCount(w) >= 2, "the WORKs still loop as instant no-ops (" + workLogCount(w) + ")");
        assertEquals(0, p.miningLegId(), "no mining leg without mining power");
        assertEquals(0, p.miningTicks());
    }

    @Test
    public void testMiningLegSurvivesANbtRoundTrip() {
        FakePilotWorld w = new FakePilotWorld();
        USSProgram program = USSProgram.of(Arrays.asList(work()));
        VoidcraftActiveBase b = base("mine-rt", USSBaseAnchor.planet(2), program);
        b.payload()
            .setLong(VoidcraftNbt.TAG_MINING, 4L);
        USSBasePilot p = USSBasePilot.create(b, program, w);
        int ticks = 0;
        while (p.miningLegId() < 1 && ++ticks < 100) {
            p.tick();
        }
        assertEquals(1, p.miningLegId(), "leg 1 starts (150 ticks; the start tick does not count down)");
        p.tick();
        p.tick();
        p.tick(); // 3 countdown ticks: 147 left
        assertEquals(147, p.miningTicks());

        NBTTagCompound baseTag = b.writeToNBT();
        baseTag.setTag(USSBasePilot.TAG_PILOT, p.writeToNBT()); // the MTE pattern: pilot nested in the base NBT
        VoidcraftActiveBase b2 = VoidcraftActiveBase.readFromNBT(baseTag);
        assertNotNull(b2);
        USSBasePilot p2 = USSBasePilot.attach(b2, w, baseTag);
        assertEquals(147, p2.miningTicks(), "the mid-leg state survives the reload");
        assertEquals(1, p2.miningLegId());

        int after = 0;
        while (after < 400 && !logContains(w.baseLogs, "mining leg complete")) {
            p2.tick();
            after++;
        }
        assertTrue(logContains(w.baseLogs, "mining leg complete"), "the reloaded leg finishes");
        assertEquals(147, after, "the leg finishes after exactly its remaining ticks");
    }

    @Test
    public void testAbandonedMiningLegStopsWhenTheProgramEnds() {
        FakePilotWorld w = new FakePilotWorld();
        VoidcraftActiveBase b = base("aband-1", USSBaseAnchor.planet(2), null); // an empty program (already over)
        b.payload()
            .setLong(VoidcraftNbt.TAG_MINING, 4L);
        USSBasePilot p = USSBasePilot.create(b, null, w);
        assertTrue(p.isCompleted(), "an empty program is done");
        assertTrue(p.startLeg(BASE_POS, 0.0, USSWorkKind.MINE), "a mining leg can still be started");
        assertEquals(1, p.miningLegId());
        p.tick(); // the program-over path abandons the in-flight leg
        assertEquals(0, p.miningTicks(), "the abandoned mining leg is cleared (the beam stops)");
        assertEquals(0, p.miningTotal());
    }

    // endregion
}
