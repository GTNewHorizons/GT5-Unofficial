package gregtech.api.logic;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.InventoryType;
import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
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
public class ProcessingLogic<T extends ProcessingLogic<T>> {

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
    protected boolean amperageOC = true;
    protected boolean isCleanroom;
    // MuTE Section, do not use for MTEs
    protected boolean hasWork;
    protected int progress;
    protected ProcessingLogicHost<T> machineHost;
    protected CheckRecipeResult recipeResult;
    protected UUID itemOutputID;
    protected UUID fluidOutputID;

    public ProcessingLogic() {}

    // #region Setters

    @Nonnull
    public T setInputItems(ItemStack... itemInputs) {
        this.inputItems = itemInputs;
        return getThis();
    }

    @Nonnull
    public T setInputItems(List<ItemStack> itemOutputs) {
        this.inputItems = itemOutputs.toArray(new ItemStack[0]);
        return getThis();
    }

    @Nonnull
    public T setInputFluids(FluidStack... fluidInputs) {
        this.inputFluids = fluidInputs;
        return getThis();
    }

    @Nonnull
    public T setInputFluids(List<FluidStack> fluidInputs) {
        this.inputFluids = fluidInputs.toArray(new FluidStack[0]);
        return getThis();
    }

    public T setSpecialSlotItem(ItemStack specialSlotItem) {
        this.specialSlotItem = specialSlotItem;
        return getThis();
    }

    /**
     * Overwrites item output result of the calculation.
     */
    public T setOutputItems(ItemStack... itemOutputs) {
        this.outputItems = itemOutputs;
        return getThis();
    }

    /**
     * Overwrites fluid output result of the calculation.
     */
    public T setOutputFluids(FluidStack... fluidOutputs) {
        this.outputFluids = fluidOutputs;
        return getThis();
    }

    public T setCurrentOutputItems(ItemStack... currentOutputItems) {
        this.currentOutputItems = currentOutputItems;
        return getThis();
    }

    public T setCurrentOutputFluids(FluidStack... currentOutputFluids) {
        this.currentOutputFluids = currentOutputFluids;
        return getThis();
    }

    /**
     * Enables single recipe locking mode.
     */
    public T setRecipeLocking(IRecipeLockable recipeLockableMachine, boolean isRecipeLocked) {
        this.recipeLockableMachine = recipeLockableMachine;
        this.isRecipeLocked = isRecipeLocked;
        return getThis();
    }

    public T setIsCleanroom(boolean isCleanroom) {
        this.isCleanroom = isCleanroom;
        return getThis();
    }

