package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

/**
 * Unit tests for the deterministic mining cargo ({@link USSShipCargo}) — the planet-based miner cargo (Phase 4
 * pass 3), the Starlifter star cargo (Phase 4 pass 1), the abstract entry format, and the stack-conversion
 * boundary.
 *
 * <p>
 * Cargo is abstract ({@code {id, Damage, amount}} entries, no stacks) while it lives on the ship — see the class
 * javadoc of {@link USSShipCargo}.
 *
 * <p>
 * {@code Materials.getDust} resolves through the GT ore-dictionary unification map, which is empty in a bare JVM —
 * so the test pre-populates the map with one recognizable dust per material (distinct damage values double as ore
 * tags). Each stack's item is registered via {@link BareJvmItemRegistry} so item ↔ id resolution works (the
 * FML-controlled registry rejects bare-JVM registrations, and {@code GTOreDictUnificator.add} would touch the broken
 * {@code OreDictionary} static init).
 */
public class USSShipCargoTest {

    /** Bare-JVM test item ids (must fit in a signed short; 30011+ avoids the pool test's 30001/30002). */
    private static int nextTestId = 30011;

    /** Damage value registered per dust (distinct per material; tests reference them by material). */
    private static final Map<Materials, Integer> DAMAGE_BY_MATERIAL = new LinkedHashMap<>();

    @BeforeAll
    public static void registerDusts() {
        // Phase 4 pass 3: every planet-catalog material (the 36 are distinct by catalog invariant).
        for (USSPlanetType type : USSPlanetType.all()) {
            for (Materials material : type.getMaterials()) {
                registerDust(material);
            }
        }
        registerDust(Materials.Stone);
        // Phase 4 pass 1 Starlifter dwarf-matter dusts
        registerDust(Materials.WhiteDwarfMatter);
        registerDust(Materials.BlackDwarfMatter);
        // NOTE: no Fluid/FluidStack setup — the bare JVM cannot construct them (FluidStack's ctor triggers
        // FluidRegistry's static init, which NPEs without a game world). The Starlifter fluid cargo is therefore
        // tested at its ABSTRACT level (material name + amount), and name → material resolution — see
        // testFluidEntryResolvesToMaterial.
    }

