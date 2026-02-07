package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.neutronActivatorRecipes;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.preciseAssemblerRecipes;
import static goodgenerator.util.MyRecipeAdder.computeRangeNKE;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.plasmaForgeRecipes;
import static gregtech.api.recipe.RecipeMaps.unpackagerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.NKE_RANGE;
import static gregtech.api.util.GTRecipeConstants.PRECISE_ASSEMBLER_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
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

        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.extremelyUnstableNaquadah, 8000, 122880, 7000, false);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.zircaloy2, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.zircaloy4, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.incoloy903, 1200, 1920, 3700, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.adamantiumAlloy, 2500, 1920, 5500, true);
        CrackRecipeAdder.reAddBlastRecipe(GGMaterial.marM200, 200, 7680, 5000, true);
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
            .itemInputs(GGMaterial.zircaloy4.get(OrePrefixes.plate, 4), GGMaterial.zircaloy2.get(OrePrefixes.ring, 2))
            .circuit(2)
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
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
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.CertusQuartz, 8))
            .circuit(1)
            .fluidInputs(Materials.Polyethylene.getMolten(1 * INGOTS))
            .itemOutputs(Loaders.YFH)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Steel.get(12L),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin, 4))
            .circuit(1)
            .fluidInputs(Materials.Polyethylene.getMolten(1 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T1.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Aluminium.get(3L),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin, 4))
            .circuit(1)
            .fluidInputs(Materials.Polyethylene.getMolten(1 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T1.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_StainlessSteel.get(2L),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin, 4))
            .circuit(1)
            .fluidInputs(Materials.Polyethylene.getMolten(1 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T1.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Titanium.get(64L),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 8),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel, 4))
            .circuit(2)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(16 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(18L),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel, 4))
            .circuit(2)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(16 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Chrome.get(4L),
                GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel, 4))
            .circuit(2)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(16 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fluid_Storage_Core_T1.get(32),
                ItemRefer.Fluid_Storage_Core_T1.get(32),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.BlackSteel, 16))
            .circuit(10)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(16 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Fluid Storage Core T3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T2.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_EV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.StainlessSteel, 4),
                ItemList.Electric_Pump_HV.get(8),
                ItemList.Quantum_Tank_LV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmium, 8),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 1L, 6),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polycaprolactam, 32))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(16 * INGOTS), Materials.Lubricant.getFluid(4_000))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T3.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Storage Core T4
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T3.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Titanium, 4),
                ItemList.Electric_Pump_EV.get(8),
                ItemList.Quantum_Tank_LV.get(4),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 8),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 4L, 6),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.StyreneButadieneRubber, 64),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 64))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(2 * STACKS), Materials.Lubricant.getFluid(16_000))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T4.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Storage Core T5
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T4.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.MysteriousCrystal, 4),
                ItemList.Electric_Pump_IV.get(8),
                ItemList.Quantum_Tank_HV.get(8),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 8),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 16L, 6),
                GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Polycaprolactam, 24),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Titanium, 64))
            .fluidInputs(
                Materials.Draconium.getMolten(16 * INGOTS),
                Materials.Titanium.getMolten(2 * INGOTS),
                Materials.Lubricant.getFluid(64_000))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T5.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Storage Core T6
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T5.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 4),
                ItemList.Electric_Pump_LuV.get(8),
                ItemList.Quantum_Tank_EV.get(16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 16),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Machine_IV_Compressor.get(64))
            .fluidInputs(
                Materials.Draconium.getMolten(16 * INGOTS),
                Materials.Titanium.getMolten(2 * INGOTS),
                Materials.Lubricant.getFluid(64_000))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T6.get(1))
            .eut(TierEU.RECIPE_UHV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Storage Core T7
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T6.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Electric_Pump_ZPM.get(8),
                ItemList.Machine_Multi_NeutroniumCompressor.get(1),
                ItemList.Quantum_Tank_EV.get(32),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 8),
                GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.InfinityCatalyst, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 16))
            .fluidInputs(
                Materials.Draconium.getMolten(2 * STACKS + 32 * INGOTS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(16 * INGOTS),
                Materials.InfinityCatalyst.getMolten(1_140))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T7.get(1))
            .eut(TierEU.RECIPE_UEV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Storage Core T8
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T7.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UHV))
            .itemInputs(
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Electric_Pump_UV.get(8),
                ItemList.Machine_Multi_NeutroniumCompressor.get(2),
                ItemList.Quantum_Tank_EV.get(64),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 16),
                GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Infinity, 24),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 16))
            .fluidInputs(
                Materials.Draconium.getMolten(4 * STACKS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(3 * STACKS + 18 * INGOTS),
                Materials.InfinityCatalyst.getMolten(39 * INGOTS + 3 * EIGHTH_INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T8.get(1))
            .eut(TierEU.RECIPE_UIV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Storage Core T9
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T8.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UEV))
            .itemInputs(
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Electric_Pump_UHV.get(8),
                ItemList.Machine_Multi_NeutroniumCompressor.get(2),
                ItemList.Quantum_Tank_IV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 32),
                GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Infinity, 36),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 8))
            .fluidInputs(
                Materials.Draconium.getMolten(4 * STACKS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(3 * STACKS + 18 * INGOTS),
                Materials.TranscendentMetal.getMolten(10 * INGOTS),
                Materials.InfinityCatalyst.getMolten(39 * INGOTS + 3 * EIGHTH_INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T9.get(1))
            .eut(TierEU.RECIPE_UMV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Storage Core T10
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Fluid_Storage_Core_T9.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UIV))
            .itemInputs(
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Electric_Pump_UEV.get(8),
                ItemList.Machine_Multi_NeutroniumCompressor.get(4),
                ItemList.Quantum_Tank_IV.get(16),
                GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 32),
                GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.CosmicNeutronium, 24),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.SpaceTime, 4))
            .fluidInputs(
                Materials.Draconium.getMolten(4 * STACKS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(5 * STACKS),
                Materials.TranscendentMetal.getMolten(30 * INGOTS),
                Materials.InfinityCatalyst.getMolten(1 * STACKS + 54 * INGOTS + 1 * EIGHTH_INGOTS))
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
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 4))
            .circuit(5)
            .fluidInputs(Materials.Polyethylene.getMolten(1 * INGOTS))
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
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 4))
            .circuit(5)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * INGOTS))
            .itemOutputs(ItemRefer.YOTTank_Cell_T2.get(1))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Fluid Cell Block T3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T2.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_EV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah, 1),
                ItemRefer.Fluid_Storage_Core_T3.get(10),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 8 },
                ItemList.Electric_Pump_IV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NiobiumTitanium, 8),
                GGMaterial.adamantiumAlloy.get(OrePrefixes.plate, 32))
            .fluidInputs(
                Materials.Quantium.getMolten(10 * INGOTS),
                GTModHandler.getIC2Coolant(8_000),
                Materials.Lubricant.getFluid(8_000))
            .itemOutputs(ItemRefer.YOTTank_Cell_T3.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Cell Block T4
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T3.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                ItemRefer.Fluid_Storage_Core_T4.get(10),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 8 },
                ItemList.Electric_Pump_LuV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.MysteriousCrystal, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.ElectrumFlux, 32))
            .fluidInputs(
                Materials.Draconium.getMolten(10 * INGOTS),
                GTModHandler.getIC2Coolant(16_000),
                Materials.Lubricant.getFluid(16_000))
            .itemOutputs(ItemRefer.YOTTank_Cell_T4.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Cell Block T5
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T4.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                ItemRefer.Fluid_Storage_Core_T5.get(10),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                ItemList.Electric_Pump_ZPM.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.DraconiumAwakened, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 64))
            .fluidInputs(
                Materials.Draconium.getMolten(10 * INGOTS),
                GTModHandler.getIC2Coolant(16_000),
                Materials.Lubricant.getFluid(16_000))
            .itemOutputs(ItemRefer.YOTTank_Cell_T5.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Cell Block T6
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T5.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                ItemRefer.Fluid_Storage_Core_T6.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 8L },
                ItemList.Electric_Pump_UV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 64))
            .fluidInputs(
                Materials.DraconiumAwakened.getMolten(10 * INGOTS),
                GTModHandler.getIC2Coolant(5 * STACKS),
                Materials.Lubricant.getFluid(32_000))
            .itemOutputs(ItemRefer.YOTTank_Cell_T6.get(1))
            .eut(TierEU.RECIPE_UHV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Cell Block T7
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T6.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                ItemRefer.Fluid_Storage_Core_T7.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 8L },
                ItemList.Electric_Pump_UHV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 32),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 32))
            .fluidInputs(
                Materials.DraconiumAwakened.getMolten(1 * STACKS + 36 * INGOTS),
                Materials.SuperCoolant.getFluid(5 * STACKS),
                Materials.Lubricant.getFluid(5 * STACKS))
            .itemOutputs(ItemRefer.YOTTank_Cell_T7.get(1))
            .eut(TierEU.RECIPE_UEV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        // Fluid Cell Block T8
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T7.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UHV))
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
                Materials.DraconiumAwakened.getMolten(1 * STACKS + 36 * INGOTS),
                Materials.SuperCoolant.getFluid(5 * STACKS),
                Materials.Lubricant.getFluid(5 * STACKS))
            .itemOutputs(ItemRefer.YOTTank_Cell_T8.get(1))
            .eut(TierEU.RECIPE_UIV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        if (NewHorizonsCoreMod.isModLoaded()) {
            // Fluid Cell Block T9
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T8.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UEV))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 2),
                    ItemRefer.Fluid_Storage_Core_T9.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.UMV), 8L },
                    ItemList.Electric_Pump_UIV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 12),
                    GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.CosmicNeutronium, 12),
                    GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.ProtoHalkonite, 12),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 64))
                .fluidInputs(
                    Materials.DraconiumAwakened.getMolten(1 * STACKS + 36 * INGOTS),
                    Materials.SuperCoolant.getFluid(5 * STACKS),
                    Materials.DimensionallyShiftedSuperfluid.getFluid(5 * STACKS))
                .itemOutputs(ItemRefer.YOTTank_Cell_T9.get(1))
                .eut(TierEU.RECIPE_UMV)
                .duration(50 * SECONDS)
                .addTo(AssemblyLine);

            // Fluid Cell Block T10
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRefer.YOTTank_Cell_T9.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_UIV))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 2),
                    ItemRefer.Fluid_Storage_Core_T10.get(2),
                    new Object[] { OrePrefixes.circuit.get(Materials.UXV), 12L },
                    ItemList.Electric_Pump_UMV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.CosmicNeutronium, 12),
                    GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.ProtoHalkonite, 12),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 10))
                .fluidInputs(
                    Materials.DraconiumAwakened.getMolten(10 * INGOTS),
                    Materials.TranscendentMetal.getMolten(10 * INGOTS),
                    Materials.SuperCoolant.getFluid(5 * STACKS),
                    Materials.DimensionallyShiftedSuperfluid.getFluid(5 * STACKS))
                .itemOutputs(ItemRefer.YOTTank_Cell_T10.get(1))
                .eut(TierEU.RECIPE_UXV)
                .duration(50 * SECONDS)
                .addTo(AssemblyLine);
        }

        // Craft 2x64X Tier to 1X+1 Tier
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fluid_Storage_Core_T6.get(64), ItemRefer.Fluid_Storage_Core_T6.get(64))
            .circuit(2)
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T7.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fluid_Storage_Core_T7.get(64), ItemRefer.Fluid_Storage_Core_T7.get(64))
            .circuit(2)
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T8.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fluid_Storage_Core_T8.get(64), ItemRefer.Fluid_Storage_Core_T8.get(64))
            .circuit(2)
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T9.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Fluid_Storage_Core_T9.get(64), ItemRefer.Fluid_Storage_Core_T9.get(64))
            .circuit(2)
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
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1))
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
            .fluidInputs(GGMaterial.lightNaquadahFuel.getFluidOrGas(1 * INGOTS))
            .itemOutputs(WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Diamond.getGems(1))
            .fluidInputs(GGMaterial.heavyNaquadahFuel.getFluidOrGas(1 * INGOTS))
            .itemOutputs(WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.HeavyFuel.getFluid(1_000))
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

        if (!OreDictionary.getOres("fuelCoke")
            .isEmpty()) {
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
            .circuit(24)
            .fluidInputs(FluidRegistry.getFluidStack("fluid.coaltaroil", 20))
            .fluidOutputs(GGMaterial.cyclopentadiene.getFluidOrGas(6))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(Materials.WoodTar.getFluid(100))
            .fluidOutputs(GGMaterial.cyclopentadiene.getFluidOrGas(4))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        // FeCl2 + Cl = FeCl3
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ferrousChloride.get(OrePrefixes.cell, 1))
            .circuit(1)
            .fluidInputs(Materials.Chlorine.getGas(1_000))
            .itemOutputs(Materials.IronIIIChloride.getCells(1))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // FeCl3 + H = FeCl2 + HCl
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.IronIIIChloride.getCells(1))
            .circuit(7)
            .fluidInputs(Materials.Hydrogen.getGas(1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .itemOutputs(GGMaterial.ferrousChloride.get(OrePrefixes.cell, 1))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // NH3 + 2C2H6O = C4H11N + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Ammonia.getCells(1))
            .circuit(1)
            .fluidInputs(Materials.Ethanol.getFluid(2_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .itemOutputs(GGMaterial.diethylamine.get(OrePrefixes.cell, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                GGMaterial.cyclopentadiene.getFluidOrGas(2_000),
                GGMaterial.ferrousChloride.getFluidOrGas(1_000),
                GGMaterial.diethylamine.getFluidOrGas(8_000),
                Materials.Ice.getSolid(4_000))
            .fluidOutputs(GGMaterial.impureFerroceneMixture.getFluidOrGas(15_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ether.get(OrePrefixes.cell, 1))
            .circuit(1)
            .fluidInputs(GGMaterial.impureFerroceneMixture.getFluidOrGas(7_500))
            .itemOutputs(GGMaterial.ferroceneSolution.get(OrePrefixes.cell, 1))
            .fluidOutputs(GGMaterial.ferroceneWaste.getFluidOrGas(5_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            GGMaterial.ferroceneWaste.getFluidOrGas(1_000),
            new FluidStack[] { Materials.Water.getFluid(400), GGMaterial.diethylamine.getFluidOrGas(800),
                Materials.HydrochloricAcid.getFluid(200) },
            GTValues.NI,
            30 * SECONDS,
            TierEU.RECIPE_MV);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            GGMaterial.ferroceneSolution.getFluidOrGas(2_000),
            new FluidStack[] { GGMaterial.ether.getFluidOrGas(2_000) },
            GGMaterial.ferrocene.get(OrePrefixes.dust, 1),
            30 * SECONDS,
            TierEU.RECIPE_MV);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ferrocene.get(OrePrefixes.dust, 4), Materials.SodiumHydroxide.getDust(8))
            .fluidInputs(
                FluidRegistry.getFluidStack("fluid.kerosene", 40_000),
                Materials.Naphtha.getFluid(3_000),
                GGMaterial.diethylamine.getFluidOrGas(1_000))
            .fluidOutputs(GGMaterial.ironedKerosene.getFluidOrGas(44_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.ferrocene.get(OrePrefixes.dust, 4))
            .fluidInputs(
                FluidRegistry.getFluidStack("combustionpromotor", 4_000),
                Materials.Naphtha.getFluid(40_000),
                Materials.LightFuel.getFluid(3_000),
                Materials.LPG.getFluid(1_000),
                Materials.Tetranitromethane.getFluid(2_000))
            .fluidOutputs(GGMaterial.ironedFuel.getFluidOrGas(50_000))
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

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
            Materials.Lava.getFluid(160_000),
            FluidRegistry.getFluidStack("ic2pahoehoelava", 160_000),
            GTModHandler.getDistilledWater(80_000),
            FluidRegistry.getFluidStack("ic2superheatedsteam", 12_800_000),
            FluidRegistry.getFluidStack("supercriticalsteam", 12_800_000),
            80000);

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
            FluidRegistry.getFluidStack("ic2hotcoolant", 16_000),
            GTModHandler.getIC2Coolant(16_000),
            GTModHandler.getDistilledWater(20_000),
            FluidRegistry.getFluidStack("ic2superheatedsteam", 3_200_000),
            FluidRegistry.getFluidStack("supercriticalsteam", 3_200_000),
            8000);

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
            FluidRegistry.getFluidStack("molten.solarsalthot", 3_200),
            FluidRegistry.getFluidStack("molten.solarsaltcold", 3_200),
            GTModHandler.getDistilledWater(20_000),
            FluidRegistry.getFluidStack("ic2superheatedsteam", 3_200_000),
            FluidRegistry.getFluidStack("supercriticalsteam", 3_200_000),
            1600);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Lepidolite, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .itemOutputs(
                Materials.RockSalt.getDust(1),
                GGMaterial.lithiumChloride.get(OrePrefixes.dust, 3),
                Materials.Cryolite.getDust(4))
            .outputChances(8000, 8000, 8000)
            .duration(7 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalBathRecipes);

        // dust to fluid extraction, which isn't autogenned in Bartworks
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.lithiumChloride.get(OrePrefixes.dust, 1))
            .fluidOutputs(GGMaterial.lithiumChloride.getMolten(1 * INGOTS))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.marM200.get(OrePrefixes.ingot, 18), Materials.Cerium.getIngots(1))
            .fluidInputs(GGMaterial.lithiumChloride.getMolten(1 * INGOTS))
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
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NiobiumTitanium, 1))
            .circuit(8)
            .itemOutputs(ItemRefer.Pressure_Resistant_Wall.get(1))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Extreme_Heat_Exchanger.get(1),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "EPE", "PHP", "SPS", 'P',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1), 'H',
                ItemList.Hull_IV.get(1), 'S', GGMaterial.marCeM200.get(OrePrefixes.plate, 1), 'E',
                GTModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Salty_Root.get(1))
            .fluidInputs(Materials.Water.getFluid(100))
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
                    GTOreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Iridium, 2),
                    GGMaterial.lumiium.get(OrePrefixes.bolt, 48))
                .fluidInputs(Materials.Palladium.getMolten(8 * INGOTS))
                .itemOutputs(ItemRefer.Precise_Assembler.get(1))
                .duration(1 * MINUTES + 30 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T1.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedDiamondCrystalChip", 8),
                    ItemList.Circuit_Chip_NAND.get(16),
                    GTModHandler.getIC2Item("reactorVentCore", 1L, 1))
                .itemOutputs(ItemRefer.HiC_T2.get(1))
                .fluidInputs(
                    Materials.Polyethylene.getMolten(2 * INGOTS),
                    GGMaterial.signalium.getMolten(1 * INGOTS),
                    GGMaterial.lumiium.getMolten(1 * HALF_INGOTS),
                    Materials.Enderium.getMolten(1 * HALF_INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T1.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedDiamondCrystalChip", 8),
                    ItemList.Circuit_Chip_NAND.get(16),
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Aluminium, 2))
                .itemOutputs(ItemRefer.HiC_T2.get(1))
                .fluidInputs(
                    Materials.Polyethylene.getMolten(2 * INGOTS),
                    GGMaterial.signalium.getMolten(1 * INGOTS),
                    GGMaterial.lumiium.getMolten(1 * HALF_INGOTS),
                    Materials.Enderium.getMolten(1 * HALF_INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T2.get(2),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(8),
                    ItemList.Circuit_Chip_CrystalSoC2.get(1),
                    GTModHandler.getIC2Item("reactorVentDiamond", 1L, 1))
                .itemOutputs(ItemRefer.HiC_T3.get(1))
                .fluidInputs(
                    GGMaterial.adamantiumAlloy.getMolten(4 * INGOTS),
                    GGMaterial.signalium.getMolten(2 * INGOTS),
                    GGMaterial.lumiium.getMolten(1 * INGOTS),
                    Materials.TungstenCarbide.getMolten(1 * HALF_INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 2)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T2.get(2),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(8),
                    ItemList.Circuit_Chip_CrystalSoC2.get(1),
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 2))
                .itemOutputs(ItemRefer.HiC_T3.get(1))
                .fluidInputs(
                    GGMaterial.adamantiumAlloy.getMolten(4 * INGOTS),
                    GGMaterial.signalium.getMolten(2 * INGOTS),
                    GGMaterial.lumiium.getMolten(1 * INGOTS),
                    Materials.TungstenCarbide.getMolten(1 * HALF_INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 2)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T3.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedEnergyChip", 8),
                    ItemList.Circuit_Chip_QuantumCPU.get(16),
                    GTModHandler.getIC2Item("reactorVentGold", 1L, 1))
                .itemOutputs(ItemRefer.HiC_T4.get(1))
                .fluidInputs(
                    GGMaterial.marM200.getMolten(8 * INGOTS),
                    GGMaterial.signalium.getMolten(4 * INGOTS),
                    GGMaterial.lumiium.getMolten(2 * INGOTS),
                    GGMaterial.artheriumSn.getMolten(1 * INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T3.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedEnergyChip", 8),
                    ItemList.Circuit_Chip_QuantumCPU.get(16),
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.EnergeticAlloy, 2))
                .itemOutputs(ItemRefer.HiC_T4.get(1))
                .fluidInputs(
                    GGMaterial.marM200.getMolten(8 * INGOTS),
                    GGMaterial.signalium.getMolten(4 * INGOTS),
                    GGMaterial.lumiium.getMolten(2 * INGOTS),
                    GGMaterial.artheriumSn.getMolten(1 * INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T4.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedManyullynCrystalChip", 8),
                    ItemList.Circuit_Chip_BioCPU.get(1),
                    Ic2Items.reactorVentSpread)
                .itemOutputs(ItemRefer.HiC_T5.get(1))
                .fluidInputs(
                    GGMaterial.titaniumBetaC.getMolten(12 * INGOTS),
                    GGMaterial.signalium.getMolten(8 * INGOTS),
                    GGMaterial.lumiium.getMolten(4 * INGOTS),
                    GGMaterial.dalisenite.getMolten(2 * INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T4.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedManyullynCrystalChip", 8),
                    ItemList.Circuit_Chip_BioCPU.get(1),
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.TungstenCarbide, 2))
                .itemOutputs(ItemRefer.HiC_T5.get(1))
                .fluidInputs(
                    GGMaterial.titaniumBetaC.getMolten(12 * INGOTS),
                    GGMaterial.signalium.getMolten(8 * INGOTS),
                    GGMaterial.lumiium.getMolten(4 * INGOTS),
                    GGMaterial.dalisenite.getMolten(2 * INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
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
                .fluidInputs(GGMaterial.zircaloy4.getMolten(4 * INGOTS))
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
                .fluidInputs(Materials.BlackSteel.getMolten(4 * INGOTS))
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
                .fluidInputs(GGMaterial.adamantiumAlloy.getMolten(8 * INGOTS))
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
                .fluidInputs(GGMaterial.artheriumSn.getMolten(8 * INGOTS))
                .itemOutputs(ItemRefer.Precise_Electronic_Unit_T3.get(4))
                .duration(4 * MINUTES)
                .eut(TierEU.RECIPE_UV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Casing_UEV.get(3),
                    ItemList.Field_Generator_UV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 8),
                    ItemRefer.HiC_T4.get(1),
                    ItemRefer.Precise_Electronic_Unit_T3.get(1),
                    GGMaterial.enrichedNaquadahAlloy.get(OrePrefixes.bolt, 32),
                    GGMaterial.tairitsu.get(OrePrefixes.gearGtSmall, 8))
                .fluidInputs(GGMaterial.preciousMetalAlloy.getMolten(8 * INGOTS))
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
            .fluidInputs(GGMaterial.marM200.getMolten(8 * INGOTS), GGMaterial.zircaloy4.getMolten(2 * INGOTS))
            .duration(60 * SECONDS)
            .eut(9001)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
            .addTo(preciseAssemblerRecipes);
        // Compact MK2 Fusion Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Fusion_Coil.get(3),
                ItemRefer.Quartz_Crystal_Resonator.get(2),
                ItemRefer.HiC_T3.get(1))
            .itemOutputs(ItemRefer.Compact_Fusion_Coil_T1.get(1))
            .fluidInputs(GGMaterial.artheriumSn.getMolten(4 * INGOTS), GGMaterial.titaniumBetaC.getMolten(1 * INGOTS))
            .duration(40 * SECONDS)
            .eut(14000)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 2)
            .addTo(preciseAssemblerRecipes);
        // Compact MK3 Fusion Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Fusion_Coil.get(3),
                ItemRefer.Radiation_Protection_Plate.get(2),
                ItemList.QuantumStar.get(4),
                ItemRefer.HiC_T4.get(1))
            .itemOutputs(ItemRefer.Compact_Fusion_Coil_T2.get(1))
            .fluidInputs(GGMaterial.dalisenite.getMolten(4 * INGOTS), GGMaterial.hikarium.getMolten(1 * INGOTS))
            .duration(40 * SECONDS)
            .eut(114514)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
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
            .fluidInputs(GGMaterial.adamantiumAlloy.getMolten(1 * STACKS))
            .itemOutputs(ItemRefer.Compact_Fusion_MK1.get(1))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Compact Fusion Computer MK-II
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Compact_Fusion_MK1.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.FusionComputer_ZPMV.get(48),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                ItemList.Circuit_Wafer_UHPIC.get(32),
                ItemList.ZPM_Coil.get(16),
                ItemList.Neutron_Reflector.get(4),
                ItemRefer.HiC_T2.get(8),
                ItemList.Field_Generator_ZPM.get(8),
                GGMaterial.artheriumSn.get(OrePrefixes.gearGtSmall, 32))
            .fluidInputs(
                GGMaterial.marCeM200.getMolten(16 * INGOTS),
                WerkstoffLoader.HDCS.getMolten(8 * INGOTS),
                GGMaterial.artheriumSn.getMolten(2 * INGOTS))
            .itemOutputs(ItemRefer.Compact_Fusion_MK2.get(1))
            .eut(TierEU.RECIPE_ZPM / 2)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);

        // Compact Fusion Computer MK-III
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Compact_Fusion_MK2.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                ItemList.FusionComputer_UV.get(48),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                ItemList.Circuit_Wafer_NPIC.get(64),
                ItemList.UV_Coil.get(16),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(8),
                ItemRefer.HiC_T3.get(8),
                ItemList.Field_Generator_UV.get(8),
                WerkstoffLoader.HDCS.get(OrePrefixes.gearGtSmall, 64))
            .fluidInputs(
                GGMaterial.titaniumBetaC.getMolten(16 * INGOTS),
                GGMaterial.dalisenite.getMolten(8 * INGOTS),
                Materials.Americium.getMolten(2 * INGOTS))
            .itemOutputs(ItemRefer.Compact_Fusion_MK3.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);

        // Compact MK4 Fusion Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Fusion_Internal.get(3),
                ItemRefer.HiC_T5.get(1),
                GregtechItemList.Energy_Core_HV.get(1))
            .itemOutputs(ItemRefer.Compact_Fusion_Coil_T3.get(1))
            .fluidInputs(
                FluidRegistry.getFluidStack("molten.energycrystal", 8 * INGOTS),
                FluidRegistry.getFluidStack("molten.laurenium", 1 * INGOTS))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
            .addTo(preciseAssemblerRecipes);

        // Compact MK4 Fusion Disassembly Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.Compact_Fusion_Coil_T3.get(1))
            .itemOutputs(GregtechItemList.Casing_Fusion_Internal.get(3))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

        // Compact MK4 Computer
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRefer.Compact_Fusion_MK3.get(1),
            2_560_000,
            4_096,
            (int) TierEU.RECIPE_UHV,
            256,
            new Object[] { GregtechItemList.FusionComputer_UV2.get(48),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 }, ItemList.Circuit_Wafer_PPIC.get(64),
                ItemList.UHV_Coil.get(16), MaterialsAlloy.TITANSTEEL.getPlateDense(8), ItemRefer.HiC_T4.get(8),
                ItemList.Field_Generator_UHV.get(8),
                GGMaterial.enrichedNaquadahAlloy.get(OrePrefixes.gearGtSmall, 64) },
            new FluidStack[] { Materials.RadoxPolymer.getMolten(9 * INGOTS),
                GGMaterial.dalisenite.getMolten(8 * INGOTS), MaterialsAlloy.BOTMIUM.getFluidStack(288) },
            ItemRefer.Compact_Fusion_MK4.get(1),
            6000,
            (int) TierEU.RECIPE_UV);

        // Compact MK5 Fusion Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Fusion_Internal2.get(3),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 1),
                ItemRefer.HiC_T5.get(4),
                GregtechItemList.Energy_Core_IV.get(1))
            .itemOutputs(ItemRefer.Compact_Fusion_Coil_T4.get(1))
            .fluidInputs(
                MaterialsAlloy.BLACK_TITANIUM.getFluidStack(8 * INGOTS),
                GGMaterial.metastableOganesson.getMolten(4 * INGOTS))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
            .addTo(preciseAssemblerRecipes);

        // Compact MK5 Computer
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRefer.Compact_Fusion_MK4.get(1),
            10_240_000,
            16_384,
            (int) TierEU.RECIPE_UEV,
            256,
            new Object[] { GregtechItemList.FusionComputer_UV3.get(48),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 }, ItemList.Circuit_Wafer_QPIC.get(64),
                ItemList.UHV_Coil.get(64), MaterialsElements.STANDALONE.HYPOGEN.getPlateDense(8),
                ItemRefer.HiC_T5.get(8), ItemList.Field_Generator_UEV.get(8),
                GGMaterial.metastableOganesson.get(OrePrefixes.gearGtSmall, 64) },
            new FluidStack[] { GGMaterial.tairitsu.getMolten(16 * INGOTS),
                MaterialsAlloy.OCTIRON.getFluidStack(8 * INGOTS),
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
            .itemInputs(Materials.Antimony.getDust(8))
            .circuit(24)
            .fluidInputs(
                GGMaterial.ether.getFluidOrGas(1_000),
                Materials.Fluorine.getGas(40_000),
                Materials.Ice.getSolid(8_000))
            .fluidOutputs(GGMaterial.antimonyPentafluorideSolution.getFluidOrGas(8_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(multiblockChemicalReactorRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            GGMaterial.antimonyPentafluorideSolution.getFluidOrGas(4_000),
            new FluidStack[] { GGMaterial.antimonyPentafluoride.getFluidOrGas(4_000),
                GGMaterial.ether.getFluidOrGas(500) },
            GTValues.NI,
            5 * SECONDS,
            TierEU.RECIPE_MV);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Polyethylene, 2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1))
            .circuit(1)
            .fluidInputs(Materials.Concrete.getMolten(16 * INGOTS))
            .itemOutputs(ItemRefer.Coolant_Tower.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }

    public static void InitLoadRecipe() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Glowstone.getDust(4), Materials.Redstone.getDust(2), Materials.Aluminium.getDust(1))
            .circuit(3)
            .itemOutputs(ItemRefer.High_Energy_Mixture.get(4))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.High_Energy_Mixture.get(2))
            .fluidInputs(Materials.PhosphoricAcid.getFluid(4_000))
            .itemOutputs(GGMaterial.lumiinessence.get(OrePrefixes.dust, 1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.AnnealedCopper.getDust(4), Materials.Ardite.getDust(2), Materials.RedAlloy.getDust(2))
            .circuit(4)
            .fluidInputs(Materials.Redstone.getMolten(2 * INGOTS))
            .itemOutputs(GGMaterial.signalium.get(OrePrefixes.dust, 1))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.TinAlloy.getDust(4),
                Materials.SterlingSilver.getDust(2),
                GGMaterial.lumiinessence.get(OrePrefixes.dust, 2))
            .circuit(4)
            .fluidInputs(Materials.Glowstone.getMolten(2 * INGOTS))
            .itemOutputs(GGMaterial.lumiium.get(OrePrefixes.dust, 1))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                GGMaterial.enrichedNaquadahAlloy.getMolten(1 * INGOTS),
                WerkstoffLoader.Oganesson.getFluidOrGas(250))
            .fluidOutputs(GGMaterial.metastableOganesson.getMolten(1 * QUARTER_INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        // Mk5 recipe
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Copper.getPlasma(4 * INGOTS), WerkstoffLoader.Oganesson.getFluidOrGas(1_000))
            .fluidOutputs(GGMaterial.metastableOganesson.getMolten(4 * INGOTS))
            .eut(TierEU.RECIPE_UEV)
            .duration(5 * SECONDS)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.metastableOganesson.get(OrePrefixes.dust))
            .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(250))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1000))
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.HiC_T5.get(0))
            .fluidInputs(
                GGMaterial.metastableOganesson.getMolten(8 * INGOTS),
                GGMaterial.preciousMetalAlloy.getMolten(16 * INGOTS),
                Materials.SpaceTime.getMolten(2 * INGOTS),
                Materials.DTR.getFluid(5_000))
            .fluidOutputs(GGMaterial.shirabon.getMolten(1 * INGOTS))
            .duration(10 * SECONDS)
            .eut(1_500_000_000)
            .metadata(COIL_HEAT, 13500)
            .addTo(plasmaForgeRecipes);
    }

    public static float EHEEfficiencyMultiplier = 0.9f;

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
                    long criticalSteamAmount = (long) (tUnit * EHEEfficiencyMultiplier * tPlasma.amount / 1000);
                    MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
                        tPlasma,
                        output,
                        GTModHandler.getDistilledWater(waterAmount),
                        FluidRegistry.getFluidStack("ic2superheatedsteam", 0), // Plasma always outputs SC steam.
                        Materials.DenseSupercriticalSteam.getGas(criticalSteamAmount),
                        1);
                }
            }
        }
    }
}
