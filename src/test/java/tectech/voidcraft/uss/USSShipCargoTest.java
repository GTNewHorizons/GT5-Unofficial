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
 * mechanics — one planet, weighted ores, capped by the depletion reserve), the Starlifter star cargo (1–3 produced
 * fluids, base × weight × √star-size, capped by the fluid reserve), the reserve/depletion math, and the
 * stack-conversion boundary.
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
            .allowedStarType(USSStarType.YELLOW_DWARF)
            .ores(
                Arrays.asList(
                    new USSPlanetOre(Materials.Copper, 100L, 1.0),
                    new USSPlanetOre(Materials.Iron, 100L, 2.0),
                    new USSPlanetOre(Materials.Tin, 100L, 3.0)))
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 0.5, 1.0, 1.0, 10, 10, false, -1);

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
            .allowedStarType(USSStarType.YELLOW_DWARF)
            .ores(Collections.singletonList(new USSPlanetOre(Materials.Copper, 100L, 1.0)))
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 0.5, 1.0, 1.0, 10, 10, false, -1);

        // A reserve of only 50 items (well below the base×weight share).
        VoidcraftUSS.MaterialReserve reserve = new VoidcraftUSS.MaterialReserve(initialCopper(50L));

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
            .allowedStarType(USSStarType.YELLOW_DWARF)
            .ores(Collections.singletonList(new USSPlanetOre(Materials.Copper, 100L, 1.0)))
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 2.0, 1.0, 1.0, 10, 10, false, -1);

        // planetSize = 2.0 → size² = 4.0 → reserve = 100 × 1_000_000 × 4.0 = 400_000_000.
        VoidcraftUSS.MaterialReserve expected = VoidcraftUSS.MaterialReserve.fromPlanet(def, planet.scale);
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
            .allowedStarType(USSStarType.YELLOW_DWARF)
            .ores(Collections.singletonList(new USSPlanetOre(Materials.Copper, 1L, 1.0))) // 1 million items
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 1.0, 1.0, 1.0, 10, 10, false, -1);

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

    // region starlifter (siphonStar) — produced fluids, base × weight × √star-size, capped by the fluid reserve

    @Test
    public void testSiphonStarYieldsProducedFluidsCappedByReserve() {
        // Yellow dwarf: Hydrogen(3.0) / Helium(2.0) / Oxygen(1.0) — all three slots produced.
        USSStarDefinition star = USSStarRegistry.byType(USSStarType.YELLOW_DWARF);
        double size = 2.0;
        long power = 12L;
        long base = USSConstants.starlifterPlasmaAmount(power);
        double sizeFactor = Math.sqrt(size);

        USSShipCargo.StarlifterResult result = USSShipCargo.siphonStar(star, size, power, null);
        NBTTagList fluids = USSShipCargo.readFluids(result.cargo);
        assertEquals(3, fluids.tagCount(), "yellow dwarf → three fluid entries");
        assertEquals(
            0,
            USSShipCargo.readItems(result.cargo)
                .tagCount(),
            "no item cargo");

        // Per-fluid amount = min(base × weight × √size, the reserve).
        long mainShare = (long) (base * star.getMain()
            .getWeight() * sizeFactor);
        long secondaryShare = (long) (base * star.getSecondary()
            .getWeight() * sizeFactor);
        long tertiaryShare = (long) (base * star.getTertiary()
            .getWeight() * sizeFactor);
        assertEquals(
            star.getMain()
                .getMaterial()
                .getName(),
            fluids.getCompoundTagAt(0)
                .getString(USSShipCargo.FLUID_ENTRY_MATERIAL),
            "main material");
        assertEquals(
            mainShare,
            fluids.getCompoundTagAt(0)
                .getLong(USSShipCargo.FLUID_ENTRY_AMOUNT),
            "main fluid amount");
        assertEquals(
            secondaryShare,
            fluids.getCompoundTagAt(1)
                .getLong(USSShipCargo.FLUID_ENTRY_AMOUNT),
            "secondary fluid amount");
        assertEquals(
            tertiaryShare,
            fluids.getCompoundTagAt(2)
                .getLong(USSShipCargo.FLUID_ENTRY_AMOUNT),
            "tertiary fluid amount");
        // The reserve is initialized from the definition, then drawn down by the siphon.
        assertEquals(
            (long) (star.getMain()
                .getAmount() * 1_000_000L
                * size
                * size) - mainShare,
            result.newReserve.remaining(
                star.getMain()
                    .getMaterial()),
            "the reserve holds the initial amount minus the siphon");
    }

    @Test
    public void testStarFluidReserveIsInitializedFromDefinition() {
        // Each produced fluid starts at amount × 1_000_000 × starSize² (zero-capacity slots get no entry).
        USSStarDefinition star = USSStarRegistry.byType(USSStarType.YELLOW_DWARF);
        double size = 3.0;
        VoidcraftUSS.MaterialReserve reserve = VoidcraftUSS.MaterialReserve.fromStar(star, size);
        for (USSStarMaterial material : star.getMaterials()) {
            long expected = material.getAmount() > 0L ? (long) (material.getAmount() * 1_000_000L * size * size) : 0L;
            assertEquals(expected, reserve.remaining(material.getMaterial()), material.getMaterial() + " initial");
        }
        // A null definition gives an empty reserve (defensive, not a crash).
        assertEquals(
            0L,
            VoidcraftUSS.MaterialReserve.fromStar(null, size)
                .remaining(Materials.Hydrogen));
    }

    @Test
    public void testSiphonStarDepletesOverMissions() {
        // Black hole: ONE produced fluid (Osmium, weight 3.0, 500 million mB capacity at size 1).
        USSStarDefinition star = USSStarRegistry.byType(USSStarType.BLACK_HOLE);
        double size = 1.0;
        long power = 10_000L; // base = 10_000_000 (the cap)
        long base = USSConstants.starlifterPlasmaAmount(power);
        USSStarMaterial osmium = star.getMain();
        assertEquals(Materials.Osmium, osmium.getMaterial());
        long initial = (long) (osmium.getAmount() * 1_000_000L);
        long firstShare = (long) (base * osmium.getWeight() * Math.sqrt(size));
        assertTrue(firstShare < initial, "the first siphon must not exhaust the reserve");

        USSShipCargo.StarlifterResult first = USSShipCargo.siphonStar(star, size, power, null);
        assertEquals(
            firstShare,
            USSShipCargo.readFluids(first.cargo)
                .getCompoundTagAt(0)
                .getLong(USSShipCargo.FLUID_ENTRY_AMOUNT),
            "first siphon amount");
        assertEquals(initial - firstShare, first.newReserve.remaining(Materials.Osmium), "depleted by the siphon");

        USSShipCargo.StarlifterResult second = USSShipCargo.siphonStar(star, size, power, first.newReserve);
        assertEquals(initial - 2L * firstShare, second.newReserve.remaining(Materials.Osmium), "second draw");

        // A small reserve caps the yield; a fully depleted star yields empty cargo (the mission still completes).
        Map<Materials, Long> small = new LinkedHashMap<>();
        small.put(Materials.Osmium, 7L);
        USSShipCargo.StarlifterResult capped = USSShipCargo
            .siphonStar(star, size, power, new VoidcraftUSS.MaterialReserve(small));
        assertEquals(
            7L,
            USSShipCargo.readFluids(capped.cargo)
                .getCompoundTagAt(0)
                .getLong(USSShipCargo.FLUID_ENTRY_AMOUNT),
            "capped by the 7 mB left");
        assertEquals(0L, capped.newReserve.remaining(Materials.Osmium), "drained to zero");

        USSShipCargo.StarlifterResult empty = USSShipCargo.siphonStar(star, size, power, capped.newReserve);
        assertEquals(
            0,
            USSShipCargo.readFluids(empty.cargo)
                .tagCount(),
            "fully depleted → empty cargo");
        assertEquals(
            0,
            USSShipCargo.readItems(empty.cargo)
                .tagCount(),
            "and no item cargo");
    }

    @Test
    public void testSiphonStarNullDefinitionIsEmptyCargo() {
        USSShipCargo.StarlifterResult result = USSShipCargo.siphonStar(null, 1.0, 5L, null);
        assertEquals(
            0,
            USSShipCargo.readFluids(result.cargo)
                .tagCount(),
            "null star → empty cargo");
        assertNotNull(result.newReserve, "an empty reserve is still returned");
        // A passed-in reserve is preserved on a null star.
        Map<Materials, Long> map = new LinkedHashMap<>();
        map.put(Materials.Hydrogen, 42L);
        USSShipCargo.StarlifterResult kept = USSShipCargo
            .siphonStar(null, 1.0, 5L, new VoidcraftUSS.MaterialReserve(map));
        assertEquals(42L, kept.newReserve.remaining(Materials.Hydrogen), "the current reserve is untouched");
    }

    @Test
    public void testSiphonStarOmitsZeroCapacitySlots() {
        // A star produces 1–3 of its three materials: the black hole ONE (Osmium), the quasi star TWO
        // (Hydrogen + Iron) — the zero-capacity slots yield no entries.
        USSStarDefinition blackHole = USSStarRegistry.byType(USSStarType.BLACK_HOLE);
        NBTTagList holeFluids = USSShipCargo.readFluids(USSShipCargo.siphonStar(blackHole, 1.0, 5L, null).cargo);
        assertEquals(1, holeFluids.tagCount(), "black hole → one fluid entry");
        assertEquals(
            Materials.Osmium.getName(),
            holeFluids.getCompoundTagAt(0)
                .getString(USSShipCargo.FLUID_ENTRY_MATERIAL),
            "the black hole's fluid is Osmium");

        USSStarDefinition quasi = USSStarRegistry.byType(USSStarType.QUASI_STAR);
        NBTTagList quasiFluids = USSShipCargo.readFluids(USSShipCargo.siphonStar(quasi, 1.0, 5L, null).cargo);
        assertEquals(2, quasiFluids.tagCount(), "quasi star → two fluid entries (the zero-capacity slot is omitted)");
        assertEquals(
            Materials.Hydrogen.getName(),
            quasiFluids.getCompoundTagAt(0)
                .getString(USSShipCargo.FLUID_ENTRY_MATERIAL),
            "the quasi star's main fluid is Hydrogen");
        assertEquals(
            Materials.Iron.getName(),
            quasiFluids.getCompoundTagAt(1)
                .getString(USSShipCargo.FLUID_ENTRY_MATERIAL),
            "the quasi star's second fluid is Iron");
    }

    // endregion

    // region star size (sampleStarSize + starRenderSize)

    @Test
    public void testSampleStarSizeIsInRange() {
        USSStarDefinition star = USSStarRegistry.byType(USSStarType.YELLOW_DWARF);
        for (long seed = 1; seed <= 5; seed++) {
            double size = USSPlanets.sampleStarSize(USSStarType.YELLOW_DWARF, seed);
            assertTrue(
                size >= star.getSizeMin() && size <= star.getSizeMax(),
                "seed " + seed + " size " + size + " within [" + star.getSizeMin() + "," + star.getSizeMax() + "]");
        }
    }

    @Test
    public void testStarRenderSizeIsTwoThirdsSquareRoot() {
        // (2/3) × √(size): size 1.0 → 2/3, size 4.0 → 4/3, size 0 → 0.
        assertEquals(2.0f / 3.0f, USSPlanets.starRenderSize(1.0), 1e-5);
        assertEquals(4.0f / 3.0f, USSPlanets.starRenderSize(4.0), 1e-5);
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
        VoidcraftUSS.MaterialReserve reserve = new VoidcraftUSS.MaterialReserve(map);

        NBTTagCompound nbt = new NBTTagCompound();
        reserve.writeToNBT(nbt);
        VoidcraftUSS.MaterialReserve loaded = VoidcraftUSS.MaterialReserve.readFromNBT(nbt);

        assertEquals(25_000_000L, loaded.remaining(Materials.Copper), "Copper survives the round trip");
        assertEquals(10_000_000L, loaded.remaining(Materials.Iron), "Iron survives the round trip");
        assertEquals(0L, loaded.remaining(Materials.Tin), "absent material → 0");
    }

    @Test
    public void testReserveMineClampsAtZero() {
        Map<Materials, Long> map = new LinkedHashMap<>();
        map.put(Materials.Copper, 100L);
        VoidcraftUSS.MaterialReserve reserve = new VoidcraftUSS.MaterialReserve(map);

        VoidcraftUSS.MaterialReserve after = reserve.mine(Materials.Copper, 50L);
        assertEquals(50L, after.remaining(Materials.Copper), "mine subtracts");

        VoidcraftUSS.MaterialReserve over = after.mine(Materials.Copper, 1000L);
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
            .allowedStarType(USSStarType.YELLOW_DWARF)
            .ores(
                Arrays
                    .asList(new USSPlanetOre(Materials.Copper, 100L, 1.0), new USSPlanetOre(Materials.Iron, 100L, 1.0)))
            .build();
        USSPlanets.USSPlanet planet = new USSPlanets.USSPlanet(def, 5.0, 1.0, 1.0, 1.0, 10, 10, false, -1);

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
        NBTTagList fluids = USSShipCargo
            .readFluids(USSShipCargo.siphonStar(USSStarRegistry.byType(USSStarType.YELLOW_DWARF), 1.0, 9L, null).cargo);
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
