package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.*;

import net.minecraft.item.ItemStack;

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

public class RecipeHandlers {

    private static void addConversionRecipe(CircuitComponent component, ItemStack stack) {
        GTValues.RA.stdBuilder()
            .itemInputs(stack)
            .itemOutputs(component.getFakeStack(1))
            .duration(1 * TICKS)
            .eut(0)
            .addTo(RecipeMaps.nanochipConversionRecipes);
        component.icon = stack.getIconIndex();
    }

    // Adds a simple processing recipe for circuit components in a module. The recipe map used for processing is
    // inferred
    // from the map stored by the input component.
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

    private static void addAssemblyMatrixRecipe(List<CircuitComponentStack> input, CircuitComponent output,
        ModuleRecipeInfo info, long eut) {
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

        // Wires
        addConversionRecipe(
            CircuitComponent.WireNiobiumTitanium,
            GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 1));
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
        // CPUs
        addConversionRecipe(CircuitComponent.ChipCrystalCPU, ItemList.Circuit_Chip_CrystalCPU.get(1));
        // Cut wafers
        addConversionRecipe(CircuitComponent.ChipNanoCPU, ItemList.Circuit_Chip_NanoCPU.get(1));
        addConversionRecipe(CircuitComponent.ChipRAM, ItemList.Circuit_Chip_Ram.get(1));
        addConversionRecipe(CircuitComponent.ChipNOR, ItemList.Circuit_Chip_NOR.get(1));
        addConversionRecipe(CircuitComponent.ChipNAND, ItemList.Circuit_Chip_NAND.get(1));
        // Superconductors
        addConversionRecipe(
            CircuitComponent.SuperconductorLuV,
            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1));
        // Frame boxes
        addConversionRecipe(
            CircuitComponent.FrameboxAluminium,
            GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1));
        // Wire processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.WireNiobiumTitanium,
            CircuitComponent.ProcessedWireNiobiumTitanium,
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
            CircuitComponent.ProcessedBoardMultifiberglassElite,
            ModuleRecipeInfo.Fast,
            TierEU.RECIPE_LV);
        // CPU processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.ChipCrystalCPU,
            CircuitComponent.ProcessedChipCrystalCPU,
            ModuleRecipeInfo.Slow,
            TierEU.RECIPE_LV);
        // Wafer cutting processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.ChipNanoCPU,
            CircuitComponent.ProcessedChipNanoCPU,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipRAM,
            CircuitComponent.ProcessedChipRAM,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipNOR,
            CircuitComponent.ProcessedChipNOR,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        addSimpleProcessingRecipe(
            CircuitComponent.ChipNAND,
            CircuitComponent.ProcessedChipNAND,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        // Superconductor processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.SuperconductorLuV,
            CircuitComponent.ProcessedSuperconductorLuV,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
        // Frame box processing recipes
        addSimpleProcessingRecipe(
            CircuitComponent.FrameboxAluminium,
            CircuitComponent.ProcessedFrameboxAluminium,
            ModuleRecipeInfo.Medium,
            TierEU.RECIPE_LV);
    }

    public static void populateFinishedCircuitRecipeMaps() {
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipCrystalCPU, 1),
                new CircuitComponentStack(CircuitComponent.ProcessedChipNanoCPU, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDTransistor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 8)),
            CircuitComponent.CrystalProcessor,
            ModuleRecipeInfo.Fast,
            32768);
        addAssemblyMatrixRecipe(
            Arrays.asList(
                new CircuitComponentStack(CircuitComponent.ProcessedBoardMultifiberglassElite, 1),
                new CircuitComponentStack(CircuitComponent.CrystalProcessor, 2),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDInductor, 6),
                new CircuitComponentStack(CircuitComponent.ProcessedAdvSMDCapacitor, 8),
                new CircuitComponentStack(CircuitComponent.ProcessedChipRAM, 24),
                new CircuitComponentStack(CircuitComponent.ProcessedWireNiobiumTitanium, 16)),
            CircuitComponent.CrystalAssembly,
            ModuleRecipeInfo.Fast,
            32768);
    }
}
