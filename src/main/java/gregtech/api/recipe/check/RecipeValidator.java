package gregtech.api.recipe.check;

import java.util.function.Function;
import java.util.function.Predicate;

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
    private final Function<GT_Recipe, CheckRecipeResult> recipeValidator;
    private final Function<GT_Recipe, GT_ParallelHelper> parallelHelperFactory;
    private final Function<GT_Recipe, GT_OverclockCalculator> overclockCalculatorFactory;

    public RecipeValidator(Function<GT_Recipe, CheckRecipeResult> recipeValidator,
        Function<GT_Recipe, GT_ParallelHelper> parallelHelperFactory,
        Function<GT_Recipe, GT_OverclockCalculator> overclockCalculatorFactory) {
        this.recipeValidator = recipeValidator;
        this.parallelHelperFactory = parallelHelperFactory;
        this.overclockCalculatorFactory = overclockCalculatorFactory;
    }

    @Override
    public boolean test(GT_Recipe recipe) {
        wasExecutedAtLeastOnce = true;
        CheckRecipeResult checkRecipeResult = checkRecipe(recipe);
        if (firstCheckResult == null) {
            firstCheckResult = checkRecipeResult;
        }
        return checkRecipeResult.wasSuccessful();
    }

    private CheckRecipeResult checkRecipe(GT_Recipe recipe) {
        lastCheckResult = recipeValidator.apply(recipe);

        if (!lastCheckResult.wasSuccessful()) {
            return lastCheckResult;
        }

        lastParallelHelper = parallelHelperFactory.apply(recipe);
        lastOverclockCalculator = overclockCalculatorFactory.apply(recipe);
        lastParallelHelper.setCalculator(lastOverclockCalculator);
        lastParallelHelper.build();

        return lastParallelHelper.getResult();
    }

    public Boolean isExecutedAtLeastOnce() {
        return wasExecutedAtLeastOnce;
    }

    /**
     * Gets first check result in case if nothing matching recipe found.
     */
    public CheckRecipeResult getFirstCheckResult() {
        return firstCheckResult;
    }

    public CheckRecipeResult getLastCheckResult() {
        return lastCheckResult;
    }

    public GT_ParallelHelper getLastParallelHelper() {
        return lastParallelHelper;
    }

    public GT_OverclockCalculator getLastOverclockCalculator() {
        return lastOverclockCalculator;
    }
}
