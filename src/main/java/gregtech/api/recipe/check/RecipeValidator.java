package gregtech.api.recipe.check;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;

/**
 * Predicate for simple recipe validation.
 * Also store some validation results for reusing it
 */
public class RecipeValidator implements Predicate<GT_Recipe> {

    private CheckRecipeResult firstCheckResult;
    private CheckRecipeResult lastCheckResult;
    private GT_ParallelHelper lastParallelHelper;
    private GT_OverclockCalculator lastOverclockCalculator;
    private boolean wasExecutedAtLeastOnce = false;
    @Nonnull
    private final Function<GT_Recipe, CheckRecipeResult> recipeValidator;
    @Nonnull
    private final Function<GT_Recipe, GT_ParallelHelper> parallelHelperFactory;
    @Nonnull
    private final Function<GT_Recipe, GT_OverclockCalculator> overclockCalculatorFactory;

    public RecipeValidator(@Nonnull Function<GT_Recipe, CheckRecipeResult> recipeValidator,
        @Nonnull Function<GT_Recipe, GT_ParallelHelper> parallelHelperFactory,
        @Nonnull Function<GT_Recipe, GT_OverclockCalculator> overclockCalculatorFactory) {
        this.recipeValidator = recipeValidator;
        this.parallelHelperFactory = parallelHelperFactory;
        this.overclockCalculatorFactory = overclockCalculatorFactory;
    }

    @Override
    public boolean test(@Nullable GT_Recipe recipe) {
        if (recipe == null) return false;
        wasExecutedAtLeastOnce = true;
        CheckRecipeResult checkRecipeResult = checkRecipe(recipe);
        if (firstCheckResult == null) {
            firstCheckResult = checkRecipeResult;
        }
        return checkRecipeResult.wasSuccessful();
    }

    @Nonnull
    private CheckRecipeResult checkRecipe(@Nonnull GT_Recipe recipe) {
        lastCheckResult = Objects.requireNonNull(recipeValidator.apply(recipe));

        if (!lastCheckResult.wasSuccessful()) {
            return Objects.requireNonNull(lastCheckResult);
        }

        lastParallelHelper = parallelHelperFactory.apply(recipe);
        lastOverclockCalculator = overclockCalculatorFactory.apply(recipe);
        lastParallelHelper.setCalculator(lastOverclockCalculator);
        lastParallelHelper.build();

        return lastParallelHelper.getResult();
    }

    public boolean isExecutedAtLeastOnce() {
        return wasExecutedAtLeastOnce;
    }

    /**
     * Gets first check result in case if nothing matching recipe found.
     */
    @Nonnull
    public CheckRecipeResult getFirstCheckResult() {
        return Objects.requireNonNull(firstCheckResult);
    }

    @Nonnull
    public CheckRecipeResult getLastCheckResult() {
        return Objects.requireNonNull(lastCheckResult);
    }

    @Nonnull
    public GT_ParallelHelper getLastParallelHelper() {
        return Objects.requireNonNull(lastParallelHelper);
    }

    @Nonnull
    public GT_OverclockCalculator getLastOverclockCalculator() {
        return Objects.requireNonNull(lastOverclockCalculator);
    }
}
