package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
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

        // MK3
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Plutonium241.getMolten(1 * INGOTS), Materials.Helium.getGas(1_000))
            .fluidOutputs(MaterialsElements.getInstance().CURIUM.getFluidStack(1 * INGOTS))
            .duration(4 * SECONDS + 16 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes);

        // MK4
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsElements.getInstance().CURIUM.getFluidStack(1 * INGOTS),
                Materials.Helium.getPlasma(1 * INGOTS))
            .fluidOutputs(MaterialsElements.getInstance().CALIFORNIUM.getFluidStack(1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 750_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Plutonium241.getMolten(1 * INGOTS), Materials.Calcium.getPlasma(1 * INGOTS))
            .fluidOutputs(Materials.Flerovium.getMolten(1 * INGOTS))
            .duration(8 * SECONDS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Manganese.getMolten(1 * INGOTS), WerkstoffLoader.Neon.getFluidOrGas(500))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().BROMINE.getPlasma(), 1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.Fluorine.getGas(1_000),
                MaterialsElements.getInstance().SELENIUM.getFluidStack(1 * INGOTS))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().TECHNETIUM.getPlasma(), 2 * INGOTS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 800_000_000L)
            .addTo(fusionRecipes);
    }
}
