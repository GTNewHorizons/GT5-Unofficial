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
     * Helper: builds a 1×h×d grid from a list of components (index = y + h*z for x=0).
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
        VoidcraftBlueprint ship = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.ENGINE,
            VoidcraftComponent.UTILITY);
        assertEquals(1, ship.width);
        assertEquals(1, ship.height);
        assertEquals(3, ship.depth);
        assertEquals(3, ship.componentCount());

        VoidcraftStats stats = ship.computeStats();
        assertEquals(10L + 8L + 5L, stats.mass);
        assertEquals(100L, stats.thrust);
        assertEquals(1.0, stats.speed, 1e-9, "thrust/mass clamped to 1");
        assertEquals(5L, stats.energyDraw);
        assertEquals(10L + 10L, stats.integrity);
        // below the recoverable threshold
        assertFalse(stats.isRecoverable());
    }

    @Test
    public void testRecoverableByIntegrity() {
        // controller + engine + 2 utility = 30 integrity = threshold
        VoidcraftBlueprint ship = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.ENGINE,
            VoidcraftComponent.UTILITY,
            VoidcraftComponent.UTILITY);
        VoidcraftStats stats = ship.computeStats();
        assertEquals(VoidcraftConstants.RECOVERABLE_INTEGRITY_THRESHOLD, stats.integrity);
        assertTrue(stats.isRecoverable());

        // one fewer utility falls below
        VoidcraftBlueprint lighter = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.ENGINE,
            VoidcraftComponent.UTILITY);
        assertFalse(
            lighter.computeStats()
                .isRecoverable());
    }

    @Test
    public void testSpeedClamping() {
        assertEquals(0.0, VoidcraftStats.speedFor(0, 100), 1e-9);
        assertEquals(1.0, VoidcraftStats.speedFor(1000, 100), 1e-9);
        assertEquals(0.5, VoidcraftStats.speedFor(50, 100), 1e-9);
        assertEquals(0.0, VoidcraftStats.speedFor(50, 0), 1e-9, "zero mass never moves");
        assertEquals(0.0, VoidcraftStats.speedFor(-5, 10), 1e-9, "negative thrust clamped to 0");
    }

    @Test
    public void testRoleAndHybridPenalty() {
        // dedicated miner
        VoidcraftBlueprint miner = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.ENGINE,
            VoidcraftComponent.MINING_CENTRE);
        assertEquals(VoidcraftRole.MINER.getBit(), miner.computeRoles());
        assertEquals(1.0, miner.computeEfficiency(), 1e-9);

        // miner + explorer hybrid: two roles
        VoidcraftBlueprint hybrid = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.ENGINE,
            VoidcraftComponent.MINING_CENTRE,
            VoidcraftComponent.SPACETIME_SCANNER);
        int roles = hybrid.computeRoles();
        assertTrue(VoidcraftRole.MINER.isActive(roles));
        assertTrue(VoidcraftRole.EXPLORER.isActive(roles));
        assertFalse(VoidcraftRole.CONSTRUCTOR.isActive(roles));
        assertEquals(VoidcraftRole.efficiencyMultiplier(2), hybrid.computeEfficiency(), 1e-9);
        assertEquals(Math.pow(VoidcraftConstants.HYBRID_ROLE_PENALTY, 1), hybrid.computeEfficiency(), 1e-9);
    }

    @Test
    public void testPureTransportHasNoRole() {
        VoidcraftBlueprint hauler = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.ENGINE,
            VoidcraftComponent.CARGO_BAY);
        assertEquals(0, hauler.computeRoles());
        assertEquals(1.0, hauler.computeEfficiency(), 1e-9, "roleless ships are not penalized");
        assertEquals(50L, hauler.computeStats().cargoSlots);
    }

    @Test
    public void testValidationControllerCount() {
        List<String> errors = new ArrayList<>();
        // two controllers: invalid
        VoidcraftBlueprint bad = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.ENGINE,
            VoidcraftComponent.CONTROLLER);
        assertFalse(bad.validate(2, errors));
        assertTrue(errors.contains("voidcraft_controller_count"));
        assertFalse(errors.isEmpty());

        errors.clear();
        // no controller: invalid
        VoidcraftBlueprint bad2 = verticalShip(
            VoidcraftComponent.ENGINE,
            VoidcraftComponent.UTILITY,
            VoidcraftComponent.CARGO_BAY);
        assertFalse(bad2.validate(2, errors));
        assertTrue(errors.contains("voidcraft_controller_count"));
    }

    @Test
    public void testValidationRequiresEngine() {
        List<String> errors = new ArrayList<>();
        VoidcraftBlueprint engineless = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.UTILITY,
            VoidcraftComponent.CARGO_BAY);
        assertFalse(engineless.validate(2, errors));
        assertTrue(errors.contains("voidcraft_no_engine"));
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
        // starlifter is tier 2
        VoidcraftBlueprint starship = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.ENGINE,
            VoidcraftComponent.STARLIFTER_ARRAY);

        assertFalse(starship.validate(0, errors));
        assertTrue(errors.contains("voidcraft_tier_too_high"));
        assertFalse(starship.validate(1, errors));
        errors.clear();
        assertTrue(starship.validate(2, errors), "tier 2 circuit allows tier 2 components");
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testTrimBoundingVolume() {
        // 5x5x10 grid with a single component at the far corner
        int w = 5, h = 5, d = 10;
        byte[] grid = new byte[w * h * d];
        grid[4 + w * (4 + h * 9)] = gv(VoidcraftComponent.ENGINE); // x=4,y=4,z=9
        VoidcraftBlueprint big = VoidcraftBlueprint.of(w, h, d, grid);
        assertEquals(1, big.componentCount());
        assertEquals(w * h * d, grid.length);

        VoidcraftBlueprint trimmed = big.trim();
        assertEquals(1, trimmed.width);
        assertEquals(1, trimmed.height);
        assertEquals(1, trimmed.depth);
        assertEquals(1, trimmed.componentCount());
        assertEquals(gv(VoidcraftComponent.ENGINE), trimmed.grid[0]);
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
        VoidcraftBlueprint ship = verticalShip(
            VoidcraftComponent.CONTROLLER,
            VoidcraftComponent.ENGINE,
            VoidcraftComponent.UTILITY,
            VoidcraftComponent.CARGO_BAY);
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
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER), gv(VoidcraftComponent.ENGINE),
            gv(VoidcraftComponent.UTILITY) };
        byte[] covers = new byte[3 * 6];
        covers[1 * 6 + 3] = cv(VoidcraftCoverComponent.CARGO_POD); // cargo pod on the engine's south face
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, 3, grid, null, covers);
        assertEquals(4, ship.componentCount(), "blocks + covers all count as parts");
        assertEquals(1, ship.countCover(VoidcraftCoverComponent.CARGO_POD));

        VoidcraftStats stats = ship.computeStats();
        assertEquals(10L + 8L + 5L + 6L, stats.mass);
        assertEquals(20L, stats.cargoSlots, "only the cargo pod contributes cargo");
        assertEquals(100L, stats.thrust);
    }

    @Test
    public void testEngineDefaultFacingIsDown() {
        // No facing data → treated as facing DOWN (the MTE placement default):
        // exhaust leaves the bottom, so the ship is pushed +Y.
        byte[] grid = { gv(VoidcraftComponent.ENGINE) };
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, 1, grid);
        VoidcraftStats stats = ship.computeStats();
        assertEquals(0, stats.thrustX);
        assertEquals(100, stats.thrustY);
        assertEquals(0, stats.thrustZ);
        assertEquals(100, stats.thrust);
    }

    @Test
    public void testEngineFacingEastPushesWest() {
        // Facing EAST (ordinal 5, stored 6): exhaust +X, ship pushed -X.
        byte[] grid = { gv(VoidcraftComponent.ENGINE) };
        byte[] facing = { 6 };
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, 1, grid, facing, null);
        VoidcraftStats stats = ship.computeStats();
        assertEquals(-100, stats.thrustX);
        assertEquals(0, stats.thrustY);
        assertEquals(0, stats.thrustZ);
        assertEquals(100, stats.thrust, "scalar thrust is the best single axis");
    }

    @Test
    public void testThrusterCoverDirection() {
        // Thruster cover on the NORTH face (ordinal 2): exhaust -Z, ship pushed +Z.
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER) };
        byte[] covers = new byte[6];
        covers[2] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE);
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, 1, grid, null, covers);
        VoidcraftStats stats = ship.computeStats();
        assertEquals(0, stats.thrustX);
        assertEquals(0, stats.thrustY);
        assertEquals(40, stats.thrustZ);
        assertEquals(40, stats.thrust);
        assertEquals(40, ship.totalThrust());
    }

    @Test
    public void testOpposingEnginesCancel() {
        // Engine 1 facing DOWN (push +Y), engine 2 facing UP (push -Y) → net zero.
        byte[] grid = { gv(VoidcraftComponent.ENGINE), gv(VoidcraftComponent.ENGINE) };
        byte[] facing = { 1, 2 }; // ordinals 0 (down) and 1 (up)
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, 2, grid, facing, null);
        VoidcraftStats stats = ship.computeStats();
        assertEquals(0, stats.thrust);
        assertEquals(200, ship.totalThrust(), "magnitudes still exist — they just cancel");

        List<String> errors = new ArrayList<>();
        assertFalse(ship.validate(2, errors));
        assertTrue(errors.contains("voidcraft_thrusters_cancelled"));
    }

    @Test
    public void testThrusterCoverCountsAsEngine() {
        // No engine block, but a thruster cover → the ship still has an engine.
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER), gv(VoidcraftComponent.UTILITY),
            gv(VoidcraftComponent.CARGO_BAY) };
        byte[] covers = new byte[3 * 6];
        covers[1 * 6 + 1] = cv(VoidcraftCoverComponent.THRUSTER_NOZZLE); // utility's top face
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, 3, grid, null, covers);
        List<String> errors = new ArrayList<>();
        assertTrue(ship.validate(2, errors), "thruster cover satisfies the engine requirement" + errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testCoverTierGate() {
        // Scanner Dish is a tier-2 cover.
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER), gv(VoidcraftComponent.ENGINE),
            gv(VoidcraftComponent.UTILITY) };
        byte[] covers = new byte[3 * 6];
        covers[2] = cv(VoidcraftCoverComponent.SCANNER_DISH);
        VoidcraftBlueprint ship = VoidcraftBlueprint.of(1, 1, 3, grid, null, covers);
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
        grid[center] = gv(VoidcraftComponent.ENGINE);
        facing[center] = 6; // facing east
        covers[center * 6 + 4] = cv(VoidcraftCoverComponent.POWER_CELL); // west face

        VoidcraftBlueprint trimmed = VoidcraftBlueprint.of(w, h, d, grid, facing, covers)
            .trim();
        assertEquals(1, trimmed.width);
        assertEquals(1, trimmed.height);
        assertEquals(1, trimmed.depth);
        assertEquals(gv(VoidcraftComponent.ENGINE), trimmed.grid[0]);
        assertEquals(6, trimmed.facingGrid[0]);
        assertEquals(cv(VoidcraftCoverComponent.POWER_CELL), trimmed.coverGrid[4]);
        assertEquals(2, trimmed.componentCount(), "engine + power cell");
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
            VoidcraftCoverComponent.fromGridValue(9)
                .isEmpty());
        assertTrue(
            VoidcraftCoverComponent.fromGridValue(-1)
                .isEmpty());
    }

    @Test
    public void testNbtRoundTripWithCovers() {
        byte[] grid = { gv(VoidcraftComponent.CONTROLLER), gv(VoidcraftComponent.ENGINE),
            gv(VoidcraftComponent.UTILITY) };
        byte[] facing = { 1, 5, 6 }; // down, north, east
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
        assertEquals(stats.thrustX, VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUST_X));
        assertEquals(stats.thrustY, VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUST_Y));
        assertEquals(stats.thrustZ, VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUST_Z));
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
}
