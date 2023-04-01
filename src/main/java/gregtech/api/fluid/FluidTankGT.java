package gregtech.api.fluid;

import static com.google.common.primitives.Ints.saturatedCast;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import gregtech.api.util.GT_Utility;

public class FluidTankGT implements IFluidTank {

    public final FluidTankGT[] AS_ARRAY = new FluidTankGT[] { this };
    private FluidStack mFluid;
    private long mCapacity = 0, mAmount = 0;
    private boolean mPreventDraining = false, mVoidExcess = false, mChangedFluids = false;
    /** HashMap of adjustable Tank Sizes based on Fluids if needed. */
    private Map<String, Long> mAdjustableCapacity = null;

    private long mAdjustableMultiplier = 1;
    /** Gives you a Tank Index in case there is multiple Tanks on a TileEntity that cares. */
    public int mIndex = 0;

    public FluidTankGT() {
        mCapacity = Long.MAX_VALUE;
    }

    public FluidTankGT(long aCapacity) {
        mCapacity = aCapacity;
    }

    public FluidTankGT(FluidStack aFluid) {
        mFluid = aFluid;
        if (aFluid != null) {
            mCapacity = aFluid.amount;
            mAmount = aFluid.amount;
        }
    }

    public FluidTankGT(FluidStack aFluid, long aCapacity) {
        mFluid = aFluid;
        mCapacity = aCapacity;
        mAmount = (aFluid == null ? 0 : aFluid.amount);
    }

    public FluidTankGT(FluidStack aFluid, long aAmount, long aCapacity) {
        mFluid = aFluid;
        mCapacity = aCapacity;
        mAmount = (aFluid == null ? 0 : aAmount);
    }

    public FluidTankGT(Fluid aFluid, long aAmount) {
        this(new FluidStack(aFluid, saturatedCast(aAmount)));
        mAmount = aAmount;
    }

    public FluidTankGT(Fluid aFluid, long aAmount, long aCapacity) {
        this(new FluidStack(aFluid, saturatedCast(aAmount)), aCapacity);
        mAmount = aAmount;
    }

    public FluidTankGT(NBTTagCompound aNBT, String aKey, long aCapacity) {
        this(aNBT.hasKey(aKey) ? aNBT.getCompoundTag(aKey) : null, aCapacity);
    }

    public FluidTankGT(NBTTagCompound aNBT, long aCapacity) {
        mCapacity = aCapacity;
        if (aNBT != null && !aNBT.hasNoTags()) {
            mFluid = FluidStack.loadFluidStackFromNBT(aNBT);
            mAmount = (isEmpty() ? 0 : aNBT.hasKey("LAmount") ? aNBT.getLong("LAmount") : mFluid.amount);
        }
    }

    public FluidTankGT readFromNBT(NBTTagCompound aNBT, String aKey) {
        if (aNBT.hasKey(aKey)) {
            aNBT = aNBT.getCompoundTag(aKey);
            if (aNBT != null && !aNBT.hasNoTags()) {
                mFluid = FluidStack.loadFluidStackFromNBT(aNBT);
                mAmount = (isEmpty() ? 0 : aNBT.hasKey("LAmount") ? aNBT.getLong("LAmount") : mFluid.amount);
            }
        }
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound aNBT, String aKey) {
        if (mFluid != null && (mPreventDraining || mAmount > 0)) {
            final NBTTagCompound tNBT = new NBTTagCompound();
            mFluid.amount = (int) mAmount;
            aNBT.setTag(aKey, mFluid.writeToNBT(tNBT));
            if (mAmount > Integer.MAX_VALUE) tNBT.setLong("LAmount", mAmount);
        } else {
            aNBT.removeTag(aKey);
        }
        return aNBT;
    }

    public FluidStack drain(int aDrained) {
        return drain(aDrained, true);
    }

    @Override
    public FluidStack drain(int aDrained, boolean aDoDrain) {
        if (isEmpty() || aDrained <= 0) return null;
        if (mAmount < aDrained) aDrained = (int) mAmount;
        final FluidStack rFluid = new FluidStack(mFluid, aDrained);
        if (aDoDrain) {
            mAmount -= aDrained;
            if (mAmount <= 0) {
                if (mPreventDraining) {
                    mAmount = 0;
                } else {
                    setEmpty();
                }
            }
        }
        return rFluid;
    }

    public boolean drainAll(long aDrained) {
        if (isEmpty() || mAmount < aDrained) return false;
        mAmount -= aDrained;
        if (mAmount <= 0) {
            if (mPreventDraining) {
                mAmount = 0;
            } else {
                setEmpty();
            }
        }
        return true;
    }

