package gregtech.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;
import com.gtnewhorizons.modularui.api.forge.IItemHandler;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.multitileentity.multiblock.base.Controller;
import gregtech.api.objects.XSTR;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;

public class GT_ParallelHelper {

    /**
     * A MetaTileEntity Controller
     */
    private GT_MetaTileEntity_MultiBlockBase mMachineMeta;
    /**
     * A MultiTileEntity Controller
     */
    private Controller<?> mMachineMulti;
    /**
     * Recipe used when trying to calculate parallels
     */
    private GT_Recipe mRecipe;
    /**
     * EUt available to the multiblock (This should be the total eut available)
     */
    private long mAvailableEUt;
    /**
     * The current parallel possible for the multiblock
     */
    private int mCurrentParallel = 0;
    /**
     * The maximum possible parallel possible for the multiblock
     */
    private int mMaxParallel = 1;
    /**
     * The Batch Modifier applied when batch mode is enabled. 1 does nothing. 2 doubles max possible
     * parallel, but also duration
     */
    private int mBatchModifier = 1;
    /**
     * The inputs of the multiblock for the current recipe check
     */
    private ItemStack[] mItemInputs;
    /**
     * The outputs of the recipe with the applied parallel
     */
    private ItemStack[] mItemOutputs;
    /**
     * The inputs of the multiblock for the current recipe check
     */
    private FluidStack[] mFluidInputs;
    /**
     * The outputs of the recipe with the applied parallel
     */
    private FluidStack[] mFluidOutputs;
    /**
     * Does the multi have void protection enabled
     */
    private boolean mVoidProtection;
    /**
     * Should the Parallel Helper automatically consume for the multi
     */
    private boolean mConsume;
    /**
     * Is batch mode turned on?
     */
    private boolean mBatchMode;
    /**
     * Should the Parallel Helper automatically calculate the outputs of the recipe with current
     * parallel
     */
    private boolean mCalculateOutputs;
    /**
     * Has the Parallel Helper been built?
     */
    private boolean mBuilt;
    /**
     * What is the duration multiplier with batch mode enabled
     */
    private float mDurationMultiplier;
    /**
     * Modifier which is applied on the recipe eut. Useful for GT++ machines
     */
    private float mEUtModifier = 1;

    public GT_ParallelHelper() {}

    /**
     * Enables void protection on a metatile multiblock. Experimental! Still needs to be worked on
     */
    public GT_ParallelHelper enableVoidProtection(GT_MetaTileEntity_MultiBlockBase aMachineMeta) {
        mVoidProtection = true;
        mMachineMeta = aMachineMeta;
        return this;
    }

    /**
     * Enables void protection on a multitile multiblock. Experimental! Still needs to be worked on
     */
    public GT_ParallelHelper enableVoidProtection(Controller<?> aMachineMulti) {
        mVoidProtection = true;
        mMachineMulti = aMachineMulti;
        return this;
    }

    /**
     * Sets the recipe, which will be used for the parallel calculation
     */
    public GT_ParallelHelper setRecipe(GT_Recipe aRecipe) {
        mRecipe = aRecipe;
        return this;
    }

    /**
     * Sets the items available for the recipe check
     */
    public GT_ParallelHelper setItemInputs(ItemStack... aItemInputs) {
        mItemInputs = aItemInputs;
        return this;
    }

    /**
     * Sets the fluid inputs available for the recipe check
     */
    public GT_ParallelHelper setFluidInputs(FluidStack... aFluidInputs) {
        mFluidInputs = aFluidInputs;
        return this;
    }

    /**
     * Sets the available eut when trying for more parallels
     */
    public GT_ParallelHelper setAvailableEUt(long aAvailableEUt) {
        mAvailableEUt = aAvailableEUt;
        return this;
    }

    /**
     * Sets the modifier for recipe eut. 1 does nothing 0.9 is 10% less. 1.1 is 10% more
     */
    public GT_ParallelHelper setEUtModifier(float aEUtModifier) {
        mEUtModifier = aEUtModifier;
        return this;
    }

    /**
     * Consume inputs when trying for parallels
     */
    public GT_ParallelHelper enableConsumption() {
        mConsume = true;
        return this;
    }

