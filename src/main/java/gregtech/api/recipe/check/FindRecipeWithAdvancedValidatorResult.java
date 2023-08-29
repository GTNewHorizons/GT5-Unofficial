package gregtech.api.recipe.check;

import org.jetbrains.annotations.NotNull;

import gregtech.api.logic.AdvancedRecipeValidatorPredicate;
import gregtech.api.util.GT_Recipe;

/**
 * Used to pass {@link AdvancedRecipeValidatorPredicate} back from recipe searching
 * without breaking existing public contract
 */
public class FindRecipeWithAdvancedValidatorResult extends FindRecipeResult {

    private final AdvancedRecipeValidatorPredicate recipeValidatorPredicate;

    private FindRecipeWithAdvancedValidatorResult(@NotNull AdvancedRecipeValidatorPredicate recipeValidatorPredicate,
        @NotNull FindRecipeResult.State state, @org.jetbrains.annotations.Nullable GT_Recipe recipe) {
        super(state, recipe);
        this.recipeValidatorPredicate = recipeValidatorPredicate;
    }

    public static FindRecipeWithAdvancedValidatorResult create(
        @NotNull AdvancedRecipeValidatorPredicate recipeValidatorPredicate, FindRecipeResult findRecipeResult) {
        return new FindRecipeWithAdvancedValidatorResult(
            recipeValidatorPredicate,
            findRecipeResult.getState(),
            findRecipeResult.getRecipe());
    }

    public AdvancedRecipeValidatorPredicate getRecipeValidatorPredicate() {
        return recipeValidatorPredicate;
    }
}
