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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

/// Reproduces the retired gtPlusPlus `RecipeGenOre`'s macerator, ore-washer, thermal-centrifuge, forge-hammer,
/// centrifuge, and electrolyzer/chemical-dehydrator recipes for every material in
/// [#ELIGIBLE_STANDARD]/[#ELIGIBLE_NO_OPTIONAL], reading the same legacy `gtPlusPlus.core.material.Material`
/// API the retired generator did (`Material#getComposites`/`#hasSolidForm`/`#getState`/`#getOre` and friends)
/// rather than re-deriving byproducts from `Materials2Materials`'s own properties.
///
/// A property-based approach (populating `GTMaterialProperties#ORE_BYPRODUCTS` from the same bonus-selection
/// algorithm, letting the canonical `ProcessingOre`/`ProcessingDirty`/`ProcessingCrushedOre`/`ProcessingPure`/
/// `ProcessingDust` autogen produce the recipes) was tried and failed the recipe census: that autogen already
/// runs unconditionally for any registered ore/crushed/dust item, with its own EU/duration formulas and its
/// own per-recipe-shape convention for which byproduct-list slot each shape reads -- a convention that
/// disagrees with the retired generator's own bonusA-vs-bonusB choice for several shapes (its "purified" chain
/// mostly reads bonusA where `RecipeGenOre` used bonusB and vice versa), so populating real byproduct data
/// changed the canonical autogen's own recipes rather than reproducing the retired generator's. This class
/// registers the retired generator's recipes as their own entries instead, coexisting with the canonical
/// autogen's (self-looped, byproduct-less) recipes for the same materials exactly as the pre-retirement tree
/// did -- including hammer recipes, whose shape happens to already match the canonical autogen's: the census
/// counts registered instances, not just distinct shapes, so the retired generator's copy is still needed to
/// restore the original duplicate count.
///
/// The shaped-crafting recipes (hard-hammer conversions, tiny/small dust conversions) and the
/// `MaterialsFluorides#FLUORITE`-specific chemical-bath recipe carry no byproduct and live in
/// [ProcessingOreCrafting] instead, unaffected by any of the above.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes`, the exact drain point the retired
/// generator's own `run()` used (queued through `gtPlusPlus.core.material.MaterialGenerator
/// #mRecipeMapsToGenerate`) -- see [ProcessingDustGeneration]'s class javadoc for why the timing matters.
public class ProcessingOreMachine {

    private ProcessingOreMachine() {}

    /// Every material the retired `RecipeGenOre` reached through `MaterialGenerator#generateOreMaterial`
    /// (`disableOptional=false`) -- the electrolyzer/chemical-dehydrator branch in [#generate] runs only for
    /// these.
    // spotless:off
    private static final Set<Material> ELIGIBLE_STANDARD = Set.of(
        MaterialsOres.CROCROITE, MaterialsOres.GEIKIELITE, MaterialsOres.NICHROMITE, MaterialsOres.TITANITE,
        MaterialsOres.ZIMBABWEITE, MaterialsOres.ZIRCONILITE, MaterialsOres.GADOLINITE_CE, MaterialsOres.GADOLINITE_Y,
        MaterialsOres.LEPERSONNITE, MaterialsOres.SAMARSKITE_Y, MaterialsOres.SAMARSKITE_YB, MaterialsOres.XENOTIME,
        MaterialsOres.YTTRIAITE, MaterialsOres.YTTRIALITE, MaterialsOres.YTTROCERITE, MaterialsOres.ZIRCON,
        MaterialsOres.POLYCRASE, MaterialsOres.ZIRCOPHYLLITE, MaterialsOres.ZIRKELITE, MaterialsOres.LANTHANITE_LA,
        MaterialsOres.LANTHANITE_CE, MaterialsOres.LANTHANITE_ND, MaterialsOres.AGARDITE_Y, MaterialsOres.AGARDITE_CD,
        MaterialsOres.AGARDITE_LA, MaterialsOres.AGARDITE_ND, MaterialsOres.HIBONITE, MaterialsOres.CERITE,
        MaterialsOres.FLUORCAPHITE, MaterialsOres.FLORENCITE, MaterialsOres.CRYOLITE, MaterialsOres.LAUTARITE,
        MaterialsOres.LAFOSSAITE, MaterialsOres.DEMICHELEITE_BR, MaterialsOres.COMANCHEITE, MaterialsOres.PERROUDITE,
        MaterialsOres.HONEAITE, MaterialsOres.ALBURNITE, MaterialsOres.MIESSIITE, MaterialsOres.KASHINITE,
        MaterialsOres.IRARSITE, MaterialsOres.GREENOCKITE, MaterialsOres.RADIOBARITE,
        MaterialsOres.DEEP_EARTH_REACTOR_FUEL_DEPOSIT, MaterialMisc.RARE_EARTH_LOW, MaterialMisc.RARE_EARTH_MID,
        MaterialMisc.RARE_EARTH_HIGH, MaterialsFluorides.FLUORITE, MaterialsAlloy.KOBOLDITE);
    // spotless:on

