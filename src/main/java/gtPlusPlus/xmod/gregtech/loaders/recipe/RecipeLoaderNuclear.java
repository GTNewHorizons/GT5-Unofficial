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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
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
            .itemInputs(MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1))
            .itemOutputs(GregtechItemList.ZirconiumPellet.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (4_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);
    }

    private static void blastFurnace() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.LithiumFluoride, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.BerylliumFluoride, Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.LithiumTetrafluoroberyllateLFTB, Shapes.dust, 7))
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
            .itemInputs(MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, (int) (12)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 5))
            .outputChances(90_00, 60_00, 30_00)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (2_400)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, (int) (5)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Hafnium, Shapes.dust, 1))
            .outputChances(90_00, 30_00, 10_00)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (4_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.LithiumCarbonateDust.get(3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.LithiumFluoride, Shapes.dust, 2))
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
            .itemOutputs(MaterialLibAPI.getStack(Materials.LithiumFluoride, Shapes.dust, 2))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 500))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (8)),
                MaterialLibAPI.getStack(Materials.Lithium7, Shapes.dust, 16))
            .itemOutputs(ItemList.Cell_Empty.get(8))
            .fluidInputs(GTUtility.getWater(8_000))
            .fluidOutputs(new FluidStack(GTPPFluids.LithiumHydroxide, 48 * INGOTS))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // LFTR Fuel Related Compounds
        // Beryllium Hydroxide
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Beryllium, Shapes.dust, 1))
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.BerylliumHydroxide, 3 * INGOTS))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // Ammonium Bifluoride
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1))
            .circuit(3)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.Ammonia, 1_000))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.AmmoniumBifluoride, 4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ammonium Bifluoride
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrofluoricAcidGT5U, CellShapes.cell, 2))
            .circuit(3)
            .itemOutputs(ItemList.Cell_Empty.get(2))
            .fluidInputs(MaterialUtils.anyFluid(Materials.Ammonia, 1_000))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.AmmoniumBifluoride, 4 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ammonium
        // To be deprecated now that it is no longer needed for ammonium bifluoride
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (1)))
            .circuit(3)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.Ammonium, 2_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Sodium Fluoride

        // Circuit 15 causes recipes added to UniversalChemical to be single-block only, so we manually circumvent this
        // logic here instead of changing the circuit
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(15)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumFluoride, Shapes.dust, 2))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 500))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(15)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumFluoride, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(15)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumFluoride, Shapes.dust, 2))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 500))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(15)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumFluoride, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void dehydratorRecipes() {
        // Makes 7-Lithium
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.SulfuricLithiumMix, 10 * INGOTS))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Lithium7, Shapes.dust, 4))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalDehydratorRecipes);

        // Makes Lithium Carbonate
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Cell_Empty.get(12),
                MaterialLibAPI.getStack(Materials.Lepidolite, Shapes.dust, (int) (20)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Potassium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials.Oxygen, CellShapes.cell, (int) (10)),
                MaterialLibAPI.getStack(Materials.Fluorine, CellShapes.cell, (int) (2)),
                GregtechItemList.LithiumCarbonateDust.get(3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, (int) (10_000)))
            .fluidOutputs(new FluidStack(GTPPFluids.SulfuricLithiumMix, 10_000))
            .eut(1_000)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Calcium Hydroxide
        // CaO + H2O = Ca(OH)2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, (int) (2)))
            .itemOutputs(GregtechItemList.CalciumHydroxideDust.get(5))
            .fluidInputs(GTUtility.getWater(1_000))
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
        FluidStack aHydrogenChloride = MaterialLibAPI
            .getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 800);
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CookedZrCl4Dust.get(1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.ZirconiumTetrafluoride, Shapes.dust, 1))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 400))
            .fluidOutputs(aHydrogenChloride)
            .eut(TierEU.RECIPE_HV)
            .duration(15 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Zirconium Chloride -> TetraFluoride
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CookedZrCl4Dust.get(1))
            .circuit(10)
            .itemOutputs(MaterialLibAPI.getStack(Materials.ZirconiumTetrafluoride, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 800))
            .fluidOutputs(aHydrogenChloride)
            .eut(TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Be(OH)2 + 2 (NH4)HF2 → (NH4)2BeF4 + 2 H2O
        // Inputs use solid rule because they are molten forms of solids
        // Outputs use fluid rule because they are not molten forms of solids
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.BerylliumHydroxide, Shapes.dust, 3),
                ItemList.Cell_Empty.get(2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 2))
            .fluidInputs(MaterialUtils.anyFluid(Materials.AmmoniumBifluoride, 8 * INGOTS))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.AmmoniumTetrafluoroberyllate, 1_000))
            .eut(TierEU.RECIPE_MV / 2)
            .duration(6 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.BerylliumHydroxide, Shapes.dust, 3))
            .fluidInputs(MaterialUtils.anyFluid(Materials.AmmoniumBifluoride, 8 * INGOTS))
            .fluidOutputs(
                GTUtility.getWater(2_000),
                MaterialUtils.anyFluid(Materials.AmmoniumTetrafluoroberyllate, 1_000))
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
                MaterialLibAPI.getStack(Materials.Ammonia, CellShapes.cell, 2),
                ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1),
                MaterialLibAPI.getStack(Materials.BerylliumFluoride, Shapes.dust, 3))
            .fluidInputs(MaterialUtils.anyFluid(Materials.AmmoniumTetrafluoroberyllate, 1_000))
            .eut(TierEU.RECIPE_MV)
            .duration(5 * MINUTES)
            .addTo(chemicalDehydratorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(17)
            .itemOutputs(MaterialLibAPI.getStack(Materials.BerylliumFluoride, Shapes.dust, 3))
            .fluidInputs(MaterialUtils.anyFluid(Materials.AmmoniumTetrafluoroberyllate, 1_000))
            .fluidOutputs(
                MaterialUtils.anyFluid(Materials.Ammonia, 2_000),
                new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 1_000))
            .eut(TierEU.RECIPE_MV)
            .duration(5 * MINUTES)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void electroMagneticSeperator() {
        // Zirconium
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Bauxite, Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Bauxite, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dustSmall, (int) (1)),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.nugget, 1))
            .outputChances(100_00, 25_00, 40_00)
            .duration(20 * SECONDS)
            .eut(24)
            .addTo(electroMagneticSeparatorRecipes);

        // Zircon
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Magnetite, Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Magnetite, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Zircon, Shapes.dustSmall, 1),
                MaterialLibAPI.getStack(Materials.Zircon, Shapes.dustTiny, 1))
            .outputChances(100_00, 12_50, 25_00)
            .duration(20 * SECONDS)
            .eut(24)
            .addTo(electroMagneticSeparatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Cassiterite, Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Cassiterite, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Zircon, Shapes.dustSmall, 1),
                MaterialLibAPI.getStack(Materials.Zircon, Shapes.dustTiny, 1))
            .outputChances(100_00, 12_50, 25_00)
            .duration(20 * SECONDS)
            .eut(24)
            .addTo(electroMagneticSeparatorRecipes);
    }

    private static void fluidExtractorRecipes() {
        // FLiBe fuel
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LithiumTetrafluoroberyllateLFTB, Shapes.dust, 1))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.LithiumTetrafluoroberyllateLFTB, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);

        // Lithium Fluoride
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LithiumFluoride, Shapes.dust, 1))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.LithiumFluoride, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);

        // Lithium Fluoride
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.BerylliumFluoride, Shapes.dust, 1))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.BerylliumFluoride, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);

        // LFTR Fuel 1
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LFTRFuel1, Shapes.dust, 1))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.LFTRFuel1, 1 * INGOTS))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(1000)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LFTRFuel2, Shapes.dust, 1))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.LFTRFuel2, 1 * INGOTS))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(1500)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LFTRFuel3, Shapes.dust, 1))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.LFTRFuel3, 1 * INGOTS))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidExtractionRecipes);

        // ZIRCONIUM_TETRAFLUORIDE
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ZirconiumTetrafluoride, Shapes.dust, 1))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.ZirconiumTetrafluoride, 1 * INGOTS))
            .duration(10 * SECONDS)
            .eut(512 + 256)
            .addTo(fluidExtractionRecipes);
    }

    private static void fusionChainRecipes() {
        // Mk1
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidPlasma, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Calcium, FluidShapes.fluidPlasma, (int) (1 * NUGGETS)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neon), 1_000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 100_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neon), 1 * INGOTS),
                new FluidStack(MaterialUtils.anyFluidOf(Materials.Arcanite), 2))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Force), 1_000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 100_000_000L)
            .addTo(fusionRecipes);

        // Mk2
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Niobium, FluidShapes.fluidPlasma, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Zinc, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Krypton), 1 * INGOTS))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Krypton), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Force), 1_000))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.AstralTitanium), 1_000))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.AstralTitanium), 1 * INGOTS),
                new FluidStack(MaterialUtils.anyFluidOf(Materials.Titansteel), 2))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Runite), 1_000))
            .duration(32 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(FUSION_THRESHOLD, 300_000_000L)
            .addTo(fusionRecipes);

        // Mk3
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.Curium, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidPlasma, (int) (1 * INGOTS)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Xenon), 1 * INGOTS))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Xenon), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Runite), 1_000))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.AdvancedNitinol), 1_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 500_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.AdvancedNitinol), 72),
                MaterialLibAPI.getFluidStack(Materials.Tartarite, FluidShapes.fluidMolten, (int) (2)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.CelestialTungsten), 1_000))
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
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Uranium233, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (1)))
            .fluidInputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 10_000))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.UraniumTetrafluoride, 25 * INGOTS))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

    }

    private static void sifter() {
        // Zirconium
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ilmenite, Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.CastIron, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Hafnium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Hafnium, Shapes.dust, 1))
            .outputChances(50_00, 2_78, 10_00, 10_00, 3_00, 3_00)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Tin, Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1))
            .outputChances(100_00, 5_56, 15_00, 10_00, 5_00, 5_00)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Cassiterite, Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Cassiterite, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1))
            .outputChances(100_00, 5_56, 15_00, 10_00, 5_00, 5_00)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        // Radium
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Thorium, Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Lead, Shapes.dust, (int) (1)),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1))
            .outputChances(100_00, 5_00, 3_00, 2_00, 1_00, 1_00)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Uranium, Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Lead, Shapes.dust, (int) (1)),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1))
            .outputChances(100_00, 5_56, 10_00, 5_00, 5_00, 5_00)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Uraninite, Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Uraninite, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (1)),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1),
                GregtechItemList.Radium226Dust.get(1))
            .outputChances(100_00, 5_56, 5_00, 2_50, 2_50, 2_50)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Pitchblende, Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Pitchblende, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Lead, Shapes.dust, (int) (1)),
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
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumFluoride, Shapes.dust, 1))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.SodiumFluoride, 1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);
    }
}
