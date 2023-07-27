package gregtech.api.util;

import javax.annotation.Nonnull;

public class GT_OverclockCalculator {

    private static final double LOG4 = Math.log(4);

    /**
     * Voltage the recipe will run at
     */
    private long recipeVoltage = 0;
    /*
     * The amount of amps the recipe needs
     */
    private long recipeAmperage = 1;
    /**
     * Voltage of the machine
     */
    private long machineVoltage = 0;
    /**
     * Amperage of the machine
     */
    private long machineAmperage = 1;
    /**
     * Duration of the recipe
     */
    private int duration = 0;
    /**
     * The parallel the machine has when trying to overclock
     */
    private int parallel = 1;

    /**
     * The min heat required for the recipe
     */
    private int recipeHeat = 0;
    /**
     * The heat the machine has when starting the recipe
     */
    private int machineHeat = 0;
    /**
     * How much the bits should be moved to the right for each 1800 above recipe heat (Used for duration)
     */
    private int durationDecreasePerHeatOC = 2;
    /**
     * Whether to enable overclocking with heat like the EBF every 1800 heat difference
     */
    private boolean heatOC;
    /**
     * Whether to enable heat discounts every 900 heat difference
     */
    private boolean heatDiscount;
    /**
     * The value used for discount final eut per 900 heat
     */
    private double heatDiscountExponent = 0.95;

    /**
     * Discount for EUt at the beginning of calculating overclocks, like GT++ machines
     */
    private double eutDiscount = 1;
    /**
     * Speeding/Slowing up/down the duration of a recipe at the beginning of calculating overclocks, like
     * GT++ machines
     */
    private double speedBoost = 1;

    /**
     * How much the bits should be moved to the left when it is overclocking (Going up, 2 meaning it is multiplied with
     * 4x)-
     */
    private int eutIncreasePerOC = 2;
    /**
     * How much the bits should be moved to the right when its overclocking (Going down, 1 meaning it is halved)
     */
    private int durationDecreasePerOC = 1;
    /**
     * Whether to give EUt Discount when the duration goes below one tick
     */
    private boolean oneTickDiscount;
    /**
     * Whether the multi should use amperage to overclock with an exponent. Incompatible with amperageOC
     */
    private boolean laserOC;
    /**
     * Laser OC's penalty for using high amp lasers for overclocking. Like what the Adv. Assline is doing
     */
    private double laserOCPenalty = 0.3;
    /**
     * Whether the multi should use amperage to overclock normally. Incompatible with laserOC
     */
    private boolean amperageOC;
    /**
     * If the OC calculator should only do a given amount of overclocks. Mainly used in fusion reactors
     */
    private boolean limitOverclocks;
    /**
     * Maximum amount of overclocks to perform, when limitOverclocks = true
     */
    private int maxOverclocks;
    /**
     * How many overclocks have been performed
     */
    private int overclockCount;

    /**
     * variable to check whether the overclocks have been calculated
     */
    private boolean calculated;

    private static final int HEAT_DISCOUNT_THRESHOLD = 900;
    private static final int HEAT_PERFECT_OVERCLOCK_THRESHOLD = 1800;

    /**
     * Creates calculator that doesn't do OC at all. Will use recipe duration.
     */
    public static GT_OverclockCalculator ofNoOverclock(@Nonnull GT_Recipe recipe) {
        return ofNoOverclock(recipe.mEUt, recipe.mDuration);
    }

    /**
     * Creates calculator that doesn't do OC at all, with set duration.
     */
    public static GT_OverclockCalculator ofNoOverclock(long eut, int duration) {
        return new GT_OverclockCalculator().setRecipeEUt(eut)
            .setDuration(duration)
            .setEUt(eut);
    }

    /**
     * An Overclock helper for calculating overclocks in many different situations
     */
    public GT_OverclockCalculator() {}

    /**
     * Constructor for creating a new calculator with the save values
     * 
     * @param calculator Calculator to copy over
     */
    public GT_OverclockCalculator(@Nonnull GT_OverclockCalculator calculator) {
        this();
        setRecipeEUt(calculator.recipeVoltage);
        setRecipeAmperage(calculator.recipeAmperage);
        setEUt(calculator.machineVoltage);
        setAmperage(calculator.machineAmperage);
        setDuration(calculator.duration);
        setParallel(calculator.parallel);
        setRecipeHeat(calculator.recipeHeat);
        setMachineHeat(calculator.machineHeat);
        setHeatPerfectOC(calculator.durationDecreasePerHeatOC);
        setHeatOC(calculator.heatOC);
        setHeatDiscount(calculator.heatDiscount);
        setHeatDiscountMultiplier((float) calculator.heatDiscountExponent);
        setEUtDiscount((float) calculator.eutDiscount);
        setSpeedBoost((float) calculator.speedBoost);
        setEUtIncreasePerOC(calculator.eutIncreasePerOC);
        setDurationDecreasePerOC(calculator.durationDecreasePerOC);
        setOneTickDiscount(calculator.oneTickDiscount);
        setLaserOC(calculator.laserOC);
        setLaserOCPenalty(calculator.laserOCPenalty);
        setAmperageOC(calculator.amperageOC);
        maxOverclocks = calculator.maxOverclocks;
        limitOverclocks = calculator.limitOverclocks;
    }

