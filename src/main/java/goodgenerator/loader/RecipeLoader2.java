package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.preciseAssemblerRecipes;
import static goodgenerator.util.MyRecipeAdder.computeRangeNKE;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidCannerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.plasmaForgeRecipes;
import static gregtech.api.recipe.RecipeMaps.unpackagerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.NKE_RANGE;
import static gregtech.api.util.GTRecipeConstants.PRECISE_ASSEMBLER_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static tectech.loader.recipe.BaseRecipeLoader.getItemContainer;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;
import tectech.recipe.TTRecipeAdder;

public class RecipeLoader2 {

    public static void RecipeLoad() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlueAlloy, 1),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 32),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Beryllium, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1))
            .itemOutputs(ItemRefer.Speeding_Pipe.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Compact MK1 Fusion Disassembly Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Compact_Fusion_Coil_T0.get(1))
            .itemOutputs(ItemList.Casing_Coil_Superconductor.get(3))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Compact MK2 Fusion Disassembly Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Compact_Fusion_Coil_T1.get(1))
            .itemOutputs(ItemList.Casing_Fusion_Coil.get(3))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        // Compact MK3 Fusion Disassembly Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Compact_Fusion_Coil_T2.get(1))
            .itemOutputs(ItemList.Casing_Fusion_Coil.get(3))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.zircaloy2, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.zircaloy2, 513, 480, 2800, false);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.zircaloy4, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.zircaloy4, 513, 480, 2800, false);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.incoloy903, 2400, 1920, 3700, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.adamantiumAlloy, 2500, 1920, 5500, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.marM200, 200, 7680, 5000, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.marM200, 220, 7680, 5000, false);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.signalium, 1600, 30720, 4000, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.lumiium, 1600, 30720, 4000, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.artheriumSn, 500, 122880, 6500, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.titaniumBetaC, 400, 7680, 5300, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.dalisenite, 800, 491520, 8700, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.hikarium, 1200, 30720, 5400, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.tairitsu, 1200, 1966080, 7400, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.preciousMetalAlloy, 2400, 7864320, 10000, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.enrichedNaquadahAlloy, 2400, 7864320, 11000, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.metastableOganesson, 600, 7864320, 12000, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.shirabon, 600, 31457280, 13000, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.atomicSeparationCatalyst, 35000, 120, 5000, false);

        GTModHandler.removeFurnaceSmelting(GGMaterial.dalisenite.get(OrePrefixes.dust)); // :doom:

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.dalisenite.get(OrePrefixes.ingotHot, 1))
            .itemOutputs(GGMaterial.dalisenite.get(OrePrefixes.ingot, 1))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.shirabon.get(OrePrefixes.ingotHot, 1))
            .itemOutputs(GGMaterial.shirabon.get(OrePrefixes.ingot, 1))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_UHV)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.zircaloy4.get(OrePrefixes.plate, 4),
                GGMaterial.zircaloy2.get(OrePrefixes.ring, 2),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1), ItemRefer.High_Density_Uranium.get(1))
            .itemOutputs(ItemRefer.Fuel_Rod_U_1.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_U_1.get(2),
                GGMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemRefer.Fuel_Rod_U_2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_U_2.get(2),
                GGMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                GTUtility.getIntegratedCircuit(5))
            .itemOutputs(ItemRefer.Fuel_Rod_U_4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_U_1.get(4),
                GGMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(ItemRefer.Fuel_Rod_U_4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1), ItemRefer.High_Density_Plutonium.get(1))
            .itemOutputs(ItemRefer.Fuel_Rod_Pu_1.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_Pu_1.get(2),
                GGMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemRefer.Fuel_Rod_Pu_2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_Pu_2.get(2),
                GGMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                GTUtility.getIntegratedCircuit(5))
            .itemOutputs(ItemRefer.Fuel_Rod_Pu_4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_Pu_1.get(4),
                GGMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(ItemRefer.Fuel_Rod_Pu_4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.YOTTank_Casing.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "BPB", "FOF", "BPB", 'B',
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.BlackSteel, 1), 'P',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 1), 'F',
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 1), 'O',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1), });

        GTModHandler.addCraftingRecipe(
            ItemRefer.YOTTank.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SPS", "ECE", "SLS", 'S', GTOreDictUnificator.get(OrePrefixes.screw, Materials.BlueSteel, 1),
                'P', ItemList.Cover_Screen.get(1), 'E', "circuitData", 'L',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 1), 'C',
                ItemRefer.YOTTank_Casing.get(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Output_IV.get(1),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiPart", 1, 440),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.CertusQuartz, 8),
                GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Plastic.getMolten(144))
            .itemOutputs(Loaders.YFH)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Steel.get(12L),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin, 4),
                GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Plastic.getMolten(144))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T1.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Aluminium.get(3L),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin, 4),
                GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Plastic.getMolten(144))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T1.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_StainlessSteel.get(2L),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin, 4),
                GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Plastic.getMolten(144))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T1.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Titanium.get(64L),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 8),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel, 4),
                GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2304))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(18L),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel, 4),
                GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2304))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Chrome.get(4L),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel, 4),
                GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2304))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fluid_Storage_Core_T1.get(32),
                ItemRefer.Fluid_Storage_Core_T1.get(32),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.BlackSteel, 16),
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2304))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T2.get(1))
            .metadata(RESEARCH_TIME, 17 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.StainlessSteel, 4),
                ItemList.Electric_Pump_HV.get(8),
                ItemList.Quantum_Tank_LV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmium, 8),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 1L, 6),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polycaprolactam, 32))
            .fluidInputs(new FluidStack(solderIndalloy, 2304), Materials.Lubricant.getFluid(4000))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T3.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T3.get(1))
            .metadata(RESEARCH_TIME, 34 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Titanium, 4),
                ItemList.Electric_Pump_EV.get(8),
                ItemList.Quantum_Tank_LV.get(4),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 8),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 4L, 6),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.StyreneButadieneRubber, 64),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 64))
            .fluidInputs(new FluidStack(solderIndalloy, 18432), Materials.Lubricant.getFluid(16000))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T4.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T4.get(1))
            .metadata(RESEARCH_TIME, 1 * HOURS + 8 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.MysteriousCrystal, 4),
                ItemList.Electric_Pump_IV.get(8),
                ItemList.Quantum_Tank_HV.get(8),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 8),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 16L, 6),
                GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Polycaprolactam, 24),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Titanium, 64))
            .fluidInputs(
                Materials.Draconium.getMolten(2304),
                Materials.Titanium.getMolten(288),
                Materials.Lubricant.getFluid(64000))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T5.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T5.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS + 15 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 4),
                ItemList.Electric_Pump_LuV.get(8),
                ItemList.Quantum_Tank_EV.get(16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 16),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Machine_IV_Compressor.get(64))
            .fluidInputs(
                Materials.Draconium.getMolten(2304),
                Materials.Titanium.getMolten(288),
                Materials.Lubricant.getFluid(64000))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T6.get(1))
            .eut(TierEU.RECIPE_UHV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T6.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS + 30 * MINUTES)
            .itemInputs(
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Electric_Pump_ZPM.get(8),
                GTModHandler.getModItem(Avaritia.ID, "Neutronium_Compressor", 1L, 0),
                ItemList.Quantum_Tank_EV.get(32),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 8),
                GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.InfinityCatalyst, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 16))
            .fluidInputs(
                Materials.Draconium.getMolten(23040),
                new FluidStack(solderIndalloy, 2304),
                Materials.InfinityCatalyst.getMolten(1140))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T7.get(1))
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T7.get(1))
            .metadata(RESEARCH_TIME, 9 * HOURS)
            .itemInputs(
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Electric_Pump_UV.get(8),
                GTModHandler.getModItem(Avaritia.ID, "Neutronium_Compressor", 2L, 0),
                ItemList.Quantum_Tank_EV.get(64),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 16),
                GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Infinity, 24),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 16))
            .fluidInputs(
                Materials.Draconium.getMolten(36864),
                new FluidStack(solderIndalloy, 30240),
                Materials.InfinityCatalyst.getMolten(5670))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T8.get(1))
            .eut(TierEU.RECIPE_UIV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T8.get(1))
            .metadata(RESEARCH_TIME, 180 * HOURS)
            .itemInputs(
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Electric_Pump_UHV.get(8),
                GTModHandler.getModItem(Avaritia.ID, "Neutronium_Compressor", 2L, 0),
                ItemList.Quantum_Tank_IV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 32),
                GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Infinity, 36),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 8))
            .fluidInputs(
                Materials.Draconium.getMolten(36864),
                new FluidStack(solderIndalloy, 30240),
                MaterialsUEVplus.TranscendentMetal.getMolten(1440),
                Materials.InfinityCatalyst.getMolten(5670))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T9.get(1))
            .eut(TierEU.RECIPE_UMV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T9.get(1))
            .metadata(RESEARCH_TIME, 360 * HOURS)
            .itemInputs(
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Electric_Pump_UEV.get(8),
                GTModHandler.getModItem(Avaritia.ID, "Neutronium_Compressor", 4L, 0),
                ItemList.Quantum_Tank_IV.get(16),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 32),
                GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.CosmicNeutronium, 24),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, MaterialsUEVplus.SpaceTime, 4))
            .fluidInputs(
                Materials.Draconium.getMolten(36864),
                new FluidStack(solderIndalloy, 46080),
                MaterialsUEVplus.TranscendentMetal.getMolten(4320),
                Materials.InfinityCatalyst.getMolten(17010))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T10.get(1))
            .eut(TierEU.RECIPE_UXV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1),
                ItemRefer.Fluid_Storage_Core_T1.get(10),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4),
                ItemList.Electric_Pump_HV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 4),
                GTUtility.getIntegratedCircuit(5))
            .fluidInputs(Materials.Plastic.getMolten(144))
            .itemOutputs(ItemRefer.YOTTank_Cell_T1.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
                ItemRefer.Fluid_Storage_Core_T2.get(10),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 4),
                ItemList.Electric_Pump_EV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 4),
                GTUtility.getIntegratedCircuit(5))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(144))
            .itemOutputs(ItemRefer.YOTTank_Cell_T2.get(1))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T2.get(1))
            .metadata(RESEARCH_TIME, 17 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah, 1),
                ItemRefer.Fluid_Storage_Core_T3.get(10),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 8 },
                ItemList.Electric_Pump_IV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NiobiumTitanium, 8),
                GGMaterial.adamantiumAlloy.get(OrePrefixes.plate, 32))
            .fluidInputs(
                Materials.Quantium.getMolten(1440),
                FluidRegistry.getFluidStack("ic2coolant", 8000),
                Materials.Lubricant.getFluid(8000))
            .itemOutputs(ItemRefer.YOTTank_Cell_T3.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T3.get(1))
            .metadata(RESEARCH_TIME, 34 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                ItemRefer.Fluid_Storage_Core_T4.get(10),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 8 },
                ItemList.Electric_Pump_LuV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.MysteriousCrystal, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.ElectrumFlux, 32))
            .fluidInputs(
                Materials.Draconium.getMolten(1440),
                FluidRegistry.getFluidStack("ic2coolant", 16000),
                Materials.Lubricant.getFluid(16000))
            .itemOutputs(ItemRefer.YOTTank_Cell_T4.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T4.get(1))
            .metadata(RESEARCH_TIME, 1 * HOURS + 8 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                ItemRefer.Fluid_Storage_Core_T5.get(10),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                ItemList.Electric_Pump_ZPM.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.DraconiumAwakened, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 64))
            .fluidInputs(
                Materials.Draconium.getMolten(1440),
                FluidRegistry.getFluidStack("ic2coolant", 16000),
                Materials.Lubricant.getFluid(16000))
            .itemOutputs(ItemRefer.YOTTank_Cell_T5.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T5.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS + 15 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                ItemRefer.Fluid_Storage_Core_T6.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 8L },
                ItemList.Electric_Pump_UV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 64))
            .fluidInputs(
                Materials.DraconiumAwakened.getMolten(1440),
                FluidRegistry.getFluidStack("ic2coolant", 46080),
                Materials.Lubricant.getFluid(32000))
            .itemOutputs(ItemRefer.YOTTank_Cell_T6.get(1))
            .eut(TierEU.RECIPE_UHV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T6.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS + 30 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                ItemRefer.Fluid_Storage_Core_T7.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 8L },
                ItemList.Electric_Pump_UHV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 32),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 32))
            .fluidInputs(
                Materials.DraconiumAwakened.getMolten(14400),
                FluidRegistry.getFluidStack("supercoolant", 46080),
                Materials.Lubricant.getFluid(46080))
            .itemOutputs(ItemRefer.YOTTank_Cell_T7.get(1))
            .eut(TierEU.RECIPE_UEV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T7.get(1))
            .metadata(RESEARCH_TIME, 9 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                ItemRefer.Fluid_Storage_Core_T8.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 8L },
                ItemList.Electric_Pump_UEV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 12),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.CosmicNeutronium, 12),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.DraconiumAwakened, 12),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 64))
            .fluidInputs(
                Materials.DraconiumAwakened.getMolten(14400),
                FluidRegistry.getFluidStack("supercoolant", 46080),
                Materials.Lubricant.getFluid(46080))
            .itemOutputs(ItemRefer.YOTTank_Cell_T8.get(1))
            .eut(TierEU.RECIPE_UIV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        if (NewHorizonsCoreMod.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T8.get(1))
                .metadata(RESEARCH_TIME, 18 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 2),
                    ItemRefer.Fluid_Storage_Core_T9.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.UMV), 8L },
                    ItemList.Electric_Pump_UIV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 12),
                    GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.CosmicNeutronium, 12),
                    GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.DraconiumAwakened, 12),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 64))
                .fluidInputs(
                    Materials.DraconiumAwakened.getMolten(14400),
                    FluidRegistry.getFluidStack("supercoolant", 46080),
                    Materials.Lubricant.getFluid(46080))
                .itemOutputs(ItemRefer.YOTTank_Cell_T9.get(1))
                .eut(TierEU.RECIPE_UMV)
                .duration(50 * SECONDS)
                .addTo(AssemblyLine);

            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T9.get(1))
                .metadata(RESEARCH_TIME, 36 * HOURS)
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 2),
                    ItemRefer.Fluid_Storage_Core_T10.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.UXV), 12L },
                    ItemList.Electric_Pump_UMV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.CosmicNeutronium, 12),
                    GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.DraconiumAwakened, 12),
                    GTOreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 10))
                .fluidInputs(
                    Materials.DraconiumAwakened.getMolten(14400),
                    MaterialsUEVplus.TranscendentMetal.getMolten(1440),
                    FluidRegistry.getFluidStack("supercoolant", 46080),
                    Materials.Lubricant.getFluid(46080))
                .itemOutputs(ItemRefer.YOTTank_Cell_T10.get(1))
                .eut(TierEU.RECIPE_UXV)
                .duration(50 * SECONDS)
                .addTo(AssemblyLine);
        }

        // Craft 2x64X Tier to 1X+1 Tier
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fluid_Storage_Core_T6.get(64),
                ItemRefer.Fluid_Storage_Core_T6.get(64),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T7.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fluid_Storage_Core_T7.get(64),
                ItemRefer.Fluid_Storage_Core_T7.get(64),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T8.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fluid_Storage_Core_T8.get(64),
                ItemRefer.Fluid_Storage_Core_T8.get(64),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T9.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fluid_Storage_Core_T9.get(64),
                ItemRefer.Fluid_Storage_Core_T9.get(64),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T10.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.YOTTank_Cell_T1.get(1))
            .itemOutputs(
                ItemRefer.Fluid_Storage_Core_T1.get(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(unpackagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.YOTTank_Cell_T2.get(1))
            .itemOutputs(
                ItemRefer.Fluid_Storage_Core_T2.get(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(unpackagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.YOTTank_Cell_T3.get(1))
            .itemOutputs(
                ItemRefer.Fluid_Storage_Core_T3.get(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(unpackagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.YOTTank_Cell_T4.get(1))
            .itemOutputs(
                ItemRefer.Fluid_Storage_Core_T4.get(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(unpackagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.YOTTank_Cell_T5.get(1))
            .itemOutputs(
                ItemRefer.Fluid_Storage_Core_T5.get(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(unpackagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.YOTTank_Cell_T6.get(1))
            .itemOutputs(
                ItemRefer.Fluid_Storage_Core_T6.get(2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(unpackagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.YOTTank_Cell_T7.get(1))
            .itemOutputs(
                ItemRefer.Fluid_Storage_Core_T7.get(2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(unpackagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.YOTTank_Cell_T8.get(1))
            .itemOutputs(
                ItemRefer.Fluid_Storage_Core_T8.get(2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(unpackagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.YOTTank_Cell_T9.get(1))
            .itemOutputs(
                ItemRefer.Fluid_Storage_Core_T9.get(2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(unpackagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.YOTTank_Cell_T10.get(1))
            .itemOutputs(
                ItemRefer.Fluid_Storage_Core_T10.get(2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(unpackagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Tiberium.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1),
                WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1))
            .outputChances(10000, 2000)
            .fluidInputs(GGMaterial.naquadahGas.getFluidOrGas(250))
            .duration(400)
            .eut(TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Firestone.getGems(1))
            .fluidInputs(GGMaterial.lightNaquadahFuel.getFluidOrGas(144))
            .itemOutputs(WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Diamond.getGems(1))
            .fluidInputs(GGMaterial.heavyNaquadahFuel.getFluidOrGas(144))
            .itemOutputs(WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.HeavyFuel.getFluid(1000))
            .fluidOutputs(
                Materials.Toluene.getFluid(400),
                Materials.Benzene.getFluid(400),
                Materials.Phenol.getFluid(250))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Coal.getGems(1))
            .itemOutputs(Materials.Ash.getDust(1))
            .outputChances(10)
            .fluidOutputs(FluidRegistry.getFluidStack("fluid.coaltar", 250))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        if (OreDictionary.getOres("fuelCoke")
            .size() > 0) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    OreDictionary.getOres("fuelCoke")
                        .get(0))
                .itemOutputs(Materials.Ash.getDust(1))
                .outputChances(10)
                .fluidOutputs(FluidRegistry.getFluidStack("fluid.coaltar", 250))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(fluidExtractionRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(FluidRegistry.getFluidStack("fluid.coaltaroil", 100))
            .fluidOutputs(GGMaterial.cyclopentadiene.getFluidOrGas(30))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.WoodTar.getFluid(500))
            .fluidOutputs(GGMaterial.cyclopentadiene.getFluidOrGas(20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        // FeCl2 + Cl = FeCl3
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ferrousChloride.get(OrePrefixes.cell, 1), GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Chlorine.getGas(1000))
            .itemOutputs(Materials.IronIIIChloride.getCells(1))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // FeCl3 + H = FeCl2 + HCl
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.IronIIIChloride.getCells(1), GTUtility.getIntegratedCircuit(7))
            .fluidInputs(Materials.Hydrogen.getGas(1000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1000))
            .itemOutputs(GGMaterial.ferrousChloride.get(OrePrefixes.cell, 1))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // NH3 + 2C2H6O = C4H11N + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Ammonia.getCells(1), GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Ethanol.getFluid(2000))
            .fluidOutputs(Materials.Water.getFluid(2000))
            .itemOutputs(GGMaterial.diethylamine.get(OrePrefixes.cell, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(
                GGMaterial.cyclopentadiene.getFluidOrGas(2000),
                GGMaterial.ferrousChloride.getFluidOrGas(1000),
                GGMaterial.diethylamine.getFluidOrGas(8000),
                Materials.Ice.getSolid(4000))
            .fluidOutputs(GGMaterial.impureFerroceneMixture.getFluidOrGas(15000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ether.get(OrePrefixes.cell, 1), GTUtility.getIntegratedCircuit(1))
            .fluidInputs(GGMaterial.impureFerroceneMixture.getFluidOrGas(7500))
            .itemOutputs(GGMaterial.ferroceneSolution.get(OrePrefixes.cell, 1))
            .fluidOutputs(GGMaterial.ferroceneWaste.getFluidOrGas(5000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            GGMaterial.ferroceneWaste.getFluidOrGas(1000),
            new FluidStack[] { Materials.Water.getFluid(400), GGMaterial.diethylamine.getFluidOrGas(800),
                GGMaterial.ether.getFluidOrGas(500) },
            GTValues.NI,
            30 * SECONDS,
            TierEU.RECIPE_MV);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            GGMaterial.ferroceneSolution.getFluidOrGas(2000),
            new FluidStack[] { GGMaterial.ether.getFluidOrGas(1000) },
            GGMaterial.ferrocene.get(OrePrefixes.dust, 1),
            30 * SECONDS,
            TierEU.RECIPE_MV);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ferrocene.get(OrePrefixes.dust, 4), Materials.SodiumHydroxide.getDust(8))
            .fluidInputs(
                FluidRegistry.getFluidStack("fluid.kerosene", 40000),
                Materials.Naphtha.getFluid(3000),
                GGMaterial.diethylamine.getFluidOrGas(1000))
            .fluidOutputs(GGMaterial.ironedKerosene.getFluidOrGas(44000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ferrocene.get(OrePrefixes.dust, 4))
            .fluidInputs(
                FluidRegistry.getFluidStack("combustionpromotor", 4000),
                Materials.Naphtha.getFluid(40000),
                Materials.LightFuel.getFluid(3000),
                Materials.LPG.getFluid(1000),
                Materials.Tetranitromethane.getFluid(2000))
            .fluidOutputs(GGMaterial.ironedFuel.getFluidOrGas(50000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(multiblockChemicalReactorRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Combustion_Generator_EV.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PCP", "MHM", "GWG", 'G', GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Titanium, 1),
                'C', "circuitData", 'W', GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 1), 'P',
                ItemList.Electric_Piston_EV, 'H', ItemList.Hull_EV, 'M', ItemList.Electric_Motor_EV });

        GTModHandler.addCraftingRecipe(
            ItemRefer.Combustion_Generator_IV.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PCP", "MHM", "GWG", 'G',
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.TungstenSteel, 1), 'C', "circuitElite", 'W',
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 1), 'P', ItemList.Electric_Piston_IV,
                'H', ItemList.Hull_IV, 'M', ItemList.Electric_Motor_IV });

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidInputs(GGMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(250))
            .itemOutputs(ItemRefer.Fuel_Rod_LU_1.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidInputs(GGMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(250))
            .itemOutputs(ItemRefer.Fuel_Rod_LPu_1.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidCannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_LPu_1.get(2),
                GGMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemRefer.Fuel_Rod_LPu_2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_LPu_2.get(2),
                GGMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                GTUtility.getIntegratedCircuit(5))
            .itemOutputs(ItemRefer.Fuel_Rod_LPu_4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_LPu_1.get(4),
                GGMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(ItemRefer.Fuel_Rod_LPu_4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_LU_1.get(2),
                GGMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemRefer.Fuel_Rod_LU_2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_LU_2.get(2),
                GGMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                GTUtility.getIntegratedCircuit(5))
            .itemOutputs(ItemRefer.Fuel_Rod_LU_4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fuel_Rod_LU_1.get(4),
                GGMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(ItemRefer.Fuel_Rod_LU_4.get(1))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
            FluidRegistry.getFluidStack("lava", 20000),
            FluidRegistry.getFluidStack("ic2pahoehoelava", 20000),
            FluidRegistry.getFluidStack("ic2distilledwater", 20000),
            FluidRegistry.getFluidStack("steam", 3200000),
            FluidRegistry.getFluidStack("ic2superheatedsteam", 1600000),
            10000);

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
            FluidRegistry.getFluidStack("ic2hotcoolant", 16000),
            FluidRegistry.getFluidStack("ic2coolant", 16000),
            FluidRegistry.getFluidStack("ic2distilledwater", 20000),
            FluidRegistry.getFluidStack("ic2superheatedsteam", 3200000),
            FluidRegistry.getFluidStack("supercriticalsteam", 32000),
            8000);

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
            FluidRegistry.getFluidStack("molten.solarsalthot", 3200),
            FluidRegistry.getFluidStack("molten.solarsaltcold", 3200),
            FluidRegistry.getFluidStack("ic2distilledwater", 20000),
            FluidRegistry.getFluidStack("ic2superheatedsteam", 3200000),
            FluidRegistry.getFluidStack("supercriticalsteam", 32000),
            1600);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Lepidolite, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
            .itemOutputs(
                Materials.RockSalt.getDust(1),
                GGMaterial.lithiumChloride.get(OrePrefixes.dust, 3),
                Materials.Cryolite.getDust(4))
            .outputChances(8000, 8000, 8000)
            .duration(7 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.marM200.get(OrePrefixes.ingot, 18), Materials.Cerium.getIngots(1))
            .fluidInputs(GGMaterial.lithiumChloride.getMolten(144))
            .itemOutputs(GGMaterial.marCeM200.get(OrePrefixes.ingotHot, 19))
            .duration(4 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 5400)
            .addTo(blastFurnaceRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.SC_Turbine_Casing.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PhP", "GCG", "PwP", 'G', GGMaterial.marM200.get(OrePrefixes.gearGt, 1), 'C',
                ItemList.Casing_Turbine.get(1), 'P', GGMaterial.marCeM200.get(OrePrefixes.plate, 1), });

        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.marM200.get(OrePrefixes.gearGt, 2),
                GGMaterial.marCeM200.get(OrePrefixes.plate, 4),
                ItemList.Casing_Turbine.get(1))
            .itemOutputs(ItemRefer.SC_Turbine_Casing.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.SC_Fluid_Turbine.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "NPN", "GHG", "IPI", 'N', "circuitMaster", 'P', GGMaterial.marM200.get(OrePrefixes.plate, 1),
                'H', ItemList.Hull_IV.get(1), 'G', GGMaterial.marCeM200.get(OrePrefixes.gearGt, 1), 'I',
                GGMaterial.incoloy903.get(OrePrefixes.pipeLarge, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.marM200.get(OrePrefixes.plate, 2),
                GGMaterial.marCeM200.get(OrePrefixes.gearGt, 2),
                GGMaterial.incoloy903.get(OrePrefixes.pipeLarge, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 2),
                ItemList.Hull_IV.get(1))
            .itemOutputs(ItemRefer.SC_Fluid_Turbine.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.incoloy903.get(OrePrefixes.plate, 4),
                GGMaterial.marCeM200.get(OrePrefixes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NiobiumTitanium, 1),
                GTUtility.getIntegratedCircuit(8))
            .itemOutputs(ItemRefer.Pressure_Resistant_Wall.get(1))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Extreme_Heat_Exchanger.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "EPE", "PHP", "SPS", 'P',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1), 'H',
                ItemList.Hull_IV.get(1), 'S', GGMaterial.marM200.get(OrePrefixes.plate, 1), 'E',
                GTModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Salty_Root.get(1))
            .fluidInputs(GTModHandler.getWater(100))
            .itemOutputs(Materials.Salt.getDust(1), Materials.RockSalt.getDust(1), Materials.Saltpeter.getDust(1))
            .outputChances(9500, 8000, 5000)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalBathRecipes);

        if (NewHorizonsCoreMod.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Robot_Arm_IV.get(4),
                    ItemRefer.HiC_T1.get(4),
                    ItemList.Tool_DataOrb.get(3),
                    GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.Titanium, 4),
                    GGMaterial.hikarium.get(OrePrefixes.gearGt, 4),
                    GGMaterial.marM200.get(OrePrefixes.plateDouble, 2),
                    ItemRefer.IC2_Ir_Plate.get(2),
                    GGMaterial.lumiium.get(OrePrefixes.bolt, 48))
                .fluidInputs(Materials.Palladium.getMolten(1152))
                .itemOutputs(ItemRefer.Precise_Assembler.get(1))
                .duration(1 * MINUTES + 30 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T1.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EngravedDiamondCrystalChip", 8),
                    ItemList.Circuit_Chip_NAND.get(16),
                    GTModHandler.getIC2Item("reactorVentCore", 1L, 1))
                .itemOutputs(ItemRefer.HiC_T2.get(1))
                .fluidInputs(
                    Materials.Plastic.getMolten(288),
                    GGMaterial.signalium.getMolten(144),
                    GGMaterial.lumiium.getMolten(72),
                    Materials.Enderium.getMolten(72))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
                .noOptimize()
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T1.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EngravedDiamondCrystalChip", 8),
                    ItemList.Circuit_Chip_NAND.get(16),
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Aluminium, 2))
                .itemOutputs(ItemRefer.HiC_T2.get(1))
                .fluidInputs(
                    Materials.Plastic.getMolten(288),
                    GGMaterial.signalium.getMolten(144),
                    GGMaterial.lumiium.getMolten(72),
                    Materials.Enderium.getMolten(72))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
                .noOptimize()
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T2.get(2),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(8),
                    ItemList.Circuit_Chip_CrystalSoC2.get(1),
                    GTModHandler.getIC2Item("reactorVentDiamond", 1L, 1))
                .itemOutputs(ItemRefer.HiC_T3.get(1))
                .fluidInputs(
                    GGMaterial.adamantiumAlloy.getMolten(576),
                    GGMaterial.signalium.getMolten(288),
                    GGMaterial.lumiium.getMolten(144),
                    Materials.TungstenCarbide.getMolten(72))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 2)
                .noOptimize()
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T2.get(2),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(8),
                    ItemList.Circuit_Chip_CrystalSoC2.get(1),
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 2))
                .itemOutputs(ItemRefer.HiC_T3.get(1))
                .fluidInputs(
                    GGMaterial.adamantiumAlloy.getMolten(576),
                    GGMaterial.signalium.getMolten(288),
                    GGMaterial.lumiium.getMolten(144),
                    Materials.TungstenCarbide.getMolten(72))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 2)
                .noOptimize()
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T3.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EngravedEnergyChip", 8),
                    ItemList.Circuit_Chip_QuantumCPU.get(16),
                    GTModHandler.getIC2Item("reactorVentGold", 1L, 1))
                .itemOutputs(ItemRefer.HiC_T4.get(1))
                .fluidInputs(
                    GGMaterial.marM200.getMolten(1152),
                    GGMaterial.signalium.getMolten(576),
                    GGMaterial.lumiium.getMolten(288),
                    GGMaterial.artheriumSn.getMolten(144))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
                .noOptimize()
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T3.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EngravedEnergyChip", 8),
                    ItemList.Circuit_Chip_QuantumCPU.get(16),
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.EnergeticAlloy, 2))
                .itemOutputs(ItemRefer.HiC_T4.get(1))
                .fluidInputs(
                    GGMaterial.marM200.getMolten(1152),
                    GGMaterial.signalium.getMolten(576),
                    GGMaterial.lumiium.getMolten(288),
                    GGMaterial.artheriumSn.getMolten(144))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
                .noOptimize()
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T4.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EngravedManyullynCrystalChip", 8),
                    ItemList.Circuit_Chip_BioCPU.get(1),
                    Ic2Items.reactorVentSpread)
                .itemOutputs(ItemRefer.HiC_T5.get(1))
                .fluidInputs(
                    GGMaterial.titaniumBetaC.getMolten(1728),
                    GGMaterial.signalium.getMolten(1152),
                    GGMaterial.lumiium.getMolten(576),
                    GGMaterial.dalisenite.getMolten(288))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
                .noOptimize()
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T4.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EngravedManyullynCrystalChip", 8),
                    ItemList.Circuit_Chip_BioCPU.get(1),
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.TungstenCarbide, 2))
                .itemOutputs(ItemRefer.HiC_T5.get(1))
                .fluidInputs(
                    GGMaterial.titaniumBetaC.getMolten(1728),
                    GGMaterial.signalium.getMolten(1152),
                    GGMaterial.lumiium.getMolten(576),
                    GGMaterial.dalisenite.getMolten(288))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
                .noOptimize()
                .addTo(preciseAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Casing_LuV.get(3),
                    ItemList.Robot_Arm_EV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Osmiridium, 2),
                    GGMaterial.marM200.get(OrePrefixes.plateDouble, 2),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1),
                    MaterialsElements.getInstance().RUTHENIUM.getBolt(32),
                    GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Platinum, 8))
                .fluidInputs(GGMaterial.zircaloy4.getMolten(576))
                .itemOutputs(ItemRefer.Imprecise_Electronic_Unit.get(2))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Casing_ZPM.get(3),
                    ItemList.Robot_Arm_IV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, GGMaterial.lumiium.getBridgeMaterial(), 2),
                    GGMaterial.marCeM200.get(OrePrefixes.plateDouble, 2),
                    ItemRefer.HiC_T1.get(1),
                    GGMaterial.signalium.get(OrePrefixes.bolt, 32),
                    GGMaterial.titaniumBetaC.get(OrePrefixes.gearGtSmall, 8))
                .fluidInputs(Materials.BlackSteel.getMolten(576))
                .itemOutputs(ItemRefer.Precise_Electronic_Unit_T1.get(2))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Casing_UV.get(3),
                    ItemList.Robot_Arm_LuV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.ElectrumFlux, 4),
                    ItemRefer.HiC_T2.get(1),
                    ItemRefer.Precise_Electronic_Unit_T1.get(1),
                    GGMaterial.marCeM200.get(OrePrefixes.bolt, 32),
                    GGMaterial.artheriumSn.get(OrePrefixes.gearGtSmall, 8))
                .fluidInputs(GGMaterial.adamantiumAlloy.getMolten(1152))
                .itemOutputs(ItemRefer.Precise_Electronic_Unit_T2.get(4))
                .duration(4 * MINUTES)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Casing_MAX.get(3),
                    ItemList.Field_Generator_ZPM.get(2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 4),
                    ItemRefer.HiC_T3.get(1),
                    ItemRefer.Precise_Electronic_Unit_T2.get(1),
                    GGMaterial.titaniumBetaC.get(OrePrefixes.bolt, 32),
                    GGMaterial.dalisenite.get(OrePrefixes.gearGtSmall, 8))
                .fluidInputs(GGMaterial.artheriumSn.getMolten(1152))
                .itemOutputs(ItemRefer.Precise_Electronic_Unit_T3.get(4))
                .duration(4 * MINUTES)
                .eut(TierEU.RECIPE_UV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    getItemContainer("Casing_UEV").get(3),
                    ItemList.Field_Generator_UV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 8),
                    ItemRefer.HiC_T4.get(1),
                    ItemRefer.Precise_Electronic_Unit_T3.get(1),
                    GGMaterial.enrichedNaquadahAlloy.get(OrePrefixes.bolt, 32),
                    GGMaterial.tairitsu.get(OrePrefixes.gearGtSmall, 8))
                .fluidInputs(GGMaterial.preciousMetalAlloy.getMolten(1152))
                .itemOutputs(ItemRefer.Precise_Electronic_Unit_T4.get(4))
                .duration(4 * MINUTES)
                .eut(TierEU.RECIPE_UHV)
                .addTo(assemblerRecipes);
        }

        // Compact MK1 Fusion Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_Superconductor.get(3),
                ItemRefer.HiC_T2.get(1),
                ItemRefer.Special_Ceramics_Plate.get(2))
            .itemOutputs(ItemRefer.Compact_Fusion_Coil_T0.get(1))
            .fluidInputs(GGMaterial.marM200.getMolten(1152), GGMaterial.zircaloy4.getMolten(288))
            .duration(60 * SECONDS)
            .eut(9001)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
            .noOptimize()
            .addTo(preciseAssemblerRecipes);
        // Compact MK2 Fusion Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Fusion_Coil.get(3),
                ItemRefer.Quartz_Crystal_Resonator.get(2),
                ItemRefer.HiC_T3.get(1))
            .itemOutputs(ItemRefer.Compact_Fusion_Coil_T1.get(1))
            .fluidInputs(GGMaterial.artheriumSn.getMolten(576), GGMaterial.titaniumBetaC.getMolten(144))
            .duration(40 * SECONDS)
            .eut(14000)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 2)
            .noOptimize()
            .addTo(preciseAssemblerRecipes);
        // Compact MK3 Fusion Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Fusion_Coil.get(3),
                ItemRefer.Radiation_Protection_Plate.get(2),
                ItemList.QuantumStar.get(4),
                ItemRefer.HiC_T4.get(1))
            .itemOutputs(ItemRefer.Compact_Fusion_Coil_T2.get(1))
            .fluidInputs(GGMaterial.dalisenite.getMolten(576), GGMaterial.hikarium.getMolten(144))
            .duration(40 * SECONDS)
            .eut(114514)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
            .noOptimize()
            .addTo(preciseAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.FusionComputer_LuV.get(48),
                ItemRefer.HiC_T1.get(8),
                GGMaterial.marCeM200.get(OrePrefixes.plate, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 8),
                ItemList.Circuit_Wafer_HPIC.get(16),
                ItemList.Field_Generator_LuV.get(4),
                GGMaterial.marM200.get(OrePrefixes.stickLong, 8))
            .fluidInputs(GGMaterial.adamantiumAlloy.getMolten(9216))
            .itemOutputs(ItemRefer.Compact_Fusion_MK1.get(1))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Compact_Fusion_MK1.get(1))
            .metadata(RESEARCH_TIME, 20 * MINUTES)
            .itemInputs(
                ItemList.FusionComputer_ZPMV.get(48),
                new Object[] { "circuitUltimate", 1 },
                new Object[] { "circuitUltimate", 1 },
                new Object[] { "circuitUltimate", 1 },
                new Object[] { "circuitUltimate", 1 },
                ItemList.Circuit_Wafer_UHPIC.get(32),
                ItemList.ZPM_Coil.get(16),
                ItemList.Neutron_Reflector.get(4),
                ItemRefer.HiC_T2.get(8),
                ItemList.Field_Generator_ZPM.get(8),
                GGMaterial.artheriumSn.get(OrePrefixes.gearGtSmall, 32))
            .fluidInputs(
                GGMaterial.marCeM200.getMolten(2304),
                WerkstoffLoader.HDCS.getMolten(1152),
                GGMaterial.artheriumSn.getMolten(288))
            .itemOutputs(ItemRefer.Compact_Fusion_MK2.get(1))
            .eut(TierEU.RECIPE_ZPM / 2)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Compact_Fusion_MK2.get(1))
            .metadata(RESEARCH_TIME, 20 * MINUTES)
            .itemInputs(
                ItemList.FusionComputer_UV.get(48),
                new Object[] { "circuitSuperconductor", 1 },
                new Object[] { "circuitSuperconductor", 1 },
                new Object[] { "circuitSuperconductor", 1 },
                new Object[] { "circuitSuperconductor", 1 },
                ItemList.Circuit_Wafer_NPIC.get(64),
                ItemList.UV_Coil.get(16),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(8),
                ItemRefer.HiC_T3.get(8),
                ItemList.Field_Generator_UV.get(8),
                WerkstoffLoader.HDCS.get(OrePrefixes.gearGtSmall, 64))
            .fluidInputs(
                GGMaterial.titaniumBetaC.getMolten(2304),
                GGMaterial.dalisenite.getMolten(1152),
                Materials.Americium.getMolten(288))
            .itemOutputs(ItemRefer.Compact_Fusion_MK3.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);

        // Compact MK4 Fusion Coil
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Casing_Fusion_Internal.get(3), ItemRefer.HiC_T5.get(1), CI.getEnergyCore(4, 1))
            .itemOutputs(ItemRefer.Compact_Fusion_Coil_T3.get(1))
            .fluidInputs(
                FluidRegistry.getFluidStack("molten.energycrystal", 1152),
                FluidRegistry.getFluidStack("molten.laurenium", 144))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
            .noOptimize()
            .addTo(preciseAssemblerRecipes);

        // Compact MK4 Fusion Disassembly Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Compact_Fusion_Coil_T3.get(1))
            .itemOutputs(GregtechItemList.Casing_Fusion_Internal.get(3))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRefer.Compact_Fusion_MK3.get(1),
            2_560_000,
            4_096,
            (int) TierEU.RECIPE_UHV,
            256,
            new Object[] { GregtechItemList.FusionComputer_UV2.get(48), new Object[] { "circuitInfinite", 1 },
                new Object[] { "circuitInfinite", 1 }, new Object[] { "circuitInfinite", 1 },
                new Object[] { "circuitInfinite", 1 }, ItemList.Circuit_Wafer_PPIC.get(64), ItemList.UHV_Coil.get(16),
                MaterialsAlloy.TITANSTEEL.getPlateDense(8), ItemRefer.HiC_T4.get(8),
                ItemList.Field_Generator_UHV.get(8),
                GGMaterial.enrichedNaquadahAlloy.get(OrePrefixes.gearGtSmall, 64) },
            new FluidStack[] { GenericChem.TEFLON.getFluidStack(2304), GGMaterial.dalisenite.getMolten(1152),
                MaterialsAlloy.BOTMIUM.getFluidStack(288) },
            ItemRefer.Compact_Fusion_MK4.get(1),
            6000,
            (int) TierEU.RECIPE_UV);

        // Compact MK5 Fusion Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Fusion_Internal2.get(3),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 1),
                ItemRefer.HiC_T5.get(4),
                CI.getEnergyCore(5, 1))
            .itemOutputs(ItemRefer.Compact_Fusion_Coil_T4.get(1))
            .fluidInputs(
                MaterialsAlloy.BLACK_TITANIUM.getFluidStack(1152),
                GGMaterial.metastableOganesson.getMolten(576))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
            .noOptimize()
            .addTo(preciseAssemblerRecipes);

        // Compact MK5 Computer
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRefer.Compact_Fusion_MK4.get(1),
            10_240_000,
            16_384,
            (int) TierEU.RECIPE_UEV,
            256,
            new Object[] { GregtechItemList.FusionComputer_UV3.get(48), new Object[] { "circuitBio", 1 },
                new Object[] { "circuitBio", 1 }, new Object[] { "circuitBio", 1 }, new Object[] { "circuitBio", 1 },
                ItemList.Circuit_Wafer_QPIC.get(64), ItemList.UHV_Coil.get(64),
                MaterialsElements.STANDALONE.HYPOGEN.getPlateDense(8), ItemRefer.HiC_T5.get(8),
                ItemList.Field_Generator_UEV.get(8), GGMaterial.metastableOganesson.get(OrePrefixes.gearGtSmall, 64) },
            new FluidStack[] { GGMaterial.tairitsu.getMolten(2304), MaterialsAlloy.OCTIRON.getFluidStack(1152),
                MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(288) },
            ItemRefer.Compact_Fusion_MK5.get(1),
            6000,
            (int) TierEU.RECIPE_UHV);

        // Compact MK5 Fusion Disassembly Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Compact_Fusion_Coil_T4.get(1))
            .itemOutputs(GregtechItemList.Casing_Fusion_Internal2.get(3))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Antimony.getDust(8), GTUtility.getIntegratedCircuit(24))
            .fluidInputs(
                GGMaterial.ether.getFluidOrGas(1000),
                Materials.Fluorine.getGas(40000),
                Materials.Ice.getSolid(8000))
            .fluidOutputs(GGMaterial.antimonyPentafluorideSolution.getFluidOrGas(8000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(multiblockChemicalReactorRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            GGMaterial.antimonyPentafluorideSolution.getFluidOrGas(4000),
            new FluidStack[] { GGMaterial.antimonyPentafluoride.getFluidOrGas(4000),
                GGMaterial.ether.getFluidOrGas(500) },
            GTValues.NI,
            5 * SECONDS,
            TierEU.RECIPE_MV);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Plastic, 2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Concrete.getMolten(2304))
            .itemOutputs(ItemRefer.Coolant_Tower.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }

    public static void InitLoadRecipe() {

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_U_Depleted_1.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                Materials.Uranium.getDust(8),
                Materials.Plutonium.getDust(2),
                Materials.Graphite.getDust(8),
                Materials.Uranium235.getDust(1),
                Materials.Plutonium241.getDust(1))
            .outputChances(10000, 10000, 10000, 9000, 5000, 3000)
            .fluidOutputs(WerkstoffLoader.Neon.getFluidOrGas(32))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_U_Depleted_2.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(2),
                Materials.Uranium.getDust(16),
                Materials.Plutonium.getDust(4),
                Materials.Graphite.getDust(16),
                Materials.Uranium235.getDust(2),
                Materials.Plutonium241.getDust(2))
            .outputChances(10000, 10000, 10000, 9000, 5000, 3000)
            .fluidOutputs(WerkstoffLoader.Neon.getFluidOrGas(64))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_U_Depleted_4.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(4),
                Materials.Uranium.getDust(32),
                Materials.Plutonium.getDust(8),
                Materials.Graphite.getDust(32),
                Materials.Uranium235.getDust(4),
                Materials.Plutonium241.getDust(4))
            .outputChances(10000, 10000, 10000, 9000, 5000, 3000)
            .fluidOutputs(WerkstoffLoader.Neon.getFluidOrGas(128))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_Pu_Depleted_1.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                Materials.Plutonium.getDust(5),
                Materials.Plutonium241.getDust(2),
                Materials.Carbon.getDust(2),
                Materials.Uranium.getDust(1),
                Materials.Uranium235.getDust(1))
            .outputChances(10000, 10000, 10000, 9000, 5000, 3000)
            .fluidOutputs(Materials.Argon.getGas(32))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_Pu_Depleted_2.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(2),
                Materials.Plutonium.getDust(10),
                Materials.Plutonium241.getDust(4),
                Materials.Carbon.getDust(4),
                Materials.Uranium.getDust(2),
                Materials.Uranium235.getDust(2))
            .outputChances(10000, 10000, 10000, 9000, 5000, 3000)
            .fluidOutputs(Materials.Argon.getGas(64))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_Pu_Depleted_4.get(1))
            .itemOutputs(
                ItemRefer.Advanced_Fuel_Rod.get(4),
                Materials.Plutonium.getDust(20),
                Materials.Plutonium241.getDust(8),
                Materials.Carbon.getDust(8),
                Materials.Uranium.getDust(4),
                Materials.Uranium235.getDust(4))
            .outputChances(10000, 10000, 10000, 9000, 5000, 3000)
            .fluidOutputs(Materials.Argon.getGas(128))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_LPu_Depleted_1.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidOutputs(GGMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_LPu_Depleted_2.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(2))
            .fluidOutputs(GGMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_LPu_Depleted_4.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(4))
            .fluidOutputs(GGMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_LU_Depleted_1.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .fluidOutputs(GGMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_LU_Depleted_2.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(2))
            .fluidOutputs(GGMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fuel_Rod_LU_Depleted_4.get(1))
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(4))
            .fluidOutputs(GGMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Glowstone.getDust(4),
                Materials.Redstone.getDust(2),
                Materials.Aluminium.getDust(1),
                GTUtility.getIntegratedCircuit(3))
            .itemOutputs(ItemRefer.High_Energy_Mixture.get(4))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.High_Energy_Mixture.get(2))
            .fluidInputs(Materials.PhosphoricAcid.getFluid(4000))
            .itemOutputs(GGMaterial.lumiinessence.get(OrePrefixes.dust, 1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.AnnealedCopper.getDust(4),
                Materials.Ardite.getDust(2),
                Materials.RedAlloy.getDust(2),
                GTUtility.getIntegratedCircuit(4))
            .fluidInputs(Materials.Redstone.getMolten(288))
            .itemOutputs(GGMaterial.signalium.get(OrePrefixes.dust, 1))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.TinAlloy.getDust(4),
                Materials.SterlingSilver.getDust(2),
                GGMaterial.lumiinessence.get(OrePrefixes.dust, 2),
                GTUtility.getIntegratedCircuit(4))
            .fluidInputs(Materials.Glowstone.getMolten(288))
            .itemOutputs(GGMaterial.lumiium.get(OrePrefixes.dust, 1))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(GGMaterial.enrichedNaquadahAlloy.getMolten(144), WerkstoffLoader.Oganesson.getFluidOrGas(250))
            .fluidOutputs(GGMaterial.metastableOganesson.getMolten(36))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.metastableOganesson.get(OrePrefixes.dust))
            .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(250))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1000))
            .noOptimize()
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.HiC_T5.get(0))
            .fluidInputs(
                GGMaterial.metastableOganesson.getMolten(1152),
                GGMaterial.preciousMetalAlloy.getMolten(2304),
                MaterialsUEVplus.SpaceTime.getMolten(288),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(5000))
            .fluidOutputs(GGMaterial.shirabon.getMolten(144))
            .duration(10 * SECONDS)
            .eut(1_500_000_000)
            .metadata(COIL_HEAT, 13500)
            .addTo(plasmaForgeRecipes);
    }

    public static float EHEEfficiencyMultiplier = 1.2f;

    public static void FinishLoadRecipe() {
        for (GTRecipe plasmaFuel : RecipeMaps.plasmaFuels.getAllRecipes()) {
            FluidStack tPlasma = GTUtility.getFluidForFilledItem(plasmaFuel.mInputs[0], true);
            if (tPlasma == null) {
                continue;
            }
            int tUnit = plasmaFuel.mSpecialValue;
            if (tUnit > 500_000) {
                tPlasma.amount = 25000;
            } else if (tUnit > 300_000) {
                tPlasma.amount = 10000;
            } else if (tUnit > 100_000) {
                tPlasma.amount = 2500;
            } else if (tUnit > 10_000) {
                tPlasma.amount = 500;
            } else {
                tPlasma.amount = 100;
            }

            String tPlasmaName = FluidRegistry.getFluidName(tPlasma);

            if (tPlasmaName.split("\\.", 2).length == 2) {
                String tOutName = tPlasmaName.split("\\.", 2)[1];
                FluidStack output = FluidRegistry.getFluidStack(tOutName, tPlasma.amount);
                if (output == null) output = FluidRegistry.getFluidStack("molten." + tOutName, tPlasma.amount);
                if (output != null) {
                    long waterAmount = (long) (tUnit * EHEEfficiencyMultiplier * tPlasma.amount / 160);
                    long criticalSteamAmount = (long) (tUnit * EHEEfficiencyMultiplier * tPlasma.amount / 100);
                    MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
                        tPlasma,
                        output,
                        FluidRegistry.getFluidStack("ic2distilledwater", (int) waterAmount),
                        FluidRegistry.getFluidStack("ic2superheatedsteam", 0), // Plasma always outputs SC steam.
                        FluidRegistry.getFluidStack("supercriticalsteam", (int) criticalSteamAmount),
                        1);
                }
            }
        }
    }
}
