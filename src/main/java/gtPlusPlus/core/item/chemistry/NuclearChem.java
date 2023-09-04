package gtPlusPlus.core.item.chemistry;

import net.minecraft.init.Items;
import net.minecraftforge.fluids.Fluid;

import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

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
            chemReator_CreateMutagen();
        }
        return true;
    }

    private static void chemReator_CreateMutagen() {
        CORE.RA.addChemicalRecipe(
                CI.getNumberedCircuit(20),
                ItemUtils.getSimpleStack(Items.nether_star, 2),
                FluidUtils.getMobEssence(5000),
                FluidUtils.getFluidStack(GeneticMutagen, 8000),
                null,
                30 * 20,
                500);
    }

}
