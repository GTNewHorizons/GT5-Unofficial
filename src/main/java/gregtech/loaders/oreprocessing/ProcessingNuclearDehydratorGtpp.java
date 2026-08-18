package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.recipe.RecipeMaps.chemicalDehydratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;

/// The chemical-dehydrator recipe turning a nuclear material's own fluid back into its dust, for every
/// material in [#ELIGIBLE], plus the one hand-written `FluoriteF` acid-leach dehydrator recipe.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingNuclearDehydratorGtpp {

    private ProcessingNuclearDehydratorGtpp() {}

    /// The frozen set of materials that get a dehydrator recipe. `UraniumTetrafluoride`,
    /// `UraniumHexafluoride`, and `ZirconiumTetrafluoride` are deliberately excluded despite otherwise
    /// qualifying.
    // spotless:off
    private static final Set<Material> ELIGIBLE = Set.of(
        Materials.AmmoniumBifluoride, Materials.BerylliumHydroxide,
        Materials.BerylliumFluoride, Materials.LithiumFluoride,
        Materials.ThoriumTetrafluoride, Materials.ThoriumHexafluoride,
        Materials.NeptuniumHexafluoride, Materials.TechnetiumHexafluoride,
        Materials.SeleniumHexafluoride, Materials.LFTRFuel1, Materials.LFTRFuel2,
        Materials.LFTRFuel3);
    // spotless:on

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
        generateFluorite();
    }

    private static void generate(Material material) {
        ItemStack dust = ProcessingDustGeneration.stackOf(OrePrefixes.dust, material, 1L);
        if (dust == null) return;
        FluidStack fluid = MaterialUtils.anyFluid(material, INGOTS);
        if (fluid == null) return;

        long voltage = MaterialUtils.voltageMultiplier(material);
        GTValues.RA.stdBuilder()
            .circuit(20)
            .itemOutputs(dust)
            .fluidInputs(fluid)
            .eut(voltage)
            .duration((int) (10 * (voltage / 5)))
            .addTo(chemicalDehydratorRecipes);
    }

    /// `FluoriteF`'s acid-leach byproduct recipe, hand-listed rather than driven by [#ELIGIBLE].
    private static void generateFluorite() {
        ItemStack input = ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials.FluoriteF, 37L);
        if (input == null) return;

        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(
                ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials.Gypsum, 15L),
                ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials.Silver, 1L),
                ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials.Gold, 2L),
                ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials.Tin, 1L),
                ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials.Copper, 2L))
            .outputChances(10000, 1000, 1000, 3000, 2000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 8000))
            .fluidOutputs(MaterialUtils.fluid(Materials.HydrofluoricAcidGT5U, 16000))
            .eut(RECIPE_HV / 2)
            .duration(10 * MINUTES)
            .addTo(chemicalDehydratorRecipes);
    }
}