    private static void registerDust(Materials material) {
        if (DAMAGE_BY_MATERIAL.containsKey(material)) {
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

    private static int damageWhiteDwarf() {
        return damageOf(Materials.WhiteDwarfMatter);
    }

    private static int damageBlackDwarf() {
        return damageOf(Materials.BlackDwarfMatter);
    }

    private static int damageStone() {
        return damageOf(Materials.Stone);
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

    // region Phase 4 pass 3 — planet-based miner cargo

    @Test
    public void testMinerCargoCoversTheSystemsPlanets() {
        USSStarType starType = USSStarType.WHITE_DWARF;
        List<USSPlanets.USSPlanet> system = USSPlanets.generate(starType, 42L);
        NBTTagCompound cargo = USSShipCargo.buildForMiner(system, 1000L);
        NBTTagList items = USSShipCargo.readItems(cargo);

        List<Materials> expectedOres = USSPlanets.materialsOf(system);
        assertEquals(expectedOres.size() + 1, items.tagCount(), "one abstract entry per deduplicated ore + stone");

        long expectedOre = USSConstants.minerOreAmount(1000L);
        long expectedStone = USSConstants.minerStoneDustAmount(1000L);
        for (Materials material : expectedOres) {
            assertEquals(expectedOre, amountForMeta(items, damageOf(material)), material.getName() + " dust amount");
        }
        assertEquals(expectedStone, amountForMeta(items, damageStone()), "stone dust amount");

        // every entry carries a resolvable item id with the entry's own meta
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound entry = items.getCompoundTagAt(i);
            Item item = Item.getItemById(entry.getShort(USSShipCargo.ENTRY_ID));
            assertNotNull(item, "entry " + i + " id must resolve");
        }
    }

    @Test
    public void testMinerCargoDeduplicatesRepeatedPlanetTypes() {
        // Two planets of the SAME type offer the same ore set — the cargo must list each ore once.
        USSPlanetType type = USSPlanetType.ROCKY_WORLD;
        List<USSPlanets.USSPlanet> system = new ArrayList<>();
        system.add(new USSPlanets.USSPlanet(type, 5.0, 0.5, 1.0, 1.0, 10, 10));
        system.add(new USSPlanets.USSPlanet(type, 7.0, 0.6, 1.0, 1.0, -10, 10));

        NBTTagList items = USSShipCargo.readItems(USSShipCargo.buildForMiner(system, 10L));
        assertEquals(
            type.getMaterials().length + 1,
            items.tagCount(),
            "repeated planet type → one ore set (deduplicated) + stone");
    }

    @Test
    public void testMinerCargoFromEmptySystemIsStoneOnly() {
        assertEquals(
            1,
            USSShipCargo.readItems(USSShipCargo.buildForMiner(null, 10L))
                .tagCount(),
            "null planets → stone dust only (defensive)");
        assertEquals(
            1,
            USSShipCargo.readItems(USSShipCargo.buildForMiner(Collections.emptyList(), 10L))
                .tagCount(),
            "empty planets → stone dust only (defensive)");
    }

    @Test
    public void testMinerOreSetDependsOnStarType() {
        // Star type → planet pool → ore set: the families must not overlap (catalog pools are disjoint).
        for (long seed = 1; seed <= 3; seed++) {
            Set<Materials> main = new HashSet<>(
                USSPlanets.materialsOf(USSPlanets.generate(USSStarType.MAIN_SEQUENCE, seed)));
            Set<Materials> white = new HashSet<>(
                USSPlanets.materialsOf(USSPlanets.generate(USSStarType.WHITE_DWARF, seed)));
            Set<Materials> supermassive = new HashSet<>(
                USSPlanets.materialsOf(USSPlanets.generate(USSStarType.SUPERMASSIVE, seed)));
            // (Set.isDisjoint is a Java 8+ addition — this build compiles against the 1.7 bootclasspath.)
            assertTrue(isDisjoint(main, white), "seed " + seed + " — main sequence vs white dwarf disjoint");
            assertTrue(isDisjoint(main, supermassive), "seed " + seed + " — main sequence vs supermassive disjoint");
            assertTrue(isDisjoint(white, supermassive), "seed " + seed + " — white dwarf vs supermassive disjoint");
        }
    }

    private static boolean isDisjoint(Set<Materials> a, Set<Materials> b) {
        for (Materials material : a) {
            if (b.contains(material)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testMinerAmountsScaleAndCap() {
        List<USSPlanets.USSPlanet> system = USSPlanets.generate(USSStarType.MAIN_SEQUENCE, 7L);
        Materials firstOre = system.get(0).type.getMaterials()[0];

        long small = USSConstants.minerOreAmount(1L);
        long large = USSConstants.minerOreAmount(100_000L);
        assertTrue(small > 0);
        assertEquals(USSConstants.MINER_ORE_DUST_CAP, large, "ore amount is capped");
        assertEquals(
            large * USSVeinMath.STONE_DUST_MULTIPLIER,
            USSConstants.minerStoneDustAmount(100_000L),
            "stone dust = ore amount x legacy EoH multiplier");

        NBTTagList smallItems = USSShipCargo.readItems(USSShipCargo.buildForMiner(system, 1L));
        assertEquals(
            small,
            amountForMeta(smallItems, damageOf(firstOre)),
            "cargo reflects the ore amount for low power");

        NBTTagList largeItems = USSShipCargo.readItems(USSShipCargo.buildForMiner(system, 100_000L));
        assertEquals(
            large,
            amountForMeta(largeItems, damageOf(firstOre)),
            "cargo reflects the capped amount for high power");
        assertEquals(
            large * USSVeinMath.STONE_DUST_MULTIPLIER,
            amountForMeta(largeItems, damageStone()),
            "stone dust scales with the capped ore amount");
    }

    @Test
    public void testMinerCargoIsDeterministic() {
        List<USSPlanets.USSPlanet> system = USSPlanets.generate(USSStarType.SUPERMASSIVE, 1234L);
        NBTTagList a = USSShipCargo.readItems(USSShipCargo.buildForMiner(system, 77L));
        NBTTagList b = USSShipCargo.readItems(USSShipCargo.buildForMiner(system, 77L));
        assertEquals(a.tagCount(), b.tagCount(), "same entry count");
        for (int i = 0; i < a.tagCount(); i++) {
            assertEquals(
                a.getCompoundTagAt(i)
                    .getShort(USSShipCargo.ENTRY_ID),
                b.getCompoundTagAt(i)
                    .getShort(USSShipCargo.ENTRY_ID),
                "id " + i);
            assertEquals(
                a.getCompoundTagAt(i)
                    .getShort(USSShipCargo.ENTRY_DAMAGE),
                b.getCompoundTagAt(i)
                    .getShort(USSShipCargo.ENTRY_DAMAGE),
                "meta " + i);
            assertEquals(
                a.getCompoundTagAt(i)
                    .getInteger(USSShipCargo.ENTRY_AMOUNT),
                b.getCompoundTagAt(i)
                    .getInteger(USSShipCargo.ENTRY_AMOUNT),
                "amount " + i);
        }
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
        List<USSPlanets.USSPlanet> system = USSPlanets.generate(USSStarType.MAIN_SEQUENCE, 1L);
        int expected = USSPlanets.materialsOf(system)
            .size() + 1;
        assertEquals(
            expected,
            USSShipCargo.readItems(USSShipCargo.buildForMiner(system, 10L))
                .tagCount(),
            "built cargo has the expected entry count");
    }

    // endregion

    // region delivery boundary

    @Test
    public void testToStacksSplitsIntoSixtyFours() {
        List<USSPlanets.USSPlanet> system = USSPlanets.generate(USSStarType.MAIN_SEQUENCE, 5L);
        NBTTagList items = USSShipCargo.readItems(USSShipCargo.buildForMiner(system, 1000L));
        List<ItemStack> stacks = USSShipCargo.toStacks(items);

        long expectedTotal = USSPlanets.materialsOf(system)
            .size() * USSConstants.minerOreAmount(1000L) + USSConstants.minerStoneDustAmount(1000L);
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

    // endregion

    // region Phase 4 pass 1 — Starlifter cargo (fluid production on top of the miner item cargo)

    @Test
    public void testStarlifterCargoByStarType() {
        long power = 12L;

        // MAIN_SEQUENCE: Stellar Plasma only — no items at all
        NBTTagCompound main = USSShipCargo.buildForStarlifter(USSStarType.MAIN_SEQUENCE, power);
        assertEquals(
            1,
            USSShipCargo.readFluids(main)
                .tagCount(),
            "main sequence → one fluid entry");
        assertEquals(
            0,
            USSShipCargo.readItems(main)
                .tagCount(),
            "main sequence → no item cargo");

        // WHITE_DWARF: Stellar Plasma + White Dwarf Matter dust
        NBTTagCompound white = USSShipCargo.buildForStarlifter(USSStarType.WHITE_DWARF, power);
        assertEquals(
            1,
            USSShipCargo.readFluids(white)
                .tagCount(),
            "white dwarf → one fluid entry");
        NBTTagList whiteItems = USSShipCargo.readItems(white);
        assertEquals(1, whiteItems.tagCount(), "white dwarf → one dust entry");
        assertEquals(
            damageWhiteDwarf(),
            (int) whiteItems.getCompoundTagAt(0)
                .getShort(USSShipCargo.ENTRY_DAMAGE),
            "white dwarf dust meta");
        assertEquals(
            USSConstants.starlifterMatterAmount(power),
            whiteItems.getCompoundTagAt(0)
                .getInteger(USSShipCargo.ENTRY_AMOUNT),
            "white dwarf dust amount");

        // SUPERMASSIVE: Stellar Plasma + Black Dwarf Matter dust
        NBTTagCompound supermassive = USSShipCargo.buildForStarlifter(USSStarType.SUPERMASSIVE, power);
        NBTTagList smItems = USSShipCargo.readItems(supermassive);
        assertEquals(1, smItems.tagCount(), "supermassive → one dust entry");
        assertEquals(
            damageBlackDwarf(),
            (int) smItems.getCompoundTagAt(0)
                .getShort(USSShipCargo.ENTRY_DAMAGE),
            "black dwarf dust meta");

        // every type carries the same Stellar Plasma amount (the star type picks the SOLID, not the plasma)
        long plasma = USSConstants.starlifterPlasmaAmount(power);
        for (USSStarType starType : new USSStarType[] { USSStarType.MAIN_SEQUENCE, USSStarType.WHITE_DWARF,
            USSStarType.SUPERMASSIVE }) {
            NBTTagList fluids = USSShipCargo.readFluids(USSShipCargo.buildForStarlifter(starType, power));
            NBTTagCompound fluidEntry = fluids.getCompoundTagAt(0);
            assertEquals(
                Materials.RawStarMatter.getName(),
                fluidEntry.getString(USSShipCargo.FLUID_ENTRY_MATERIAL),
                starType + " → RawStarMatter");
            assertEquals(plasma, fluidEntry.getLong(USSShipCargo.FLUID_ENTRY_AMOUNT), starType + " → plasma amount");
        }
    }

    @Test
    public void testStarlifterNullTypeFallsBackToMainSequence() {
        NBTTagCompound cargo = USSShipCargo.buildForStarlifter(null, 5L);
        assertEquals(
            1,
            USSShipCargo.readFluids(cargo)
                .tagCount());
        assertEquals(
            0,
            USSShipCargo.readItems(cargo)
                .tagCount(),
            "main-sequence fallback → no items");
    }

    @Test
    public void testFluidEntryRoundTrip() {
        NBTTagCompound cargo = USSShipCargo.buildForStarlifter(USSStarType.WHITE_DWARF, 7L);
        NBTTagCompound nbt = new NBTTagCompound();
        cargo.setString("probe", "ok"); // make sure readFluids tolerates extra keys
        nbt.setTag(USSShipCargo.TAG_FLUIDS, cargo.getTagList(USSShipCargo.TAG_FLUIDS, 10));

        NBTTagList fluids = USSShipCargo.readFluids(nbt);
        assertEquals(1, fluids.tagCount(), "round-tripped list survives");
        assertEquals(
            USSConstants.starlifterPlasmaAmount(7L),
            fluids.getCompoundTagAt(0)
                .getLong(USSShipCargo.FLUID_ENTRY_AMOUNT),
            "amount survives the round trip");
        assertEquals(
            USSConstants.starlifterMatterAmount(7L),
            USSShipCargo.readItems(cargo)
                .getCompoundTagAt(0)
                .getInteger(USSShipCargo.ENTRY_AMOUNT),
            "matter amount present in the original cargo");
    }

    @Test
    public void testFluidEntryResolvesToMaterial() {
        NBTTagList fluids = USSShipCargo.readFluids(USSShipCargo.buildForStarlifter(USSStarType.WHITE_DWARF, 9L));
        NBTTagCompound fluidEntry = fluids.getCompoundTagAt(0);

        String name = fluidEntry.getString(USSShipCargo.FLUID_ENTRY_MATERIAL);
        Materials material = Materials.get(name);
        assertNotEquals(Materials._NULL, material, "the entry's material name must resolve");
        assertEquals(Materials.RawStarMatter, material, "resolves to RawStarMatter");
        assertTrue(
            fluidEntry.getLong(USSShipCargo.FLUID_ENTRY_AMOUNT) > 0,
            "the entry carries a positive amount (unbounded long — no stack cap)");

        // unknown material name → the sentinel (the bay skips it, never voids the rest)
        assertEquals(Materials._NULL, Materials.get("definitely_not_a_material"), "unknown name → _NULL sentinel");
    }

    // endregion
}
