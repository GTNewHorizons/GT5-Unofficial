package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;

public class FluidSolidifier implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.lapis_block))
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Iron, Materials2FluidShapes.fluidMolten, (int) (9 * INGOTS)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Titanium, Materials2FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.TungstenSteel,
                    Materials2FluidShapes.fluidMolten,
                    (int) (8 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 3))
            .fluidInputs(WerkstoffLoader.RhodiumPlatedPalladium.getMolten(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Iridium, Materials2FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Osmium, Materials2FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Neutronium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (8 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 7))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 8))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.TranscendentMetal,
                    Materials2FluidShapes.fluidMolten,
                    (int) (8 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(fluidSolidifierRecipes);

        // Hexanite Borosilicate Glass is in Forming Press Recipes

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 10))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Universium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (8 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(fluidSolidifierRecipes);

    }
}
