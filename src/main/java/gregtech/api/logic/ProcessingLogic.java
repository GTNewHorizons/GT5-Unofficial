package gregtech.api.logic;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.FindRecipeResult;
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
    protected ItemStack[] inputItems;
    protected ItemStack[] outputItems;
    protected ItemStack[] currentOutputItems;
    protected FluidStack[] inputFluids;
    protected FluidStack[] outputFluids;
    protected FluidStack[] currentOutputFluids;
    protected long calculatedEut;
    protected long duration;
    protected long availableVoltage;
    protected long availableAmperage;
    protected int overClockTimeReduction = 2;
    protected int overClockPowerIncrease = 4;
    protected boolean protectItems;
    protected boolean protectFluids;
    protected boolean isRecipeLocked;
    protected int maxParallel = 1;
    protected int calculatedParallels = 0;
    protected Supplier<Integer> maxParallelSupplier;
    protected int batchSize = 1;

    public ProcessingLogic() {}

    // === Setters ===

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

    /**
     * Sets machine used for void protection logic (namely, parallel logic.)
     */
    public ProcessingLogic setMachine(IVoidable machine) {
        this.machine = machine;
        return this;
    }

    /**
     * Overwrites duration result of the calculation.
     */
    public ProcessingLogic setDuration(long duration) {
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
     * Sets custom overclock ratio. By default, it's 2/4.
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
        return this.setOverclock(4, 4);
    }

    /**
     * Clears calculated results and provided machine inputs to prepare for the next machine operation.
     */
    public ProcessingLogic clear() {
        this.inputItems = null;
        this.inputFluids = null;
        this.outputItems = null;
        this.outputFluids = null;
        this.calculatedEut = 0;
        this.duration = 0;
        this.calculatedParallels = 0;
        return this;
    }

    // === Logic ===

    /**
     * Executes the recipe check: Find recipe from recipemap, Calculate parallel, overclock and outputs.
     */
    @Nonnull
    public CheckRecipeResult process() {
        if (recipeMapSupplier == null) return CheckRecipeResultRegistry.NO_RECIPE;

        GT_Recipe_Map recipeMap = recipeMapSupplier.get();
        if (recipeMap == null) return CheckRecipeResultRegistry.NO_RECIPE;

        if (maxParallelSupplier != null) {
            maxParallel = maxParallelSupplier.get();
        }

        FindRecipeResult findRecipeResult;

        if (isRecipeLocked && recipeLockableMachine != null && recipeLockableMachine.getSingleRecipeCheck() != null) {
            findRecipeResult = FindRecipeResult.ofSuccess(
                recipeLockableMachine.getSingleRecipeCheck()
                    .getRecipe());
        } else {
            findRecipeResult = recipeMap
                .findRecipeWithResult(lastRecipe, false, false, availableVoltage, inputFluids, null, inputItems);
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

        if (helper == null) return CheckRecipeResultRegistry.NO_RECIPE;

        helper.build();

        if (helper.getCurrentParallel() <= 0) return CheckRecipeResultRegistry.OUTPUT_FULL;

        calculatedParallels = helper.getCurrentParallel();

        GT_OverclockCalculator calculator = createOverclockCalculator(recipe, helper);

        // We allow OC calculator to be null. If so we don't OC.
        if (calculator == null) {
            calculatedEut = recipe.mEUt;
            duration = recipe.mDuration;
        } else {
            calculator.calculate();
            if (calculator.getConsumption() == Long.MAX_VALUE - 1
                || calculator.getDuration() == Integer.MAX_VALUE - 1) {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            calculatedEut = calculator.getConsumption();
            duration = calculator.getDuration();
        }

        outputItems = helper.getItemOutputs();
        outputFluids = helper.getFluidOutputs();

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /**
     * Override to tweak parallel logic if needed.
     */
    protected GT_ParallelHelper createParallelHelper(GT_Recipe recipe) {
        return new GT_ParallelHelper().setRecipe(recipe)
            .setItemInputs(inputItems)
            .setFluidInputs(inputFluids)
            .setAvailableEUt(availableVoltage * availableAmperage)
            .setMachine(machine, protectItems, protectFluids)
            .setRecipeLocked(recipeLockableMachine, isRecipeLocked)
            .setMaxParallel(maxParallel)
            .enableBatchMode(batchSize)
            .enableConsumption()
            .enableOutputCalculation();
    }

    /**
     * Override to do additional check for finding recipe if needed, mainly for special value of the recipe.
     */
    @Nonnull
    protected CheckRecipeResult validateRecipe(GT_Recipe recipe) {
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /**
     * Override to tweak overclock logic if needed.
     */
    protected GT_OverclockCalculator createOverclockCalculator(GT_Recipe recipe, GT_ParallelHelper helper) {
        return new GT_OverclockCalculator().setRecipeEUt(recipe.mEUt)
            .setParallel((int) Math.floor(helper.getCurrentParallel() / helper.getDurationMultiplier()))
            .setDuration(recipe.mDuration)
            .setAmperage(availableAmperage)
            .setEUt(availableVoltage)
            .setDurationDecreasePerOC(overClockTimeReduction)
            .setEUtIncreasePerOC(overClockPowerIncrease);
    }

    // === Getters ===

    public ItemStack[] getOutputItems() {
        return outputItems;
    }

    public FluidStack[] getOutputFluids() {
        return outputFluids;
    }

    public long getDuration() {
        return duration;
    }

    public long getCalculatedEut() {
        return calculatedEut;
    }
}
