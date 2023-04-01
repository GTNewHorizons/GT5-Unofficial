package gregtech.api.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.multitileentity.multiblock.base.MultiBlockController;
import gregtech.api.objects.XSTR;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;

public class GT_ParallelHelper {

    /**
     * @mMachineMeta a MetaTileEntity Controller
     */
    private GT_MetaTileEntity_MultiBlockBase mMachineMeta;
    /**
     * @mMachineMulti a MultiTileEntity Controller
     */
    private MultiBlockController<?> mMachineMulti;
    /**
     * @mRecipe Recipe used when trying to calculate parallels
     */
    private GT_Recipe mRecipe;
    /**
     * @mAvailableEUt EUt available to the multiblock (This should be the total eut available)
     */
    private long mAvailableEUt;
    /**
     * @mCurrentParallel The current parallel possible for the multiblock
     * @mMaxParallel The maximum possible parallel possible for the multiblock
     * @mBatchModifier The Batch Modifier applied when batch mode is enabled. 1 does nothing. 2 doubles max possible
     *                 parallel, but also duration
     */
    private int mCurrentParallel = 0, mMaxParallel = 1, mBatchModifier = 1;
    /**
     * @mItemInputs The inputs of the multiblock for the current recipe check
     * @mItemOutputs The outputs of the recipe with the applied parallel
     */
    private ItemStack[] mItemInputs, mItemOutputs;
    /**
     * @mFluidInputs The inputs of the multiblock for the current recipe check
     * @mFluidOutputs The outputs of the recipe with the applied parallel
     */
    private FluidStack[] mFluidInputs, mFluidOutputs;
    /**
     * @mVoidProtection Does the multi have void protection enabled
     * @mConsume Should the Parallel Helper automatically consume for the multi
     * @mBatchMode Is batch mode turned on?
     * @mCalculateOutputs Should the Parallel Helper automatically calculate the outputs of the recipe with current
     *                    parallel
     * @mBuilt Has the Parallel Helper been built?
     */
    private boolean mVoidProtection, mConsume, mBatchMode, mCalculateOutputs, mBuilt;
    /**
     * @mDurationMultiplier What is the duration multiplier with batch mode enabled
     * @mEUtModifier Modifier which is applied on the recipe eut. Useful for GT++ machines
     */
    private float mDurationMultiplier, mEUtModifier = 1;

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
    public GT_ParallelHelper enableVoidProtection(MultiBlockController<?> aMachineMulti) {
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
                mMaxParallel = Math.min(calculateMaxParallelsForBusses(), mMaxParallel);
            }

