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

package bartworks.common.loaders;

import static bartworks.API.recipe.BartWorksRecipeMaps.bacterialVatRecipes;
import static bartworks.API.recipe.BartWorksRecipeMaps.bioLabRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.GLASS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class BioRecipeLoader {

    public static void run() {
        registerWaterBasedBioLabIncubations();
        registerBacterialVatRecipes();
    }

    public static void registerWaterBasedBioLabIncubations() {
        FluidStack[] easyFluids = { Materials.Water.getFluid(1_000), GTModHandler.getDistilledWater(1_000) };
        for (FluidStack fluidStack : easyFluids) {

            GTValues.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), new ItemStack(Items.rotten_flesh))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.rottenFleshBacteria))
                .outputChances(33_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), new ItemStack(Items.fermented_spider_eye))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.eColi))
                .outputChances(45_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), ItemList.Food_Dough.get(1L))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.CommonYeast))
                .outputChances(75_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), ItemList.Food_Dough_Sugar.get(1L))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.WhineYeast))
                .outputChances(25_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), ItemList.Bottle_Wine.get(1L))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.WhineYeast))
                .outputChances(33_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), ItemList.Bottle_Beer.get(1L))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.BeerYeast))
                .outputChances(25_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), ItemList.Bottle_Dark_Beer.get(1L))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.BeerYeast))
                .outputChances(33_00)
                .fluidInputs(fluidStack)
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(bioLabRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(BioItemList.getPetriDish(null), new ItemStack(Blocks.dirt))
                .itemOutputs(BioItemList.getPetriDish(BioCultureLoader.anaerobicOil))
                .outputChances(100)
                .fluidInputs(fluidStack)
                .duration(1 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(bioLabRecipes);
        }
    }

    @SuppressWarnings({ "PointlessArithmeticExpression", "RedundantSuppression" })
    public static void registerWaterBasedBacterialVatRecipes() {
        FluidStack[] easyFluids = { Materials.Water.getFluid(1_000), GTModHandler.getDistilledWater(1_000) };
        for (FluidStack fluidStack : easyFluids) {

            GTValues.RA.stdBuilder()
                .itemInputs(new Object[] { "cropGrape", 16 })
                .special(BioItemList.getPetriDish(BioCultureLoader.WhineYeast))
                .fluidInputs(new FluidStack(fluidStack, 100))
                .fluidOutputs(FluidRegistry.getFluidStack("potion.wine", 12))
                .metadata(GLASS, 3)
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(bacterialVatRecipes);
        }
    }

    @SuppressWarnings({ "PointlessArithmeticExpression", "RedundantSuppression" })
    public static void registerBacterialVatRecipes() {
        registerWaterBasedBacterialVatRecipes();

        GTValues.RA.stdBuilder()
            .special(BioItemList.getPetriDish(BioCultureLoader.WhineYeast))
            .fluidInputs(FluidRegistry.getFluidStack("potion.grapejuice", 100))
            .fluidOutputs(FluidRegistry.getFluidStack("potion.wine", 12))
            .metadata(GLASS, 3)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(bacterialVatRecipes);

        GTValues.RA.stdBuilder()
            .special(BioItemList.getPetriDish(BioCultureLoader.anaerobicOil))
            .fluidInputs(Materials.FermentedBiomass.getFluid(10_000))
            .fluidOutputs(new FluidStack(FluidLoader.fulvicAcid, 1_000))
            .metadata(GLASS, 3)
            .duration(2 * MINUTES + 17 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(bacterialVatRecipes);
    }

    public static void runOnServerStarted() {}
}
