package gregtech.api.recipe.maps;

import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;

import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Special Class for Furnace Recipe handling.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FurnaceBackend extends NonGTBackend {

    public FurnaceBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected FindRecipeResult overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot, Predicate<GT_Recipe> recipeValidator, @Nullable GT_Recipe cachedRecipe) {
        if (items.length == 0 || items[0] == null) {
            return NOT_FOUND;
        }
        if (cachedRecipe != null && cachedRecipe.isRecipeInputEqual(false, true, fluids, items)
            && recipeValidator.test(cachedRecipe)) {
            return FindRecipeResult.ofSuccess(cachedRecipe);
        }
        ItemStack output = GT_ModHandler.getSmeltingOutput(items[0], false, null);
        return output == null ? NOT_FOUND
            : GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, items[0]))
                .itemOutputs(output)
                .duration(128)
                .eut(4)
                .noOptimize()
                .buildAndGetResult(recipeValidator);
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return GT_ModHandler.getSmeltingOutput(item, false, null) != null;
    }
}
