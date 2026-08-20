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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
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
                MaterialLibAPI.getStack(Materials.Beryllium, Shapes.plate, 32),
                Circuits.IV.get(1))
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

        CrackRecipeAdder.reAddBlastRecipe(Materials.ExtremelyUnstableNaquadah, 8000, 122880, 7000, false);
        CrackRecipeAdder.reAddBlastRecipe(Materials.Zircaloy2, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.Zircaloy4, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.Incoloy903, 1200, 1920, 3700, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.AdamantiumAlloy, 2500, 1920, 5500, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.MARM200Steel, 200, 7680, 5000, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.Signalium, 1600, 30720, 4000, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.Lumiium, 1600, 30720, 4000, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.ArtheriumSn, 500, 122880, 6500, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.TanmolyiumBetaC, 400, 7680, 5300, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.Dalisenite, 800, 491520, 8700, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.Hikarium, 1200, 30720, 5400, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.Tairitsu, 1200, 1966080, 7400, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.PreciousMetalsAlloy, 2400, 7864320, 10000, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.EnrichedNaquadahAlloy, 2400, 7864320, 11000, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.MetastableOganesson, 600, 7864320, 12000, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.Shirabon, 600, 31457280, 13000, true);
        CrackRecipeAdder.reAddBlastRecipe(Materials.AtomicSeparationCatalyst, 35000, 120, 5000, false);

        GTModHandler.removeFurnaceSmelting(MaterialLibAPI.getStack(Materials.Dalisenite, Shapes.dust, 1)); // :doom:

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Dalisenite, Shapes.ingotHot, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Dalisenite, Shapes.ingot, 1))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Shirabon, Shapes.ingotHot, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Shirabon, Shapes.ingot, 1))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_UHV)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Zircaloy4, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Zircaloy2, Shapes.ring, 2))
            .circuit(2)
            .itemOutputs(ItemRefer.Advanced_Fuel_Rod.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.YOTTank_Casing.get(1),
            GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "BPB", "FOF", "BPB", 'B', MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.plate, 1),
                'P', GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 1), 'F',
                MaterialLibAPI.getStack(Materials.Polytetrafluoroethylene, Shapes.plate, 1), 'O',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1), });

        GTModHandler.addCraftingRecipe(
            ItemRefer.YOTTank.get(1),
            GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SPS", "ECE", "SLS", 'S', MaterialLibAPI.getStack(Materials.BlueSteel, Shapes.screw, 1), 'P',
                ItemList.Cover_Screen.get(1), 'E', "circuitData", 'L',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 1), 'C',
                ItemRefer.YOTTank_Casing.get(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Output_IV.get(1),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiPart", 1, 440),
                MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.screw, 8))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 1 * INGOTS))
            .itemOutputs(Loaders.YFH)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Steel.get(12L),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.plateQuadruple, 4))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 1 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T1.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Aluminium.get(3L),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.plateQuadruple, 4))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 1 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T1.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_StainlessSteel.get(2L),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.plateQuadruple, 4))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 1 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T1.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Titanium.get(64L),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.plateDense, 8),
                MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.plateQuadruple, 4))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Polytetrafluoroethylene, FluidShapes.fluidMolten, 16 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(18L),
                MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.plateQuadruple, 4))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Polytetrafluoroethylene, FluidShapes.fluidMolten, 16 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_Chrome.get(4L),
                MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.plateQuadruple, 4))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Polytetrafluoroethylene, FluidShapes.fluidMolten, 16 * INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.Fluid_Storage_Core_T1.get(32),
                ItemRefer.Fluid_Storage_Core_T1.get(32),
                MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.plateDouble, 16))
            .circuit(10)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Polytetrafluoroethylene, FluidShapes.fluidMolten, 16 * INGOTS))
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
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.plateDense, 8),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 1L, 6),
                MaterialLibAPI.getStack(Materials.Polycaprolactam, Shapes.foil, 32))
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.Indalloy140, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 4_000))
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
                MaterialLibAPI.getStack(Materials.Europium, Shapes.plateDense, 8),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 4L, 6),
                MaterialLibAPI.getStack(Materials.StyreneButadieneRubber, Shapes.foil, 64),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 64))
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.Indalloy140, 2 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 16_000))
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
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.plateDense, 8),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 16L, 6),
                MaterialLibAPI.getStack(Materials.Polycaprolactam, Shapes.plateQuintuple, 24),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Titanium, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Draconium, FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, 2 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 64_000))
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
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plateDense, 16),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.plateDense, 16),
                GTModHandler.getModItem(GalacticraftMars.ID, "item.null", 64L, 6),
                ItemList.Machine_IV_Compressor.get(64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Draconium, FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, 2 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 64_000))
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
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.plateQuintuple, 8),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.plateDense, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Draconium, FluidShapes.fluidMolten, 2 * STACKS + 32 * INGOTS),
                MaterialUtils.anyFluid(Materials.Indalloy140, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.InfinityCatalyst, FluidShapes.fluidMolten, 1_140))
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
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plateQuintuple, 24),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.plateDense, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Draconium, FluidShapes.fluidMolten, 4 * STACKS),
                MaterialUtils.anyFluid(Materials.Indalloy140, 3 * STACKS + 18 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials.InfinityCatalyst,
                    FluidShapes.fluidMolten,
                    39 * INGOTS + 3 * EIGHTH_INGOTS))
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
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plateQuintuple, 36),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.plateDense, 8))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Draconium, FluidShapes.fluidMolten, 4 * STACKS),
                MaterialUtils.anyFluid(Materials.Indalloy140, 3 * STACKS + 18 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.TranscendentMetal, FluidShapes.fluidMolten, 10 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials.InfinityCatalyst,
                    FluidShapes.fluidMolten,
                    39 * INGOTS + 3 * EIGHTH_INGOTS))
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
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plateQuintuple, 24),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.plateDouble, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Draconium, FluidShapes.fluidMolten, 4 * STACKS),
                MaterialUtils.anyFluid(Materials.Indalloy140, 5 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.TranscendentMetal, FluidShapes.fluidMolten, 30 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials.InfinityCatalyst,
                    FluidShapes.fluidMolten,
                    1 * STACKS + 54 * INGOTS + 1 * EIGHTH_INGOTS))
            .itemOutputs(ItemRefer.Fluid_Storage_Core_T10.get(1))
            .eut(TierEU.RECIPE_UXV)
            .duration(20 * SECONDS)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1),
                ItemRefer.Fluid_Storage_Core_T1.get(10),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.plate, 4),
                ItemList.Electric_Pump_HV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 4))
            .circuit(5)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 1 * INGOTS))
            .itemOutputs(ItemRefer.YOTTank_Cell_T1.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
                ItemRefer.Fluid_Storage_Core_T2.get(10),
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Shapes.plate, 4),
                ItemList.Electric_Pump_EV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 4))
            .circuit(5)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Polytetrafluoroethylene, FluidShapes.fluidMolten, 1 * INGOTS))
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
                new Object[] { Circuits.LuV.getIngredient(), 8 },
                ItemList.Electric_Pump_IV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NiobiumTitanium, 8),
                MaterialLibAPI.getStack(Materials.AdamantiumAlloy, Shapes.plate, 32))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Quantium, FluidShapes.fluidMolten, 10 * INGOTS),
                GTModHandler.getIC2Coolant(8_000),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 8_000))
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
                new Object[] { Circuits.ZPM.getIngredient(), 8 },
                ItemList.Electric_Pump_LuV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.MysteriousCrystal, 8),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.plate, 32))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Draconium, FluidShapes.fluidMolten, 10 * INGOTS),
                GTModHandler.getIC2Coolant(16_000),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 16_000))
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
                new Object[] { Circuits.UV.getIngredient(), 8 },
                ItemList.Electric_Pump_ZPM.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.DraconiumAwakened, 8),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.plate, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Draconium, FluidShapes.fluidMolten, 10 * INGOTS),
                GTModHandler.getIC2Coolant(16_000),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 16_000))
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
                new Object[] { Circuits.UHV.getIngredient(), 8L },
                ItemList.Electric_Pump_UV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 8),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidMolten, 10 * INGOTS),
                GTModHandler.getIC2Coolant(5 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 32_000))
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
                new Object[] { Circuits.UEV.getIngredient(), 8L },
                ItemList.Electric_Pump_UHV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 16),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plate, 32),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, 32))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidMolten, 1 * STACKS + 36 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 5 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 5 * STACKS))
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
                new Object[] { Circuits.UIV.getIngredient(), 8L },
                ItemList.Electric_Pump_UEV.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 12),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plateDouble, 12),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.plateDouble, 12),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, 64))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidMolten, 1 * STACKS + 36 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 5 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 5 * STACKS))
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
                    new Object[] { Circuits.UMV.getIngredient(), 8L },
                    ItemList.Electric_Pump_UIV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 12),
                    MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plateDouble, 12),
                    MaterialLibAPI.getStack(Materials.protohalkonite, Shapes.plateDouble, 12),
                    MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, 64))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidMolten, 1 * STACKS + 36 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 5 * STACKS),
                    MaterialLibAPI
                        .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 5 * STACKS))
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
                    new Object[] { Circuits.UXV.getIngredient(), 12L },
                    ItemList.Electric_Pump_UMV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 64),
                    MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plateDouble, 12),
                    MaterialLibAPI.getStack(Materials.protohalkonite, Shapes.plateDouble, 12),
                    MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.plate, 10))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidMolten, 10 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.TranscendentMetal, FluidShapes.fluidMolten, 10 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 5 * STACKS),
                    MaterialLibAPI
                        .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 5 * STACKS))
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
            .itemInputs(MaterialLibAPI.getStack(Materials.Tiberium, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.gem, 1),
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.gem, 1))
            .outputChances(10000, 2000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NaquadahGas, FluidShapes.fluidLiquid, 250))
            .duration(400)
            .eut(TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Firestone, Shapes.gem, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.LightNaquadahFuel, FluidShapes.fluidLiquid, INGOTS))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Tiberium, Shapes.gem, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HeavyNaquadahFuel, FluidShapes.fluidLiquid, INGOTS))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Tiberium, Shapes.gem, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 400),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 250))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
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
                .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
                .outputChances(10)
                .fluidOutputs(FluidRegistry.getFluidStack("fluid.coaltar", 250))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(fluidExtractionRecipes);
        }

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(FluidRegistry.getFluidStack("fluid.coaltaroil", 20))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Cyclopentadiene, FluidShapes.fluidLiquid, 6))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 100))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Cyclopentadiene, FluidShapes.fluidLiquid, 4))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);

        // FeCl2 + Cl = FeCl3
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.IronIIChloride, CellShapes.cell, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.IronIIIChloride, CellShapes.cell, 1))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // FeCl3 + H = FeCl2 + HCl
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.IronIIIChloride, CellShapes.cell, 1))
            .circuit(7)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.IronIIChloride, CellShapes.cell, 1))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // NH3 + 2C2H6O = C4H11N + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ammonia, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Diethylamine, CellShapes.cell, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Cyclopentadiene, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.IronIIChloride, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Diethylamine, FluidShapes.fluidLiquid, 8_000),
                GTUtility.getIceSolid(4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.ImpureFerroceneMixture, FluidShapes.fluidLiquid, 15_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ether, CellShapes.cell, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ImpureFerroceneMixture, FluidShapes.fluidLiquid, 7_500))
            .itemOutputs(MaterialLibAPI.getStack(Materials.FerroceneSolution, CellShapes.cell, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.FerroceneWaste, FluidShapes.fluidLiquid, 5_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.FerroceneWaste, FluidShapes.fluidLiquid, 1_000),
            new FluidStack[] { GTUtility.getWater(400),
                MaterialLibAPI.getFluidStack(Materials.Diethylamine, FluidShapes.fluidLiquid, 800),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 200) },
            GTValues.NI,
            30 * SECONDS,
            TierEU.RECIPE_MV);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.FerroceneSolution, FluidShapes.fluidLiquid, 2_000),
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Ether, FluidShapes.fluidLiquid, 2_000) },
            MaterialLibAPI.getStack(Materials.Ferrocene, Shapes.dust, 1),
            30 * SECONDS,
            TierEU.RECIPE_MV);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Ferrocene, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 8))
            .fluidInputs(
                FluidRegistry.getFluidStack("fluid.kerosene", 40_000),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 3_000),
                MaterialLibAPI.getFluidStack(Materials.Diethylamine, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.JetFuelNo3, FluidShapes.fluidLiquid, 44_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ferrocene, Shapes.dust, 4))
            .fluidInputs(
                FluidRegistry.getFluidStack("combustionpromotor", 4_000),
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 40_000),
                MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 3_000),
                MaterialLibAPI.getFluidStack(Materials.LPG, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Tetranitromethane, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.JetFuelA, FluidShapes.fluidLiquid, 50_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(multiblockChemicalReactorRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Combustion_Generator_EV.get(1),
            GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PCP", "MHM", "GWG", 'G', MaterialLibAPI.getStack(Materials.Titanium, Shapes.gearGt, 1), 'C',
                "circuitData", 'W', GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 1), 'P',
                ItemList.Electric_Piston_EV, 'H', ItemList.Hull_EV, 'M', ItemList.Electric_Motor_EV });

        GTModHandler.addCraftingRecipe(
            ItemRefer.Combustion_Generator_IV.get(1),
            GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PCP", "MHM", "GWG", 'G', MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.gearGt, 1),
                'C', "circuitElite", 'W', GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 1), 'P',
                ItemList.Electric_Piston_IV, 'H', ItemList.Hull_IV, 'M', ItemList.Electric_Motor_IV });

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
            GTUtility.getLava(160_000),
            FluidRegistry.getFluidStack("ic2pahoehoelava", 160_000),
            GTModHandler.getDistilledWater(80_000),
            FluidRegistry.getFluidStack("ic2superheatedsteam", 12_800_000),
            FluidRegistry.getFluidStack("supercriticalsteam", 12_800_000),
            80000);

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
            FluidRegistry.getFluidStack("ic2hotcoolant", 128_000),
            GTModHandler.getIC2Coolant(128_000),
            GTModHandler.getDistilledWater(160_000),
            FluidRegistry.getFluidStack("ic2superheatedsteam", 25_600_000),
            FluidRegistry.getFluidStack("supercriticalsteam", 25_600_000),
            8000);

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
            FluidRegistry.getFluidStack("molten.solarsalthot", 3_200),
            FluidRegistry.getFluidStack("molten.solarsaltcold", 3_200),
            GTModHandler.getDistilledWater(20_000),
            FluidRegistry.getFluidStack("ic2superheatedsteam", 3_200_000),
            FluidRegistry.getFluidStack("supercriticalsteam", 3_200_000),
            1600);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Lepidolite, Shapes.crushedPurified, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RockSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.LithiumChloride, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Cryolite, Shapes.dust, 4))
            .outputChances(8000, 8000, 8000)
            .duration(7 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalBathRecipes);

        // dust to fluid extraction, which isn't autogenned in Bartworks
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LithiumChloride, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LithiumChloride, FluidShapes.fluidMolten, INGOTS))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.MARM200Steel, Shapes.ingot, 18),
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.ingot, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.LithiumChloride, FluidShapes.fluidMolten, INGOTS))
            .itemOutputs(MaterialLibAPI.getStack(Materials.MARCeM200Steel, Shapes.ingotHot, 19))
            .duration(4 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 5400)
            .addTo(blastFurnaceRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.SC_Turbine_Casing.get(1),
            GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PhP", "GCG", "PwP", 'G', MaterialLibAPI.getStack(Materials.MARM200Steel, Shapes.gearGt, 1),
                'C', ItemList.Casing_Turbine.get(1), 'P',
                MaterialLibAPI.getStack(Materials.MARCeM200Steel, Shapes.plate, 1), });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.MARM200Steel, Shapes.gearGt, 2),
                MaterialLibAPI.getStack(Materials.MARCeM200Steel, Shapes.plate, 4),
                ItemList.Casing_Turbine.get(1))
            .itemOutputs(ItemRefer.SC_Turbine_Casing.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.SCSteamTurbine.get(1),
            GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "NPN", "GHG", "IPI", 'N', "circuitMaster", 'P',
                MaterialLibAPI.getStack(Materials.MARM200Steel, Shapes.plate, 1), 'H', ItemList.Hull_IV.get(1), 'G',
                MaterialLibAPI.getStack(Materials.MARCeM200Steel, Shapes.gearGt, 1), 'I',
                MaterialLibAPI.getStack(Materials.Incoloy903, TEBlockShapes.pipeLarge, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.MARM200Steel, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.MARCeM200Steel, Shapes.gearGt, 2),
                MaterialLibAPI.getStack(Materials.Incoloy903, TEBlockShapes.pipeLarge, 2),
                Circuits.LuV.get(2),
                ItemList.Hull_IV.get(1))
            .itemOutputs(ItemList.SCSteamTurbine.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Incoloy903, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.MARCeM200Steel, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NiobiumTitanium, 1))
            .circuit(8)
            .itemOutputs(ItemRefer.Pressure_Resistant_Wall.get(1))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemRefer.Extreme_Heat_Exchanger.get(1),
            GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "EPE", "PHP", "SPS", 'P',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1), 'H',
                ItemList.Hull_IV.get(1), 'S', MaterialLibAPI.getStack(Materials.MARCeM200Steel, Shapes.plate, 1), 'E',
                GTModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 1) });

        if (NewHorizonsCoreMod.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Robot_Arm_IV.get(4),
                    ItemRefer.HiC_T1.get(4),
                    ItemList.Tool_DataOrb.get(3),
                    GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.Titanium, 4),
                    MaterialLibAPI.getStack(Materials.Hikarium, Shapes.gearGt, 4),
                    MaterialLibAPI.getStack(Materials.MARM200Steel, Shapes.plateDouble, 2),
                    GTOreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Iridium, 2),
                    MaterialLibAPI.getStack(Materials.Lumiium, Shapes.bolt, 48))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Palladium, FluidShapes.fluidMolten, 8 * INGOTS))
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
                    MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 2 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Signalium, FluidShapes.fluidMolten, INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lumiium, FluidShapes.fluidMolten, HALF_INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Enderium, FluidShapes.fluidMolten, 1 * HALF_INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 1)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T1.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedDiamondCrystalChip", 8),
                    ItemList.Circuit_Chip_NAND.get(16),
                    MaterialLibAPI.getStack(Materials.Aluminium, Shapes.rotor, 2))
                .itemOutputs(ItemRefer.HiC_T2.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 2 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Signalium, FluidShapes.fluidMolten, INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lumiium, FluidShapes.fluidMolten, HALF_INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Enderium, FluidShapes.fluidMolten, 1 * HALF_INGOTS))
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
                    MaterialLibAPI.getFluidStack(Materials.AdamantiumAlloy, FluidShapes.fluidMolten, 4 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Signalium, FluidShapes.fluidMolten, 2 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lumiium, FluidShapes.fluidMolten, INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.TungstenCarbide, FluidShapes.fluidMolten, 1 * HALF_INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 2)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T2.get(2),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(8),
                    ItemList.Circuit_Chip_CrystalSoC2.get(1),
                    MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.rotor, 2))
                .itemOutputs(ItemRefer.HiC_T3.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.AdamantiumAlloy, FluidShapes.fluidMolten, 4 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Signalium, FluidShapes.fluidMolten, 2 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lumiium, FluidShapes.fluidMolten, INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.TungstenCarbide, FluidShapes.fluidMolten, 1 * HALF_INGOTS))
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
                    MaterialLibAPI.getFluidStack(Materials.MARM200Steel, FluidShapes.fluidMolten, 8 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Signalium, FluidShapes.fluidMolten, 4 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lumiium, FluidShapes.fluidMolten, 2 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.ArtheriumSn, FluidShapes.fluidMolten, INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T3.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedEnergyChip", 8),
                    ItemList.Circuit_Chip_QuantumCPU.get(16),
                    MaterialLibAPI.getStack(Materials.EnergeticAlloy, Shapes.rotor, 2))
                .itemOutputs(ItemRefer.HiC_T4.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.MARM200Steel, FluidShapes.fluidMolten, 8 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Signalium, FluidShapes.fluidMolten, 4 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lumiium, FluidShapes.fluidMolten, 2 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.ArtheriumSn, FluidShapes.fluidMolten, INGOTS))
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
                    MaterialLibAPI.getFluidStack(Materials.TanmolyiumBetaC, FluidShapes.fluidMolten, 12 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Signalium, FluidShapes.fluidMolten, 8 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lumiium, FluidShapes.fluidMolten, 4 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Dalisenite, FluidShapes.fluidMolten, 2 * INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
                .addTo(preciseAssemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemRefer.HiC_T4.get(2),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EngravedManyullynCrystalChip", 8),
                    ItemList.Circuit_Chip_BioCPU.get(1),
                    MaterialLibAPI.getStack(Materials.TungstenCarbide, Shapes.rotor, 2))
                .itemOutputs(ItemRefer.HiC_T5.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.TanmolyiumBetaC, FluidShapes.fluidMolten, 12 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Signalium, FluidShapes.fluidMolten, 8 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lumiium, FluidShapes.fluidMolten, 4 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Dalisenite, FluidShapes.fluidMolten, 2 * INGOTS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
                .addTo(preciseAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Casing_LuV.get(3),
                    ItemList.Robot_Arm_EV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Osmiridium, 2),
                    MaterialLibAPI.getStack(Materials.MARM200Steel, Shapes.plateDouble, 2),
                    Circuits.EV.get(1),

                    MaterialLibAPI.getStack(Materials.Ruthenium, Shapes.bolt, 32),
                    MaterialLibAPI.getStack(Materials.Platinum, Shapes.gearGtSmall, 8))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Zircaloy4, FluidShapes.fluidMolten, 4 * INGOTS))
                .itemOutputs(ItemRefer.Imprecise_Electronic_Unit.get(2))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Casing_ZPM.get(3),
                    ItemList.Robot_Arm_IV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Lumiium, 2),
                    MaterialLibAPI.getStack(Materials.MARCeM200Steel, Shapes.plateDouble, 2),
                    ItemRefer.HiC_T1.get(1),
                    MaterialLibAPI.getStack(Materials.Signalium, Shapes.bolt, 32),
                    MaterialLibAPI.getStack(Materials.TanmolyiumBetaC, Shapes.gearGtSmall, 8))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.BlackSteel, FluidShapes.fluidMolten, 4 * INGOTS))
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
                    MaterialLibAPI.getStack(Materials.MARCeM200Steel, Shapes.bolt, 32),
                    MaterialLibAPI.getStack(Materials.ArtheriumSn, Shapes.gearGtSmall, 8))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.AdamantiumAlloy, FluidShapes.fluidMolten, 8 * INGOTS))
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
                    MaterialLibAPI.getStack(Materials.TanmolyiumBetaC, Shapes.bolt, 32),
                    MaterialLibAPI.getStack(Materials.Dalisenite, Shapes.gearGtSmall, 8))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ArtheriumSn, FluidShapes.fluidMolten, 8 * INGOTS))
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
                    MaterialLibAPI.getStack(Materials.EnrichedNaquadahAlloy, Shapes.bolt, 32),
                    MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.gearGtSmall, 8))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.PreciousMetalsAlloy, FluidShapes.fluidMolten, 8 * INGOTS))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.MARM200Steel, FluidShapes.fluidMolten, 8 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Zircaloy4, FluidShapes.fluidMolten, 2 * INGOTS))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.ArtheriumSn, FluidShapes.fluidMolten, 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.TanmolyiumBetaC, FluidShapes.fluidMolten, INGOTS))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Dalisenite, FluidShapes.fluidMolten, 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Hikarium, FluidShapes.fluidMolten, INGOTS))
            .duration(40 * SECONDS)
            .eut(114514)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
            .addTo(preciseAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.FusionComputer_LuV.get(48),
                ItemRefer.HiC_T1.get(8),
                MaterialLibAPI.getStack(Materials.MARCeM200Steel, Shapes.plate, 32),
                Circuits.LuV.get(8),
                ItemList.Circuit_Wafer_HPIC.get(16),
                ItemList.Field_Generator_LuV.get(4),
                MaterialLibAPI.getStack(Materials.MARM200Steel, Shapes.stickLong, 8))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AdamantiumAlloy, FluidShapes.fluidMolten, STACKS))
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
                new Object[] { Circuits.ZPM.getIngredient(), 4 },
                ItemList.Circuit_Wafer_UHPIC.get(32),
                ItemList.ZPM_Coil.get(16),
                ItemList.Neutron_Reflector.get(4),
                ItemRefer.HiC_T2.get(8),
                ItemList.Field_Generator_ZPM.get(8),
                MaterialLibAPI.getStack(Materials.ArtheriumSn, Shapes.gearGtSmall, 32))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.MARCeM200Steel, FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.HighDurabilityCompoundSteel, FluidShapes.fluidMolten, 8 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.ArtheriumSn, FluidShapes.fluidMolten, 2 * INGOTS))
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
                new Object[] { Circuits.UV.getIngredient(), 4 },
                ItemList.Circuit_Wafer_NPIC.get(64),
                ItemList.UV_Coil.get(16),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(8),
                ItemRefer.HiC_T3.get(8),
                ItemList.Field_Generator_UV.get(8),
                MaterialLibAPI.getStack(Materials.HighDurabilityCompoundSteel, Shapes.gearGtSmall, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.TanmolyiumBetaC, FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Dalisenite, FluidShapes.fluidMolten, 8 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidMolten, 2 * INGOTS))
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
                new Object[] { Circuits.UHV.getIngredient(), 4 }, ItemList.Circuit_Wafer_PPIC.get(64),
                ItemList.UHV_Coil.get(16), MaterialLibAPI.getStack(Materials.Titansteel, Shapes.plateDense, 8),
                ItemRefer.HiC_T4.get(8), ItemList.Field_Generator_UHV.get(8),
                MaterialLibAPI.getStack(Materials.EnrichedNaquadahAlloy, Shapes.gearGtSmall, 64) },
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.RadoxPoly, FluidShapes.fluidMolten, 9 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Dalisenite, FluidShapes.fluidMolten, 8 * INGOTS),
                MaterialUtils.anyFluid(Materials.Botmium, 288) },
            ItemRefer.Compact_Fusion_MK4.get(1),
            6000,
            (int) TierEU.RECIPE_UV);

        // Compact MK5 Fusion Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Fusion_Internal2.get(3),
                Circuits.UEV.get(1),
                ItemRefer.HiC_T5.get(4),
                GregtechItemList.Energy_Core_IV.get(1))
            .itemOutputs(ItemRefer.Compact_Fusion_Coil_T4.get(1))
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.BlackTitanium, 8 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, 4 * INGOTS))
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
                new Object[] { Circuits.UEV.getIngredient(), 4 }, ItemList.Circuit_Wafer_QPIC.get(64),
                ItemList.UEV_Coil.get(16), MaterialLibAPI.getStack(Materials.Hypogen, Shapes.plateDense, 8),
                ItemRefer.HiC_T5.get(8), ItemList.Field_Generator_UEV.get(8),
                MaterialLibAPI.getStack(Materials.MetastableOganesson, Shapes.gearGtSmall, 64) },
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Tairitsu, FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialUtils.anyFluid(Materials.Octiron, 8 * INGOTS), MaterialUtils.anyFluid(Materials.Rhugnor, 288) },
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
            .itemInputs(MaterialLibAPI.getStack(Materials.Antimony, Shapes.dust, 8))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ether, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 40_000),
                GTUtility.getIceSolid(8_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.AntimonyPentafluorideSolution, FluidShapes.fluidLiquid, 8_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(multiblockChemicalReactorRecipes);

        CrackRecipeAdder.addUniversalDistillationRecipe(
            MaterialLibAPI.getFluidStack(Materials.AntimonyPentafluorideSolution, FluidShapes.fluidLiquid, 4_000),
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.AntimonyPentafluoride, FluidShapes.fluidLiquid, 4_000),
                MaterialLibAPI.getFluidStack(Materials.Ether, FluidShapes.fluidLiquid, 500) },
            GTValues.NI,
            5 * SECONDS,
            TierEU.RECIPE_MV);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Plastic, 2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Concrete, FluidShapes.fluidMolten, 16 * INGOTS))
            .itemOutputs(ItemRefer.Coolant_Tower.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }

    public static void InitLoadRecipe() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Glowstone, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 1))
            .circuit(3)
            .itemOutputs(ItemRefer.High_Energy_Mixture.get(4))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.High_Energy_Mixture.get(2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 4_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Lumiinessence, Shapes.dust, 1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AnnealedCopper, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Ardite, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.RedAlloy, Shapes.dust, 2))
            .circuit(4)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Redstone, FluidShapes.fluidMolten, 2 * INGOTS))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Signalium, Shapes.dust, 1))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.TinAlloy, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.SterlingSilver, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Lumiinessence, Shapes.dust, 2))
            .circuit(4)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glowstone, FluidShapes.fluidMolten, 2 * INGOTS))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Lumiium, Shapes.dust, 1))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.EnrichedNaquadahAlloy, FluidShapes.fluidMolten, INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, 250))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, QUARTER_INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        // Mk5 recipe
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Copper, FluidShapes.fluidPlasma, 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, 4 * INGOTS))
            .eut(TierEU.RECIPE_UEV)
            .duration(5 * SECONDS)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.MetastableOganesson, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oganesson, FluidShapes.fluidLiquid, 250))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(NKE_RANGE, computeRangeNKE(1100, 1000))
            .addTo(neutronActivatorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemRefer.HiC_T5.get(0))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, 8 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.PreciousMetalsAlloy, FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, 2 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 5_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Shirabon, FluidShapes.fluidMolten, INGOTS))
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
                        MaterialLibAPI.getFluidStack(
                            Materials.DenseSupercriticalSteam,
                            FluidShapes.fluidGas,
                            (int) criticalSteamAmount),
                        1);
                }
            }
        }
    }
}
