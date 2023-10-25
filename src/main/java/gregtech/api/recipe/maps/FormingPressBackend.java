package gregtech.api.recipe.maps;

import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;

import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Special Class for Forming Press handling.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FormingPressBackend extends RecipeMapBackend {

    public FormingPressBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected FindRecipeResult modifyFoundRecipe(FindRecipeResult result, ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot) {
        for (ItemStack mold : items) {
            if (ItemList.Shape_Mold_Credit.isStackEqual(mold, false, true)) {
                NBTTagCompound nbt = mold.getTagCompound();
                if (nbt == null) nbt = new NBTTagCompound();
                if (!nbt.hasKey("credit_security_id")) nbt.setLong("credit_security_id", System.nanoTime());
                mold.setTagCompound(nbt);

                GT_Recipe recipe = result.getRecipeNonNull();
                recipe = recipe.copy();
                recipe.mCanBeBuffered = false;
                recipe.mOutputs[0].setTagCompound(nbt);
                return FindRecipeResult.ofSuccess(recipe);
            }
        }
        return result;
    }

    @Override
    protected FindRecipeResult findFallback(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        Predicate<GT_Recipe> recipeValidator) {
        if (items.length < 2) {
            return NOT_FOUND;
        }
        return findRenamingRecipe(items, recipeValidator);
    }

    private FindRecipeResult findRenamingRecipe(ItemStack[] inputs, Predicate<GT_Recipe> recipeValidator) {
        ItemStack mold = findNameMoldIndex(inputs);
        if (mold == null) return NOT_FOUND;
        ItemStack input = findStackToRename(inputs, mold);
        if (input == null) return NOT_FOUND;
        ItemStack output = GT_Utility.copyAmount(1, input);
        if (output == null) return NOT_FOUND;
        output.setStackDisplayName(mold.getDisplayName());
        return GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(0, mold), GT_Utility.copyAmount(1, input))
            .itemOutputs(output)
            .duration(128)
            .eut(8)
            .noBuffer()
            .nbtSensitive()
            .buildAndGetResult(recipeValidator);
    }

    @Nullable
    private ItemStack findNameMoldIndex(ItemStack[] inputs) {
        for (ItemStack stack : inputs) {
            if (ItemList.Shape_Mold_Name.isStackEqual(stack, false, true)) return stack;
        }
        return null;
    }

    @Nullable
    private ItemStack findStackToRename(ItemStack[] inputs, ItemStack mold) {
        for (ItemStack stack : inputs) {
            if (stack == mold || stack == null) continue;
            return stack;
        }
        return null;
    }
}
