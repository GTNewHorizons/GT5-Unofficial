package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;

public class ChemicalBath implements Runnable {

    @Override
    public void run() {

        for (int i = 0; i < Dyes.dyeBrown.getSizeOfFluidList(); ++i) {
            GT_Values.RA.addChemicalBathRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Dyes.dyeRed.getFluidDye(i, 36),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 6),
                    null,
                    null,
                    null,
                    64,
                    2);
            GT_Values.RA.addChemicalBathRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Dyes.dyeGreen.getFluidDye(i, 36),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 7),
                    null,
                    null,
                    null,
                    64,
                    2);
            GT_Values.RA.addChemicalBathRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Dyes.dyePurple.getFluidDye(i, 36),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 8),
                    null,
                    null,
                    null,
                    64,
                    2);
            GT_Values.RA.addChemicalBathRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Dyes.dyeYellow.getFluidDye(i, 36),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 9),
                    null,
                    null,
                    null,
                    64,
                    2);
            GT_Values.RA.addChemicalBathRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Dyes.dyeLime.getFluidDye(i, 36),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 10),
                    null,
                    null,
                    null,
                    64,
                    2);
            GT_Values.RA.addChemicalBathRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    Dyes.dyeBrown.getFluidDye(i, 36),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 11),
                    null,
                    null,
                    null,
                    64,
                    2);
        }

        for (int i = 6; i < 11; i++) {
            GT_Values.RA.addChemicalBathRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, i),
                    Materials.Chlorine.getGas(50),
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, 0),
                    null,
                    null,
                    null,
                    64,
                    2);
        }
    }
}
