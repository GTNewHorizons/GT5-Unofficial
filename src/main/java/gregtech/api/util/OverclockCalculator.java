package gregtech.api.util;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

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

    // Constants
    protected static final int HEAT_DISCOUNT_THRESHOLD = 900;
    protected static final int HEAT_OVERCLOCK_THRESHOLD = 1800;
    protected static final double LOG4 = Math.log(4);

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
     * Sets the maximum number of overclocks that can be performed, regardless of how much power is available.
     * Negative values are rounded up to 0.
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

    protected void calculateOverclock() {
        // Determine the base duration, using the custom supplier if available.
        double duration = durationUnderOneTickSupplier != null ? durationUnderOneTickSupplier.get()
            : this.duration * durationModifier;

        // If currentParallel isn't set, assume full parallel usage.
        currentParallel = Math.max(currentParallel, parallel);

        // Treat ULV (tier 0) as LV (tier 1) for overclocking calculations.
        double recipePower = recipeEUt * parallel * eutModifier * calculateHeatDiscountMultiplier();
        double recipePowerTier = Math.max(Math.log(recipePower / 8) / LOG4, 1);
        double machinePower = machineVoltage * (amperageOC ? machineAmperage : Math.min(machineAmperage, parallel));
        double machinePowerTier = Math.max(Math.log(machinePower / 8) / LOG4, 1);

        // If overclocking is disabled, use the base values and return.
        if (noOverclock) {
            calculatedConsumption = (long) Math.ceil(recipePower);
            calculatedDuration = (int) Math.ceil(duration);
            return;
        }

        // Special handling for laser overclocking.
        if (laserOC) {
            double eutOverclock = recipePower;

            // Keep increasing power until normal overclocks are used.
            int regularOverclocks = 0;
            while (eutOverclock * 4.0 < machinePower && regularOverclocks < maxRegularOverclocks) {
                eutOverclock *= 4.0;
                regularOverclocks++;
            }

            // Keep increasing power until it hits the machine's limit.
            int laserOverclocks = 0;
            while (eutOverclock * (4.0 + 0.3 * (laserOverclocks + 1)) < machinePower) {
                eutOverclock *= (4.0 + 0.3 * (laserOverclocks + 1));
                laserOverclocks++;
            }

            overclocks = regularOverclocks + laserOverclocks;
            calculatedConsumption = (long) Math.ceil(eutOverclock);
            duration /= Math.pow(durationDecreasePerOC, overclocks);
            calculatedDuration = (int) Math.max(duration, 1);
            return;
        }

        // Limit overclocks allowed by power tier.
        overclocks = Math.min(maxOverclocks, (int) (machinePowerTier - recipePowerTier));

        // If amperage overclocks are disabled, limit overclocks by voltage tier.
        if (!amperageOC) {
            int voltageTierMachine = (int) Math.max(Math.ceil(Math.log((double) machineVoltage / 8) / LOG4), 1);
            int voltageTierRecipe = (int) Math.max(Math.ceil(Math.log((double) recipeEUt / 8) / LOG4), 1);
            overclocks = Math.min(overclocks, voltageTierMachine - voltageTierRecipe);
        }

        // Make sure overclocks don't go negative. This allows recipes needing >1A to run on a single hatch.
        overclocks = Math.max(overclocks, 0);

        // Split overclocks into heat-based and regular overclocks.
        int heatOverclocks = Math.min(heatOC ? (machineHeat - recipeHeat) / HEAT_OVERCLOCK_THRESHOLD : 0, overclocks);
        int regularOverclocks = overclocks - heatOverclocks;

        // Adjust power consumption and processing time based on overclocks.
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
        // Determine the base duration, using the custom supplier if available.
        double duration = durationUnderOneTickSupplier != null ? durationUnderOneTickSupplier.get()
            : this.duration * durationModifier;

        // Treat ULV (tier 0) as LV (tier 1) for overclocking calculations.
        double recipePower = recipeEUt * parallel * eutModifier * calculateHeatDiscountMultiplier();
        double recipePowerTier = Math.max(Math.log(recipePower / 8) / LOG4, 1);
        double machinePower = machineVoltage * (amperageOC ? machineAmperage : Math.min(machineAmperage, parallel));
        double machinePowerTier = Math.max(Math.log(machinePower / 8) / LOG4, 1);

        // If overclocking is disabled, use the base values and return.
        if (noOverclock) return duration;

        // Special handling for laser overclocking.
        if (laserOC) {
            double eutOverclock = recipePower;

            // Keep increasing power until normal overclocks are used.
            int regularOverclocks = 0;
            while (eutOverclock * 4.0 < machinePower && regularOverclocks < maxRegularOverclocks) {
                eutOverclock *= 4.0;
                regularOverclocks++;
            }

            // Keep increasing power until it hits the machine's limit.
            int laserOverclocks = 0;
            while (eutOverclock * (4.0 + 0.3 * (laserOverclocks + 1)) < machinePower) {
                eutOverclock *= (4.0 + 0.3 * (laserOverclocks + 1));
                laserOverclocks++;
            }

            int overclocks = regularOverclocks + laserOverclocks;
            return duration / Math.pow(durationDecreasePerOC, overclocks);
        }

        // Limit overclocks allowed by power tier.
        int overclocks = Math.min(maxOverclocks, (int) (machinePowerTier - recipePowerTier));

        // If amperage overclocks are disabled, limit overclocks by voltage tier.
        if (!amperageOC) {
            int voltageTierMachine = (int) Math.max(Math.ceil(Math.log((double) machineVoltage / 8) / LOG4), 1);
            int voltageTierRecipe = (int) Math.max(Math.ceil(Math.log((double) recipeEUt / 8) / LOG4), 1);
            overclocks = Math.min(overclocks, voltageTierMachine - voltageTierRecipe);
        }

        // Make sure overclocks don't go negative. This allows recipes needing >1A to run on a single hatch.
        overclocks = Math.max(overclocks, 0);

        // Split overclocks into heat-based and regular overclocks.
        int heatOverclocks = Math.min(heatOC ? (machineHeat - recipeHeat) / HEAT_OVERCLOCK_THRESHOLD : 0, overclocks);
        int regularOverclocks = overclocks - heatOverclocks;

        // Adjust power consumption and processing time based on overclocks.
        duration /= Math.pow(durationDecreasePerHeatOC, heatOverclocks);
        duration /= Math.pow(durationDecreasePerOC, regularOverclocks);

        return duration;
    }
}
