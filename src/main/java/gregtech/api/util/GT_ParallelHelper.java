package gregtech.api.util;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.SingleRecipeCheck;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class GT_ParallelHelper {

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
    private double mDurationMultiplier;
    /**
     * Modifier which is applied on the recipe eut. Useful for GT++ machines
     */
    private float mEUtModifier = 1;

    public GT_ParallelHelper() {}

    /**
     * Sets MetaTE controller, with current configuration for void protection mode.
     *
     * @deprecated Use {@link #setMachine(IVoidable)}
     */
    @Deprecated
    public GT_ParallelHelper setController(GT_MetaTileEntity_MultiBlockBase machineMeta) {
        return setMachine(machineMeta, machineMeta.protectsExcessItem(), machineMeta.protectsExcessFluid());
    }

    /**
     * Sets MetaTE controller, with void protection mode forcibly.
     *
     * @deprecated Use {@link #setMachine(IVoidable, boolean, boolean)}
     */
    @Deprecated
    public GT_ParallelHelper setController(GT_MetaTileEntity_MultiBlockBase machineMeta, boolean protectExcessItem,
        boolean protectExcessFluid) {
        return setMachine(machineMeta, protectExcessItem, protectExcessFluid);
    }

    /**
     * Sets machine, with current configuration for void protection mode.
     */
    public GT_ParallelHelper setMachine(IVoidable machine) {
        return setMachine(machine, machine.protectsExcessItem(), machine.protectsExcessFluid());
    }

    /**
     * Sets machine, with void protection mode forcibly.
     */
    public GT_ParallelHelper setMachine(IVoidable machine, boolean protectExcessItem, boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.machine = machine;
        return this;
    }

    /**
     * Sets the recipe, which will be used for the parallel calculation
     */
    public GT_ParallelHelper setRecipe(@Nonnull GT_Recipe aRecipe) {
        mRecipe = Objects.requireNonNull(aRecipe);
        return this;
    }

    public GT_ParallelHelper setRecipeLocked(IRecipeLockable singleRecipeMachine, boolean isRecipeLocked) {
        this.singleRecipeMachine = singleRecipeMachine;
        this.isRecipeLocked = isRecipeLocked;
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
        mBatchMode = aBatchModifier > 1;
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
        if (mRecipe == null) {
            throw new IllegalStateException("Recipe is not set");
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
    public double getDurationMultiplierDouble() {
        if (!mBuilt) {
            throw new IllegalStateException("Tried to get duration multiplier before building");
        }
        if (mBatchMode) {
            return mDurationMultiplier;
        }
        return 1;
    }

    /**
     * @deprecated Use {@link #getDurationMultiplierDouble()}
     */
    @Deprecated
    public float getDurationMultiplier() {
        return (float) getDurationMultiplierDouble();
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
        if (mItemInputs == null) {
            mItemInputs = new ItemStack[0];
        }
        if (mFluidInputs == null) {
            mFluidInputs = new FluidStack[0];
        }

        ItemStack[] tItemInputs;
        FluidStack[] tFluidInputs;
        // see if people want to consume their inputs or not
        if (mConsume) {
            tItemInputs = mItemInputs;
            tFluidInputs = mFluidInputs;
        } else {
            // copy to prevent consuming original inputs
            tItemInputs = new ItemStack[mItemInputs.length];
            for (int i = 0; i < mItemInputs.length; i++) {
                tItemInputs[i] = mItemInputs[i].copy();
            }
            tFluidInputs = new FluidStack[mFluidInputs.length];
            for (int i = 0; i < mFluidInputs.length; i++) {
                tFluidInputs[i] = mFluidInputs[i].copy();
            }
        }

        if (mBatchMode) {
            mMaxParallel *= mBatchModifier;
        }

        SingleRecipeCheck recipeCheck = null;
        SingleRecipeCheck.Builder tSingleRecipeCheckBuilder = null;
        if (isRecipeLocked && singleRecipeMachine != null) {
            recipeCheck = singleRecipeMachine.getSingleRecipeCheck();
            if (recipeCheck == null) {
                // Machine is configured to lock to a single recipe, but haven't built the recipe checker yet.
                // Build the checker on next successful recipe.
                GT_Recipe.GT_Recipe_Map recipeMap = singleRecipeMachine.getRecipeMap();
                if (recipeMap != null) {
                    tSingleRecipeCheckBuilder = SingleRecipeCheck.builder(recipeMap)
                        .setBefore(tItemInputs, tFluidInputs);
                }
            }
        }

        // Let's look at how many parallels we can get with void protection
        if (protectExcessItem || protectExcessFluid) {
            if (machine == null) {
                throw new IllegalStateException("Tried to calculate void protection, but machine is not set");
            }
            VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper();
            voidProtectionHelper.setMachine(machine)
                .setItemOutputs(mRecipe.mOutputs)
                .setFluidOutputs(mRecipe.mFluidOutputs)
                .setMaxParallel(mMaxParallel)
                .build();
            mMaxParallel = Math.min(voidProtectionHelper.getMaxParallel(), mMaxParallel);
        }

        final int tRecipeEUt = (int) Math.ceil(mRecipe.mEUt * mEUtModifier);
        final int batchCorrectedMaxParallel = mMaxParallel / mBatchModifier;
        // Consume inputs to determine normal parallel
        if (recipeCheck != null) {
            int actualMaxParallel = (int) Math.min(batchCorrectedMaxParallel, mAvailableEUt / tRecipeEUt);
            mCurrentParallel = recipeCheck.checkRecipeInputs(true, actualMaxParallel, tItemInputs, tFluidInputs);
        } else {
            long tCurrentUsage = 0;
            boolean builtRecipeCheck = false;
            for (; mCurrentParallel < batchCorrectedMaxParallel
                && tCurrentUsage < (mAvailableEUt - tRecipeEUt); mCurrentParallel++) {
                if (!mRecipe.isRecipeInputEqual(true, false, tFluidInputs, tItemInputs)) {
                    break;
                }
                tCurrentUsage += tRecipeEUt;
                if (tSingleRecipeCheckBuilder != null && !builtRecipeCheck) {
                    // If recipe checker is not built yet, build and set it
                    SingleRecipeCheck builtCheck = tSingleRecipeCheckBuilder.setAfter(tItemInputs, tFluidInputs)
                        .setRecipe(mRecipe)
                        .build();
                    singleRecipeMachine.setSingleRecipeCheck(builtCheck);
                    builtRecipeCheck = true;
                }
            }
        }

        // If Batch Mode is enabled determine how many extra parallels we can get
        if (mBatchMode && mCurrentParallel > 0) {
            int tExtraParallels = 0;
            final int maxExtraParallels = mCurrentParallel * (mBatchModifier - 1);
            if (recipeCheck != null) {
                tExtraParallels = recipeCheck.checkRecipeInputs(true, maxExtraParallels, tItemInputs, tFluidInputs);
            } else {
                while (tExtraParallels < maxExtraParallels
                    && mRecipe.isRecipeInputEqual(true, false, tFluidInputs, tItemInputs)) {
                    tExtraParallels++;
                }
            }
            mDurationMultiplier = 1.0f + (float) tExtraParallels / mCurrentParallel;
            mCurrentParallel += tExtraParallels;
        }

        // If we want to calculate outputs we do it here
        if (mCalculateOutputs && mCurrentParallel > 0) {
            if (mRecipe.mOutputs != null) {
                calculateItemOutputs();
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

    protected void calculateItemOutputs() {
        mItemOutputs = new ItemStack[mRecipe.mOutputs.length];
        for (int i = 0; i < mRecipe.mOutputs.length; i++) {
            if (mRecipe.getOutputChance(i) >= 10000) {
                ItemStack item = mRecipe.getOutput(i)
                    .copy();
                item.stackSize *= mCurrentParallel;
                mItemOutputs[i] = item;
                continue;
            }
            int items = 0;
            int itemStackSize = mRecipe.getOutput(i).stackSize;
            for (int roll = 0; roll < mCurrentParallel; roll++) {
                if (mRecipe.getOutputChance(i) >= XSTR.XSTR_INSTANCE.nextInt(10000)) {
                    items += itemStackSize;
                }
            }
            ItemStack item = mRecipe.getOutput(i)
                .copy();
            item.stackSize = items;
            mItemOutputs[i] = item;
        }
    }
}
