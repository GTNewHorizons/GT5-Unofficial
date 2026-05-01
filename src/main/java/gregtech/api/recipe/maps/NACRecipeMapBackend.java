package gregtech.api.recipe.maps;

import org.jetbrains.annotations.NotNull;

import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;

public class NACRecipeMapBackend extends RecipeMapBackend {

    private int maxDuration;

    public NACRecipeMapBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    public @NotNull GTRecipe compileRecipe(GTRecipe recipe) {
        this.maxDuration = Math.max(maxDuration, recipe.mDuration);
        return super.compileRecipe(recipe);
    }

    public int getMaxDuration() {
        return maxDuration;
    }
}
