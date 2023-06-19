package gregtech.api.recipe.check;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
     * Recipe cannot be null if state == FOUND or INSUFFICIENT_VOLTAGE.
     */
    @Nonnull
    public GT_Recipe getRecipeNonNull() {
        return Objects.requireNonNull(recipe);
    }

    public static FindRecipeResult ofSuccess(@Nonnull GT_Recipe recipe) {
        return new FindRecipeResult(State.FOUND, Objects.requireNonNull(recipe));
    }

    public static FindRecipeResult ofInsufficientVoltage(@Nonnull GT_Recipe recipe) {
        return new FindRecipeResult(State.INSUFFICIENT_VOLTAGE, Objects.requireNonNull(recipe));
    }

    public static final FindRecipeResult NOT_FOUND = new FindRecipeResult(State.NOT_FOUND, null);
    public static final FindRecipeResult EXPLODE = new FindRecipeResult(State.EXPLODE, null);
    public static final FindRecipeResult ON_FIRE = new FindRecipeResult(State.ON_FIRE, null);

    public enum State {

        /**
         * Successfully found recipe.
         */
        FOUND(true),
        /**
         * Recipe was found, but voltage is not sufficient to run.
         */
        INSUFFICIENT_VOLTAGE(false),
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
