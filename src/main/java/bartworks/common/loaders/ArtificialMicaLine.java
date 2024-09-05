package bartworks.common.loaders;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import bartworks.system.material.BWGTMaterialReference;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ArtificialMicaLine {

    public static void runArtificialMicaRecipe() {
        // Mg + O = MgO

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Magnesium.getDust(1))
            .itemOutputs(Materials.Magnesia.getDust(2))
            .fluidInputs(Materials.Oxygen.getGas(1000))
            .duration(2 * SECONDS)
            .eut(8)
            .addTo(UniversalChemical);
        // Si + 6HF = H2SiF6 + 4H

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Silicon.getDust(1), Materials.Empty.getCells(4))
            .itemOutputs(Materials.Hydrogen.getCells(4))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(6000))
            .fluidOutputs(WerkstoffLoader.HexafluorosilicicAcid.getFluidOrGas(1000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);
        // K + Cl = KCl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Potassium.getDust(1), GTUtility.getIntegratedCircuit(2))
            .itemOutputs(Materials.RockSalt.getDust(2))
            .fluidInputs(Materials.Chlorine.getGas(1000))
            .duration(20 * TICKS)
            .eut(8)
            .addTo(UniversalChemical);

        // 2KCl + H2SiF6 = 2HCl + K2SiF6
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RockSalt.getDust(4))
            .itemOutputs(WerkstoffLoader.Potassiumfluorosilicate.get(OrePrefixes.dust, 9))
            .fluidInputs(WerkstoffLoader.HexafluorosilicicAcid.getFluidOrGas(1000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(2000))
            .duration(1 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        // 2K + CO2 + O = K2CO3

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Potassium.getDust(2), Materials.CarbonDioxide.getCells(1))
            .itemOutputs(WerkstoffLoader.PotassiumCarbonate.get(OrePrefixes.dust, 6), Materials.Empty.getCells(1))
            .fluidInputs(Materials.Oxygen.getGas(1000))
            .duration(2 * SECONDS)
            .eut(8)
            .addTo(UniversalChemical);
        // K2O + CO2 = K2CO3

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Potash.getDust(3), Materials.CarbonDioxide.getCells(1))
            .itemOutputs(WerkstoffLoader.PotassiumCarbonate.get(OrePrefixes.dust, 6), Materials.Empty.getCells(1))
            .duration(2 * SECONDS)
            .eut(8)
            .addTo(UniversalChemical);

        // 55Quartz Dust + 20K2SiF6 + 12Al2O3 + 4K2CO3 = 91Raw Fluorophlogopite Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.QuartzSand.getDust(55),
                WerkstoffLoader.Potassiumfluorosilicate.get(OrePrefixes.dust, 20),
                WerkstoffLoader.Alumina.get(OrePrefixes.dust, 12),
                WerkstoffLoader.PotassiumCarbonate.get(OrePrefixes.dust, 4),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(
                WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 64),
                WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 27))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        // 55Quartzite/Nether Quartz Dust + 20K2SiF6 + 57Al2O3 + 4K2CO3 = 136Raw Fluorophlogopite Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Quartzite.getDust(55),
                WerkstoffLoader.Potassiumfluorosilicate.get(OrePrefixes.dust, 20),
                WerkstoffLoader.Alumina.get(OrePrefixes.dust, 57),
                WerkstoffLoader.PotassiumCarbonate.get(OrePrefixes.dust, 4),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(
                WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 64),
                WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 64),
                WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 8))
            .fluidInputs()
            .fluidOutputs()
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.NetherQuartz.getDust(55),
                WerkstoffLoader.Potassiumfluorosilicate.get(OrePrefixes.dust, 20),
                WerkstoffLoader.Alumina.get(OrePrefixes.dust, 57),
                WerkstoffLoader.PotassiumCarbonate.get(OrePrefixes.dust, 4),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(
                WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 64),
                WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 64),
                WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 8))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        // 62Certus Quartz Dust + 10K2SiF6 + 12Al2O3 + 7K2CO3 = 91Raw Fluorophlogopite Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.CertusQuartz.getDust(62),
                WerkstoffLoader.Potassiumfluorosilicate.get(OrePrefixes.dust, 10),
                WerkstoffLoader.Alumina.get(OrePrefixes.dust, 12),
                WerkstoffLoader.PotassiumCarbonate.get(OrePrefixes.dust, 7),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(
                WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 64),
                WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 27))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        // MgO(s) = MgO(l)

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Magnesia.getDust(1))
            .fluidOutputs(Materials.Magnesia.getMolten(144))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        // 27Raw Fluorophlogopite Dust + 720MgO(l) = 4608Fluorophlogopite(l)
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 27))
            .itemOutputs(Materials.Glass.getDust(1))
            .fluidInputs(BWGTMaterialReference.Magnesia.getMolten(720))
            .fluidOutputs(WerkstoffLoader.HotFluorophlogopite.getFluidOrGas(4608))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1700)
            .addTo(blastFurnaceRecipes);

        // 144Fluorophlogopite(l) = Fluorophlogopite
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Plate.get(0))
            .itemOutputs(WerkstoffLoader.Fluorophlogopite.get(OrePrefixes.plate, 1))
            .fluidInputs(WerkstoffLoader.HotFluorophlogopite.getFluidOrGas(144))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .noOptimize()
            .addTo(vacuumFreezerRecipes);

        // Fluorophlogopite = 4Insulator Foil
        if (NewHorizonsCoreMod.isModLoaded()) {

            GTValues.RA.stdBuilder()
                .itemInputs(
                    WerkstoffLoader.Fluorophlogopite.get(OrePrefixes.plate, 1),
                    GTUtility.getIntegratedCircuit(1))
                .itemOutputs(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 4))
                .duration(10 * TICKS)
                .eut(600)
                .addTo(benderRecipes);

        }
    }
}
