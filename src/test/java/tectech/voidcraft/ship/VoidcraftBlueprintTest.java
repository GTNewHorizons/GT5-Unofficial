package tectech.voidcraft.ship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

public class VoidcraftBlueprintTest {

    private static byte gv(VoidcraftComponent component) {
        return (byte) component.toGridValue();
    }

    /**
     * Helper: builds a 1×h×d grid from a list of components (index = y + h*z for x=0), NO facing data (cells
     * default to facing DOWN — which, under the pass 18 rule, contributes no thrust).
     */
    private static VoidcraftBlueprint verticalShip(VoidcraftComponent... components) {
        int depth = components.length;
        byte[] grid = new byte[depth];
        for (int i = 0; i < components.length; i++) {
            grid[i] = gv(components[i]);
        }
        return VoidcraftBlueprint.of(1, 1, depth, grid);
    }

    @Test
    public void testMinimalHaulerStats() {
        // Pass 24 (corrected): the nose is the FAR end (grid +Z, away from the assembler) and the nozzle mounts on
        // the BACK (−Z, assembler side). The nozzle sits at the near end, so its exhaust (toward the assembler) is
        // clear of the hull — the hull on the +Z side never blocks it. Pass 23: controller + frame + nozzle cover.
        int depth = 8;
        byte[] grid = new byte[depth];
        grid[6] = gv(VoidcraftComponent.CONTROLLER);
        grid[7] = gv(VoidcraftComponent.FRAME);
        byte[] covers = new byte[depth * 6];
        covers[0 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, depth, grid, null, covers);
        assertEquals(1, ship.width);
        assertEquals(1, ship.height);
        assertEquals(8, ship.depth);
        assertEquals(3, ship.componentCount());

        VoidcraftStats stats = ship.computeStats();
        assertEquals(10L + 5L + 3L, stats.mass, "controller + frame + nozzle cover");
        assertEquals(120L, stats.thrust, "the back-mounted, unblocked nozzle cover counts its full magnitude");
        assertEquals(120.0 / 18.0, stats.speed, 1e-9, "pass 18: speed = thrust/mass, unclamped at 1");
        assertEquals(2L, stats.energyDraw, "nozzle draw");
        assertEquals(10L + 10L, stats.integrity, "controller + frame");
    }

