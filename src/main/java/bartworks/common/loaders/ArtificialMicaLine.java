package bartworks.common.loaders;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class ArtificialMicaLine {

    public static void runArtificialMicaRecipe() {

        // Mg + O = MgO
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.dust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // Si + 6HF = H2SiF6 + 4H
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, 4))
            .fluidInputs(MaterialUtils.fluid(Materials2Materials.HydrofluoricAcidGT5U, 6_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HexafluorosilicicAcid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // K + Cl = KCl
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.RockSalt, Materials2Shapes.dust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // 2KCl + H2SiF6 = 2HCl + K2SiF6
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.RockSalt, Materials2Shapes.dust, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Potassiumfluorosilicate, Materials2Shapes.dust, 9))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HexafluorosilicicAcid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(MaterialUtils.fluid(Materials2Materials.HydrochloricAcidGT5U, 2_000))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        // 2K + CO2 + O = K2CO3
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.PotassiumCarbonate, Materials2Shapes.dust, 6),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // K2O + CO2 = K2CO3
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Potash, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.PotassiumCarbonate, Materials2Shapes.dust, 6),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials2Materials.Empty, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 55Quartz Dust + 20K2SiF6 + 12Al2O3 + 4K2CO3 = 91Raw Fluorophlogopite Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.QuartzSand, Materials2Shapes.dust, 55),
                MaterialLibAPI.getStack(Materials2Materials.Potassiumfluorosilicate, Materials2Shapes.dust, 20),
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 12),
                MaterialLibAPI.getStack(Materials2Materials.PotassiumCarbonate, Materials2Shapes.dust, 4))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 27))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        // 55Quartzite/Nether Quartz Dust + 20K2SiF6 + 57Al2O3 + 4K2CO3 = 136Raw Fluorophlogopite Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.dust, 55),
                MaterialLibAPI.getStack(Materials2Materials.Potassiumfluorosilicate, Materials2Shapes.dust, 20),
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 57),
                MaterialLibAPI.getStack(Materials2Materials.PotassiumCarbonate, Materials2Shapes.dust, 4))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 8))
            .fluidOutputs()
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.dust, 55),
                MaterialLibAPI.getStack(Materials2Materials.Potassiumfluorosilicate, Materials2Shapes.dust, 20),
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 57),
                MaterialLibAPI.getStack(Materials2Materials.PotassiumCarbonate, Materials2Shapes.dust, 4))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 8))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        // 62Certus Quartz Dust + 10K2SiF6 + 12Al2O3 + 7K2CO3 = 91Raw Fluorophlogopite Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.dust, 62),
                MaterialLibAPI.getStack(Materials2Materials.Potassiumfluorosilicate, Materials2Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 12),
                MaterialLibAPI.getStack(Materials2Materials.PotassiumCarbonate, Materials2Shapes.dust, 7))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 27))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        // MgO(s) = MgO(l)
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Magnesia, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        // 27Raw Fluorophlogopite Dust + 720MgO(l) = 4608Fluorophlogopite(l)
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.RawFluorophlogopite, Materials2Shapes.dust, 27))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Magnesia, Materials2FluidShapes.fluidMolten, (int) (5 * INGOTS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.UnformedFluorophlogopite,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (32 * INGOTS)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1700)
            .addTo(blastFurnaceRecipes);

        // 144Fluorophlogopite(l) = Fluorophlogopite
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Plate.get(0))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Fluorophlogopite, Materials2Shapes.plate, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.UnformedFluorophlogopite,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1 * INGOTS)))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);

        // Fluorophlogopite = 4Insulator Foil
        if (NewHorizonsCoreMod.isModLoaded()) {

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Fluorophlogopite, Materials2Shapes.plate, 1))
                .circuit(1)
                .itemOutputs(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MicaInsulatorFoil", 4))
                .duration(10 * TICKS)
                .eut(TierEU.RECIPE_HV)
                .addTo(benderRecipes);

        }
    }
}