    /// Every material the retired `RecipeGenOre` reached through `MaterialGenerator
    /// #generateOreMaterialWithAllExcessComponents` (`disableOptional=true`) -- everything in [#generate]
    /// except the electrolyzer/chemical-dehydrator branch.
    private static final Set<Material> ELIGIBLE_NO_OPTIONAL = Set
        .of(MaterialsElements.STANDALONE.GRANITE, MaterialsElements.STANDALONE.RUNITE);

    private static Material mStone;

    public static void run() {
        if (mStone == null) {
            mStone = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Stone);
        }
        for (Material material : ELIGIBLE_STANDARD) {
            generate(material, false);
        }
        for (Material material : ELIGIBLE_NO_OPTIONAL) {
            generate(material, true);
        }
    }

    private static void generate(final Material material, final boolean disableOptional) {
        int tVoltageMultiplier = MaterialUtils.getVoltageForTier(material.vTier);
        final ItemStack dustStone = ItemUtils.getItemStackOfAmountFromOreDict("dustStone", 1);

        Material bonusA = null;
        Material bonusB = null;

        // Setup Bonuses
        ArrayList<Material> aMatComp = new ArrayList<>(MaterialUtils.getCompoundMaterialsRecursively(material));
        if (aMatComp.size() < 3) {
            while (aMatComp.size() < 3) {
                aMatComp.add(material);
            }
        }

        final ArrayList<Material> amJ = new ArrayList<>();
        for (Material g : aMatComp) {
            if (g.hasSolidForm()) {
                amJ.add(g);
                if (amJ.size() >= 2) break;
            }
        }

        boolean allFailed = false;
        final ArrayList<MaterialStack> composites = material.getComposites();
        if (amJ.size() < 2) {
            allFailed = true;
            if (!composites.isEmpty() && composites.get(0) != null) {
                bonusA = composites.get(0)
                    .getStackMaterial();
            } else {
                bonusA = material;
            }

            // If Secondary Output has no solid output, try the third (If it exists), then the fourth/fifth
            for (byte i = 1; i < Math.min(composites.size(), 5); i++) {
                if (composites.get(i) == null) break;
                bonusB = composites.get(i)
                    .getStackMaterial();
                if (bonusB != null && bonusB.hasSolidForm()) {
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
        for (MaterialStack r : composites) {
            if (r != null) {
                componentMap.add(Pair.of(r.getPartsPerOneHundred(), r.getStackMaterial()));
            }
        }

        if (bonusA == null || !bonusA.hasSolidForm()) {
            bonusA = mStone;
        }
        if (bonusB == null || !bonusB.hasSolidForm()) {
            bonusB = mStone;
        }

        ItemStack matDust = getDust(material);
        ItemStack matDustA = getDust(bonusA);
        ItemStack matDustB = getDust(bonusB);

        // Macerate ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(material.getOre(1))
            .itemOutputs(material.getCrushed(2), matDustA, dustStone)
            .outputChances(100_00, 10_00, 50_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate raw ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(material.getRawOre(1))
            .itemOutputs(material.getCrushed(2), matDustA, dustStone)
            .outputChances(100_00, 5_00, 50_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate Crushed to Impure Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getDustImpure(1), matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate Washed to Purified Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getDustPurified(1), matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate Centrifuged to Pure Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedCentrifuged(1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Wash
        RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getCrushedPurified(1), matDustA, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getCrushedPurified(1), matDustA, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTModHandler.getDistilledWater(200))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        // Thermal Centrifuge
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getCrushedCentrifuged(1), matDustB, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getCrushedCentrifuged(1), matDustA, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        // Forge Hammer
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedCentrifuged(1))
            .itemOutputs(matDust)
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getDustImpure(1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getDustPurified(1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getOre(1))
            .itemOutputs(material.getCrushed(1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        // Centrifuge
        GTValues.RA.stdBuilder()
            .itemInputs(material.getDustPurified(1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 11_11)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, material.getMass() * 8L))
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getDustImpure(1))
            .itemOutputs(matDust, matDustB)
            .outputChances(100_00, 11_11)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, material.getMass() * 8L))
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
            boolean nonSolid = dehydrator ? f.getValue()
                .getState() != MaterialState.SOLID
                && f.getValue()
                    .getState() != MaterialState.ORE
                : f.getValue()
                    .getState() != MaterialState.SOLID;
            if (nonSolid) {
                mInternalOutputs[mCounter++] = f.getValue()
                    .getCell(f.getKey());
                mCellCount += f.getKey();
                mTotalCount += f.getKey();
            } else {
                mInternalOutputs[mCounter++] = f.getValue()
                    .getDust(f.getKey());
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

        ItemStack mainDust = material.getDust(material.smallestStackSizeWhenProcessing);
        if (mainDust == null) {
            mainDust = material.getDust(mTotalCount);
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
            recipe.duration((int) Math.max(material.getMass() * 4L, 1))
                .addTo(chemicalDehydratorRecipes);
        } else {
            recipe.duration((int) Math.max(material.getMass() * 3L, 1))
                .addTo(electrolyzerRecipes);
        }
    }

    private static ItemStack getDust(Material m) {
        ItemStack x = m.getDust(1);
        if (x == null) {
            x = mStone.getDust(1);
        }
        return x;
    }
}
