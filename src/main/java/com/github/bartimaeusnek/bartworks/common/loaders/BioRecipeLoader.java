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

import static com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps.bacterialVatRecipes;
import static com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps.bioLabRecipes;
import static gregtech.api.enums.Mods.CropsPlusPlus;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.SIEVERTS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
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

            GT_Values.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), new ItemStack(Items.rotten_flesh))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.rottenFleshBacteria))
                .outputChances(33_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), new ItemStack(Items.fermented_spider_eye))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.eColi))
                .outputChances(45_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), ItemList.Food_Dough.get(1L))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.CommonYeast))
                .outputChances(75_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), ItemList.Food_Dough_Sugar.get(1L))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.WhineYeast))
                .outputChances(25_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), ItemList.Bottle_Wine.get(1L))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.WhineYeast))
                .outputChances(33_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), ItemList.Bottle_Beer.get(1L))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.BeerYeast))
                .outputChances(25_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), ItemList.Bottle_Dark_Beer.get(1L))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.BeerYeast))
                .outputChances(33_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), new ItemStack(Blocks.dirt))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.anaerobicOil))
                .outputChances(100)
                .fluidInputs(fluidStack)
                .duration(1 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(bioLabRecipes);
        }
    }

    public static void registerWaterBasedBacterialVatRecipes() {
        FluidStack[] easyFluids = { Materials.Water.getFluid(1000L),
            FluidRegistry.getFluidStack("ic2distilledwater", 1000) };
        for (FluidStack fluidStack : easyFluids) {
            if (CropsPlusPlus.isModLoaded()) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.getIntegratedCircuit(2), new ItemStack(Items.sugar, 64))
                    .special(BioItemList.getPetriDish(BioCultureLoader.CommonYeast))
                    .fluidInputs(new FluidStack(fluidStack, 100))
                    .fluidOutputs(FluidRegistry.getFluidStack("potion.ghp", 1))
                    .duration(17 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_EV)
                    .noOptimize()
                    .addTo(bacterialVatRecipes);
            }

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Crop_Drop_Grapes.get(16))
                .special(BioItemList.getPetriDish(BioCultureLoader.WhineYeast))
                .fluidInputs(new FluidStack(fluidStack, 100))
                .fluidOutputs(FluidRegistry.getFluidStack("potion.wine", 12))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .noOptimize()
                .addTo(bacterialVatRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Items.sugar, 4),
                    ItemList.IC2_Hops.get(16L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 8L))
                .special(BioItemList.getPetriDish(BioCultureLoader.BeerYeast))
                .fluidInputs(new FluidStack(fluidStack, 100))
                .fluidOutputs(FluidRegistry.getFluidStack("potion.beer", 5))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .noOptimize()
                .addTo(bacterialVatRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.IC2_Hops.get(32L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 16L))
                .special(BioItemList.getPetriDish(BioCultureLoader.BeerYeast))
                .fluidInputs(new FluidStack(fluidStack, 100))
                .fluidOutputs(FluidRegistry.getFluidStack("potion.darkbeer", 10))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .noOptimize()
                .addTo(bacterialVatRecipes);
        }
    }

    public static void registerBacterialVatRecipes() {
        registerWaterBasedBacterialVatRecipes();

        GT_Values.RA.stdBuilder()
            .special(BioItemList.getPetriDish(BioCultureLoader.WhineYeast))
            .fluidInputs(FluidRegistry.getFluidStack("potion.grapejuice", 100))
            .fluidOutputs(FluidRegistry.getFluidStack("potion.wine", 12))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .noOptimize()
            .addTo(bacterialVatRecipes);

        GT_Values.RA.stdBuilder()
            .special(BioItemList.getPetriDish(BioCultureLoader.anaerobicOil))
            .fluidInputs(Materials.FermentedBiomass.getFluid(10000))
            .fluidOutputs(new FluidStack(FluidLoader.fulvicAcid, 1000))
            .duration(2 * MINUTES + 17 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .noOptimize()
            .addTo(bacterialVatRecipes);
    }

    public static void runOnServerStarted() {
        RecipeMaps.fermentingRecipes.getAllRecipes()
            .forEach(
                recipe -> GT_Values.RA.stdBuilder()
                    .special(BioItemList.getPetriDish(BioCultureLoader.generalPurposeFermentingBacteria))
                    .fluidInputs(recipe.mFluidInputs)
                    .fluidOutputs(recipe.mFluidOutputs)
                    .duration(recipe.mDuration)
                    .eut(recipe.mEUt)
                    .metadata(SIEVERTS, (int) GT_Utility.getTier(recipe.mEUt))
                    .addTo(bacterialVatRecipes));

    }
}
