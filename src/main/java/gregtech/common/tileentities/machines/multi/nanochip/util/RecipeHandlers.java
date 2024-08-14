package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;

public class RecipeHandlers {

    public static final IRecipeMap assemblyMatrixRecipeTransformer = IRecipeMap.newRecipeMap(builder -> {
        // Quick test to see if the fake items work fine
        ItemStack testInput = CircuitComponent.SMDCapacitor.stack;
        ArrayList<ItemStack> itemInputs = new ArrayList<>(Arrays.asList(builder.getItemInputsBasic()));
        itemInputs.add(testInput);

        return builder.itemInputs(itemInputs.toArray(new ItemStack[] {}))
            .addTo(RecipeMaps.nanochipAssemblyMatrixRecipes);
    });
}
