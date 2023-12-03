package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;

public class Electrolyzer implements Runnable {

    // TODO: fix the chemical balance issues there are below
    @Override
    public void run() {

        GT_Values.RA.stdBuilder().itemInputs(WerkstoffLoader.Forsterit.get(OrePrefixes.dust, 7))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L))
                .fluidOutputs(Materials.Oxygen.getGas(2000L)).duration(10 * SECONDS).eut(90).addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 6))
                .itemOutputs(
                        WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L))
                .fluidOutputs(Materials.Oxygen.getGas(2000L)).duration(12 * SECONDS + 10 * TICKS).eut(90)
                .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(WerkstoffLoader.Fayalit.get(OrePrefixes.dust, 7))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L))
                .fluidOutputs(Materials.Oxygen.getGas(2000L)).duration(16 * SECONDS).eut(90).addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(WerkstoffLoader.Prasiolite.get(OrePrefixes.dust, 16))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 5L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L))
                .duration(29 * SECONDS).eut(90).addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(WerkstoffLoader.Hedenbergit.get(OrePrefixes.dust, 10))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 2L))
                .fluidOutputs(Materials.Oxygen.getGas(2000L)).duration(15 * SECONDS).eut(90).addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(WerkstoffLoader.FuchsitAL.get(OrePrefixes.dust, 21), ItemList.Cell_Empty.get(2))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2))
                .fluidOutputs(Materials.Oxygen.getGas(2000L)).duration(19 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_MV)
                .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(WerkstoffLoader.FuchsitCR.get(OrePrefixes.dust, 21), ItemList.Cell_Empty.get(2))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2))
                .fluidOutputs(Materials.Oxygen.getGas(2000L)).duration(23 * SECONDS).eut(TierEU.RECIPE_MV)
                .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(WerkstoffLoader.VanadioOxyDravit.get(OrePrefixes.dust, 53), ItemList.Cell_Empty.get(3))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnalium, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3))
                .fluidOutputs(Materials.Oxygen.getGas(19000L)).duration(35 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_MV)
                .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        WerkstoffLoader.ChromoAluminoPovondrait.get(OrePrefixes.dust, 53),
                        ItemList.Cell_Empty.get(3))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnalium, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3))
                .fluidOutputs(Materials.Oxygen.getGas(19000L)).duration(36 * SECONDS).eut(TierEU.RECIPE_MV)
                .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(WerkstoffLoader.FluorBuergerit.get(OrePrefixes.dust, 50), ItemList.Cell_Empty.get(3))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fluorine, 3))
                .fluidOutputs(Materials.Oxygen.getGas(6000L)).duration(36 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_MV)
                .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(WerkstoffLoader.Olenit.get(OrePrefixes.dust, 51), ItemList.Cell_Empty.get(1))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 9L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1))
                .fluidOutputs(Materials.Oxygen.getGas(1000L)).duration(39 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_MV)
                .addTo(electrolyzerRecipes);

    }
}
