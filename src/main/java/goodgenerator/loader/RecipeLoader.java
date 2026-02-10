package goodgenerator.loader;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.PRECISE_ASSEMBLER_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gregtech.loaders.postload.MachineRecipeLoader.solderingMats;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import goodgenerator.util.MaterialFix;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;

public class RecipeLoader {

    public static void RecipeLoad() {

        // Radiation Protection Plate
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Iridium, 8L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 8L))
            .circuit(1)
            .fluidInputs(Materials.Lead.getMolten(8 * INGOTS))
            .itemOutputs(ItemRefer.Radiation_Protection_Plate.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Lanthanum, 4L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 8L))
            .circuit(1)
            .fluidInputs(Materials.Lead.getMolten(8 * INGOTS))
            .itemOutputs(ItemRefer.Radiation_Protection_Plate.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // LNR Controller - Large Naquadah Reactor
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemList.Generator_Naquadah_Mark_III.get(1)
                    .copy())
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Adamantium, 8),
                ItemRefer.Radiation_Protection_Plate.get(16),
                ItemList.Field_Generator_ZPM.get(2),
                ItemList.Electric_Pump_ZPM.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorLuV, 8),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Naquadah, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.PrismaticNaquadah, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Osmium, 16))
            .fluidInputs(
                Materials.Trinium.getMolten(4 * INGOTS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(32 * INGOTS),
                Materials.Lubricant.getFluid(8_000))
            .itemOutputs(ItemRefer.Large_Naquadah_Reactor.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // LNR Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Radiation_Protection_Plate.get(6),
                ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 1),
                ItemList.Field_Generator_MV.get(1))
            .circuit(1)
            .itemOutputs(ItemRefer.Field_Restriction_Casing.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // LNR Frame
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.PrismaticNaquadah, 8),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSE, 4))
            .circuit(24)
            .itemOutputs(ItemRefer.Radiation_Proof_Steel_Frame_Box.get(1))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Uranium Liquid Fuel Process Line
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.graphiteUraniumMixture.get(OrePrefixes.dust, 4),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.TungstenCarbide, 2))
            .circuit(1)
            .itemOutputs(ItemRefer.Wrapped_Uranium_Ingot.get(1))
            .duration(1 * MINUTES + 10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Wrapped_Uranium_Ingot.get(4))
            .itemOutputs(
                ItemRefer.High_Density_Uranium_Nugget.get(1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenCarbide, 8))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 8)
            .addTo(implosionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.High_Density_Uranium_Nugget.get(9))
            .itemOutputs(ItemRefer.High_Density_Uranium.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.High_Density_Uranium.get(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Quantium, 4))
            .circuit(1)
            .fluidInputs(Materials.Radon.getGas(1_000))
            .fluidOutputs(GGMaterial.uraniumBasedLiquidFuel.getFluidOrGas(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(GGMaterial.uraniumBasedLiquidFuel.getFluidOrGas(10), Materials.Hydrogen.getGas(100L))
            .fluidOutputs(GGMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(10))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(FUSION_THRESHOLD, 200_000_000L)
            .addTo(fusionRecipes);

        // Thorium Liquid Process Line
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.uraniumCarbideThoriumMixture.get(OrePrefixes.dust, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.TungstenSteel, 4))
            .circuit(1)
            .itemOutputs(ItemRefer.Wrapped_Thorium_Ingot.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Wrapped_Thorium_Ingot.get(1))
            .itemOutputs(
                ItemRefer.High_Density_Thorium_Nugget.get(1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenSteel, 8))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 4)
            .addTo(implosionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.High_Density_Thorium_Nugget.get(9))
            .itemOutputs(ItemRefer.High_Density_Thorium.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.High_Density_Thorium.get(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Draconium, 2))
            .circuit(2)
            .fluidInputs(Materials.Mercury.getFluid(1_000))
            .fluidOutputs(GGMaterial.thoriumBasedLiquidFuel.getFluidOrGas(4_000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.thoriumBasedLiquidFuel.get(OrePrefixes.cell, 1))
            .circuit(1)
            .fluidInputs(Materials.Helium.getPlasma(250L))
            .itemOutputs(GGMaterial.thoriumBasedLiquidFuelExcited.get(OrePrefixes.cell, 1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(mixerRecipes);

        // Liquid Plutonium Process Line
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.plutoniumOxideUraniumMixture.get(OrePrefixes.dust, 8),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.HSSS, 4))
            .circuit(1)
            .itemOutputs(ItemRefer.Wrapped_Plutonium_Ingot.get(1))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Wrapped_Plutonium_Ingot.get(2))
            .itemOutputs(
                ItemRefer.High_Density_Plutonium_Nugget.get(1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.HSSS, 8))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(ADDITIVE_AMOUNT, 16)
            .addTo(implosionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.High_Density_Plutonium_Nugget.get(9))
            .itemOutputs(ItemRefer.High_Density_Plutonium.get(1))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.High_Density_Plutonium.get(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 2))
            .circuit(1)
            .fluidOutputs(GGMaterial.plutoniumBasedLiquidFuel.getFluidOrGas(1_000))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.Lutetium.getMolten(1 * NUGGETS),
                GGMaterial.plutoniumBasedLiquidFuel.getFluidOrGas(20))
            .fluidOutputs(GGMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(20))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LuV / 2)
            .metadata(FUSION_THRESHOLD, 220_000_000L)
            .addTo(fusionRecipes);

        // Th-232
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 24),
                GGMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 0))
            .circuit(1)
            .fluidInputs(Materials.NitricAcid.getFluid(6_000))
            .fluidOutputs(GGMaterial.oxalate.getFluidOrGas(3_000), Materials.NitricOxide.getGas(6_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        // Th + 2O = ThO2
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 1))
            .fluidInputs(Materials.Oxygen.getGas(2_000))
            .itemOutputs(WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 3))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        // Th + 8HNO3 =HF= Th(NO3)4 + 4NO2 + 4H2O
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Thorium.getDust(1))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(100), Materials.NitricAcid.getFluid(8_000))
            .fluidOutputs(GGMaterial.thoriumNitrate.getFluidOrGas(1_000), Materials.NitrogenDioxide.getGas(4_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        // 4NaOH + Th(NO3)4 = Th(OH)4 + 4NaNO3
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(12))
            .circuit(1)
            .fluidInputs(GGMaterial.thoriumNitrate.getFluidOrGas(1_000))
            .itemOutputs(
                GGMaterial.thoriumHydroxide.get(OrePrefixes.dust, 9),
                WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 20))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // 2 NaNO3 + H2SO4 = Na2SO4 + 2HNO3
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 10))
            .circuit(1)
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_000))
            .fluidOutputs(Materials.NitricAcid.getFluid(2_000))
            .itemOutputs(WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 7))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Th(OH)4 + 4HF = ThF4 + 4H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.thoriumHydroxide.get(OrePrefixes.dust, 9))
            .circuit(1)
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .fluidOutputs(MaterialsFluorides.THORIUM_TETRAFLUORIDE.getFluidStack(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zn + 2Cl = ZnCl2
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1))
            .circuit(1)
            .fluidInputs(Materials.Chlorine.getGas(2_000))
            .itemOutputs(GGMaterial.zincChloride.get(OrePrefixes.dust, 3))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // ZnCl2 + 3Ca + ThF4 = ZnTh + CaCl2 + 2CaF2
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.zincChloride.get(OrePrefixes.dust, 3), Materials.Calcium.getDust(3))
            .fluidInputs(GGMaterial.thorium232Tetrafluoride.getFluidOrGas(1_000))
            .fluidOutputs(WerkstoffLoader.CalciumChloride.getFluidOrGas(3_000))
            .itemOutputs(
                GGMaterial.zincThoriumAlloy.get(OrePrefixes.ingot, 1),
                WerkstoffLoader.Fluorspar.get(OrePrefixes.dust, 6))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 3000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.zincThoriumAlloy.get(OrePrefixes.ingot, 1))
            .circuit(11)
            .fluidOutputs(Materials.Zinc.getMolten(1 * INGOTS))
            .itemOutputs(WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1900)
            .metadata(ADDITIVE_AMOUNT, 250)
            .addTo(BlastFurnaceWithGas);

        // 2V + 5O = V2O5
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 2))
            .circuit(24)
            .fluidInputs(Materials.Oxygen.getGas(5_000))
            .itemOutputs(GGMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 7))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 2500)
            .addTo(blastFurnaceRecipes);

        // Atomic Separation Catalyst
        ItemStack[] mat1 = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 32),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Firestone, 4) };
        ItemStack[] mat2 = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Draconium, 4),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Quantium, 4), };
        ItemStack[] mat3 = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ardite, 4),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Manyullyn, 4) };

        for (ItemStack m1 : mat1) {
            for (ItemStack m2 : mat2) {
                for (ItemStack m3 : mat3) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(m1, m2, m3)
                        .circuit(4)
                        .fluidInputs(Materials.Naquadah.getMolten(2 * INGOTS))
                        .itemOutputs(ItemRefer.Raw_Atomic_Separation_Catalyst.get(63))
                        .duration(15 * SECONDS)
                        .eut(TierEU.RECIPE_HV)
                        .addTo(mixerRecipes);
                }
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.Tiberium.get(OrePrefixes.plate, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 8))
            .itemOutputs(GGMaterial.orundum.get(OrePrefixes.plate, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.orundum.get(OrePrefixes.plate, 2), ItemRefer.Raw_Atomic_Separation_Catalyst.get(4))
            .fluidInputs(Materials.Plutonium.getMolten(1 * INGOTS))
            .itemOutputs(GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot, 1))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 5000)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot, 1))
            .itemOutputs(GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingot, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(vacuumFreezerRecipes);

        CrackRecipeAdder.crackerAdder(
            GGMaterial.naquadahGas.getFluidOrGas(1_000),
            GGMaterial.atomicSeparationCatalyst.getMolten(4),
            new FluidStack[] { Materials.Helium.getGas(300), WerkstoffLoader.Neon.getFluidOrGas(50),
                Materials.Argon.getGas(80), WerkstoffLoader.Krypton.getFluidOrGas(20),
                WerkstoffLoader.Xenon.getFluidOrGas(40), Materials.Radon.getGas(14_000) },
            GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadah, 1),
            6,
            4020,
            300);

        CrackRecipeAdder.crackerAdder(
            GGMaterial.lightNaquadahFuel.getFluidOrGas(1_000),
            GGMaterial.atomicSeparationCatalyst.getMolten(4),
            new FluidStack[] { Materials.Radon.getGas(1_400), GGMaterial.naquadahGas.getFluidOrGas(400),
                Materials.Uranium.getMolten(9 * HALF_INGOTS), GGMaterial.heavyNaquadahFuel.getFluidOrGas(280),
                Materials.Plutonium.getMolten(4 * INGOTS), GGMaterial.naquadahAsphalt.getFluidOrGas(140) },
            GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium235, 1),
            6,
            4020,
            450);

        CrackRecipeAdder.crackerAdder(
            GGMaterial.heavyNaquadahFuel.getFluidOrGas(1_000),
            GGMaterial.atomicSeparationCatalyst.getMolten(6),
            new FluidStack[] { Materials.Radon.getGas(1_000), GGMaterial.naquadahGas.getFluidOrGas(450),
                GGMaterial.lightNaquadahFuel.getFluidOrGas(560), Materials.Uranium.getMolten(5 * INGOTS),
                Materials.Lutetium.getMolten(9 * HALF_INGOTS), GGMaterial.naquadahAsphalt.getFluidOrGas(240) },
            GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1),
            6,
            4020,
            500);

        CrackRecipeAdder.crackerAdder(
            GGMaterial.naquadahAsphalt.getFluidOrGas(1_000),
            GGMaterial.atomicSeparationCatalyst.getMolten(12),
            new FluidStack[] { GGMaterial.lightNaquadahFuel.getFluidOrGas(600), Materials.Uranium.getMolten(8 * INGOTS),
                Materials.Thorium.getMolten(6 * INGOTS), Materials.Plutonium.getMolten(11 * HALF_INGOTS),
                Materials.Thulium.getMolten(216), GGMaterial.heavyNaquadahFuel.getFluidOrGas(350) },
            GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadria, 1),
            6,
            4020,
            800);

        // 2C2H6O =H2SO4= C4H10O + H2O
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(Materials.Ethanol.getFluid(1_000), Materials.SulfuricAcid.getFluid(1_000))
            .fluidOutputs(GGMaterial.ether.getFluidOrGas(500), Materials.DilutedSulfuricAcid.getFluid(1_500))
            .duration(25 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.GasolineRaw.getCells(9), Materials.Ethanol.getCells(1))
            .itemOutputs(GGMaterial.ethanolGasoline.get(OrePrefixes.cell, 10))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ether.get(OrePrefixes.cell))
            .metadata(FUEL_VALUE, 537)
            .metadata(FUEL_TYPE, 0)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ether.get(OrePrefixes.cell))
            .metadata(FUEL_VALUE, 537)
            .metadata(FUEL_TYPE, 1)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ethanolGasoline.get(OrePrefixes.cell))
            .metadata(FUEL_VALUE, 1100)
            .metadata(FUEL_TYPE, 0)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.cyclopentadiene.get(OrePrefixes.cell))
            .metadata(FUEL_VALUE, 70)
            .metadata(FUEL_TYPE, 1)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ironedFuel.get(OrePrefixes.cell))
            .metadata(FUEL_VALUE, 2248)
            .metadata(FUEL_TYPE, 0)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ironedKerosene.get(OrePrefixes.cell))
            .metadata(FUEL_VALUE, 1824)
            .metadata(FUEL_TYPE, 0)
            .addTo(GTRecipeConstants.Fuel);

        // Sb + 3Cl = SbCl3
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Antimony, 1))
            .circuit(1)
            .fluidInputs(GGMaterial.ether.getFluidOrGas(1_000), Materials.Chlorine.getGas(3_000))
            .fluidOutputs(GGMaterial.antimonyTrichloride.getFluidOrGas(1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // SbCl3 + 2Cl = SbCl5
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.antimonyTrichloride.getFluidOrGas(1_000), Materials.Chlorine.getGas(2_000))
            .fluidOutputs(GGMaterial.antimonyPentachlorideSolution.getFluidOrGas(1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            GGMaterial.antimonyPentachlorideSolution.getFluidOrGas(1_000),
            new FluidStack[] { GGMaterial.ether.getFluidOrGas(1_000),
                GGMaterial.antimonyPentachloride.getFluidOrGas(1_000) },
            GTValues.NI,
            30 * SECONDS,
            TierEU.RECIPE_MV);

        // SbCl5 + 5HF = SbF5 + 5HCl
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                GGMaterial.antimonyPentachloride.getFluidOrGas(1_000),
                Materials.HydrofluoricAcid.getFluid(5_000))
            .fluidOutputs(
                GGMaterial.antimonyPentafluoride.getFluidOrGas(1_000),
                Materials.HydrochloricAcid.getFluid(5_000))
            .duration(21 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // SbH5 + HF = HSbF6
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                GGMaterial.antimonyPentafluoride.getFluidOrGas(1_000),
                Materials.HydrofluoricAcid.getFluid(1_000))
            .fluidOutputs(GGMaterial.fluoroantimonicAcid.getFluidOrGas(1_000))
            .duration(42 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 16))
            .circuit(16)
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(3_000))
            .fluidOutputs(GGMaterial.acidNaquadahEmulsion.getFluidOrGas(2_000))
            .itemOutputs(GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 3))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, 8))
            .circuit(3)
            .fluidInputs(GGMaterial.acidNaquadahEmulsion.getFluidOrGas(1_000))
            .fluidOutputs(GGMaterial.naquadahEmulsion.getFluidOrGas(1_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.AntimonyTrioxide, 1),
                WerkstoffLoader.Fluorspar.get(OrePrefixes.dust, 4))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            GGMaterial.naquadahSolution.getFluidOrGas(20),
            new FluidStack[] { GGMaterial.naquadahAsphalt.getFluidOrGas(2),
                GGMaterial.heavyNaquadahFuel.getFluidOrGas(5), GGMaterial.lightNaquadahFuel.getFluidOrGas(10),
                FluidRegistry.getFluidStack("water", 10), GGMaterial.naquadahGas.getFluidOrGas(60) },
            GTValues.NI,
            1 * SECONDS,
            TierEU.RECIPE_EV);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.naquadahGas.get(OrePrefixes.cell))
            .metadata(FUEL_VALUE, 1024)
            .metadata(FUEL_TYPE, 1)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                GGMaterial.lightNaquadahFuel.getFluidOrGas(780),
                GGMaterial.heavyNaquadahFuel.getFluidOrGas(360))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkI.getFluidOrGas(100))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(FUSION_THRESHOLD, 320_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.ElectrumFlux, 32))
            .circuit(1)
            .fluidInputs(
                GGMaterial.naquadahBasedFuelMkI.getFluidOrGas(100),
                GGMaterial.naquadahGas.getFluidOrGas(1_500))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkII.getFluidOrGas(100))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_UHV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 32))
            .circuit(16)
            .fluidInputs(GGMaterial.fluoroantimonicAcid.getFluidOrGas(4_000))
            .fluidOutputs(GGMaterial.acidNaquadahEmulsion.getFluidOrGas(8_000))
            .itemOutputs(GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 17))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_IV / 2)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        // Naquadah Fuel Refinery
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemList.Generator_Naquadah_Mark_V.get(1)
                    .copy())
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 8),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(64),
                ItemList.Field_Generator_UV.get(8),
                ItemList.Electric_Pump_UHV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.MysteriousCrystal, 8),
                ItemList.Circuit_Wafer_NPIC.get(16),
                ItemList.UHV_Coil.get(64),
                new Object[] { "craftingLensYellow", 16 },
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Thulium, 64))
            .fluidInputs(
                Materials.Quantium.getMolten(1 * STACKS),
                Materials.DraconiumAwakened.getMolten(32 * INGOTS),
                GGMaterial.extremelyUnstableNaquadah.getMolten(10 * INGOTS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(1 * STACKS + 36 * INGOTS))
            .itemOutputs(ItemRefer.Naquadah_Fuel_Refinery.get(1))
            .eut(TierEU.RECIPE_UHV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Advanced Radiation Proof Plate
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Radiation_Protection_Plate.get(1))
            .metadata(SCANNING, new Scanning(40 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                ItemRefer.Radiation_Protection_Plate.get(2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.ElectrumFlux, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Trinium, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.VibrantAlloy, 4),
                ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(4))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(8 * INGOTS))
            .itemOutputs(ItemRefer.Advanced_Radiation_Protection_Plate.get(1))
            .eut(TierEU.RECIPE_ZPM / 2)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        // Naquadah Fuel Refinery Casing
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Casing.get(1))
            .metadata(SCANNING, new Scanning(30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Thulium, 1),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(6),
                ItemList.Field_Generator_IV.get(2),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 16),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NaquadahAlloy, 64),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Manyullyn, 64),
                GGMaterial.orundum.get(OrePrefixes.plate, 4))
            .fluidInputs(
                Materials.TungstenSteel.getMolten(8 * INGOTS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(16 * INGOTS))
            .itemOutputs(ItemRefer.Naquadah_Fuel_Refinery_Casing.get(1))
            .eut(TierEU.RECIPE_ZPM / 2)
            .duration(25 * SECONDS)
            .addTo(AssemblyLine);

        if (NewHorizonsCoreMod.isModLoaded()) {

            // Field Restriction Coil
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingot))
                .metadata(SCANNING, new Scanning(40 * SECONDS, TierEU.RECIPE_ZPM))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmium, 1),
                    ItemList.Field_Generator_UV.get(2),
                    ItemList.Electric_Pump_UV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Americium, 8),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.BlackPlutonium, 16),
                    ItemList.Circuit_Wafer_PPIC.get(32),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1L })
                .fluidInputs(
                    WerkstoffLoader.Krypton.getFluidOrGas(1_000),
                    Materials.ElectrumFlux.getMolten(1 * STACKS),
                    Materials.Lubricant.getFluid(128_000))
                .itemOutputs(ItemRefer.Field_Restriction_Coil_T1.get(1))
                .eut(TierEU.RECIPE_ZPM)
                .duration(60 * SECONDS)
                .addTo(AssemblyLine);

            // Advanced Field Restriction Coil
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Coil_T1.get(1))
                .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_UV))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                    ItemList.Field_Generator_UHV.get(2),
                    ItemList.Electric_Pump_UHV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 8),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 16),
                    ItemList.Circuit_Wafer_PPIC.get(48),
                    new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L })
                .fluidInputs(
                    Materials.Radon.getPlasma(1_000),
                    Materials.DraconiumAwakened.getMolten(1 * STACKS),
                    Materials.Lubricant.getFluid(128_000))
                .itemOutputs(ItemRefer.Field_Restriction_Coil_T2.get(1))
                .eut(TierEU.RECIPE_UV)
                .duration(60 * SECONDS)
                .addTo(AssemblyLine);

            // Ultimate Field Restriction Coil
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Coil_T2.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_UHV))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                    ItemList.Field_Generator_UEV.get(2),
                    ItemList.Electric_Pump_UEV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TranscendentMetal, 8),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Infinity, 16),
                    ItemList.Circuit_Wafer_PPIC.get(64),
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L })
                .fluidInputs(
                    WerkstoffLoader.Oganesson.getFluidOrGas(1_000),
                    Materials.Neutronium.getMolten(1 * STACKS),
                    Materials.DimensionallyShiftedSuperfluid.getFluid(64_000))
                .itemOutputs(ItemRefer.Field_Restriction_Coil_T3.get(1))
                .eut(TierEU.RECIPE_UHV)
                .duration(60 * SECONDS)
                .addTo(AssemblyLine);

            // Temporal Field Restriction Coil
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Coil_T3.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES + 10 * SECONDS, TierEU.RECIPE_UEV))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1),
                    ItemList.Field_Generator_UIV.get(2),
                    ItemList.Electric_Pump_UIV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.SpaceTime, 8),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.SpaceTime, 16),
                    ItemList.Circuit_Wafer_PPIC.get(64),
                    new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1L })
                .fluidInputs(
                    GGMaterial.metastableOganesson.getMolten(1_000),
                    Materials.TranscendentMetal.getMolten(1 * STACKS),
                    Materials.DimensionallyShiftedSuperfluid.getFluid(64_000))
                .itemOutputs(ItemRefer.Field_Restriction_Coil_T4.get(1))
                .eut(TierEU.RECIPE_UEV)
                .duration(60 * SECONDS)
                .addTo(AssemblyLine);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BorosilicateGlass, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.ReinforcedGlass, 6),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 32),
                ItemList.Field_Generator_HV.get(4))
            .circuit(6)
            .fluidInputs(Materials.Naquadria.getMolten(2 * INGOTS))
            .itemOutputs(ItemRefer.Field_Restriction_Glass.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Ca + O = CaO
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1))
            .circuit(1)
            .fluidInputs(Materials.Oxygen.getGas(1_000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, 2))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // AlN = Al + N
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1))
            .fluidOutputs(Materials.Nitrogen.getGas(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Aluminum_Nitride_Dust.get(4), WerkstoffLoader.YttriumOxide.get(OrePrefixes.dust, 5))
            .circuit(9)
            .fluidInputs(Materials.GlueAdvanced.getFluid(1_000))
            .itemOutputs(ItemRefer.Special_Ceramics_Dust.get(9))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Aluminum_Nitride_Dust.get(4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uraninite, 5))
            .circuit(9)
            .fluidInputs(Materials.GlueAdvanced.getFluid(1_000))
            .itemOutputs(ItemRefer.Special_Ceramics_Dust.get(9))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Special_Ceramics_Dust.get(2), ItemList.Shape_Extruder_Plate.get(0L))
            .itemOutputs(ItemRefer.Special_Ceramics_Plate.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(extruderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Special_Ceramics_Dust.get(2), ItemList.Shape_Mold_Casing.get(0))
            .itemOutputs(ItemRefer.Special_Ceramics_Plate.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(formingPressRecipes);

        // Advanced Recipe with netherite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Netherite, 1),
                ItemList.Shape_Mold_Casing.get(0))
            .itemOutputs(ItemRefer.Special_Ceramics_Plate.get(32))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(formingPressRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Raw_Cylinder.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "PFP", "PPP", 'P', ItemRefer.Special_Ceramics_Plate.get(1), 'F',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Raw_Cylinder.get(1),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Titanium, 1),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Titanium, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6))
            .circuit(4)
            .itemOutputs(ItemRefer.Titanium_Plated_Cylinder.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2))
            .circuit(16)
            .fluidInputs(Materials.LiquidOxygen.getGas(1_000), Materials.NitrogenDioxide.getGas(1_000))
            .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 2))
            .circuit(16)
            .fluidInputs(Materials.LiquidOxygen.getGas(1_000), Materials.NitrogenDioxide.getGas(1_000))
            .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2))
            .circuit(16)
            .fluidInputs(
                FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 1_000),
                Materials.NitrogenDioxide.getGas(1_000))
            .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 2))
            .circuit(16)
            .fluidInputs(
                FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 1_000),
                Materials.NitrogenDioxide.getGas(1_000))
            .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Universal_Chemical_Fuel_Engine.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "TZT", "ALB", "WGW", 'T',
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 1), 'Z', "circuitUltimate", 'A',
                ItemList.Machine_Multi_DieselEngine.get(1), 'B', ItemList.Machine_Multi_ExtremeDieselEngine.get(1), 'L',
                ItemList.Hull_LuV, 'W', GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 1), 'G',
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Osmium, 1), });

        // neutron activator
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.PolyvinylChloride, 4),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Polyethylene, 4))
            .circuit(8)
            .fluidInputs(FluidRegistry.getFluidStack("dye.chemical.dyecyan", 1 * INGOTS))
            .itemOutputs(ItemRefer.Plastic_Case.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.PolyvinylChloride, 4),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Polyethylene, 4))
            .circuit(8)
            .fluidInputs(FluidRegistry.getFluidStack("dye.watermixed.dyecyan", 1 * INGOTS))
            .itemOutputs(ItemRefer.Plastic_Case.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Plastic_Case.get(1),
            new Object[] { "PCP", "CDC", "PCP", 'P',
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.PolyvinylChloride, 1), 'C',
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Polyethylene, 1), 'D', "dyeCyan" });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Coated_Basic.get(1),
                ItemList.Circuit_Chip_ULPIC.get(1),
                ItemList.ULV_Coil.get(2),
                ItemList.Battery_RE_ULV_Tantalum.get(1))
            .itemOutputs(ItemRefer.Micro_Heater.get(1))
            .fluidInputs(Materials.RedAlloy.getMolten(1 * INGOTS), Materials.Aluminium.getMolten(1 * INGOTS))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Quartzite, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 4))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .itemOutputs(ItemRefer.Quartz_Wafer.get(1))
            .outputChances(3333)
            .requiresCleanRoom()
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Quartzite, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 4))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .itemOutputs(ItemRefer.Quartz_Wafer.get(1))
            .requiresCleanRoom()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Quartz_Wafer.get(1),
                ItemRefer.Special_Ceramics_Plate.get(2),
                ItemRefer.Micro_Heater.get(1),
                ItemList.Circuit_Chip_ILC.get(4))
            .itemOutputs(ItemRefer.Quartz_Crystal_Resonator.get(1))
            .fluidInputs(
                Materials.EnergeticAlloy.getMolten(1 * HALF_INGOTS),
                Materials.Silver.getMolten(1 * EIGHTH_INGOTS))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);

        for (Materials tMat : solderingMats) {
            int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.Quartz_Crystal_Resonator.get(2),
                    ItemRefer.Plastic_Case.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1),
                    ItemList.Cover_Screen.get(1),
                    GTOreDictUnificator.get(OrePrefixes.componentCircuit, Materials.Diode, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8))
                .fluidInputs(tMat.getMolten(tMultiplier * INGOTS))
                .itemOutputs(ItemRefer.Inverter.get(1))
                .duration(12 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.Quartz_Crystal_Resonator.get(2),
                    ItemRefer.Plastic_Case.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1),
                    ItemList.Cover_Screen.get(1),
                    ItemList.Circuit_Parts_DiodeASMD.get(4),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8))
                .fluidInputs(tMat.getMolten(tMultiplier * INGOTS))
                .itemOutputs(ItemRefer.Inverter.get(1))
                .duration(12 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);
            if (NewHorizonsCoreMod.isModLoaded()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
                        GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedGoldChip", 16),
                        ItemList.Circuit_Chip_SoC2.get(8),
                        ItemList.Circuit_Chip_NOR.get(32),
                        GGMaterial.signalium.get(OrePrefixes.bolt, 32),
                        GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8),
                        GTModHandler.getIC2Item("reactorVent", 1L, 1))
                    .fluidInputs(tMat.getMolten(2 * tMultiplier * INGOTS))
                    .itemOutputs(ItemRefer.HiC_T1.get(1))
                    .duration(1 * MINUTES)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
                        GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedGoldChip", 16),
                        ItemList.Circuit_Chip_SoC2.get(8),
                        ItemList.Circuit_Chip_NOR.get(32),
                        GGMaterial.signalium.get(OrePrefixes.bolt, 32),
                        GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8),
                        GTOreDictUnificator.get(OrePrefixes.rotor, Materials.TinAlloy, 1))
                    .fluidInputs(tMat.getMolten(2 * tMultiplier * INGOTS))
                    .itemOutputs(ItemRefer.HiC_T1.get(1))
                    .duration(1 * MINUTES)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(assemblerRecipes);
            }
        }

        // Neutron Accelerator ULV
        GTModHandler.addCraftingRecipe(
            Loaders.NeutronAccelerators[0].copy(),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "WPM", "CHI", "WPM", 'W', GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 1),
                'P', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Lead, 1), 'M',
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Lead, 1), 'C',
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 1), 'H', ItemList.Hull_ULV, 'I',
                ItemRefer.Inverter.get(1), });

        // Neutron Accelerator LV
        GTModHandler.addCraftingRecipe(
            Loaders.NeutronAccelerators[1].copy(),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "WPM", "CHI", "WPM", 'W',
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.RedAlloy, 1), 'P',
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 1), 'M', ItemList.Electric_Motor_LV,
                'C', "plateAnyRubber", 'H', ItemList.Hull_LV, 'I', ItemRefer.Inverter.get(1), });

        // Neutron Accelerator MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Inverter.get(1),
                ItemList.Hull_MV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polyethylene, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Beryllium, 2),
                ItemList.Electric_Motor_MV.get(2))
            .itemOutputs(Loaders.NeutronAccelerators[2].copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Neutron Accelerator HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Inverter.get(1),
                ItemList.Hull_HV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.PolyvinylChloride, 1),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Beryllium, 2),
                ItemList.Electric_Motor_HV.get(2))
            .itemOutputs(Loaders.NeutronAccelerators[3].copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Neutron Accelerator EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Inverter.get(1),
                ItemList.Hull_EV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.StyreneButadieneRubber, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.IronMagnetic, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenCarbide, 2),
                ItemList.Electric_Motor_EV.get(2))
            .itemOutputs(Loaders.NeutronAccelerators[4].copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Neutron Accelerator IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Inverter.get(1),
                ItemList.Hull_IV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.RubberSilicone, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.SteelMagnetic, 4),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.TungstenCarbide, 2),
                ItemList.Electric_Motor_IV.get(2))
            .itemOutputs(Loaders.NeutronAccelerators[5].copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Neutron Accelerator LuV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[5].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
            .itemInputs(
                ItemRefer.Inverter.get(2),
                ItemList.Hull_LuV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorLuV, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.NetherStar, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.NeodymiumMagnetic, 8),
                ItemList.Electric_Motor_LuV.get(2))
            .fluidInputs(Materials.Argon.getGas(3_000))
            .itemOutputs(Loaders.NeutronAccelerators[6].copy())
            .eut(TierEU.RECIPE_LuV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator ZPM
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[6].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemRefer.Inverter.get(2),
                ItemList.Hull_ZPM.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.NetherStar, 1),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Polybenzimidazole, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.SamariumMagnetic, 8),
                ItemList.Electric_Motor_ZPM.get(2))
            .fluidInputs(WerkstoffLoader.Xenon.getFluidOrGas(3_000))
            .itemOutputs(Loaders.NeutronAccelerators[7].copy())
            .eut(TierEU.RECIPE_ZPM)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator UV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[7].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_ZPM))
            .itemInputs(
                ItemRefer.Inverter.get(4),
                ItemList.Hull_UV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.NetherStar, 2),
                GTOreDictUnificator.get(OrePrefixes.plateTriple, Materials.Polybenzimidazole, 4),
                ItemList.ZPM_Coil.get(8),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.NickelZincFerrite, 16),
                ItemList.Electric_Motor_UV.get(2))
            .fluidInputs(WerkstoffLoader.Oganesson.getFluidOrGas(3_000))
            .itemOutputs(Loaders.NeutronAccelerators[8].copy())
            .eut(TierEU.RECIPE_UV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator UHV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[8].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_UV))
            .itemInputs(
                ItemRefer.Inverter.get(8),
                ItemList.Hull_MAX.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 4),
                GGMaterial.orundum.get(OrePrefixes.plate, 2),
                GTOreDictUnificator.get(OrePrefixes.plateTriple, Materials.Kevlar, 8),
                ItemList.UV_Coil.get(8),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 16),
                ItemList.Electric_Motor_UHV.get(2))
            .fluidInputs(
                WerkstoffLoader.Oganesson.getFluidOrGas(12_000),
                Materials.Grade5PurifiedWater.getFluid(16_000))
            .itemOutputs(Loaders.NeutronAccelerators[9].copy())
            .eut(TierEU.RECIPE_UHV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator UEV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[9].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_UHV))
            .itemInputs(
                ItemRefer.Inverter.get(16),
                ItemList.Hull_UEV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 4),
                GGMaterial.orundum.get(OrePrefixes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Kevlar, 16),
                ItemList.UHV_Coil.get(8),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TengamAttuned, 16),
                ItemList.Electric_Motor_UEV.get(2))
            .fluidInputs(
                GGMaterial.metastableOganesson.getMolten(3 * INGOTS),
                Materials.Grade6PurifiedWater.getFluid(16_000))
            .itemOutputs(Loaders.NeutronAccelerators[10].copy())
            .eut(TierEU.RECIPE_UEV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator UIV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[10].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_UEV))
            .itemInputs(
                ItemRefer.Inverter.get(32),
                ItemList.Hull_UIV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 4),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.RadoxPolymer, 16),
                ItemList.Tesseract.get(8),
                ItemList.Electromagnet_Tengam.get(1),
                ItemList.Electric_Motor_UIV.get(2),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 16))
            .fluidInputs(
                GGMaterial.metastableOganesson.getMolten(12 * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(4_000),
                Materials.Grade7PurifiedWater.getFluid(16_000))
            .itemOutputs(Loaders.NeutronAccelerators[11].copy())
            .eut(TierEU.RECIPE_UIV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        // Neutron Accelerator UMV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[11].copy())
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_UIV))
            .itemInputs(
                ItemRefer.Inverter.get(64),
                ItemList.Hull_UMV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUMV, 4),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlate(32),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.RadoxPolymer, 1),
                ItemList.EnergisedTesseract.get(12),
                ItemList.Electromagnet_Tengam.get(1),
                ItemList.Electric_Motor_UMV.get(2),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Creon, 16),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Mellion, 16))
            .fluidInputs(
                GGMaterial.shirabon.getMolten(3 * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(8_000),
                Materials.Grade8PurifiedWater.getFluid(16_000))
            .itemOutputs(Loaders.NeutronAccelerators[12].copy())
            .eut(TierEU.RECIPE_UMV)
            .duration(15 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_IV.get(1L),
                ItemList.Cover_ActivityDetector.get(1L),
                ItemList.Cover_Screen.get(1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.VibrantAlloy, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1),
                ItemList.Sensor_HV.get(2))
            .circuit(1)
            .fluidInputs(Materials.Helium.getGas(1_000))
            .itemOutputs(Loaders.NS.copy())
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Neutron_Source.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { " P ", "PUP", " P ", 'P',
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Steel, 1), 'U',
                ItemRefer.High_Density_Uranium.get(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.HiC_T2.get(2), ItemList.Emitter_EV.get(2), ItemRefer.Neutron_Source.get(1))
            .itemOutputs(Loaders.NA.copy())
            .fluidInputs(
                Materials.StainlessSteel.getMolten(4 * INGOTS),
                Materials.TungstenCarbide.getMolten(1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);
    }

    public static void InitLoadRecipe() {

        // Al2O3 + 2N + 3C = 2AlN + 3CO
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sapphire, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3))
            .fluidInputs(Materials.LiquidNitrogen.getGas(2_000))
            .fluidOutputs(Materials.CarbonMonoxide.getGas(3_000))
            .itemOutputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4600)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.GreenSapphire, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3))
            .fluidInputs(Materials.LiquidNitrogen.getGas(2_000))
            .fluidOutputs(Materials.CarbonMonoxide.getGas(3_000))
            .itemOutputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4600)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3))
            .fluidInputs(Materials.LiquidNitrogen.getGas(2_000))
            .fluidOutputs(Materials.CarbonMonoxide.getGas(3_000))
            .itemOutputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4600)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingotHot, 1))
            .itemOutputs(GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingot, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.naquadahBasedFuelMkVIDepleted.getFluidOrGas(1_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1))
            .outputChances(1000, 500, 400, 50, 20, 5)
            .fluidOutputs(GGMaterial.metastableOganesson.getMolten(122))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1))
            .outputChances(1000, 500, 400, 50, 20, 5)
            .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(182))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.naquadahBasedFuelMkIVDepleted.getFluidOrGas(1_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 32))
            .outputChances(9900, 9500, 9000, 8000, 5000, 3000)
            .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(6 * INGOTS))
            .duration(2 * MINUTES + 5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.naquadahBasedFuelMkIIIDepleted.getFluidOrGas(1_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 32))
            .outputChances(9500, 9000, 8000, 7000, 5000, 4000)
            .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(720))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.naquadahBasedFuelMkIIDepleted.getFluidOrGas(1_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Americium, 32),
                WerkstoffLoader.Californium.get(OrePrefixes.dust, 32))
            .outputChances(9000, 8500, 5000, 4000, 2000)
            .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(1 * INGOTS))
            .duration(6 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 16L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Barium, 6L))
            .outputChances(6000, 1000, 5000)
            .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(10))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.thoriumBasedLiquidFuelDepleted.getFluidOrGas(1_000))
            .itemOutputs(
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Praseodymium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Praseodymium, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 4))
            .outputChances(10000, 8000, 10000, 8000, 3000, 5000)
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tritanium, 9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 2))
            .outputChances(5000, 8000, 7500)
            .fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(1 * INGOTS))
            .duration(2 * MINUTES + 5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(MaterialsFluorides.THORIUM_TETRAFLUORIDE.getFluidStack(4_000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 1))
            .fluidOutputs(GGMaterial.thorium232Tetrafluoride.getFluidOrGas(3_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.naquadahEmulsion.getFluidOrGas(1_000))
            .itemOutputs(
                GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 4),
                GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 2),
                GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 1),
                GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 1),
                GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 1),
                GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 1))
            .outputChances(8000, 7500, 5000, 2000, 500, 100)
            .fluidOutputs(GGMaterial.naquadahSolution.getFluidOrGas(500))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(GGMaterial.naquadahBasedFuelMkIDepleted.getFluidOrGas(1_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 48),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Europium, 32))
            .outputChances(9000, 8500, 5000, 4000, 2000)
            .fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(1 * INGOTS))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 4))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1),
                WerkstoffLoader.Tiberium.get(OrePrefixes.dust, 1))
            .outputChances(10000, 9500, 8000, 2500, 2000, 2000)
            .fluidOutputs(Materials.Radon.getGas(20))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);
    }

    public static void Fixer() {
        MaterialFix.MaterialFluidExtractionFix(GGMaterial.atomicSeparationCatalyst);
        MaterialFix.MaterialFluidExtractionFix(GGMaterial.extremelyUnstableNaquadah);
        MaterialFix.MaterialFluidExtractionFix(GGMaterial.metastableOganesson);
        MaterialFix.MaterialFluidExtractionFix(GGMaterial.shirabon);
    }
}
