package gregtech.api.util;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

/// Calculates the overclocks, energy consumption, and duration of a recipe on a machine.
///
/// Overclocks that push the duration below one tick lose speed. [#calculateMaxParallelUnderOneTick] converts that
/// lost speed into parallels. Energy stays what the configured parallel costs at the full overclock count.
public class OverclockCalculator {

    // Basic properties
    /** EUt the recipe originally runs at */
    protected long recipeEUt = 0;
    /** Voltage of the machine */
    protected long machineVoltage = 0;
    /** Amperage of the machine */
    protected long machineAmperage = 1;
    /** Duration of the recipe */
    protected int duration = 0;
    /** A supplier used for machines which have a custom way of calculating base duration, like Neutron Activator */
    protected Supplier<Double> durationUnderOneTickSupplier;
    /** The parallel the machine has when trying to overclock */
    protected int parallel = 1;
    /** The max amount of tiers above the machine voltage a recipe is valid */
    protected int maxTierSkip = 1;

    // Modifiers
    /** Energy modifier that is applied at the start of calculating overclocks, like GT++ machines */
    protected double eutModifier = 1.00;
    /** Duration modifier that is applied at the start of calculating overclocks, like GT++ machines */
    protected double durationModifier = 1.00;

    // Overclock parameters
    /** How much the energy would be multiplied by per overclock available */
    protected double eutIncreasePerOC = 4;
    /** How much the duration would be divided by per overclock made that isn't an overclock from HEAT */
    protected double durationDecreasePerOC = 2;
    /** Whether the multi should use laser overclocks. */
    protected boolean laserOC;
    /** Whether the multi should use amperage to overclock normally. */
    protected boolean amperageOC;
    /** Maximum number of overclocks to perform. Defaults to no limit. */
    protected int maxOverclocks = Integer.MAX_VALUE;
    /** Maximum number of regular overclocks to perform before exotic (e.g. laser) overclocks. Defaults to no limit. */
    protected int maxRegularOverclocks = Integer.MAX_VALUE;
    /** How many overclocks have been performed */
    protected int overclocks = 0;
    /** Should we actually try to calculate overclocking */
    protected boolean noOverclock;
    /** The parallel the machine actually used. */
    protected int currentParallel;

    // Heat parameters
    /** The min heat required for the recipe */
    protected int recipeHeat = 0;
    /** The heat the machine has when starting the recipe */
    protected int machineHeat = 0;
    /** How much the duration should be divided by for each 1800K above recipe heat */
    protected final double durationDecreasePerHeatOC = 4;
    /** Whether to enable overclocking with heat like the EBF every 1800 heat difference */
    protected boolean heatOC;
    /** Whether to enable heat discounts every 900 heat difference */
    protected boolean heatDiscount;
    /** The value used for discount final eut per 900 heat */
    protected double heatDiscountExponent = 0.95;

    // Results
    /** variable to check whether the overclocks have been calculated */
    protected boolean calculated;
    /** The calculated duration result. */
    protected int calculatedDuration;
    /** The calculated energy consumption result. */
    protected long calculatedConsumption;

    // Helpers
    /// Stores the results from [#calculateLaserOCs]
    private record ResultLaserOCs(int regularOverclocks, int laserOverclocks, double eutOverclock) {}

    // Constants
    protected static final int HEAT_DISCOUNT_THRESHOLD = 900;
    protected static final int HEAT_OVERCLOCK_THRESHOLD = 1800;

    /** Creates calculator that doesn't do OC at all. Will use recipe duration. */
    public static OverclockCalculator ofNoOverclock(@Nonnull GTRecipe recipe) {
        return ofNoOverclock(recipe.mEUt, recipe.mDuration);
    }

    /** Creates calculator that doesn't do OC at all, with set duration. */
    public static OverclockCalculator ofNoOverclock(long eut, int duration) {
        return new OverclockCalculator().setRecipeEUt(eut)
            .setDuration(duration)
            .setEUt(eut)
            .setNoOverclock(true);
    }

    /** An Overclock helper for calculating overclocks in many different situations */
    public OverclockCalculator() {}

    // region setters
    /** @param recipeEUt Sets the Recipe's starting voltage */
    @Nonnull
    public OverclockCalculator setRecipeEUt(long recipeEUt) {
        this.recipeEUt = recipeEUt;
        return this;
    }