    /**
     * Sets the MaxParallel a multi can handle
     */
    public GT_ParallelHelper setMaxParallel(int aMaxParallel) {
        mMaxParallel = aMaxParallel;
        return this;
    }

    /**
     * Enables Batch mode. Can do up to an additional processed recipes of mCurrentParallel * mBatchModifier A batch
     * modifier of 1 does nothing
     */
    public GT_ParallelHelper enableBatchMode(int aBatchModifier) {
        mBatchMode = true;
        mBatchModifier = aBatchModifier;
        return this;
    }

    /**
     * Enables the outputs to be calculated with its current Parallels, useful if one isn't doing anything special with
     * outputs
     */
    public GT_ParallelHelper enableOutputCalculation() {
        mCalculateOutputs = true;
        return this;
    }

    /**
     * Finishes the GT_ParallelHelper. Anything changed after this will not effect anything
     */
    public GT_ParallelHelper build() {
        if (mBuilt) {
            throw new IllegalStateException("Tried to build twice");
        }
        mBuilt = true;
        determineParallel();
        return this;
    }

    /**
     * @return The current parallels possible by the multiblock
     */
    public int getCurrentParallel() {
        if (!mBuilt) {
            throw new IllegalStateException("Tried to get parallels before building");
        }
        return mCurrentParallel;
    }

    /**
     * @return The duration multiplier if batch mode was enabled for the multiblock
     */
    public float getDurationMultiplier() {
        if (!mBuilt) {
            throw new IllegalStateException("Tried to get duration multiplier before building");
        }
        if (mBatchMode) {
            return mDurationMultiplier;
        }
        return 1;
    }

    /**
     * @return The ItemOutputs from the recipe
     */
    public ItemStack[] getItemOutputs() {
        if (!mBuilt || !mCalculateOutputs) {
            throw new IllegalStateException(
                "Tried to get item outputs before building or without enabling calculation of outputs");
        }
        return mItemOutputs;
    }

    /**
     * @return The FluidOutputs from the recipe
     */
    public FluidStack[] getFluidOutputs() {
        if (!mBuilt || !mCalculateOutputs) {
            throw new IllegalStateException(
                "Tried to get fluid outputs before building or without enabling calculation of outputs");
        }
        return mFluidOutputs;
    }

