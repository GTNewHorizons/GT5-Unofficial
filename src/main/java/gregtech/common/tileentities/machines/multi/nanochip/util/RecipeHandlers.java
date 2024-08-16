package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;

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
    // Note that the duration of the recipe just indicates the speed, not necessarily the actual duration, since recipes
    // are always processed in 20 ticks.
    // TODO: Formalize this duration system a bit more once I get to implementing it
    private static void addSimpleProcessingRecipe(CircuitComponent input, CircuitComponent output, int duration) {
        RecipeMap<?> recipeMap = input.processingMap;
        if (recipeMap == null) {
            throw new IllegalArgumentException(
                "Tried to add component processing recipe for a component without an associated recipemap");
        }
        GT_Values.RA.stdBuilder()
            .itemInputs(input.getFakeStack(1))
            .itemOutputs(output.getFakeStack(1))
            .duration(duration)
            .eut(0)
            .addTo(recipeMap);
    }

    public static void populateCircuitComponentRecipeMaps() {
        addConversionRecipe(
            CircuitComponent.WireNiobiumTitanium,
            GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 1));
        addConversionRecipe(CircuitComponent.SMDTransistor, ItemList.Circuit_Parts_TransistorSMD.get(1));
        addConversionRecipe(CircuitComponent.SMDInductor, ItemList.Circuit_Parts_InductorSMD.get(1));
        addConversionRecipe(CircuitComponent.SMDCapacitor, ItemList.Circuit_Parts_CapacitorSMD.get(1));
        addConversionRecipe(CircuitComponent.SMDDiode, ItemList.Circuit_Parts_DiodeSMD.get(1));
        addConversionRecipe(CircuitComponent.AdvSMDTransistor, ItemList.Circuit_Parts_TransistorASMD.get(1));
        addConversionRecipe(CircuitComponent.AdvSMDInductor, ItemList.Circuit_Parts_InductorASMD.get(1));
        addConversionRecipe(CircuitComponent.AdvSMDCapacitor, ItemList.Circuit_Parts_CapacitorASMD.get(1));
        addConversionRecipe(CircuitComponent.AdvSMDDiode, ItemList.Circuit_Parts_DiodeASMD.get(1));
        addConversionRecipe(CircuitComponent.OpticalSMDTransistor, ItemList.Circuit_Parts_TransistorXSMD.get(1));
        addConversionRecipe(CircuitComponent.OpticalSMDInductor, ItemList.Circuit_Parts_InductorXSMD.get(1));
        addConversionRecipe(CircuitComponent.OpticalSMDCapacitor, ItemList.Circuit_Parts_CapacitorXSMD.get(1));
        addConversionRecipe(CircuitComponent.OpticalSMDDiode, ItemList.Circuit_Parts_DiodeXSMD.get(1));

        // Add processing recipes for SMD module
        addSimpleProcessingRecipe(CircuitComponent.SMDTransistor, CircuitComponent.ProcessedSMDTransistor, 20);
        addSimpleProcessingRecipe(CircuitComponent.SMDInductor, CircuitComponent.ProcessedSMDInductor, 20);
        addSimpleProcessingRecipe(CircuitComponent.SMDCapacitor, CircuitComponent.ProcessedSMDCapacitor, 20);
        addSimpleProcessingRecipe(CircuitComponent.SMDDiode, CircuitComponent.ProcessedSMDDiode, 20);
        addSimpleProcessingRecipe(CircuitComponent.AdvSMDTransistor, CircuitComponent.ProcessedAdvSMDTransistor, 20);
        addSimpleProcessingRecipe(CircuitComponent.AdvSMDInductor, CircuitComponent.ProcessedAdvSMDInductor, 20);
        addSimpleProcessingRecipe(CircuitComponent.AdvSMDCapacitor, CircuitComponent.ProcessedAdvSMDCapacitor, 20);
        addSimpleProcessingRecipe(CircuitComponent.AdvSMDDiode, CircuitComponent.ProcessedAdvSMDDiode, 20);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDTransistor,
            CircuitComponent.ProcessedOpticalSMDTransistor,
            20);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDInductor,
            CircuitComponent.ProcessedOpticalSMDInductor,
            20);
        addSimpleProcessingRecipe(
            CircuitComponent.OpticalSMDCapacitor,
            CircuitComponent.ProcessedOpticalSMDCapacitor,
            20);
        addSimpleProcessingRecipe(CircuitComponent.OpticalSMDDiode, CircuitComponent.ProcessedOpticalSMDDiode, 20);
    }

    private static GT_Recipe findRecipeUsingStack(ItemStack input, RecipeMap<?> map) {
        return map.findRecipeQuery()
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
            CircuitComponent componentInput = CircuitComponent.getFromFakeStack(processedFakeInput);
            ItemStack fakeInputStack = componentInput.getFakeStack(input.stackSize);
            itemInputs.set(i, fakeInputStack);
        }

        return builder.itemInputs(itemInputs.toArray(new ItemStack[] {}))
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
    });
}
