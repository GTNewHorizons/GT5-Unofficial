package gregtech.api.util;

public class GT_OverclockCalculator {

    /**
     * @mAmps - Amperage of the multiblock
     * @mEUt - Voltage of the multiblock
     * @mRecipeEUt - Voltage the recipe will run at
     * @mRecipeAmps - The amount of amps the recipe needs
     */
    private long mAmps = 1, mEUt = 0, mRecipeEUt = 0, mRecipeAmps = 1;
    /**
     * @mEUtDiscount - Discount for EUt at the beginning of calculating overclocks, like GT++ machines
     * @mSpeedBoost - Speeding/Slowing up/down the duration of a recipe at the beginning of calculating overclocks, like
     *              GT++ machines
     * @mHeatDiscountAmont - The value used for discount final eut per 900 heat
     */
    private float mEUtDiscount = 1, mSpeedBoost = 1, mHeatDiscountAmount = 0.95f;
    /**
     * @mEUtIncreasePerOC - How much the bits should be moved to the left when it is overclocking (Going up, 2 meaning
     *                    it is multiplied with 4x)
     * @mDurationDecreasePerOC - How much the bits should be moved to the right when its overclocking (Going down, 1
     *                         meaning it is halved)
     * @mDuration - Duration of the recipe
     * @mParallel - The parallel the multi has when trying to overclock
     * @mRecipeHeat - The min heat required for the recipe
     * @mMultiHeat - The heat the multi has when starting the recipe
     * @mHeatPerfectOC - How much the bits should be moved to the right for each 1800 above recipe heat (Used for
     *                 duration)
     */
    private int mEUtIncreasePerOC = 2, mDurationDecreasePerOC = 1, mDuration = 0, mParallel = 1, mRecipeHeat = 0,
        mMultiHeat = 0, mHeatPerfectOC = 2;
    /**
     * @mHeatOC - Whether to enable overclocking with heat like the EBF every 1800 heat difference
     * @mOneTickDiscount - Whether to give EUt Discount when the duration goes below one tick
     * @calculates - variable to check whether the overclocks have been calculated
     * @mHeatDiscount - Whether to enable heat discounts every 900 heat difference
     */
    private boolean mHeatOC, mOneTickDiscount, calculated, mHeatDiscount;

    private static final int HEAT_DISCOUNT_THRESHOLD = 900;
    private static final int HEAT_PERFECT_OVERCLOCK_THRESHOLD = 1800;

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

    /**
     * @param aAmps Sets the Amperage that the multi can support
     */
    public GT_OverclockCalculator setAmperage(long aAmps) {
        mAmps = aAmps;
        return this;
    }

    /**
     * @param aRecipeAmps Sets the Amperage of the recipe
     */
    public GT_OverclockCalculator setRecipeAmperage(long aRecipeAmps) {
        mRecipeAmps = aRecipeAmps;
        return this;
    }

    /**
     * Enables Perfect OC in calculation
     */
    public GT_OverclockCalculator enablePerfectOC() {
        mDurationDecreasePerOC = 2;
        return this;
    }

    /**
     * Enables calculating overclocking using EBF's perfectOC
     */
    public GT_OverclockCalculator enableHeatOC() {
        mHeatOC = true;
        return this;
    }

