package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;

/// Reproduces the retired gtPlusPlus `MaterialGenerator#generateNuclearDusts`'s chemical-dehydrator recipe (a
/// material's own fluid dehydrating back into its dust) for every material in [#ELIGIBLE], plus the one
/// hand-written `FluoriteF` acid-leach dehydrator recipe `MaterialGenerator#generateOreMaterial` carried
/// alongside it.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingNuclearDehydratorGtpp {

    private ProcessingNuclearDehydratorGtpp() {}

    /// Every material `generateNuclearDusts` reached with its `generateDehydratorRecipe` parameter left at the
    /// default `true` (`ModItems#runMaterialGenerator`): `UraniumTetrafluoride`/`UraniumHexafluoride` passed
    /// `false` there and are excluded, and `ZirconiumTetrafluoride` -- also left at the default -- is excluded
    /// too, since its fluid registration ran after this recipe's `matInfo.getFluid() != null` gate in the
    /// retired call order, so gtpp itself never actually registered a recipe for it.
    // spotless:off
    private static final Set<Material> ELIGIBLE = Set.of(
        Materials2Materials.AmmoniumBifluoride, Materials2Materials.BerylliumHydroxide,
        Materials2Materials.BerylliumFluoride, Materials2Materials.LithiumFluoride,
        Materials2Materials.ThoriumTetrafluoride, Materials2Materials.ThoriumHexafluoride,
        Materials2Materials.NeptuniumHexafluoride, Materials2Materials.TechnetiumHexafluoride,
        Materials2Materials.SeleniumHexafluoride, Materials2Materials.LFTRFuel1, Materials2Materials.LFTRFuel2,
        Materials2Materials.LFTRFuel3);
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
        FluidStack fluid = MU.legacyGtppFluid(material, INGOTS);
        if (fluid == null) return;

        long voltage = voltageMultiplier(material);
        GTValues.RA.stdBuilder()
            .circuit(20)
            .itemOutputs(dust)
            .fluidInputs(fluid)
            .eut(voltage)
            .duration((int) (10 * (voltage / 5)))
            .addTo(chemicalDehydratorRecipes);
    }

    /// `FluoriteF`'s acid-leach byproduct recipe -- not membership-driven like [#ELIGIBLE], since gtpp
    /// hand-wrote this one recipe rather than deriving it from the material's own fluid.
    private static void generateFluorite() {
        ItemStack input = ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials2Materials.FluoriteF, 37L);
        if (input == null) return;

        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(
                ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials2Materials.Gypsum, 15L),
                ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials2Materials.Silver, 1L),
                ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials2Materials.Gold, 2L),
                ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials2Materials.Tin, 1L),
                ProcessingDustGeneration.stackOf(OrePrefixes.dust, Materials2Materials.Copper, 2L))
            .outputChances(10000, 1000, 1000, 3000, 2000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 8000))
            .fluidOutputs(MU.fluid(Materials2Materials.HydrofluoricAcidGT5U, 16000))
            .eut(RECIPE_HV / 2)
            .duration(10 * MINUTES)
            .addTo(chemicalDehydratorRecipes);
    }

    private static long voltageMultiplier(Material material) {
        Long multiplier = material.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER);
        return multiplier != null ? multiplier : 16L;
    }
}
