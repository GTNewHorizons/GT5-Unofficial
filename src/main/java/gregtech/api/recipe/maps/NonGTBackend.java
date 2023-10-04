package gregtech.api.recipe.maps;

import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Abstract class for general recipe handling of non GT recipes
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class NonGTBackend extends RecipeMapBackend {

    public NonGTBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected abstract FindRecipeResult overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot, Predicate<GT_Recipe> recipeValidator, @Nullable GT_Recipe cachedRecipe);

    @Override
    protected boolean doesOverwriteFindRecipe() {
        return true;
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return false;
    }

    @Override
    public boolean containsInput(Fluid fluid) {
        return false;
    }

    @Override
    public void reInit() {}

    @Override
    protected GT_Recipe addToItemMap(GT_Recipe recipe) {
        return recipe;
    }
}
