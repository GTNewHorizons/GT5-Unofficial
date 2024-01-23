package goodgenerator.loader;

import static gregtech.api.enums.Mods.GTPlusPlus;
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
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gregtech.api.util.GT_RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GT_RecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;
import static gregtech.loaders.postload.GT_MachineRecipeLoader.solderingMats;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.items.MyMaterial;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import goodgenerator.util.MaterialFix;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_Utility;

public class RecipeLoader {

    public static void RecipeLoad() {

        // Radiation Protection Plate
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Iridium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 8L),
                        GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Lead.getMolten(1152)).itemOutputs(ItemRefer.Radiation_Protection_Plate.get(1))
                .duration(20 * SECONDS).eut(TierEU.RECIPE_EV).addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lanthanum, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 8L),
                        GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Lead.getMolten(1152)).itemOutputs(ItemRefer.Radiation_Protection_Plate.get(1))
                .duration(20 * SECONDS).eut(TierEU.RECIPE_EV).addTo(assemblerRecipes);

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        // LNR Controller
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, ItemList.Generator_Naquadah_Mark_III.get(1).copy())
                .metadata(RESEARCH_TIME, 1 * HOURS + 24 * MINUTES)
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Adamantium, 8),
                        ItemRefer.Radiation_Protection_Plate.get(16),
                        ItemList.Field_Generator_ZPM.get(2),
                        ItemList.Electric_Pump_ZPM.get(8),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorLuV, 8),
                        GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Naquadah, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmium, 16))
                .fluidInputs(
                        Materials.Trinium.getMolten(576),
                        new FluidStack(solderIndalloy, 4608),
                        Materials.Lubricant.getFluid(8000))
                .itemOutputs(ItemRefer.Large_Naquadah_Reactor.get(1)).eut(TierEU.RECIPE_ZPM)
                .duration(13 * MINUTES + 30 * SECONDS).addTo(AssemblyLine);

        // LNR Casing
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.Radiation_Protection_Plate.get(6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 1),
                        ItemList.Field_Generator_MV.get(1),
                        GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemRefer.Field_Restriction_Casing.get(1)).duration(20 * SECONDS).eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);

        // LNR Frame
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 8),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSE, 4),
                        GT_Utility.getIntegratedCircuit(24))
                .itemOutputs(ItemRefer.Radiation_Proof_Steel_Frame_Box.get(1)).duration(16 * SECONDS)
                .eut(TierEU.RECIPE_EV).addTo(assemblerRecipes);

        // Uranium Liquid Fuel Process Line
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        MyMaterial.graphiteUraniumMixture.get(OrePrefixes.dust, 4),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.TungstenCarbide, 2),
                        GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemRefer.Wrapped_Uranium_Ingot.get(1)).duration(1 * MINUTES + 10 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemRefer.Wrapped_Uranium_Ingot.get(4))
                .itemOutputs(
                        ItemRefer.High_Density_Uranium_Nugget.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenCarbide, 8))
                .duration(1 * SECONDS).eut(TierEU.RECIPE_LV).metadata(ADDITIVE_AMOUNT, 8).addTo(implosionRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemRefer.High_Density_Uranium_Nugget.get(9))
                .itemOutputs(ItemRefer.High_Density_Uranium.get(1)).duration(30 * SECONDS).eut(TierEU.RECIPE_HV)
                .addTo(compressorRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.High_Density_Uranium.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quantium, 4),
                        GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Radon.getGas(1000L))
                .fluidOutputs(MyMaterial.uraniumBasedLiquidFuel.getFluidOrGas(1000)).duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LuV / 2).addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
                .fluidInputs(MyMaterial.uraniumBasedLiquidFuel.getFluidOrGas(10), Materials.Hydrogen.getGas(100L))
                .fluidOutputs(MyMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(10)).duration(2 * SECONDS)
                .eut(TierEU.RECIPE_IV).metadata(FUSION_THRESHOLD, 200000000).addTo(fusionRecipes);

        // Thorium Liquid Process Line
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        MyMaterial.uraniumCarbideThoriumMixture.get(OrePrefixes.dust, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.TungstenSteel, 4),
                        GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemRefer.Wrapped_Thorium_Ingot.get(1)).duration(15 * SECONDS).eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemRefer.Wrapped_Thorium_Ingot.get(1))
                .itemOutputs(
                        ItemRefer.High_Density_Thorium_Nugget.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenSteel, 8))
                .duration(1 * SECONDS).eut(TierEU.RECIPE_LV).metadata(ADDITIVE_AMOUNT, 4).addTo(implosionRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemRefer.High_Density_Thorium_Nugget.get(9))
                .itemOutputs(ItemRefer.High_Density_Thorium.get(1)).duration(10 * SECONDS).eut(TierEU.RECIPE_MV)
                .addTo(compressorRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.High_Density_Thorium.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Draconium, 2),
                        GT_Utility.getIntegratedCircuit(2))
                .fluidInputs(Materials.Mercury.getFluid(1000L))
                .fluidOutputs(MyMaterial.thoriumBasedLiquidFuel.getFluidOrGas(4000))
                .duration(2 * MINUTES + 30 * SECONDS).eut(TierEU.RECIPE_HV / 2).addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        MyMaterial.thoriumBasedLiquidFuel.get(OrePrefixes.cell, 1),
                        GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Helium.getPlasma(250L))
                .itemOutputs(MyMaterial.thoriumBasedLiquidFuelExcited.get(OrePrefixes.cell, 1)).duration(6 * SECONDS)
                .eut(TierEU.RECIPE_IV / 2).addTo(mixerRecipes);

        // Liquid Plutonium Process Line
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        MyMaterial.plutoniumOxideUraniumMixture.get(OrePrefixes.dust, 8),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.HSSS, 4),
                        GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemRefer.Wrapped_Plutonium_Ingot.get(1)).duration(1 * MINUTES + 30 * SECONDS)
                .eut(TierEU.RECIPE_EV).addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemRefer.Wrapped_Plutonium_Ingot.get(2))
                .itemOutputs(
                        ItemRefer.High_Density_Plutonium_Nugget.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.HSSS, 8))
                .duration(1 * SECONDS).eut(TierEU.RECIPE_LV).metadata(ADDITIVE_AMOUNT, 16).addTo(implosionRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemRefer.High_Density_Plutonium_Nugget.get(9))
                .itemOutputs(ItemRefer.High_Density_Plutonium.get(1)).duration(1 * MINUTES).eut(TierEU.RECIPE_MV)
                .addTo(compressorRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.High_Density_Plutonium.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 2),
                        GT_Utility.getIntegratedCircuit(1))
                .fluidOutputs(MyMaterial.plutoniumBasedLiquidFuel.getFluidOrGas(1000)).duration(18 * SECONDS)
                .eut(TierEU.RECIPE_LuV).addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
                .fluidInputs(Materials.Lutetium.getMolten(16), MyMaterial.plutoniumBasedLiquidFuel.getFluidOrGas(20))
                .fluidOutputs(MyMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(20)).duration(1 * SECONDS)
                .eut(TierEU.RECIPE_LuV / 2).metadata(FUSION_THRESHOLD, 220000000).addTo(fusionRecipes);

        // Th-232
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 24),
                        MyMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 0),
                        GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(FluidRegistry.getFluidStack("nitricacid", 6000))
                .fluidOutputs(MyMaterial.oxalate.getFluidOrGas(3000), Materials.NitricOxide.getGas(6000))
                .duration(30 * SECONDS).eut(TierEU.RECIPE_MV).addTo(multiblockChemicalReactorRecipes);

        // Th + 2O = ThO2
        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 1))
                .fluidInputs(Materials.Oxygen.getGas(2000))
                .itemOutputs(WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 3)).duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV).metadata(COIL_HEAT, 1200).addTo(blastFurnaceRecipes);

        // Th + 8HNO3 =HF= Th(NO3)4 + 4NO2 + 4H2O
        GT_Values.RA.stdBuilder().itemInputs(Materials.Thorium.getDust(1))
                .fluidInputs(Materials.HydrofluoricAcid.getFluid(100), FluidRegistry.getFluidStack("nitricacid", 8000))
                .fluidOutputs(MyMaterial.thoriumNitrate.getFluidOrGas(1000), Materials.NitrogenDioxide.getGas(4000))
                .duration(2 * SECONDS).eut(TierEU.RECIPE_MV).addTo(multiblockChemicalReactorRecipes);

        // 4NaOH + Th(NO3)4 = Th(OH)4 + 4NaNO3
        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1), Materials.SodiumHydroxide.getDust(12))
                .fluidInputs(MyMaterial.thoriumNitrate.getFluidOrGas(1000))
                .itemOutputs(
                        MyMaterial.thoriumHydroxide.get(OrePrefixes.dust, 9),
                        WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 20))
                .duration(10 * SECONDS).eut(TierEU.RECIPE_MV).addTo(UniversalChemical);

        // 2 NaNO3 + H2SO4 = Na2SO4 + 2HNO3
        GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(1), WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 10))
                .fluidInputs(Materials.SulfuricAcid.getFluid(1000)).fluidOutputs(Materials.NitricAcid.getFluid(2000))
                .itemOutputs(WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 7)).duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(UniversalChemical);

        // Th(OH)4 + 4HF = ThF4 + 4H2O
        GT_Values.RA.stdBuilder()
                .itemInputs(MyMaterial.thoriumHydroxide.get(OrePrefixes.dust, 9), GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.HydrofluoricAcid.getFluid(4000))
                .fluidOutputs(MyMaterial.thoriumTetrafluoride.getFluidOrGas(1000)).duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        // Zn + 2Cl = ZnCl2
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                        GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Chlorine.getGas(2000))
                .itemOutputs(MyMaterial.zincChloride.get(OrePrefixes.dust, 3)).duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        // ZnCl2 + 3Ca + ThF4 = ZnTh + CaCl2 + 2CaF2
        GT_Values.RA.stdBuilder()
                .itemInputs(MyMaterial.zincChloride.get(OrePrefixes.dust, 3), Materials.Calcium.getDust(3))
                .fluidInputs(MyMaterial.thorium232Tetrafluoride.getFluidOrGas(1000))
                .fluidOutputs(WerkstoffLoader.CalciumChloride.getFluidOrGas(3000))
                .itemOutputs(
                        MyMaterial.zincThoriumAlloy.get(OrePrefixes.ingot, 1),
                        WerkstoffLoader.Fluorspar.get(OrePrefixes.dust, 6))
                .duration(15 * SECONDS).eut(TierEU.RECIPE_MV).metadata(COIL_HEAT, 3000).addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(MyMaterial.zincThoriumAlloy.get(OrePrefixes.ingot, 1), GT_Utility.getIntegratedCircuit(11))
                .fluidInputs(Materials.Argon.getGas(250)).fluidOutputs(Materials.Zinc.getMolten(144))
                .itemOutputs(WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1)).duration(7 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV).metadata(COIL_HEAT, 1900).addTo(blastFurnaceRecipes);

        // 2V + 5O = V2O5
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 2),
                        GT_Utility.getIntegratedCircuit(24))
                .fluidInputs(Materials.Oxygen.getGas(5000))
                .itemOutputs(MyMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 7)).duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV).metadata(COIL_HEAT, 2500).addTo(blastFurnaceRecipes);

        // Atomic Separation Catalyst
        ItemStack[] mat1 = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 32),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Firestone, 4) };
        ItemStack[] mat2 = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Draconium, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quantium, 4), };
        ItemStack[] mat3 = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ardite, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Manyullyn, 4) };

        for (ItemStack m1 : mat1) {
            for (ItemStack m2 : mat2) {
                for (ItemStack m3 : mat3) {
                    GT_Values.RA.stdBuilder().itemInputs(m1, m2, m3, GT_Utility.getIntegratedCircuit(4))
                            .fluidInputs(Materials.Naquadah.getMolten(288))
                            .itemOutputs(ItemRefer.Raw_Atomic_Separation_Catalyst.get(63)).duration(15 * SECONDS)
                            .eut(TierEU.RECIPE_HV).addTo(mixerRecipes);
                }
            }
        }

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        WerkstoffLoader.Tiberium.get(OrePrefixes.plate, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 8))
                .itemOutputs(MyMaterial.orundum.get(OrePrefixes.plate, 1)).duration(20 * SECONDS)
                .eut(TierEU.RECIPE_IV / 2).addTo(formingPressRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        MyMaterial.orundum.get(OrePrefixes.plate, 2),
                        ItemRefer.Raw_Atomic_Separation_Catalyst.get(4))
                .fluidInputs(Materials.Plutonium.getMolten(144))
                .itemOutputs(MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot, 1)).duration(3 * MINUTES)
                .eut(TierEU.RECIPE_HV).metadata(COIL_HEAT, 5000).addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder().itemInputs(MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot, 1))
                .itemOutputs(MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingot, 1)).duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LuV).addTo(vacuumFreezerRecipes);

        CrackRecipeAdder.crackerAdder(
                MyMaterial.naquadahGas.getFluidOrGas(1000),
                MyMaterial.atomicSeparationCatalyst.getMolten(4),
                new FluidStack[] { Materials.Helium.getGas(300), WerkstoffLoader.Neon.getFluidOrGas(50),
                        Materials.Argon.getGas(80), WerkstoffLoader.Krypton.getFluidOrGas(20),
                        WerkstoffLoader.Xenon.getFluidOrGas(40), Materials.Radon.getGas(14000) },
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadah, 1),
                6,
                4020,
                300);

        CrackRecipeAdder.crackerAdder(
                MyMaterial.lightNaquadahFuel.getFluidOrGas(1000),
                MyMaterial.atomicSeparationCatalyst.getMolten(4),
                new FluidStack[] { Materials.Radon.getGas(1400), MyMaterial.naquadahGas.getFluidOrGas(400),
                        Materials.Uranium.getMolten(648), MyMaterial.heavyNaquadahFuel.getFluidOrGas(280),
                        Materials.Plutonium.getMolten(576), MyMaterial.naquadahAsphalt.getFluidOrGas(140) },
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium235, 1),
                6,
                4020,
                450);

        CrackRecipeAdder.crackerAdder(
                MyMaterial.heavyNaquadahFuel.getFluidOrGas(1000),
                MyMaterial.atomicSeparationCatalyst.getMolten(6),
                new FluidStack[] { Materials.Radon.getGas(1000), MyMaterial.naquadahGas.getFluidOrGas(450),
                        MyMaterial.lightNaquadahFuel.getFluidOrGas(560), Materials.Uranium.getMolten(720),
                        Materials.Lutetium.getMolten(648), MyMaterial.naquadahAsphalt.getFluidOrGas(240) },
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1),
                6,
                4020,
                500);

        CrackRecipeAdder.crackerAdder(
                MyMaterial.naquadahAsphalt.getFluidOrGas(1000),
                MyMaterial.atomicSeparationCatalyst.getMolten(12),
                new FluidStack[] { MyMaterial.lightNaquadahFuel.getFluidOrGas(600), Materials.Uranium.getMolten(1152),
                        Materials.Thorium.getMolten(864), Materials.Plutonium.getMolten(792),
                        Materials.Thulium.getMolten(216), MyMaterial.heavyNaquadahFuel.getFluidOrGas(350) },
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadria, 1),
                6,
                4020,
                800);

        // 2C2H6O =H2SO4= C4H10O + H2O
        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(2))
                .fluidInputs(Materials.Ethanol.getFluid(1000), Materials.SulfuricAcid.getFluid(1000))
                .fluidOutputs(MyMaterial.ether.getFluidOrGas(500), Materials.DilutedSulfuricAcid.getFluid(1500))
                .duration(25 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_MV).addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder().itemInputs(Materials.GasolineRaw.getCells(9), Materials.Ethanol.getCells(1))
                .itemOutputs(MyMaterial.ethanolGasoline.get(OrePrefixes.cell, 10)).duration(15 * TICKS)
                .eut(TierEU.RECIPE_MV).addTo(UniversalChemical);

        GT_Values.RA.stdBuilder().itemInputs(MyMaterial.ether.get(OrePrefixes.cell)).metadata(FUEL_VALUE, 537)
                .metadata(FUEL_TYPE, 0).addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder().itemInputs(MyMaterial.ether.get(OrePrefixes.cell)).metadata(FUEL_VALUE, 537)
                .metadata(FUEL_TYPE, 1).addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder().itemInputs(MyMaterial.ethanolGasoline.get(OrePrefixes.cell))
                .metadata(FUEL_VALUE, 1100).metadata(FUEL_TYPE, 0).addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder().itemInputs(MyMaterial.cyclopentadiene.get(OrePrefixes.cell)).metadata(FUEL_VALUE, 70)
                .metadata(FUEL_TYPE, 1).addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder().itemInputs(MyMaterial.ironedFuel.get(OrePrefixes.cell)).metadata(FUEL_VALUE, 2248)
                .metadata(FUEL_TYPE, 0).addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder().itemInputs(MyMaterial.ironedKerosene.get(OrePrefixes.cell)).metadata(FUEL_VALUE, 1824)
                .metadata(FUEL_TYPE, 0).addTo(GT_RecipeConstants.Fuel);

        // Sb + 3Cl = SbCl3
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Antimony, 1))
                .fluidInputs(MyMaterial.ether.getFluidOrGas(1000), Materials.Chlorine.getGas(3000))
                .fluidOutputs(MyMaterial.antimonyTrichloride.getFluidOrGas(1000)).duration(3 * SECONDS)
                .eut(TierEU.RECIPE_LV).addTo(multiblockChemicalReactorRecipes);

        // SbCl3 + 2Cl = SbCl5
        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.antimonyTrichloride.getFluidOrGas(1000), Materials.Chlorine.getGas(2000))
                .fluidOutputs(MyMaterial.antimonyPentachlorideSolution.getFluidOrGas(1000)).duration(9 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(multiblockChemicalReactorRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
                MyMaterial.antimonyPentachlorideSolution.getFluidOrGas(1000),
                new FluidStack[] { MyMaterial.ether.getFluidOrGas(1000),
                        MyMaterial.antimonyPentachloride.getFluidOrGas(1000) },
                GT_Values.NI,
                30 * SECONDS,
                TierEU.RECIPE_MV);

        // SbCl5 + 5HF = SbF5 + 5HCl
        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(
                        MyMaterial.antimonyPentachloride.getFluidOrGas(1000),
                        Materials.HydrofluoricAcid.getFluid(5000))
                .fluidOutputs(
                        MyMaterial.antimonyPentafluoride.getFluidOrGas(1000),
                        Materials.HydrochloricAcid.getFluid(5000))
                .duration(21 * SECONDS).eut(TierEU.RECIPE_LV).addTo(multiblockChemicalReactorRecipes);

        // SbH5 + HF = HSbF6
        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(
                        MyMaterial.antimonyPentafluoride.getFluidOrGas(1000),
                        Materials.HydrofluoricAcid.getFluid(1000))
                .fluidOutputs(MyMaterial.fluoroantimonicAcid.getFluidOrGas(1000)).duration(42 * SECONDS)
                .eut(TierEU.RECIPE_EV).addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 16),
                        GT_Utility.getIntegratedCircuit(16))
                .fluidInputs(Materials.HydrofluoricAcid.getFluid(3000))
                .fluidOutputs(MyMaterial.acidNaquadahEmulsion.getFluidOrGas(2000))
                .itemOutputs(MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 3)).duration(3 * MINUTES)
                .eut(TierEU.RECIPE_EV).metadata(COIL_HEAT, 3400).addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_Utility.getIntegratedCircuit(3),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, 8))
                .fluidInputs(MyMaterial.acidNaquadahEmulsion.getFluidOrGas(1000))
                .fluidOutputs(MyMaterial.naquadahEmulsion.getFluidOrGas(1000))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.AntimonyTrioxide, 1),
                        WerkstoffLoader.Fluorspar.get(OrePrefixes.dust, 4))
                .duration(12 * SECONDS).eut(TierEU.RECIPE_LV).addTo(multiblockChemicalReactorRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
                MyMaterial.naquadahSolution.getFluidOrGas(20),
                new FluidStack[] { MyMaterial.naquadahAsphalt.getFluidOrGas(2),
                        MyMaterial.heavyNaquadahFuel.getFluidOrGas(5), MyMaterial.lightNaquadahFuel.getFluidOrGas(10),
                        FluidRegistry.getFluidStack("water", 10), MyMaterial.naquadahGas.getFluidOrGas(60) },
                GT_Values.NI,
                1 * SECONDS,
                TierEU.RECIPE_EV);

        GT_Values.RA.stdBuilder().itemInputs(MyMaterial.naquadahGas.get(OrePrefixes.cell)).metadata(FUEL_VALUE, 1024)
                .metadata(FUEL_TYPE, 1).addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder()
                .fluidInputs(
                        MyMaterial.lightNaquadahFuel.getFluidOrGas(780),
                        MyMaterial.heavyNaquadahFuel.getFluidOrGas(360))
                .fluidOutputs(MyMaterial.naquadahBasedFuelMkI.getFluidOrGas(100)).duration(25 * SECONDS)
                .eut(TierEU.RECIPE_LuV).metadata(FUSION_THRESHOLD, 320000000).addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 4),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.ElectrumFlux, 32))
                .fluidInputs(
                        MyMaterial.naquadahBasedFuelMkI.getFluidOrGas(100),
                        MyMaterial.naquadahGas.getFluidOrGas(1500))
                .fluidOutputs(MyMaterial.naquadahBasedFuelMkII.getFluidOrGas(100)).duration(25 * SECONDS)
                .eut(TierEU.RECIPE_UHV / 2).addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 32),
                        GT_Utility.getIntegratedCircuit(16))
                .fluidInputs(MyMaterial.fluoroantimonicAcid.getFluidOrGas(4000))
                .fluidOutputs(MyMaterial.acidNaquadahEmulsion.getFluidOrGas(8000))
                .itemOutputs(MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 17)).duration(3 * MINUTES)
                .eut(TierEU.RECIPE_IV / 2).metadata(COIL_HEAT, 3400).addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, ItemList.Generator_Naquadah_Mark_V.get(1).copy())
                .metadata(RESEARCH_TIME, 7 * HOURS)
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 8),
                        ItemRefer.Advanced_Radiation_Protection_Plate.get(64),
                        ItemList.Field_Generator_UV.get(8),
                        ItemList.Electric_Pump_UHV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.MysteriousCrystal, 8),
                        ItemList.Circuit_Wafer_NPIC.get(16),
                        ItemList.UHV_Coil.get(64),
                        new Object[] { "craftingLensYellow", 16 },
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Thulium, 64))
                .fluidInputs(
                        Materials.Quantium.getMolten(9216L),
                        Materials.DraconiumAwakened.getMolten(4608L),
                        MyMaterial.extremelyUnstableNaquadah.getMolten(1440),
                        new FluidStack(solderIndalloy, 14400))
                .itemOutputs(ItemRefer.Naquadah_Fuel_Refinery.get(1)).eut(TierEU.RECIPE_UHV).duration(30 * MINUTES)
                .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, ItemRefer.Radiation_Protection_Plate.get(1))
                .metadata(RESEARCH_TIME, 1 * HOURS + 7 * MINUTES)
                .itemInputs(
                        ItemRefer.Radiation_Protection_Plate.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ElectrumFlux, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Trinium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VibrantAlloy, 4),
                        ItemRefer.Radiation_Protection_Plate.get(1))
                .fluidInputs(new FluidStack(solderIndalloy, 1152))
                .itemOutputs(ItemRefer.Advanced_Radiation_Protection_Plate.get(1)).eut(TierEU.RECIPE_ZPM / 2)
                .duration(50 * SECONDS).addTo(AssemblyLine);

        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Casing.get(1))
                .metadata(RESEARCH_TIME, 3 * HOURS + 30 * MINUTES)
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Thulium, 1),
                        ItemRefer.Advanced_Radiation_Protection_Plate.get(6),
                        ItemList.Field_Generator_IV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 16),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NaquadahAlloy, 32),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NaquadahAlloy, 32),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Manyullyn, 32),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Manyullyn, 32),
                        MyMaterial.orundum.get(OrePrefixes.plate, 4))
                .fluidInputs(Materials.TungstenSteel.getMolten(1152), new FluidStack(solderIndalloy, 2304))
                .itemOutputs(ItemRefer.Naquadah_Fuel_Refinery_Casing.get(1)).eut(TierEU.RECIPE_ZPM / 2)
                .duration(25 * SECONDS).addTo(AssemblyLine);

        if (NewHorizonsCoreMod.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                    .metadata(RESEARCH_ITEM, MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingot))
                    .metadata(RESEARCH_TIME, 4 * HOURS + 10 * MINUTES)
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmium, 1),
                            ItemList.Field_Generator_UV.get(2),
                            ItemList.Electric_Pump_UV.get(8),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Americium, 8),
                            GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.BlackPlutonium, 16),
                            ItemList.Circuit_Wafer_PPIC.get(32),
                            new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L })
                    .fluidInputs(
                            WerkstoffLoader.Krypton.getFluidOrGas(1000),
                            Materials.ElectrumFlux.getMolten(9216),
                            Materials.Lubricant.getFluid(128000))
                    .itemOutputs(ItemRefer.Field_Restriction_Coil_T1.get(1)).eut(TierEU.RECIPE_ZPM)
                    .duration(15 * MINUTES).addTo(AssemblyLine);

            GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Coil_T1.get(1))
                    .metadata(RESEARCH_TIME, 4 * HOURS + 50 * MINUTES)
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                            ItemList.Field_Generator_UHV.get(2),
                            ItemList.Electric_Pump_UHV.get(8),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 8),
                            GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 16),
                            ItemList.Circuit_Wafer_PPIC.get(48),
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L })
                    .fluidInputs(
                            Materials.Radon.getPlasma(1000),
                            Materials.DraconiumAwakened.getMolten(9216),
                            Materials.Lubricant.getFluid(128000))
                    .itemOutputs(ItemRefer.Field_Restriction_Coil_T2.get(1)).eut(TierEU.RECIPE_ZPM)
                    .duration(30 * MINUTES).addTo(AssemblyLine);

            GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Coil_T2.get(1))
                    .metadata(RESEARCH_TIME, 5 * HOURS + 30 * MINUTES)
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                            ItemList.Field_Generator_UEV.get(2),
                            ItemList.Electric_Pump_UEV.get(8),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 8),
                            GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Infinity, 16),
                            ItemList.Circuit_Wafer_PPIC.get(64),
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L })
                    .fluidInputs(
                            WerkstoffLoader.Oganesson.getFluidOrGas(1000),
                            Materials.Neutronium.getMolten(9216),
                            Materials.Lubricant.getFluid(128000))
                    .itemOutputs(ItemRefer.Field_Restriction_Coil_T3.get(1)).eut(TierEU.RECIPE_ZPM).duration(1 * HOURS)
                    .addTo(AssemblyLine);

            GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, ItemRefer.Field_Restriction_Coil_T3.get(1))
                    .metadata(RESEARCH_TIME, 7 * HOURS)
                    .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1),
                            ItemList.Field_Generator_UIV.get(2),
                            ItemList.Electric_Pump_UIV.get(8),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 8),
                            GT_OreDictUnificator.get(OrePrefixes.pipeMedium, MaterialsUEVplus.SpaceTime, 16),
                            ItemList.Circuit_Wafer_PPIC.get(64),
                            new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1L })
                    .fluidInputs(
                            MyMaterial.metastableOganesson.getMolten(1000),
                            MaterialsUEVplus.TranscendentMetal.getMolten(9216),
                            Materials.Lubricant.getFluid(128000))
                    .itemOutputs(ItemRefer.Field_Restriction_Coil_T4.get(1)).eut(TierEU.RECIPE_ZPM).duration(2 * HOURS)
                    .addTo(AssemblyLine);
        }

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BorosilicateGlass, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ReinforceGlass, 6),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 32),
                        ItemList.Field_Generator_HV.get(4),
                        GT_Utility.getIntegratedCircuit(6))
                .fluidInputs(Materials.Naquadria.getMolten(288)).itemOutputs(ItemRefer.Field_Restriction_Glass.get(1))
                .duration(15 * SECONDS).eut(TierEU.RECIPE_ZPM).addTo(assemblerRecipes);

        // Ca + O = CaO
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_Utility.getIntegratedCircuit(1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1))
                .fluidInputs(Materials.Oxygen.getGas(1000))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, 2)).duration(10 * TICKS)
                .eut(TierEU.RECIPE_LV).addTo(UniversalChemical);

        // AlN = Al + N
        GT_Values.RA.stdBuilder().itemInputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1))
                .fluidOutputs(Materials.Nitrogen.getGas(1000L)).duration(5 * SECONDS).eut(TierEU.RECIPE_MV)
                .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.Aluminum_Nitride_Dust.get(4),
                        WerkstoffLoader.YttriumOxide.get(OrePrefixes.dust, 5),
                        GT_Utility.getIntegratedCircuit(9))
                .fluidInputs(FluidRegistry.getFluidStack("advancedglue", 1000))
                .itemOutputs(ItemRefer.Special_Ceramics_Dust.get(9)).duration(5 * SECONDS).eut(TierEU.RECIPE_EV)
                .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.Aluminum_Nitride_Dust.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uraninite, 5),
                        GT_Utility.getIntegratedCircuit(9))
                .fluidInputs(FluidRegistry.getFluidStack("advancedglue", 1000))
                .itemOutputs(ItemRefer.Special_Ceramics_Dust.get(9)).duration(5 * SECONDS).eut(TierEU.RECIPE_EV)
                .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(ItemRefer.Special_Ceramics_Dust.get(2), ItemList.Shape_Extruder_Plate.get(0L))
                .itemOutputs(ItemRefer.Special_Ceramics_Plate.get(1)).duration(20 * SECONDS).eut(TierEU.RECIPE_HV)
                .addTo(extruderRecipes);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Raw_Cylinder.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "PPP", "PFP", "PPP", 'P', ItemRefer.Special_Ceramics_Plate.get(1), 'F',
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1) });

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.Raw_Cylinder.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Titanium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Titanium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6),
                        GT_Utility.getIntegratedCircuit(4))
                .itemOutputs(ItemRefer.Titanium_Plated_Cylinder.get(1)).duration(15 * SECONDS).eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_Utility.getIntegratedCircuit(16),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2))
                .fluidInputs(FluidRegistry.getFluidStack("liquidoxygen", 1000), Materials.NitrogenDioxide.getGas(1000))
                .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 500)).duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV).addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(16), WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 2))
                .fluidInputs(FluidRegistry.getFluidStack("liquidoxygen", 1000), Materials.NitrogenDioxide.getGas(1000))
                .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 500)).duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV).addTo(multiblockChemicalReactorRecipes);

        if (GTPlusPlus.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_Utility.getIntegratedCircuit(16),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2))
                    .fluidInputs(
                            FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 1000),
                            Materials.NitrogenDioxide.getGas(1000))
                    .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 2000)).duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV).addTo(multiblockChemicalReactorRecipes);
            GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_Utility.getIntegratedCircuit(16),
                            WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 2))
                    .fluidInputs(
                            FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 1000),
                            Materials.NitrogenDioxide.getGas(1000))
                    .fluidOutputs(FluidRegistry.getFluidStack("combustionpromotor", 2000)).duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV).addTo(multiblockChemicalReactorRecipes);
        }

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Universal_Chemical_Fuel_Engine.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "TZT", "ALB", "WGW", 'T',
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 1), 'Z', "circuitUltimate",
                        'A', ItemList.Machine_Multi_DieselEngine.get(1), 'B',
                        ItemList.Machine_Multi_ExtremeDieselEngine.get(1), 'L', ItemList.Hull_LuV, 'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 1), 'G',
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Osmium, 1), });

        // neutron activator
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.PolyvinylChloride, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Plastic, 4),
                        GT_Utility.getIntegratedCircuit(8))
                .fluidInputs(FluidRegistry.getFluidStack("dye.chemical.dyecyan", 144))
                .itemOutputs(ItemRefer.Plastic_Case.get(1)).duration(5 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.PolyvinylChloride, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Plastic, 4),
                        GT_Utility.getIntegratedCircuit(8))
                .fluidInputs(FluidRegistry.getFluidStack("dye.watermixed.dyecyan", 144))
                .itemOutputs(ItemRefer.Plastic_Case.get(1)).duration(5 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Plastic_Case.get(1),
                new Object[] { "PCP", "CDC", "PCP", 'P',
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.PolyvinylChloride, 1), 'C',
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Plastic, 1), 'D', "dyeCyan" });

        MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Coated_Basic.get(1), ItemList.Circuit_Chip_ULPIC.get(1),
                        ItemList.ULV_Coil.get(2), ItemList.Battery_RE_ULV_Tantalum.get(1) },
                new FluidStack[] { Materials.RedAlloy.getMolten(144), Materials.Aluminium.getMolten(144) },
                ItemRefer.Micro_Heater.get(1),
                120,
                40,
                1);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Quartzite, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 4))
                .fluidInputs(Materials.Water.getFluid(1000)).itemOutputs(ItemRefer.Quartz_Wafer.get(1))
                .outputChances(3333).requiresCleanRoom().duration(5 * MINUTES).eut(TierEU.RECIPE_LV)
                .addTo(autoclaveRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Quartzite, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 4))
                .fluidInputs(FluidRegistry.getFluidStack("ic2distilledwater", 1000))
                .itemOutputs(ItemRefer.Quartz_Wafer.get(1)).requiresCleanRoom().duration(1 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_LV).addTo(autoclaveRecipes);

        MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                new ItemStack[] { ItemRefer.Quartz_Wafer.get(1), ItemRefer.Special_Ceramics_Plate.get(2),
                        ItemRefer.Micro_Heater.get(1), ItemList.Circuit_Chip_ILC.get(4) },
                new FluidStack[] { Materials.EnergeticAlloy.getMolten(72), Materials.Silver.getMolten(18) },
                ItemRefer.Quartz_Crystal_Resonator.get(1),
                480,
                40,
                1);

        for (Materials tMat : solderingMats) {
            int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                    : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            GT_Values.RA.stdBuilder()
                    .itemInputs(
                            ItemRefer.Quartz_Crystal_Resonator.get(2),
                            ItemRefer.Plastic_Case.get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1),
                            ItemList.Cover_Screen.get(1),
                            GT_OreDictUnificator.get(OrePrefixes.componentCircuit, Materials.Diode, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8))
                    .fluidInputs(tMat.getMolten(144 * tMultiplier)).itemOutputs(ItemRefer.Inverter.get(1))
                    .duration(12 * SECONDS).eut(TierEU.RECIPE_MV).addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                    .itemInputs(
                            ItemRefer.Quartz_Crystal_Resonator.get(2),
                            ItemRefer.Plastic_Case.get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1),
                            ItemList.Cover_Screen.get(1),
                            ItemList.Circuit_Parts_DiodeASMD.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8))
                    .fluidInputs(tMat.getMolten(144 * tMultiplier)).itemOutputs(ItemRefer.Inverter.get(1))
                    .duration(12 * SECONDS).eut(TierEU.RECIPE_MV).addTo(assemblerRecipes);
            if (NewHorizonsCoreMod.isModLoaded()) {
                GT_Values.RA.stdBuilder()
                        .itemInputs(
                                ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
                                GT_ModHandler.getModItem("dreamcraft", "item.EngravedGoldChip", 16),
                                ItemList.Circuit_Chip_SoC2.get(8),
                                ItemList.Circuit_Chip_NOR.get(32),
                                MyMaterial.signalium.get(OrePrefixes.bolt, 32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8),
                                GT_ModHandler.getIC2Item("reactorVent", 1L, 1))
                        .fluidInputs(tMat.getMolten(288 * tMultiplier)).itemOutputs(ItemRefer.HiC_T1.get(1))
                        .duration(1 * MINUTES).eut(TierEU.RECIPE_IV).addTo(assemblerRecipes);

                GT_Values.RA.stdBuilder()
                        .itemInputs(
                                ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
                                GT_ModHandler.getModItem("dreamcraft", "item.EngravedGoldChip", 16),
                                ItemList.Circuit_Chip_SoC2.get(8),
                                ItemList.Circuit_Chip_NOR.get(32),
                                MyMaterial.signalium.get(OrePrefixes.bolt, 32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8),
                                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.TinAlloy, 1))
                        .fluidInputs(tMat.getMolten(288 * tMultiplier)).itemOutputs(ItemRefer.HiC_T1.get(1))
                        .duration(1 * MINUTES).eut(TierEU.RECIPE_IV).addTo(assemblerRecipes);
            }
        }

        GT_ModHandler.addCraftingRecipe(
                Loaders.NeutronAccelerators[0].copy(),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "WPM", "CHI", "WPM", 'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 1), 'P',
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lead, 1), 'M',
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Lead, 1), 'C',
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 1), 'H', ItemList.Hull_ULV, 'I',
                        ItemRefer.Inverter.get(1), });

        GT_ModHandler.addCraftingRecipe(
                Loaders.NeutronAccelerators[1].copy(),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "WPM", "CHI", "WPM", 'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1), 'P',
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 1), 'M',
                        ItemList.Electric_Motor_LV, 'C', "plateAnyRubber", 'H', ItemList.Hull_LV, 'I',
                        ItemRefer.Inverter.get(1), });

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.Inverter.get(1),
                        ItemList.Hull_MV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnyCopper, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Beryllium, 2),
                        ItemList.Electric_Motor_MV.get(2))
                .itemOutputs(Loaders.NeutronAccelerators[2].copy()).duration(15 * SECONDS).eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.Inverter.get(1),
                        ItemList.Hull_HV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.PolyvinylChloride, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Beryllium, 2),
                        ItemList.Electric_Motor_HV.get(2))
                .itemOutputs(Loaders.NeutronAccelerators[3].copy()).duration(15 * SECONDS).eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[5].copy())
                .metadata(RESEARCH_TIME, 16 * MINUTES)
                .itemInputs(
                        ItemRefer.Inverter.get(2),
                        ItemList.Hull_LuV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NetherStar, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NeodymiumMagnetic, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NeodymiumMagnetic, 4),
                        ItemList.Electric_Motor_LuV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorMV, 4))
                .fluidInputs(Materials.Argon.getGas(3000)).itemOutputs(Loaders.NeutronAccelerators[6].copy())
                .eut(TierEU.RECIPE_LuV).duration(15 * SECONDS).addTo(AssemblyLine);

        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[6].copy())
                .metadata(RESEARCH_TIME, 16 * MINUTES)
                .itemInputs(
                        ItemRefer.Inverter.get(2),
                        ItemList.Hull_ZPM.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.VanadiumGallium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NetherStar, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Polybenzimidazole, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SamariumMagnetic, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SamariumMagnetic, 4),
                        ItemList.Electric_Motor_ZPM.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorEV, 4))
                .fluidInputs(WerkstoffLoader.Xenon.getFluidOrGas(3000))
                .itemOutputs(Loaders.NeutronAccelerators[7].copy()).eut(TierEU.RECIPE_ZPM).duration(15 * SECONDS)
                .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, Loaders.NeutronAccelerators[7].copy())
                .metadata(RESEARCH_TIME, 16 * MINUTES)
                .itemInputs(
                        ItemRefer.Inverter.get(4),
                        ItemList.Hull_UV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NetherStar, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plateTriple, Materials.Polybenzimidazole, 4),
                        ItemList.ZPM_Coil.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NickelZincFerrite, 16),
                        ItemList.ZPM_Coil.get(4),
                        ItemList.Electric_Motor_UV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorLuV, 4))
                .fluidInputs(WerkstoffLoader.Oganesson.getFluidOrGas(3000))
                .itemOutputs(Loaders.NeutronAccelerators[8].copy()).eut(TierEU.RECIPE_UV).duration(15 * SECONDS)
                .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemList.Casing_IV.get(1L),
                        ItemList.Cover_ActivityDetector.get(1L),
                        ItemList.Cover_Screen.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VibrantAlloy, 4),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1),
                        ItemList.Sensor_HV.get(2),
                        GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(Materials.Helium.getGas(1000)).itemOutputs(Loaders.NS.copy()).duration(10 * SECONDS)
                .eut(TierEU.RECIPE_EV).addTo(assemblerRecipes);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Neutron_Source.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { " P ", "PUP", " P ", 'P',
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Steel, 1), 'U',
                        ItemRefer.High_Density_Uranium.get(1) });

        MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                new ItemStack[] { ItemRefer.HiC_T2.get(2), ItemList.Emitter_EV.get(2),
                        ItemRefer.Neutron_Source.get(1) },
                new FluidStack[] { Materials.StainlessSteel.getMolten(576), Materials.TungstenCarbide.getMolten(144) },
                Loaders.NA.copy(),
                7680,
                100,
                1);
    }

    public static void InitLoadRecipe() {

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.Inverter.get(1),
                        ItemList.Hull_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StyreneButadieneRubber, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.IronMagnetic, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenCarbide, 2),
                        ItemList.Electric_Motor_EV.get(2))
                .itemOutputs(Loaders.NeutronAccelerators[4].copy()).duration(15 * SECONDS).eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemRefer.Inverter.get(1),
                        ItemList.Hull_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicone, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SteelMagnetic, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.TungstenCarbide, 2),
                        ItemList.Electric_Motor_IV.get(2))
                .itemOutputs(Loaders.NeutronAccelerators[5].copy()).duration(15 * SECONDS).eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);

        // Al2O3 + 2N + 3C = 2AlN + 3CO
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sapphire, 5),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3))
                .fluidInputs(FluidRegistry.getFluidStack("liquidnitrogen", 2000))
                .fluidOutputs(Materials.CarbonMonoxide.getGas(3000)).itemOutputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
                .duration(10 * SECONDS).eut(TierEU.RECIPE_EV).metadata(COIL_HEAT, 4600).addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GreenSapphire, 5),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3))
                .fluidInputs(FluidRegistry.getFluidStack("liquidnitrogen", 2000))
                .fluidOutputs(Materials.CarbonMonoxide.getGas(3000)).itemOutputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
                .duration(10 * SECONDS).eut(TierEU.RECIPE_EV).metadata(COIL_HEAT, 4600).addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 5),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3))
                .fluidInputs(FluidRegistry.getFluidStack("liquidnitrogen", 2000))
                .fluidOutputs(Materials.CarbonMonoxide.getGas(3000)).itemOutputs(ItemRefer.Aluminum_Nitride_Dust.get(2))
                .duration(10 * SECONDS).eut(TierEU.RECIPE_EV).metadata(COIL_HEAT, 4600).addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 1),
                        GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingotHot))
                .duration(6 * MINUTES + 40 * SECONDS).eut(TierEU.RECIPE_ZPM).metadata(COIL_HEAT, 7000)
                .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder().itemInputs(MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingotHot, 1))
                .itemOutputs(MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingot, 1)).duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LuV).addTo(vacuumFreezerRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.naquadahBasedFuelMkVIDepleted.getFluidOrGas(1000))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1))
                .outputChances(1000, 500, 400, 50, 20, 5).fluidOutputs(MyMaterial.metastableOganesson.getMolten(122))
                .duration(12 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LuV).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1000))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1))
                .outputChances(1000, 500, 400, 50, 20, 5).fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(182))
                .duration(12 * SECONDS + 10 * TICKS).eut(TierEU.RECIPE_LuV).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.naquadahBasedFuelMkIVDepleted.getFluidOrGas(1000))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 32))
                .outputChances(9900, 9500, 9000, 8000, 5000, 3000)
                .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(864)).duration(2 * MINUTES + 5 * SECONDS)
                .eut(TierEU.RECIPE_LuV).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.naquadahBasedFuelMkIIIDepleted.getFluidOrGas(1000))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 32))
                .outputChances(9500, 9000, 8000, 7000, 5000, 4000)
                .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(720)).duration(1 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_LuV).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.naquadahBasedFuelMkIIDepleted.getFluidOrGas(1000))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 32),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Americium, 32),
                        WerkstoffLoader.Californium.get(OrePrefixes.dust, 32))
                .outputChances(9000, 8500, 5000, 4000, 2000).fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(144))
                .duration(6 * MINUTES + 40 * SECONDS).eut(TierEU.RECIPE_EV).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1000))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Barium, 6L))
                .outputChances(6000, 1000, 5000).fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(10))
                .duration(50 * SECONDS).eut(TierEU.RECIPE_EV / 2).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.thoriumBasedLiquidFuelDepleted.getFluidOrGas(1000))
                .itemOutputs(
                        WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 64),
                        WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 16),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Praseodymium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Praseodymium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 2),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 4))
                .outputChances(10000, 8000, 10000, 8000, 3000, 5000).duration(1 * MINUTES + 15 * SECONDS)
                .eut(TierEU.RECIPE_EV / 2).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1000))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tritanium, 9),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 2))
                .outputChances(5000, 8000, 7500).fluidOutputs(WerkstoffLoader.Krypton.getFluidOrGas(144))
                .duration(2 * MINUTES + 5 * SECONDS).eut(TierEU.RECIPE_IV).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.thoriumTetrafluoride.getFluidOrGas(1000))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Thorium, 1))
                .fluidOutputs(MyMaterial.thorium232Tetrafluoride.getFluidOrGas(750)).duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.naquadahEmulsion.getFluidOrGas(1000))
                .itemOutputs(
                        MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 4),
                        MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 2),
                        MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 1),
                        MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 1),
                        MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 1),
                        MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 1))
                .outputChances(8000, 7500, 5000, 2000, 500, 100)
                .fluidOutputs(MyMaterial.naquadahSolution.getFluidOrGas(500)).duration(40 * SECONDS)
                .eut(TierEU.RECIPE_MV).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_Utility.getIntegratedCircuit(1))
                .fluidInputs(MyMaterial.naquadahBasedFuelMkIDepleted.getFluidOrGas(1000))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 48),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 32),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Europium, 32))
                .outputChances(9000, 8500, 5000, 4000, 2000).fluidOutputs(WerkstoffLoader.Xenon.getFluidOrGas(144))
                .duration(5 * MINUTES).eut(TierEU.RECIPE_EV).addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder().itemInputs(MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 4))
                .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1),
                        WerkstoffLoader.Tiberium.get(OrePrefixes.dust, 1))
                .outputChances(10000, 9500, 8000, 2500, 2000, 2000).fluidOutputs(Materials.Radon.getGas(20))
                .duration(45 * SECONDS).eut(TierEU.RECIPE_MV).addTo(centrifugeRecipes);
    }

    public static void Fixer() {
        MaterialFix.MaterialFluidExtractionFix(MyMaterial.atomicSeparationCatalyst);
        MaterialFix.MaterialFluidExtractionFix(MyMaterial.extremelyUnstableNaquadah);
        MaterialFix.MaterialFluidExtractionFix(MyMaterial.metastableOganesson);
        MaterialFix.MaterialFluidExtractionFix(MyMaterial.shirabon);
    }
}
