package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.material.MaterialsElements;

public class RecipeLoaderGTNH {

    public static void generate() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(new ItemStack(Items.ender_pearl, 1, 0))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ender"), 250))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        // MK4
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Plutonium241.getMolten(144), Materials.Helium.getGas(1000))
            .fluidOutputs(MaterialsElements.getInstance().CURIUM.getFluidStack(144))
            .duration(4 * SECONDS + 16 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 500_000_000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsElements.getInstance().CURIUM.getFluidStack(144), Materials.Helium.getPlasma(144))
            .fluidOutputs(MaterialsElements.getInstance().CALIFORNIUM.getFluidStack(144))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 750_000_000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Plutonium241.getMolten(144), Materials.Calcium.getPlasma(144))
            .fluidOutputs(Materials.Flerovium.getMolten(144))
            .duration(8 * SECONDS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 1_000_000_000)
            .addTo(fusionRecipes);
    }
}
