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
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialRefStack;
import gregtech.api.material.MaterialUtils;
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
/// The shaped-crafting and [Materials#FluoriteF] chemical-bath recipes carry no byproduct and live
/// in [ProcessingOreCrafting].
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see [ProcessingDustGeneration]'s
/// class javadoc for why that timing matters.
public class ProcessingOreMachine {

    private ProcessingOreMachine() {}

    /// The frozen set whose members get the electrolyzer/chemical-dehydrator branch in [#generate]. Together
    /// with [#ELIGIBLE_NO_OPTIONAL] this is the same 51-material union [ProcessingOreCrafting#ELIGIBLE]
    /// declares for its own crafting recipes.
    // spotless:off
    private static final Set<Material> ELIGIBLE_STANDARD = Set.of(
        Materials.Crocoite, Materials.Geikielite, Materials.Nichromite,
        Materials.Titanite, Materials.Zimbabweite, Materials.Zirconolite,
        Materials.GadoliniteCe, Materials.GadoliniteY, Materials.Lepersonnite,
        Materials.SamarskiteY, Materials.SamarskiteYb, Materials.Xenotime,
        Materials.Yttriaite, Materials.Yttrialite, Materials.Yttrocerite,
        Materials.Zircon, Materials.Polycrase, Materials.Zircophyllite,
        Materials.Zirkelite, Materials.LanthaniteLa, Materials.LanthaniteCe,
        Materials.LanthaniteNd, Materials.AgarditeY, Materials.AgarditeCd,
        Materials.AgarditeLa, Materials.AgarditeNd, Materials.Hibonite,
        Materials.Cerite, Materials.Fluorcaphite, Materials.Florencite,
        Materials.CryoliteF, Materials.Lautarite, Materials.Lafossaite,
        Materials.DemicheleiteBr, Materials.Comancheite, Materials.Perroudite,
        Materials.Honeaite, Materials.Alburnite, Materials.Miessiite,
        Materials.Kashinite, Materials.Irarsite, Materials.Greenockite,
        Materials.BariteRa, Materials.RadioactiveMineralMix, Materials.RareEarthI,
        Materials.RareEarthII, Materials.RareEarthIII, Materials.FluoriteF,
        Materials.Koboldite);
    // spotless:on

    /// The frozen set whose members get everything in [#generate] except the electrolyzer/chemical-dehydrator
    /// branch.
    private static final Set<Material> ELIGIBLE_NO_OPTIONAL = Set.of(Materials.AncientGranite, Materials.Runite);

    private static final Material mStone = Materials.Stone;

    public static void run() {
        for (Material material : ELIGIBLE_STANDARD) {
            generate(material, false);
        }
        for (Material material : ELIGIBLE_NO_OPTIONAL) {
            generate(material, true);
        }
    }

