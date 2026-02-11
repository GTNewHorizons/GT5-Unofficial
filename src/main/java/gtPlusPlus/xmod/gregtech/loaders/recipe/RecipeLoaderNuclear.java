package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.material.nuclear.MaterialsNuclides;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipeLoaderNuclear {

    public static void generate() {
        createRecipes();
        RecipeLoaderLFTR.generate();
        RecipeLoaderNuclearFuelProcessing.generate();
    }

    private static void createRecipes() {
        autoclave();
        blastFurnace();
        chemicalBathRecipes();
        chemicalReactorRecipes();
        dehydratorRecipes();
        electroMagneticSeperator();
        fluidExtractorRecipes();
        fluidHeater();
        fusionChainRecipes();
        macerator();
        mixerRecipes();
        sifter();
    }

    private static void autoclave() {
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1))
            .itemOutputs(GregtechItemList.ZirconiumPellet.get(1))
            .fluidInputs(Materials.Chlorine.getGas(4_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);
    }

    private static void blastFurnace() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsFluorides.LITHIUM_FLUORIDE.getDust(4),
                MaterialsFluorides.BERYLLIUM_FLUORIDE.getDust(3))
            .itemOutputs(MaterialsNuclides.Li2BeF4.getDust(7))
            .duration(2 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.ZrCl4Dust.get(1))
            .itemOutputs(GregtechItemList.CookedZrCl4Dust.get(1))
            .duration(1 * MINUTES)
            .eut(340)
            .metadata(COIL_HEAT, 300)
            .addTo(blastFurnaceRecipes);
    }

    private static void chemicalBathRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Tin.getDust(12))
            .itemOutputs(
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 3),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 4),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 5))
            .outputChances(90_00, 60_00, 30_00)
            .fluidInputs(Materials.Chlorine.getGas(2_400))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Rutile.getDust(5))
            .itemOutputs(
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 3),
                Materials.Titanium.getDust(1),
                MaterialsElements.getInstance().HAFNIUM.getDust(1))
            .outputChances(90_00, 30_00, 10_00)
            .fluidInputs(Materials.Chlorine.getGas(4_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.LithiumCarbonateDust.get(3))
            .itemOutputs(MaterialsFluorides.LITHIUM_FLUORIDE.getDust(2))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 500))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalBathRecipes);
    }

    private static void chemicalReactorRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.LithiumCarbonateDust.get(6), GregtechItemList.CalciumHydroxideDust.get(5))
            .itemOutputs(GregtechItemList.Li2CO3CaOH2Dust.get(11))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.LithiumHydroxideDust.get(3))
            .itemOutputs(MaterialsFluorides.LITHIUM_FLUORIDE.getDust(2))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 500))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Oxygen.getCells(8), MaterialsElements.getInstance().LITHIUM7.getDust(16))
            .itemOutputs(ItemList.Cell_Empty.get(8))
            .fluidInputs(Materials.Water.getFluid(8_000))
            .fluidOutputs(new FluidStack(GTPPFluids.LithiumHydroxide, 48 * INGOTS))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // LFTR Fuel Related Compounds
        // Beryllium Hydroxide
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().BERYLLIUM.getDust(1))
            .circuit(3)
            .fluidInputs(Materials.Oxygen.getGas(1_000), Materials.Hydrogen.getGas(1_000))
            .fluidOutputs(MaterialsFluorides.BERYLLIUM_HYDROXIDE.getFluidStack(3 * INGOTS))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // Ammonium Bifluoride
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1))
            .circuit(3)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialMisc.AMMONIA.getFluidStack(1_000))
            .fluidOutputs(MaterialsFluorides.AMMONIUM_BIFLUORIDE.getFluidStack(4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ammonium Bifluoride
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrofluoricAcid.getCells(2))
            .circuit(3)
            .itemOutputs(ItemList.Cell_Empty.get(2))
            .fluidInputs(MaterialMisc.AMMONIA.getFluidStack(1_000))
            .fluidOutputs(MaterialsFluorides.AMMONIUM_BIFLUORIDE.getFluidStack(4 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ammonium
        // To be deprecated now that it is no longer needed for ammonium bifluoride
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Hydrogen.getCells(1))
            .circuit(3)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(Materials.Ammonia.getGas(1_000))
            .fluidOutputs(MaterialMisc.AMMONIUM.getFluidStack(2_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Sodium Fluoride

        // Circuit 15 causes recipes added to UniversalChemical to be single-block only, so we manually circumvent this
        // logic here instead of changing the circuit
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3))
            .circuit(15)
            .itemOutputs(MaterialsFluorides.SODIUM_FLUORIDE.getDust(2))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 500))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3))
            .circuit(15)
            .itemOutputs(MaterialsFluorides.SODIUM_FLUORIDE.getDust(2))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3))
            .circuit(15)
            .itemOutputs(MaterialsFluorides.SODIUM_FLUORIDE.getDust(2))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 500))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3))
            .circuit(15)
            .itemOutputs(MaterialsFluorides.SODIUM_FLUORIDE.getDust(2))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void dehydratorRecipes() {
        // Makes 7-Lithium
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.SulfuricLithiumMix, 10 * INGOTS))
            .itemOutputs(
                Materials.Sulfur.getDust(3),
                Materials.Copper.getDust(1),
                Materials.Sodium.getDust(1),
                Materials.Carbon.getDust(1),
                MaterialsElements.getInstance().LITHIUM7.getDust(4))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalDehydratorRecipes);

        // Makes Lithium Carbonate
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(12), Materials.Lepidolite.getDust(20))
            .itemOutputs(
                Materials.Potassium.getDust(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4),
                Materials.Oxygen.getCells(10),
                Materials.Fluorine.getCells(2),
                GregtechItemList.LithiumCarbonateDust.get(3))
            .fluidInputs(Materials.SulfuricAcid.getFluid(10_000))
            .fluidOutputs(new FluidStack(GTPPFluids.SulfuricLithiumMix, 10_000))
            .eut(1_000)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Calcium Hydroxide
        // CaO + H2O = Ca(OH)2
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Quicklime.getDust(2))
            .itemOutputs(GregtechItemList.CalciumHydroxideDust.get(5))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .eut(TierEU.RECIPE_MV)
            .duration(12 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // 2 LiOH + CaCO3
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Li2CO3CaOH2Dust.get(11))
            .itemOutputs(GregtechItemList.LithiumHydroxideDust.get(6), GregtechItemList.CalciumCarbonateDust.get(5))
            .eut(1_000)
            .duration(6 * MINUTES)
            .addTo(chemicalDehydratorRecipes);

        // LiOH Liquid to Dust
        GTValues.RA.stdBuilder()
            .itemOutputs(GregtechItemList.LithiumHydroxideDust.get(1))
            .fluidInputs(new FluidStack(GTPPFluids.LithiumHydroxide, 1 * INGOTS))
            .eut(TierEU.RECIPE_MV / 2)
            .duration(1 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Zirconium Chloride -> TetraFluoride
        FluidStack aHydrogenChloride = Materials.HydrochloricAcid.getFluid(800);
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CookedZrCl4Dust.get(1))
            .circuit(11)
            .itemOutputs(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getDust(1))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 400))
            .fluidOutputs(aHydrogenChloride)
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Zirconium Chloride -> TetraFluoride
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CookedZrCl4Dust.get(1))
            .circuit(10)
            .itemOutputs(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getDust(1))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(800))
            .fluidOutputs(aHydrogenChloride)
            .eut(TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Be(OH)2 + 2 (NH4)HF2 → (NH4)2BeF4 + 2 H2O
        // Inputs use solid rule because they are molten forms of solids
        // Outputs use fluid rule because they are not molten forms of solids
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.BERYLLIUM_HYDROXIDE.getDust(3), ItemList.Cell_Empty.get(2))
            .itemOutputs(Materials.Water.getCells(2))
            .fluidInputs(MaterialsFluorides.AMMONIUM_BIFLUORIDE.getFluidStack(8 * INGOTS))
            .fluidOutputs(MaterialsFluorides.AMMONIUM_TETRAFLUOROBERYLLATE.getFluidStack(1_000))
            .eut(TierEU.RECIPE_MV / 2)
            .duration(6 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.BERYLLIUM_HYDROXIDE.getDust(3))
            .fluidInputs(MaterialsFluorides.AMMONIUM_BIFLUORIDE.getFluidStack(8 * INGOTS))
            .fluidOutputs(
                Materials.Water.getFluid(2_000),
                MaterialsFluorides.AMMONIUM_TETRAFLUOROBERYLLATE.getFluidStack(1_000))
            .eut(TierEU.RECIPE_MV)
            .duration(5 * MINUTES)
            .addTo(multiblockChemicalReactorRecipes);

        // (NH4)2BeF4 → 2 NH3 + 2 HF + BeF2
        // Ammonium tetrafluoroberyllate uses fluid rule because it is not a molten form of a solid
        // Beryllium fluoride uses solid rule
        // Industrial strength hydrofluoric acid follows its usual convention where it is twice as dense as regular
        // hydrofluoric acid
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(3))
            .circuit(17)
            .itemOutputs(
                MaterialMisc.AMMONIA.getCell(2),
                ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1),
                MaterialsFluorides.BERYLLIUM_FLUORIDE.getDust(3))
            .fluidInputs(MaterialsFluorides.AMMONIUM_TETRAFLUOROBERYLLATE.getFluidStack(1_000))
            .eut(TierEU.RECIPE_MV)
            .duration(5 * MINUTES)
            .addTo(chemicalDehydratorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(17)
            .itemOutputs(MaterialsFluorides.BERYLLIUM_FLUORIDE.getDust(3))
            .fluidInputs(MaterialsFluorides.AMMONIUM_TETRAFLUOROBERYLLATE.getFluidStack(1_000))
            .fluidOutputs(
                MaterialMisc.AMMONIA.getFluidStack(2_000),
                new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 1_000))
            .eut(TierEU.RECIPE_MV)
            .duration(5 * MINUTES)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void electroMagneticSeperator() {
        // Zirconium
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Bauxite, 1))
            .itemOutputs(
                Materials.Bauxite.getDust(1),
                Materials.Rutile.getDustSmall(1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.nugget, 1))
            .outputChances(100_00, 25_00, 40_00)
            .duration(20 * SECONDS)
            .eut(24)
            .addTo(electroMagneticSeparatorRecipes);

        // Zircon
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Magnetite, 1))
            .itemOutputs(
                Materials.Magnetite.getDust(1),
                MaterialsOres.ZIRCON.getSmallDust(1),
                MaterialsOres.ZIRCON.getTinyDust(1))
            .outputChances(100_00, 12_50, 25_00)
            .duration(20 * SECONDS)
            .eut(24)
            .addTo(electroMagneticSeparatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Cassiterite, 1))
            .itemOutputs(
                Materials.Cassiterite.getDust(1),
                MaterialsOres.ZIRCON.getSmallDust(1),
                MaterialsOres.ZIRCON.getTinyDust(1))
            .outputChances(100_00, 12_50, 25_00)
            .duration(20 * SECONDS)
            .eut(24)
            .addTo(electroMagneticSeparatorRecipes);
    }

    private static void fluidExtractorRecipes() {
        // FLiBe fuel
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsNuclides.Li2BeF4.getDust(1))
            .fluidOutputs(MaterialsNuclides.Li2BeF4.getFluidStack(1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);

        // Lithium Fluoride
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.LITHIUM_FLUORIDE.getDust(1))
            .fluidOutputs(MaterialsFluorides.LITHIUM_FLUORIDE.getFluidStack(1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);

        // Lithium Fluoride
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.BERYLLIUM_FLUORIDE.getDust(1))
            .fluidOutputs(MaterialsFluorides.BERYLLIUM_FLUORIDE.getFluidStack(1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);

        // LFTR Fuel 1
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsNuclides.LiFBeF2ZrF4U235.getDust(1))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ZrF4U235.getFluidStack(1 * INGOTS))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(1000)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsNuclides.LiFBeF2ZrF4UF4.getDust(1))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ZrF4UF4.getFluidStack(1 * INGOTS))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(1500)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsNuclides.LiFBeF2ThF4UF4.getDust(1))
            .fluidOutputs(MaterialsNuclides.LiFBeF2ThF4UF4.getFluidStack(1 * INGOTS))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidExtractionRecipes);

        // ZIRCONIUM_TETRAFLUORIDE
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getDust(1))
            .fluidOutputs(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(1 * INGOTS))
            .duration(10 * SECONDS)
            .eut(512 + 256)
            .addTo(fluidExtractionRecipes);
    }

    private static void fusionChainRecipes() {
        // Mk1
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Boron.getPlasma(1 * INGOTS), Materials.Calcium.getPlasma(1 * NUGGETS))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().NEON.getPlasma(), 1_000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 100_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().NEON.getPlasma(), 1 * INGOTS),
                new FluidStack(MaterialsAlloy.ARCANITE.getFluid(), 2))
            .fluidOutputs(new FluidStack(MaterialsElements.STANDALONE.FORCE.getPlasma(), 1_000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 100_000_000L)
            .addTo(fusionRecipes);

        // Mk2
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Niobium.getPlasma(1 * INGOTS), Materials.Zinc.getPlasma(1 * INGOTS))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().KRYPTON.getPlasma(), 1 * INGOTS))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().KRYPTON.getPlasma(), 1 * INGOTS),
                new FluidStack(MaterialsElements.STANDALONE.FORCE.getPlasma(), 1_000))
            .fluidOutputs(new FluidStack(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlasma(), 1_000))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlasma(), 1 * INGOTS),
                new FluidStack(MaterialsAlloy.TITANSTEEL.getFluid(), 2))
            .fluidOutputs(new FluidStack(MaterialsElements.STANDALONE.RUNITE.getPlasma(), 1_000))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300_000_000L)
            .addTo(fusionRecipes);

        // Mk3
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsElements.getInstance().CURIUM.getFluidStack(1 * INGOTS),
                Materials.Americium.getPlasma(1 * INGOTS))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 1 * INGOTS))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 1 * INGOTS),
                new FluidStack(MaterialsElements.STANDALONE.RUNITE.getPlasma(), 1_000))
            .fluidOutputs(new FluidStack(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlasma(), 1_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlasma(), 72),
                Materials.Tartarite.getMolten(2))
            .fluidOutputs(new FluidStack(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 1_000))
            .duration(8 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes);
    }

    private static void macerator() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.ZirconiumPellet.get(1))
            .itemOutputs(GregtechItemList.ZrCl4Dust.get(5))
            .eut(2)
            .duration(20 * SECONDS)
            .addTo(maceratorRecipes);
    }

    private static void mixerRecipes() {
        // Rebalanced to correct the chemistry
        // UF4 uses solid rule due to item form even though item form currently is inaccessible because item form may be
        // accessible in future and must be consistent
        // UF4 solid rule also assumes 1:144 item:fluid ratio in this case
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().URANIUM233.getDust(4), Materials.Uranium235.getDust(1))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 10_000))
            .fluidOutputs(MaterialsFluorides.URANIUM_TETRAFLUORIDE.getFluidStack(25 * INGOTS))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

    }

    private static void sifter() {
        // Zirconium
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Ilmenite, 1))
            .itemOutputs(
                Materials.Iron.getDust(1),
                Materials.WroughtIron.getDust(1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                MaterialsElements.getInstance().HAFNIUM.getDust(1),
                MaterialsElements.getInstance().HAFNIUM.getDust(1))
            .outputChances(50_00, 2_78, 10_00, 10_00, 3_00, 3_00)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Tin, 1))
            .itemOutputs(
                Materials.Tin.getDust(1),
                Materials.Zinc.getDust(1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1))
            .outputChances(100_00, 5_56, 15_00, 10_00, 5_00, 5_00)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Cassiterite, 1))
            .itemOutputs(
                Materials.Cassiterite.getDust(1),
                Materials.Tin.getDust(1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1),
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 1))
            .outputChances(100_00, 5_56, 15_00, 10_00, 5_00, 5_00)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        // Radium
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Thorium, 1))
            .itemOutputs(
                Materials.Thorium.getDust(1),
                Materials.Lead.getDust(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1))
            .outputChances(100_00, 5_00, 3_00, 2_00, 1_00, 1_00)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Uranium, 1))
            .itemOutputs(
                Materials.Uranium.getDust(1),
                Materials.Lead.getDust(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1))
            .outputChances(100_00, 5_56, 10_00, 5_00, 5_00, 5_00)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Uraninite, 1))
            .itemOutputs(
                Materials.Uraninite.getDust(1),
                Materials.Uranium.getDust(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1))
            .outputChances(100_00, 5_56, 5_00, 2_50, 2_50, 2_50)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Pitchblende, 1))
            .itemOutputs(
                Materials.Pitchblende.getDust(1),
                Materials.Lead.getDust(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1))
            .outputChances(100_00, 5_56, 5_00, 2_50, 2_50, 2_50)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);
    }

    private static void fluidHeater() {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.SODIUM_FLUORIDE.getDust(1))
            .fluidOutputs(MaterialsFluorides.SODIUM_FLUORIDE.getFluidStack(1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);
    }
}
