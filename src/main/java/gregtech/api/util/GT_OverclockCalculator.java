package gregtech.api.util;

public class GT_OverclockCalculator {
    private long mAmps = 1, mEUt = 0, mRecipeEUt = 0;
    private float mEUtDiscount = 1, mSpeedBoost = 1, mHeatDiscount = 0.95f;
    private int mEUtIncrasePerOC = 2,
            mDurationDecresePerOC = 1,
            mDuration = 0,
            mParallel = 1,
            mRecipeHeat = 0,
            mMultiHeat = 0;
    private boolean mHeatOC, mOneTickDiscount, calculated;

    /**
     * An Overclock helper for calculating overclocks in many different situations
     */
    public GT_OverclockCalculator() {}

    /**
     * @param aRecipeEUt Sets the Recipe's starting voltage
     */
    public GT_OverclockCalculator setRecipeEUt(long aRecipeEUt) {
        mRecipeEUt = aRecipeEUt;
        return this;
    }

    /**
     * @param aEUt Sets the EUt that the multiblock can use. This is the voltage of the multi
     */
    public GT_OverclockCalculator setEUt(long aEUt) {
        mEUt = aEUt;
        return this;
    }

    /**
     * @param aDuration Sets the duration of the recipe
     */
    public GT_OverclockCalculator setDuration(int aDuration) {
        mDuration = aDuration;
        return this;
    }

    public GT_OverclockCalculator setAmperage(long aAmps) {
        mAmps = aAmps;
        return this;
    }

    /**
     * Enables Perfect OC in calculation
     */
    public GT_OverclockCalculator enablePerfectOC() {
        mDurationDecresePerOC = 2;
        return this;
    }

    /**
     * Enables calculating overclocking using EBF's dicounts and the like
     */
    public GT_OverclockCalculator enableHeatOC() {
        mHeatOC = true;
        return this;
    }

    /**
     * Sets the starting heat of the recipe
     */
    public GT_OverclockCalculator setRecipeHeat(int aRecipeHeat) {
        mRecipeHeat = aRecipeHeat;
        return this;
    }

    /**
     * Sets the heat of the coils on the multi
     */
    public GT_OverclockCalculator setMultiHeat(int aMultiHeat) {
        mMultiHeat = aMultiHeat;
        return this;
    }

    /**
     * Sets an EUtDiscount. 0.9 is 10% more energy. 1.1 is 10% less energy
     */
    public GT_OverclockCalculator setEUtDiscount(float aEUtDiscount) {
        mEUtDiscount = aEUtDiscount;
        return this;
    }

    /**
     * Sets a Speed Boost for the multiblock. 0.9 is 10% slower. 1.1 is 10% faster
     */
    public GT_OverclockCalculator setSpeedBoost(float aSpeedBoost) {
        mSpeedBoost = aSpeedBoost;
        return this;
    }

    /**
     * Sets the parallel that the multiblock uses
     */
    public GT_OverclockCalculator setParallel(int aParallel) {
        mParallel = aParallel;
        return this;
    }

    /**
     * Sets the heat discount during OC calculation if HeatOC is used. Default: 0.95 = 5% discount
     * Used like a EU/t Discount
     */
    public GT_OverclockCalculator setHeatDiscount(float aHeatDiscount) {
        mHeatDiscount = aHeatDiscount;
        return this;
    }

    /**
     * Sets the amount that the EUt increases per overclock. This uses BitShifting! Default is 2, which is a 4x increase
     */
    public GT_OverclockCalculator setEUtIncreasePerOC(int aEUtIncreasePerOC) {
        mEUtIncrasePerOC = aEUtIncreasePerOC;
        return this;
    }

    /**
     * Sets the amount that the duration decreases per overclock. This uses BitShifting! Default is 1, which halves the duration
     */
    public GT_OverclockCalculator setDurationDecreasePerOC(int aDurationDecreasePerOC) {
        mDurationDecresePerOC = aDurationDecreasePerOC;
        return this;
    }

    /**
     * Enables One Tick Discount on EUt based on Duration Decrease Per Overclock. This functions the same as single blocks.
     */
    public GT_OverclockCalculator enableOneTickDiscount() {
        mOneTickDiscount = true;
        return this;
    }

    /**
     * Call this when all values have been put it.
     */
    public GT_OverclockCalculator calculate() {
        CalculateOverclock();
        calculated = true;
        return this;
    }

    private void CalculateOverclock() {
        if (mRecipeEUt > mEUt || mRecipeHeat > mMultiHeat) {
            mRecipeEUt = Long.MAX_VALUE;
            mDuration = Integer.MAX_VALUE;
            return;
        }

        mRecipeEUt = (long) Math.ceil(mRecipeEUt / mEUtDiscount);
        mDuration = (int) Math.ceil(mDuration / mSpeedBoost);
        int heatDiscounts = (mMultiHeat - mRecipeHeat) / 900;
        if (mHeatOC) {
            while (mRecipeHeat + 1800 <= mMultiHeat && mRecipeEUt * mParallel << 2 < mEUt * mAmps) {
                if (mDuration < 1) {
                    break;
                }
                mRecipeEUt <<= mEUtIncrasePerOC;
                mDuration >>= 2;
                mRecipeHeat += 1800;
            }
        }

        while (mRecipeEUt * mParallel << 2 < mEUt * mAmps) {
            if (mDuration <= 1) {
                break;
            }
            mRecipeEUt <<= mEUtIncrasePerOC;
            mDuration >>= mDurationDecresePerOC;
        }

        if (mDuration < 1) {
            mDuration = 1;
        }

        if (mHeatOC) {
            mRecipeEUt = (long) Math.ceil(mRecipeEUt * Math.pow(mHeatDiscount, heatDiscounts));
        }

        if (mOneTickDiscount) {
            int voltageDifferece = GT_Utility.getTier(mEUt) - GT_Utility.getTier(mRecipeEUt);
            mRecipeEUt >>= voltageDifferece * mDurationDecresePerOC;
            if (mRecipeEUt < 1) {
                mRecipeEUt = 1;
            }
        }

        mRecipeEUt *= mParallel;
    }

    /**
     * @return The consumtipn after overclock has been calculated
     */
    public long getConsumption() throws Exception {
        if (!calculated) {
            throw new Exception("Trtying to get consumption before calculating!");
        }
        return mRecipeEUt;
    }

    /**
     * @return The duration of the recipe after overclock has been calculated
     */
    public int getDuration() throws Exception {
        if (!calculated) {
            if (!calculated) {
                throw new Exception("Trtying to get duration before calculating!");
            }
        }
        return mDuration;
    }
}