    /**
     * Called by build(). Determines the parallels and everything else that needs to be done at build time
     */
    protected void determineParallel() {
        if (mRecipe.mEUt > mAvailableEUt) {
            return;
        }
        ItemStack[] tItemInputs = null;
        FluidStack[] tFluidInputs = null;
        boolean tMEOutputBus = false;
        boolean tMEOutputHatch = false;
        long tCurrentUsage = 0;
        // see if people want to consume their inputs with the Parallel Helper or not
        if (mConsume) {
            tItemInputs = mItemInputs;
            tFluidInputs = mFluidInputs;
        } else {
            if (mItemInputs == null) {
                tItemInputs = new ItemStack[] {};
            } else {
                tItemInputs = new ItemStack[mItemInputs.length];
                for (int i = 0; i < mItemInputs.length; i++) {
                    tItemInputs[i] = mItemInputs[i].copy();
                }
            }

            if (mFluidInputs == null) {
                mFluidInputs = new FluidStack[] {};
            } else {
                tFluidInputs = new FluidStack[mFluidInputs.length];
                for (int i = 0; i < mFluidInputs.length; i++) {
                    tFluidInputs[i] = mFluidInputs[i].copy();
                }
            }
        }
        if (mBatchMode) {
            mMaxParallel *= mBatchModifier;
        }
        // Let's look at how many parallels we can get with void protection
        if (mVoidProtection) {
            if (mMachineMeta != null) {
                for (GT_MetaTileEntity_Hatch tHatch : mMachineMeta.mOutputBusses) {
                    if (tHatch instanceof GT_MetaTileEntity_Hatch_OutputBus_ME) {
                        tMEOutputBus = true;
                        break;
                    }
                }

                for (GT_MetaTileEntity_Hatch tHatch : mMachineMeta.mOutputHatches) {
                    if (tHatch instanceof GT_MetaTileEntity_Hatch_Output_ME) {
                        tMEOutputHatch = true;
                        break;
                    }
                }
                if (!tMEOutputBus) {
                    mMaxParallel = Math.min(calculateMaxItemParallelsForMeTEs(), mMaxParallel);
                }

                if (!tMEOutputHatch) {
                    mMaxParallel = Math.min(calculateMaxFluidParallelsForMeTEs(), mMaxParallel);
                }
            } else if (mMachineMulti != null) {
                mMaxParallel = Math.min(calculateMaxItemParallelsForMuTEs(), mMaxParallel);
                mMaxParallel = Math.min(calculateMaxFluidParallelsForMuTEs(), mMaxParallel);
            }
        }

        float tRecipeEUt = mRecipe.mEUt * mEUtModifier;
        // Consume inputs to determine normal parallel
        for (; mCurrentParallel < mMaxParallel / mBatchModifier
            && tCurrentUsage < (mAvailableEUt - tRecipeEUt); mCurrentParallel++) {
            if (mRecipe.isRecipeInputEqual(true, false, tFluidInputs, tItemInputs)) {
                tCurrentUsage += tRecipeEUt;
            } else {
                break;
            }
        }

        // If Batch Mode is enabled determine how many extra parallels we can get
        if (mBatchMode) {
            int tExtraParallels = 0;
            while (tExtraParallels < mCurrentParallel * (mBatchModifier - 1)
                && mRecipe.isRecipeInputEqual(false, false, tFluidInputs, tItemInputs)) {
                mRecipe.isRecipeInputEqual(true, false, tFluidInputs, tItemInputs);
                tExtraParallels++;
            }
            mDurationMultiplier = 1.0f + (float) tExtraParallels / mCurrentParallel;
            mCurrentParallel += tExtraParallels;
        }

        // If we want to calculate outputs we do it here
        if (mCalculateOutputs) {
            if (mRecipe.mOutputs != null) {
                mItemOutputs = new ItemStack[mRecipe.mOutputs.length];
                for (int i = 0; i < mRecipe.mOutputs.length; i++) {
                    if (mRecipe.getOutputChance(i) >= XSTR.XSTR_INSTANCE.nextInt(10000)) {
                        if (mRecipe.getOutput(i) == null) {
                            mItemOutputs[i] = null;
                        } else {
                            ItemStack tItem = mRecipe.getOutput(i)
                                .copy();
                            tItem.stackSize *= mCurrentParallel;
                            mItemOutputs[i] = tItem;
                        }
                    }
                }
            }
            if (mRecipe.mFluidOutputs != null) {
                mFluidOutputs = new FluidStack[mRecipe.mFluidOutputs.length];
                for (int i = 0; i < mRecipe.mFluidOutputs.length; i++) {
                    if (mRecipe.getFluidOutput(i) == null) {
                        mFluidOutputs[i] = null;
                    } else {
                        FluidStack tFluid = mRecipe.getFluidOutput(i)
                            .copy();
                        tFluid.amount *= mCurrentParallel;
                        mFluidOutputs[i] = tFluid;
                    }
                }
            }
        }
    }

    /**
     * Calculates the max parallel for fluids if void protection is turned on
     */
    private int calculateMaxFluidParallelsForMuTEs() {
        if (mMachineMulti == null || mMachineMulti.getOutputTanks() == null) {
            return 0;
        }
        return calculateMaxFluidParallels(
            Arrays.asList(mMachineMulti.getOutputTanks()),
            tHatch -> tHatch.getFluidAmount() == 0,
            (tHatch, fluidStack) -> true);
    }

    /**
     * Calculates the max parallel for fluids if void protection is turned on
     */
    private int calculateMaxFluidParallelsForMeTEs() {
        if (mMachineMeta == null) {
            return 0;
        }
        return calculateMaxFluidParallels(
            mMachineMeta.mOutputHatches,
            tHatch -> tHatch.mMode == 0 && tHatch.getFluidAmount() == 0,
            (tHatch, fluidStack) -> {
                if (GT_ModHandler.isSteam(fluidStack)) {
                    return tHatch.outputsSteam();
                } else {
                    if (!tHatch.outputsLiquids()) {
                        return false;
                    }
                    String tLockedFluidName = tHatch.getLockedFluidName();
                    return !tHatch.isFluidLocked() || tLockedFluidName == null
                        || tLockedFluidName.equals(
                            fluidStack.getFluid()
                                .getName());
                }
            });
    }

