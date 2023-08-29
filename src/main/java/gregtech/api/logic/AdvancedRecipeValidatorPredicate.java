package gregtech.api.logic;

import java.util.function.Function;
import java.util.function.Predicate;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;

/**
 * Predicate for simple recipe validation.
 * Also store some validation results for reusing it
 */
public class AdvancedRecipeValidatorPredicate implements Predicate<GT_Recipe> {

    /**
     * Used to store first check result in case if nothing matching recipe found.
     */
    public CheckRecipeResult firstCheckResult;
    public CheckRecipeResult lastCheckResult;
    public GT_ParallelHelper lastParallelHelper;
    public GT_OverclockCalculator lastOverclockCalculator;
    public Boolean wasExecutedAtLeastOnce = false;
    private final Function<GT_Recipe, CheckRecipeResult> recipeValidator;
    private final Function<GT_Recipe, GT_ParallelHelper> parallelHelperFactory;
    private final Function<GT_Recipe, GT_OverclockCalculator> overclockCalculatorFactory;

    public AdvancedRecipeValidatorPredicate(Function<GT_Recipe, CheckRecipeResult> recipeValidator,
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
}
