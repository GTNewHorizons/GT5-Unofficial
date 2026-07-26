package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialRefStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

/// Macerator, ore-washer, thermal-centrifuge, forge-hammer, centrifuge, and electrolyzer/chemical-dehydrator
/// recipes for the gtPlusPlus ore minerals in [#ELIGIBLE_STANDARD]/[#ELIGIBLE_NO_OPTIONAL], each carrying the
/// two bonus byproducts [#registerOreProcessing] picks from the mineral's flattened composition.
///
/// These recipes are registered here rather than through the canonical `ProcessingOre`/`ProcessingDirty`/
/// `ProcessingCrushedOre`/`ProcessingPure` autogen because that autogen reads
/// [GTMaterialProperties#ORE_BYPRODUCTS] with its own per-shape convention for which slot each recipe takes,
/// which does not line up with the bonus pair these minerals need; it still runs for the same materials, so
/// both sets of recipes coexist.
///
/// [#hasSolidForm] gates which composition parts can be a byproduct: a part qualifies only when its `dust`,
/// `block`, `dustTiny`, and `dustSmall` shapes all resolve. Material state alone is not equivalent and picks
/// different byproducts. [#gtppState] serves the narrower cell-vs-dust split in [#registerDecomposition]
/// (`SOLID`/`ORE` parts decompose to dust, everything else to a cell).
///
/// The shaped-crafting and [Materials2Materials#FluoriteF] chemical-bath recipes carry no byproduct and live
/// in [ProcessingOreCrafting].
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see [ProcessingDustGeneration]'s
/// class javadoc for why that timing matters.
public class ProcessingOreMachine {

    private ProcessingOreMachine() {}

    /// Every material the retired `RecipeGenOre` reached through `MaterialGenerator#generateOreMaterial`
    /// (`disableOptional=false`) -- the electrolyzer/chemical-dehydrator branch in [#generate] runs only for
    /// these. Together with [#ELIGIBLE_NO_OPTIONAL] this is the exact same 51-material union
    /// [ProcessingOreCrafting#ELIGIBLE] declares for its own, disableOptional-independent, crafting recipes.
    // spotless:off
    private static final Set<Material> ELIGIBLE_STANDARD = Set.of(
        Materials2Materials.Crocoite, Materials2Materials.Geikielite, Materials2Materials.Nichromite,
        Materials2Materials.Titanite, Materials2Materials.Zimbabweite, Materials2Materials.Zirconolite,
        Materials2Materials.GadoliniteCe, Materials2Materials.GadoliniteY, Materials2Materials.Lepersonnite,
        Materials2Materials.SamarskiteY, Materials2Materials.SamarskiteYb, Materials2Materials.Xenotime,
        Materials2Materials.Yttriaite, Materials2Materials.Yttrialite, Materials2Materials.Yttrocerite,
        Materials2Materials.Zircon, Materials2Materials.Polycrase, Materials2Materials.Zircophyllite,
        Materials2Materials.Zirkelite, Materials2Materials.LanthaniteLa, Materials2Materials.LanthaniteCe,
        Materials2Materials.LanthaniteNd, Materials2Materials.AgarditeY, Materials2Materials.AgarditeCd,
        Materials2Materials.AgarditeLa, Materials2Materials.AgarditeNd, Materials2Materials.Hibonite,
        Materials2Materials.Cerite, Materials2Materials.Fluorcaphite, Materials2Materials.Florencite,
        Materials2Materials.CryoliteF, Materials2Materials.Lautarite, Materials2Materials.Lafossaite,
        Materials2Materials.DemicheleiteBr, Materials2Materials.Comancheite, Materials2Materials.Perroudite,
        Materials2Materials.Honeaite, Materials2Materials.Alburnite, Materials2Materials.Miessiite,
        Materials2Materials.Kashinite, Materials2Materials.Irarsite, Materials2Materials.Greenockite,
        Materials2Materials.BariteRa, Materials2Materials.RadioactiveMineralMix, Materials2Materials.RareEarthI,
        Materials2Materials.RareEarthII, Materials2Materials.RareEarthIII, Materials2Materials.FluoriteF,
        Materials2Materials.Koboldite);
    // spotless:on

    /// Every material the retired `RecipeGenOre` reached through `MaterialGenerator
    /// #generateOreMaterialWithAllExcessComponents` (`disableOptional=true`) -- everything in [#generate]
    /// except the electrolyzer/chemical-dehydrator branch.
    private static final Set<Material> ELIGIBLE_NO_OPTIONAL = Set
        .of(Materials2Materials.AncientGranite, Materials2Materials.Runite);