    /** @param machineVoltage Sets the EUt that the machine can use. This is the voltage of the machine */
    @Nonnull
    public OverclockCalculator setEUt(long machineVoltage) {
        this.machineVoltage = machineVoltage;
        return this;
    }

    /** @param duration Sets the duration of the recipe */
    @Nonnull
    public OverclockCalculator setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    /** @param machineAmperage Sets the Amperage that the machine can support */
    @Nonnull
    public OverclockCalculator setAmperage(long machineAmperage) {
        this.machineAmperage = machineAmperage;
        return this;
    }

    /** Enables Perfect OC in calculation */
    @Nonnull
    public OverclockCalculator enablePerfectOC() {
        this.durationDecreasePerOC = 4;
        return this;
    }

    /** Set if we should be calculating overclocking using EBF's perfectOC */
    @Nonnull
    public OverclockCalculator setHeatOC(boolean heatOC) {
        this.heatOC = heatOC;
        return this;
    }

    /** Sets if we should add a heat discount at the start of calculating an overclock, just like the EBF */
    @Nonnull
    public OverclockCalculator setHeatDiscount(boolean heatDiscount) {
        this.heatDiscount = heatDiscount;
        return this;
    }

    /** Sets the starting heat of the recipe */
    @Nonnull
    public OverclockCalculator setRecipeHeat(int recipeHeat) {
        this.recipeHeat = recipeHeat;
        return this;
    }

    /** Sets the heat of the coils on the machine */
    @Nonnull
    public OverclockCalculator setMachineHeat(int machineHeat) {
        this.machineHeat = machineHeat;
        return this;
    }

    /** Sets an EUtDiscount. 0.9 is 10% less energy. 1.1 is 10% more energy */
    @Nonnull
    public OverclockCalculator setEUtDiscount(double aEUtDiscount) {
        this.eutModifier = aEUtDiscount;
        return this;
    }

    /** Sets a Speed Boost for the multiblock. 0.9 is 10% faster. 1.1 is 10% slower */
    @Nonnull
    public OverclockCalculator setDurationModifier(double aSpeedBoost) {
        this.durationModifier = aSpeedBoost;
        return this;
    }

    /** Sets the parallel that the multiblock uses */
    @Nonnull
    public OverclockCalculator setParallel(int aParallel) {
        this.parallel = aParallel;
        return this;
    }

    /** Sets the max tiers above the machine's voltage a valid recipe can be */
    @Nonnull
    public OverclockCalculator setMaxTierSkips(int aMaxTierSkips) {
        this.maxTierSkip = aMaxTierSkips;
        return this;
    }

    @Nonnull
    public OverclockCalculator setUnlimitedTierSkips() {
        this.maxTierSkip = Integer.MAX_VALUE;
        return this;
    }

    /**
     * Sets the heat discount during OC calculation if HeatOC is used. Default: 0.95 = 5% discount Used like a EU/t
     * Discount
     */
    @Nonnull
    public OverclockCalculator setHeatDiscountMultiplier(double heatDiscountExponent) {
        this.heatDiscountExponent = heatDiscountExponent;
        return this;
    }

