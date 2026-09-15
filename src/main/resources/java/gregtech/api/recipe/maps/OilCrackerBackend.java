package gregtech.api.recipe.maps;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OilCrackerBackend extends RecipeMapBackend {

    private final Set<String> validCatalystFluidNames = new HashSet<>();

    public OilCrackerBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    public GTRecipe compileRecipe(GTRecipe recipe) {
        super.compileRecipe(recipe);
        addRecipeCatalystFluid(recipe);
        return recipe;
    }

    @Override
    public void removeRecipes(Collection<? extends GTRecipe> recipesToRemove) {
        super.removeRecipes(recipesToRemove);
        rebuildCatalystFluidIndex();
    }

    @Override
    public void clearRecipes() {
        super.clearRecipes();
        validCatalystFluidNames.clear();
    }

    @Override
    public void reInit() {
        super.reInit();
        rebuildCatalystFluidIndex();
    }

    private void rebuildCatalystFluidIndex() {
        validCatalystFluidNames.clear();
        for (GTRecipe recipe : getAllRecipes()) {
            addRecipeCatalystFluid(recipe);
        }
    }

    private void addRecipeCatalystFluid(GTRecipe recipe) {
        if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 1 && recipe.mFluidInputs[1] != null) {
            validCatalystFluidNames.add(
                recipe.mFluidInputs[1].getFluid()
                    .getName());
        }
    }

    public boolean isValidCatalystFluid(FluidStack fluid) {
        return validCatalystFluidNames.contains(
            fluid.getFluid()
                .getName());
    }
}
