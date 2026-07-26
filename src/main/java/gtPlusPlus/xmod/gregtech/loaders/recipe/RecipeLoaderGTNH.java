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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;

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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Plutonium241,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MU.legacyGtppFluid(Materials2Materials.Curium, 1 * INGOTS))
            .duration(4 * SECONDS + 16 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes);

        // MK4
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MU.legacyGtppFluid(Materials2Materials.Curium, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .fluidOutputs(MU.legacyGtppFluid(Materials2Materials.Californium, 1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 750_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Plutonium241,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Calcium, Materials2FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .fluidOutputs(MU.molten(Materials2Materials.FleroviumGT5U, 1 * INGOTS))
            .duration(8 * SECONDS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Manganese,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)),
                WerkstoffLoader.Neon.getFluidOrGas(500))
            .fluidOutputs(new FluidStack(MU.legacyGtppPlasmaOf(Materials2Materials.Bromine), 1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MU.legacyGtppFluid(Materials2Materials.Selenium, 1 * INGOTS))
            .fluidOutputs(new FluidStack(MU.legacyGtppPlasmaOf(Materials2Materials.Technetium), 2 * INGOTS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 800_000_000L)
            .addTo(fusionRecipes);
    }
}
