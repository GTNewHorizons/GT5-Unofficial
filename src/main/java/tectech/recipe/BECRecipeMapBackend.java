package tectech.recipe;

import javax.annotation.Nonnull;

import gregtech.api.enums.NaniteTier;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;

public class BECRecipeMapBackend extends RecipeMapBackend {

    public BECRecipeMapBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    public @Nonnull GTRecipe compileRecipe(@Nonnull GTRecipe recipe) {
        NaniteTier[] tiers = recipe.getMetadata(GTRecipeConstants.NANITE_TIERS);

        if (tiers != null && tiers.length != recipe.mInputs.length) {
            throw new IllegalArgumentException("nanite tiers length must match item input length");
        }

        return super.compileRecipe(recipe);
    }
}
