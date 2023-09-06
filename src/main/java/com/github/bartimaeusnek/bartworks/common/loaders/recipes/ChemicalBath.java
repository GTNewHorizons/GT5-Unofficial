package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;

public class ChemicalBath implements Runnable {

    @Override
    public void run() {

        for (int i = 0; i < Dyes.dyeBrown.getSizeOfFluidList(); ++i) {

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                    .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 6))
                    .fluidInputs(Dyes.dyeRed.getFluidDye(i, 36)).noFluidOutputs().duration(3 * SECONDS + 4 * TICKS)
                    .eut(2).addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                    .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 7))
                    .fluidInputs(Dyes.dyeGreen.getFluidDye(i, 36)).noFluidOutputs().duration(3 * SECONDS + 4 * TICKS)
                    .eut(2).addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                    .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 8))
                    .fluidInputs(Dyes.dyePurple.getFluidDye(i, 36)).noFluidOutputs().duration(3 * SECONDS + 4 * TICKS)
                    .eut(2).addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                    .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 9))
                    .fluidInputs(Dyes.dyeYellow.getFluidDye(i, 36)).noFluidOutputs().duration(3 * SECONDS + 4 * TICKS)
                    .eut(2).addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                    .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 10))
                    .fluidInputs(Dyes.dyeLime.getFluidDye(i, 36)).noFluidOutputs().duration(3 * SECONDS + 4 * TICKS)
                    .eut(2).addTo(sChemicalBathRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                    .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 11))
                    .fluidInputs(Dyes.dyeBrown.getFluidDye(i, 36)).noFluidOutputs().duration(3 * SECONDS + 4 * TICKS)
                    .eut(2).addTo(sChemicalBathRecipes);

        }

        for (int i = 6; i < 11; i++) {

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, i))
                    .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                    .fluidInputs(Materials.Chlorine.getGas(50)).noFluidOutputs().duration(3 * SECONDS + 4 * TICKS)
                    .eut(2).addTo(sChemicalBathRecipes);

        }
    }
}