    /**
     * @param recipeEUt Sets the Recipe's starting voltage
     */
    public GT_OverclockCalculator setRecipeEUt(long recipeEUt) {
        this.recipeVoltage = recipeEUt;
        return this;
    }

    /**
     * @param machineVoltage Sets the EUt that the machine can use. This is the voltage of the machine
     */
    public GT_OverclockCalculator setEUt(long machineVoltage) {
        this.machineVoltage = machineVoltage;
        return this;
    }

    /**
     * @param duration Sets the duration of the recipe
     */
    public GT_OverclockCalculator setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * @param machineAmperage Sets the Amperage that the machine can support
     */
    public GT_OverclockCalculator setAmperage(long machineAmperage) {
        this.machineAmperage = machineAmperage;
        return this;
    }

    /**
     * @param recipeAmperage Sets the Amperage of the recipe
     */
    public GT_OverclockCalculator setRecipeAmperage(long recipeAmperage) {
        this.recipeAmperage = recipeAmperage;
        return this;
    }

    /**
     * Enables Perfect OC in calculation
     */
    public GT_OverclockCalculator enablePerfectOC() {
        this.durationDecreasePerOC = 2;
        return this;
    }

    /**
     * Use {@link #setHeatOC(boolean)}
     */
    @Deprecated
    public GT_OverclockCalculator enableHeatOC() {
        return setHeatOC(true);
    }

    /**
     * Set if we should be calculating overclocking using EBF's perfectOC
     */
    public GT_OverclockCalculator setHeatOC(boolean heatOC) {
        this.heatOC = heatOC;
        return this;
    }

    /**
     * Use {@link #setHeatDiscount(boolean)}
     */
    @Deprecated
    public GT_OverclockCalculator enableHeatDiscount() {
        return setHeatDiscount(true);
    }

    /**
     * Sets if we should add a heat discount at the end of calculating an overclock, just like the EBF
     */
    public GT_OverclockCalculator setHeatDiscount(boolean heatDiscount) {
        this.heatDiscount = heatDiscount;
        return this;
    }

    /**
     * Sets the starting heat of the recipe
     */
    public GT_OverclockCalculator setRecipeHeat(int recipeHeat) {
        this.recipeHeat = recipeHeat;
        return this;
    }

    /**
     * Use {@link #setMachineHeat(int)}
     */
    @Deprecated
    public GT_OverclockCalculator setMultiHeat(int machineHeat) {
        return setMachineHeat(machineHeat);
    }

    /**
     * Sets the heat of the coils on the machine
     */
    public GT_OverclockCalculator setMachineHeat(int machineHeat) {
        this.machineHeat = machineHeat;
        return this;
    }

    /**
     * Sets an EUtDiscount. 0.9 is 10% less energy. 1.1 is 10% more energy
     */
    public GT_OverclockCalculator setEUtDiscount(float aEUtDiscount) {
        this.eutDiscount = aEUtDiscount;
        return this;
    }

    /**
     * Sets a Speed Boost for the multiblock. 0.9 is 10% faster. 1.1 is 10% slower
     */
    public GT_OverclockCalculator setSpeedBoost(float aSpeedBoost) {
        this.speedBoost = aSpeedBoost;
        return this;
    }

    /**
     * Sets the parallel that the multiblock uses
     */
    public GT_OverclockCalculator setParallel(int aParallel) {
        this.parallel = aParallel;
        return this;
    }

    /**
     * Sets the heat discount during OC calculation if HeatOC is used. Default: 0.95 = 5% discount Used like a EU/t
     * Discount
     */
    public GT_OverclockCalculator setHeatDiscountMultiplier(float heatDiscountExponent) {
        this.heatDiscountExponent = heatDiscountExponent;
        return this;
    }

    /**
     * Sets the Overclock that should be calculated when one. This uses BitShifting! Default is 2, which is a 4x
     * decrease
     */
    public GT_OverclockCalculator setHeatPerfectOC(int heatPerfectOC) {
        this.durationDecreasePerHeatOC = heatPerfectOC;
        return this;
    }

    /**
     * Sets the amount that the EUt increases per overclock. This uses BitShifting! Default is 2, which is a 4x increase
     */
    public GT_OverclockCalculator setEUtIncreasePerOC(int aEUtIncreasePerOC) {
        this.eutIncreasePerOC = aEUtIncreasePerOC;
        return this;
    }

    /**
     * Sets the amount that the duration decreases per overclock. This uses BitShifting! Default is 1, which halves the
     * duration
     */
    public GT_OverclockCalculator setDurationDecreasePerOC(int durationDecreasePerOC) {
        this.durationDecreasePerOC = durationDecreasePerOC;
        return this;
    }

    /**
     * Use {@link #setOneTickDiscount(boolean)}
     */
    @Deprecated
    public GT_OverclockCalculator enableOneTickDiscount() {
        return setOneTickDiscount(true);
    }

