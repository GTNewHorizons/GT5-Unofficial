package gregtech.api.logic;

import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
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
public class ProcessingLogic<P extends ProcessingLogic<P>> {

    protected IVoidable machine;
    protected IRecipeLockable recipeLockableMachine;
    protected Supplier<RecipeMap<?>> recipeMapSupplier;
    protected GT_Recipe lastRecipe;
    protected RecipeMap<?> lastRecipeMap;
    protected ItemStack specialSlotItem;
    protected ItemStack[] inputItems;
    protected ItemStack[] outputItems;
    protected FluidStack[] inputFluids;
    protected FluidStack[] outputFluids;
    protected long calculatedEut;
    protected int duration;
    protected long availableVoltage;
    protected long availableAmperage;
    protected int overClockTimeReduction = 1;
    protected int overClockPowerIncrease = 2;
    protected boolean protectItems;
    protected boolean protectFluids;
    protected boolean isRecipeLocked;
    protected int maxParallel = 1;
    protected int calculatedParallels = 0;
    protected Supplier<Integer> maxParallelSupplier;
    protected int batchSize = 1;
    protected float euModifier = 1.0f;
    protected float speedBoost = 1.0f;
    protected boolean amperageOC = true;
    protected boolean isCleanroom;
    // MuTE Section, do not use for MTEs
    protected boolean hasWork;
    protected int progress;
    @Nonnull
    protected ProcessingLogicHost<P> machineHost;
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
    public P setInputItems(ItemStack... itemInputs) {
        this.inputItems = itemInputs;
        return getThis();
    }

    @Nonnull
    public P setInputItems(List<ItemStack> itemOutputs) {
        this.inputItems = itemOutputs.toArray(new ItemStack[0]);
        return getThis();
    }

    @Nonnull
    public P setInputFluids(FluidStack... fluidInputs) {
        this.inputFluids = fluidInputs;
        return getThis();
    }

    @Nonnull
    public P setInputFluids(List<FluidStack> fluidInputs) {
        this.inputFluids = fluidInputs.toArray(new FluidStack[0]);
        return getThis();
    }

    public P setSpecialSlotItem(ItemStack specialSlotItem) {
        this.specialSlotItem = specialSlotItem;
        return getThis();
    }

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

