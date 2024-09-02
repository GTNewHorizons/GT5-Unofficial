package gregtech.api.recipe.maps;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
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
    protected abstract GTRecipe overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot, @Nullable GTRecipe cachedRecipe);

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
    protected GTRecipe addToItemMap(GTRecipe recipe) {
        return recipe;
    }
}
