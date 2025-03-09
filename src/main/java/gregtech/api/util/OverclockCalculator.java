package gregtech.api.util;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

public class OverclockCalculator {

    // Basic properties
    /** EUt the recipe originally runs at */
    private long recipeEUt = 0;
    /** Voltage of the machine */
    private long machineVoltage = 0;
    /** Amperage of the machine */
    private long machineAmperage = 1;
    /** Duration of the recipe */
    private int duration = 0;
    /** A supplier used for machines which have a custom way of calculating base duration, like Neutron Activator */
    private Supplier<Double> durationUnderOneTickSupplier;
    /** The parallel the machine has when trying to overclock */
    private int parallel = 1;

    // Modifiers
    /** Energy modifier that is applied at the start of calculating overclocks, like GT++ machines */
    private double eutModifier = 1.00;
    /** Duration modifier that is applied at the start of calculating overclocks, like GT++ machines */
    private double durationModifier = 1.00;

    // Overclock parameters
    /** How much the energy would be multiplied by per overclock available */
    private double eutIncreasePerOC = 4;
    /** How much the duration would be divided by per overclock made that isn't an overclock from HEAT */
    private double durationDecreasePerOC = 2;
    /** Whether the multi should use laser overclocks. */
    private boolean laserOC;
    /** Whether the multi should use amperage to overclock normally. */
    private boolean amperageOC;
    /** Maximum number of overclocks to perform. Defaults to no limit. */
    private int maxOverclocks = Integer.MAX_VALUE;
    /** How many overclocks have been performed */
    private int overclocks = 0;
    /** Should we actually try to calculate overclocking */
    private boolean noOverclock;
    /** The parallel the machine actually used. */
    private int currentParallel;

    // Heat parameters
    /** The min heat required for the recipe */
    private int recipeHeat = 0;
    /** The heat the machine has when starting the recipe */
    private int machineHeat = 0;
    /** How much the duration should be divided by for each 1800K above recipe heat */
    private final double durationDecreasePerHeatOC = 4;
    /** Whether to enable overclocking with heat like the EBF every 1800 heat difference */
    private boolean heatOC;
    /** Whether to enable heat discounts every 900 heat difference */
    private boolean heatDiscount;
    /** The value used for discount final eut per 900 heat */
    private double heatDiscountExponent = 0.95;

    // Results
    /** variable to check whether the overclocks have been calculated */
    private boolean calculated;
    /** The calculated duration result. */
    private int calculatedDuration;
    /** The calculated energy consumption result. */
    private long calculatedConsumption;

    // Constants
    private static final int HEAT_DISCOUNT_THRESHOLD = 900;
    private static final int HEAT_OVERCLOCK_THRESHOLD = 1800;
    private static final double LOG4 = Math.log(4);

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
     * Limit the amount of overclocks that can be performed, regardless of how much power is available. Mainly used for
     * fusion reactors.
     */
    @Nonnull
    public OverclockCalculator limitOverclockCount(int maxOverclocks) {
        this.maxOverclocks = maxOverclocks;
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
        return Math.pow(heatDiscountExponent, heatDiscounts);
    }

