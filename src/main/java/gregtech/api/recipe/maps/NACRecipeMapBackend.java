package gregtech.api.recipe.maps;

import java.util.Collection;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NACRecipeMapBackend extends RecipeMapBackend {

    private int maxDuration;

    public NACRecipeMapBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    public @NotNull GTRecipe compileRecipe(GTRecipe recipe) {
        GTRecipe compiledRecipe = super.compileRecipe(recipe);
        addRecipeDuration(compiledRecipe);
        return compiledRecipe;
    }

    @Override
    public void removeRecipes(Collection<? extends GTRecipe> recipesToRemove) {
        super.removeRecipes(recipesToRemove);
        rebuildMaxDuration();
    }

    @Override
    public void clearRecipes() {
        super.clearRecipes();
        maxDuration = 0;
    }

    @Override
    public void reInit() {
        super.reInit();
        rebuildMaxDuration();
    }

    private void rebuildMaxDuration() {
        maxDuration = 0;
        for (GTRecipe recipe : getAllRecipes()) {
            addRecipeDuration(recipe);
        }
    }

    private void addRecipeDuration(GTRecipe recipe) {
        maxDuration = Math.max(maxDuration, recipe.mDuration);
    }

    public int getMaxDuration() {
        return maxDuration;
    }
}
