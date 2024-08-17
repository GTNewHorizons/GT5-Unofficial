package gregtech.api.util;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

public class GT_OverclockCalculator {

    // region variables
    // region basic properties
    /**
     * EUt the recipe originally runs at
     */
    private long recipeEUt = 0;
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
     * A supplier used for machines which have a custom way of calculating base duration, like Neutron Activator
     */
    private Supplier<Double> durationUnderOneTickSupplier;
    /**
     * The parallel the machine has when trying to overclock
     */
    private int parallel = 1;
    // endregion
    // region extra factors
    /**
     * Discount for EUt at the beginning of calculating overclocks, like GT++ machines
     */
    private double eutDiscount = 1;
    /**
     * Speeding/Slowing up/down the duration of a recipe at the beginning of calculating overclocks, like GT++ machines
     */
    private double speedBoost = 1;
    // endregion
    // region overclock parameters
    /**
     * How much the energy would be multiplied by per overclock available
     */
    private double eutIncreasePerOC = 4;
    /**
     * A supplier used for machines which have a custom way of calculating energy increase multipliers for every
     * overclock, like Advanced Assembling Line
     */
    private Function<Integer, Double> eutIncreasePerOCSupplier = getDefaultEutIncreasePerOCSupplier();
    /**
     * How much the duration would be divided by per overclock made that isn't an overclock from HEAT
     */
    private double durationDecreasePerOC = 2;
    /**
     * A supplier used for machines which have a custom way of calculating duration decrease multipliers for every
     * overclock
     */
    private Function<Integer, Double> durationDecreasePerOCSupplier = getDefaultDurationDecreasePerOCSupplier();
    /**
     * Whether at least one of {@link #eutIncreasePerOCSupplier} and {@link #durationDecreasePerOCSupplier} has been set
     */
    private boolean hasAtLeastOneSupplierBeenSet;
    /**
     * Whether to give EUt Discount when the duration goes below one tick
     */
    private boolean oneTickDiscount;
    /**
     * Whether the multi should use amperage to overclock normally.
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
     * Should we actually try to calculate overclocking
     */
    private boolean noOverclock;
    /**
     * The parallel the machine actually used.
     */
    private int currentParallel;
    // endregion
    // region heat overclock
    /**
     * The min heat required for the recipe
     */
    private int recipeHeat = 0;
    /**
     * The heat the machine has when starting the recipe
     */
    private int machineHeat = 0;
    /**
     * How much the duration should be divided by for each 1800K above recipe heat
     */
    private double durationDecreasePerHeatOC = 4;
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
    // endregion
    // region result
    /**
     * variable to check whether the overclocks have been calculated
     */
    private boolean calculated;
    /**
     * The calculated duration result.
     */
    private int calculatedDuration;
    /**
     * The calculated energy consumption result.
     */
    private long calculatedConsumption;
    // endregion
    // region constants
    private static final int HEAT_DISCOUNT_THRESHOLD = 900;
    private static final int HEAT_PERFECT_OVERCLOCK_THRESHOLD = 1800;
    private static final double LOG2 = Math.log(2);
    // endregion
    // endregion

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
            .setEUt(eut)
            .setNoOverclock(true);
    }

    /**
     * An Overclock helper for calculating overclocks in many different situations
     */
    public GT_OverclockCalculator() {}

    // region setters
    /**
     * @param recipeEUt Sets the Recipe's starting voltage
     */
    @Nonnull
    public GT_OverclockCalculator setRecipeEUt(long recipeEUt) {
        this.recipeEUt = recipeEUt;
        return this;
    }

    /**
     * @param machineVoltage Sets the EUt that the machine can use. This is the voltage of the machine
     */
    @Nonnull
    public GT_OverclockCalculator setEUt(long machineVoltage) {
        this.machineVoltage = machineVoltage;
        return this;
    }

    /**
     * @param duration Sets the duration of the recipe
     */
    @Nonnull
    public GT_OverclockCalculator setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * @param machineAmperage Sets the Amperage that the machine can support
     */
    @Nonnull
    public GT_OverclockCalculator setAmperage(long machineAmperage) {
        this.machineAmperage = machineAmperage;
        return this;
    }

    /**
     * Enables Perfect OC in calculation
     */
    @Nonnull
    public GT_OverclockCalculator enablePerfectOC() {
        this.durationDecreasePerOC = 4;
        return this;
    }

    /**
     * Set if we should be calculating overclocking using EBF's perfectOC
     */
    @Nonnull
    public GT_OverclockCalculator setHeatOC(boolean heatOC) {
        this.heatOC = heatOC;
        return this;
    }

    /**
     * Sets if we should add a heat discount at the end of calculating an overclock, just like the EBF
     */
    @Nonnull
    public GT_OverclockCalculator setHeatDiscount(boolean heatDiscount) {
        this.heatDiscount = heatDiscount;
        return this;
    }

    /**
     * Sets the starting heat of the recipe
     */
    @Nonnull
    public GT_OverclockCalculator setRecipeHeat(int recipeHeat) {
        this.recipeHeat = recipeHeat;
        return this;
    }

    /**
     * Sets the heat of the coils on the machine
     */
    @Nonnull
    public GT_OverclockCalculator setMachineHeat(int machineHeat) {
        this.machineHeat = machineHeat;
        return this;
    }

    /**
     * Sets an EUtDiscount. 0.9 is 10% less energy. 1.1 is 10% more energy
     */
    @Nonnull
    public GT_OverclockCalculator setEUtDiscount(float aEUtDiscount) {
        this.eutDiscount = aEUtDiscount;
        return this;
    }

    /**
     * Sets a Speed Boost for the multiblock. 0.9 is 10% faster. 1.1 is 10% slower
     */
    @Nonnull
    public GT_OverclockCalculator setSpeedBoost(float aSpeedBoost) {
        this.speedBoost = aSpeedBoost;
        return this;
    }

    /**
     * Sets the parallel that the multiblock uses
     */
    @Nonnull
    public GT_OverclockCalculator setParallel(int aParallel) {
        this.parallel = aParallel;
        return this;
    }

    /**
     * Sets the heat discount during OC calculation if HeatOC is used. Default: 0.95 = 5% discount Used like a EU/t
     * Discount
     */
    @Nonnull
    public GT_OverclockCalculator setHeatDiscountMultiplier(float heatDiscountExponent) {
        this.heatDiscountExponent = heatDiscountExponent;
        return this;
    }

    /**
     * Sets the Overclock that should be calculated when a heat OC is applied.
     */
    @Nonnull
    public GT_OverclockCalculator setHeatPerfectOC(double heatPerfectOC) {
        if (heatPerfectOC <= 0) throw new IllegalArgumentException("Heat OC can't be a negative number or zero");
        this.durationDecreasePerHeatOC = heatPerfectOC;
        return this;
    }

    /**
     * Sets the amount that the eut would be multiplied by per overclock. Do not set as 1(ONE) if the duration decrease
     * is also 1(ONE)!
     */
    @Nonnull
    public GT_OverclockCalculator setEUtIncreasePerOC(double eutIncreasePerOC) {
        if (eutIncreasePerOC <= 0)
            throw new IllegalArgumentException("EUt increase can't be a negative number or zero");
        this.eutIncreasePerOC = eutIncreasePerOC;
        return this;
    }

    /**
     * Sets the amount that the duration would be divided by per overclock. Do not set as 1(ONE) if the eut increase is
     * also 1(ONE)!
     */
    @Nonnull
    public GT_OverclockCalculator setDurationDecreasePerOC(double durationDecreasePerOC) {
        if (durationDecreasePerOC <= 0)
            throw new IllegalArgumentException("Duration decrease can't be a negative number or zero");
        this.durationDecreasePerOC = durationDecreasePerOC;
        return this;
    }

    /**
     * Set One Tick Discount on EUt based on Duration Decrease Per Overclock. This functions the same as single blocks.
     */
    @Nonnull
    public GT_OverclockCalculator setOneTickDiscount(boolean oneTickDiscount) {
        this.oneTickDiscount = oneTickDiscount;
        return this;
    }

    /**
     * Limit the amount of overclocks that can be performed, regardless of how much power is available. Mainly used for
     * fusion reactors.
     */
    @Nonnull
    public GT_OverclockCalculator limitOverclockCount(int maxOverclocks) {
        this.limitOverclocks = true;
        this.maxOverclocks = maxOverclocks;
        return this;
    }

    @Nonnull
    public GT_OverclockCalculator setAmperageOC(boolean amperageOC) {
        this.amperageOC = amperageOC;
        return this;
    }

    /**
     * Set a supplier for calculating custom duration for when its needed under one tick
     */
    @Nonnull
    public GT_OverclockCalculator setDurationUnderOneTickSupplier(Supplier<Double> supplier) {
        this.durationUnderOneTickSupplier = supplier;
        return this;
    }

    /**
     * Sets if we should do overclocking or not
     */
    @Nonnull
    public GT_OverclockCalculator setNoOverclock(boolean noOverclock) {
        this.noOverclock = noOverclock;
        return this;
    }

    /**
     * Set a supplier for calculating custom EUt increase multipliers for every overclock
     */
    public GT_OverclockCalculator setEutIncreasePerOCSupplier(Function<Integer, Double> eutIncreasePerOCSupplier) {
        this.eutIncreasePerOCSupplier = eutIncreasePerOCSupplier;
        this.hasAtLeastOneSupplierBeenSet = true;
        return this;
    }

    /**
     * Set a supplier for calculating custom duration decrease multipliers for every overclock
     */
    public GT_OverclockCalculator setDurationDecreasePerOCSupplier(
        Function<Integer, Double> durationDecreasePerOCSupplier) {
        this.durationDecreasePerOCSupplier = durationDecreasePerOCSupplier;
        this.hasAtLeastOneSupplierBeenSet = true;
        return this;
    }

    /**
     * Set actually performed parallel
     */
    public GT_OverclockCalculator setCurrentParallel(int currentParallel) {
        this.currentParallel = currentParallel;
        // Sets parallel to the actually performed one if machine's parallel is underused.
        this.parallel = Math.min(parallel, currentParallel);
        return this;
    }

    // endregion
    // region calculate
    /**
     * Call this when all values have been put it.
     */
    @Nonnull
    public GT_OverclockCalculator calculate() {
        if (calculated) {
            throw new IllegalStateException("Tried to calculate overclocks twice");
        }
        calculateOverclock();
        calculated = true;
        return this;
    }

    private void calculateOverclock() {
        double durationInDouble = durationUnderOneTickSupplier != null ? durationUnderOneTickSupplier.get()
            : duration * speedBoost;
        calculatedConsumption = recipeEUt;
        double heatDiscountMultiplier = calculateHeatDiscountMultiplier();
        // Usually a safeguard when currentParallel is not set: We assume parallel is fully used.
        currentParallel = Math.max(currentParallel, parallel);

        if (noOverclock) {
            calculatedConsumption = calculateFinalRecipeEUt(heatDiscountMultiplier);
            calculatedDuration = (int) Math.ceil(durationInDouble);
            return;
        }

        // First we need to overclock to reach 1 tick.
        // Then we need to overclock under one tick to get more extra parallels.
        // We stop overclocking if we've already reached 1 tick and got enough parallels to actually perform.
        double requiredUnderOneTickMultiplier = durationInDouble * currentParallel / parallel;
        if (hasAtLeastOneSupplierBeenSet) { // custom overclock
            double currentEutIncrease = eutIncreasePerOCSupplier.apply(overclockCount + 1);
            double currentDurationDecrease = durationDecreasePerOCSupplier.apply(overclockCount + 1);
            double machinePower = calculateMachinePower();
            double currentConsumption = calculateRecipePower(heatDiscountMultiplier);
            double currentUnderOneTickMultiplier = 1;
            // Whether we have enough power for the next overclock;
            // whether we need more overclock to reach 1 tick and get enough extra parallel;
            // whether we have reached the overclock limit
            while (machinePower > currentConsumption * currentEutIncrease
                && requiredUnderOneTickMultiplier > currentUnderOneTickMultiplier
                && (!limitOverclocks || overclockCount < maxOverclocks)) {
                currentConsumption *= currentEutIncrease;
                durationInDouble /= currentDurationDecrease;
                overclockCount++;
                currentEutIncrease = eutIncreasePerOCSupplier.apply(overclockCount + 1);
                currentDurationDecrease = durationDecreasePerOCSupplier.apply(overclockCount + 1);
            }
            calculatedConsumption = (long) Math.max(currentConsumption, 1);
            calculatedDuration = (int) Math.max(durationInDouble, 1);
        } else { // general overclock
            double recipePowerTier = calculateRecipePowerTier(heatDiscountMultiplier);
            double machinePowerTier = calculateMachinePowerTier();

            int maxOverclockCount = calculateAmountOfOverclocks(machinePowerTier, recipePowerTier);
            if (limitOverclocks) maxOverclockCount = Math.min(maxOverclocks, maxOverclockCount);
            if (!amperageOC) {
                // Limit overclocks by voltage tier.
                maxOverclockCount = Math.min(maxOverclockCount, calculateRecipeToMachineVoltageDifference());
            }
            overclockCount = calculateAmountOfNeededOverclocks(maxOverclockCount, requiredUnderOneTickMultiplier);

            // If triggered, it indicates that recipe power > machine power.
            // Not just a safeguard. This also means that you can run a 1.2A recipe on a single hatch for a regular gt
            // multi.
            // This is intended, including the fact that you don't get an OC with a one tier upgrade in that case.
            overclockCount = Math.max(overclockCount, 0);

            int heatOverclockCount = Math.min(calculateMaxAmountOfHeatOverclocks(), overclockCount);
            calculatedConsumption = (long) Math.floor(recipeEUt * Math.pow(eutIncreasePerOC, overclockCount));
            durationInDouble /= Math.pow(durationDecreasePerHeatOC, heatOverclockCount)
                * Math.pow(durationDecreasePerOC, overclockCount - heatOverclockCount);
            if (oneTickDiscount) {
                calculatedConsumption = (long) Math
                    .floor(calculatedConsumption / Math.pow(durationDecreasePerOC, maxOverclockCount - overclockCount));
                calculatedConsumption = Math.max(calculatedConsumption, 1);
            }
            calculatedConsumption = calculateFinalRecipeEUt(heatDiscountMultiplier);
            calculatedDuration = (int) Math.max(durationInDouble, 1);
        }
    }

    private double calculateRecipePower(double heatDiscountMultiplier) {
        return recipeEUt * parallel * eutDiscount * heatDiscountMultiplier;
    }

    private double calculateRecipePowerTier(double heatDiscountMultiplier) {
        return calculatePowerTier(calculateRecipePower(heatDiscountMultiplier));
    }

    private double calculateMachinePower() {
        return machineVoltage * (amperageOC ? machineAmperage : Math.min(machineAmperage, parallel));
    }

    private double calculateMachinePowerTier() {
        return calculatePowerTier(calculateMachinePower());
    }

    private int calculateRecipeToMachineVoltageDifference() {
        return (int) (Math.ceil(calculatePowerTier(machineVoltage)) - Math.ceil(calculatePowerTier(recipeEUt)));
    }

    private double calculatePowerTier(double voltage) {
        return 1 + Math.max(0, (Math.log(voltage) / LOG2) - 5) / 2;
    }

    private long calculateFinalRecipeEUt(double heatDiscountMultiplier) {
        return (long) Math.ceil(calculatedConsumption * eutDiscount * heatDiscountMultiplier * parallel);
    }

    private int calculateMaxAmountOfHeatOverclocks() {
        return heatOC ? (machineHeat - recipeHeat) / HEAT_PERFECT_OVERCLOCK_THRESHOLD : 0;
    }

    /**
     * Calculate maximum possible overclocks ignoring if we are going to go under 1 tick
     */
    private int calculateAmountOfOverclocks(double machinePowerTier, double recipePowerTier) {
        return (int) (machinePowerTier - recipePowerTier);
    }

    private int calculateAmountOfNeededOverclocks(int maxOverclockCount, double requiredUnderOneTickMultiplier) {
        int neededHeatOC = (int) Math.min(
            calculateMaxAmountOfHeatOverclocks(),
            Math.ceil(Math.log(requiredUnderOneTickMultiplier) / Math.log(durationDecreasePerHeatOC)));
        neededHeatOC = Math.max(neededHeatOC, 0);
        int neededNormalOC = (int) Math.ceil(
            (Math.log(requiredUnderOneTickMultiplier) - Math.log(durationDecreasePerHeatOC) * neededHeatOC)
                / Math.log(durationDecreasePerOC));
        neededNormalOC = Math.max(neededNormalOC, 0);
        return Math.min(maxOverclockCount, neededHeatOC + neededNormalOC);
    }

    private double calculateHeatDiscountMultiplier() {
        int heatDiscounts = heatDiscount ? (machineHeat - recipeHeat) / HEAT_DISCOUNT_THRESHOLD : 0;
        return Math.pow(heatDiscountExponent, heatDiscounts);
    }

    // endregion
    // region result getters
    /**
     * @return The consumption after overclock has been calculated
     */
    public long getConsumption() {
        if (!calculated) {
            throw new IllegalStateException("Tried to get consumption before calculating");
        }
        return calculatedConsumption;
    }

    /**
     * @return The duration of the recipe after overclock has been calculated
     */
    public int getDuration() {
        if (!calculated) {
            throw new IllegalStateException("Tried to get duration before calculating");
        }
        return calculatedDuration;
    }

    /**
     * @return Number of performed overclocks
     */
    public int getPerformedOverclocks() {
        if (!calculated) {
            throw new IllegalStateException("Tried to get performed overclocks before calculating");
        }
        return overclockCount;
    }

    /**
     * @return Whether the calculation has happened
     */
    public boolean getCalculationStatus() {
        return calculated;
    }

    // endregion
    // region misc
    /**
     * Returns duration as a double to show how much it is overclocking too much to determine extra parallel. This
     * doesn't count as calculating
     */
    public double calculateDurationUnderOneTick() {
        double durationInDouble = durationUnderOneTickSupplier != null ? durationUnderOneTickSupplier.get()
            : duration * speedBoost;
        if (noOverclock) return durationInDouble;
        double heatDiscountMultiplier = calculateHeatDiscountMultiplier();
        if (hasAtLeastOneSupplierBeenSet) {
            int overclockCount = 0;
            double currentEutIncrease = eutIncreasePerOCSupplier.apply(overclockCount + 1);
            double currentDurationDecrease = durationDecreasePerOCSupplier.apply(overclockCount + 1);
            double machinePower = calculateMachinePower();
            double recipePower = calculateRecipePower(heatDiscountMultiplier);
            while (machinePower > recipePower * currentEutIncrease
                && (!limitOverclocks || overclockCount < maxOverclocks)) {
                recipePower *= currentEutIncrease;
                durationInDouble /= currentDurationDecrease;
                overclockCount++;
                currentEutIncrease = eutIncreasePerOCSupplier.apply(overclockCount + 1);
                currentDurationDecrease = durationDecreasePerOCSupplier.apply(overclockCount + 1);
            }
        } else {
            int maxOverclockCount = calculateAmountOfOverclocks(
                calculateMachinePowerTier(),
                calculateRecipePowerTier(heatDiscountMultiplier));
            if (limitOverclocks) maxOverclockCount = Math.min(maxOverclocks, maxOverclockCount);
            int heatOverclocks = Math.min(calculateMaxAmountOfHeatOverclocks(), maxOverclockCount);
            durationInDouble /= Math.pow(durationDecreasePerOC, maxOverclockCount - heatOverclocks)
                * Math.pow(durationDecreasePerHeatOC, heatOverclocks);
        }
        return durationInDouble;
    }

    private Function<Integer, Double> getDefaultEutIncreasePerOCSupplier() {
        return overclockCount -> eutIncreasePerOC;
    }

    private Function<Integer, Double> getDefaultDurationDecreasePerOCSupplier() {
        return overclockCount -> overclockCount <= calculateMaxAmountOfHeatOverclocks() ? durationDecreasePerHeatOC
            : durationDecreasePerOC;
    }

    // endregion
}
