package gregtech.api.recipe.maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FuelBackend extends RecipeMapBackend {

    private final Map<String, GTRecipe> recipesByFluidInput = new HashMap<>();

    public FuelBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected Collection<GTRecipe> doAdd(GTRecipeBuilder builder) {
        if (builder.getDuration() == -1) {
            builder.duration(0);
        }
        if (builder.getEUt() == -1) {
            builder.eut(0);
        }
        return super.doAdd(builder);
    }

    @Override
    public GTRecipe compileRecipe(GTRecipe recipe) {
        super.compileRecipe(recipe);
        addFuelToFluidInputIndex(recipe);
        return recipe;
    }

    @Override
    public void removeRecipes(Collection<? extends GTRecipe> recipesToRemove) {
        super.removeRecipes(recipesToRemove);
        rebuildFluidInputIndex();
    }

    @Override
    public void clearRecipes() {
        super.clearRecipes();
        recipesByFluidInput.clear();
    }

    @Override
    public void reInit() {
        super.reInit();
        rebuildFluidInputIndex();
    }

    private void rebuildFluidInputIndex() {
        recipesByFluidInput.clear();
        for (GTRecipe recipe : getAllRecipes()) {
            addFuelToFluidInputIndex(recipe);
        }
    }

    private void addFuelToFluidInputIndex(GTRecipe recipe) {
        if (recipe.mInputs != null && GTUtility.getNonnullElementCount(recipe.mInputs) == 1
            && (recipe.mFluidInputs == null || GTUtility.getNonnullElementCount(recipe.mFluidInputs) == 0)) {
            FluidStack fluidStack = GTUtility.getFluidForFilledItem(recipe.mInputs[0], true);
            if (fluidStack != null) {
                fluidStack.amount = 0;
                recipesByFluidInput.put(
                    fluidStack.getFluid()
                        .getName(),
                    recipe);
            }
        } else if ((recipe.mInputs == null || GTUtility.getNonnullElementCount(recipe.mInputs) == 0)
            && recipe.mFluidInputs != null
            && GTUtility.getNonnullElementCount(recipe.mFluidInputs) >= 1
            && recipe.mFluidInputs[0] != null) {
                recipesByFluidInput.put(
                    recipe.mFluidInputs[0].getFluid()
                        .getName(),
                    recipe);
            }
    }

    @Nullable
    public GTRecipe findFuel(FluidStack fluidStack) {
        return findFuel(fluidStack.getFluid());
    }

    @Nullable
    public GTRecipe findFuel(Fluid fluid) {
        return recipesByFluidInput.get(fluid.getName());
    }
}
