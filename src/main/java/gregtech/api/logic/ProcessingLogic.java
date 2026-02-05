package gregtech.api.logic;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.objects.GTDualInputPattern;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.common.tileentities.machines.IDualInputInventoryWithPattern;

/**
 * Logic class to calculate result of recipe check from inputs, based on recipemap.
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class ProcessingLogic {

    // Traits
    protected IVoidable machine;
    protected IRecipeLockable recipeLockableMachine;
    protected boolean isRecipeLocked;
    protected ItemStack[] inputItems;
    protected FluidStack[] inputFluids;
    protected ItemStack specialSlotItem;
    protected int maxParallel = 1;
    protected Supplier<Integer> maxParallelSupplier;
    protected int batchSize = 1;
    protected Supplier<RecipeMap<?>> recipeMapSupplier;
    protected double euModifier = 1.0;
    protected double speedBoost = 1.0;
    protected long availableVoltage;
    protected long availableAmperage;
    protected int maxTierSkips = 1;
    protected boolean protectItems;
    protected boolean protectFluids;
    protected double overClockTimeReduction = 2.0;
    protected double overClockPowerIncrease = 4.0;
    protected boolean amperageOC = true;
    protected boolean recipeCaching = true;

    // Calculated results
    protected ItemStack[] outputItems;
    protected FluidStack[] outputFluids;
    protected long calculatedEut;
    protected int duration;
    protected int calculatedParallels = 0;

    // Cache
    protected RecipeMap<?> lastRecipeMap;
    protected GTRecipe lastRecipe;

    /**
     * The {@link IDualInputInventoryWithPattern} that has any possible recipe found in the last call of
     * {@link #tryCachePossibleRecipesFromPattern(IDualInputInventoryWithPattern)}.
     */
    protected IDualInputInventoryWithPattern activeDualInv;
    /**
     * The cache keyed by the {@link IDualInputInventoryWithPattern}, storing the possible recipes of the inv.
     * <p>
     * The entries can be removed by {@link #removeInventoryRecipeCache(IDualInputInventoryWithPattern)}, and it
     * requires the hatches to actively call it when the inventory is changed (like the pattern is changed).
     * <p>
     * It will also be fully cleared when the {@link #getCurrentRecipeMap()} is not same to the last.
     */
    protected Map<IDualInputInventoryWithPattern, Set<GTRecipe>> dualInvWithPatternToRecipeCache = new HashMap<>();

    public ProcessingLogic() {}

    // region Setters

    /**
     * Sets machine used for void protection logic.
     */
    public ProcessingLogic setMachine(IVoidable machine) {
        this.machine = machine;
        return this;
    }

    /**
     * Enables single recipe locking mode.
     */
    public ProcessingLogic setRecipeLocking(IRecipeLockable recipeLockableMachine, boolean isRecipeLocked) {
        this.recipeLockableMachine = recipeLockableMachine;
        this.isRecipeLocked = isRecipeLocked;
        return this;
    }

    @Nonnull
    public ProcessingLogic setInputItems(ItemStack... itemInputs) {
        this.inputItems = itemInputs;
        return this;
    }

    @Nonnull
    public ProcessingLogic setInputItems(List<ItemStack> itemInputs) {
        this.inputItems = itemInputs.toArray(new ItemStack[0]);
        return this;
    }

    @Nonnull
    public ProcessingLogic setInputFluids(FluidStack... fluidInputs) {
        this.inputFluids = fluidInputs;
        return this;
    }

    @Nonnull
    public ProcessingLogic setInputFluids(List<FluidStack> fluidInputs) {
        this.inputFluids = fluidInputs.toArray(new FluidStack[0]);
        return this;
    }

    public ProcessingLogic setSpecialSlotItem(ItemStack specialSlotItem) {
        this.specialSlotItem = specialSlotItem;
        return this;
    }

    /**
     * Try to cache the possible recipes from the pattern.
     * <p>
     * If the inventory can be cached, and any possible recipe is found, {@link #activeDualInv the active inv} will be
     * set to the given inventory.
     *
     * @return {@code true} if the inv shouldn't be cached, or there is already a cached recipe sets, or the recipes are
     *         cached successfully. {@code false} if there is no recipe found.
     */
    public boolean tryCachePossibleRecipesFromPattern(IDualInputInventoryWithPattern inv) {
        // call getCurrentRecipeMap here to clear dualInv recipe cache if recipe map changes
        RecipeMap<?> recipeMap = getCurrentRecipeMap();

        if (!inv.shouldBeCached()) {
            return true;
        }

        // check existing caches
        if (dualInvWithPatternToRecipeCache.containsKey(inv)) {
            activeDualInv = inv;
            return true;
        }

        // get recipes from the pattern
        GTDualInputPattern inputs = inv.getPatternInputs();
        setInputItems(inputs.inputItems);
        setInputFluids(inputs.inputFluid);
        Set<GTRecipe> recipes = findRecipeMatches(recipeMap).collect(Collectors.toCollection(LinkedHashSet::new));

        // reset the status
        setInputItems();
        setInputFluids();

        if (!recipes.isEmpty()) {
            dualInvWithPatternToRecipeCache.put(inv, recipes);
            activeDualInv = inv;
            return true;
        } else {
            return false;
        }
    }

    public void removeInventoryRecipeCache(IDualInputInventoryWithPattern inv) {
        dualInvWithPatternToRecipeCache.remove(inv);
    }

    /**
     * Sets max amount of parallel.
     */
    public ProcessingLogic setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
        return this;
    }

    /**
     * Sets method to get max amount of parallel.
     */
    public ProcessingLogic setMaxParallelSupplier(Supplier<Integer> supplier) {
        this.maxParallelSupplier = supplier;
        return this;
    }

    /**
     * Sets batch size for batch mode.
     */
    public ProcessingLogic setBatchSize(int size) {
        this.batchSize = size;
        return this;
    }

    public ProcessingLogic setRecipeMap(RecipeMap<?> recipeMap) {
        return setRecipeMapSupplier(() -> recipeMap);
    }

    public ProcessingLogic setRecipeMapSupplier(Supplier<RecipeMap<?>> supplier) {
        this.recipeMapSupplier = supplier;
        return this;
    }

    public ProcessingLogic setEuModifier(double modifier) {
        this.euModifier = modifier;
        return this;
    }

    public ProcessingLogic setSpeedBonus(double speedModifier) {
        this.speedBoost = speedModifier;
        return this;
    }

    /**
     * Sets voltage of the machine. It doesn't need to be actual voltage (excluding amperage) of the machine; For
     * example, most of the multiblock machines set maximum possible input power (including amperage) as voltage and 1
     * as amperage. That way recipemap search will be executed with overclocked voltage.
     */
    public ProcessingLogic setAvailableVoltage(long voltage) {
        this.availableVoltage = voltage;
        return this;
    }

    /**
     * Sets amperage of the machine. This amperage doesn't involve in EU/t when searching recipemap. Useful for
     * preventing tier skip but still considering amperage for parallel.
     */
    public ProcessingLogic setAvailableAmperage(long amperage) {
        this.availableAmperage = amperage;
        return this;
    }

    /**
     * Sets the max amount of tier skips, which is how many voltage tiers above the input voltage a recipe is valid. For
     * unlimited tier skips, use {@link #setUnlimitedTierSkips()}
     */
    public ProcessingLogic setMaxTierSkips(int tierSkips) {
        this.maxTierSkips = tierSkips;
        return this;
    }

    public ProcessingLogic setUnlimitedTierSkips() {
        this.maxTierSkips = Integer.MAX_VALUE;
        return this;
    }

    public ProcessingLogic setVoidProtection(boolean protectItems, boolean protectFluids) {
        this.protectItems = protectItems;
        this.protectFluids = protectFluids;
        return this;
    }

    public ProcessingLogic setOverclock(double timeReduction, double powerIncrease) {
        this.overClockTimeReduction = timeReduction;
        this.overClockPowerIncrease = powerIncrease;
        return this;
    }

    /**
     * Sets overclock ratio to 4/4.
     */
    public ProcessingLogic enablePerfectOverclock() {
        return this.setOverclock(4.0, 4.0);
    }

    /**
     * Sets whether the multi should use amperage to OC or not.
     */
    public ProcessingLogic setAmperageOC(boolean amperageOC) {
        this.amperageOC = amperageOC;
        return this;
    }

    /**
     * Disable caching of matched recipes.
     */
    public ProcessingLogic noRecipeCaching() {
        this.recipeCaching = false;
        return this;
    }

    // endregion

    // region Overwrite calculated result

    /**
     * Overwrites calculated item output.
     */
    public ProcessingLogic overwriteOutputItems(ItemStack... itemOutputs) {
        this.outputItems = itemOutputs;
        return this;
    }

    /**
     * Overwrites calculated fluid output.
     */
    public ProcessingLogic overwriteOutputFluids(FluidStack... fluidOutputs) {
        this.outputFluids = fluidOutputs;
        return this;
    }

    /**
     * Overwrites calculated EU/t.
     */
    public ProcessingLogic overwriteCalculatedEut(long calculatedEut) {
        this.calculatedEut = calculatedEut;
        return this;
    }

    /**
     * Overwrites calculated duration.
     */
    public ProcessingLogic overwriteCalculatedDuration(int duration) {
        this.duration = duration;
        return this;
    }

    // endregion

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
        this.activeDualInv = null;
        return this;
    }

    // region Logic

    /**
     * Refreshes recipemap to use. Remember to call this before {@link #process} to make sure correct recipemap is used.
     *
     * @return Recipemap to use now
     */
    protected RecipeMap<?> getCurrentRecipeMap() {
        RecipeMap<?> recipeMap;
        if (recipeMapSupplier == null) {
            recipeMap = null;
        } else {
            recipeMap = recipeMapSupplier.get();
        }
        if (lastRecipeMap != recipeMap) {
            if (lastRecipeMap != null) {
                dualInvWithPatternToRecipeCache.clear();
            }
            lastRecipe = null;
            lastRecipeMap = recipeMap;
        }
        return recipeMap;
    }

    /**
     * Executes the recipe check: Find recipe from recipemap, Calculate parallel, overclock and outputs.
     */
    @Nonnull
    public CheckRecipeResult process() {
        RecipeMap<?> recipeMap = getCurrentRecipeMap();

        if (maxParallelSupplier != null) {
            maxParallel = maxParallelSupplier.get();
        }

        if (inputItems == null) {
            inputItems = GTValues.emptyItemStackArray;
        }
        if (inputFluids == null) {
            inputFluids = GTValues.emptyFluidStackArray;
        }

        if (activeDualInv != null) {
            Set<GTRecipe> matchedRecipes = dualInvWithPatternToRecipeCache.get(activeDualInv);
            for (GTRecipe matchedRecipe : matchedRecipes) {
                if (matchedRecipe.maxParallelCalculatedByInputs(1, inputFluids, inputItems) == 1) {
                    CalculationResult foundResult = validateAndCalculateRecipe(matchedRecipe);
                    return foundResult.checkRecipeResult;
                }
            }
            activeDualInv = null;
            return CheckRecipeResultRegistry.NO_RECIPE;
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
        Stream<GTRecipe> matchedRecipes = findRecipeMatches(recipeMap);
        Iterable<GTRecipe> recipeIterable = matchedRecipes::iterator;
        CheckRecipeResult checkRecipeResult = CheckRecipeResultRegistry.NO_RECIPE;
        for (GTRecipe matchedRecipe : recipeIterable) {
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
    private CalculationResult validateAndCalculateRecipe(@Nonnull GTRecipe recipe) {
        CheckRecipeResult result = validateRecipe(recipe);
        if (!result.wasSuccessful()) {
            return CalculationResult.ofFailure(result);
        }

        ParallelHelper helper = createParallelHelper(recipe);
        OverclockCalculator calculator = createOverclockCalculator(recipe);
        helper.setCalculator(calculator);
        helper.build();

        if (!helper.getResult()
            .wasSuccessful()) {
            return CalculationResult.ofFailure(helper.getResult());
        }

        return CalculationResult.ofSuccess(applyRecipe(recipe, helper, calculator, result));
    }

    /**
     * Check has been succeeded, so it applies the recipe and calculated parameters. At this point, inputs have been
     * already consumed.
     */
    @Nonnull
    protected CheckRecipeResult applyRecipe(@Nonnull GTRecipe recipe, @Nonnull ParallelHelper helper,
        @Nonnull OverclockCalculator calculator, @Nonnull CheckRecipeResult result) {
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
    protected double calculateDuration(@Nonnull GTRecipe recipe, @Nonnull ParallelHelper helper,
        @Nonnull OverclockCalculator calculator) {
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
    protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
        if (map == null) {
            return Stream.empty();
        }
        return map.findRecipeQuery()
            .caching(recipeCaching)
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
    protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /**
     * Override to tweak parallel logic if needed.
     */
    @Nonnull
    protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
        return new ParallelHelper().setRecipe(recipe)
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

    /**
     * Override to tweak overclock logic if needed.
     */
    @Nonnull
    protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
        return new OverclockCalculator().setRecipeEUt(recipe.mEUt)
            .setAmperage(availableAmperage)
            .setEUt(availableVoltage)
            .setMaxTierSkips(maxTierSkips)
            .setDuration(recipe.mDuration)
            .setDurationModifier(speedBoost)
            .setEUtDiscount(euModifier)
            .setAmperageOC(amperageOC)
            .setDurationDecreasePerOC(overClockTimeReduction)
            .setEUtIncreasePerOC(overClockPowerIncrease);
    }

    /**
     * Override to perform additional logic when recipe starts.
     * <p>
     * This is called when the recipe processing logic has finished all checks, consumed all inputs, but has not yet set
     * the outputs to be produced. Returning a result other than SUCCESSFUL will void all inputs!
     */
    @Nonnull
    protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    // endregion

    // region Getters

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
     * {@link #checkRecipeResult} being successful, when duration or power is overflowed. Being failure means recipe
     * cannot meet requirements and recipe search should be continued if possible.
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