    public long remove(long aDrained) {
        if (isEmpty() || mAmount <= 0 || aDrained <= 0) return 0;
        if (mAmount < aDrained) aDrained = mAmount;
        mAmount -= aDrained;
        if (mAmount <= 0) {
            if (mPreventDraining) {
                mAmount = 0;
            } else {
                setEmpty();
            }
        }
        return aDrained;
    }

    public long add(long aFilled) {
        if (isEmpty() || aFilled <= 0) return 0;
        final long tCapacity = capacity();
        if (mAmount + aFilled > tCapacity) {
            if (!mVoidExcess) aFilled = tCapacity - mAmount;
            mAmount = tCapacity;
            return aFilled;
        }
        mAmount += aFilled;
        return aFilled;
    }

    public long add(long aFilled, FluidStack aFluid) {
        if (aFluid == null || aFilled <= 0) return 0;
        if (isEmpty()) {
            mFluid = aFluid.copy();
            mChangedFluids = true;
            mAmount = Math.min(capacity(aFluid), aFilled);
            return mVoidExcess ? aFilled : mAmount;
        }
        return contains(aFluid) ? add(aFilled) : 0;
    }

    public int fill(FluidStack aFluid) {
        return fill(aFluid, true);
    }

    @Override
    public int fill(FluidStack aFluid, boolean aDoFill) {
        if (aFluid == null) return 0;
        if (aDoFill) {
            if (isEmpty()) {
                mFluid = aFluid.copy();
                mChangedFluids = true;
                mAmount = Math.min(capacity(aFluid), aFluid.amount);
                return mVoidExcess ? aFluid.amount : (int) mAmount;
            }
            if (!contains(aFluid)) return 0;
            final long tCapacity = capacity(aFluid);
            long tFilled = tCapacity - mAmount;
            if (aFluid.amount < tFilled) {
                mAmount += aFluid.amount;
                tFilled = aFluid.amount;
            } else mAmount = tCapacity;
            return mVoidExcess ? aFluid.amount : (int) tFilled;
        }
        return saturatedCast(
                isEmpty() ? mVoidExcess ? aFluid.amount : Math.min(capacity(aFluid), aFluid.amount)
                        : contains(aFluid)
                                ? mVoidExcess ? aFluid.amount : Math.min(capacity(aFluid) - mAmount, aFluid.amount)
                                : 0);
    }

    public boolean canFillAll(FluidStack aFluid) {
        return aFluid == null || aFluid.amount <= 0
                || (isEmpty() ? mVoidExcess || aFluid.amount <= capacity(aFluid)
                        : contains(aFluid) && (mVoidExcess || mAmount + aFluid.amount <= capacity(aFluid)));
    }

    public boolean canFillAll(long aAmount) {
        return aAmount <= 0 || mVoidExcess || mAmount + aAmount <= capacity();
    }

    public boolean fillAll(FluidStack aFluid) {
        if (aFluid == null || aFluid.amount <= 0) return true;
        if (isEmpty()) {
            final long tCapacity = capacity(aFluid);
            if (aFluid.amount <= tCapacity || mVoidExcess) {
                mFluid = aFluid.copy();
                mChangedFluids = true;
                mAmount = aFluid.amount;
                if (mAmount > tCapacity) mAmount = tCapacity;
                return true;
            }
            return false;
        }
        if (contains(aFluid)) {
            if (mAmount + aFluid.amount <= capacity()) {
                mAmount += aFluid.amount;
                return true;
            }
            if (mVoidExcess) {
                mAmount = capacity();
                return true;
            }
        }
        return false;
    }

    public boolean fillAll(FluidStack aFluid, long aMultiplier) {
        if (aMultiplier <= 0) return true;
        if (aMultiplier == 1) return fillAll(aFluid);
        if (aFluid == null || aFluid.amount <= 0) return true;
        if (isEmpty()) {
            final long tCapacity = capacity(aFluid);
            if (aFluid.amount * aMultiplier <= tCapacity || mVoidExcess) {
                mFluid = aFluid.copy();
                mChangedFluids = true;
                mAmount = aFluid.amount * aMultiplier;
                if (mAmount > tCapacity) mAmount = tCapacity;
                return true;
            }
            return false;
        }
        if (contains(aFluid)) {
            if (mAmount + aFluid.amount * aMultiplier <= capacity()) {
                mAmount += aFluid.amount * aMultiplier;
                return true;
            }
            if (mVoidExcess) {
                mAmount = capacity();
                return true;
            }
        }
        return false;
    }

    /** Resets Tank Contents entirely */
    public FluidTankGT setEmpty() {
        mFluid = null;
        mChangedFluids = true;
        mAmount = 0;
        return this;
    }

    /** Sets Fluid Content, taking Amount from the Fluid Parameter */
    public FluidTankGT setFluid(FluidStack aFluid) {
        mFluid = aFluid;
        mChangedFluids = true;
        mAmount = (aFluid == null ? 0 : aFluid.amount);
        return this;
    }

