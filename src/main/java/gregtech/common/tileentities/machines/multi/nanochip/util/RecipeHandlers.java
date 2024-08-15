package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;

public class RecipeHandlers {

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

        // Basic process is simple for now: Take each item input and translate it to a circuit component (if possible)
        // Note that this breaks for all recursive circuit steps, so we will definitely need a smarter way to do this,
        // or
        // simply not auto-generate these recipes
        for (int i = 0; i < itemInputs.size(); ++i) {
            ItemStack input = itemInputs.get(i);
            CircuitComponent component = CircuitComponent.getFromRealStack(input);
            // If a component was found, replace the input
            if (component != null) {
                ItemStack fakeStack = component.getFakeStack(input.stackSize);
                itemInputs.set(i, fakeStack);
            }
        }

        return builder.itemInputs(itemInputs.toArray(new ItemStack[] {}))
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
    });
}