    private static final Material mStone = Materials2Materials.Stone;

    public static void run() {
        for (Material material : ELIGIBLE_STANDARD) {
            generate(material, false);
        }
        for (Material material : ELIGIBLE_NO_OPTIONAL) {
            generate(material, true);
        }
    }

    private static void generate(final Material material, final boolean disableOptional) {
        Integer tier = material.getProperty(GTMaterialProperties.TIER);
        int tVoltageMultiplier = voltageForTier(tier != null ? tier : 0);
        final ItemStack dustStone = ProcessingDustGeneration.stackOf(OrePrefixes.dust, mStone, 1);

        Material bonusA = null;
        Material bonusB = null;

        // Setup Bonuses
        List<Material> aMatComp = new ArrayList<>(compoundMaterialsRecursively(material));
        while (aMatComp.size() < 3) {
            aMatComp.add(material);
        }

        final List<Material> amJ = new ArrayList<>();
        for (Material g : aMatComp) {
            if (hasSolidForm(g)) {
                amJ.add(g);
                if (amJ.size() >= 2) break;
            }
        }

        boolean allFailed = false;
        final List<MaterialRefStack> composites = composition(material);
        if (amJ.size() < 2) {
            allFailed = true;
            bonusA = composites.isEmpty() ? material
                : composites.get(0)
                    .material()
                    .resolve();

            // If Secondary Output has no solid output, try the third (If it exists), then the fourth/fifth
            for (int i = 1; i < Math.min(composites.size(), 5); i++) {
                bonusB = composites.get(i)
                    .material()
                    .resolve();
                if (bonusB != null && hasSolidForm(bonusB)) {
                    allFailed = false;
                    break;
                }
            }
        } else {
            bonusA = amJ.get(0);
            bonusB = amJ.get(1);
        }

        if (bonusA == null) {
            bonusA = tVoltageMultiplier > 100 ? material : mStone;
        }
        if (allFailed || bonusB == null) {
            bonusB = tVoltageMultiplier > 100 ? material : mStone;
        }

        ArrayList<Pair<Integer, Material>> componentMap = new ArrayList<>();
        for (MaterialRefStack r : composites) {
            componentMap.add(
                Pair.of(
                    (int) partsPerOneHundred(r.amount()),
                    r.material()
                        .resolve()));
        }

        if (bonusA == null || !hasSolidForm(bonusA)) {
            bonusA = mStone;
        }
        if (bonusB == null || !hasSolidForm(bonusB)) {
            bonusB = mStone;
        }

        ItemStack matDust = getDust(material);
        ItemStack matDustA = getDust(bonusA);
        ItemStack matDustB = getDust(bonusB);

        // Macerate ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.ore, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 2), matDustA, dustStone)
            .outputChances(100_00, 10_00, 50_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate raw ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.rawOre, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 2), matDustA, dustStone)
            .outputChances(100_00, 5_00, 50_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate Crushed to Impure Dust
        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.dustImpure, material, 1), matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate Washed to Purified Dust
        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushedPurified, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.dustPure, material, 1), matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate Centrifuged to Pure Dust
        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushedCentrifuged, material, 1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Wash
        RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 1))
            .itemOutputs(
                ProcessingDustGeneration.stackOf(OrePrefixes.crushedPurified, material, 1),
                matDustA,
                dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 1))
            .itemOutputs(
                ProcessingDustGeneration.stackOf(OrePrefixes.crushedPurified, material, 1),
                matDustA,
                dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTModHandler.getDistilledWater(200))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        // Thermal Centrifuge
        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 1))
            .itemOutputs(
                ProcessingDustGeneration.stackOf(OrePrefixes.crushedCentrifuged, material, 1),
                matDustB,
                dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushedPurified, material, 1))
            .itemOutputs(
                ProcessingDustGeneration.stackOf(OrePrefixes.crushedCentrifuged, material, 1),
                matDustA,
                dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        // Forge Hammer
        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushedCentrifuged, material, 1))
            .itemOutputs(matDust)
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.dustImpure, material, 1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushedPurified, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.dustPure, material, 1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.ore, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        // Centrifuge
        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.dustPure, material, 1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 11_11)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, MU.mass(material) * 8L))
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.dustImpure, material, 1))
            .itemOutputs(matDust, matDustB)
            .outputChances(100_00, 11_11)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, MU.mass(material) * 8L))
            .addTo(centrifugeRecipes);

        // Electrolyzer / Chemical Dehydrator
        if (!disableOptional) {
            if (!componentMap.isEmpty() && componentMap.size() <= 6) {
                registerDecomposition(material, componentMap, 6, false, tVoltageMultiplier);
            } else if (componentMap.size() > 6 && componentMap.size() <= 9) {
                registerDecomposition(material, componentMap, 9, true, tVoltageMultiplier);
            }
        }
    }

    /// The retired generator's electrolyzer (`slots=6`)/chemical-dehydrator (`slots=9`) decomposition recipe:
    /// one dust or empty-cell-filling item per composite part (a cell for a non-`SOLID` part -- the
    /// dehydrator branch also counts `ORE` as solid, exactly as the retired generator did), all at 100% chance
    /// (`GTValues.NI`'s null-replacement pass before the null-strip is a no-op -- `NI` is itself `null` --
    /// reproduced anyway so this stays a literal port), input dust sized to the composition's GCD-reduced
    /// total (falling back to the raw total, then skipping the recipe if neither resolves).
    private static void registerDecomposition(Material material, ArrayList<Pair<Integer, Material>> componentMap,
        int slots, boolean dehydrator, int tVoltageMultiplier) {
        ItemStack[] mInternalOutputs = new ItemStack[slots];
        int[] mChances = new int[slots];
        int mCellCount = 0;
        int mTotalCount = 0;

        int mCounter = 0;
        for (Pair<Integer, Material> f : componentMap) {
            String state = gtppState(f.getValue());
            boolean nonSolid = dehydrator ? !"SOLID".equals(state) && !"ORE".equals(state) : !"SOLID".equals(state);
            if (nonSolid) {
                mInternalOutputs[mCounter++] = cellStack(f.getValue(), f.getKey());
                mCellCount += f.getKey();
                mTotalCount += f.getKey();
            } else {
                mInternalOutputs[mCounter++] = ProcessingDustGeneration
                    .stackOf(OrePrefixes.dust, f.getValue(), f.getKey());
                mTotalCount += f.getKey();
            }
        }

        for (int g = 0; g < mInternalOutputs.length; g++) {
            mChances[g] = (mInternalOutputs[g] != null ? 10000 : 0);
        }

        ItemStack emptyCell = null;
        if (mCellCount > 0) {
            emptyCell = ItemList.Cell_Empty.get(mCellCount);
        }

        ItemStack mainDust = ProcessingDustGeneration
            .stackOf(OrePrefixes.dust, material, smallestStackSizeWhenProcessing(material));
        if (mainDust == null) {
            mainDust = ProcessingDustGeneration.stackOf(OrePrefixes.dust, material, mTotalCount);
            if (mainDust == null) {
                return;
            }
        }

        for (int j = 0; j < mInternalOutputs.length; j++) {
            if (mInternalOutputs[j] == null) {
                mInternalOutputs[j] = GTValues.NI;
            }
        }

        List<ItemStack> internalOutputs = new ArrayList<>(Arrays.asList(mInternalOutputs));
        internalOutputs.removeIf(Objects::isNull);
        int[] chances = new int[internalOutputs.size()];
        System.arraycopy(mChances, 0, chances, 0, internalOutputs.size());

        ItemStack[] inputs = emptyCell == null ? new ItemStack[] { mainDust } : new ItemStack[] { mainDust, emptyCell };

        GTRecipeBuilder recipe = GTValues.RA.stdBuilder()
            .itemInputs(inputs)
            .itemOutputs(internalOutputs.toArray(new ItemStack[0]))
            .outputChances(chances)
            .eut(tVoltageMultiplier);
        if (dehydrator) {
            recipe.duration((int) Math.max(MU.mass(material) * 4L, 1))
                .addTo(chemicalDehydratorRecipes);
        } else {
            recipe.duration((int) Math.max(MU.mass(material) * 3L, 1))
                .addTo(electrolyzerRecipes);
        }
    }

    private static ItemStack getDust(Material m) {
        ItemStack x = ProcessingDustGeneration.stackOf(OrePrefixes.dust, m, 1);
        if (x == null) {
            x = ProcessingDustGeneration.stackOf(OrePrefixes.dust, mStone, 1);
        }
        return x;
    }

    /// A composite part's cell for the electrolyzer/chemical-dehydrator decomposition recipe: the retired
    /// `MaterialReconstruction#cellStack`'s own resolution -- the plain `cell` shape, falling back to
    /// `cellMolten` for a material whose single fluid claimed the [gregtech.api.enums.materials2.
    /// Materials2FluidShapes#fluidMolten] shape instead of a liquid/gas cell-eligible one (every `SOLID`- and
    /// `LIQUID`-state gtpp material registers its fluid this way). [ProcessingDustGeneration#stackOf]'s own
    /// [gregtech.api.util.GTOreDictUnificator] fallback is deliberately not used here: for a material whose
    /// plain `cell` shape does not exist, that fallback resolves the legacy gtPlusPlus cell item instead of
    /// `cellMolten`. Package-visible: [ProcessingPlasmaGtpp] shares this same cell resolution for its own
    /// plasma-cell cooldown recipe's cell output, which needs the identical cell-then-cellMolten fallback.
    static ItemStack cellStack(Material material, long amount) {
        ItemStack cell = MU.stack(OrePrefixes.cell, material, amount);
        return cell != null ? cell : MU.stack(OrePrefixes.cellMolten, material, amount);
    }

    private static List<MaterialRefStack> composition(Material material) {
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        return composition != null ? composition : List.of();
    }

    /// The flattened leaf materials of `toSearch`'s composite tree, by a BFS with a 1000-node hard limit.
    ///
    /// Only a material this class already generates recipes for is expanded; every other part is a leaf even
    /// when it carries a [GTMaterialProperties#COMPOSITION] of its own. That composition is chemical data,
    /// which is not the same question as whether a part decomposes into further byproducts here: Koboldite's
    /// `Thaumium` part is composed of `[Iron, Magic]`, but it is a byproduct in its own right, and expanding
    /// it would shift both byproduct picks along the flattened list. The ore materials that genuinely do
    /// decompose further ([Materials2Materials#Fluorcaphite], the rare earth mixes) are all members of
    /// [#ELIGIBLE_STANDARD]/[#ELIGIBLE_NO_OPTIONAL] themselves.
    private static List<Material> compoundMaterialsRecursively(Material toSearch) {
        List<Material> result = new ArrayList<>();
        Deque<Material> toCheck = new ArrayDeque<>();
        toCheck.add(toSearch);
        int processed = 0;
        while (!toCheck.isEmpty() && processed < 1000) {
            Material current = toCheck.remove();
            List<MaterialRefStack> composites = EXPANDABLE.contains(current) ? composition(current) : List.of();
            if (composites.isEmpty()) {
                result.add(current);
            } else {
                for (MaterialRefStack entry : composites) {
                    Material child = entry.material()
                        .resolve();
                    if (child != null) toCheck.add(child);
                }
            }
            processed++;
        }
        return result;
    }

    /// The materials whose composition decomposes further in this walk. A material outside this set is a
    /// byproduct in its own right and stays a leaf even when it carries a [GTMaterialProperties#COMPOSITION]:
    /// that property is chemical data, which is a different question. Koboldite's `Thaumium` and the rare earth
    /// mixes' `BlackMetal` are the cases that make the distinction visible -- `Thaumium` is composed of
    /// `[Iron, Magic]` but is itself the byproduct, while `BlackMetal` genuinely resolves to its own parts.
    // spotless:off
    private static final Set<Material> EXPANDABLE = Set.of(
        Materials2Materials.AgarditeCd, Materials2Materials.AgarditeLa, Materials2Materials.AgarditeNd,
        Materials2Materials.AgarditeY, Materials2Materials.Alburnite, Materials2Materials.AncientGranite,
        Materials2Materials.BariteRa, Materials2Materials.BlackMetal, Materials2Materials.Cerite,
        Materials2Materials.Comancheite, Materials2Materials.Crocoite, Materials2Materials.CryoliteF,
        Materials2Materials.DemicheleiteBr, Materials2Materials.Florencite, Materials2Materials.Fluorcaphite,
        Materials2Materials.FluoriteF, Materials2Materials.GadoliniteCe, Materials2Materials.GadoliniteY,
        Materials2Materials.Geikielite, Materials2Materials.Greenockite, Materials2Materials.Hibonite,
        Materials2Materials.Honeaite, Materials2Materials.Irarsite, Materials2Materials.Kashinite,
        Materials2Materials.Koboldite, Materials2Materials.Lafossaite, Materials2Materials.LanthaniteCe,
        Materials2Materials.LanthaniteLa, Materials2Materials.LanthaniteNd, Materials2Materials.Lautarite,
        Materials2Materials.Lepersonnite, Materials2Materials.Miessiite, Materials2Materials.Nichromite,
        Materials2Materials.Perroudite, Materials2Materials.Polycrase, Materials2Materials.RadioactiveMineralMix,
        Materials2Materials.RareEarthI, Materials2Materials.RareEarthII, Materials2Materials.RareEarthIII,
        Materials2Materials.SamarskiteY, Materials2Materials.SamarskiteYb, Materials2Materials.StrontiumOxide,
        Materials2Materials.Titanite, Materials2Materials.Xenotime, Materials2Materials.Yttriaite,
        Materials2Materials.Yttrialite, Materials2Materials.Yttrocerite, Materials2Materials.Zimbabweite,
        Materials2Materials.Zircon, Materials2Materials.Zirconolite, Materials2Materials.Zircophyllite,
        Materials2Materials.Zirkelite);
    // spotless:on

    /// `material`'s state, taken from [GTMaterialProperties#GTPP_STATE] when set and otherwise derived from
    /// which fluid it carries: a molten or solid fluid means `SOLID`, else a liquid fluid means `LIQUID`, else
    /// a gas means `GAS`, else `SOLID`. Package-visible: [ProcessingMaterialDecompositionGtpp] shares this
    /// same state derivation for its own composite-part decomposition.
    static String gtppState(Material material) {
        String state = material.getProperty(GTMaterialProperties.GTPP_STATE);
        if (state != null) return state;
        if (MU.molten(material, 1) != null || MU.solid(material, 1) != null) return "SOLID";
        if (MU.fluid(material, 1) != null) return "LIQUID";
        if (MU.gas(material, 1) != null) return "GAS";
        return "SOLID";
    }

    /// The retired `Material#hasSolidForm()` predicate: a material counts as solid only when its `dust`,
    /// `block`, `dustTiny`, and `dustSmall` shapes all resolve -- the exact conjunction
    /// `ItemUtils#checkForInvalidItems` applied to `getDust`/`getBlock`/`getTinyDust`/`getSmallDust`, not a
    /// [#gtppState] stand-in: a material can carry `GTPP_STATE == SOLID` while missing one of these four
    /// shapes (or vice versa for a non-`SOLID` material that still carries all four), and the retired predicate
    /// followed the shapes, not the state.
    private static boolean hasSolidForm(Material material) {
        return ProcessingDustGeneration.stackOf(OrePrefixes.dust, material, 1) != null
            && ProcessingDustGeneration.stackOf(OrePrefixes.block, material, 1) != null
            && ProcessingDustGeneration.stackOf(OrePrefixes.dustTiny, material, 1) != null
            && ProcessingDustGeneration.stackOf(OrePrefixes.dustSmall, material, 1) != null;
    }

    private static long partsPerOneHundred(long amount) {
        return amount >= 1 && amount <= 100 ? amount : 100;
    }

    /// `material`'s own [GTMaterialProperties#COMPOSITION] parts-per-hundred, GCD-reduced and summed -- the
    /// retired `Material#smallestStackSizeWhenProcessing` field.
    private static long smallestStackSizeWhenProcessing(Material material) {
        List<MaterialRefStack> composites = composition(material);
        if (composites.isEmpty()) return 0;
        long[] parts = new long[composites.size()];
        for (int i = 0; i < composites.size(); i++) {
            parts[i] = partsPerOneHundred(
                composites.get(i)
                    .amount());
        }
        long divisor = gcd(parts);
        long sum = 0;
        for (long part : parts) {
            sum += divisor != 0 ? part / divisor : part;
        }
        return sum;
    }

    private static long gcd(long[] values) {
        long result = values[0];
        for (int i = 1; i < values.length; i++) {
            result = gcd(result, values[i]);
        }
        return result;
    }

    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /// The retired `MaterialUtils#getVoltageForTier` table, inlined -- deliberately not [GTValues#VP], which
    /// the retired generator's own tier scaling differs from.
    private static int voltageForTier(int tier) {
        return switch (tier) {
            case 0 -> 16;
            case 1 -> 30;
            case 2 -> 120;
            case 3 -> 480;
            case 4 -> 1920;
            case 5 -> 7680;
            case 6 -> 30720;
            case 7 -> 122880;
            case 8 -> 491520;
            case 9 -> 1966080;
            case 10 -> 7864320;
            case 11 -> 31457280;
            case 12 -> 125829120;
            case 13 -> 503316480;
            case 14 -> 2013265920;
            default -> Integer.MAX_VALUE;
        };
    }
}