    /**
     * Sets the amount that the eut would be multiplied by per overclock. Do not set as 1(ONE) if the duration decrease
     * is also 1(ONE)!
     */
    @Nonnull
    public OverclockCalculator setEUtIncreasePerOC(double eutIncreasePerOC) {
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
    public OverclockCalculator setDurationDecreasePerOC(double durationDecreasePerOC) {
        if (durationDecreasePerOC <= 0)
            throw new IllegalArgumentException("Duration decrease can't be a negative number or zero");
        this.durationDecreasePerOC = durationDecreasePerOC;
        return this;
    }

    /**
     * Sets the maximum number of overclocks that can be performed, regardless of how much power is available. Negative
     * values are rounded up to 0.
     */
    @Nonnull
    public OverclockCalculator setMaxOverclocks(int maxOverclocks) {
        this.maxOverclocks = Math.max(maxOverclocks, 0);
        return this;
    }

    /**
     * Sets the maximum number of regular overclocks that can be performed before exotic (e.g. laser) overclocks,
     * regardless of how much power is available. Negative values are rounded up to 0.
     */
    @Nonnull
    public OverclockCalculator setMaxRegularOverclocks(int maxRegularOverclocks) {
        this.maxRegularOverclocks = Math.max(maxRegularOverclocks, 0);
        return this;
    }

    @Nonnull
    public OverclockCalculator setLaserOC(boolean laserOC) {
        this.laserOC = laserOC;
        return this;
    }

    @Nonnull
    public OverclockCalculator setAmperageOC(boolean amperageOC) {
        this.amperageOC = amperageOC;
        return this;
    }

    /** Set a supplier for calculating custom duration for when its needed under one tick */
    @Nonnull
    public OverclockCalculator setDurationUnderOneTickSupplier(Supplier<Double> supplier) {
        this.durationUnderOneTickSupplier = supplier;
        return this;
    }

    /** Sets if we should do overclocking or not */
    @Nonnull
    public OverclockCalculator setNoOverclock(boolean noOverclock) {
        this.noOverclock = noOverclock;
        return this;
    }

    /** Set actually performed parallel */
    public OverclockCalculator setCurrentParallel(int currentParallel) {
        this.currentParallel = currentParallel;
        // Sets parallel to the actually performed one if machine's parallel is underused.
        this.parallel = Math.min(parallel, currentParallel);
        return this;
    }

    /** @return Current parallel */
    public int getCurrentParallel() {
        return currentParallel;
    }

    /** @return The consumption after overclock has been calculated */
    public long getConsumption() {
        if (!calculated) {
            throw new IllegalStateException("Tried to get consumption before calculating");
        }
        return calculatedConsumption;
    }

    /** @return The duration of the recipe after overclock has been calculated */
    public int getDuration() {
        if (!calculated) {
            throw new IllegalStateException("Tried to get duration before calculating");
        }
        return calculatedDuration;
    }

    /** @return Number of performed overclocks */
    public int getPerformedOverclocks() {
        if (!calculated) {
            throw new IllegalStateException("Tried to get performed overclocks before calculating");
        }
        return overclocks;
    }

    public static long getMaxAllowedRecipeEUt(long machineVoltage, int maxTierSkip) {
        if (maxTierSkip == Integer.MAX_VALUE) return Long.MAX_VALUE;
        return (long) Math.floor(machineVoltage * GTUtility.powInt(4, maxTierSkip));
    }

    public long getMaxAllowedRecipeEUt() {
        return getMaxAllowedRecipeEUt(this.machineVoltage, this.maxTierSkip);
    }

    public boolean hasDurationUnderOneTickSupplier() {
        return durationUnderOneTickSupplier != null;
    }

    public double getDurationUnderOneTickSupplier() {
        return durationUnderOneTickSupplier.get();
    }

    /** Call this when all values have been put it. */
    @Nonnull
    public OverclockCalculator calculate() {
        if (calculated) {
            throw new IllegalStateException("Tried to calculate overclocks twice");
        }
        calculateOverclock();
        calculated = true;
        return this;
    }

    public double calculateHeatDiscountMultiplier() {
        int heatDiscounts = heatDiscount ? (machineHeat - recipeHeat) / HEAT_DISCOUNT_THRESHOLD : 0;
        return GTUtility.powInt(heatDiscountExponent, heatDiscounts);
    }

    private ResultLaserOCs calculateLaserOCs(double recipePower, double machinePower) {
        final double powForTiers = Math.max(recipePower, 32);
        int regularOCs;
        if (eutIncreasePerOC == 4) regularOCs = (int) Math
            .max(0, Math.min(maxRegularOverclocks, GTUtility.log4((long) (machinePower / powForTiers))));
        else regularOCs = (int) Math.max(
            0,
            Math.min(maxRegularOverclocks, Math.log((long) (machinePower / powForTiers)) / Math.log(eutIncreasePerOC)));

        double eutOverclock = recipePower * GTUtility.powInt(eutIncreasePerOC, regularOCs);
        double retEUt = eutOverclock;
        int laserOCs = 0;
        eutOverclock *= (eutIncreasePerOC + 0.3 * (laserOCs + 1));
        while (eutOverclock <= machinePower) {
            laserOCs++;
            retEUt = eutOverclock;
            eutOverclock *= (eutIncreasePerOC + 0.3 * (laserOCs + 1));
        }

        return new ResultLaserOCs(regularOCs, laserOCs, retEUt);
    }

    protected void calculateOverclock() {
        double duration = calculateBaseDuration();

        // If currentParallel isn't set, assume full parallel usage.
        currentParallel = Math.max(currentParallel, parallel);

        double recipePower = calculateRecipePower();
        double machinePower = calculateMachinePower();

        if (noOverclock) {
            calculatedConsumption = (long) Math.ceil(recipePower);
            calculatedDuration = (int) Math.ceil(duration);
            return;
        }

        if (laserOC) {
            final ResultLaserOCs calcOCs = calculateLaserOCs(recipePower, machinePower);

            overclocks = calcOCs.regularOverclocks + calcOCs.laserOverclocks;
            calculatedConsumption = (long) Math.ceil(calcOCs.eutOverclock);
            calculatedDuration = (int) Math.max(duration / GTUtility.powInt(durationDecreasePerOC, overclocks), 1);
            return;
        }

        overclocks = calculateOverclockCount(recipePower, machinePower);
        int heatOverclocks = calculateHeatOverclocks(overclocks);
        int regularOverclocks = overclocks - heatOverclocks;

        calculatedConsumption = (long) Math.ceil(recipePower * GTUtility.powInt(eutIncreasePerOC, overclocks));
        duration /= GTUtility.powInt(durationDecreasePerHeatOC, heatOverclocks);
        duration /= GTUtility.powInt(durationDecreasePerOC, regularOverclocks);
        calculatedDuration = (int) Math.max(duration, 1);
    }

    /// Max parallel after converting the overclock speed lost below one tick into parallels, rounded up. Returns the
    /// current parallel when no speed is lost. This doesn't count as calculating.
    public int calculateMaxParallelUnderOneTick() {
        if (noOverclock) return parallel;
        double duration = calculateBaseDuration();
        double speedup = calculateSpeedup();
        if (speedup <= duration) return parallel;
        return GTUtility.safeInt((long) Math.ceil(parallel * speedup / duration), 0);
    }

    private double calculateBaseDuration() {
        return durationUnderOneTickSupplier != null ? durationUnderOneTickSupplier.get()
            : this.duration * durationModifier;
    }

    private double calculateRecipePower() {
        return recipeEUt * parallel * eutModifier * calculateHeatDiscountMultiplier();
    }

    private double calculateMachinePower() {
        return machineVoltage * (amperageOC ? machineAmperage : Math.min(machineAmperage, parallel));
    }

    /// Overclocks performed at the current parallel, ignoring laser mode. ULV counts as LV for tier purposes.
    private int calculateOverclockCount(double recipePower, double machinePower) {
        int tiersAbove = (int) GTUtility.log4((long) machinePower / Math.max((long) Math.ceil(recipePower), 32));
        int overclocks = Math.min(maxOverclocks, tiersAbove);
        if (!amperageOC) {
            int voltageTierMachine = (int) Math.max(GTUtility.log4ceil(machineVoltage / 8), 1);
            int voltageTierRecipe = (int) Math.max(GTUtility.log4ceil(recipeEUt / 8), 1);
            overclocks = Math.min(overclocks, voltageTierMachine - voltageTierRecipe);
        }
        return Math.max(overclocks, 0);
    }

    private int calculateHeatOverclocks(int overclocks) {
        return Math.min(heatOC ? (machineHeat - recipeHeat) / HEAT_OVERCLOCK_THRESHOLD : 0, overclocks);
    }

    /// Total duration divisor of every overclock performed at the current parallel.
    private double calculateSpeedup() {
        double recipePower = calculateRecipePower();
        double machinePower = calculateMachinePower();
        if (laserOC) {
            ResultLaserOCs calcOCs = calculateLaserOCs(recipePower, machinePower);
            return GTUtility.powInt(durationDecreasePerOC, calcOCs.regularOverclocks + calcOCs.laserOverclocks);
        }
        int overclocks = calculateOverclockCount(recipePower, machinePower);
        int heatOverclocks = calculateHeatOverclocks(overclocks);
        return GTUtility.powInt(durationDecreasePerHeatOC, heatOverclocks)
            * GTUtility.powInt(durationDecreasePerOC, overclocks - heatOverclocks);
    }
}
