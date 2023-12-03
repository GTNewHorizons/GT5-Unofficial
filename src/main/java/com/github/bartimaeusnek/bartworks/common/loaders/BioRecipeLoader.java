/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.loaders;

import static gregtech.api.enums.Mods.CropsPlusPlus;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class BioRecipeLoader {

    public static void run() {
        registerWaterBasedBioLabIncubations();
        registerBacterialVatRecipes();
    }

    public static void registerWaterBasedBioLabIncubations() {
        FluidStack[] easyFluids = { Materials.Water.getFluid(1000L),
                FluidRegistry.getFluidStack("ic2distilledwater", 1000) };
        for (FluidStack fluidStack : easyFluids) {

            BWRecipes.instance.addBioLabRecipeIncubation(
                    new ItemStack(Items.rotten_flesh),
                    BioCultureLoader.rottenFleshBacteria,
                    new int[] { 3300 },
                    new FluidStack[] { fluidStack },
                    500,
                    (int) TierEU.RECIPE_HV,
                    BW_Util.STANDART);

            BWRecipes.instance.addBioLabRecipeIncubation(
                    new ItemStack(Items.fermented_spider_eye),
                    BioCultureLoader.eColi,
                    new int[] { 4500 },
                    new FluidStack[] { fluidStack },
                    500,
                    (int) TierEU.RECIPE_HV,
                    BW_Util.STANDART);

            BWRecipes.instance.addBioLabRecipeIncubation(
                    ItemList.Food_Dough.get(1L),
                    BioCultureLoader.CommonYeast,
                    new int[] { 7500 },
                    new FluidStack[] { fluidStack },
                    500,
                    (int) TierEU.RECIPE_HV,
                    BW_Util.STANDART);

            BWRecipes.instance.addBioLabRecipeIncubation(
                    ItemList.Food_Dough_Sugar.get(1L),
                    BioCultureLoader.WhineYeast,
                    new int[] { 2500 },
                    new FluidStack[] { fluidStack },
                    500,
                    (int) TierEU.RECIPE_HV,
                    BW_Util.STANDART);

            BWRecipes.instance.addBioLabRecipeIncubation(
                    ItemList.Bottle_Wine.get(1L),
                    BioCultureLoader.WhineYeast,
                    new int[] { 3300 },
                    new FluidStack[] { fluidStack },
                    500,
                    (int) TierEU.RECIPE_HV,
                    BW_Util.STANDART);

            BWRecipes.instance.addBioLabRecipeIncubation(
                    ItemList.Bottle_Beer.get(1L),
                    BioCultureLoader.BeerYeast,
                    new int[] { 2500 },
                    new FluidStack[] { fluidStack },
                    500,
                    (int) TierEU.RECIPE_HV,
                    BW_Util.STANDART);

            BWRecipes.instance.addBioLabRecipeIncubation(
                    ItemList.Bottle_Dark_Beer.get(1L),
                    BioCultureLoader.BeerYeast,
                    new int[] { 3300 },
                    new FluidStack[] { fluidStack },
                    500,
                    (int) TierEU.RECIPE_HV,
                    BW_Util.STANDART);

            BWRecipes.instance.addBioLabRecipeIncubation(
                    new ItemStack(Blocks.dirt),
                    BioCultureLoader.anaerobicOil,
                    new int[] { 100 },
                    new FluidStack[] { fluidStack },
                    1500,
                    (int) TierEU.RECIPE_EV,
                    BW_Util.STANDART);
        }
    }

    public static void registerWaterBasedBacterialVatRecipes() {
        FluidStack[] easyFluids = { Materials.Water.getFluid(1000L),
                FluidRegistry.getFluidStack("ic2distilledwater", 1000) };
        for (FluidStack fluidStack : easyFluids) {
            if (CropsPlusPlus.isModLoaded()) {
                BWRecipes.instance.addBacterialVatRecipe(
                        new ItemStack[] { new ItemStack(Items.sugar, 64) },
                        new FluidStack[] { new FluidStack(fluidStack, 100) },
                        BioCultureLoader.CommonYeast,
                        new FluidStack[] { FluidRegistry.getFluidStack("potion.ghp", 1) },
                        350,
                        (int) TierEU.RECIPE_EV);
            }

            BWRecipes.instance.addBacterialVatRecipe(
                    new ItemStack[] { ItemList.Crop_Drop_Grapes.get(16) },
                    new FluidStack[] { new FluidStack(fluidStack, 100) },
                    BioCultureLoader.WhineYeast,
                    new FluidStack[] { FluidRegistry.getFluidStack("potion.wine", 12) },
                    200,
                    (int) TierEU.RECIPE_MV);

            BWRecipes.instance.addBacterialVatRecipe(
                    new ItemStack[] { new ItemStack(Items.sugar, 4), ItemList.IC2_Hops.get(16L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 8L) },
                    new FluidStack[] { new FluidStack(fluidStack, 100) },
                    BioCultureLoader.BeerYeast,
                    new FluidStack[] { FluidRegistry.getFluidStack("potion.beer", 5) },
                    600,
                    (int) TierEU.RECIPE_LV);
            BWRecipes.instance.addBacterialVatRecipe(
                    new ItemStack[] { ItemList.IC2_Hops.get(32L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 16L) },
                    new FluidStack[] { new FluidStack(fluidStack, 100) },
                    BioCultureLoader.BeerYeast,
                    new FluidStack[] { FluidRegistry.getFluidStack("potion.darkbeer", 10) },
                    600,
                    (int) TierEU.RECIPE_LV);
        }
    }

    public static void registerBacterialVatRecipes() {
        registerWaterBasedBacterialVatRecipes();

        BWRecipes.instance.addBacterialVatRecipe(
                null,
                new FluidStack[] { FluidRegistry.getFluidStack("potion.grapejuice", 100) },
                BioCultureLoader.WhineYeast,
                new FluidStack[] { FluidRegistry.getFluidStack("potion.wine", 12) },
                400,
                (int) TierEU.RECIPE_LV);

        BWRecipes.instance.addBacterialVatRecipe(
                null,
                new FluidStack[] { Materials.FermentedBiomass.getFluid(10000) },
                BioCultureLoader.anaerobicOil,
                new FluidStack[] { new FluidStack(FluidLoader.fulvicAcid, 1000) },
                2748,
                (int) TierEU.RECIPE_HV);
    }

    public static void runOnServerStarted() {
        RecipeMaps.fermentingRecipes.getAllRecipes().forEach(
                recipe -> BWRecipes.instance.addBacterialVatRecipe(
                        new ItemStack[] { null },
                        BioCultureLoader.generalPurposeFermentingBacteria,
                        recipe.mFluidInputs,
                        recipe.mFluidOutputs,
                        recipe.mDuration,
                        recipe.mEUt,
                        GT_Utility.getTier(recipe.mEUt)));
    }
}
