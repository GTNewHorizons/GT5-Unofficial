package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalPlantRecipes;
import static gregtech.api.recipe.RecipeMaps.coldTrapRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.fissionFuelProcessingRecipes;
import static gregtech.api.recipe.RecipeMaps.reactorProcessingUnitRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFurnaceRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.material.nuclear.MaterialsNuclides;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class RecipeLoaderNuclearFuelProcessing {

    public static void generate() {

        // Create Fuels

        // 7LiF - BeF2 - ZrF4 - U235 - 590C
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsFluorides.LITHIUM_FLUORIDE.getFluidStack(550),
                MaterialsFluorides.BERYLLIUM_FLUORIDE.getFluidStack(150),
                MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(60),
                Materials.Uranium235.getMolten(240))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ZrF4U235.getFluidStack(1000))
            .duration(30 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(fissionFuelProcessingRecipes);
        // 7LiF - BeF2 - ZrF4 - UF4 - 650C
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsFluorides.LITHIUM_FLUORIDE.getFluidStack(600),
                MaterialsFluorides.BERYLLIUM_FLUORIDE.getFluidStack(250),
                MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(80),
                MaterialsFluorides.URANIUM_TETRAFLUORIDE.getFluidStack(70))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ZrF4UF4.getFluidStack(1000))
            .duration(40 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
        // 7liF - BeF2 - ThF4 - UF4 - 566C
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsFluorides.LITHIUM_FLUORIDE.getFluidStack(580),
                MaterialsFluorides.BERYLLIUM_FLUORIDE.getFluidStack(270),
                MaterialsFluorides.THORIUM_TETRAFLUORIDE.getFluidStack(80),
                MaterialsFluorides.URANIUM_TETRAFLUORIDE.getFluidStack(70))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ThF4UF4.getFluidStack(1000))
            .duration(50 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);

        // Reprocess Fuels

        // Reactor Blanket step 1 - Fluorination
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().FLUORINE.getCell(6))
            .circuit(17)
            .itemOutputs(
                ItemList.Cell_Empty.get(5),
                MaterialsFluorides.LITHIUM_FLUORIDE.getCell(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1))
            .outputChances(10000, 10000, 500, 500, 500, 250, 250, 250)
            .fluidInputs(MaterialsNuclides.LiFThF4.getFluidStack(10000))
            .fluidOutputs(MaterialsNuclides.UF6F2.getFluidStack(1500))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().FLUORINE.getCell(6))
            .circuit(18)
            .itemOutputs(
                ItemList.Cell_Empty.get(4),
                MaterialsFluorides.LITHIUM_FLUORIDE.getCell(1),
                MaterialsFluorides.BERYLLIUM_FLUORIDE.getCell(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1))
            .outputChances(10000, 10000, 10000, 1000, 1000, 1000, 500, 500, 500)
            .fluidInputs(MaterialsNuclides.LiFBeF2ThF4.getFluidStack(10000))
            .fluidOutputs(MaterialsNuclides.UF6F2.getFluidStack(3000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        // Reactor Blanket step 1 - Fluorination
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().FLUORINE.getCell(6))
            .circuit(7)
            .itemOutputs(
                ItemList.Cell_Empty.get(4),
                MaterialsFluorides.LITHIUM_FLUORIDE.getCell(2),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1))
            .outputChances(10000, 10000, 1000, 1000, 1000, 1000, 1000, 1000)
            .fluidInputs(MaterialsNuclides.Sparged_LiFThF4.getFluidStack(10000))
            .fluidOutputs(MaterialsNuclides.UF6F2.getFluidStack(3000))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().FLUORINE.getCell(6))
            .circuit(8)
            .itemOutputs(
                ItemList.Cell_Empty.get(2),
                MaterialsFluorides.LITHIUM_FLUORIDE.getCell(2),
                MaterialsFluorides.BERYLLIUM_FLUORIDE.getCell(2),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1),
                GregtechItemList.Protactinium233Dust.get(1))
            .outputChances(10000, 10000, 10000, 2000, 2000, 2000, 2000, 2000, 2000)
            .fluidInputs(MaterialsNuclides.Sparged_LiFBeF2ThF4.getFluidStack(10000))
            .fluidOutputs(MaterialsNuclides.UF6F2.getFluidStack(6000))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(reactorProcessingUnitRecipes);

        // Reactor Blanket step 2 - Sorption + Cold Trap
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.SODIUM_FLUORIDE.getCell(4))
            .circuit(8)
            .itemOutputs(
                MaterialsElements.getInstance().FLUORINE.getCell(2),
                MaterialsFluorides.URANIUM_HEXAFLUORIDE.getCell(2),
                MaterialsElements.getInstance().URANIUM233.getDust(1),
                MaterialsElements.getInstance().URANIUM233.getDust(1),
                MaterialsElements.getInstance().URANIUM233.getDust(1))
            .outputChances(10000, 10000, 3000, 2000, 1000)
            .fluidInputs(MaterialsNuclides.UF6F2.getFluidStack(3000))
            .fluidOutputs(MaterialsFluorides.SODIUM_FLUORIDE.getFluidStack(2000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(coldTrapRecipes);

        // LiBeF2UF4FP + F2 = LiFBeF2 & UF6F2FP
        // Reactor Core step 1 - Process Burnt Salt
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().FLUORINE.getCell(1))
            .circuit(1)
            .itemOutputs(MaterialsNuclides.UF6F2FP.getCell(1))
            .outputChances(10000)
            .fluidInputs(MaterialsNuclides.LiFBeF2UF4FP.getFluidStack(1000))
            .fluidOutputs(new FluidStack(GTPPFluids.ImpureMoltenSaltBase, 1000))
            .duration(2 * HOURS)
            .eut(TierEU.RECIPE_HV)
            .addTo(reactorProcessingUnitRecipes);
        // LiBeF2UF4FP + F2 = LiFBeF2 & UF6F2FP
        // Reactor Core step 1 - Process Burnt Salt
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().FLUORINE.getCell(3))
            .circuit(1)
            .itemOutputs(ItemList.Cell_Empty.get(1), MaterialsNuclides.UF6F2FP.getCell(2))
            .fluidInputs(MaterialsNuclides.Sparged_LiFBeF2UF4FP.getFluidStack(1000))
            .fluidOutputs(new FluidStack(GTPPFluids.ImpureMoltenSaltBase, 2000))
            .duration(60 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(reactorProcessingUnitRecipes);

        // Reactor Core step 2A - Sorption + Cold Trap
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.SODIUM_FLUORIDE.getCell(3))
            .circuit(8)
            .itemOutputs(
                MaterialsElements.getInstance().FLUORINE.getCell(1),
                MaterialsFluorides.URANIUM_HEXAFLUORIDE.getCell(2),
                MaterialsElements.getInstance().PHOSPHORUS.getDust(1),
                MaterialsElements.getInstance().PHOSPHORUS.getDust(1),
                MaterialsElements.getInstance().PHOSPHORUS.getDust(1),
                MaterialsElements.getInstance().PHOSPHORUS.getDust(1),
                MaterialsElements.getInstance().PHOSPHORUS.getDust(1),
                MaterialsElements.getInstance().PHOSPHORUS.getDust(1))
            .outputChances(10000, 10000, 5000, 5000, 5000, 5000, 5000, 5000)
            .fluidInputs(MaterialsNuclides.UF6F2FP.getFluidStack(2000))
            .fluidOutputs(MaterialsFluorides.SODIUM_FLUORIDE.getFluidStack(2000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(coldTrapRecipes);

        // Reactor Core step 2B - Distillation
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.ImpureMoltenSaltBase, 1000))
            .fluidOutputs(MaterialsNuclides.LiFBeF2.getFluidStack(250))
            .duration(7 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

        // UF6 -> UF4 reduction
        // UF6 + LiFBeF2 + H2 -> LiFBeF2UF4 + HF
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.URANIUM_HEXAFLUORIDE.getCell(1), MaterialsNuclides.LiFBeF2.getCell(1))
            .fluidInputs(Materials.Hydrogen.getGas(2_000))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 2))
            .fluidOutputs(MaterialsNuclides.LiFBeF2UF4.getFluidStack(3000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 5400)
            .addTo(blastFurnaceRecipes);
        // Alternative recipe to the above, for chemplant, to not use cells

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsFluorides.URANIUM_HEXAFLUORIDE.getFluidStack(1000),
                MaterialsNuclides.LiFBeF2.getFluidStack(1000),
                MaterialsElements.getInstance().HYDROGEN.getFluidStack(2000))
            .fluidOutputs(
                MaterialsNuclides.LiFBeF2UF4.getFluidStack(3000),
                new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 2000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(100),
                MaterialsNuclides.LiFBeF2UF4.getFluidStack(900))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ZrF4UF4.getFluidStack(1000))
            .duration(12 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsFluorides.THORIUM_TETRAFLUORIDE.getFluidStack(100),
                MaterialsNuclides.LiFBeF2UF4.getFluidStack(900))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ThF4UF4.getFluidStack(1000))
            .duration(15 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(fissionFuelProcessingRecipes);

        // Pa dust -> Pa-233 dust: converts legacy plain-Pa dust into the decaying isotope
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().PROTACTINIUM.getDust(1))
            .itemOutputs(GregtechItemList.Protactinium233Dust.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        neptuniumLine();
        technetiumLine();
        seleniumLine();
        uraniumLine();
        zirconiumLine();
    }

    // NpF6 + 2H2O -> NpO2F2 + 4HF (hydrolysis, recovers 2/3 of the fluorine; Exxon, titanium casing)
    // NpO2F2 -> Np + O2 + F2 (vacuum decomposition, Naquadah coils)
    private static void neptuniumLine() {
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsFluorides.NEPTUNIUM_HEXAFLUORIDE.getFluidStack(1_000),
                Materials.Water.getFluid(2_000))
            .itemOutputs(MaterialsFluorides.NEPTUNYL_FLUORIDE.getDust(1))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 4) // Titanium
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.NEPTUNYL_FLUORIDE.getDust(1))
            .itemOutputs(MaterialsElements.getInstance().NEPTUNIUM.getDust(1))
            .fluidOutputs(Materials.Oxygen.getGas(2_000), Materials.Fluorine.getGas(2_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 7201) // Naquadah coils
            .addTo(vacuumFurnaceRecipes);
    }

    // TcF6 + 2NaOH + H2O -> TcO2 + 4HF + 2NaF + O (Exxon, titanium casing)
    // TcO2 -> Tc + O2 (electrolysis)
    private static void technetiumLine() {
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsFluorides.TECHNETIUM_HEXAFLUORIDE.getFluidStack(1_000),
                Materials.Water.getFluid(1_000))
            .itemInputs(Materials.SodiumHydroxide.getDust(2))
            .itemOutputs(MaterialMisc.TECHNETIUM_DIOXIDE.getDust(1), MaterialsFluorides.SODIUM_FLUORIDE.getDust(2))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(4_000), Materials.Oxygen.getGas(1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(CHEMPLANT_CASING_TIER, 4) // Titanium
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialMisc.TECHNETIUM_DIOXIDE.getDust(1))
            .itemOutputs(MaterialsElements.getInstance().TECHNETIUM.getDust(1))
            .fluidOutputs(Materials.Oxygen.getGas(2_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(electrolyzerRecipes);
    }

    // SeF6 + 2NaOH + H2O -> SeO2 (dust) + 4HF + 2NaF + O (Exxon, titanium casing)
    // Yields SeO2 dust, which feeds the existing dust -> fluid -> selenious acid -> selenium chain.
    private static void seleniumLine() {
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsFluorides.SELENIUM_HEXAFLUORIDE.getFluidStack(1_000), Materials.Water.getFluid(1_000))
            .itemInputs(Materials.SodiumHydroxide.getDust(2))
            .itemOutputs(MaterialMisc.SELENIUM_DIOXIDE.getDust(1), MaterialsFluorides.SODIUM_FLUORIDE.getDust(2))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(4_000), Materials.Oxygen.getGas(1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(CHEMPLANT_CASING_TIER, 4) // Titanium
            .addTo(chemicalPlantRecipes);
    }

    // UF6 + 2H2O -> UO2F2 + 4HF (hydrolysis; Exxon, titanium casing)
    // UO2F2 + H2 -> UO2 + 2HF (reduction; UO2 = uraninite; TPV coils)
    // UF4 + 2H2O -> UO2 + 4HF (un-make UF4; LCR recycling)
    private static void uraniumLine() {
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsFluorides.URANIUM_HEXAFLUORIDE.getFluidStack(1_000), Materials.Water.getFluid(2_000))
            .itemOutputs(MaterialsFluorides.URANYL_FLUORIDE.getDust(1))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 4) // Titanium
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.URANYL_FLUORIDE.getDust(1))
            .fluidInputs(Materials.Hydrogen.getGas(1_000))
            .itemOutputs(Materials.Uraninite.getDust(1))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(2_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4501) // TPV coils
            .addTo(vacuumFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsFluorides.URANIUM_TETRAFLUORIDE.getFluidStack(1_000), Materials.Water.getFluid(2_000))
            .itemOutputs(Materials.Uraninite.getDust(1))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);
    }

    // ZrF4 + 2H2O -> ZrO2 + 4HF (un-make ZrF4; Zirconia feeds into zirconium lanthanides line)
    private static void zirconiumLine() {
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(1_000),
                Materials.Water.getFluid(2_000))
            .itemOutputs(WerkstoffMaterialPool.Zirconia.get(OrePrefixes.dust, 1))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);
    }
}
