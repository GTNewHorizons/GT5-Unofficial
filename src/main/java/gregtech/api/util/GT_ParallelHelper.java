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
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class GT_ParallelHelper {

    private static final double MAX_BATCH_MODE_TICK_TIME = 128;
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
    private GT_Recipe recipe;
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
    private int maxParallel = 1;
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
     * The inputs of the machine for current recipe check
     */
    private ItemInventoryLogic itemInputInventory;
    /**
     * The output item inventory of the machine
     */
    private ItemInventoryLogic itemOutputInventory;
    /**
     * The outputs of the recipe with the applied parallel
     */
    private ItemStack[] itemOutputs;
    /**
     * The inputs of the multiblock for the current recipe check
     */
    private FluidStack[] fluidInputs;
    /**
     * The inputs of the machine for the current recipe check
     */
    private FluidInventoryLogic fluidInputInventory;
    /**
     * The output fluid inventory of the machine;
     */
    private FluidInventoryLogic fluidOutputInventory;
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
    private float eutModifier = 1;
    /**
     * Multiplier that is applied on the output chances
     */
    private double chanceMultiplier = 1;
    /**
     * Multiplier by which the output will be multiplied
     */
    private int outputMultiplier = 1;
    /**
     * Method for calculating max parallel from given inputs.
     */
    private MaxParallelCalculator maxParallelCalculator = GT_Recipe::maxParallelCalculatedByInputs;
    /**
     * Method for consuming inputs after determining how many parallels it can execute.
     */
    private InputConsumer inputConsumer = GT_Recipe::consumeInput;

    /**
     * Calculator to use for overclocking
     */
    private GT_OverclockCalculator calculator;
    @Nonnull
    private CheckRecipeResult result = CheckRecipeResultRegistry.NONE;

    private Function<Integer, ItemStack[]> customItemOutputCalculation;

    private Function<Integer, FluidStack[]> customFluidOutputCalculation;

    /**
     * MuTE Mode this is a mode for changing how the GT_ParallelHelper works as Mutes don't use ItemStack and FluidStack
     * arrays for inputs
     */
    private boolean muteMode = false;

    public GT_ParallelHelper() {}

    /**
     * Sets machine, with current configuration for void protection mode.
     */
    @Nonnull
    public GT_ParallelHelper setMachine(IVoidable machine) {
        return setMachine(machine, machine.protectsExcessItem(), machine.protectsExcessFluid());
    }

    /**
     * Sets machine, with void protection mode forcibly.
     */
    @Nonnull
    public GT_ParallelHelper setMachine(IVoidable machine, boolean protectExcessItem, boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.machine = machine;
        return this;
    }

    /**
     * Sets the recipe, which will be used for the parallel calculation
     */
    @Nonnull
    public GT_ParallelHelper setRecipe(@Nonnull GT_Recipe aRecipe) {
        recipe = Objects.requireNonNull(aRecipe);
        return this;
    }

    @Nonnull
    public GT_ParallelHelper setRecipeLocked(IRecipeLockable singleRecipeMachine, boolean isRecipeLocked) {
        this.singleRecipeMachine = singleRecipeMachine;
        this.isRecipeLocked = isRecipeLocked;
        return this;
    }

    /**
     * Sets the items available for the recipe check
     */
    @Nonnull
    public GT_ParallelHelper setItemInputs(ItemStack... aItemInputs) {
        this.itemInputs = aItemInputs;
        return this;
    }

    /**
     * Sets the fluid inputs available for the recipe check
     */
    @Nonnull
    public GT_ParallelHelper setFluidInputs(FluidStack... aFluidInputs) {
        this.fluidInputs = aFluidInputs;
        return this;
    }

    /**
     * Sets the available eut when trying for more parallels
     */
    @Nonnull
    public GT_ParallelHelper setAvailableEUt(long aAvailableEUt) {
        this.availableEUt = aAvailableEUt;
        return this;
    }

    /**
     * Sets the modifier for recipe eut. 1 does nothing 0.9 is 10% less. 1.1 is 10% more
     */
    @Nonnull
    public GT_ParallelHelper setEUtModifier(float aEUtModifier) {
        this.eutModifier = aEUtModifier;
        return this;
    }

    /**
     * Sets the multiplier that is applied on output chances. 1 does nothing. 0.9 is 10% less. 1.1 is 10% more.
     * Only useful for item outputs for sure.
     */
    @Nonnull
    public GT_ParallelHelper setChanceMultiplier(double chanceMultiplier) {
        this.chanceMultiplier = chanceMultiplier;
        return this;
    }

    /**
     * Sets the item/fluid output multiplier. 1 does nothing. 2 doubles the item and fluid outputs.
     */
    @Nonnull
    public GT_ParallelHelper setOutputMultiplier(int outputMultiplier) {
        this.outputMultiplier = outputMultiplier;
        return this;
    }

    @Nonnull
    public GT_ParallelHelper setCalculator(GT_OverclockCalculator calculator) {
        this.calculator = calculator;
        return this;
    }

    /**
     * Set if we should consume inputs or not when trying for parallels
     *
     * @param consume Should we consume inputs
     */
    @Nonnull
    public GT_ParallelHelper setConsumption(boolean consume) {
        this.consume = consume;
        return this;
    }

    /**
     * Sets the MaxParallel a multi can handle
     */
    @Nonnull
    public GT_ParallelHelper setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
        return this;
    }

    /**
     * Enables Batch mode. Can do up to an additional processed recipes of mCurrentParallel * mBatchModifier A batch
     * modifier of 1 does nothing
     */
    @Nonnull
    public GT_ParallelHelper enableBatchMode(int batchModifier) {
        this.batchMode = batchModifier > 1;
        this.batchModifier = batchModifier;
        return this;
    }

    /**
     * Sets if we should calculate outputs with the parallels we found or not
     *
     * @param calculateOutputs Should we calculate outputs with the helper or not
     */
    @Nonnull
    public GT_ParallelHelper setOutputCalculation(boolean calculateOutputs) {
        this.calculateOutputs = calculateOutputs;
        return this;
    }

    /**
     * Set a custom way to calculate item outputs. You are given the amount of parallels and must return an ItemStack
     * array
     */
    @Nonnull
    public GT_ParallelHelper setCustomItemOutputCalculation(Function<Integer, ItemStack[]> custom) {
        customItemOutputCalculation = custom;
        return this;
    }

    /**
     * Set a custom way to calculate item outputs. You are given the amount of parallels and must return a FluidStack
     * array
     */
    @Nonnull
    public GT_ParallelHelper setCustomFluidOutputCalculation(Function<Integer, FluidStack[]> custom) {
        customFluidOutputCalculation = custom;
        return this;
    }

    @Nonnull
    public GT_ParallelHelper setMuTEMode(boolean muteMode) {
        this.muteMode = muteMode;
        return this;
    }

    @Nonnull
    public GT_ParallelHelper setItemInputInventory(ItemInventoryLogic itemInputInventory) {
        this.itemInputInventory = itemInputInventory;
        return this;
    }

    @Nonnull
    public GT_ParallelHelper setFluidInputInventory(FluidInventoryLogic fluidInputInventory) {
        this.fluidInputInventory = fluidInputInventory;
        return this;
    }

    /**
     * Sets method for calculating max parallel from given inputs.
     */
    public GT_ParallelHelper setMaxParallelCalculator(MaxParallelCalculator maxParallelCalculator) {
        this.maxParallelCalculator = maxParallelCalculator;
        return this;
    }

    /**
     * Sets method for consuming inputs after determining how many parallels it can execute.
     */
    public GT_ParallelHelper setInputConsumer(InputConsumer inputConsumer) {
        this.inputConsumer = inputConsumer;
        return this;
    }

    @Nonnull
    public GT_ParallelHelper setItemOutputInventory(ItemInventoryLogic itemOutputInventory) {
        this.itemOutputInventory = itemOutputInventory;
        return this;
    }

    @Nonnull
    public GT_ParallelHelper setFluidOutputInventory(FluidInventoryLogic fluidOutputInventory) {
        this.fluidOutputInventory = fluidOutputInventory;
        return this;
    }

    /**
     * Finishes the GT_ParallelHelper. Anything changed after this will not effect anything
     */
    @Nonnull
    public GT_ParallelHelper build() {
        if (built) {
            throw new IllegalStateException("Tried to build twice");
        }
        if (recipe == null) {
            throw new IllegalStateException("Recipe is not set");
        }
        built = true;
        determineParallel();
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
    protected void determineParallel() {
        if (maxParallel <= 0) {
            return;
        }
        if (itemInputs == null) {
            itemInputs = new ItemStack[0];
        }
        if (fluidInputs == null) {
            fluidInputs = new FluidStack[0];
        }

        if (!consume) {
            copyInputs();
        }

        if (calculator == null) {
            calculator = new GT_OverclockCalculator().setEUt(availableEUt)
                .setRecipeEUt(recipe.mEUt)
                .setDuration(recipe.mDuration)
                .setEUtDiscount(eutModifier);
        }

        final int tRecipeEUt = (int) Math.ceil(recipe.mEUt * eutModifier);
        if (availableEUt < tRecipeEUt) {
            result = CheckRecipeResultRegistry.insufficientPower(tRecipeEUt);
            return;
        }

        // Save the original max parallel before calculating our overclocking under 1 tick
        int originalMaxParallel = maxParallel;
        calculator.setParallel(originalMaxParallel);
        double tickTimeAfterOC = calculator.calculateDurationUnderOneTick();
        if (tickTimeAfterOC < 1) {
            maxParallel = GT_Utility.safeInt((long) (maxParallel / tickTimeAfterOC), 0);
        }

        int maxParallelBeforeBatchMode = maxParallel;
        if (batchMode) {
            maxParallel = GT_Utility.safeInt((long) maxParallel * batchModifier, 0);
        }

        final ItemStack[] truncatedItemOutputs = recipe.mOutputs != null
            ? Arrays.copyOfRange(recipe.mOutputs, 0, Math.min(machine.getItemOutputLimit(), recipe.mOutputs.length))
            : new ItemStack[0];
        final FluidStack[] truncatedFluidOutputs = recipe.mFluidOutputs != null ? Arrays
            .copyOfRange(recipe.mFluidOutputs, 0, Math.min(machine.getFluidOutputLimit(), recipe.mFluidOutputs.length))
            : new FluidStack[0];

        SingleRecipeCheck recipeCheck = null;
        SingleRecipeCheck.Builder tSingleRecipeCheckBuilder = null;
        if (isRecipeLocked && singleRecipeMachine != null) {
            recipeCheck = singleRecipeMachine.getSingleRecipeCheck();
            if (recipeCheck == null) {
                // Machine is configured to lock to a single recipe, but haven't built the recipe checker yet.
                // Build the checker on next successful recipe.
                RecipeMap<?> recipeMap = singleRecipeMachine.getRecipeMap();
                if (recipeMap != null) {
                    tSingleRecipeCheckBuilder = SingleRecipeCheck.builder(recipeMap)
                        .setBefore(itemInputs, fluidInputs);
                }
            }
        }

        // Let's look at how many parallels we can get with void protection
        if (protectExcessItem || protectExcessFluid) {
            if (machine == null && !muteMode) {
                throw new IllegalStateException("Tried to calculate void protection, but machine is not set");
            }
            VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper();
            voidProtectionHelper.setMachine(machine)
                .setItemOutputs(truncatedItemOutputs)
                .setFluidOutputs(truncatedFluidOutputs)
                .setChangeGetter(recipe::getOutputChance)
                .setOutputMultiplier(outputMultiplier)
                .setChanceMultiplier(chanceMultiplier)
                .setMaxParallel(maxParallel)
                .setItemOutputInventory(itemOutputInventory)
                .setFluidOutputInventory(fluidOutputInventory)
                .setMuTEMode(muteMode)
                .build();
            maxParallel = Math.min(voidProtectionHelper.getMaxParallel(), maxParallel);
            if (voidProtectionHelper.isItemFull()) {
                result = CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
                return;
            }
            if (voidProtectionHelper.isFluidFull()) {
                result = CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
                return;
            }
        }

        maxParallelBeforeBatchMode = Math.min(maxParallel, maxParallelBeforeBatchMode);

        // determine normal parallel
        int actualMaxParallel = tRecipeEUt > 0 ? (int) Math.min(maxParallelBeforeBatchMode, availableEUt / tRecipeEUt)
            : maxParallelBeforeBatchMode;
        if (recipeCheck != null) {
            currentParallel = recipeCheck.checkRecipeInputs(true, actualMaxParallel, itemInputs, fluidInputs);
        } else {
            currentParallel = (int) maxParallelCalculator.calculate(recipe, actualMaxParallel, fluidInputs, itemInputs);
            if (currentParallel > 0) {
                if (tSingleRecipeCheckBuilder != null) {
                    // If recipe checker is not built yet, build and set it
                    inputConsumer.consume(recipe, 1, fluidInputs, itemInputs);
                    SingleRecipeCheck builtCheck = tSingleRecipeCheckBuilder.setAfter(itemInputs, fluidInputs)
                        .setRecipe(recipe)
                        .build();
                    singleRecipeMachine.setSingleRecipeCheck(builtCheck);
                    inputConsumer.consume(recipe, currentParallel - 1, fluidInputs, itemInputs);
                } else {
                    inputConsumer.consume(recipe, currentParallel, fluidInputs, itemInputs);
                }
            }
        }

        if (currentParallel <= 0) {
            result = CheckRecipeResultRegistry.INTERNAL_ERROR;
            return;
        }

        calculator.setCurrentParallel(currentParallel)
            .calculate();
        // If Batch Mode is enabled determine how many extra parallels we can get
        if (batchMode && currentParallel > 0 && calculator.getDuration() < MAX_BATCH_MODE_TICK_TIME) {
            int tExtraParallels;
            double batchMultiplierMax = MAX_BATCH_MODE_TICK_TIME / calculator.getDuration();
            final int maxExtraParallels = (int) Math.floor(
                Math.min(
                    currentParallel * Math.min(batchMultiplierMax - 1, batchModifier - 1),
                    maxParallel - currentParallel));
            if (recipeCheck != null) {
                tExtraParallels = recipeCheck.checkRecipeInputs(true, maxExtraParallels, itemInputs, fluidInputs);
            } else {
                tExtraParallels = (int) maxParallelCalculator
                    .calculate(recipe, maxExtraParallels, fluidInputs, itemInputs);
                inputConsumer.consume(recipe, tExtraParallels, fluidInputs, itemInputs);
            }
            durationMultiplier = 1.0f + (float) tExtraParallels / currentParallel;
            currentParallel += tExtraParallels;
        }

        // If we want to calculate outputs we do it here
        if (calculateOutputs && currentParallel > 0) {
            calculateItemOutputs(truncatedItemOutputs);
            calculateFluidOutputs(truncatedFluidOutputs);
        }
        result = CheckRecipeResultRegistry.SUCCESSFUL;
    }

    protected void copyInputs() {
        ItemStack[] itemInputsToUse;
        FluidStack[] fluidInputsToUse;
        itemInputsToUse = new ItemStack[itemInputs.length];
        for (int i = 0; i < itemInputs.length; i++) {
            itemInputsToUse[i] = itemInputs[i].copy();
        }
        fluidInputsToUse = new FluidStack[fluidInputs.length];
        for (int i = 0; i < fluidInputs.length; i++) {
            fluidInputsToUse[i] = fluidInputs[i].copy();
        }
        itemInputs = itemInputsToUse;
        fluidInputs = fluidInputsToUse;
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

        double calculate(GT_Recipe recipe, int maxParallel, FluidStack[] fluids, ItemStack[] items);
    }

    @FunctionalInterface
    public interface InputConsumer {

        void consume(GT_Recipe recipe, int amountMultiplier, FluidStack[] aFluidInputs, ItemStack[] aInputs);
    }
}