    /**
     * Calculates the max parallel for fluids if void protection is turned on
     */
    private <T extends IFluidTank> int calculateMaxFluidParallels(List<T> hatches, Function<T, Boolean> isEmpty,
        BiFunction<T, FluidStack, Boolean> acceptsFluid) {
        if (hatches.size() < mRecipe.mFluidOutputs.length) {
            return 0;
        }

        // A map to hold the items we will be 'inputting' into the output hatches. These fluidstacks are actually
        // the recipe outputs.
        Map<FluidStack, Integer> tFluidOutputMap = new HashMap<>();

        // Map that keeps track of the number of parallel crafts we can accommodate for each fluid output.
        // In the pair, we keep track of number of full crafts plus mb of fluid in a partial craft, to avoid
        // issues with floating point math not being completely accurate when summing.
        Map<FluidStack, ParallelData> tParallels = new HashMap<>();

        // Iterate over the outputs, calculating require stack spacing they will require.
        for (FluidStack aY : mRecipe.mFluidOutputs) {
            if (aY == null) {
                continue;
            }
            tFluidOutputMap.merge(aY, aY.amount, Integer::sum);
            tParallels.put(aY, new ParallelData(0, 0));
        }

        if (tFluidOutputMap.isEmpty()) {
            // nothing to output, bail early
            return mMaxParallel;
        }

        for (T tHatch : hatches) {
            int tSpaceLeft = tHatch.getCapacity() - tHatch.getFluidAmount();

            // check if hatch filled
            if (tSpaceLeft <= 0) continue;

            // check if hatch is empty and unrestricted
            if (isEmpty.apply(tHatch)) continue;

            for (Entry<FluidStack, ParallelData> entry : tParallels.entrySet()) {
                FluidStack tFluidOutput = entry.getKey();
                if (acceptsFluid.apply(tHatch, tFluidOutput)) continue;
                // this fluid is not prevented by restrictions on output hatch
                if (tHatch.getFluidAmount() == 0 || GT_Utility.areFluidsEqual(tHatch.getFluid(), tFluidOutput)) {
                    ParallelData tParallel = entry.getValue();
                    Integer tCraftSize = tFluidOutputMap.get(tFluidOutput);
                    tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
                    tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
                }
            }
        }
        // now that all partial/restricted hatches have been counted, create a priority queue for our outputs
        // the lowest priority fluid is the number of complete parallel crafts we can support
        PriorityQueue<ParallelStackInfo<FluidStack>> aParallelQueue = new PriorityQueue<>(
            Comparator.comparing(i -> i.batch));
        for (Entry<FluidStack, ParallelData> entry : tParallels.entrySet()) {
            aParallelQueue
                .add(new ParallelStackInfo<>(entry.getValue().batch, entry.getValue().partial, entry.getKey()));
        }
        // add extra parallels for open slots as well
        for (T tHatch : hatches) {
            // partially filled or restricted hatch. done in last pass
            if (!isEmpty.apply(tHatch)) continue;

            ParallelStackInfo<FluidStack> tParallel = aParallelQueue.poll();
            assert tParallel != null; // will always be true, specifying assert here to avoid IDE/compiler warnings
            Integer tCraftSize = tFluidOutputMap.get(tParallel.stack);
            int tSpaceLeft = tHatch.getCapacity();
            tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
            tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
            aParallelQueue.add(tParallel);
        }
        return aParallelQueue.element().batch;
    }

    /**
     * Calculates the max parallels one can do with items if void protection is on
     */
    private int calculateMaxItemParallelsForMuTEs() {
        List<ItemStack> busStacks = new ArrayList<>();
        if (mMachineMulti != null) {
            IItemHandler inv = mMachineMulti.getOutputInventory();
            if (inv != null && inv.getSlots() > 0) {
                for (int i = 0; i < inv.getSlots(); i++) {
                    busStacks.add(inv.getStackInSlot(i));
                }
            }
        }
        return calculateMaxItemParallels(busStacks);
    }

