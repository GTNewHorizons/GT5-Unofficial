package gregtech.api.interfaces;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;

/**
 * Implement on a MetaTileEntity to expose non-consumed recipe inputs (e.g. molds in the Extruder)
 * so they can be shown as a suffix in the AE2 terminal interface name.
 */
public interface INonConsumedItemDisplay {

    /**
     * Returns the list of non-consumed items from the last matched recipe (items with stackSize == 0
     * in the recipe definition). Returns an empty list if no recipe has been run yet or none exist.
     */
    List<ItemStack> getNonConsumedInputDisplayItems();

    /**
     * @return Whether the given stack should be displayed as a non-consumed item of the given recipemap. Integrated
     *         Circuits are excluded because they are shown by the ghost circuit suffix instead.
     */
    static boolean isDisplayableItem(@Nullable RecipeMap<?> recipeMap, @Nullable ItemStack stack) {
        if (recipeMap == null || stack == null || GTUtility.isAnyIntegratedCircuit(stack)) return false;
        return recipeMap.getNonConsumedInputItemIds()
            .contains(GTUtility.ItemId.create(stack));
    }
}