    private static void generate(final Material material, final boolean disableOptional) {
        int tVoltageMultiplier = voltageForTier(MaterialUtils.tier(material));
        final ItemStack dustStone = ProcessingDustGeneration.stackOf(OrePrefixes.dust, mStone, 1);

        Material bonusA = null;
        Material bonusB = null;

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
                if (hasSolidForm(bonusB)) {
                    allFailed = false;
                    break;
                }
            }
        } else {
            bonusA = amJ.get(0);
            bonusB = amJ.get(1);
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

        if (!hasSolidForm(bonusA)) {
            bonusA = mStone;
        }
        if (!hasSolidForm(bonusB)) {
            bonusB = mStone;
        }

        ItemStack matDust = getDust(material);
        ItemStack matDustA = getDust(bonusA);
        ItemStack matDustB = getDust(bonusB);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.ore, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 2), matDustA, dustStone)
            .outputChances(100_00, 10_00, 50_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.rawOre, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 2), matDustA, dustStone)
            .outputChances(100_00, 5_00, 50_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.dustImpure, material, 1), matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushedPurified, material, 1))
            .itemOutputs(ProcessingDustGeneration.stackOf(OrePrefixes.dustPure, material, 1), matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushedCentrifuged, material, 1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

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

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.dustPure, material, 1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 11_11)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, MaterialUtils.mass(material) * 8L))
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.dustImpure, material, 1))
            .itemOutputs(matDust, matDustB)
            .outputChances(100_00, 11_11)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, MaterialUtils.mass(material) * 8L))
            .addTo(centrifugeRecipes);

        if (!disableOptional) {
            if (!componentMap.isEmpty() && componentMap.size() <= 6) {
                registerDecomposition(material, componentMap, 6, false, tVoltageMultiplier);
            } else if (componentMap.size() > 6 && componentMap.size() <= 9) {
                registerDecomposition(material, componentMap, 9, true, tVoltageMultiplier);
            }
        }
    }

    /// The electrolyzer (`slots=6`)/chemical-dehydrator (`slots=9`) decomposition recipe: one dust or
    /// empty-cell-filling item per composite part (a cell for a non-`SOLID` part; the dehydrator branch counts
    /// `ORE` as solid too), all at 100% chance, input dust sized to the composition's GCD-reduced total
    /// (falling back to the raw total, then skipping the recipe if neither resolves).
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
                mInternalOutputs[mCounter++] = MaterialParts.cell(f.getValue(), f.getKey());
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
            recipe.duration((int) Math.max(MaterialUtils.mass(material) * 4L, 1))
                .addTo(chemicalDehydratorRecipes);
        } else {
            recipe.duration((int) Math.max(MaterialUtils.mass(material) * 3L, 1))
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
    /// decompose further ([Materials#Fluorcaphite], the rare earth mixes) are all members of
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
                    toCheck.add(
                        entry.material()
                            .resolve());
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
        Materials.AgarditeCd, Materials.AgarditeLa, Materials.AgarditeNd,
        Materials.AgarditeY, Materials.Alburnite, Materials.AncientGranite,
        Materials.BariteRa, Materials.BlackMetal, Materials.Cerite,
        Materials.Comancheite, Materials.Crocoite, Materials.CryoliteF,
        Materials.DemicheleiteBr, Materials.Florencite, Materials.Fluorcaphite,
        Materials.FluoriteF, Materials.GadoliniteCe, Materials.GadoliniteY,
        Materials.Geikielite, Materials.Greenockite, Materials.Hibonite,
        Materials.Honeaite, Materials.Irarsite, Materials.Kashinite,
        Materials.Koboldite, Materials.Lafossaite, Materials.LanthaniteCe,
        Materials.LanthaniteLa, Materials.LanthaniteNd, Materials.Lautarite,
        Materials.Lepersonnite, Materials.Miessiite, Materials.Nichromite,
        Materials.Perroudite, Materials.Polycrase, Materials.RadioactiveMineralMix,
        Materials.RareEarthI, Materials.RareEarthII, Materials.RareEarthIII,
        Materials.SamarskiteY, Materials.SamarskiteYb, Materials.StrontiumOxide,
        Materials.Titanite, Materials.Xenotime, Materials.Yttriaite,
        Materials.Yttrialite, Materials.Yttrocerite, Materials.Zimbabweite,
        Materials.Zircon, Materials.Zirconolite, Materials.Zircophyllite,
        Materials.Zirkelite);
    // spotless:on

    /// `material`'s state, taken from [GTMaterialProperties#GTPP_STATE] when set and otherwise derived from
    /// which fluid it carries: a molten or solid fluid means `SOLID`, else a liquid fluid means `LIQUID`, else
    /// a gas means `GAS`, else `SOLID`. Package-visible: [ProcessingMaterialDecompositionGtpp] shares this
    /// same state derivation for its own composite-part decomposition.
    static String gtppState(Material material) {
        String state = material.getProperty(GTMaterialProperties.GTPP_STATE);
        if (state != null) return state;
        if (MaterialUtils.molten(material, 1) != null || MaterialUtils.solid(material, 1) != null) return "SOLID";
        if (MaterialUtils.fluid(material, 1) != null) return "LIQUID";
        if (MaterialUtils.gas(material, 1) != null) return "GAS";
        return "SOLID";
    }

    /// Whether a material counts as solid here: its `dust`, `block`, `dustTiny`, and `dustSmall` shapes must
    /// all resolve. Deliberately not a [#gtppState] stand-in -- a material can carry `GTPP_STATE == SOLID`
    /// while missing one of these four shapes, and vice versa, and these recipes follow the shapes.
    private static boolean hasSolidForm(Material material) {
        return ProcessingDustGeneration.stackOf(OrePrefixes.dust, material, 1) != null
            && ProcessingDustGeneration.stackOf(OrePrefixes.block, material, 1) != null
            && ProcessingDustGeneration.stackOf(OrePrefixes.dustTiny, material, 1) != null
            && ProcessingDustGeneration.stackOf(OrePrefixes.dustSmall, material, 1) != null;
    }

    private static long partsPerOneHundred(long amount) {
        return amount >= 1 && amount <= 100 ? amount : 100;
    }

    /// `material`'s own [GTMaterialProperties#COMPOSITION] parts-per-hundred, GCD-reduced and summed.
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

    /// The tier-to-voltage table these recipes scale by -- deliberately not [GTValues#VP], whose ladder
    /// differs.
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
