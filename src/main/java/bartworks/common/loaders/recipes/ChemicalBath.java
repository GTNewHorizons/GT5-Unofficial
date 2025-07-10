package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;

public class ChemicalBath implements Runnable {

    @Override
    public void run() {

        for (Fluid dyeFluid : Dyes.dyeRed.getFluidDyes()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 6))
                .fluidInputs(new FluidStack(dyeFluid, 36))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(2)
                .addTo(chemicalBathRecipes);
        }

        for (Fluid dyeFluid : Dyes.dyeGreen.getFluidDyes()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 7))
                .fluidInputs(new FluidStack(dyeFluid, 36))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(2)
                .addTo(chemicalBathRecipes);
        }

        for (Fluid dyeFluid : Dyes.dyePurple.getFluidDyes()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 8))
                .fluidInputs(new FluidStack(dyeFluid, 36))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(2)
                .addTo(chemicalBathRecipes);
        }

        for (Fluid dyeFluid : Dyes.dyeYellow.getFluidDyes()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 9))
                .fluidInputs(new FluidStack(dyeFluid, 36))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(2)
                .addTo(chemicalBathRecipes);
        }

        for (Fluid dyeFluid : Dyes.dyeLime.getFluidDyes()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 10))
                .fluidInputs(new FluidStack(dyeFluid, 36))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(2)
                .addTo(chemicalBathRecipes);
        }

        for (Fluid dyeFluid : Dyes.dyeBrown.getFluidDyes()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 11))
                .fluidInputs(new FluidStack(dyeFluid, 36))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(2)
                .addTo(chemicalBathRecipes);
        }

        for (int i = 6; i < 11; i++) {

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, i))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .fluidInputs(Materials.Chlorine.getGas(50))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(2)
                .addTo(chemicalBathRecipes);

        }
    }
}