    /** Sets Fluid Content and Amount */
    public FluidTankGT setFluid(FluidStack aFluid, long aAmount) {
        mFluid = aFluid;
        mChangedFluids = true;
        mAmount = (aFluid == null ? 0 : aAmount);
        return this;
    }

    /** Sets Fluid Content, taking Amount from the Tank Parameter */
    public FluidTankGT setFluid(FluidTankGT aTank) {
        mFluid = new FluidStack(aTank.mFluid, saturatedCast(aTank.mAmount));
        mChangedFluids = true;
        mAmount = aTank.mAmount;
        return this;
    }

    /** Sets the Tank Index for easier Reverse Mapping. */
    public FluidTankGT setIndex(int aIndex) {
        mIndex = aIndex;
        return this;
    }

    /** Sets the Capacity */
    public FluidTankGT setCapacity(long aCapacity) {
        if (aCapacity >= 0) mCapacity = aCapacity;
        return this;
    }

    /** Sets the Capacity Multiplier */
    public FluidTankGT setCapacityMultiplier(long aCapacityMultiplier) {
        if (aCapacityMultiplier >= 0) mAdjustableMultiplier = aCapacityMultiplier;
        return this;
    }

    /** Sets Tank capacity Map, should it be needed. */
    public FluidTankGT setCapacity(Map<String, Long> aMap, long aCapacityMultiplier) {
        mAdjustableCapacity = aMap;
        mAdjustableMultiplier = aCapacityMultiplier;
        return this;
    }

    /** Always keeps at least 0 Liters of Fluid instead of setting it to null */
    public FluidTankGT setPreventDraining() {
        return setPreventDraining(true);
    }

    /** Always keeps at least 0 Liters of Fluid instead of setting it to null */
    public FluidTankGT setPreventDraining(boolean aPrevent) {
        mPreventDraining = aPrevent;
        return this;
    }

    /** Voids any Overlow */
    public FluidTankGT setVoidExcess() {
        return setVoidExcess(true);
    }

    /** Voids any Overlow */
    public FluidTankGT setVoidExcess(boolean aVoidExcess) {
        mVoidExcess = aVoidExcess;
        return this;
    }

    public boolean isFull() {
        return mFluid != null && mAmount >= capacity();
    }

    public long capacity() {
        return capacity(mFluid);
    }

    public long capacity(FluidStack aFluid) {
        if (mAdjustableCapacity == null || aFluid == null) return mCapacity;
        return capacity(aFluid.getFluid());
    }

    public long capacity(Fluid aFluid) {
        if (mAdjustableCapacity == null || aFluid == null) return mCapacity;
        return capacity(aFluid.getName());
    }

    public long capacity(String aFluid) {
        if (mAdjustableCapacity == null || aFluid == null) return mCapacity;

        final Long tSize = mAdjustableCapacity.get(aFluid);
        return tSize == null ? Math.max(mAmount, mCapacity)
                : Math.max(tSize * mAdjustableMultiplier, Math.max(mAmount, mCapacity));
    }

    public boolean isHalf() {
        return mFluid != null && mAmount * 2 >= capacity();
    }

    public boolean contains(Fluid aFluid) {
        return mFluid != null && mFluid.getFluid() == aFluid;
    }

    public boolean contains(FluidStack aFluid) {
        return GT_Utility.areFluidsEqual(mFluid, aFluid);
    }

    public boolean has(long aAmount) {
        return mAmount >= aAmount;
    }

    public boolean has() {
        return mAmount > 0;
    }

    public boolean check() {
        if (mChangedFluids) {
            mChangedFluids = false;
            return true;
        }
        return false;
    }

    public boolean update() {
        return mChangedFluids = true;
    }

    public boolean changed() {
        return mChangedFluids;
    }

    public long amount() {
        return isEmpty() ? 0 : mAmount;
    }

    public boolean isEmpty() {
        return mFluid == null;
    }

    public long amount(long aMax) {
        return isEmpty() || aMax <= 0 ? 0 : Math.min(mAmount, aMax);
    }

    public String name() {
        return mFluid == null ? null
                : mFluid.getFluid()
                        .getName();
    }

    public FluidStack get() {
        return mFluid;
    }

    public FluidStack get(long aMax) {
        return isEmpty() || aMax <= 0 ? null : new FluidStack(mFluid, saturatedCast(Math.min(mAmount, aMax)));
    }

    @Override
    public FluidStack getFluid() {
        if (mFluid != null) mFluid.amount = saturatedCast(mAmount);
        return mFluid;
    }

    @Override
    public int getFluidAmount() {
        return saturatedCast(mAmount);
    }

    @Override
    public int getCapacity() {
        return saturatedCast(capacity());
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(isEmpty() ? null : mFluid.copy(), saturatedCast(capacity()));
    }
}