            if (!tMEOutputHatch) {
                mMaxParallel = Math.min(calculateMaxParallelsForHatches(), mMaxParallel);
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
    private int calculateMaxParallelsForHatches() {
        // For now we are gonna ignore MuTEs existence as there are no recipes for them
        if (mMachineMeta != null && mMachineMeta.mOutputHatches.size() >= mRecipe.mFluidOutputs.length) {
            // A map to hold the items we will be 'inputting' into the output hatches. These fluidstacks are actually
            // the
            // recipe outputs.
            Map<FluidStack, Integer> tFluidOutputMap = new HashMap<>();

            // Map that keeps track of the number of parallel crafts we can accommodate for each fluid output.
            // In the pair, we keep track of number of full crafts plus mb of fluid in a partial craft, to avoid
            // issues with floating point math not being completely accurate when summing.
            Map<FluidStack, MutablePair<Integer, Integer>> tParallels = new HashMap<>();

            // Iterate over the outputs, calculating require stack spacing they will require.
            for (FluidStack aY : mRecipe.mFluidOutputs) {
                if (aY == null) {
                    continue;
                }
                tFluidOutputMap.merge(aY, aY.amount, Integer::sum);
                tParallels.put(aY, new MutablePair<>(0, 0));
            }

            if (tFluidOutputMap.isEmpty()) {
                // nothing to output, bail early
                return mMaxParallel;
            }

            for (GT_MetaTileEntity_Hatch_Output tHatch : mMachineMeta.mOutputHatches) {
                int tSpaceLeft = tHatch.getCapacity() - tHatch.getFluidAmount();

                // check if hatch filled
                if (tSpaceLeft <= 0) continue;

                // check if hatch is empty and unrestricted
                if (tHatch.mMode == 0 && tHatch.getFluidAmount() == 0) continue;

                String tLockedFluidName = tHatch.getLockedFluidName();
                for (Entry<FluidStack, MutablePair<Integer, Integer>> entry : tParallels.entrySet()) {
                    FluidStack tFluidOutput = entry.getKey();
                    if (GT_ModHandler.isSteam(tFluidOutput)) {
                        if (!tHatch.outputsSteam()) {
                            continue;
                        }
                    } else {
                        if (!tHatch.outputsLiquids()) {
                            continue;
                        }
                        if (tHatch.isFluidLocked() && tLockedFluidName != null
                                && !tLockedFluidName.equals(
                                        tFluidOutput.getFluid()
                                                    .getName())) {
                            continue;
                        }
                    }
                    // this fluid is not prevented by restrictions on output hatch
                    if (tHatch.getFluidAmount() == 0 || GT_Utility.areFluidsEqual(tHatch.getFluid(), tFluidOutput)) {
                        MutablePair<Integer, Integer> tParallel = entry.getValue();
                        Integer tCraftSize = tFluidOutputMap.get(tFluidOutput);
                        tParallel.left += (tParallel.right + tSpaceLeft) / tCraftSize;
                        tParallel.right = (tParallel.right + tSpaceLeft) % tCraftSize;
                    }
                }
            }
            // now that all partial/restricted hatches have been counted, create a priority queue for our outputs
            // the lowest priority fluid is the number of complete parallel crafts we can support
            PriorityQueue<MutableTriple<Integer, Integer, FluidStack>> aParallelQueue = new PriorityQueue<>(
                    Comparator.comparing(MutableTriple::getLeft));
            for (Entry<FluidStack, MutablePair<Integer, Integer>> entry : tParallels.entrySet()) {
                aParallelQueue.add(new MutableTriple<>(entry.getValue().left, entry.getValue().right, entry.getKey()));
            }
            // add extra parallels for open slots as well
            for (GT_MetaTileEntity_Hatch_Output tHatch : mMachineMeta.mOutputHatches) {
                // partially filled or restricted hatch. done in last pass
                if (tHatch.getFluidAmount() > 0 || tHatch.mMode != 0) continue;

                MutableTriple<Integer, Integer, FluidStack> tParallel = aParallelQueue.poll();
                assert tParallel != null; // will always be true, specifying assert here to avoid IDE/compiler warnings
                Integer tCraftSize = tFluidOutputMap.get(tParallel.right);
                int tSpaceLeft = tHatch.getCapacity();
                tParallel.left += (tParallel.middle + tSpaceLeft) / tCraftSize;
                tParallel.middle = (tParallel.middle + tSpaceLeft) % tCraftSize;
                aParallelQueue.add(tParallel);
            }
            return aParallelQueue.element().left;
        }
        return 0;
    }

    /**
     * Calculates the max parallels one can do with items if void protection is on
     */
    private int calculateMaxParallelsForBusses() {
        // Same thing we are gonna ignore MuTEs existence for now. should be in theory the same later

        // A map to hold the items we will be 'inputting' into the output buses. These itemstacks are actually the
        // recipe outputs.
        Map<ItemStack, Integer> tItemOutputMap = new ItemStackMap<>();

        // Map that keeps track of the number of parallel crafts we can accommodate for each item output.
        // In the pair, we keep track of number of full crafts plus number of items in a partial craft, to avoid
        // issues with floating point math not being completely accurate when summing.
        Map<ItemStack, MutablePair<Integer, Integer>> tParallels = new ItemStackMap<>();
        int tSlotsFree = 0;
        for (ItemStack tItem : mRecipe.mOutputs) {
            tItemOutputMap.merge(tItem, tItem.stackSize, Integer::sum);
            tParallels.put(tItem, new MutablePair<>(0, 0));
        }

        if (tItemOutputMap.isEmpty()) {
            // nothing to output, bail early
            return mMaxParallel;
        }

        if (mRecipe.mOutputs.length > 0 && mMachineMeta != null) {
            for (final GT_MetaTileEntity_Hatch tBus : mMachineMeta.mOutputBusses) {
                if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tBus)) {
                    continue;
                }
                final IInventory tBusInv = tBus.getBaseMetaTileEntity();
                for (int i = 0; i < tBusInv.getSizeInventory(); i++) {
                    ItemStack tBusStack = tBus.getStackInSlot(i);
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
                        MutablePair<Integer, Integer> tParallel = tParallels.get(tBusStack);
                        tParallel.left += (tParallel.right + tSpaceLeft) / tCraftSize;
                        tParallel.right = (tParallel.right + tSpaceLeft) % tCraftSize;
                    }
                }
            }
            // now that all partial stacks have been counted, create a priority queue for our outputs
            // the lowest priority item is the number of complete parallel crafts we can support
            PriorityQueue<MutableTriple<Integer, Integer, ItemStack>> aParallelQueue = new PriorityQueue<>(
                    Comparator.comparing(MutableTriple::getLeft));
            for (Entry<ItemStack, MutablePair<Integer, Integer>> entry : tParallels.entrySet()) {
                aParallelQueue.add(new MutableTriple<>(entry.getValue().left, entry.getValue().right, entry.getKey()));
            }

            while (tSlotsFree > 0) {
                MutableTriple<Integer, Integer, ItemStack> tParallel = aParallelQueue.poll();
                assert tParallel != null; // will always be true, specifying assert here to avoid IDE/compiler warnings
                Integer tCraftSize = tItemOutputMap.get(tParallel.right);
                int tStackSize = tParallel.right.getMaxStackSize();
                tParallel.left += (tParallel.middle + tStackSize) / tCraftSize;
                tParallel.middle = (tParallel.middle + tStackSize) % tCraftSize;
                aParallelQueue.add(tParallel);
                --tSlotsFree;
            }

            return aParallelQueue.element().left;
        }
        return 0;
    }
}
