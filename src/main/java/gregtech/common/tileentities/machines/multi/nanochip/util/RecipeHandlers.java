package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.NanochipAssemblyRecipeInfo;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class RecipeHandlers {

    private static void addConversionRecipe(CircuitComponent component, ItemStack stack) {
        GT_Values.RA.stdBuilder()
            .itemInputs(stack)
            .itemOutputs(component.getFakeStack(1))
            .duration(1)
            .eut(0)
            .addTo(RecipeMaps.nanochipConversionRecipes);
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
        GT_Values.RA.stdBuilder()
            .metadata(NanochipAssemblyRecipeInfo.INSTANCE, info)
            .itemInputs(input.getFakeStack(info.baseParallel))
            .itemOutputs(output.getFakeStack(info.baseParallel))
            .duration(20)
            .eut(eut)
            .addTo(recipeMap);
    }

    public static void populateCircuitComponentRecipeMaps() {
        // Note: To correctly generate localized names, currently all conversion recipes need to be registered
        // before processing recipes. I'll admit this is a bit messy, so I may try to find a solution for this
        // in the future (TODO)

        // Wires
        addConversionRecipe(
            CircuitComponent.WireNiobiumTitanium,
            GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 1));
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
            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1));
        // Frame boxes
        addConversionRecipe(
            CircuitComponent.FrameboxAluminium,
            GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1));
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

    private static GT_Recipe findRecipeUsingStack(ItemStack input, RecipeMap<?> map) {
        return map.findRecipeQuery()
            .dontCheckStackSizes(true)
            .items(input)
            .find();
    }

    private static ItemStack findResultingStack(ItemStack input, RecipeMap<?> map) {
        GT_Recipe recipe = findRecipeUsingStack(input, map);
        if (recipe == null) return null;
        return recipe.mOutputs[0];
    }

    private static ItemStack traverseCircuitRecipes(ItemStack input, RecipeMap<?> inputProcessingMap) {
        // TODO: In this algorithm, we might still need to keep track of amount ratios (1 SMD = N Processed SMD = 1/N
        // Circuits or something)

        // Special case: if this item is a finished circuit item, we already know the component to use and we don't do
        // this full iteration
        CircuitComponent circuitComponent = CircuitComponent.realCircuitToComponent
            .get(GT_Utility.ItemId.createNoCopy(input));
        if (circuitComponent != null) return circuitComponent.getFakeStack(input.stackSize);
        // If the recipe map is null, we have a finalized CC (probably, this could also be a bug or missing mappings)
        if (inputProcessingMap == null) return input;
        // If this map is the nanochip assembly matrix map, return the input as we have finished the process
        if (inputProcessingMap.unlocalizedName.equals(RecipeMaps.nanochipAssemblyMatrixRecipes.unlocalizedName)) {
            return input;
        }
        // Find the result of applying the recipe map to this input
        ItemStack result = findResultingStack(input, inputProcessingMap);
        // If this is null, we cannot process this stack into a finalized circuit (probably some mappings for items are
        // missing).
        if (result == null) return null;
        // This resulting item is a fake stack, so find the circuit component that corresponds to it
        CircuitComponent component = CircuitComponent.getFromFakeStack(result);
        // Process this component further in the next recipe map
        return traverseCircuitRecipes(result, component.processingMap);
    }

    public static final IRecipeMap assemblyMatrixRecipeTransformer = IRecipeMap.newRecipeMap(builder -> {
        // This RecipeMap is added as a downstream of the circuit assembler recipe map. This means that any
        // circuit assembler recipe will also be added to this map, so we can process it further.
        // One note is that BW also modifies this recipe map, to make the recipes that should go in the CAL 6x more
        // expensive and to add its own recipes to the CAL. However, this happens after the recipe map is built already.
        // Also, when re-adding recipes, it uses the (deprecated) RecipeMap::add, which bypasses the backend method to
        // also
        // add recipes to the downstream maps, so this transformer won't be called for the modified recipes.
        // This is a good thing, it means we can deal with unmodified circuit assembler recipes here and don't have to
        // worry about the CAL recipe transformation code at all.

        // Before we do anything, find the output item, so we can check if this recipe should be touched at all
        ItemStack realOutput = builder.getItemOutput(0);
        GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(realOutput);
        CircuitComponent outputComponent = CircuitComponent.realCircuitToComponent.get(id);
        if (outputComponent == null) {
            // Not a nanochip assembly recipe because it has no matching fake circuit, return empty list
            return Collections.emptyList();
        }

        // Grab a copy of the item input list
        ArrayList<ItemStack> itemInputs = new ArrayList<>(Arrays.asList(builder.getItemInputsBasic()));

        // For each input, we follow the following procedure
        // 1. Find the circuit component produced by this item
        // 2. Follow the recipe map processing this circuit component given by CircuitComponent#processingMap to find
        // the next step in the process. This gives us a new circuit component in the chain.
        // 3. Keep following this chain of circuit components until we arrive at a component that is an input in the
        // assembly matrix.
        // 4. Use this component as the input of the assembly step.
        // Note that this process will only work for the basic circuits, the advanced processing lines using
        // the nanochip assembly complex will not be autogenerated by this process.
        for (int i = 0; i < itemInputs.size(); ++i) {
            ItemStack input = itemInputs.get(i);
            // Traverse the recipe map to find the final input needed in the assembly matrix
            ItemStack processedFakeInput = traverseCircuitRecipes(input, RecipeMaps.nanochipConversionRecipes);
            // If none was found, we simply keep this unchanged in the assembly matrix. Likely some mappings are
            // missing,
            // so the recipe will be incorrectly generated in this case, but for debugging purposes this is fine.
            if (processedFakeInput == null) continue;
            // Get the input component, so we can construct a new fake input stack with the correct stack size easily.
            // Note that this *is* a roundabout way to copying the processedFakeInput ItemStack, but whatever, this
            // does logically make sense and is a bit more explicit. As a bonus, this verifies that processedFakeInput
            // is in fact a circuit component, so this is another sanity check that can catch bugs in
            // traverseCircuitRecipes.
            CircuitComponent componentInput = CircuitComponent.getFromFakeStack(processedFakeInput);
            ItemStack fakeInputStack = componentInput.getFakeStack(input.stackSize);
            itemInputs.set(i, fakeInputStack);
        }

        ItemStack fakeOutput = outputComponent.getFakeStack(realOutput.stackSize);
        return builder.itemInputs(itemInputs.toArray(new ItemStack[] {}))
            // Note that we keep the EU/t of the original recipe, because we want to use this to tier the recipes
            // and keep the overall power cost of assembling using CAL vs NAC similar.
            .itemOutputs(fakeOutput)
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
    });
}
