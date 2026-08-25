package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

/**
 * Unit tests for the deterministic cargo builders ({@link USSShipCargo}) — the planet-based miner cargo (the
 * mechanics pass — one planet, weighted ores, capped by the depletion reserve), the Starlifter star cargo (three
 * fluids, base × weight × √star-size), the reserve/depletion math, and the stack-conversion boundary.
 *
 * <p>
 * Bare-JVM: only {@link Materials} data + abstract NBT entries — no Forge fluid/block objects. The abstract entries
 * resolve through the GT ore-dictionary unification map, which is empty in a bare JVM — so the test pre-populates
 * the map with one recognizable dust per material (distinct damage values double as ore tags).
 */
public class USSShipCargoTest {

    /** Bare-JVM test item ids (must fit in a signed short). */
    private static int nextTestId = 30011;

    /** Damage value registered per dust (distinct per material; tests reference them by material). */
    private static final Map<Materials, Integer> DAMAGE_BY_MATERIAL = new LinkedHashMap<>();

    @BeforeEach
    public void setUp() {
        // A fresh catalog per test (the catalog is static; reset + re-register).
        USSPlanetRegistry.clear();
        USSStarRegistry.clear();
        USSPlanetCatalog.resetForTests();
        USSStarCatalog.resetForTests();
        USSPlanetCatalog.registerAll();
        USSStarCatalog.registerAll();
        // Register dusts for every planet-catalog ore material (idempotent — only the first test actually registers).
        for (USSPlanetDefinition def : USSPlanetRegistry.all()) {
            for (USSPlanetOre ore : def.getOres()) {
                registerDust(ore.getOreType());
            }
        }
    }

    private static void registerDust(Materials material) {
        if (material == null || material == Materials._NULL || DAMAGE_BY_MATERIAL.containsKey(material)) {
            return;
        }
        int damage = DAMAGE_BY_MATERIAL.size() + 1;
        Item item = BareJvmItemRegistry.register(new Item(), nextTestId++);
        GTOreDictUnificator.getName2StackMap()
            .put(
                OrePrefixes.dust.get(material)
                    .toString(),
                new ItemStack(item, 1, damage));
        DAMAGE_BY_MATERIAL.put(material, damage);
    }

    private static int damageOf(Materials material) {
        Integer damage = DAMAGE_BY_MATERIAL.get(material);
        assertNotNull(damage, "a test dust must be registered for " + material.getName());
        return damage;
    }

