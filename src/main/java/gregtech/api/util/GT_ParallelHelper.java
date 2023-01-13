package gregtech.api.util;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.multitileentity.multiblock.base.MultiBlockController;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_ParallelHelper {
    private GT_MetaTileEntity_MultiBlockBase mMachineMeta;
    private MultiBlockController mMachineMulti;
    private GT_Recipe mRecipe;
    private long mAvailableEUt;
    private int mCurrentParallel = 0, mMaxParallel = 1, mBatchModifier = 1;
    private ItemStack[] mItemInputs, mItemOutputs;
    private FluidStack[] mFluidInputs, mFluidOutputs;
    private boolean mVoidProtection, mConsume, mBatchMode, mBuilt;
    private float mDurationMultiplier;

    public GT_ParallelHelper() {}

    /**
     * Enables void protection on a multiblock. This allows the multi to not void if it produces too much
     */
    public GT_ParallelHelper enableVoidProtection(GT_MetaTileEntity_MultiBlockBase aMachineMeta) {
        mVoidProtection = true;
        mMachineMeta = aMachineMeta;
        return this;
    }

    public GT_ParallelHelper enableVoidProtection(MultiBlockController aMachineMulti) {
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
     * Enables Batch mode. Can do up to an additional processed recipes of mCurrentParallel * mBatchModifier
     * A batch modifier of 1 does nothing
     */
    public GT_ParallelHelper enableBatchMode(int aBatchModifier) {
        mBatchMode = true;
        mBatchModifier = aBatchModifier;
        return this;
    }

    public GT_ParallelHelper build() {
        mBuilt = true;
        doStuff();
        return this;
    }

    private void doStuff() {
        ItemStack[] tItemInputs = mConsume ? mItemInputs : mItemInputs.clone();
        FluidStack[] tFluidInputs = mConsume ? mFluidInputs : mFluidInputs.clone();
        boolean tMEOutputBus = false;
        boolean tMEOutputHatch = false;
        long tCurrentUsage = 0;
        int tMaxCurrentParallel = mMaxParallel;
        Map<FluidStack, Long> tFluidOutputMap = new HashMap<FluidStack, Long>();
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

            if (!tMEOutputBus || !tMEOutputHatch) {
                int tMaxParallelsFluids = !tMEOutputHatch ? calculateMaxParallelsForHatches() : mMaxParallel;
                int tMaxParallelsItems = !tMEOutputHatch ? calculateMaxParallelsForBusses() : mMaxParallel;
                tMaxCurrentParallel = Math.min(mMaxParallel, Math.min(tMaxParallelsFluids, tMaxParallelsItems));
            }
        }

        while (mRecipe.isRecipeInputEqual(false, true, tFluidInputs, tItemInputs)
                && mAvailableEUt > tCurrentUsage + mRecipe.mEUt
                && mCurrentParallel <= tMaxCurrentParallel) {
            mRecipe.isRecipeInputEqual(true, true, tFluidInputs, tItemInputs);
            tCurrentUsage += mRecipe.mEUt;
            mCurrentParallel++;
        }

        if (mBatchMode) {
            int tExtraParallels = 0;
            while (mRecipe.isRecipeInputEqual(true, true, tFluidInputs, tItemInputs)
                    && tExtraParallels <= mCurrentParallel * mBatchModifier) {
                tExtraParallels++;
            }
            mCurrentParallel += tExtraParallels;
            mDurationMultiplier = 1.0f + 128.0f / tExtraParallels;
        }
    }

    private int calculateMaxParallelsForHatches() {
        // For now we are gonna ignore MuTEs existence as there are no recipes for them
        if (mRecipe.mFluidOutputs.length > 0
                && mMachineMeta != null
                && mMachineMeta.mOutputHatches.size() >= mRecipe.mFluidOutputs.length) {}

        return 0;
    }

    private int calculateMaxParallelsForBusses() {
        // Same thing we are gonna ignore MuTEs existence for now. should be in theory the same later
        Map<ItemStack, Integer> tItemOutputMap = new ItemStackMap<>();
        int tSlotsFree = 0;
        for (ItemStack tItem : mRecipe.mOutputs) {
            tItemOutputMap.merge(tItem, tItem.stackSize * mMaxParallel, Integer::sum);
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
                        Integer tOutputCountBoxed = tItemOutputMap.get(tBusStack);
                        if (tOutputCountBoxed == null)
                            // we don't have a matching stack to output, ignore this bus stack
                            continue;
                        int tOutputCount = tOutputCountBoxed;
                        if (tOutputCount <= tSpaceLeft) {
                            // completely fits in this bus stack, remove this input stack
                            tItemOutputMap.remove(tBusStack);
                        } else {
                            // have left over
                            tItemOutputMap.put(tBusStack, tOutputCount - tSpaceLeft);
                        }
                    }
                }
            }
            if (tItemOutputMap.size() > 0) {
                int tTotalLeftStacks = tItemOutputMap.entrySet().stream()
                        .mapToInt(GT_ParallelHelper::toStackCount)
                        .sum();
                if (tTotalLeftStacks > tSlotsFree) {
                    return (int) Math.floor((double) tSlotsFree / tTotalLeftStacks * mMaxParallel);
                    // We do not have enough free slots in total to accommodate the remaining managed stacks.
                }
            }
        }
        return 0;
    }

    private static int toStackCount(Entry<ItemStack, Integer> e) {
        int tMaxStackSize = e.getKey().getMaxStackSize();
        int tStackSize = e.getValue();
        return (tStackSize + tMaxStackSize - 1) / tMaxStackSize;
    }
}
