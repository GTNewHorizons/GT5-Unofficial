package gregtech.api.recipe.check;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import gregtech.api.util.GT_Recipe;

/**
 * Wrapper class to get result of recipe search for recipemap. Note that this only validates recipe input and voltage,
 * and does not involve in actual check in the machine such as output space or special value.
 */
public class FindRecipeResult {

    @Nonnull
    private final State state;
    @Nullable
    private final GT_Recipe recipe;
    @Nullable
    private RecipeValidator recipeValidator;

    private FindRecipeResult(@Nonnull State state, @Nullable GT_Recipe recipe) {
        this.state = state;
        this.recipe = recipe;
    }

    @Nonnull
    public State getState() {
        return state;
    }

    public boolean isSuccessful() {
        return state.success;
    }

    /**
     * If you already checked {@link #isSuccessful()}, you can use {@link #getRecipeNonNull()} instead.
     */
    @Nullable
    public GT_Recipe getRecipe() {
        return recipe;
    }

    /**
     * You should use this ONLY WHEN state == FOUND.
     */
    @Nonnull
    public GT_Recipe getRecipeNonNull() {
        return Objects.requireNonNull(recipe);
    }

    /**
     * Gets recipeValidator if it is not null.
     * Be sure to call hasRecipeValidator before to determine if recipeValidator exists
     *
     * @return not null recipe validator
     */
    @NotNull
    public RecipeValidator getRecipeValidator() {
        return Objects.requireNonNull(recipeValidator);
    }

    /**
     * Sets recipeValidator which used to get this result
     */
    public void setRecipeValidator(@Nullable RecipeValidator recipeValidator) {
        this.recipeValidator = recipeValidator;
    }

    /**
     * Gets if this result has recipeValidator
     */
    public boolean hasRecipeValidator() {
        return recipeValidator != null;
    }

    /**
     * Successfully found recipe.
     */
    public static FindRecipeResult ofSuccess(@Nonnull GT_Recipe recipe) {
        return new FindRecipeResult(State.FOUND, Objects.requireNonNull(recipe));
    }

    /**
     * No recipe found.
     */
    public static final FindRecipeResult NOT_FOUND = new FindRecipeResult(State.NOT_FOUND, null);
    /**
     * For Microwave.
     */
    public static final FindRecipeResult EXPLODE = new FindRecipeResult(State.EXPLODE, null);
    /**
     * For Microwave.
     */
    public static final FindRecipeResult ON_FIRE = new FindRecipeResult(State.ON_FIRE, null);

    public enum State {

        /**
         * Successfully found recipe.
         */
        FOUND(true),
        /**
         * No recipe found.
         */
        NOT_FOUND(false),
        /**
         * For Microwave.
         */
        EXPLODE(false),
        /**
         * For Microwave.
         */
        ON_FIRE(false);

        private final boolean success;

        State(boolean success) {
            this.success = success;
        }
    }
}
