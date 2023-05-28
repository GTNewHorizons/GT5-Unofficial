package gregtech.api.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.multitileentity.multiblock.base.Controller;
import gregtech.api.objects.XSTR;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
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
     * Sets MetaTE controller, with current configuration for void protection mode.
     */
    public GT_ParallelHelper setController(GT_MetaTileEntity_MultiBlockBase machineMeta) {
        return setController(machineMeta, machineMeta.protectsExcessItem(), machineMeta.protectsExcessFluid());
    }

    /**
     * Sets MetaTE controller, with void protection mode forcibly.
     */
    public GT_ParallelHelper setController(GT_MetaTileEntity_MultiBlockBase machineMeta, boolean protectExcessItem,
        boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.mMachineMeta = machineMeta;
        return this;
    }

    /**
     * Sets MuTE controller, with current configuration for void protection mode.
     */
    public GT_ParallelHelper setController(Controller<?> machineMulti) {
        return setController(machineMulti, machineMulti.protectsExcessItem(), machineMulti.protectsExcessFluid());
    }

    /**
     * Sets MuTE controller, with void protection mode forcibly.
     */
    public GT_ParallelHelper setController(Controller<?> machineMulti, boolean protectExcessItem,
        boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.mMachineMulti = machineMulti;
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
        if (protectExcessItem || protectExcessFluid) {
            VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper();
            if (mMachineMeta != null) {
                voidProtectionHelper.setController(mMachineMeta, protectExcessItem, protectExcessFluid);
            } else if (mMachineMulti != null) {
                voidProtectionHelper.setController(mMachineMulti, protectExcessItem, protectExcessFluid);
            }
            voidProtectionHelper.setItemOutputs(mRecipe.mOutputs)
                .setFluidOutputs(mRecipe.mFluidOutputs)
                .setMaxParallel(mMaxParallel)
                .build();
            mMaxParallel = Math.min(voidProtectionHelper.getMaxParallel(), mMaxParallel);
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
}
