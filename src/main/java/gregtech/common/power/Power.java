package gregtech.common.power;

import gregtech.api.util.GT_Utility;

public abstract class Power {
    protected final byte tier;
    protected int recipeEuPerTick;
    protected int recipeDuration;

    public Power(byte tier) {
        this.tier = tier;
    }

    public byte getTier() {
        return tier;
    }

    public abstract String getTierString();

    /**
     * This method should be called prior to usage of any value except the power tier.
     */
    public abstract void computePowerUsageAndDuration(int euPerTick, int duration);

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
        return GT_Utility.formatNumbers(getDurationTicks()) + GT_Utility.trans("224", " ticks");
    }

    public abstract String getTotalPowerString();

    public abstract String getPowerUsageString();

    public abstract String getVoltageString();

    public abstract String getAmperageString();
}
