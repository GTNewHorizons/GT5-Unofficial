package gregtech.api.logic;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;

/**
 * Logic class to calculate result of recipe check from inputs, based on recipemap.
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class ProcessingLogic extends AbstractProcessingLogic<ProcessingLogic> {

    protected IRecipeLockable recipeLockableMachine;
    protected ItemStack specialSlotItem;
    protected ItemStack[] inputItems;
    protected FluidStack[] inputFluids;
    protected boolean isRecipeLocked;
    // MuTE Section, do not use for MTEs
    protected boolean hasWork;
    protected int progress;
    @Nonnull
    protected CheckRecipeResult recipeResult = CheckRecipeResultRegistry.NONE;
    @Nullable
    protected UUID itemOutputID;
    @Nullable
    protected UUID fluidOutputID;
    protected boolean muteMode;

    public ProcessingLogic() {}

    // #region Setters

    @Nonnull
    public ProcessingLogic setInputItems(ItemStack... itemInputs) {
        this.inputItems = itemInputs;
        return getThis();
    }

    @Nonnull
    public ProcessingLogic setInputItems(List<ItemStack> itemOutputs) {
        this.inputItems = itemOutputs.toArray(new ItemStack[0]);
        return getThis();
    }

    @Nonnull
    public ProcessingLogic setInputFluids(FluidStack... fluidInputs) {
        this.inputFluids = fluidInputs;
        return getThis();
    }

    @Nonnull
    public ProcessingLogic setInputFluids(List<FluidStack> fluidInputs) {
        this.inputFluids = fluidInputs.toArray(new FluidStack[0]);
        return getThis();
    }

    public ProcessingLogic setSpecialSlotItem(ItemStack specialSlotItem) {
        this.specialSlotItem = specialSlotItem;
        return getThis();
    }

    /**
     * Enables single recipe locking mode.
     */
    public ProcessingLogic setRecipeLocking(IRecipeLockable recipeLockableMachine, boolean isRecipeLocked) {
        this.recipeLockableMachine = recipeLockableMachine;
        this.isRecipeLocked = isRecipeLocked;
        return getThis();
    }

    /**
     * Clears calculated results and provided machine inputs to prepare for the next machine operation.
     */

    public ProcessingLogic clear() {
        this.inputItems = null;
        this.inputFluids = null;
        this.specialSlotItem = null;
        this.outputItems = null;
        this.outputFluids = null;
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
    public CheckRecipeResult process() {
        RecipeMap<?> recipeMap = preProcess();

        if (inputItems == null) {
            inputItems = new ItemStack[0];
        }
        if (inputFluids == null) {
            inputFluids = new FluidStack[0];
        }

        if (isRecipeLocked && recipeLockableMachine != null && recipeLockableMachine.getSingleRecipeCheck() != null) {
            // Recipe checker is already built, we'll use it
            SingleRecipeCheck singleRecipeCheck = recipeLockableMachine.getSingleRecipeCheck();
            // Validate recipe here, otherwise machine will show "not enough output space"
            // even if recipe cannot be found
            if (singleRecipeCheck.checkRecipeInputs(false, 1, inputItems, inputFluids) == 0) {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            return validateAndCalculateRecipe(
                recipeLockableMachine.getSingleRecipeCheck()
                    .getRecipe()).checkRecipeResult;
        }
        // If processRecipe is not overridden, advanced recipe validation logic is used, and we can reuse calculations.
        Stream<GT_Recipe> matchedRecipes = findRecipeMatches(recipeMap);
        Iterable<GT_Recipe> recipeIterable = matchedRecipes::iterator;
        CheckRecipeResult checkRecipeResult = CheckRecipeResultRegistry.NO_RECIPE;
        for (GT_Recipe matchedRecipe : recipeIterable) {
            CalculationResult foundResult = validateAndCalculateRecipe(matchedRecipe);
            if (foundResult.successfullyConsumedInputs) {
                // Successfully found and set recipe, so return it
                return foundResult.checkRecipeResult;
            }
            if (foundResult.checkRecipeResult != CheckRecipeResultRegistry.NO_RECIPE) {
                // Recipe failed in interesting way, so remember that and continue searching
                checkRecipeResult = foundResult.checkRecipeResult;
            }
        }
        return checkRecipeResult;
    }

    /**
     * Checks if supplied recipe is valid for process. This involves voltage check, output full check. If successful,
     * additionally performs input consumption, output calculation with parallel, and overclock calculation.
     *
     * @param recipe The recipe which will be checked and processed
     */
    @Nonnull
    private CalculationResult validateAndCalculateRecipe(@Nonnull GT_Recipe recipe) {
        CheckRecipeResult result = validateRecipe(recipe);
        if (!result.wasSuccessful()) {
            return CalculationResult.ofFailure(result);
        }

        return processRecipe(recipe);
    }

    /**
     * Checks if supplied recipe is valid for process.
     * If so, additionally performs input consumption, output calculation with parallel, and overclock calculation.
     *
     * Use {@link #processRecipe(GT_Recipe, ItemInventoryLogic, FluidInventoryLogic)} for MuTEs
     * 
     * @param recipe The recipe which will be checked and processed
     */
    @Nonnull
    protected CalculationResult processRecipe(@Nonnull GT_Recipe recipe) {
        CheckRecipeResult result = validateRecipe(recipe);
        if (!result.wasSuccessful()) {
            return CalculationResult.ofFailure(result);
        }

        GT_ParallelHelper helper = createParallelHelper(recipe);
        GT_OverclockCalculator calculator = createOverclockCalculator(recipe);
        helper.setCalculator(calculator);
        helper.build();

        return applyRecipe(recipe, helper, calculator, result);
    }

    /**
     * Applies the recipe and calculated parameters
     */
    @Nonnull
    protected CalculationResult applyRecipe(@Nonnull GT_Recipe recipe, @Nonnull GT_ParallelHelper helper,
        @Nonnull GT_OverclockCalculator calculator, @Nonnull CheckRecipeResult result) {
        if (!helper.getResult()
            .wasSuccessful()) {
            return CalculationResult.ofFailure(helper.getResult());
        }

        return CalculationResult.ofSuccess(applyRecipeR(recipe, helper, calculator, result));
    }

    /**
     * Override to tweak final duration that will be set as a result of this logic class.
     */
    protected double calculateDuration(@Nonnull GT_Recipe recipe, @Nonnull GT_ParallelHelper helper,
        @Nonnull GT_OverclockCalculator calculator) {
        return calculator.getDuration() * helper.getDurationMultiplierDouble();
    }

    /**
     * Finds a list of matched recipes. At this point no additional check to the matched recipe has been done.
     * <p>
     * Override {@link #validateRecipe} to have custom check.
     * <p>
     * Override this method if it doesn't work with normal recipemaps.
     */
    @Nonnull
    protected Stream<GT_Recipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
        if (map == null) {
            return Stream.empty();
        }
        return map.findRecipeQuery()
            .items(inputItems)
            .fluids(inputFluids)
            .specialSlot(specialSlotItem)
            .cachedRecipe(lastRecipe)
            .findAll();
    }

    /**
     * Override to do additional check for found recipe if needed.
     */
    @Nonnull
    protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /**
     * Override to tweak parallel logic if needed.
     * Use {@link #createParallelHelper(GT_Recipe, ItemInventoryLogic, FluidInventoryLogic)} for MuTEs
     */
    @Nonnull
    protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe) {
        return new GT_ParallelHelper().setRecipe(recipe)
            .setItemInputs(inputItems)
            .setFluidInputs(inputFluids)
            .setAvailableEUt(availableVoltage * availableAmperage)
            .setMachine(machine, protectItems, protectFluids)
            .setRecipeLocked(recipeLockableMachine, isRecipeLocked)
            .setMaxParallel(maxParallel)
            .setEUtModifier(euModifier)
            .enableBatchMode(batchSize)
            .setConsumption(true)
            .setOutputCalculation(true);
    }

    @Nonnull
    protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe, @Nonnull ItemInventoryLogic itemInput,
        @Nonnull FluidInventoryLogic fluidInput) {
        return new GT_ParallelHelper().setRecipe(recipe)
            .setItemInputInventory(itemInput)
            .setFluidInputInventory(fluidInput)
            .setAvailableEUt(availableVoltage * availableAmperage)
            .setMaxParallel(maxParallel)
            .setEUtModifier(euModifier)
            .enableBatchMode(batchSize)
            .setConsumption(true)
            .setOutputCalculation(true)
            .setMuTEMode(muteMode);
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
     * Override to perform additional logic when recipe starts.
     *
     * This is called when the recipe processing logic has finished all
     * checks, consumed all inputs, but has not yet set the outputs to
     * be produced. Returning a result other than SUCCESSFUL will void
     * all inputs!
     */
    @Nonnull
    protected CheckRecipeResult onRecipeStart(@Nonnull GT_Recipe recipe) {
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    // endregion

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

    // endregion

    /**
     * Represents the status of check recipe calculation. {@link #successfullyConsumedInputs} does not necessarily mean
     * {@link #checkRecipeResult} being successful, when duration or power is overflowed. Being failure means
     * recipe cannot meet requirements and recipe search should be continued if possible.
     */
    protected final static class CalculationResult {

        public final boolean successfullyConsumedInputs;
        public final CheckRecipeResult checkRecipeResult;

        public static CalculationResult ofSuccess(CheckRecipeResult checkRecipeResult) {
            return new CalculationResult(true, checkRecipeResult);
        }

        public static CalculationResult ofFailure(CheckRecipeResult checkRecipeResult) {
            return new CalculationResult(false, checkRecipeResult);
        }

        private CalculationResult(boolean successfullyConsumedInputs, CheckRecipeResult checkRecipeResult) {
            this.successfullyConsumedInputs = successfullyConsumedInputs;
            this.checkRecipeResult = checkRecipeResult;
        }
    }
}
