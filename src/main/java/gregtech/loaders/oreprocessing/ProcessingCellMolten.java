package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

/// Fluid-extractor melting of a molten-cell material's solid shapes into its molten fluid, plus the molten-cell
/// Forge `FluidContainerRegistry` registration. Dispatched once per cellMolten-generating material by
/// [gregtech.loaders.shapeconsumers.ConsumerCellMolten].
///
/// Ported from the retired bartworks `MoltenCellLoader`. Shape-to-molten extraction is not part of GregTech's
/// own autogen (which only does the reverse, mold solidification of molten into ingot/plate/... -- that stays
/// canonical); it was a werkstoff-only feature. Membership is therefore restricted to werkstoff-derived
/// materials ([GTMaterialProperties#WERKSTOFF_IDS]) so the cutover neither drops it for the werkstoffe that
/// carried it nor grants it to the gregtech/gtPlusPlus molten-cell materials that never had it.
public class ProcessingCellMolten implements IOreRecipeRegistrator {

    public static final ProcessingCellMolten INSTANCE = new ProcessingCellMolten();

    private ProcessingCellMolten() {}

    private static void extract(ItemStack input, Material material, long moltenAmount) {
        if (input == null) return;
        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .fluidOutputs(MU.molten(material, moltenAmount))
            .duration(15 * SECONDS)
            .eut(calculateRecipeEU(material, 2))
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (material.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) return;
        // A cellMolten shape without a resolvable molten fluid (an incomplete fold) has nothing to extract into.
        if (MU.molten(material, INGOTS) == null) return;

        ItemStack ingot = GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L);
        if (ingot == null) {
            ItemStack dust = GTOreDictUnificator.get(OrePrefixes.dust, material, 1L);
            if (dust == null) return;
            extract(dust, material, INGOTS);
            extract(GTOreDictUnificator.get(OrePrefixes.dustSmall, material, 1L), material, QUARTER_INGOTS);
            extract(GTOreDictUnificator.get(OrePrefixes.dustTiny, material, 1L), material, NUGGETS);
        } else {
            extract(ingot, material, INGOTS);
            extract(GTOreDictUnificator.get(OrePrefixes.nugget, material, 1L), material, NUGGETS);
            if (GTOreDictUnificator.get(OrePrefixes.plate, material, 1L) != null) {
                extract(GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1L), material, INGOTS);
                extract(GTOreDictUnificator.get(OrePrefixes.plate, material, 1L), material, INGOTS);
                extract(GTOreDictUnificator.get(OrePrefixes.stick, material, 1L), material, HALF_INGOTS);
            }
        }

        FluidContainerRegistry.registerFluidContainer(
            MU.molten(material, INGOTS),
            GTUtility.copyAmount(1, stack),
            GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 1));
    }
}