    /**
     * Enables single recipe locking mode.
     */
    public P setRecipeLocking(IRecipeLockable recipeLockableMachine, boolean isRecipeLocked) {
        this.recipeLockableMachine = recipeLockableMachine;
        this.isRecipeLocked = isRecipeLocked;
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

    public ProcessingLogic setRecipeMap(RecipeMap<?> recipeMap) {
        return setRecipeMapSupplier(() -> recipeMap);
    }

    public ProcessingLogic setRecipeMapSupplier(Supplier<RecipeMap<?>> supplier) {
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

    public P setMachineHost(@Nonnull ProcessingLogicHost<P> machineHost) {
        this.machineHost = machineHost;
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
    @Nonnull
    public P setCalculatedEut(long calculatedEut) {
        this.calculatedEut = calculatedEut;
        return getThis();
    }

    /**
     * Sets voltage of the machine. It doesn't need to be actual voltage (excluding amperage) of the machine;
     * For example, most of the multiblock machines set maximum possible input power (including amperage) as voltage
     * and 1 as amperage. That way recipemap search will be executed with overclocked voltage.
     */
    @Nonnull
    public P setAvailableVoltage(long voltage) {
        availableVoltage = voltage;
        return getThis();
    }

    /**
     * Sets amperage of the machine. This amperage doesn't involve in EU/t when searching recipemap.
     * Useful for preventing tier skip but still considering amperage for parallel.
     */
    @Nonnull
    public P setAvailableAmperage(long amperage) {
        availableAmperage = amperage;
        return getThis();
    }

    @Nonnull
    public P setVoidProtection(boolean protectItems, boolean protectFluids) {
        this.protectItems = protectItems;
        this.protectFluids = protectFluids;
        return getThis();
    }

    /**
     * Sets custom overclock ratio. 2/4 by default.
     * Parameters represent number of bit shift, so 1 -> 2x, 2 -> 4x.
     */
    @Nonnull
    public P setOverclock(int timeReduction, int powerIncrease) {
        this.overClockTimeReduction = timeReduction;
        this.overClockPowerIncrease = powerIncrease;
        return getThis();
    }

    /**
     * Sets overclock ratio to 4/4.
     */
    @Nonnull
    public P enablePerfectOverclock() {
        return this.setOverclock(2, 2);
    }

    /**
     * Sets wether the multi should use amperage to OC or not
     */
    @Nonnull
    public P setAmperageOC(boolean amperageOC) {
        this.amperageOC = amperageOC;
        return getThis();
    }

    @Nonnull
    public P setMuTEMode(boolean muteMode) {
        this.muteMode = muteMode;
        return getThis();
    }

    /**
     * Clears calculated results and provided machine inputs to prepare for the next machine operation.
     */

    public P clear() {
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
        RecipeMap<?> recipeMap;
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
        if (findRecipeResult.hasRecipeValidator()) {
            RecipeValidator recipeValidator = findRecipeResult.getRecipeValidator();

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
     * If so, additionally performs input consumption, output calculation with parallel, and overclock calculation.
     *
     * Use {@link #processRecipe(GT_Recipe, ItemInventoryLogic, FluidInventoryLogic)} for MuTEs
     * 
     * @param recipe The recipe which will be checked and processed
     */
    @Nonnull
    protected CheckRecipeResult processRecipe(@Nonnull GT_Recipe recipe) {
        CheckRecipeResult result = validateRecipe(recipe);
        if (!result.wasSuccessful()) {
            return result;
        }

        GT_ParallelHelper helper = createParallelHelper(recipe);
        GT_OverclockCalculator calculator = createOverclockCalculator(recipe);
        helper.setCalculator(calculator);
        helper.build();

        return applyRecipe(recipe, helper, calculator, result);
    }

    @Nonnull
    protected CheckRecipeResult processRecipe(@Nonnull List<GT_Recipe> recipes, @Nonnull ItemInventoryLogic itemInput,
        @Nonnull FluidInventoryLogic fluidInput) {
        CheckRecipeResult result = CheckRecipeResultRegistry.INTERNAL_ERROR;
        for (GT_Recipe recipe : recipes) {
            Objects.requireNonNull(recipe);
            GT_ParallelHelper helper = createParallelHelper(recipe, itemInput, fluidInput);
            GT_OverclockCalculator calculator = createOverclockCalculator(recipe);
            helper.setCalculator(calculator);
            helper.build();
            result = helper.getResult();
            if (result.wasSuccessful()) {
                return applyRecipe(recipe, helper, calculator, result);
            }
        }
        return result;
    }

    /**
     * Applies the recipe and calculated parameters
     */
    @Nonnull
    protected CheckRecipeResult applyRecipe(@Nonnull GT_Recipe recipe, @Nonnull GT_ParallelHelper helper,
        @Nonnull GT_OverclockCalculator calculator, @Nonnull CheckRecipeResult result) {
        if (!helper.getResult()
            .wasSuccessful()) {
            return CalculationResult.ofFailure(helper.getResult());
        }

        return CalculationResult.ofSuccess(applyRecipe(recipe, helper, calculator, result));
    }

    /**
     * Check has been succeeded, so it applies the recipe and calculated parameters.
     * At this point, inputs have been already consumed.
     */
    private CheckRecipeResult applyRecipe(@NotNull GT_Recipe recipe, GT_ParallelHelper helper,
        GT_OverclockCalculator calculator, CheckRecipeResult result) {
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

        CheckRecipeResult hookResult = onRecipeStart(recipe);
        if (!hookResult.wasSuccessful()) {
            return hookResult;
        }

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
