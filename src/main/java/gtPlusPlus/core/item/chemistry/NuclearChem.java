package gtPlusPlus.core.item.chemistry;

import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;

import net.minecraftforge.fluids.Fluid;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class NuclearChem extends ItemPackage {

    public static Fluid Burnt_LiFBeF2ThF4UF4;
    public static Fluid Burnt_LiFBeF2ZrF4UF4;
    public static Fluid Burnt_LiFBeF2ZrF4U235;

    public static Fluid Impure_LiFBeF2;

    public static Fluid GeneticMutagen;
    private static boolean generateMutagenRecipe = false;

    @Override
    public void items() {}

    @Override
    public void blocks() {}

    @Override
    public void fluids() {
        // Create Used Nuclear Fuels
        Burnt_LiFBeF2ThF4UF4 = FluidUtils.generateFluidNonMolten(
                "BurntLiFBeF2ThF4UF4",
                "Burnt LiFBeF2ThF4UF4 Salt",
                545,
                new short[] { 48, 175, 48, 100 },
                null,
                null);
        Burnt_LiFBeF2ZrF4UF4 = FluidUtils.generateFluidNonMolten(
                "BurntLiFBeF2ZrF4UF4",
                "Burnt LiFBeF2ZrF4UF4 Salt",
                520,
                new short[] { 48, 168, 68, 100 },
                null,
                null);
        Burnt_LiFBeF2ZrF4U235 = FluidUtils.generateFluidNonMolten(
                "BurntLiFBeF2ZrF4U235",
                "Burnt LiFBeF2ZrF4U235 Salt",
                533,
                new short[] { 68, 185, 48, 100 },
                null,
                null);
        Impure_LiFBeF2 = FluidUtils.generateFluidNonMolten(
                "ImpureLiFBeF2",
                "Impure Molten Salt Base",
                533,
                new short[] { 110, 75, 186, 100 },
                null,
                null);
        if (FluidUtils.getFluidStack("fluid.Mutagen", 1) == null) {
            GeneticMutagen = FluidUtils.generateFluidNonMolten(
                    "GeneticMutagen",
                    "Genetic Mutagen",
                    12,
                    new short[] { 22, 148, 185, 100 },
                    null,
                    null);
            generateMutagenRecipe = true;
        } else {
            GeneticMutagen = FluidUtils.getFluidStack("fluid.Mutagen", 1).getFluid();
        }
    }

    @Override
    public String errorMessage() {
        return "Bad Nuclear Chemistry Recipes.";
    }

    @Override
    public boolean generateRecipes() {
        if (generateMutagenRecipe) {
            chemReactor_CreateMutagen();
        }
        return true;
    }

    private static void chemReactor_CreateMutagen() {
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 2),
                        GT_Utility.getIntegratedCircuit(20))
                .fluidInputs(FluidUtils.getMobEssence(5000))
                .fluidOutputs(FluidUtils.getFluidStack(GeneticMutagen, 8000)).duration(30 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(UniversalChemical);
    }
}
