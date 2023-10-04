package gregtech.api.recipe.maps;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AssemblerBackend extends RecipeMapBackend {

    public AssemblerBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected FindRecipeResult modifyFoundRecipe(FindRecipeResult result, ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot) {
        GT_Recipe recipe = result.getRecipeNonNull();
        for (ItemStack item : items) {
            if (ItemList.Paper_Printed_Pages.isStackEqual(item, false, true)) {
                recipe = recipe.copy();
                recipe.mCanBeBuffered = false;
                recipe.mOutputs[0].setTagCompound(item.getTagCompound());
            }
        }
        return FindRecipeResult.ofSuccess(recipe);
    }
}