    private void calculateOverclock() {
        // Get the base duration
        double duration = durationUnderOneTickSupplier != null ? durationUnderOneTickSupplier.get()
            : this.duration * durationModifier;

        // Usually a safeguard when currentParallel is not set: We assume parallel is fully used.
        currentParallel = Math.max(currentParallel, parallel);

        double recipePower = recipeEUt * parallel * eutModifier * calculateHeatDiscountMultiplier();
        double recipePowerTier = Math.log(recipePower / 8) / LOG4;
        double machinePower = machineVoltage * (amperageOC ? machineAmperage : Math.min(machineAmperage, parallel));
        double machinePowerTier = Math.log(machinePower / 8) / LOG4;

        // Early return if overclocks are disabled.
        if (noOverclock) {
            calculatedConsumption = (long) Math.ceil(recipePower);
            calculatedDuration = (int) Math.ceil(duration);
            return;
        }

        // Specialized logic for laser overclocks.
        if (laserOC) {
            double eutLaserOverclock = recipePower;
            overclocks = 0;

            while (eutLaserOverclock * (4.0 + 0.3 * (overclocks + 1)) < machinePower) {
                eutLaserOverclock *= (4.0 + 0.3 * (overclocks + 1));
                overclocks++;
            }

            calculatedConsumption = (long) Math.ceil(eutLaserOverclock);
            duration /= Math.pow(durationDecreasePerOC, overclocks);
            calculatedDuration = (int) Math.max(duration, 1);
            return;
        }

        // Limit overclocks by power tier.
        overclocks = Math.min(maxOverclocks, (int) (machinePowerTier - recipePowerTier));

        // Limit overclocks by voltage tier if amperage overclocks are disabled.
        if (!amperageOC) {
            int voltageTierMachine = (int) Math.max(Math.ceil(Math.log((double) machineVoltage / 8) / LOG4), 0);
            int voltageTierRecipe = (int) Math.max(Math.ceil(Math.log((double) recipeEUt / 8) / LOG4), 0);
            overclocks = Math.min(overclocks, voltageTierMachine - voltageTierRecipe);
        }

        // If triggered, it indicates that recipe power > machine power. Not just a safeguard.
        // This also means that you can run a 1.2A recipe on a single hatch for a regular gt multi.
        // This is intended, including the fact that you don't get an OC with a one tier upgrade in that case.
        overclocks = Math.max(overclocks, 0);

        int heatOverclocks = Math.min(heatOC ? (machineHeat - recipeHeat) / HEAT_OVERCLOCK_THRESHOLD : 0, overclocks);
        int regularOverclocks = overclocks - heatOverclocks;

        calculatedConsumption = (long) Math.ceil(recipePower * Math.pow(eutIncreasePerOC, overclocks));
        duration /= Math.pow(durationDecreasePerHeatOC, heatOverclocks);
        duration /= Math.pow(durationDecreasePerOC, regularOverclocks);
        calculatedDuration = (int) Math.max(duration, 1);
    }

    /**
     * Returns duration as a double to show how much it is overclocking too much to determine extra parallel. This
     * doesn't count as calculating
     */
    public double calculateDurationUnderOneTick() {
        // Get the base duration
        double duration = durationUnderOneTickSupplier != null ? durationUnderOneTickSupplier.get()
            : this.duration * durationModifier;

        double recipePower = recipeEUt * parallel * eutModifier * calculateHeatDiscountMultiplier();
        double recipePowerTier = Math.log(recipePower / 8) / LOG4;
        double machinePower = machineVoltage * (amperageOC ? machineAmperage : Math.min(machineAmperage, parallel));
        double machinePowerTier = Math.log(machinePower / 8) / LOG4;

        if (noOverclock) return duration;

        // Specialized logic for laser overclocks.
        if (laserOC) {
            double eutLaserOverclock = recipePower;
            int overclocks = 0;

            while (eutLaserOverclock * (4.0 + 0.3 * (overclocks + 1)) < machinePower) {
                eutLaserOverclock *= (4.0 + 0.3 * (overclocks + 1));
                overclocks++;
            }

            return duration / Math.pow(durationDecreasePerOC, overclocks);
        }

        // Limit overclocks by power tier.
        int overclocks = Math.min(maxOverclocks, (int) (machinePowerTier - recipePowerTier));

        // Limit overclocks by voltage tier if amperage overclocks are disabled.
        if (!amperageOC) {
            int voltageTierMachine = (int) Math.max(Math.ceil(Math.log((double) machineVoltage / 8) / LOG4), 0);
            int voltageTierRecipe = (int) Math.max(Math.ceil(Math.log((double) recipeEUt / 8) / LOG4), 0);
            overclocks = Math.min(overclocks, voltageTierMachine - voltageTierRecipe);
        }

        // If triggered, it indicates that recipe power > machine power. Not just a safeguard.
        // This also means that you can run a 1.2A recipe on a single hatch for a regular gt multi.
        // This is intended, including the fact that you don't get an OC with a one tier upgrade in that case.
        overclocks = Math.max(overclocks, 0);

        int heatOverclocks = Math.min(heatOC ? (machineHeat - recipeHeat) / HEAT_OVERCLOCK_THRESHOLD : 0, overclocks);
        int regularOverclocks = overclocks - heatOverclocks;

        duration /= Math.pow(durationDecreasePerHeatOC, heatOverclocks);
        duration /= Math.pow(durationDecreasePerOC, regularOverclocks);

        return duration;
    }
}