    @Test
    public void testIntegritySumsComponents() {
        // Integrity is the ship's TIME BUDGET (the time-limit pass): the blueprint sums every part's integrity,
        // and the total is the number of seconds the ship survives in the USS.
        VoidcraftBlueprint ship = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.FRAME,
            VoidcraftComponent.FRAME);
        VoidcraftStats stats = ship.computeStats();
        assertEquals(10L + 10L + 10L, stats.integrity, "controller + 2 frames = 30 seconds of USS time");
    }

    @Test
    public void testSpeedIsThrustPerMass() {
        // Pass 18: speed = thrust/mass, clamped at 0 only — a strong engine load exceeds the old [0, 1] scale.
        assertEquals(0.0, VoidcraftStats.speedFor(0, 100), 1e-9);
        assertEquals(10.0, VoidcraftStats.speedFor(1000, 100), 1e-9, "no longer clamped to 1");
        assertEquals(0.5, VoidcraftStats.speedFor(50, 100), 1e-9);
        assertEquals(0.0, VoidcraftStats.speedFor(50, 0), 1e-9, "zero mass never moves");
        assertEquals(0.0, VoidcraftStats.speedFor(-5, 10), 1e-9, "negative thrust clamped to 0");
    }

    @Test
    public void testRoleAndHybridPenalty() {
        // Pass 23: roles come from the COVERS (mining array / scanner dish) on the controller + frame hull.
        // dedicated miner
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER), gv(VoidcraftComponent.FRAME) };
        byte[] covers = new byte[grid.length * 6];
        covers[0 * 6 + 2] = cv(VoidcraftCoverComponent.MINING_ARRAY);
        VoidcraftBlueprint miner = VoidcraftBlueprint.of(1, 1, 2, grid, null, covers);
        assertEquals(VoidcraftRole.MINER.getBit(), miner.computeRoles());
        assertEquals(1.0, miner.computeEfficiency(), 1e-9);

        // miner + explorer hybrid: two roles
        byte[] hybridCovers = new byte[grid.length * 6];
        hybridCovers[0 * 6 + 2] = cv(VoidcraftCoverComponent.MINING_ARRAY);
        hybridCovers[1 * 6 + 2] = cv(VoidcraftCoverComponent.SCANNER_DISH);
        VoidcraftBlueprint hybrid = VoidcraftBlueprint.of(1, 1, 2, grid, null, hybridCovers);
        int roles = hybrid.computeRoles();
        assertTrue(VoidcraftRole.MINER.isActive(roles));
        assertTrue(VoidcraftRole.EXPLORER.isActive(roles));
        assertFalse(VoidcraftRole.CONSTRUCTOR.isActive(roles));
        assertEquals(VoidcraftRole.efficiencyMultiplier(2), hybrid.computeEfficiency(), 1e-9);
        assertEquals(Math.pow(VoidcraftConstants.HYBRID_ROLE_PENALTY, 1), hybrid.computeEfficiency(), 1e-9);
    }

    @Test
    public void testPureTransportHasNoRole() {
        // Pass 23: cargo now ships as the CARGO_POD cover.
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER), gv(VoidcraftComponent.FRAME) };
        byte[] covers = new byte[grid.length * 6];
        covers[1 * 6 + 2] = cv(VoidcraftCoverComponent.CARGO_POD);
        VoidcraftBlueprint hauler = VoidcraftBlueprint.of(1, 1, 2, grid, null, covers);
        assertEquals(0, hauler.computeRoles());
        assertEquals(1.0, hauler.computeEfficiency(), 1e-9, "roleless ships are not penalized");
        assertEquals(200L, hauler.computeStats().cargoSlots, "one cargo pod = 200 slots");
    }

    @Test
    public void testValidationControllerCount() {
        List<String> errors = new ArrayList<>();
        // two controllers: invalid
        VoidcraftBlueprint bad = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.FRAME,
            VoidcraftComponent.CONTROLLER);
        assertFalse(bad.validate(2, errors));
        assertTrue(errors.contains("voidcraft_controller_count"));
        assertFalse(errors.isEmpty());

        errors.clear();
        // no controller: invalid (frame-only hull)
        VoidcraftBlueprint bad2 = verticalShip(
            VoidcraftComponent.FRAME,
            VoidcraftComponent.FRAME,
            VoidcraftComponent.FRAME);
        assertFalse(bad2.validate(2, errors));
        assertTrue(errors.contains("voidcraft_controller_count"));
    }

    @Test
    public void testValidationRequiresEngine() {
        // Pass 23: a valid hull (controller + frame) with NO thruster cover → voidcraft_no_engine.
        List<String> errors = new ArrayList<>();
        VoidcraftBlueprint engineless = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.FRAME,
            VoidcraftComponent.FRAME);
        assertFalse(engineless.validate(2, errors));
        assertTrue(errors.contains("voidcraft_no_engine"));
    }

    @Test
    public void testValidationRequiresFrame() {
        // Pass 23: a ship with NO frame hull block → voidcraft_no_frame.
        List<String> errors = new ArrayList<>();
        int depth = 8;
        byte[] grid = new byte[depth];
        grid[7] = gv(VoidcraftComponent.CONTROLLER);
        byte[] covers = new byte[depth * 6];
        covers[0 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint frameless = VoidcraftBlueprint.of(1, 1, depth, grid, null, covers);
        assertFalse(frameless.validate(2, errors));
        assertTrue(errors.contains("voidcraft_no_frame"), "no frame hull block" + errors);
    }

    @Test
    public void testValidationRejectsCoverOnlyBlocks() {
        // Pass 23: the cover-only functions (engine, cargo bay, ...) can no longer be full blocks — old builds
        // holding them are rejected with a reason (no backwards compatibility, standing directive).
        List<String> errors = new ArrayList<>();
        int depth = 8;
        byte[] grid = new byte[depth];
        grid[6] = gv(VoidcraftComponent.CONTROLLER);
        grid[7] = gv(VoidcraftComponent.FRAME);
        grid[5] = gv(VoidcraftComponent.ENGINE); // an old-world engine block
        byte[] covers = new byte[depth * 6];
        covers[0 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint legacy = VoidcraftBlueprint.of(1, 1, depth, grid, null, covers);
        assertFalse(legacy.validate(2, errors));
        assertTrue(
            errors.contains("voidcraft_cover_only_component"),
            "cover-only part as a full block is the reported reason" + errors);
    }

    @Test
    public void testValidationSize() {
        List<String> errors = new ArrayList<>();
        // single component: below minimum
        VoidcraftBlueprint tiny = verticalShip(VoidcraftComponent.CONTROLLER);
        assertFalse(tiny.validate(2, errors));
        assertTrue(errors.contains("voidcraft_too_small"));

        // empty ship
        byte[] empty = new byte[1];
        VoidcraftBlueprint nothing = VoidcraftBlueprint.of(1, 1, 1, empty);
        errors.clear();
        assertFalse(nothing.validate(2, errors));
    }

    @Test
    public void testValidationTierGate() {
        List<String> errors = new ArrayList<>();
        // Pass 23: the STAR_SIPHON cover is tier 2; pass 24 (corrected): nozzle cover at the near end — its
        // exhaust (toward the assembler) is clear, so it thrusts
        int depth = 8;
        byte[] grid = new byte[depth];
        grid[6] = gv(VoidcraftComponent.CONTROLLER);
        grid[7] = gv(VoidcraftComponent.FRAME);
        byte[] covers = new byte[depth * 6];
        covers[7 * 6 + 1] = cv(VoidcraftCoverComponent.STAR_SIPHON);
        covers[0 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint starship = VoidcraftBlueprint.of(1, 1, depth, grid, null, covers);

        assertFalse(starship.validate(0, errors));
        assertTrue(errors.contains("voidcraft_tier_too_high"));
        assertFalse(starship.validate(1, errors));
        errors.clear();
        assertTrue(starship.validate(2, errors), "tier 2 circuit allows tier 2 covers");
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testTrimBoundingVolume() {
        // 5x5x10 grid with a single frame block at the far corner (trim is independent of validation)
        int w = 5, h = 5, d = 10;
        byte[] grid = new byte[w * h * d];
        grid[4 + w * (4 + h * 9)] = gv(VoidcraftComponent.FRAME); // x=4,y=4,z=9
        VoidcraftBlueprint big = VoidcraftBlueprint.of(w, h, d, grid);
        assertEquals(1, big.componentCount());
        assertEquals(w * h * d, grid.length);

        VoidcraftBlueprint trimmed = big.trim();
        assertEquals(1, trimmed.width);
        assertEquals(1, trimmed.height);
        assertEquals(1, trimmed.depth);
        assertEquals(1, trimmed.componentCount());
        assertEquals(gv(VoidcraftComponent.FRAME), trimmed.grid[0]);
    }

    @Test
    public void testConstructorValidation() {
        assertThrows(IllegalArgumentException.class, () -> VoidcraftBlueprint.of(0, 1, 1, new byte[1]));
        assertThrows(IllegalArgumentException.class, () -> VoidcraftBlueprint.of(6, 1, 1, new byte[6]));
        assertThrows(IllegalArgumentException.class, () -> VoidcraftBlueprint.of(1, 1, 11, new byte[11]));
        assertThrows(IllegalArgumentException.class, () -> VoidcraftBlueprint.of(1, 1, 2, new byte[1]));
        // unknown grid value (200 is above the highest component grid value)
        byte[] bad = { 0, (byte) 200 };
        assertThrows(IllegalArgumentException.class, () -> VoidcraftBlueprint.of(1, 1, 2, bad));
    }

    @Test
    public void testNbtRoundTrip() {
        // Pass 23: controller + frame hull; pass 24 (corrected): nozzle cover at the near end (exhaust toward the
        // assembler is clear) → real thrust/speed to round-trip through the denormalized NBT fields.
        int depth = 8;
        byte[] grid = new byte[depth];
        grid[6] = gv(VoidcraftComponent.CONTROLLER);
        grid[7] = gv(VoidcraftComponent.FRAME);
        byte[] covers = new byte[depth * 6];
        covers[0 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, depth, grid, null, covers);
        NBTTagCompound nbt = new NBTTagCompound();
        VoidcraftNbt.write(nbt, ship, "test-uuid", "Test Ship", 12345L);

        VoidcraftBlueprint read = VoidcraftNbt.read(nbt);
        assertNotNull(read);
        assertEquals(ship.width, read.width);
        assertEquals(ship.height, read.height);
        assertEquals(ship.depth, read.depth);
        for (int i = 0; i < ship.grid.length; i++) {
            assertEquals(ship.grid[i], read.grid[i]);
        }

        // denormalized stats must match
        VoidcraftStats stats = ship.computeStats();
        assertEquals(stats.mass, VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_MASS));
        assertEquals(stats.thrust, VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUST));
        assertEquals(stats.speed, VoidcraftNbt.readDouble(nbt, VoidcraftNbt.TAG_SPEED), 1e-9);
        assertEquals(ship.computeRoles(), VoidcraftNbt.readInt(nbt, VoidcraftNbt.TAG_ROLES));
        assertEquals(ship.computeEfficiency(), VoidcraftNbt.readDouble(nbt, VoidcraftNbt.TAG_EFFICIENCY), 1e-9);
    }

    private static byte cv(VoidcraftCoverComponent cover) {
        return (byte) cover.toGridValue();
    }

    @Test
    public void testCoverStatsContribute() {
        // Pass 24 (corrected): nozzle cover at the near end (exhaust toward the assembler is clear); cargo pod on
        // a hull face. Pass 23: covers carry ALL the function stats.
        int depth = 8;
        byte[] grid = new byte[depth];
        grid[6] = gv(VoidcraftComponent.CONTROLLER);
        grid[7] = gv(VoidcraftComponent.FRAME);
        byte[] covers = new byte[depth * 6];
        covers[0 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        covers[7 * 6 + 1] = cv(VoidcraftCoverComponent.CARGO_POD); // cargo pod on the frame's top face
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, depth, grid, null, covers);
        assertEquals(4, ship.componentCount(), "blocks + covers all count as parts");
        assertEquals(1, ship.countCover(VoidcraftCoverComponent.CARGO_POD));

        VoidcraftStats stats = ship.computeStats();
        assertEquals(10L + 5L + 3L + 6L, stats.mass, "controller + frame + nozzle + pod");
        assertEquals(200L, stats.cargoSlots, "only the cargo pod contributes cargo");
        assertEquals(120L, stats.thrust, "the back-mounted nozzle cover counts");
    }

    @Test
    public void testThrusterCoverOnBackFaceCounts() {
        // Pass 18/23/24 (corrected): the nozzle cover on the BACK face (side 2 = NORTH, −Z = the assembler side,
        // BACK_FACE) counts; on the +Z face (side 3 = SOUTH) it is dead weight. (The old engine-block facing tests
        // are gone: pass 23 thrust is covers-only.) The nozzle is on the near end, so its exhaust (toward the
        // assembler) is clear of the hull.
        int depth = 7;
        byte[] grid = new byte[depth];
        grid[6] = gv(VoidcraftComponent.CONTROLLER);
        byte[] coversBack = new byte[depth * 6];
        coversBack[0 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint shipBack = VoidcraftBlueprint.of(1, 1, depth, grid, null, coversBack);
        assertEquals(120, shipBack.computeStats().thrust);
        assertEquals(120, shipBack.totalThrust());

        byte[] coversFront = new byte[depth * 6];
        coversFront[0 * 6 + 3] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE); // side 3 = SOUTH (+Z) — the nose
        VoidcraftBlueprint shipFront = VoidcraftBlueprint.of(1, 1, depth, grid, null, coversFront);
        assertEquals(0, shipFront.computeStats().thrust);
        assertEquals(0, shipFront.totalThrust());
    }

    @Test
    public void testThrusterFacingWrongWayFailsValidation() {
        // Pass 19/23: a nozzle cover mounted on a NON-BACK face BREAKS the digitization — reported with a reason.
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER), gv(VoidcraftComponent.FRAME) };
        byte[] covers = new byte[grid.length * 6];
        covers[1 * 6 + 1] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE); // side 1 = UP — not the back
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, 2, grid, null, covers);
        assertEquals(0, ship.computeStats().thrust);

        List<String> errors = new ArrayList<>();
        assertFalse(ship.validate(2, errors));
        assertTrue(
            errors.contains("voidcraft_thruster_wrong_facing"),
            "wrong-facing thruster is the reported reason" + errors);
    }

    @Test
    public void testThrusterCoverOnBackCountsAsEngine() {
        // Pass 23/24 (corrected): a thruster cover ON THE BACK FACE (−Z, assembler side) of the near cell → thrust
        // — the nozzle cover is the only engine (pass 23 removed the engine block). The nozzle is on the near end,
        // so its exhaust (toward the assembler) is clear of the hull on the +Z side.
        int depth = 8; // z0: the nozzle cell, z6/z7: the hull; the exhaust side (−Z) is open
        byte[] grid = new byte[depth];
        grid[6] = gv(VoidcraftComponent.CONTROLLER);
        grid[7] = gv(VoidcraftComponent.FRAME);
        byte[] covers = new byte[depth * 6];
        covers[0 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, depth, grid, null, covers);
        assertEquals(3, ship.componentCount(), "blocks + covers all count as parts");
        assertEquals(120, ship.computeStats().thrust);
        List<String> errors = new ArrayList<>();
        assertTrue(ship.validate(2, errors), "back-facing thruster cover satisfies the thrust requirement" + errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testNozzleBlockedByBlockBehind() {
        // Pass 24 (corrected): the exhaust comes out the BACK face (−Z, the assembler side). A Voidcraft block on
        // the nozzle's EXHAUST side (−Z) blocks it → "Engine blocked". Here the controller sits at z0, the nozzle
        // (on a frame) at z1 — so the controller is on the nozzle's exhaust side and blocks it.
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER), gv(VoidcraftComponent.FRAME), gv(VoidcraftComponent.FRAME) };
        byte[] covers = new byte[3 * 6];
        covers[1 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, 3, grid, null, covers);
        assertEquals(0, ship.computeStats().thrust, "blocked nozzle contributes nothing");

        List<String> errors = new ArrayList<>();
        assertFalse(ship.validate(2, errors));
        assertTrue(errors.contains("voidcraft_engine_blocked"), "blocked nozzle is the reported reason" + errors);
    }

    @Test
    public void testExhaustClearanceWindowIsFive() {
        // Pass 24 (corrected): the exhaust comes out the BACK face (−Z, the assembler side); the 5 cells on that
        // exhaust side must be free of Voidcraft blocks. A nozzle on the NEAR END (z=0) is always clear — its
        // exhaust points at the assembler, outside the hull — so it thrusts even with the hull directly adjacent
        // (the user's working layout: [assembler] → [nozzle frame] → [hull]). A nozzle deeper in the ship is
        // blocked when the hull sits on its exhaust side within 5 cells.
        //
        // clear: nozzle on the near end (z=0), hull directly adjacent on the +Z side.
        byte[] gridClear = { gv(VoidcraftComponent.FRAME), gv(VoidcraftComponent.CONTROLLER) };
        byte[] coversClear = new byte[2 * 6];
        coversClear[0 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint clear = VoidcraftBlueprint.of(1, 1, 2, gridClear, null, coversClear);
        assertEquals(120, clear.computeStats().thrust, "near-end nozzle with adjacent hull is clear");

        // blocked: nozzle deeper in the ship (z=2), hull on its exhaust side (z=1, z=0) within 5 cells.
        byte[] gridBlocked = { gv(VoidcraftComponent.FRAME), gv(VoidcraftComponent.FRAME),
            gv(VoidcraftComponent.CONTROLLER) };
        byte[] coversBlocked = new byte[3 * 6];
        coversBlocked[2 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint blocked = VoidcraftBlueprint.of(1, 1, 3, gridBlocked, null, coversBlocked);
        assertEquals(0, blocked.computeStats().thrust, "hull on the exhaust side blocks the thruster");
    }

    @Test
    public void testCoverTierGate() {
        // Scanner Dish is a tier-2 cover. Nozzle cover at the near end (exhaust toward the assembler is clear,
        // pass 24 corrected).
        int depth = 8;
        byte[] grid = new byte[depth];
        grid[6] = gv(VoidcraftComponent.CONTROLLER);
        grid[7] = gv(VoidcraftComponent.FRAME);
        byte[] covers = new byte[depth * 6];
        covers[6 * 6 + 1] = cv(VoidcraftCoverComponent.SCANNER_DISH); // tier 2
        covers[0 * 6 + VoidcraftBlueprint.BACK_FACE] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE); // thrust valid
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, depth, grid, null, covers);
        assertEquals(2, ship.maxTier());

        List<String> errors = new ArrayList<>();
        assertFalse(ship.validate(1, errors));
        assertTrue(errors.contains("voidcraft_tier_too_high"));
        errors.clear();
        assertTrue(ship.validate(2, errors));
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testTrimCarriesCoversAndFacing() {
        int w = 3, h = 3, d = 3;
        byte[] grid = new byte[w * h * d];
        byte[] facing = new byte[w * h * d];
        byte[] covers = new byte[w * h * d * 6];
        int center = 1 + w * (1 + h * 1); // x=1,y=1,z=1
        grid[center] = gv(VoidcraftComponent.FRAME);
        facing[center] = 6; // facing east
        covers[center * 6 + 4] = cv(VoidcraftCoverComponent.POWER_CELL); // west face

        VoidcraftBlueprint trimmed = VoidcraftBlueprint.of(w, h, d, grid, facing, covers)
            .trim();
        assertEquals(1, trimmed.width);
        assertEquals(1, trimmed.height);
        assertEquals(1, trimmed.depth);
        assertEquals(gv(VoidcraftComponent.FRAME), trimmed.grid[0]);
        assertEquals(6, trimmed.facingGrid[0]);
        assertEquals(cv(VoidcraftCoverComponent.POWER_CELL), trimmed.coverGrid[4]);
        assertEquals(2, trimmed.componentCount(), "frame + power cell");
    }

    @Test
    public void testGridValueRoundTrips() {
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            assertEquals(
                cover,
                VoidcraftCoverComponent.fromGridValue(cover.toGridValue())
                    .orElse(null));
        }
        assertTrue(
            VoidcraftCoverComponent.fromGridValue(0)
                .isEmpty());
        assertTrue(
            VoidcraftCoverComponent.fromGridValue(11)
                .isEmpty());
        assertTrue(
            VoidcraftCoverComponent.fromGridValue(-1)
                .isEmpty());
    }

    @Test
    public void testNbtRoundTripWithCovers() {
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER), gv(VoidcraftComponent.FRAME), gv(VoidcraftComponent.FRAME) };
        byte[] facing = { 1, (byte) (VoidcraftBlueprint.BACK_FACE + 1), 6 }; // down, back, east
        byte[] covers = new byte[18];
        covers[1 * 6 + 3] = cv(VoidcraftCoverComponent.CARGO_POD);
        covers[2 * 6 + 0] = cv(VoidcraftCoverComponent.ARMOR_PLATE);
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, 3, grid, facing, covers);

        NBTTagCompound nbt = new NBTTagCompound();
        VoidcraftNbt.write(nbt, ship, "test-uuid", "Cover Ship", 12345L);

        VoidcraftBlueprint read = VoidcraftNbt.read(nbt);
        assertNotNull(read);
        assertEquals(ship, read);
        // denormalized stats must match
        VoidcraftStats stats = ship.computeStats();
        assertEquals(stats.thrust, VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUST));
        assertEquals(stats.speed, VoidcraftNbt.readDouble(nbt, VoidcraftNbt.TAG_SPEED), 1e-9);
    }

    @Test
    public void testNbtRejectsCorruptPayload() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(VoidcraftNbt.TAG_FORMAT, VoidcraftConstants.NBT_FORMAT_VERSION);
        nbt.setInteger(VoidcraftNbt.TAG_WIDTH, 1);
        nbt.setInteger(VoidcraftNbt.TAG_HEIGHT, 1);
        nbt.setInteger(VoidcraftNbt.TAG_DEPTH, 2);
        nbt.setByteArray(VoidcraftNbt.TAG_GRID, new byte[] { 1, 1, 1 }); // length mismatch
        assertEquals(null, VoidcraftNbt.read(nbt));

        NBTTagCompound versioned = new NBTTagCompound();
        versioned.setInteger(VoidcraftNbt.TAG_FORMAT, VoidcraftConstants.NBT_FORMAT_VERSION + 1);
        assertEquals(null, VoidcraftNbt.read(versioned));

        NBTTagCompound empty = new NBTTagCompound();
        assertEquals(null, VoidcraftNbt.read(empty));
    }

    // region Voidbase (the 15x15x15 base volume + base validation + the parts list)

    @Test
    public void testBaseBlueprintFifteenCubes() {
        // ofBase accepts up to 15 in every direction (the 15x15x15 base volume)
        int d = 15;
        byte[] grid = new byte[d * d * d];
        grid[0] = gv(VoidcraftComponent.CONTROLLER);
        grid[1] = gv(VoidcraftComponent.FRAME);
        VoidcraftBlueprint base = VoidcraftBlueprint.ofBase(15, 15, 15, grid);
        assertEquals(15, base.width);
        assertEquals(15, base.height);
        assertEquals(15, base.depth);
        // The ship factory still rejects 15 (ships stay 5x5x10)
        assertThrows(IllegalArgumentException.class, () -> VoidcraftBlueprint.of(15, 1, 1, new byte[15]));
        // ofBase rejects 16
        assertThrows(IllegalArgumentException.class, () -> VoidcraftBlueprint.ofBase(16, 1, 1, new byte[16]));
    }

    @Test
    public void testBaseValidationWithoutThrusters() {
        // A build with no thruster at all: invalid as a ship (voidcraft_no_engine), valid as a base
        int depth = 4;
        byte[] grid = new byte[depth];
        grid[0] = gv(VoidcraftComponent.CONTROLLER);
        grid[1] = gv(VoidcraftComponent.FRAME);
        grid[2] = gv(VoidcraftComponent.FRAME);
        grid[3] = gv(VoidcraftComponent.FRAME);
        byte[] covers = new byte[depth * 6];
        covers[1 * 6 + 0] = cv(VoidcraftCoverComponent.POWER_CELL);
        VoidcraftBlueprint base = VoidcraftBlueprint.ofBase(1, 1, depth, grid, null, covers);
        List<String> shipErrors = new ArrayList<>();
        assertFalse(base.validate(2, shipErrors), "a ship without a thruster is invalid");
        assertTrue(shipErrors.contains("voidcraft_no_engine"));
        List<String> baseErrors = new ArrayList<>();
        assertTrue(base.validateForBase(2, baseErrors), "the same build is a valid base (no thruster rule)");
        assertTrue(baseErrors.isEmpty());
    }

    @Test
    public void testBaseValidationStillEnforcesStructure() {
        // Base validation keeps the ship's structural rules: two controllers, no frame
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER), gv(VoidcraftComponent.CONTROLLER) };
        VoidcraftBlueprint bad = VoidcraftBlueprint.ofBase(2, 1, 1, grid);
        List<String> errors = new ArrayList<>();
        assertFalse(bad.validateForBase(2, errors));
        assertTrue(errors.contains("voidcraft_controller_count"));
        assertTrue(errors.contains("voidcraft_no_frame"));
        // ... and the tier rule
        byte[] tierGrid = new byte[4];
        tierGrid[0] = gv(VoidcraftComponent.CONTROLLER);
        tierGrid[1] = gv(VoidcraftComponent.FRAME);
        byte[] tierCovers = new byte[4 * 6];
        tierCovers[1 * 6 + 0] = cv(VoidcraftCoverComponent.STAR_SIPHON);
        VoidcraftBlueprint tiered = VoidcraftBlueprint.ofBase(1, 1, 4, tierGrid, null, tierCovers);
        List<String> tierErrors = new ArrayList<>();
        assertFalse(tiered.validateForBase(1, tierErrors), "a tier-2 part fails a tier-1 circuit");
        assertTrue(tierErrors.contains("voidcraft_tier_too_high"));
    }

    @Test
    public void testBasePartsList() {
        int depth = 4;
        byte[] grid = new byte[depth];
        grid[0] = gv(VoidcraftComponent.CONTROLLER);
        grid[1] = gv(VoidcraftComponent.FRAME);
        grid[2] = gv(VoidcraftComponent.FRAME);
        grid[3] = gv(VoidcraftComponent.FRAME);
        byte[] covers = new byte[depth * 6];
        covers[1 * 6 + 0] = cv(VoidcraftCoverComponent.POWER_CELL);
        covers[1 * 6 + 1] = cv(VoidcraftCoverComponent.POWER_CELL);
        covers[2 * 6 + 0] = cv(VoidcraftCoverComponent.SOLAR_PANEL);
        VoidcraftBlueprint base = VoidcraftBlueprint.ofBase(1, 1, depth, grid, null, covers);
        java.util.Map<String, Long> parts = base.partsList();
        assertEquals(1L, parts.get("block.CONTROLLER"));
        assertEquals(3L, parts.get("block.FRAME"));
        assertEquals(2L, parts.get("cover.POWER_CELL"));
        assertEquals(1L, parts.get("cover.SOLAR_PANEL"));
        assertEquals(4, parts.size());
        // Stable order: blocks in component meta order, then covers in cover id order
        List<String> keys = new ArrayList<>(parts.keySet());
        assertEquals("block.CONTROLLER", keys.get(0));
        assertEquals("block.FRAME", keys.get(1));
        assertEquals("cover.POWER_CELL", keys.get(2));
        assertEquals("cover.SOLAR_PANEL", keys.get(3));
    }

    @Test
    public void testEnergyGenSumsIntoStats() {
        int depth = 4;
        byte[] grid = new byte[depth];
        grid[0] = gv(VoidcraftComponent.CONTROLLER);
        grid[1] = gv(VoidcraftComponent.FRAME);
        grid[2] = gv(VoidcraftComponent.FRAME);
        grid[3] = gv(VoidcraftComponent.FRAME);
        byte[] covers = new byte[depth * 6];
        covers[1 * 6 + 0] = cv(VoidcraftCoverComponent.SOLAR_PANEL);
        covers[2 * 6 + 0] = cv(VoidcraftCoverComponent.SOLAR_PANEL);
        covers[2 * 6 + 1] = cv(VoidcraftCoverComponent.REPAIR_BAY);
        VoidcraftBlueprint base = VoidcraftBlueprint.ofBase(1, 1, depth, grid, null, covers);
        VoidcraftStats stats = base.computeStats();
        assertEquals(4000L, stats.energyGen, "two solar panels at 2000 EU/t each");
        assertEquals(2000L, stats.energyDraw, "the repair bay draw");
    }

    // region toGridSide (world-facing cover -> grid-side cover; the assemblers delegate here)

    /** All six assembler front orientations (unit axes). */
    private static final int[][] FRONTS = { { 0, -1, 0 }, // DOWN
        { 0, 1, 0 }, // UP
        { 0, 0, -1 }, // NORTH
        { 0, 0, 1 }, // SOUTH
        { -1, 0, 0 }, // WEST
        { 1, 0, 0 }, // EAST
    };

    /** The ForgeDirection world-side ordinal of a unit axis (0 DOWN, 1 UP, 2 NORTH, 3 SOUTH, 4 WEST, 5 EAST). */
    private static int ordinalOf(int x, int y, int z) {
        if (y < 0) {
            return 0;
        }
        if (y > 0) {
            return 1;
        }
        if (z < 0) {
            return 2;
        }
        if (z > 0) {
            return 3;
        }
        if (x < 0) {
            return 4;
        }
        return 5;
    }

    /** The world-side ordinal opposite the given one (0<->1, 2<->3, 4<->5). */
    private static int opposite(int side) {
        return side ^ 1;
    }

    @Test
    public void testToGridSideIsABijectionForEveryAssemblerOrientation() {
        // The six world directions must map to six DISTINCT grid sides, no matter which way the assembler
        // faces (a direction landing on an already-used side would silently lose a cover face).
        for (int[] front : FRONTS) {
            boolean[] seen = new boolean[6];
            for (int worldSide = 0; worldSide < 6; worldSide++) {
                int side = VoidcraftBlueprint.toGridSide(front[0], front[1], front[2], worldSide);
                assertTrue(side >= 0 && side < 6, "in-range grid side");
                assertFalse(
                    seen[side],
                    "front " + java.util.Arrays
                        .toString(front) + " maps world side " + worldSide + " to grid side " + side + " twice");
                seen[side] = true;
            }
        }
    }

    @Test
    public void testToGridSideTracksTheAssemblerFacing() {
        // A cover facing AWAY from the assembler is the grid far side (+Z, 3); one facing TOWARD it is the
        // grid back (2) - for every assembler orientation.
        for (int[] front : FRONTS) {
            int away = ordinalOf(front[0], front[1], front[2]);
            assertEquals(
                3,
                VoidcraftBlueprint.toGridSide(front[0], front[1], front[2], away),
                "away cover -> grid +Z (front " + java.util.Arrays.toString(front) + ")");
            assertEquals(
                2,
                VoidcraftBlueprint.toGridSide(front[0], front[1], front[2], opposite(away)),
                "toward cover -> grid -Z (front " + java.util.Arrays.toString(front) + ")");
        }
    }

    @Test
    public void testToGridSideHorizontalFront() {
        // A SOUTH-facing assembler: grid +Y = world UP, grid +X = world EAST, grid +Z = world SOUTH.
        assertEquals(0, VoidcraftBlueprint.toGridSide(0, 0, 1, 0), "DOWN cover -> grid -Y");
        assertEquals(1, VoidcraftBlueprint.toGridSide(0, 0, 1, 1), "UP cover -> grid +Y");
        assertEquals(2, VoidcraftBlueprint.toGridSide(0, 0, 1, 2), "NORTH cover -> grid -Z (toward assembler)");
        assertEquals(3, VoidcraftBlueprint.toGridSide(0, 0, 1, 3), "SOUTH cover -> grid +Z");
        assertEquals(4, VoidcraftBlueprint.toGridSide(0, 0, 1, 4), "WEST cover -> grid -X");
        assertEquals(5, VoidcraftBlueprint.toGridSide(0, 0, 1, 5), "EAST cover -> grid +X");
    }

    @Test
    public void testToGridSideVerticalFront() {
        // An UP-facing assembler: grid +Z = world UP, grid +Y = world SOUTH, grid +X = world EAST.
        assertEquals(2, VoidcraftBlueprint.toGridSide(0, 1, 0, 0), "DOWN cover -> grid -Z (toward assembler)");
        assertEquals(3, VoidcraftBlueprint.toGridSide(0, 1, 0, 1), "UP cover -> grid +Z");
        assertEquals(0, VoidcraftBlueprint.toGridSide(0, 1, 0, 2), "NORTH cover -> grid -Y");
        assertEquals(1, VoidcraftBlueprint.toGridSide(0, 1, 0, 3), "SOUTH cover -> grid +Y");
        assertEquals(4, VoidcraftBlueprint.toGridSide(0, 1, 0, 4), "WEST cover -> grid -X");
        assertEquals(5, VoidcraftBlueprint.toGridSide(0, 1, 0, 5), "EAST cover -> grid +X");
    }

    // endregion
}
