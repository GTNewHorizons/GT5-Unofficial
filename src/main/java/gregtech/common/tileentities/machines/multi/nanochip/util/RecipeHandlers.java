package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static tectech.thing.CustomItemList.DATApipe;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.NanochipAssemblyRecipeInfo;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent.CircuitComponentStack;
import gtPlusPlus.core.material.MaterialMisc;

public class RecipeHandlers {

    private static void addConversionRecipe(CircuitComponent component, ItemStack stack) {
        GTValues.RA.stdBuilder()
            .itemInputs(stack)
            .itemOutputs(component.getFakeStack(1))
            .duration(1 * TICKS)
            .eut(0)
            .addTo(RecipeMaps.nanochipConversionRecipes);
    }

    // Adds a simple processing recipe for circuit components in a module. The recipe map used for processing is
    // inferred from the map stored by the input component.
    private static void addSimpleProcessingRecipe(CircuitComponent input, CircuitComponent output,
        ModuleRecipeInfo info, long eut) {
        RecipeMap<?> recipeMap = input.processingMap;
        if (recipeMap == null) {
            throw new IllegalArgumentException(
                "Tried to add component processing recipe for a component without an associated recipemap");
        }
        GTValues.RA.stdBuilder()
            .metadata(NanochipAssemblyRecipeInfo.INSTANCE, info)
            .itemInputs(input.getFakeStack(info.getBaseParallel()))
            .itemOutputs(output.getFakeStack(info.getBaseParallel()))
            .duration(ModuleRecipeInfo.MODULE_RECIPE_TIME)
            .eut(eut)
            .addTo(recipeMap);
    }

    // Adds a simple processing recipe with a fluid for circuit components in a module. The recipe map
    // used for processing is inferred from the map stored by the input component.
    private static void addSimpleProcessingRecipe(CircuitComponent input, FluidStack inputStack,
        CircuitComponent output, ModuleRecipeInfo info, long eut) {
        RecipeMap<?> recipeMap = input.processingMap;
        if (recipeMap == null) {
            throw new IllegalArgumentException(
                "Tried to add component processing recipe for a component without an associated recipemap");
        }
        GTValues.RA.stdBuilder()
            .metadata(NanochipAssemblyRecipeInfo.INSTANCE, info)
            .itemInputs(input.getFakeStack(info.getBaseParallel()))
            .fluidInputs(inputStack)
            .itemOutputs(output.getFakeStack(info.getBaseParallel()))
            .duration(ModuleRecipeInfo.MODULE_RECIPE_TIME)
            .eut(eut)
            .addTo(recipeMap);
    }

    private static void addAssemblyMatrixRecipe(List<CircuitComponentStack> input, List<FluidStack> fluidInputs,
        CircuitComponent output, ModuleRecipeInfo info, long eut) {
        if (!output.processingMap.equals(RecipeMaps.nanochipAssemblyMatrixRecipes)) {
            throw new IllegalArgumentException("Invalid RecipeMap passed to addAssemblyMatrixRecipe!");
        } else if (output.realCircuit == null) {
            throw new IllegalArgumentException("No real circuit was defined for given output!");
        }
        ItemStack realOutput = output.realCircuit.copy();
        realOutput.stackSize = info.getBaseParallel();
        ItemStack[] inputsWithRealCircuits = input.stream()
            .map(c -> {
                if (c.getCircuitComponent().realCircuit != null) {
                    ItemStack realCircuit = c.getCircuitComponent().realCircuit.copy();
                    realCircuit.stackSize = info.getBaseParallel() * c.getSize();
                    return realCircuit;
                }
                return c.getCircuitComponent()
                    .getFakeStack(info.getBaseParallel() * c.getSize());
            })
            .toArray(ItemStack[]::new);
        ItemStack[] inputsWithFakeCircuits = input.stream()
            .map(
                c -> c.getCircuitComponent()
                    .getFakeStack(info.getBaseParallel() * c.getSize()))
            .toArray(ItemStack[]::new);
        GTRecipeBuilder builder = GTValues.RA.stdBuilder()
            .metadata(NanochipAssemblyRecipeInfo.INSTANCE, info)
            .fluidInputs(fluidInputs.toArray(new FluidStack[]{}))
            .itemOutputs(output.getFakeStack(info.getBaseParallel()))
            .duration(ModuleRecipeInfo.MODULE_RECIPE_TIME)
            .eut(eut);
        // Add real recipe that will actually be utilized in recipe checks
        builder.copy()
            .hidden()
            .itemInputs(inputsWithFakeCircuits)
            .addTo(output.processingMap);
        // Add fake recipe that the user can see in NEI but will never actually be used for recipe checks
        builder.copy()
            .fake()
            .itemInputs(inputsWithRealCircuits)
            .itemOutputs(realOutput)
            .addTo(output.processingMap);
    }

