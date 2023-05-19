package gregtech.common.power;

import gregtech.api.util.GT_Utility;

public class SteamPower extends Power {

    private final double euPerTickOverride;
    private final double durationOverride;
    private final String[] STEAM_TIER_NAMES = { "Bronze", "Steel" };

    public SteamPower(byte tier, double euPerTickMultiplier, double durationMultiplier) {
        super(tier);
        this.euPerTickOverride = euPerTickMultiplier;
        this.durationOverride = durationMultiplier;
    }

    @Override
    public byte getTier() {
        return 1;
    }

    @Override
    public String getTierString() {
        return STEAM_TIER_NAMES[tier - 1];
    }

    @Override
    public void computePowerUsageAndDuration(int euPerTick, int duration) {
        recipeEuPerTick = (int) (euPerTick * euPerTickOverride);
        recipeDuration = (int) (duration * durationOverride);
    }

    @Override
    public String getTotalPowerString() {
        // 2L normal steam == 1EU
        return GT_Utility.formatNumbers(2L * recipeDuration * recipeEuPerTick) + " Steam";
    }

    @Override
    public String getPowerUsageString() {
        // 2L normal steam == 1EU
        return GT_Utility.formatNumbers(20L * 2L * recipeEuPerTick) + " L/s  Steam";
    }

    @Override
    public String getVoltageString() {
        return null;
    }

    @Override
    public String getAmperageString() {
        return null;
    }
}
