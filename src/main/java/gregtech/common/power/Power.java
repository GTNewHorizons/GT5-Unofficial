package gregtech.common.power;

import gregtech.api.util.GT_Utility;

public abstract class Power {

    protected final byte tier;
    protected int recipeEuPerTick;
    protected int recipeDuration;
    protected final int specialValue;

    public Power(byte tier) {
        this(tier, 0);
    }

    public Power(byte tier, int specialValue) {
        this.tier = tier;
        this.specialValue = specialValue;
    }

    public byte getTier() {
        return tier;
    }

    public abstract String getTierString();

    /**
     * This method should be called prior to usage of any value except the power tier.
     */
    public abstract void computePowerUsageAndDuration(int euPerTick, int duration);

    public void computePowerUsageAndDuration(int euPerTick, int duration, int specialValue) {
        computePowerUsageAndDuration(euPerTick, duration);
    }

    public int getEuPerTick() {
        return recipeEuPerTick;
    }

    public int getDurationTicks() {
        return recipeDuration;
    }

    public double getDurationSeconds() {
        return 0.05d * getDurationTicks();
    }

    public String getDurationStringSeconds() {
        return GT_Utility.formatNumbers(getDurationSeconds()) + GT_Utility.trans("161", " secs");
    }

    public String getDurationStringTicks() {
        if (getDurationTicks() == 1) {
            return GT_Utility.formatNumbers(getDurationTicks()) + GT_Utility.trans("209.1", " tick");
        }
        return GT_Utility.formatNumbers(getDurationTicks()) + GT_Utility.trans("209", " ticks");
    }

    public abstract String getTotalPowerString();

    public abstract String getPowerUsageString();

    public abstract String getVoltageString();

    public abstract String getAmperageString();

    public int compareTo(byte tier, int specialValue) {
        if (this.tier < tier) {
            return this.tier - tier;
        }
        return this.specialValue - specialValue;
    }
}
