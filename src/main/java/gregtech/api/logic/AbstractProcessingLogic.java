package gregtech.api.logic;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.recipe.check.RecipeValidator;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;

/**
 * Logic class to calculate result of recipe check from inputs.
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public abstract class AbstractProcessingLogic<P extends AbstractProcessingLogic<P>> {

    protected IVoidable machine;
    protected Supplier<GT_Recipe.GT_Recipe_Map> recipeMapSupplier;
    protected GT_Recipe lastRecipe;
    protected GT_Recipe.GT_Recipe_Map lastRecipeMap;
    protected ItemStack[] outputItems;
    protected FluidStack[] outputFluids;
    protected long calculatedEut;
    protected int duration;
    protected long availableVoltage;
    protected long availableAmperage;
    protected int overClockTimeReduction = 1;
    protected int overClockPowerIncrease = 2;
    protected boolean protectItems;
    protected boolean protectFluids;
    protected int maxParallel = 1;
    protected Supplier<Integer> maxParallelSupplier;
    protected int calculatedParallels = 0;
    protected int batchSize = 1;
    protected float euModifier = 1.0f;
    protected float speedBoost = 1.0f;
    protected boolean amperageOC = true;
    protected boolean isCleanroom;

    // #region Setters

    /**
     * Overwrites item output result of the calculation.
     */
    public P setOutputItems(ItemStack... itemOutputs) {
        this.outputItems = itemOutputs;
        return getThis();
    }

    /**
     * Overwrites fluid output result of the calculation.
     */
    public P setOutputFluids(FluidStack... fluidOutputs) {
        this.outputFluids = fluidOutputs;
        return getThis();
    }

    public P setIsCleanroom(boolean isCleanroom) {
        this.isCleanroom = isCleanroom;
        return getThis();
    }

    /**
     * Sets max amount of parallel.
     */
    public P setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
        return getThis();
    }

    /**
     * Sets method to get max amount of parallel.
     */
    public P setMaxParallelSupplier(Supplier<Integer> supplier) {
        this.maxParallelSupplier = supplier;
        return getThis();
    }

    /**
     * Sets batch size for batch mode.
     */
    public P setBatchSize(int size) {
        this.batchSize = size;
        return getThis();
    }

    public P setRecipeMap(GT_Recipe.GT_Recipe_Map recipeMap) {
        return setRecipeMapSupplier(() -> recipeMap);
    }

    public P setRecipeMapSupplier(Supplier<GT_Recipe.GT_Recipe_Map> supplier) {
        this.recipeMapSupplier = supplier;
        return getThis();
    }

    public P setEuModifier(float modifier) {
        this.euModifier = modifier;
        return getThis();
    }

    public P setSpeedBonus(float speedModifier) {
        this.speedBoost = speedModifier;
        return getThis();
    }

    /**
     * Sets machine used for void protection logic.
     */
    public P setMachine(IVoidable machine) {
        this.machine = machine;
        return getThis();
    }

    /**
     * Overwrites duration result of the calculation.
     */
    public P setDuration(int duration) {
        this.duration = duration;
        return getThis();
    }

    /**
     * Overwrites EU/t result of the calculation.
     */
    public P setCalculatedEut(long calculatedEut) {
        this.calculatedEut = calculatedEut;
        return getThis();
    }

    /**
     * Sets voltage of the machine. It doesn't need to be actual voltage (excluding amperage) of the machine;
     * For example, most of the multiblock machines set maximum possible input power (including amperage) as voltage
     * and 1 as amperage. That way recipemap search will be executed with overclocked voltage.
     */
    public P setAvailableVoltage(long voltage) {
        availableVoltage = voltage;
        return getThis();
    }

    /**
     * Sets amperage of the machine. This amperage doesn't involve in EU/t when searching recipemap.
     * Useful for preventing tier skip but still considering amperage for parallel.
     */
    public P setAvailableAmperage(long amperage) {
        availableAmperage = amperage;
        return getThis();
    }

    public P setVoidProtection(boolean protectItems, boolean protectFluids) {
        this.protectItems = protectItems;
        this.protectFluids = protectFluids;
        return getThis();
    }

    /**
     * Sets custom overclock ratio. 2/4 by default.
     * Parameters represent number of bit shift, so 1 -> 2x, 2 -> 4x.
     */
    public P setOverclock(int timeReduction, int powerIncrease) {
        this.overClockTimeReduction = timeReduction;
        this.overClockPowerIncrease = powerIncrease;
        return getThis();
    }

    /**
     * Sets overclock ratio to 4/4.
     */
    public P enablePerfectOverclock() {
        return this.setOverclock(2, 2);
    }

    /**
     * Sets whether the multi should use amperage to OC or not
     */
    public P setAmperageOC(boolean amperageOC) {
        this.amperageOC = amperageOC;
        return getThis();
    }

    /**
     * Clears calculated results (and provided machine inputs) to prepare for the next machine operation.
     */
    public P clear() {
        this.calculatedEut = 0;
        this.duration = 0;
        this.calculatedParallels = 0;
        return getThis();
    }

    // #endregion

    // #region Logic

    /**
     * Executes the recipe check: Find recipe from recipemap, Calculate parallel, overclock and outputs.
     */
    @Nonnull
    public abstract CheckRecipeResult process();

    /**
     * Refreshes recipemap to use. Remember to call this before {@link #process} to make sure correct recipemap is used.
     *
     * @return Recipemap to use now
     */
    protected GT_Recipe.GT_Recipe_Map preProcess() {
        GT_Recipe.GT_Recipe_Map recipeMap;
        if (recipeMapSupplier == null) {
            recipeMap = null;
        } else {
            recipeMap = recipeMapSupplier.get();
        }
        if (lastRecipeMap != recipeMap) {
            lastRecipe = null;
            lastRecipeMap = recipeMap;
        }

        if (maxParallelSupplier != null) {
            maxParallel = maxParallelSupplier.get();
        }

        return recipeMap;
    }

    /**
     * {@link FindRecipeResult} can have special validator to check as many recipes as possible to match.
     * This method tries to reuse result from the recipe checks.
     *
     * @return CheckRecipeResult that has meaningful context. Otherwise, null.
     */
    @Nullable
    protected CheckRecipeResult processRecipeValidator(FindRecipeResult findRecipeResult) {
        // If processRecipe is not overridden, advanced recipe validation logic is used, and we can reuse calculations.
        if (!findRecipeResult.hasRecipeValidator()) {
            return null;
        }

        RecipeValidator recipeValidator = findRecipeResult.getRecipeValidator();
        if (!recipeValidator.isExecutedAtLeastOnce()) {
            // We don't have anything to look for.
            return null;
        }

        // There are two cases:
        // 1 - there are actually no matching recipes
        // 2 - there are some matching recipes, but we rejected it due to our advanced validation (e.g. OUTPUT_FULL)
        if (findRecipeResult.getState() == FindRecipeResult.State.NOT_FOUND) {
            // Here we're handling case 2
            // If there are matching recipes but our validation rejected them,
            // we should return a first one to display a proper error in the machine GUI
            return recipeValidator.getFirstCheckResult();
        }

        // If everything is ok, reuse our calculations
        if (findRecipeResult.isSuccessful()) {
            return applyRecipe(
                findRecipeResult.getRecipeNonNull(),
                recipeValidator.getLastParallelHelper(),
                recipeValidator.getLastOverclockCalculator(),
                recipeValidator.getLastCheckResult());
        }

        return null;
    }

    /**
     * Applies the recipe and calculated parameters
     */
    @Nonnull
    protected CheckRecipeResult applyRecipe(@Nonnull GT_Recipe recipe, @Nonnull GT_ParallelHelper helper,
        @Nonnull GT_OverclockCalculator calculator, @Nonnull CheckRecipeResult result) {
        if (!helper.getResult()
            .wasSuccessful()) {
            return helper.getResult();
        }

        if (recipe.mCanBeBuffered) {
            lastRecipe = recipe;
        } else {
            lastRecipe = null;
        }
        calculatedParallels = helper.getCurrentParallel();

        if (calculator.getConsumption() == Long.MAX_VALUE) {
            return CheckRecipeResultRegistry.POWER_OVERFLOW;
        }
        if (calculator.getDuration() == Integer.MAX_VALUE) {
            return CheckRecipeResultRegistry.DURATION_OVERFLOW;
        }

        calculatedEut = calculator.getConsumption();

        double finalDuration = calculateDuration(recipe, helper, calculator);
        if (finalDuration >= Integer.MAX_VALUE) {
            return CheckRecipeResultRegistry.DURATION_OVERFLOW;
        }
        duration = (int) finalDuration;

        outputItems = helper.getItemOutputs();
        outputFluids = helper.getFluidOutputs();

        return result;
    }

    /**
     * Override to tweak final duration that will be set as a result of this logic class.
     */
    protected double calculateDuration(@Nonnull GT_Recipe recipe, @Nonnull GT_ParallelHelper helper,
        @Nonnull GT_OverclockCalculator calculator) {
        return calculator.getDuration() * helper.getDurationMultiplierDouble();
    }

    /**
     * Override to do additional check for finding recipe if needed, mainly for special value of the recipe.
     */
    @Nonnull
    protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /**
     * Override to tweak overclock logic if needed.
     */
    @Nonnull
    protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
        return new GT_OverclockCalculator().setRecipeEUt(recipe.mEUt)
            .setAmperage(availableAmperage)
            .setEUt(availableVoltage)
            .setDuration(recipe.mDuration)
            .setSpeedBoost(speedBoost)
            .setEUtDiscount(euModifier)
            .setAmperageOC(amperageOC)
            .setDurationDecreasePerOC(overClockTimeReduction)
            .setEUtIncreasePerOC(overClockPowerIncrease);
    }

    /**
     * Use {@link #createOverclockCalculator(GT_Recipe)}
     */
    @Nonnull
    @Deprecated
    protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe,
        @Nullable GT_ParallelHelper helper) {
        return createOverclockCalculator(recipe);
    }

    // #endregion

    // #region Getters

    public ItemStack[] getOutputItems() {
        return outputItems;
    }

    public FluidStack[] getOutputFluids() {
        return outputFluids;
    }

    public int getDuration() {
        return duration;
    }

    public long getCalculatedEut() {
        return calculatedEut;
    }

    public int getCurrentParallels() {
        return calculatedParallels;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public P getThis() {
        return (P) this;
    }

    // #endregion
}