    /**
     * Calculates the max parallels one can do with items if void protection is on
     */
    private int calculateMaxItemParallelsForMeTEs() {
        List<ItemStack> busStacks = new ArrayList<>();
        if (mMachineMeta != null) {
            for (final GT_MetaTileEntity_Hatch tBus : mMachineMeta.mOutputBusses) {
                if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tBus)) {
                    continue;
                }
                final IInventory tBusInv = tBus.getBaseMetaTileEntity();
                for (int i = 0; i < tBusInv.getSizeInventory(); i++) {
                    busStacks.add(tBus.getStackInSlot(i));
                }
            }
        }
        return calculateMaxItemParallels(busStacks);
    }

    /**
     * Calculates the max parallels one can do with items if void protection is on
     *
     * @param busStacks List of itemstacks that are already stored in buses
     */
    private int calculateMaxItemParallels(List<ItemStack> busStacks) {
        // A map to hold the items we will be 'inputting' into the output buses. These itemstacks are actually the
        // recipe outputs.
        Map<ItemStack, Integer> tItemOutputMap = new ItemStackMap<>();

        // Map that keeps track of the number of parallel crafts we can accommodate for each item output.
        // In the pair, we keep track of number of full crafts plus number of items in a partial craft, to avoid
        // issues with floating point math not being completely accurate when summing.
        Map<ItemStack, ParallelData> tParallels = new ItemStackMap<>();
        int tSlotsFree = 0;
        for (ItemStack tItem : mRecipe.mOutputs) {
            tItemOutputMap.merge(tItem, tItem.stackSize, Integer::sum);
            tParallels.put(tItem, new ParallelData(0, 0));
        }

        if (tItemOutputMap.isEmpty()) {
            // nothing to output, bail early
            return mMaxParallel;
        }

        if (mRecipe.mOutputs.length > 0) {
            for (ItemStack tBusStack : busStacks) {
                if (tBusStack == null) {
                    tSlotsFree++;
                } else {
                    // get the real stack size
                    // we ignore the bus inventory stack limit here as no one set it to anything other than 64
                    int tMaxBusStackSize = tBusStack.getMaxStackSize();
                    if (tBusStack.stackSize >= tMaxBusStackSize)
                        // this bus stack is full. no checking
                        continue;
                    int tSpaceLeft = tMaxBusStackSize - tBusStack.stackSize;
                    Integer tCraftSize = tItemOutputMap.get(tBusStack);
                    if (tCraftSize == null) {
                        // we don't have a matching stack to output, ignore this bus stack
                        continue;
                    }
                    ParallelData tParallel = tParallels.get(tBusStack);
                    tParallel.batch += (tParallel.partial + tSpaceLeft) / tCraftSize;
                    tParallel.partial = (tParallel.partial + tSpaceLeft) % tCraftSize;
                }

            }
            // now that all partial stacks have been counted, create a priority queue for our outputs
            // the lowest priority item is the number of complete parallel crafts we can support
            PriorityQueue<ParallelStackInfo<ItemStack>> aParallelQueue = new PriorityQueue<>(
                Comparator.comparing(i -> i.batch));
            for (Entry<ItemStack, ParallelData> entry : tParallels.entrySet()) {
                aParallelQueue
                    .add(new ParallelStackInfo<>(entry.getValue().batch, entry.getValue().partial, entry.getKey()));
            }

            while (tSlotsFree > 0) {
                ParallelStackInfo<ItemStack> tParallel = aParallelQueue.poll();
                assert tParallel != null; // will always be true, specifying assert here to avoid IDE/compiler warnings
                Integer tCraftSize = tItemOutputMap.get(tParallel.stack);
                int tStackSize = tParallel.stack.getMaxStackSize();
                tParallel.batch += (tParallel.partial + tStackSize) / tCraftSize;
                tParallel.partial = (tParallel.partial + tStackSize) % tCraftSize;
                aParallelQueue.add(tParallel);
                --tSlotsFree;
            }

            return aParallelQueue.element().batch;
        }
        return 0;
    }

    static class ParallelData {

        int batch;
        int partial;

        ParallelData(int batch, int partial) {
            this.batch = batch;
            this.partial = partial;
        }
    }

    static class ParallelStackInfo<T> {

        int batch;
        int partial;
        T stack;

        ParallelStackInfo(int batch, int partial, T stack) {
            this.batch = batch;
            this.partial = partial;
            this.stack = stack;
        }
    }
}
