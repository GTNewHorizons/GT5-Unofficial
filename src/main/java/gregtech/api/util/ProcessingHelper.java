package gregtech.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class ProcessingHelper {
    private static final double MAX_BATCH_MODE_TICK_TIME = 128;
    private static final int HEAT_DISCOUNT_THRESHOLD = 900;
    private static final double OC_EUT_MODIFIER = 4;
    protected static final double OC_DURATION_MODIFIER = 0.5;
    /**
     * Machine used for calculation
     */
    private IVoidable machine;
    /**
     * Machine used for single recipe locking calculation
     */
    private IRecipeLockable singleRecipeMachine;
    /**
     * Is locked to a single recipe?
     */
    private boolean isRecipeLocked;
    /**
     * Recipe used when trying to calculate parallels
     */
    private GTRecipe recipe;
    /**
     * EUt available to the multiblock (This should be the total eut available)
     */
    private long availableEUt;
    /**
     * The current parallel possible for the multiblock
     */
    private int currentParallel = 0;
    /**
     * The maximum possible parallel possible for the multiblock
     */
    private int maxParallels = 1;
    /**
     * The Batch Modifier applied when batch mode is enabled. 1 does nothing. 2 doubles max possible
     * parallel, but also duration
     */
    private int batchModifier = 1;
    /**
     * The inputs of the multiblock for the current recipe check
     */
    private ItemStack[] itemInputs;
    /**
     * The outputs of the recipe with the applied parallel
     */
    private ItemStack[] itemOutputs;
    /**
     * The inputs of the multiblock for the current recipe check
     */
    private FluidStack[] fluidInputs;
    /**
     * The outputs of the recipe with the applied parallel
     */
    private FluidStack[] fluidOutputs;
    /**
     * Does the multi have void protection enabled for items
     */
    private boolean protectExcessItem;
    /**
     * Does the multi have void protection enabled for fluids
     */
    private boolean protectExcessFluid;
    /**
     * Should the Parallel Helper automatically consume for the multi
     */
    private boolean consume;
    /**
     * Is batch mode turned on?
     */
    private boolean batchMode;
    /**
     * Should the Parallel Helper automatically calculate the outputs of the recipe with current parallel?
     */
    private boolean calculateOutputs;
    /**
     * Has the Parallel Helper been built?
     */
    private boolean built;
    /**
     * What is the duration multiplier with batch mode enabled
     */
    private double durationMultiplier;
    /**
     * Modifier which is applied on the recipe eut. Useful for GT++ machines
     */
    private double eutModifier = 1;
    /**
     * Modifier which is applied the recipe duration. Useful for GT++ machines
     */
    private double durationModifier = 1;
    /**
     * A supplier used for machines which have a custom way of calculating base duration, like Neutron Activator
     */
    private Function<GTRecipe, Double> durationSupplier;
    /**
     * Does this machine perform overclocks?
     */
    private boolean overclock;
    protected Function<Integer, Double> ocEUtModifierSupplier;
    protected Function<Integer, Double> ocDurationModifierSupplier;
    /**
     * Multiplier that is applied on the output chances
     */
    private double chanceMultiplier = 1;
    /**
     * Multiplier by which the output will be multiplied
     */
    private int outputMultiplier = 1;
    /**
     * Are perfect heat overclocks enabled?
     */
    private boolean heatOC;
    /**
     * Are heat discounts enabled?
     */
    private boolean heatDiscount;
    /**
     * Energy discount if heatDiscount is used. Applied for every 900 degrees above the recipe requirement.
     */
    private double heatDiscountMultiplier;
    /**
     * Heat of the machine used for processing.
     */
    private int machineHeat;
    /**
     * Method for calculating max parallel from given inputs.
     */
    private MaxParallelCalculator maxParallelCalculator = GTRecipe::maxParallelCalculatedByInputs;
    /**
     * Method for consuming inputs after determining how many parallels it can execute.
     */
    private InputConsumer inputConsumer = GTRecipe::consumeInput;

    @Nonnull
    private CheckRecipeResult result = CheckRecipeResultRegistry.NONE;

    private Function<Integer, ItemStack[]> customItemOutputCalculation;

    private Function<Integer, FluidStack[]> customFluidOutputCalculation;

    public ProcessingHelper() {}

    /**
     * Sets machine, with current configuration for void protection mode.
     */
    @Nonnull
    public ProcessingHelper setMachine(IVoidable machine) {
        return setMachine(machine, machine.protectsExcessItem(), machine.protectsExcessFluid());
    }

    /**
     * Sets machine, with void protection mode forcibly.
     */
    @Nonnull
    public ProcessingHelper setMachine(IVoidable machine, boolean protectExcessItem, boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.machine = machine;
        return this;
    }

    /**
     * Sets the recipe, which will be used for the parallel calculation
     */
    @Nonnull
    public ProcessingHelper setRecipe(@Nonnull GTRecipe aRecipe) {
        recipe = Objects.requireNonNull(aRecipe);
        return this;
    }

    @Nonnull
    public ProcessingHelper setRecipeLocked(IRecipeLockable singleRecipeMachine, boolean isRecipeLocked) {
        this.singleRecipeMachine = singleRecipeMachine;
        this.isRecipeLocked = isRecipeLocked;
        return this;
    }

    /**
     * Sets the items available for the recipe check
     */
    @Nonnull
    public ProcessingHelper setItemInputs(ItemStack... aItemInputs) {
        this.itemInputs = aItemInputs;
        return this;
    }

    /**
     * Sets the fluid inputs available for the recipe check
     */
    @Nonnull
    public ProcessingHelper setFluidInputs(FluidStack... aFluidInputs) {
        this.fluidInputs = aFluidInputs;
        return this;
    }

    /**
     * Sets the available eut when trying for more parallels
     */
    @Nonnull
    public ProcessingHelper setAvailableEUt(long aAvailableEUt) {
        this.availableEUt = aAvailableEUt;
        return this;
    }

    /**
     * Sets the modifier for recipe eut. 1 does nothing 0.9 is 10% less. 1.1 is 10% more
     */
    @Nonnull
    public ProcessingHelper setEUtModifier(double aEUtModifier) {
        this.eutModifier = aEUtModifier;
        return this;
    }

    /**
     * Sets the modifier for recipe duration. 1 does nothing, 2 is twice as slow, 0.5 is twice as fast
     */
    @Nonnull
    public ProcessingHelper setDurationModifier(double durationModifier) {
        this.durationModifier = durationModifier;
        return this;
    }

    /**
     * Set the supplier used for machines which have a custom way of calculating base duration, like Neutron Activator
     */
    @Nonnull
    public ProcessingHelper setDurationSupplier(Function<GTRecipe, Double> durationSupplier) {
        this.durationSupplier = durationSupplier;
        return this;
    }

    /**
     * Sets whether the machine performs overclocks.
     */
    @Nonnull
    public ProcessingHelper setOverclock(boolean overclock) {
        this.overclock = overclock;
        return this;
    }

    @Nonnull
    public ProcessingHelper setOCEUtModifierSupplier(Function<Integer, Double> ocEUtModifierSupplier) {
        this.ocEUtModifierSupplier = ocEUtModifierSupplier;
        return this;
    }

    @Nonnull
    public ProcessingHelper setOCDurationModifierSupplier(Function<Integer, Double> ocDurationModifierSupplier) {
        this.ocDurationModifierSupplier = ocDurationModifierSupplier;
        return this;
    }

    /**
     * Sets the multiplier that is applied on output chances. 1 does nothing. 0.9 is 10% less. 1.1 is 10% more.
     * Only useful for item outputs for sure.
     */
    @Nonnull
    public ProcessingHelper setChanceMultiplier(double chanceMultiplier) {
        this.chanceMultiplier = chanceMultiplier;
        return this;
    }

    /**
     * Sets the item/fluid output multiplier. 1 does nothing. 2 doubles the item and fluid outputs.
     */
    @Nonnull
    public ProcessingHelper setOutputMultiplier(int outputMultiplier) {
        this.outputMultiplier = outputMultiplier;
        return this;
    }

    /**
     * Set if we should consume inputs or not when trying for parallels
     *
     * @param consume Should we consume inputs
     */
    @Nonnull
    public ProcessingHelper setConsumption(boolean consume) {
        this.consume = consume;
        return this;
    }

    /**
     * Sets the MaxParallel a multi can handle
     */
    @Nonnull
    public ProcessingHelper setMaxParallels(int maxParallels) {
        this.maxParallels = maxParallels;
        return this;
    }

    /**
     * Enables Batch mode, allowing additional recipes to be processed in parallel
     * with proportionally increased processing time. Processes up to mCurrentParallel * BatchModifier recipes.
     */
    @Nonnull
    public ProcessingHelper setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
        return this;
    }

    /**
     * Sets the batch modifier, which determines how many additional recipes
     * can be processed in parallel when Batch Mode is enabled.
     **/
    @Nonnull
    public ProcessingHelper setBatchModifier(int batchModifier) {
        this.batchModifier = batchModifier;
        return this;
    }

    /**
     * Sets if we should calculate outputs with the parallels we found or not
     *
     * @param calculateOutputs Should we calculate outputs with the helper or not
     */
    @Nonnull
    public ProcessingHelper setOutputCalculation(boolean calculateOutputs) {
        this.calculateOutputs = calculateOutputs;
        return this;
    }

    /**
     * Set a custom way to calculate item outputs. You are given the amount of parallels and must return an ItemStack
     * array
     */
    @Nonnull
    public ProcessingHelper setCustomItemOutputCalculation(Function<Integer, ItemStack[]> custom) {
        customItemOutputCalculation = custom;
        return this;
    }

    /**
     * Set a custom way to calculate item outputs. You are given the amount of parallels and must return a FluidStack
     * array
     */
    @Nonnull
    public ProcessingHelper setCustomFluidOutputCalculation(Function<Integer, FluidStack[]> custom) {
        customFluidOutputCalculation = custom;
        return this;
    }

    /**
     * Sets method for calculating max parallel from given inputs.
     */
    public ProcessingHelper setMaxParallelCalculator(MaxParallelCalculator maxParallelCalculator) {
        this.maxParallelCalculator = maxParallelCalculator;
        return this;
    }

    /**
     * Sets method for consuming inputs after determining how many parallels it can execute.
     */
    public ProcessingHelper setInputConsumer(InputConsumer inputConsumer) {
        this.inputConsumer = inputConsumer;
        return this;
    }

    @Nonnull
    public ProcessingHelper setHeatOC(boolean heatOC) {
        this.heatOC = heatOC;
        return this;
    }

    @Nonnull
    public ProcessingHelper setHeatDiscount(boolean heatDiscount) {
        this.heatDiscount = heatDiscount;
        return this;
    }

    @Nonnull
    public ProcessingHelper setHeatDiscountMultiplier(double heatDiscountMultiplier) {
        this.heatDiscountMultiplier = heatDiscountMultiplier;
        return this;
    }

    @Nonnull
    public ProcessingHelper setMachineHeat(int machineHeat) {
        this.machineHeat = machineHeat;
        return this;
    }

    /**
     * @return The current parallels possible by the multiblock
     */
    public int getCurrentParallel() {
        if (!built) {
            throw new IllegalStateException("Tried to get parallels before building");
        }
        return currentParallel;
    }

    /**
     * @return The duration multiplier if batch mode was enabled for the multiblock
     */
    public double getDurationMultiplierDouble() {
        if (!built) {
            throw new IllegalStateException("Tried to get duration multiplier before building");
        }
        if (batchMode && durationMultiplier > 0) {
            return durationMultiplier;
        }
        return 1;
    }

    /**
     * @return The ItemOutputs from the recipe
     */
    @Nonnull
    public ItemStack[] getItemOutputs() {
        if (!built || !calculateOutputs) {
            throw new IllegalStateException(
                "Tried to get item outputs before building or without enabling calculation of outputs");
        }
        return itemOutputs;
    }

    /**
     * @return The FluidOutputs from the recipe
     */
    @Nonnull
    public FluidStack[] getFluidOutputs() {
        if (!built || !calculateOutputs) {
            throw new IllegalStateException(
                "Tried to get fluid outputs before building or without enabling calculation of outputs");
        }
        return fluidOutputs;
    }

    /**
     * @return The result of why a recipe could've failed or succeeded
     */
    @Nonnull
    public CheckRecipeResult getResult() {
        if (!built) {
            throw new IllegalStateException("Tried to get recipe result before building");
        }
        return result;
    }

    /**
     * Called by build(). Determines the parallels and everything else that needs to be done at build time
     */
    public ProcessingResult process() {
        // Sanitize inputs
        this.itemInputs = this.itemInputs == null ? new ItemStack[0] : this.itemInputs;
        this.fluidInputs = this.fluidInputs == null ? new FluidStack[0] : this.fluidInputs;

        // If not consuming, perform deep copy
        if (!this.consume) {
            this.itemInputs = Arrays.stream(this.itemInputs)
                .map(ItemStack::copy)
                .toArray(ItemStack[]::new);
            this.fluidInputs = Arrays.stream(this.fluidInputs)
                .map(FluidStack::copy)
                .toArray(FluidStack[]::new);
        }

        // Truncate and sanitize the outputs
        final ItemStack[] truncatedItemOutputs = this.recipe.mOutputs == null ? new ItemStack[0]
            : Arrays.copyOfRange(
                this.recipe.mOutputs,
                0,
                Math.min(this.machine.getItemOutputLimit(), this.recipe.mOutputs.length));
        final FluidStack[] truncatedFluidOutputs = this.recipe.mFluidOutputs == null ? new FluidStack[0]
            : Arrays.copyOfRange(
                this.recipe.mFluidOutputs,
                0,
                Math.min(this.machine.getFluidOutputLimit(), this.recipe.mFluidOutputs.length));

        // Calculate heat discount
        final double heatDiscount = this.heatDiscount ? Math.pow(
            this.heatDiscountMultiplier,
            Math.max(0, (this.recipe.mSpecialValue - this.machineHeat) / HEAT_DISCOUNT_THRESHOLD)) : 1;

        // Check whether there is sufficient energy
        final int EUt = (int) Math.ceil(this.recipe.mEUt * this.eutModifier * heatDiscount);
        if (this.availableEUt < EUt) {
            return ProcessingResult.failure(CheckRecipeResultRegistry.insufficientPower(EUt));
        }

        // Determine parallels before sub-tick and batch
        int parallels = EUt > 0 ? (int) Math.min(this.maxParallels, this.availableEUt / EUt) : this.maxParallels;

        // Determine duration using speed modifier or using custom supplier
        double duration = this.durationSupplier == null ? this.recipe.mDuration * this.durationModifier
            : this.durationSupplier.apply(this.recipe);

        if (this.overclock) {
            double ocEUtModifier = ocEUtModifierSupplier == null ? OC_EUT_MODIFIER : ocEUtModifierSupplier.apply(0);
            double ocDurationModifier = ocDurationModifierSupplier == null ? OC_DURATION_MODIFIER : ocDurationModifierSupplier.apply(0);
        }

        // public double calculateDurationUnderOneTick() {
        // if (noOverclock) return durationInDouble;
        // double heatDiscountMultiplier = calculateHeatDiscountMultiplier();
        // if (hasAtLeastOneSupplierBeenSet) {
        // int overclockCount = 0;
        // double currentEutIncrease = eutIncreasePerOCSupplier.apply(overclockCount + 1);
        // double currentDurationDecrease = durationDecreasePerOCSupplier.apply(overclockCount + 1);
        // double machinePower = calculateMachinePower();
        // double recipePower = calculateRecipePower(heatDiscountMultiplier);
        // while (machinePower > recipePower * currentEutIncrease
        // && (!limitOverclocks || overclockCount < maxOverclocks)) {
        // recipePower *= currentEutIncrease;
        // durationInDouble /= currentDurationDecrease;
        // overclockCount++;
        // currentEutIncrease = eutIncreasePerOCSupplier.apply(overclockCount + 1);
        // currentDurationDecrease = durationDecreasePerOCSupplier.apply(overclockCount + 1);
        // }
        // } else {
        // int maxOverclockCount = calculateAmountOfOverclocks(
        // calculateMachinePowerTier(),
        // calculateRecipePowerTier(heatDiscountMultiplier));
        // if (limitOverclocks) maxOverclockCount = Math.min(maxOverclocks, maxOverclockCount);
        // int heatOverclocks = Math.min(calculateMaxAmountOfHeatOverclocks(), maxOverclockCount);
        // durationInDouble /= Math.pow(durationDecreasePerOC, maxOverclockCount - heatOverclocks)
        // * Math.pow(durationDecreasePerHeatOC, heatOverclocks);
        // }
        // return durationInDouble;
        // }

        // TODO
        // double overclockedDuration;
        // int subTickParallels;
        // if (overclockedDuration < 1) {
        // subTickParallels = GTUtility.safeInt((long) (maxParallels / overclockedDuration), 0);
        // }

        // int batchParallels;
        // if (batchMode) {
        // batchParallels = GTUtility.safeInt((long) maxParallels * batchModifier, 0);
        // }

        // todo
        // SingleRecipeCheck recipeCheck = null;
        // SingleRecipeCheck.Builder tSingleRecipeCheckBuilder = null;
        // if (isRecipeLocked && singleRecipeMachine != null) {
        // recipeCheck = singleRecipeMachine.getSingleRecipeCheck();
        // if (recipeCheck == null) {
        // // Machine is configured to lock to a single recipe, but haven't built the recipe checker yet.
        // // Build the checker on next successful recipe.
        // RecipeMap<?> recipeMap = singleRecipeMachine.getRecipeMap();
        // if (recipeMap != null) {
        // tSingleRecipeCheckBuilder = SingleRecipeCheck.builder(recipeMap)
        // .setBefore(itemInputs, fluidInputs);
        // }
        // }
        // }

        // Let's look at how many parallels we can get with void protection
        // if (this.protectExcessItem || this.protectExcessFluid) {
        // if (this.machine == null) {
        // throw new IllegalStateException("Tried to calculate void protection, but machine is not set");
        // }
        // VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper();
        // voidProtectionHelper.setMachine(machine)
        // .setItemOutputs(truncatedItemOutputs)
        // .setFluidOutputs(truncatedFluidOutputs)
        // .setChangeGetter(recipe::getOutputChance)
        // .setOutputMultiplier(outputMultiplier)
        // .setChanceMultiplier(chanceMultiplier)
        // .setMaxParallel(maxParallels)
        // .build();
        // maxParallels = Math.min(voidProtectionHelper.getMaxParallel(), maxParallels);
        // if (voidProtectionHelper.isItemFull()) {
        // return ProcessingResult.failure(CheckRecipeResultRegistry.ITEM_OUTPUT_FULL);
        // }
        // if (voidProtectionHelper.isFluidFull()) {
        // return ProcessingResult.failure(CheckRecipeResultRegistry.FLUID_OUTPUT_FULL);
        // }
        // }
        //
        // maxParallelBeforeBatchMode = Math.min(maxParallels, maxParallelBeforeBatchMode);
//
//        if (recipeCheck != null) {
//            currentParallel = recipeCheck.checkRecipeInputs(true, actualMaxParallel, itemInputs, fluidInputs);
//        } else {
//            currentParallel = (int) maxParallelCalculator.calculate(recipe, actualMaxParallel, fluidInputs, itemInputs);
//            if (currentParallel > 0) {
//                if (tSingleRecipeCheckBuilder != null) {
//                    // If recipe checker is not built yet, build and set it
//                    inputConsumer.consume(recipe, 1, fluidInputs, itemInputs);
//                    SingleRecipeCheck builtCheck = tSingleRecipeCheckBuilder.setAfter(itemInputs, fluidInputs)
//                        .setRecipe(recipe)
//                        .build();
//                    singleRecipeMachine.setSingleRecipeCheck(builtCheck);
//                    inputConsumer.consume(recipe, currentParallel - 1, fluidInputs, itemInputs);
//                } else {
//                    inputConsumer.consume(recipe, currentParallel, fluidInputs, itemInputs);
//                }
//            }
//        }
//
//        if (currentParallel <= 0) {
//            result = CheckRecipeResultRegistry.INTERNAL_ERROR;
//            return;
//        }
//
//        calculator.setCurrentParallel(currentParallel)
//            .calculate();
//        // If Batch Mode is enabled determine how many extra parallels we can get
//        if (batchMode && currentParallel > 0 && calculator.getDuration() < MAX_BATCH_MODE_TICK_TIME) {
//            int tExtraParallels;
//            double batchMultiplierMax = MAX_BATCH_MODE_TICK_TIME / calculator.getDuration();
//            final int maxExtraParallels = (int) Math.floor(
//                Math.min(
//                    currentParallel * Math.min(batchMultiplierMax - 1, batchModifier - 1),
//                    maxParallels - currentParallel));
//            if (recipeCheck != null) {
//                tExtraParallels = recipeCheck.checkRecipeInputs(true, maxExtraParallels, itemInputs, fluidInputs);
//            } else {
//                tExtraParallels = (int) maxParallelCalculator
//                    .calculate(recipe, maxExtraParallels, fluidInputs, itemInputs);
//                inputConsumer.consume(recipe, tExtraParallels, fluidInputs, itemInputs);
//            }
//            durationMultiplier = 1.0f + (float) tExtraParallels / currentParallel;
//            currentParallel += tExtraParallels;
//        }
//
//        // If we want to calculate outputs we do it here
//        if (calculateOutputs && currentParallel > 0) {
//            calculateItemOutputs(truncatedItemOutputs);
//            calculateFluidOutputs(truncatedFluidOutputs);
//        }
        return ProcessingResult.success(CheckRecipeResultRegistry.SUCCESSFUL);
    }

    private void calculateItemOutputs(ItemStack[] truncatedItemOutputs) {
        if (customItemOutputCalculation != null) {
            itemOutputs = customItemOutputCalculation.apply(currentParallel);
            return;
        }
        if (truncatedItemOutputs.length == 0) return;
        ArrayList<ItemStack> itemOutputsList = new ArrayList<>();
        for (int i = 0; i < truncatedItemOutputs.length; i++) {
            if (recipe.getOutput(i) == null) continue;
            ItemStack origin = recipe.getOutput(i)
                .copy();
            final long itemStackSize = origin.stackSize;
            double chancedOutputMultiplier = calculateChancedOutputMultiplier(
                (int) (recipe.getOutputChance(i) * chanceMultiplier),
                currentParallel);
            long items = (long) Math.ceil(itemStackSize * chancedOutputMultiplier * outputMultiplier);
            addItemsLong(itemOutputsList, origin, items);
        }
        itemOutputs = itemOutputsList.toArray(new ItemStack[0]);
    }

    private void calculateFluidOutputs(FluidStack[] truncatedFluidOutputs) {
        if (customFluidOutputCalculation != null) {
            fluidOutputs = customFluidOutputCalculation.apply(currentParallel);
            return;
        }
        if (truncatedFluidOutputs.length == 0) return;
        ArrayList<FluidStack> fluidOutputsList = new ArrayList<>();
        for (int i = 0; i < truncatedFluidOutputs.length; i++) {
            if (recipe.getFluidOutput(i) == null) continue;
            FluidStack origin = recipe.getFluidOutput(i)
                .copy();
            long fluids = (long) this.outputMultiplier * origin.amount * currentParallel;

            addFluidsLong(fluidOutputsList, origin, fluids);
        }
        fluidOutputs = fluidOutputsList.toArray(new FluidStack[0]);
    }

    private static final Random rand = new Random();

    public static double calculateChancedOutputMultiplier(int chanceInt, int parallel) {
        // Multiply the integer part of the chance directly with parallel
        double multiplier = Math.floorDiv(chanceInt, 10000) * parallel;
        int transformedChanceInt = chanceInt % 10000;
        if (transformedChanceInt == 0) return multiplier;
        // Calculation of the Decimal Part of chance
        double chance = transformedChanceInt / 10000.0;
        double mean = parallel * chance;
        double stdDev = Math.sqrt(parallel * chance * (1 - chance));
        // Check if everything within 3 standard deviations of mean is within the range
        // of possible values (0 ~ currentParallel)
        boolean isSuitableForFittingWithNormalDistribution = mean - 3 * stdDev >= 0 && mean + 3 * stdDev <= parallel;
        if (isSuitableForFittingWithNormalDistribution) {
            // Use Normal Distribution to fit Binomial Distribution
            double tMultiplier = stdDev * rand.nextGaussian() + mean;
            multiplier += Math.max(Math.min(tMultiplier, parallel), 0);
        } else {
            // Do Binomial Distribution by loop
            for (int roll = 0; roll < parallel; roll++) {
                if (transformedChanceInt > XSTR.XSTR_INSTANCE.nextInt(10000)) {
                    multiplier += 1;
                }
            }
        }
        return multiplier;
    }

    public static void addItemsLong(ArrayList<ItemStack> itemList, ItemStack origin, long amount) {
        if (amount > 0) {
            while (amount > Integer.MAX_VALUE) {
                ItemStack item = origin.copy();
                item.stackSize = Integer.MAX_VALUE;
                itemList.add(item);
                amount -= Integer.MAX_VALUE;
            }
            ItemStack item = origin.copy();
            item.stackSize = (int) amount;
            itemList.add(item);
        }
    }

    public static void addFluidsLong(ArrayList<FluidStack> fluidList, FluidStack origin, long amount) {
        if (amount > 0) {
            while (amount > Integer.MAX_VALUE) {
                FluidStack fluid = origin.copy();
                fluid.amount = Integer.MAX_VALUE;
                fluidList.add(fluid);
                amount -= Integer.MAX_VALUE;
            }
            FluidStack fluid = origin.copy();
            fluid.amount = (int) amount;
            fluidList.add(fluid);
        }
    }

    @FunctionalInterface
    public interface MaxParallelCalculator {

        double calculate(GTRecipe recipe, int maxParallel, FluidStack[] fluids, ItemStack[] items);
    }

    @FunctionalInterface
    public interface InputConsumer {

        void consume(GTRecipe recipe, int amountMultiplier, FluidStack[] aFluidInputs, ItemStack[] aInputs);
    }
}
