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

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;

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
                MaterialLibAPI.getFluidStack(Materials.Plutonium241, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.Curium, 1 * INGOTS))
            .duration(4 * SECONDS + 16 * TICKS)
            .eut(98304)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes);

        // MK4
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.Curium, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.Californium, 1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 750_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Plutonium241, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Calcium, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .fluidOutputs(MaterialUtils.molten(Materials.FleroviumGT5U, 1 * INGOTS))
            .duration(8 * SECONDS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Manganese, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Neon, FluidShapes.fluidLiquid, 500))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Bromine), 1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 1_000),
                MaterialUtils.anyFluid(Materials.Selenium, 1 * INGOTS))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Technetium), 2 * INGOTS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(196608)
            .metadata(FUSION_THRESHOLD, 800_000_000L)
            .addTo(fusionRecipes);
    }
}