    /**
     * Sets max amount of parallel.
     */
    public T setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
        return getThis();
    }

    /**
     * Sets method to get max amount of parallel.
     */
    public T setMaxParallelSupplier(Supplier<Integer> supplier) {
        this.maxParallelSupplier = supplier;
        return getThis();
    }

    /**
     * Sets batch size for batch mode.
     */
    public T setBatchSize(int size) {
        this.batchSize = size;
        return getThis();
    }

    public T setRecipeMap(GT_Recipe_Map recipeMap) {
        return setRecipeMapSupplier(() -> recipeMap);
    }

    public T setRecipeMapSupplier(Supplier<GT_Recipe_Map> supplier) {
        this.recipeMapSupplier = supplier;
        return getThis();
    }

    public T setEuModifier(float modifier) {
        this.euModifier = modifier;
        return getThis();
    }

    public T setSpeedBonus(float speedModifier) {
        this.speedBoost = speedModifier;
        return getThis();
    }

    /**
     * Sets machine used for void protection logic.
     */
    public T setMachine(IVoidable machine) {
        this.machine = machine;
        return getThis();
    }

    /**
     * Overwrites duration result of the calculation.
     */
    public T setDuration(int duration) {
        this.duration = duration;
        return getThis();
    }

    /**
     * Overwrites EU/t result of the calculation.
     */
    public T setCalculatedEut(long calculatedEut) {
        this.calculatedEut = calculatedEut;
        return getThis();
    }

    /**
     * Sets voltage of the machine. It doesn't need to be actual voltage (excluding amperage) of the machine;
     * For example, most of the multiblock machines set maximum possible input power (including amperage) as voltage
     * and 1 as amperage. That way recipemap search will be executed with overclocked voltage.
     */
    public T setAvailableVoltage(long voltage) {
        availableVoltage = voltage;
        return getThis();
    }

    /**
     * Sets amperage of the machine. This amperage doesn't involve in EU/t when searching recipemap.
     * Useful for preventing tier skip but still considering amperage for parallel.
     */
    public T setAvailableAmperage(long amperage) {
        availableAmperage = amperage;
        return getThis();
    }

    public T setVoidProtection(boolean protectItems, boolean protectFluids) {
        this.protectItems = protectItems;
        this.protectFluids = protectFluids;
        return getThis();
    }

    /**
     * Sets custom overclock ratio. 2/4 by default.
     * Parameters represent number of bit shift, so 1 -> 2x, 2 -> 4x.
     */
    public T setOverclock(int timeReduction, int powerIncrease) {
        this.overClockTimeReduction = timeReduction;
        this.overClockPowerIncrease = powerIncrease;
        return getThis();
    }

    /**
     * Sets overclock ratio to 4/4.
     */
    public T enablePerfectOverclock() {
        return this.setOverclock(2, 2);
    }

    /**
     * Sets wether the multi should use amperage to OC or not
     */
    public T setAmperageOC(boolean amperageOC) {
        this.amperageOC = amperageOC;
        return getThis();
    }

    /**
     * Clears calculated results and provided machine inputs to prepare for the next machine operation.
     */
    public T clear() {
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
        CheckRecipeResult result;
        if (findRecipeResult.isSuccessful()) {
            recipe = findRecipeResult.getRecipeNonNull();
            result = validateRecipe(recipe);
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
        GT_OverclockCalculator calculator = createOverclockCalculator(recipe);
        helper.setCalculator(calculator);
        helper.build();

        if (!helper.getResult()
            .wasSuccessful()) {
            return helper.getResult();
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
     * Override if you don't work with regular gt recipe maps
     */
    @Nonnull
    protected FindRecipeResult findRecipe(@Nullable GT_Recipe_Map map) {
        if (map == null) return FindRecipeResult.NOT_FOUND;
        return map.findRecipeWithResult(
            lastRecipe,
            false,
            false,
            amperageOC ? availableVoltage * availableAmperage : availableVoltage,
            inputFluids,
            specialSlotItem,
            inputItems);
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
            .setConsumption(true)
            .setOutputCalculation(true);
    }

    /**
     * Override to do additional check for finding recipe if needed, mainly for special value of the recipe.
     */
    @Nonnull
    protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
        return CheckRecipeResultRegistry.SUCCESSFUL;
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

    // #endregion

    // #region Other

    @Nonnull
    public T getThis() {
        return (T) this;
    }

    public void startCheck() {
        recipeResult = process();
    }

    public void progress() {
        if (!hasWork) return;
        if (progress == duration) {
            progress = 0;
            duration = 0;
            calculatedEut = 0;
            output();
            return;
        }
        progress++;
    }

    protected void output() {
        ItemInventoryLogic itemOutput = machineHost.getItemLogic(InventoryType.Output, itemOutputID);
        FluidInventoryLogic fluidOutput = machineHost.getFluidLogic(InventoryType.Output, fluidOutputID);
        if (itemOutput == null || fluidOutput == null) return;
        for (ItemStack item : outputItems) {
            if (item == null) continue;
            itemOutput.insertItem(item);
        }
        for (FluidStack fluid : outputFluids) {
            if (fluid == null) continue;
            fluidOutput.fill(fluid.getFluid(), fluid.amount, false);
        }
    }

    public boolean canWork() {
        return !hasWork;
    }
    // #endregion
}