    /**
     * Enables adding a heat discount at the end of calculating an overclock, just like the EBF
     */
    public GT_OverclockCalculator enableHeatDiscount() {
        mHeatDiscount = true;
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
     * Sets an EUtDiscount. 0.9 is 10% less energy. 1.1 is 10% more energy
     */
    public GT_OverclockCalculator setEUtDiscount(float aEUtDiscount) {
        mEUtDiscount = aEUtDiscount;
        return this;
    }

    /**
     * Sets a Speed Boost for the multiblock. 0.9 is 10% faster. 1.1 is 10% slower
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
     * Sets the heat discount during OC calculation if HeatOC is used. Default: 0.95 = 5% discount Used like a EU/t
     * Discount
     */
    public GT_OverclockCalculator setHeatDiscount(float aHeatDiscount) {
        mHeatDiscountAmount = aHeatDiscount;
        return this;
    }

    /**
     * Sets the Overclock that should be calculated when one. This uses BitShifting! Default is 2, which is a 4x
     * decrease
     */
    public GT_OverclockCalculator setHeatPerfectOC(int aHeatPerfectOC) {
        mHeatPerfectOC = aHeatPerfectOC;
        return this;
    }

    /**
     * Sets the amount that the EUt increases per overclock. This uses BitShifting! Default is 2, which is a 4x increase
     */
    public GT_OverclockCalculator setEUtIncreasePerOC(int aEUtIncreasePerOC) {
        mEUtIncreasePerOC = aEUtIncreasePerOC;
        return this;
    }

    /**
     * Sets the amount that the duration decreases per overclock. This uses BitShifting! Default is 1, which halves the
     * duration
     */
    public GT_OverclockCalculator setDurationDecreasePerOC(int aDurationDecreasePerOC) {
        mDurationDecreasePerOC = aDurationDecreasePerOC;
        return this;
    }

    /**
     * Enables One Tick Discount on EUt based on Duration Decrease Per Overclock. This functions the same as single
     * blocks.
     */
    public GT_OverclockCalculator enableOneTickDiscount() {
        mOneTickDiscount = true;
        return this;
    }

    /**
     * Call this when all values have been put it.
     */
    public GT_OverclockCalculator calculate() {
        calculateOverclock();
        calculated = true;
        return this;
    }

    private void calculateOverclock() {
        if (mRecipeEUt > mEUt || mRecipeHeat > mMultiHeat) {
            mRecipeEUt = Long.MAX_VALUE;
            mDuration = Integer.MAX_VALUE;
            return;
        }
        int heatDiscounts = mHeatDiscount ? (mMultiHeat - mRecipeHeat) / HEAT_DISCOUNT_THRESHOLD : 0;
        double heatDiscountMultiplier = Math.pow(mHeatDiscountAmount, heatDiscounts);
        mDuration = (int) Math.ceil(mDuration * mSpeedBoost);
        if (mHeatOC) {
            while (mRecipeHeat + HEAT_PERFECT_OVERCLOCK_THRESHOLD <= mMultiHeat
                && (long) Math.ceil(mRecipeEUt * mParallel * mRecipeAmps * mEUtDiscount * heatDiscountMultiplier) << 2
                    < mEUt * mAmps) {
                if (mDuration < 1) {
                    break;
                }
                mRecipeEUt <<= mEUtIncreasePerOC;
                mDuration >>= mHeatPerfectOC;
                mRecipeHeat += HEAT_PERFECT_OVERCLOCK_THRESHOLD;
            }
        }

        int tRecipeTier = GT_Utility.getTier(mRecipeEUt);
        if (tRecipeTier == 0) {
            int tTier = GT_Utility.getTier(mEUt);
            int tTierDifference = tTier - 1;
            long tNextConsumption = ((long) Math
                .ceil(mRecipeEUt * mParallel * mRecipeAmps * mEUtDiscount * heatDiscountMultiplier))
                << mEUtIncreasePerOC;
            while (tTierDifference > 0 && tNextConsumption < mEUt * mAmps) {
                mRecipeEUt <<= mEUtIncreasePerOC;
                mDuration >>= mDurationDecreasePerOC;
                tNextConsumption <<= mEUtIncreasePerOC;
                tTierDifference--;
            }
        } else {
            long tNextConsumption = ((long) Math
                .ceil(mRecipeEUt * mParallel * mRecipeAmps * mEUtDiscount * heatDiscountMultiplier))
                << mEUtIncreasePerOC;
            while (tNextConsumption < mEUt * mAmps) {
                if (mDuration <= 1) {
                    break;
                }
                mRecipeEUt <<= mEUtIncreasePerOC;
                mDuration >>= mDurationDecreasePerOC;
                tNextConsumption <<= mEUtIncreasePerOC;
            }
        }

        if (mDuration < 1) {
            mDuration = 1;
        }

        if (mOneTickDiscount) {
            int voltageDifference = GT_Utility.getTier(mEUt) - GT_Utility.getTier(mRecipeEUt);
            mRecipeEUt >>= voltageDifference * mDurationDecreasePerOC;
            if (mRecipeEUt < 1) {
                mRecipeEUt = 1;
            }
        }

        mRecipeEUt = (long) Math.ceil(mRecipeEUt * mParallel * mRecipeAmps * mEUtDiscount * heatDiscountMultiplier);
    }

    /**
     * @return The consumption after overclock has been calculated
     */
    public long getConsumption() {
        if (!calculated) {
            calculate();
        }
        return mRecipeEUt;
    }

    /**
     * @return The duration of the recipe after overclock has been calculated
     */
    public int getDuration() {
        if (!calculated) {
            calculate();
        }
        return mDuration;
    }
}
