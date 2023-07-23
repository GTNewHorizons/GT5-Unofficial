package gregtech.api.logic;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

/**
 * Logic class to calculate result of recipe check from inputs, based on recipemap.
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class ProcessingLogic {

    protected IVoidable machine;
    protected IRecipeLockable recipeLockableMachine;
    protected Supplier<GT_Recipe_Map> recipeMapSupplier;
    protected GT_Recipe lastRecipe;
    protected GT_Recipe_Map lastRecipeMap;
    protected ItemStack specialSlotItem;
    protected ItemStack[] inputItems;
    protected ItemStack[] outputItems;
    protected ItemStack[] currentOutputItems;
    protected FluidStack[] inputFluids;
    protected FluidStack[] outputFluids;
    protected FluidStack[] currentOutputFluids;
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

    public ProcessingLogic() {}

    // region Setters

    public ProcessingLogic setInputItems(ItemStack... itemInputs) {
        this.inputItems = itemInputs;
        return this;
    }

    public ProcessingLogic setInputItems(List<ItemStack> itemOutputs) {
        this.inputItems = itemOutputs.toArray(new ItemStack[0]);
        return this;
    }

    public ProcessingLogic setInputFluids(FluidStack... fluidInputs) {
        this.inputFluids = fluidInputs;
        return this;
    }

    public ProcessingLogic setInputFluids(List<FluidStack> fluidInputs) {
        this.inputFluids = fluidInputs.toArray(new FluidStack[0]);
        return this;
    }

    public ProcessingLogic setSpecialSlotItem(ItemStack specialSlotItem) {
        this.specialSlotItem = specialSlotItem;
        return this;
    }

    /**
     * Overwrites item output result of the calculation.
     */
    public ProcessingLogic setOutputItems(ItemStack... itemOutputs) {
        this.outputItems = itemOutputs;
        return this;
    }

    /**
     * Overwrites fluid output result of the calculation.
     */
    public ProcessingLogic setOutputFluids(FluidStack... fluidOutputs) {
        this.outputFluids = fluidOutputs;
        return this;
    }

    public ProcessingLogic setCurrentOutputItems(ItemStack... currentOutputItems) {
        this.currentOutputItems = currentOutputItems;
        return this;
    }

    public ProcessingLogic setCurrentOutputFluids(FluidStack... currentOutputFluids) {
        this.currentOutputFluids = currentOutputFluids;
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

    public ProcessingLogic setRecipeMap(GT_Recipe_Map recipeMap) {
        return setRecipeMapSupplier(() -> recipeMap);
    }

    public ProcessingLogic setRecipeMapSupplier(Supplier<GT_Recipe_Map> supplier) {
        this.recipeMapSupplier = supplier;
        return this;
    }

    public ProcessingLogic setEuModifier(float modifier) {
        this.euModifier = modifier;
        return this;
    }

    public ProcessingLogic setSpeedBonus(float speedModifier) {
        this.speedBoost = speedModifier;
        return this;
    }

    /**
     * Sets machine used for void protection logic.
     */
    public ProcessingLogic setMachine(IVoidable machine) {
        this.machine = machine;
        return this;
    }

    /**
     * Overwrites duration result of the calculation.
     */
    public ProcessingLogic setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Overwrites EU/t result of the calculation.
     */
    public ProcessingLogic setCalculatedEut(long calculatedEut) {
        this.calculatedEut = calculatedEut;
        return this;
    }

    /**
     * Sets voltage of the machine. It doesn't need to be actual voltage (excluding amperage) of the machine;
     * For example, most of the multiblock machines set maximum possible input power (including amperage) as voltage
     * and 1 as amperage. That way recipemap search will be executed with overclocked voltage.
     */
    public ProcessingLogic setAvailableVoltage(long voltage) {
        availableVoltage = voltage;
        return this;
    }

    /**
     * Sets amperage of the machine. This amperage doesn't involve in EU/t when searching recipemap.
     * Useful for preventing tier skip but still considering amperage for parallel.
     */
    public ProcessingLogic setAvailableAmperage(long amperage) {
        availableAmperage = amperage;
        return this;
    }

    public ProcessingLogic setVoidProtection(boolean protectItems, boolean protectFluids) {
        this.protectItems = protectItems;
        this.protectFluids = protectFluids;
        return this;
    }

    /**
     * Sets custom overclock ratio. 2/4 by default.
     * Parameters represent number of bit shift, so 1 -> 2x, 2 -> 4x.
     */
    public ProcessingLogic setOverclock(int timeReduction, int powerIncrease) {
        this.overClockTimeReduction = timeReduction;
        this.overClockPowerIncrease = powerIncrease;
        return this;
    }

    /**
     * Sets overclock ratio to 4/4.
     */
    public ProcessingLogic enablePerfectOverclock() {
        return this.setOverclock(2, 2);
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
        return this;
    }

    // endregion

    // region Logic

    /**
     * Executes the recipe check: Find recipe from recipemap, Calculate parallel, overclock and outputs.
     */
    @Nonnull
    public CheckRecipeResult process() {
        GT_Recipe_Map recipeMap;
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

        FindRecipeResult findRecipeResult;
        if (isRecipeLocked && recipeLockableMachine != null && recipeLockableMachine.getSingleRecipeCheck() != null) {
            // Recipe checker is already built, we'll use it
            SingleRecipeCheck singleRecipeCheck = recipeLockableMachine.getSingleRecipeCheck();
            // Validate recipe here, otherwise machine will show "not enough output space"
            // even if recipe cannot be found
            if (singleRecipeCheck.checkRecipeInputs(false, 1, inputItems, inputFluids) == 0) {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }
            findRecipeResult = FindRecipeResult.ofSuccess(
                recipeLockableMachine.getSingleRecipeCheck()
                    .getRecipe());
        } else {
            findRecipeResult = findRecipe(recipeMap);
        }

        GT_Recipe recipe;
        if (findRecipeResult.isSuccessful()) {
            recipe = findRecipeResult.getRecipeNonNull();
            CheckRecipeResult result = validateRecipe(recipe);
            if (!result.wasSuccessful()) {
                return result;
            } else {
                lastRecipe = recipe;
            }
        } else {
            if (findRecipeResult.getState() == FindRecipeResult.State.INSUFFICIENT_VOLTAGE) {
                return CheckRecipeResultRegistry.insufficientPower(findRecipeResult.getRecipeNonNull().mEUt);
            } else {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }
        }

        GT_ParallelHelper helper = createParallelHelper(recipe);

        helper.build();

        if (helper.getCurrentParallel() <= 0) return CheckRecipeResultRegistry.OUTPUT_FULL;

        calculatedParallels = helper.getCurrentParallel();

        GT_OverclockCalculator calculator = createOverclockCalculator(recipe, helper);

        calculator.calculate();
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

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /**
     * Override to tweak final duration that will be set as a result of this logic class.
     */
    protected double calculateDuration(@Nonnull GT_Recipe recipe, @Nonnull GT_ParallelHelper helper,
        @Nonnull GT_OverclockCalculator calculator) {
        return calculator.getDuration() * helper.getDurationMultiplierDouble();
    }

    /**
     * Override if you don't work with regular gt recipe maps
     */
    @Nonnull
    protected FindRecipeResult findRecipe(@Nullable GT_Recipe_Map map) {
        if (map == null) return FindRecipeResult.NOT_FOUND;
        return map
            .findRecipeWithResult(lastRecipe, false, false, availableVoltage, inputFluids, specialSlotItem, inputItems);
    }

    /**
     * Override to tweak parallel logic if needed.
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
            .enableConsumption()
            .enableOutputCalculation();
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
    protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe,
        @Nonnull GT_ParallelHelper helper) {
        return new GT_OverclockCalculator().setRecipeEUt(recipe.mEUt)
            .setParallel((int) Math.floor(helper.getCurrentParallel() / helper.getDurationMultiplierDouble()))
            .setDuration(recipe.mDuration)
            .setAmperage(availableAmperage)
            .setEUt(availableVoltage)
            .setSpeedBoost(speedBoost)
            .setEUtDiscount(euModifier)
            .setDurationDecreasePerOC(overClockTimeReduction)
            .setEUtIncreasePerOC(overClockPowerIncrease);
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
}
