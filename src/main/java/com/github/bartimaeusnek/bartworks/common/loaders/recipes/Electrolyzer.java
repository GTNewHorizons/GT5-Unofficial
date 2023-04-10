package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class Electrolyzer implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.Forsterit.get(OrePrefixes.dust, 7),
                GT_Values.NI,
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                200,
                90);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 6),
                GT_Values.NI,
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                250,
                90);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.Fayalit.get(OrePrefixes.dust, 7),
                GT_Values.NI,
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                320,
                90);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.Prasiolite.get(OrePrefixes.dust, 16),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 5L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                580,
                90);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.Hedenbergit.get(OrePrefixes.dust, 10),
                GT_Values.NI,
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 2L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                null,
                300,
                90);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.FuchsitAL.get(OrePrefixes.dust, 21),
                ItemList.Cell_Empty.get(2),
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2),
                GT_Values.NI,
                GT_Values.NI,
                null,
                390,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.FuchsitCR.get(OrePrefixes.dust, 21),
                ItemList.Cell_Empty.get(2),
                GT_Values.NF,
                Materials.Oxygen.getGas(2000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2),
                GT_Values.NI,
                GT_Values.NI,
                null,
                460,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.VanadioOxyDravit.get(OrePrefixes.dust, 53),
                ItemList.Cell_Empty.get(3),
                GT_Values.NF,
                Materials.Oxygen.getGas(19000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnalium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3),
                null,
                710,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.ChromoAluminoPovondrait.get(OrePrefixes.dust, 53),
                ItemList.Cell_Empty.get(3),
                GT_Values.NF,
                Materials.Oxygen.getGas(19000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnalium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3),
                null,
                720,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.FluorBuergerit.get(OrePrefixes.dust, 50),
                ItemList.Cell_Empty.get(3),
                GT_Values.NF,
                Materials.Oxygen.getGas(6000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 6L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fluorine, 3),
                null,
                730,
                120);
        GT_Values.RA.addElectrolyzerRecipe(
                WerkstoffLoader.Olenit.get(OrePrefixes.dust, 51),
                ItemList.Cell_Empty.get(1),
                GT_Values.NF,
                Materials.Oxygen.getGas(1000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 9L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 6L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 3),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1),
                GT_Values.NI,
                null,
                790,
                120);
    }
}