    /** The amount of the single entry with meta {@code damage} (the test dusts use distinct metas). */
    private static int amountForMeta(NBTTagList items, int damage) {
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound entry = items.getCompoundTagAt(i);
            if (entry.getShort(USSShipCargo.ENTRY_DAMAGE) == damage) {
                return entry.getInteger(USSShipCargo.ENTRY_AMOUNT);
            }
        }
        throw new AssertionError("no entry with meta " + damage);
    }

    // region miner (minePlanet) — weighted ores, capped by the reserve

    @Test
    public void testMinePlanetYieldsWeightedOres() {
        // A planet with 3 ores (weights 1.0, 2.0, 3.0) — the yield is base × (weight / Σweight).
        USSPlanetDefinition def = USSPlanetDefinition.builder()
            .id("weighted_world")
            .texture("Ma")
            .sizeRange(0.5f, 0.5f)
            .allowedStarType(USSStarType.MAIN_SEQUENCE)
            .ores(
                Arrays.asList(
                    new USSPlanetOre(Materials.Copper, 100L, 1.0),
                    new USSPlanetOre(Materials.Iron, 100L, 2.0),
                    new USSPlanetOre(Materials.Tin, 100L, 3.0)))
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 0.5, 1.0, 1.0, 10, 10);

        long base = USSConstants.minerOreAmount(1000L);
        USSShipCargo.MinerResult result = USSShipCargo.minePlanet(planet, 1000L, null);

        NBTTagList items = USSShipCargo.readItems(result.cargo);
        assertEquals(3, items.tagCount(), "one entry per ore");
        assertEquals(
            (long) (base * (1.0 / 6.0)),
            amountForMeta(items, damageOf(Materials.Copper)),
            "Copper = base×1/6");
        assertEquals((long) (base * (2.0 / 6.0)), amountForMeta(items, damageOf(Materials.Iron)), "Iron = base×2/6");
        assertEquals((long) (base * (3.0 / 6.0)), amountForMeta(items, damageOf(Materials.Tin)), "Tin = base×3/6");
    }

    @Test
    public void testMinePlanetCapsByReserve() {
        // A planet with a SMALL reserve — the yield is capped by the reserve (not the base×weight share).
        USSPlanetDefinition def = USSPlanetDefinition.builder()
            .id("scarce_world")
            .texture("Ma")
            .sizeRange(0.5f, 0.5f)
            .allowedStarType(USSStarType.MAIN_SEQUENCE)
            .ores(Collections.singletonList(new USSPlanetOre(Materials.Copper, 100L, 1.0)))
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 0.5, 1.0, 1.0, 10, 10);

        // A reserve of only 50 items (well below the base×weight share).
        VoidcraftUSS.PlanetReserve reserve = new VoidcraftUSS.PlanetReserve(initialCopper(50L));

        long base = USSConstants.minerOreAmount(1000L);
        assertTrue(base > 50L, "the base must exceed the reserve for this test");

        USSShipCargo.MinerResult result = USSShipCargo.minePlanet(planet, 1000L, reserve);
        assertEquals(
            50L,
            amountForMeta(USSShipCargo.readItems(result.cargo), damageOf(Materials.Copper)),
            "yield capped by the reserve");
        assertEquals(0L, result.newReserve.remaining(Materials.Copper), "reserve fully depleted");
    }

    @Test
    public void testMinePlanetInitializesReserveFromDefinition() {
        // A null reserve → initialized from the planet definition (ore.amount × planetSize²).
        USSPlanetDefinition def = USSPlanetDefinition.builder()
            .id("init_world")
            .texture("Ma")
            .sizeRange(0.5f, 0.5f)
            .allowedStarType(USSStarType.MAIN_SEQUENCE)
            .ores(Collections.singletonList(new USSPlanetOre(Materials.Copper, 100L, 1.0)))
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 2.0, 1.0, 1.0, 10, 10);

        // planetSize = 2.0 → size² = 4.0 → reserve = 100 × 1_000_000 × 4.0 = 400_000_000.
        VoidcraftUSS.PlanetReserve expected = VoidcraftUSS.PlanetReserve.fromPlanet(def, planet.scale);
        assertEquals(400_000_000L, expected.remaining(Materials.Copper), "reserve = amount × planetSize²");

        USSShipCargo.MinerResult result = USSShipCargo.minePlanet(planet, 10L, null);
        assertNotNull(result.newReserve, "a new reserve is returned");
        assertTrue(result.newReserve.remaining(Materials.Copper) < 400_000_000L, "the reserve is decremented");
    }

    @Test
    public void testMinePlanetDepletesAcrossMissions() {
        // Mine the same planet twice — the second mission yields less (the reserve decreased).
        USSPlanetDefinition def = USSPlanetDefinition.builder()
            .id("deplete_world")
            .texture("Ma")
            .sizeRange(0.5f, 0.5f)
            .allowedStarType(USSStarType.MAIN_SEQUENCE)
            .ores(Collections.singletonList(new USSPlanetOre(Materials.Copper, 1L, 1.0))) // 1 million items
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 1.0, 1.0, 1.0, 10, 10);

        long base = USSConstants.minerOreAmount(100L);
        USSShipCargo.MinerResult mission1 = USSShipCargo.minePlanet(planet, 100L, null);
        long yield1 = amountForMeta(USSShipCargo.readItems(mission1.cargo), damageOf(Materials.Copper));

        USSShipCargo.MinerResult mission2 = USSShipCargo.minePlanet(planet, 100L, mission1.newReserve);
        long yield2 = amountForMeta(USSShipCargo.readItems(mission2.cargo), damageOf(Materials.Copper));

        // The reserve is finite (1M × 1.0² = 1M), so both missions draw from it and the reserve shrinks.
        assertTrue(yield1 > 0, "first mission yields");
        assertTrue(yield2 > 0, "second mission yields");
        assertTrue(
            mission2.newReserve.remaining(Materials.Copper) < mission1.newReserve.remaining(Materials.Copper),
            "the reserve decreases across missions");
    }

    @Test
    public void testMinePlanetNullOrEmptyIsEmptyCargo() {
        assertEquals(
            0,
            USSShipCargo.readItems(USSShipCargo.minePlanet(null, 10L, null).cargo)
                .tagCount(),
            "null planet → empty cargo");
    }

    // endregion

    // region starlifter (3-arg buildForStarlifter) — three fluids, base × weight × √star-size

    @Test
    public void testStarlifterYieldsThreeWeightedFluids() {
        long power = 12L;
        long seed = 42L;
        NBTTagCompound cargo = USSShipCargo.buildForStarlifter(USSStarType.MAIN_SEQUENCE, power, seed);

        NBTTagList fluids = USSShipCargo.readFluids(cargo);
        assertEquals(3, fluids.tagCount(), "main sequence → three fluid entries");
        assertEquals(
            0,
            USSShipCargo.readItems(cargo)
                .tagCount(),
            "no item cargo");

        // The amounts follow base × weight × √(sampled size).
        double size = USSPlanets.sampleStarSize(USSStarType.MAIN_SEQUENCE, seed);
        double sizeFactor = Math.sqrt(size);
        long base = USSConstants.starlifterPlasmaAmount(power);

        USSStarDefinition star = USSStarRegistry.byType(USSStarType.MAIN_SEQUENCE);
        assertEquals(
            base * star.getMain()
                .getWeight() * sizeFactor,
            fluids.getCompoundTagAt(0)
                .getLong(USSShipCargo.FLUID_ENTRY_AMOUNT),
            1.0,
            "main fluid amount");
        assertEquals(
            base * star.getSecondary()
                .getWeight() * sizeFactor,
            fluids.getCompoundTagAt(1)
                .getLong(USSShipCargo.FLUID_ENTRY_AMOUNT),
            1.0,
            "secondary fluid amount");
        assertEquals(
            base * star.getTertiary()
                .getWeight() * sizeFactor,
            fluids.getCompoundTagAt(2)
                .getLong(USSShipCargo.FLUID_ENTRY_AMOUNT),
            1.0,
            "tertiary fluid amount");
    }

    @Test
    public void testStarlifterFluidMaterialsMatchStar() {
        long power = 5L;
        long seed = 7L;
        USSStarDefinition star = USSStarRegistry.byType(USSStarType.WHITE_DWARF);

        NBTTagList fluids = USSShipCargo
            .readFluids(USSShipCargo.buildForStarlifter(USSStarType.WHITE_DWARF, power, seed));
        assertEquals(3, fluids.tagCount(), "white dwarf → three fluid entries");
        assertEquals(
            star.getMain()
                .getMaterial()
                .getName(),
            fluids.getCompoundTagAt(0)
                .getString(USSShipCargo.FLUID_ENTRY_MATERIAL),
            "main material");
        assertEquals(
            star.getSecondary()
                .getMaterial()
                .getName(),
            fluids.getCompoundTagAt(1)
                .getString(USSShipCargo.FLUID_ENTRY_MATERIAL),
            "secondary material");
        assertEquals(
            star.getTertiary()
                .getMaterial()
                .getName(),
            fluids.getCompoundTagAt(2)
                .getString(USSShipCargo.FLUID_ENTRY_MATERIAL),
            "tertiary material");
    }

    @Test
    public void testStarlifterNullStarFallsBackToMainSequence() {
        // A null star type is a defensive case — it falls back to MAIN_SEQUENCE (consistent with generate +
        // sampleStarSize), so it yields the main sequence's 3 fluids (not an empty cargo).
        NBTTagCompound cargo = USSShipCargo.buildForStarlifter(null, 5L, 1L);
        assertEquals(
            3,
            USSShipCargo.readFluids(cargo)
                .tagCount(),
            "null star → main sequence's 3 fluids");
        assertEquals(
            0,
            USSShipCargo.readItems(cargo)
                .tagCount(),
            "no item cargo");
    }

    @Test
    public void testStarlifterUnregisteredStarIsEmptyCargo() {
        // An enum value with NO registered star definition → empty cargo (not a crash).
        NBTTagCompound cargo = USSShipCargo.buildForStarlifter(USSStarType.SUPERMASSIVE, 5L, 1L);
        // (SUPERMASSIVE IS registered in the default catalog — so this yields fluids. The unregistered case is
        // exercised by clearing the registry first.)
        USSStarRegistry.clear();
        NBTTagCompound empty = USSShipCargo.buildForStarlifter(USSStarType.SUPERMASSIVE, 5L, 1L);
        assertEquals(
            0,
            USSShipCargo.readFluids(empty)
                .tagCount(),
            "unregistered star → no fluids");
    }

    // endregion

    // region star size (sampleStarSize + starRenderSize)

    @Test
    public void testSampleStarSizeIsInRange() {
        USSStarDefinition star = USSStarRegistry.byType(USSStarType.MAIN_SEQUENCE);
        for (long seed = 1; seed <= 5; seed++) {
            double size = USSPlanets.sampleStarSize(USSStarType.MAIN_SEQUENCE, seed);
            assertTrue(
                size >= star.getSizeMin() && size <= star.getSizeMax(),
                "seed " + seed + " size " + size + " within [" + star.getSizeMin() + "," + star.getSizeMax() + "]");
        }
    }

    @Test
    public void testStarRenderSizeIsHalfSquareRoot() {
        // 0.5 × √(size): size 1.0 → 0.5, size 4.0 → 1.0, size 0 → 0.
        assertEquals(0.5f, USSPlanets.starRenderSize(1.0), 1e-5);
        assertEquals(1.0f, USSPlanets.starRenderSize(4.0), 1e-5);
        assertEquals(0.0f, USSPlanets.starRenderSize(0.0), 1e-9);
        assertEquals(0.0f, USSPlanets.starRenderSize(-1.0), 1e-9);
    }

    // endregion

    // region reserve NBT round-trip

    @Test
    public void testReserveNbtRoundTrip() {
        Map<Materials, Long> map = new LinkedHashMap<>();
        map.put(Materials.Copper, 25_000_000L);
        map.put(Materials.Iron, 10_000_000L);
        VoidcraftUSS.PlanetReserve reserve = new VoidcraftUSS.PlanetReserve(map);

        NBTTagCompound nbt = new NBTTagCompound();
        reserve.writeToNBT(nbt);
        VoidcraftUSS.PlanetReserve loaded = VoidcraftUSS.PlanetReserve.readFromNBT(nbt);

        assertEquals(25_000_000L, loaded.remaining(Materials.Copper), "Copper survives the round trip");
        assertEquals(10_000_000L, loaded.remaining(Materials.Iron), "Iron survives the round trip");
        assertEquals(0L, loaded.remaining(Materials.Tin), "absent material → 0");
    }

    @Test
    public void testReserveMineClampsAtZero() {
        Map<Materials, Long> map = new LinkedHashMap<>();
        map.put(Materials.Copper, 100L);
        VoidcraftUSS.PlanetReserve reserve = new VoidcraftUSS.PlanetReserve(map);

        VoidcraftUSS.PlanetReserve after = reserve.mine(Materials.Copper, 50L);
        assertEquals(50L, after.remaining(Materials.Copper), "mine subtracts");

        VoidcraftUSS.PlanetReserve over = after.mine(Materials.Copper, 1000L);
        assertEquals(0L, over.remaining(Materials.Copper), "mine clamps at 0 (no negative)");
    }

    // endregion

    // region delivery boundary

    @Test
    public void testToStacksSplitsIntoSixtyFours() {
        USSPlanetDefinition def = USSPlanetDefinition.builder()
            .id("stack_world")
            .texture("Ma")
            .sizeRange(0.5f, 0.5f)
            .allowedStarType(USSStarType.MAIN_SEQUENCE)
            .ores(
                Arrays
                    .asList(new USSPlanetOre(Materials.Copper, 100L, 1.0), new USSPlanetOre(Materials.Iron, 100L, 1.0)))
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 1.0, 1.0, 1.0, 10, 10);

        USSShipCargo.MinerResult result = USSShipCargo.minePlanet(planet, 1000L, null);
        NBTTagList items = USSShipCargo.readItems(result.cargo);
        List<ItemStack> stacks = USSShipCargo.toStacks(items);

        long expectedTotal = amountForMeta(items, damageOf(Materials.Copper))
            + amountForMeta(items, damageOf(Materials.Iron));
        long total = 0;
        for (ItemStack stack : stacks) {
            assertNotNull(stack);
            assertTrue(
                stack.stackSize >= 1 && stack.stackSize <= 64,
                "every stack within the vanilla cap: " + stack.stackSize);
            total += stack.stackSize;
        }
        assertEquals(expectedTotal, total, "no amount lost in the conversion");
    }

    @Test
    public void testToStacksSkipsUnknownIds() {
        NBTTagList items = new NBTTagList();
        NBTTagCompound entry = new NBTTagCompound();
        entry.setShort(USSShipCargo.ENTRY_ID, (short) 30001); // unregistered id → skipped
        entry.setShort(USSShipCargo.ENTRY_DAMAGE, (short) 0);
        entry.setInteger(USSShipCargo.ENTRY_AMOUNT, 128);
        items.appendTag(entry);
        assertEquals(
            0,
            USSShipCargo.toStacks(items)
                .size(),
            "unresolvable id → no stacks");
        assertEquals(
            0,
            USSShipCargo.toStacks(null)
                .size(),
            "null list → empty");
    }

    @Test
    public void testReadItemsNeverNull() {
        assertEquals(
            0,
            USSShipCargo.readItems(null)
                .tagCount(),
            "null cargo → empty list");
        assertEquals(
            0,
            USSShipCargo.readItems(new NBTTagCompound())
                .tagCount(),
            "missing list → empty list");
    }

    @Test
    public void testFluidEntryResolvesToMaterial() {
        NBTTagList fluids = USSShipCargo.readFluids(USSShipCargo.buildForStarlifter(USSStarType.MAIN_SEQUENCE, 9L, 1L));
        NBTTagCompound fluidEntry = fluids.getCompoundTagAt(0);

        String name = fluidEntry.getString(USSShipCargo.FLUID_ENTRY_MATERIAL);
        Materials material = Materials.get(name);
        assertNotEquals(Materials._NULL, material, "the entry's material name must resolve");
        assertTrue(fluidEntry.getLong(USSShipCargo.FLUID_ENTRY_AMOUNT) > 0, "the entry carries a positive amount");

        // unknown material name → the sentinel (the bay skips it, never voids the rest)
        assertEquals(Materials._NULL, Materials.get("definitely_not_a_material"), "unknown name → _NULL sentinel");
    }

    // endregion

    // region helpers

    private static Map<Materials, Long> initialCopper(long amount) {
        Map<Materials, Long> map = new LinkedHashMap<>();
        map.put(Materials.Copper, amount);
        return map;
    }

    // endregion
}
