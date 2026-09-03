package gregtech.api.recipe.maps;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NACRecipeMapBackend extends RecipeMapBackend {

    private final int[] maxDurations = new int[GTValues.V.length];

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
        Arrays.fill(maxDurations, 0);
    }

    @Override
    public void reInit() {
        super.reInit();
        rebuildMaxDuration();
    }

    private void rebuildMaxDuration() {
        Arrays.fill(maxDurations, 0);
        for (GTRecipe recipe : getAllRecipes()) {
            addRecipeDuration(recipe);
        }
    }

    private void addRecipeDuration(GTRecipe recipe) {
        int tier = GTUtility.getTier(recipe.mEUt);
        for (int i = tier; i < maxDurations.length; i++) {
            maxDurations[i] = Math.max(maxDurations[i], recipe.mDuration);
        }
    }

    public int getMaxDuration(int tier) {
        if (tier == -1) return maxDurations[maxDurations.length - 1];
        return maxDurations[tier];
    }
}