    /**
     * Set One Tick Discount on EUt based on Duration Decrease Per Overclock. This functions the same as single
     * blocks.
     */
    public GT_OverclockCalculator setOneTickDiscount(boolean oneTickDiscount) {
        this.oneTickDiscount = oneTickDiscount;
        return this;
    }

    /**
     * Limit the amount of overclocks that can be performed, regardless of how much power is available. Mainly used for
     * fusion reactors.
     */
    public GT_OverclockCalculator limitOverclockCount(int maxOverclocks) {
        this.limitOverclocks = true;
        this.maxOverclocks = maxOverclocks;
        return this;
    }

    public GT_OverclockCalculator setLaserOC(boolean laserOC) {
        this.laserOC = laserOC;
        return this;
    }

    public GT_OverclockCalculator setAmperageOC(boolean amperageOC) {
        this.amperageOC = amperageOC;
        return this;
    }

    public GT_OverclockCalculator setLaserOCPenalty(double laserOCPenalty) {
        this.laserOCPenalty = laserOCPenalty;
        return this;
    }

    /**
     * Call this when all values have been put it.
     */
    public GT_OverclockCalculator calculate() {
        if (calculated) {
            throw new IllegalStateException("Tried to calculate overclocks twice");
        }
        calculateOverclock();
        calculated = true;
        return this;
    }

    private void calculateOverclock() {
        if (laserOC && amperageOC) {
            throw new IllegalStateException("Tried to calculate overclock with both laser and amperage overclocking");
        }
        int heatDiscounts = heatDiscount ? (machineHeat - recipeHeat) / HEAT_DISCOUNT_THRESHOLD : 0;
        double heatDiscountMultiplier = Math.pow(heatDiscountExponent, heatDiscounts);
        duration = (int) Math.ceil(duration * speedBoost);
        int heatOCs = 0;
        if (heatOC) {
            heatOCs = (machineHeat - recipeHeat) / HEAT_PERFECT_OVERCLOCK_THRESHOLD;
        }

        double recipePowerTier = calculateRecipePowerTier(heatDiscountMultiplier);
        double machinePowerTier = calculateMachinePowerTier();
        // Math.log(a) / Math.log(b) equals to log_b (a)
        overclockCount = (int) Math
            .min(machinePowerTier - recipePowerTier, Math.log(duration) / Math.log(1 << durationDecreasePerOC));
        if (overclockCount < 0) {
            recipeVoltage = Long.MAX_VALUE;
            duration = Integer.MAX_VALUE;
            return;
        }

        overclockCount = limitOverclocks ? Math.min(maxOverclocks, overclockCount) : overclockCount;
        heatOCs = Math.min(heatOCs, overclockCount);
        recipeVoltage <<= eutIncreasePerOC * overclockCount;
        duration >>= durationDecreasePerOC * (overclockCount - heatOCs);
        duration >>= durationDecreasePerHeatOC * heatOCs;
        if (oneTickDiscount) {
            recipeVoltage >>= durationDecreasePerOC * ((int) (machinePowerTier - recipePowerTier - overclockCount));
            if (recipeVoltage < 1) {
                recipeVoltage = 1;
            }
        }

        if (laserOC) {
            calculateLaserOC();
        }

        if (duration < 1) {
            duration = 1;
        }

        recipeVoltage = calculateFinalRecipeEUt(heatDiscountMultiplier);
    }

    private double calculateRecipePowerTier(double heatDiscountMultiplier) {
        return Math.max(
            1,
            Math.log(recipeVoltage * parallel * eutDiscount * heatDiscountMultiplier * recipeAmperage) / LOG4 - 1);
    }

    private double calculateMachinePowerTier() {
        return Math.max(1, Math.log(machineVoltage * (amperageOC ? machineAmperage : 1)) / LOG4 - 1);
    }

    private long calculateFinalRecipeEUt(double heatDiscountMultiplier) {
        return (long) Math.ceil(recipeVoltage * eutDiscount * heatDiscountMultiplier * parallel * recipeAmperage);
    }

    private void calculateLaserOC() {
        long inputEut = machineVoltage * machineAmperage;
        double currentPenalty = (1 << eutIncreasePerOC) + laserOCPenalty;
        while (inputEut > recipeVoltage * currentPenalty && recipeVoltage * currentPenalty > 0 && duration > 1) {
            duration >>= durationDecreasePerOC;
            recipeVoltage *= currentPenalty;
            currentPenalty += laserOCPenalty;
        }
    }

    /**
     * @return The consumption after overclock has been calculated
     */
    public long getConsumption() {
        if (!calculated) {
            calculate();
        }
        return recipeVoltage;
    }

    /**
     * @return The duration of the recipe after overclock has been calculated
     */
    public int getDuration() {
        if (!calculated) {
            calculate();
        }
        return duration;
    }

    /**
     * @return Number of performed overclocks
     */
    public int getPerformedOverclocks() {
        if (!calculated) {
            calculate();
        }
        return overclockCount;
    }

    public int getDurationDecreasePerOC() {
        return durationDecreasePerOC;
    }
}
