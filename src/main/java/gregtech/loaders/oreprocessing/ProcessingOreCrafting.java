package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.util.GTModHandler;

/// Reproduces the retired gtPlusPlus `RecipeGenOre`'s crafting-table recipes for every material in [#ELIGIBLE]:
/// the three hard-hammer shaped conversions (crushed/crushedPurified/crushedCentrifuged into their next dust
/// stage without a machine) and the four dust/dustSmall/dustTiny size conversions [ProcessingDustGeneration]
/// registers for its own, disjoint material list. [ProcessingOreMachine] covers the retired generator's
/// machine recipes (macerator, ore-washer, thermal centrifuge, forge hammer, centrifuge,
/// electrolyzer/chemical-dehydrator) -- these crafting-table ones have no canonical ore-autogen counterpart
/// at all, machine or otherwise, and carry no byproduct of their own, so they were unaffected by that class's
/// property-based approach failing the recipe census (see its class javadoc).
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes`, the exact drain point the retired
/// generator's own `run()` used (queued through the retired gtPlusPlus `MaterialGenerator#mRecipeMapsToGenerate`)
/// -- see [ProcessingDustGeneration]'s class javadoc for why the timing matters. [#generateFluoriteChemicalBath]
/// reproduces `RecipeGenOre`'s one material-specific recipe, gated there the same way (`material ==
/// MaterialsFluorides.FLUORITE`) rather than through [#ELIGIBLE] membership.
public class ProcessingOreCrafting {

    private ProcessingOreCrafting() {}

    /// Every material the retired `RecipeGenOre` reached, through either `MaterialGenerator#generateOreMaterial`
    /// or `#generateOreMaterialWithAllExcessComponents` -- unlike [ProcessingOreMachine]'s electrolyzer/
    /// chemical-dehydrator eligibility, this crafting section ran unconditionally for both, so it is not split
    /// by `disableOptional`.
    // spotless:off
    private static final Set<Material> ELIGIBLE = Set.of(
        Materials2Materials.AgarditeCd, Materials2Materials.AgarditeLa, Materials2Materials.AgarditeNd,
        Materials2Materials.AgarditeY, Materials2Materials.Alburnite, Materials2Materials.AncientGranite,
        Materials2Materials.BariteRa, Materials2Materials.Cerite, Materials2Materials.Comancheite,
        Materials2Materials.Crocoite, Materials2Materials.CryoliteF, Materials2Materials.DemicheleiteBr,
        Materials2Materials.Florencite, Materials2Materials.Fluorcaphite, Materials2Materials.FluoriteF,
        Materials2Materials.GadoliniteCe, Materials2Materials.GadoliniteY, Materials2Materials.Geikielite,
        Materials2Materials.Greenockite, Materials2Materials.Hibonite, Materials2Materials.Honeaite,
        Materials2Materials.Irarsite, Materials2Materials.Kashinite, Materials2Materials.Koboldite,
        Materials2Materials.Lafossaite, Materials2Materials.LanthaniteCe, Materials2Materials.LanthaniteLa,
        Materials2Materials.LanthaniteNd, Materials2Materials.Lautarite, Materials2Materials.Lepersonnite,
        Materials2Materials.Miessiite, Materials2Materials.Nichromite, Materials2Materials.Perroudite,
        Materials2Materials.Polycrase, Materials2Materials.RadioactiveMineralMix, Materials2Materials.RareEarthI,
        Materials2Materials.RareEarthII, Materials2Materials.RareEarthIII, Materials2Materials.Runite,
        Materials2Materials.SamarskiteY, Materials2Materials.SamarskiteYb, Materials2Materials.Titanite,
        Materials2Materials.Xenotime, Materials2Materials.Yttriaite, Materials2Materials.Yttrialite,
        Materials2Materials.Yttrocerite, Materials2Materials.Zimbabweite, Materials2Materials.Zircon,
        Materials2Materials.Zirconolite, Materials2Materials.Zircophyllite, Materials2Materials.Zirkelite);
    // spotless:on

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
        generateFluoriteChemicalBath();
    }

    private static void generate(Material material) {
        ItemStack crushed = ProcessingDustGeneration.stackOf(OrePrefixes.crushed, material, 1L);
        ItemStack crushedPurified = ProcessingDustGeneration.stackOf(OrePrefixes.crushedPurified, material, 1L);
        ItemStack crushedCentrifuged = ProcessingDustGeneration.stackOf(OrePrefixes.crushedCentrifuged, material, 1L);
        ItemStack dustImpure = ProcessingDustGeneration.stackOf(OrePrefixes.dustImpure, material, 1L);
        ItemStack dustPure = ProcessingDustGeneration.stackOf(OrePrefixes.dustPure, material, 1L);
        ItemStack dust = ProcessingDustGeneration.stackOf(OrePrefixes.dust, material, 1L);

        if (crushedPurified != null && dustPure != null) {
            GTModHandler.addCraftingRecipe(
                dustPure,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "h  ", "P  ", "   ", 'P', crushedPurified });
        }
        if (crushed != null && dustImpure != null) {
            GTModHandler.addCraftingRecipe(
                dustImpure,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "h  ", "C  ", "   ", 'C', crushed });
        }
        if (crushedCentrifuged != null && dust != null) {
            GTModHandler.addCraftingRecipe(
                dust,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "h  ", "C  ", "   ", 'C', crushedCentrifuged });
        }

        if (dust == null) return;

        ItemStack tinyDust = ProcessingDustGeneration.stackOf(OrePrefixes.dustTiny, material, 1L);
        if (tinyDust != null) {
            GTModHandler.addCraftingRecipe(
                dust,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "TTT", "TTT", "TTT", 'T', tinyDust });
            GTModHandler.addCraftingRecipe(
                ProcessingDustGeneration.stackOf(OrePrefixes.dustTiny, material, 9L),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "D  ", "   ", "   ", 'D', dust });
        }

        ItemStack smallDust = ProcessingDustGeneration.stackOf(OrePrefixes.dustSmall, material, 1L);
        if (smallDust != null) {
            GTModHandler.addCraftingRecipe(
                dust,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SS ", "SS ", "   ", 'S', smallDust });
            GTModHandler.addCraftingRecipe(
                ProcessingDustGeneration.stackOf(OrePrefixes.dustSmall, material, 4L),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { " D ", "   ", "   ", 'D', dust });
        }
    }

    private static void generateFluoriteChemicalBath() {
        GTValues.RA.stdBuilder()
            .itemInputs(ProcessingDustGeneration.stackOf(OrePrefixes.crushed, Materials2Materials.FluoriteF, 1L))
            .itemOutputs(
                ProcessingDustGeneration.stackOf(OrePrefixes.crushedPurified, Materials2Materials.FluoriteF, 4L),
                ProcessingDustGeneration.stackOf(OrePrefixes.dustImpure, Materials2Materials.FluoriteF, 2L),
                ProcessingDustGeneration.stackOf(OrePrefixes.dustPure, Materials2Materials.FluoriteF, 1L))
            .outputChances(10000, 5000, 1000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, 1000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(chemicalBathRecipes);
    }
}
