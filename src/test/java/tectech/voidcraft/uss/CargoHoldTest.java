package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * Bare-JVM tests for the Voidcraft cargo-capacity framework ({@link CargoHold}) — the bounded internal cargo a
 * Voidcraft fills while mining / starlifting / constructing, and the generic modify framework (add / remove /
 * transfer) for future ship-to-ship transfer.
 *
 * <p>
 * Capacity model (user spec): {@code 1 cargo unit = 1 item = 100 mB of fluid}. Items consume their count in units;
 * fluids consume {@code mB / 100} units. Adding is clamped by the remaining capacity ("they cannot mine if it is
 * full").
 *
 * <p>
 * The item resolution (for {@link USSShipCargo#fillHold}) goes through the GT ore-dictionary unification map, which
 * is empty in a bare JVM — so the test pre-populates it with one recognizable dust per material (like
 * {@link USSShipCargoTest}).
 */
public class CargoHoldTest {

    private static int nextTestId = 30101;
    private static final java.util.Map<Materials, Integer> DAMAGE_BY_MATERIAL = new java.util.LinkedHashMap<>();

    @BeforeEach
    public void setUp() {
        USSPlanetRegistry.clear();
        USSStarRegistry.clear();
        USSPlanetCatalog.resetForTests();
        USSStarCatalog.resetForTests();
        USSPlanetCatalog.registerAll();
        USSStarCatalog.registerAll();
    }

    private static void registerDust(Materials material) {
        if (material == null || material == Materials._NULL || DAMAGE_BY_MATERIAL.containsKey(material)) {
            return;
        }
        int damage = DAMAGE_BY_MATERIAL.size() + 1;
        net.minecraft.item.Item item = BareJvmItemRegistry.register(new net.minecraft.item.Item(), nextTestId++);
        GTOreDictUnificator.getName2StackMap()
            .put(
                OrePrefixes.dust.get(material)
                    .toString(),
                new net.minecraft.item.ItemStack(item, 1, damage));
        DAMAGE_BY_MATERIAL.put(material, damage);
    }

    // region capacity model

    @Test
    public void testEmptyHold() {
        CargoHold hold = CargoHold.of(100L);
        assertEquals(100L, hold.getCapacity(), "capacity preserved");
        assertTrue(hold.isEmpty(), "a fresh hold is empty");
        assertFalse(hold.isFull(), "a fresh hold is not full");
        assertEquals(0L, hold.usedUnits(), "no used units");
        assertEquals(100L, hold.remainingUnits(), "full remaining capacity");
    }

    @Test
    public void testAddItemsClampedByCapacity() {
        CargoHold hold = CargoHold.of(100L);
        // Add 60 items (1 item = 1 unit) — fits.
        CargoHold after = hold.addItems(Materials.Copper, 60L);
        assertEquals(60L, after.itemsOf(Materials.Copper), "60 items on board");
        assertEquals(60L, after.usedUnits(), "60 units used (1 item = 1 unit)");
        assertEquals(40L, after.remainingUnits(), "40 units remaining");

        // Add another 50 items — clamped to the remaining 40.
        CargoHold clamped = after.addItems(Materials.Iron, 50L);
        assertEquals(40L, clamped.itemsOf(Materials.Iron), "clamped to the remaining capacity");
        assertEquals(100L, clamped.usedUnits(), "at capacity");
        assertTrue(clamped.isFull(), "full after clamping");

        // A full hold accepts nothing more.
        CargoHold full = clamped.addItems(Materials.Tin, 10L);
        assertEquals(0L, full.itemsOf(Materials.Tin), "a full hold accepts 0 more items");
        assertEquals(100L, full.usedUnits(), "still at capacity");
    }

    @Test
    public void testAddFluidsClampedByCapacity() {
        CargoHold hold = CargoHold.of(100L);
        // Add 5000 mB of fluid (100 mB = 1 unit → 50 units) — fits.
        CargoHold after = hold.addFluids(Materials.Water, 5000L);
        assertEquals(5000L, after.fluidsOf(Materials.Water), "5000 mB on board");
        assertEquals(50L, after.usedUnits(), "50 units used (5000 mB / 100)");
        assertEquals(50L, after.remainingUnits(), "50 units remaining");

        // Add 10000 mB more — clamped to the remaining 50 units (5000 mB).
        CargoHold clamped = after.addFluids(Materials.Lava, 10000L);
        assertEquals(5000L, clamped.fluidsOf(Materials.Lava), "clamped to the remaining capacity (5000 mB)");
        assertEquals(100L, clamped.usedUnits(), "at capacity");
        assertTrue(clamped.isFull(), "full after clamping");
    }

    @Test
    public void testMixedItemsAndFluidsShareCapacity() {
        CargoHold hold = CargoHold.of(100L);
        // 50 items (50 units) + 3000 mB fluid (30 units) = 80 units — fits.
        CargoHold after = hold.addItems(Materials.Copper, 50L)
            .addFluids(Materials.Water, 3000L);
        assertEquals(50L, after.itemsOf(Materials.Copper), "50 items");
        assertEquals(3000L, after.fluidsOf(Materials.Water), "3000 mB fluid");
        assertEquals(80L, after.usedUnits(), "50 + 30 = 80 units used");
        assertEquals(20L, after.remainingUnits(), "20 units remaining");

        // 25 items (25 units) would exceed the 20 remaining — clamped to 20.
        CargoHold clamped = after.addItems(Materials.Iron, 25L);
        assertEquals(20L, clamped.itemsOf(Materials.Iron), "clamped to the remaining 20 units");
        assertTrue(clamped.isFull(), "full after the mixed fill");
    }

    @Test
    public void testRemoveClampsAtZero() {
        CargoHold hold = CargoHold.of(100L)
            .addItems(Materials.Copper, 50L)
            .addFluids(Materials.Water, 2000L);
        // Remove 30 items — fits.
        CargoHold after = hold.removeItems(Materials.Copper, 30L);
        assertEquals(20L, after.itemsOf(Materials.Copper), "20 items left");
        // Remove 100 items (only 20 on board) — clamped to 20.
        CargoHold over = after.removeItems(Materials.Copper, 100L);
        assertEquals(0L, over.itemsOf(Materials.Copper), "clamped at 0 (no negative)");
        // Remove 5000 mB (only 2000 on board) — clamped to 2000.
        CargoHold fluidOver = hold.removeFluids(Materials.Water, 5000L);
        assertEquals(0L, fluidOver.fluidsOf(Materials.Water), "fluid clamped at 0");
    }

    @Test
    public void testZeroCapacityHoldAcceptsNothing() {
        CargoHold hold = CargoHold.of(0L);
        assertTrue(hold.isFull(), "a zero-capacity hold is full");
        CargoHold after = hold.addItems(Materials.Copper, 10L);
        assertEquals(0L, after.itemsOf(Materials.Copper), "accepts nothing");
    }

    @Test
    public void testNegativeCapacityClampedToZero() {
        CargoHold hold = CargoHold.of(-50L);
        assertEquals(0L, hold.getCapacity(), "negative capacity clamped to 0");
    }

    // endregion

    // region transfer (the generic ship-to-ship primitive)

    @Test
    public void testTransferToEmptyTarget() {
        CargoHold source = CargoHold.of(100L)
            .addItems(Materials.Copper, 50L)
            .addFluids(Materials.Water, 3000L);
        CargoHold target = CargoHold.of(200L);

        CargoHold.TransferResult result = source.transferTo(target);
        // Source is emptied, target is filled.
        assertEquals(0L, result.source.itemsOf(Materials.Copper), "source items transferred out");
        assertEquals(0L, result.source.fluidsOf(Materials.Water), "source fluids transferred out");
        assertEquals(50L, result.target.itemsOf(Materials.Copper), "target received the items");
        assertEquals(3000L, result.target.fluidsOf(Materials.Water), "target received the fluids");
        assertEquals(80L, result.target.usedUnits(), "target holds 80 units (50 + 30)");
    }

    @Test
    public void testTransferClampedByTargetCapacity() {
        // Source has 100 items; target has only room for 40.
        CargoHold source = CargoHold.of(1000L)
            .addItems(Materials.Copper, 100L);
        CargoHold target = CargoHold.of(40L);

        CargoHold.TransferResult result = source.transferTo(target);
        assertEquals(40L, result.target.itemsOf(Materials.Copper), "target clamped to its capacity (40)");
        assertEquals(60L, result.source.itemsOf(Materials.Copper), "source keeps the remainder (60)");
        assertTrue(result.target.isFull(), "target is full");
    }

    @Test
    public void testTransferToNullTargetIsNoOp() {
        CargoHold source = CargoHold.of(100L)
            .addItems(Materials.Copper, 50L);
        CargoHold.TransferResult result = source.transferTo(null);
        assertEquals(50L, result.source.itemsOf(Materials.Copper), "no-op: source unchanged");
        assertEquals(null, result.target, "no target");
    }

    @Test
    public void testTransferIsImmutability() {
        CargoHold source = CargoHold.of(100L)
            .addItems(Materials.Copper, 50L);
        CargoHold target = CargoHold.of(100L);
        source.transferTo(target);
        // The original holds are unchanged (immutable).
        assertEquals(50L, source.itemsOf(Materials.Copper), "original source unchanged");
        assertEquals(0L, target.itemsOf(Materials.Copper), "original target unchanged");
    }

    // endregion

    // region NBT round-trip

    @Test
    public void testNbtRoundTrip() {
        CargoHold hold = CargoHold.of(200L)
            .addItems(Materials.Copper, 50L)
            .addFluids(Materials.Water, 3000L);

        NBTTagCompound nbt = new NBTTagCompound();
        hold.writeToNBT(nbt);
        CargoHold loaded = CargoHold.readFromNBT(nbt);

        assertEquals(200L, loaded.getCapacity(), "capacity survives");
        assertEquals(50L, loaded.itemsOf(Materials.Copper), "items survive");
        assertEquals(3000L, loaded.fluidsOf(Materials.Water), "fluids survive");
        assertEquals(80L, loaded.usedUnits(), "used units survive");
    }

    @Test
    public void testNbtReadNullIsEmpty() {
        CargoHold loaded = CargoHold.readFromNBT(null);
        assertNotNull(loaded, "never null");
        assertEquals(0L, loaded.getCapacity(), "null NBT → zero capacity");
        assertTrue(loaded.isEmpty(), "null NBT → empty");
    }

    // endregion

    // region integration (fillHold + cargoFromHold)

    @Test
    public void testFillHoldFromMinerCargo() {
        // A planet with 2 ores (Copper + Iron) — mine it and fill the hold.
        registerDust(Materials.Copper);
        registerDust(Materials.Iron);

        USSPlanetDefinition def = USSPlanetDefinition.builder()
            .id("fill_world")
            .texture("Ma")
            .sizeRange(0.5f, 0.5f)
            .allowedStarType(USSStarType.MAIN_SEQUENCE)
            .ores(
                Arrays
                    .asList(new USSPlanetOre(Materials.Copper, 100L, 1.0), new USSPlanetOre(Materials.Iron, 100L, 1.0)))
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 1.0, 1.0, 1.0, 10, 10);

        long base = USSConstants.minerOreAmount(1000L);
        USSShipCargo.MinerResult result = USSShipCargo.minePlanet(planet, 1000L, null);
        NBTTagCompound cargo = result.cargo;

        // Fill a hold with capacity = base (so the full yield fits).
        CargoHold hold = CargoHold.of(base);
        CargoHold filled = USSShipCargo.fillHold(hold, cargo);
        long expected = (long) (base * 0.5) + (long) (base * 0.5); // two ores, equal weight
        assertEquals(expected, filled.usedUnits(), "the full yield fits in the hold");
        assertEquals(base * 0.5, filled.itemsOf(Materials.Copper), 1.0, "Copper on board");

        // Derive the cargo back from the hold — it should match the yield.
        NBTTagCompound derived = USSShipCargo.cargoFromHold(filled);
        NBTTagList items = USSShipCargo.readItems(derived);
        assertEquals(2, items.tagCount(), "two item entries");
    }

    @Test
    public void testFillHoldClampedByCapacity() {
        // A hold with capacity = 10 (small) — the yield is clamped.
        registerDust(Materials.Copper);

        USSPlanetDefinition def = USSPlanetDefinition.builder()
            .id("scarce_world")
            .texture("Ma")
            .sizeRange(0.5f, 0.5f)
            .allowedStarType(USSStarType.MAIN_SEQUENCE)
            .ores(Collections.singletonList(new USSPlanetOre(Materials.Copper, 100L, 1.0)))
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 1.0, 1.0, 1.0, 10, 10);

        long base = USSConstants.minerOreAmount(1000L);
        assertTrue(base > 10L, "the base must exceed the hold capacity for this test");
        USSShipCargo.MinerResult result = USSShipCargo.minePlanet(planet, 1000L, null);

        CargoHold hold = CargoHold.of(10L);
        CargoHold filled = USSShipCargo.fillHold(hold, result.cargo);
        assertEquals(10L, filled.itemsOf(Materials.Copper), "clamped to the hold capacity (10)");
        assertTrue(filled.isFull(), "the hold is full");
    }

    @Test
    public void testFillHoldFromStarlifterCargo() {
        // A starlifter cargo (3 fluids) — fill a hold.
        long power = 10L;
        long seed = 42L;
        NBTTagCompound cargo = USSShipCargo.buildForStarlifter(USSStarType.MAIN_SEQUENCE, power, seed);
        NBTTagList fluids = USSShipCargo.readFluids(cargo);
        long totalMB = 0;
        for (int i = 0; i < fluids.tagCount(); i++) {
            totalMB += fluids.getCompoundTagAt(i)
                .getLong(USSShipCargo.FLUID_ENTRY_AMOUNT);
        }
        // A hold with enough capacity for the full fluid cargo.
        long capacity = totalMB / CargoHold.MB_PER_UNIT + 10;
        CargoHold hold = CargoHold.of(capacity);
        CargoHold filled = USSShipCargo.fillHold(hold, cargo);
        long usedMB = 0;
        for (java.util.Map.Entry<Materials, Long> e : filled.getFluids()
            .entrySet()) {
            usedMB += e.getValue();
        }
        assertEquals(totalMB, usedMB, "the full fluid cargo fits in the hold");
    }

    @Test
    public void testShipCargoCapacityFromPayload() {
        // A ship's cargo capacity comes from the payload's vc_cargo (the blueprint's cargoSlots), times the
        // pass-27 CARGO_UNIT_MULTIPLIER (100× — the user's "cargo hold size increased by a factor of 100").
        NBTTagCompound payload = new NBTTagCompound();
        payload.setLong(VoidcraftNbt.TAG_CARGO, 500L);
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch("uuid-test", "cap-ship", 1.0, 1000L, payload, null, null, 42, null); // null origin → (0,0,0)
        assertEquals(
            500L * VoidcraftActiveShip.CARGO_UNIT_MULTIPLIER,
            ship.cargoCapacity(),
            "the capacity is the payload's vc_cargo × CARGO_UNIT_MULTIPLIER");
        // The hold is initialized with that capacity at construction.
        assertNotNull(ship.getHold(), "the hold is initialized at construction");
        assertEquals(
            500L * VoidcraftActiveShip.CARGO_UNIT_MULTIPLIER,
            ship.getHold()
                .getCapacity(),
            "the hold's capacity = the ship's cargoCapacity");
    }

    // endregion
}