    public static void populateCircuitComponentRecipeMaps() {
        // Note: To correctly generate localized names, currently all conversion recipes need to be registered
        // before processing recipes. I'll admit this is a bit messy, so I may try to find a solution for this
        // in the future (TODO)

        // Conversion recipes
        // Wires
        addConversionRecipe(
            CircuitComponent.WireNiobiumTitanium,
            GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 1));
        addConversionRecipe(
            CircuitComponent.WireYttriumBariumCuprate,
            GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.YttriumBariumCuprate, 1));
        addConversionRecipe(
            CircuitComponent.WireLumiium,
            new ItemStack(WerkstoffLoader.items.get(OrePrefixes.wireFine), 1, 10101));
        // SMDs
        addConversionRecipe(CircuitComponent.SMDTransistor, ItemList.Circuit_Parts_TransistorSMD.get(1));
        addConversionRecipe(CircuitComponent.SMDInductor, ItemList.Circuit_Parts_InductorSMD.get(1));
        addConversionRecipe(CircuitComponent.SMDCapacitor, ItemList.Circuit_Parts_CapacitorSMD.get(1));
        addConversionRecipe(CircuitComponent.SMDDiode, ItemList.Circuit_Parts_DiodeSMD.get(1));
        addConversionRecipe(CircuitComponent.SMDResistor, ItemList.Circuit_Parts_ResistorSMD.get(1));
        addConversionRecipe(CircuitComponent.AdvSMDTransistor, ItemList.Circuit_Parts_TransistorASMD.get(1));
        addConversionRecipe(CircuitComponent.AdvSMDInductor, ItemList.Circuit_Parts_InductorASMD.get(1));
        addConversionRecipe(CircuitComponent.AdvSMDCapacitor, ItemList.Circuit_Parts_CapacitorASMD.get(1));
        addConversionRecipe(CircuitComponent.AdvSMDDiode, ItemList.Circuit_Parts_DiodeASMD.get(1));
        addConversionRecipe(CircuitComponent.AdvSMDResistor, ItemList.Circuit_Parts_ResistorASMD.get(1));
        addConversionRecipe(CircuitComponent.OpticalSMDTransistor, ItemList.Circuit_Parts_TransistorXSMD.get(1));
        addConversionRecipe(CircuitComponent.OpticalSMDInductor, ItemList.Circuit_Parts_InductorXSMD.get(1));
        addConversionRecipe(CircuitComponent.OpticalSMDCapacitor, ItemList.Circuit_Parts_CapacitorXSMD.get(1));
        addConversionRecipe(CircuitComponent.OpticalSMDDiode, ItemList.Circuit_Parts_DiodeXSMD.get(1));
        addConversionRecipe(CircuitComponent.OpticalSMDResistor, ItemList.Circuit_Parts_ResistorXSMD.get(1));
        // Boards
        addConversionRecipe(
            CircuitComponent.BoardMultifiberglassElite,
            ItemList.Circuit_Board_Multifiberglass_Elite.get(1));
        addConversionRecipe(CircuitComponent.BoardWetwareLifesupport, ItemList.Circuit_Board_Wetware_Extreme.get(1));
        addConversionRecipe(CircuitComponent.BoardBioMutated, ItemList.Circuit_Board_Bio_Ultra.get(1));
        addConversionRecipe(CircuitComponent.BoardOptical, ItemList.Circuit_Board_Optical.get(1));
        // CPUs
        addConversionRecipe(CircuitComponent.ChipCrystalCPU, ItemList.Circuit_Chip_CrystalCPU.get(1));
        addConversionRecipe(CircuitComponent.ChipAdvCrystalCPU, ItemList.Circuit_Chip_CrystalSoC.get(1));
        addConversionRecipe(CircuitComponent.ChipOpticalCPU, ItemList.Optically_Perfected_CPU.get(1));
        // RAM
        addConversionRecipe(CircuitComponent.OpticalRAM, ItemList.Optically_Compatible_Memory.get(1));
        // Wafers
        addConversionRecipe(CircuitComponent.WaferNanoCPU, ItemList.Circuit_Wafer_NanoCPU.get(1));
        addConversionRecipe(CircuitComponent.WaferRAM, ItemList.Circuit_Wafer_Ram.get(1));
        addConversionRecipe(CircuitComponent.WaferNOR, ItemList.Circuit_Wafer_NOR.get(1));
        addConversionRecipe(CircuitComponent.WaferNAND, ItemList.Circuit_Wafer_NAND.get(1));
        addConversionRecipe(CircuitComponent.WaferASOC, ItemList.Circuit_Wafer_SoC2.get(1));
        // Superconductors
        addConversionRecipe(
            CircuitComponent.SuperconductorLuV,
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1));
        addConversionRecipe(
            CircuitComponent.SuperconductorZPM,
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1));
        addConversionRecipe(
            CircuitComponent.SuperconductorUHV,
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1));
        addConversionRecipe(
            CircuitComponent.SuperconductorUEV,
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 1));
        // Frame boxes
        addConversionRecipe(
            CircuitComponent.FrameboxAluminium,
            GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1));
        addConversionRecipe(
            CircuitComponent.FrameboxTritanium,
            GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 1));
        // Bolts
        addConversionRecipe(
            CircuitComponent.BoltEnrichedHolmium,
            GTOreDictUnificator.get(OrePrefixes.bolt, Materials.EnrichedHolmium, 1));
        // Cables
        addConversionRecipe(CircuitComponent.CableOpticalFiber, DATApipe.get(1));
        // Foil
        addConversionRecipe(
            CircuitComponent.FoilSiliconeRubber,
            GTOreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 1));
        addConversionRecipe(
            CircuitComponent.FoilPolybenzimidazole,
            GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 1));

        // Processing recipes
        // Wire processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.WireNiobiumTitanium,
            CircuitComponent.ProcessedWireNiobiumTitanium,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.WireYttriumBariumCuprate,
            CircuitComponent.ProcessedWireYttriumBariumCuprate,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.WireLumiium,
            CircuitComponent.ProcessedWireLumiium,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        // SMD processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.SMDResistor,
            CircuitComponent.ProcessedSMDResistor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.SMDTransistor,
            CircuitComponent.ProcessedSMDTransistor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.SMDInductor,
            CircuitComponent.ProcessedSMDInductor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.SMDCapacitor,
            CircuitComponent.ProcessedSMDCapacitor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.SMDDiode,
            CircuitComponent.ProcessedSMDDiode,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDResistor,
            CircuitComponent.ProcessedAdvSMDResistor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDTransistor,
            CircuitComponent.ProcessedAdvSMDTransistor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDInductor,
            CircuitComponent.ProcessedAdvSMDInductor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDCapacitor,
            CircuitComponent.ProcessedAdvSMDCapacitor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.AdvSMDDiode,
            CircuitComponent.ProcessedAdvSMDDiode,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDResistor,
            CircuitComponent.ProcessedOpticalSMDResistor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDTransistor,
            CircuitComponent.ProcessedOpticalSMDTransistor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDInductor,
            CircuitComponent.ProcessedOpticalSMDInductor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDCapacitor,
            CircuitComponent.ProcessedOpticalSMDCapacitor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDDiode,
            CircuitComponent.ProcessedOpticalSMDDiode,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        // Board processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.BoardMultifiberglassElite,
            Materials.IronIIIChloride.getFluid(1000),
            CircuitComponent.ProcessedBoardMultifiberglassElite,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.BoardWetwareLifesupport,
            Materials.GrowthMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedBoardWetwareLifesupport,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.BoardBioMutated,
            Materials.BioMediumSterilized.getFluid(1000),
            CircuitComponent.ProcessedBoardBioMutated,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.BoardOptical,
            Materials.MysteriousCrystal.getFluid(1000),
            CircuitComponent.ProcessedBoardOptical,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        // CPU processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.ChipCrystalCPU,
            CircuitComponent.ProcessedChipCrystalCPU,
            ModuleRecipeInfo.Slow,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipAdvCrystalCPU,
            CircuitComponent.ProcessedChipAdvCrystalCPU,
            ModuleRecipeInfo.Slow,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipOpticalCPU,
            CircuitComponent.ProcessedChipOpticalCPU,
            ModuleRecipeInfo.Slow,
            TierEU.RECIPE_LV);
        // RAM processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalRAM,
            CircuitComponent.ProcessedOpticalRAM,
            ModuleRecipeInfo.Slow,
            TierEU.RECIPE_LV);
        // Wafer cutting processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.WaferNanoCPU,
            Materials.Lubricant.getFluid(250),
            CircuitComponent.ProcessedChipNanoCPU,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.WaferRAM,
            Materials.Lubricant.getFluid(67),
            CircuitComponent.ProcessedChipRAM,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.WaferNOR,
            Materials.Lubricant.getFluid(135),
            CircuitComponent.ProcessedChipNOR,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.WaferNAND,
            Materials.Lubricant.getFluid(135),
            CircuitComponent.ProcessedChipNAND,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.WaferASOC,
            Materials.Lubricant.getFluid(250),
            CircuitComponent.ProcessedChipASOC,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        // Superconductor processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorLuV,
            CircuitComponent.ProcessedSuperconductorLuV,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorZPM,
            CircuitComponent.ProcessedSuperconductorZPM,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUHV,
            CircuitComponent.ProcessedSuperconductorUHV,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorUEV,
            CircuitComponent.ProcessedSuperconductorUEV,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        // Frame box processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.FrameboxAluminium,
            Materials.Grade1PurifiedWater.getFluid(1000),
            CircuitComponent.ProcessedFrameboxAluminium,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.FrameboxTritanium,
            Materials.Grade1PurifiedWater.getFluid(1000),
            CircuitComponent.ProcessedFrameboxTritanium,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
    }

    public static void populateFinishedCircuitRecipeMaps() {
        // Circuit assembly line replacements
        // TODO: Tweak EU/t
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipCrystalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 8)),
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 72)),
            CircuitComponent.CrystalProcessor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.CrystalProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 16)),
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 144)),
            CircuitComponent.CrystalAssembly,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.CrystalAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNAND, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 32)),
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 75)),
            CircuitComponent.CrystalComputer,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedFrameboxAluminium, 2),
                new CircuitComponentStack(CircuitComponent.CrystalComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorLuV, 16)),
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 288)),
            CircuitComponent.CrystalMainframe,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.Neuroprocessor, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipCrystalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 8)),
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 72)),
            CircuitComponent.WetwareProcessor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardWetwareLifesupport, 1),
                new CircuitComponentStack(CircuitComponent.WetwareProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 12),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 16)),
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 144)),
            CircuitComponent.WetwareAssembly,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardWetwareLifesupport, 2),
                new CircuitComponentStack(CircuitComponent.WetwareAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDDiode, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 24)),
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 144)),
            CircuitComponent.WetwareComputer,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedFrameboxTritanium, 2),
                new CircuitComponentStack(CircuitComponent.WetwareComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDResistor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDDiode, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 48),
                new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorZPM, 64),
                new CircuitComponentStack(CircuitComponent.FoilSiliconeRubber, 64)),
            Arrays.asList(
                FluidRegistry.getFluidStack("molten.indalloy140", 2880),
                FluidRegistry.getFluidStack("ic2coolant", 10000),
                Materials.Radon.getFluid(2500)),
            CircuitComponent.WetwareMainframe,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.Bioprocessor, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipAdvCrystalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 12),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 12),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 16)),
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 72)),
            CircuitComponent.BiowareProcessor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardBioMutated, 1),
                new CircuitComponentStack(CircuitComponent.BiowareProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 12),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedWireYttriumBariumCuprate, 24)),
            Arrays.asList(FluidRegistry.getFluidStack("molten.indalloy140", 144)),
            CircuitComponent.BiowareAssembly,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardBioMutated, 2),
                new CircuitComponentStack(CircuitComponent.BiowareAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDResistor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDDiode, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 32),
                new CircuitComponentStack(CircuitComponent.FoilSiliconeRubber, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1440),
                Materials.BioMediumSterilized.getFluid(1440),
                Materials.SuperCoolant.getFluid(10000)),
            CircuitComponent.BiowareComputer,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedFrameboxTritanium, 4),
                new CircuitComponentStack(CircuitComponent.BiowareComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDResistor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDDiode, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUHV, 64),
                new CircuitComponentStack(CircuitComponent.FoilSiliconeRubber, 64),
                new CircuitComponentStack(CircuitComponent.FoilSiliconeRubber, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880),
                Materials.BioMediumSterilized.getFluid(2880),
                Materials.SuperCoolant.getFluid(20000)),
            CircuitComponent.BiowareMainframe,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedChipOpticalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalRAM, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 16),
                new CircuitComponentStack(CircuitComponent.CableOpticalFiber, 4),
                new CircuitComponentStack(CircuitComponent.BoltEnrichedHolmium, 16)),
            Arrays.asList(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(288)),
            CircuitComponent.OpticalProcessor,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardOptical, 1),
                new CircuitComponentStack(CircuitComponent.OpticalProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 16),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 20),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 20),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedWireLumiium, 24),
                new CircuitComponentStack(CircuitComponent.FoilSiliconeRubber, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1440),
                Materials.Radon.getFluid(1440),
                Materials.SuperCoolant.getFluid(10000),
                FluidRegistry.getFluidStack("oganesson", 500)),
            CircuitComponent.OpticalAssembly,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardOptical, 2),
                new CircuitComponentStack(CircuitComponent.OpticalAssembly, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNOR, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedChipASOC, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedWireLumiium, 32),
                new CircuitComponentStack(CircuitComponent.FoilSiliconeRubber, 64),
                new CircuitComponentStack(CircuitComponent.FoilPolybenzimidazole, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880),
                Materials.Radon.getFluid(2880),
                Materials.SuperCoolant.getFluid(20000),
                FluidRegistry.getFluidStack("oganesson", 1000)),
            CircuitComponent.OpticalComputer,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedFrameboxTritanium, 8),
                new CircuitComponentStack(CircuitComponent.OpticalComputer, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDInductor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDCapacitor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDResistor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDTransistor, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedOpticalSMDDiode, 32),
                new CircuitComponentStack(CircuitComponent.ProcessedChipASOC, 64),
                new CircuitComponentStack(CircuitComponent.ProcessedSuperconductorUEV, 64),
                new CircuitComponentStack(CircuitComponent.FoilSiliconeRubber, 64),
                new CircuitComponentStack(CircuitComponent.FoilSiliconeRubber, 64),
                new CircuitComponentStack(CircuitComponent.FoilPolybenzimidazole, 64),
                new CircuitComponentStack(CircuitComponent.FoilPolybenzimidazole, 64)),
            Arrays.asList(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(5760),
                Materials.Radon.getFluid(5760),
                Materials.SuperCoolant.getFluid(40000),
                FluidRegistry.getFluidStack("oganesson", 2000)),
            CircuitComponent.OpticalMainframe,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LuV);
    }
}
