package gregtech.common.power;

import gregtech.api.util.GT_Utility;

public class EUPower extends Power {

    protected final int amperage;
    protected int originalVoltage;

    public EUPower(byte tier, int amperage) {
        super(tier);
        this.amperage = amperage;
    }

    public EUPower(byte tier, int amperage, int specialValue) {
        super(tier, specialValue);
        this.amperage = amperage;
    }

    @Override
    // This generic EU Power class has no overclock defined and does no special calculations.
    public void computePowerUsageAndDuration(int euPerTick, int duration) {
        originalVoltage = computeVoltageForEuRate(euPerTick);
        recipeEuPerTick = euPerTick;
        recipeDuration = duration;
    }

    @Override
    public String getTierString() {
        return GT_Utility.getColoredTierNameFromTier(tier);
    }

    @Override
    public String getTotalPowerString() {
        return GT_Utility.formatNumbers((long) recipeDuration * recipeEuPerTick) + " EU";
    }

    @Override
    public String getPowerUsageString() {
        return GT_Utility.formatNumbers(recipeEuPerTick) + " EU/t";
    }

    @Override
    public String getVoltageString() {
        String voltageDescription = GT_Utility.formatNumbers(originalVoltage) + " EU/t";
        voltageDescription += GT_Utility.getTierNameWithParentheses(originalVoltage);
        return voltageDescription;
    }

    @Override
    public String getAmperageString() {
        return GT_Utility.formatNumbers(amperage);
    }

    protected int computeVoltageForEuRate(int euPerTick) {
        return amperage != 0 ? euPerTick / amperage : euPerTick;
    }
}
