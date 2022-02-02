package gregtech.common.power;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import net.minecraft.util.EnumChatFormatting;

public class EUPower extends Power {
    protected final int amperage;

    public EUPower(byte tier, int amperage) {
        super(tier);
        this.amperage = amperage;
    }

    @Override
    // This generic EU Power class has no overclock defined and does no special calculations.
    public void computePowerUsageAndDuration(int euPerTick, int duration) {
        recipeEuPerTick = euPerTick;
        recipeDuration = duration;
    }

    @Override
    public String getTierString() {
        return GT_Values.TIER_COLORS[tier] + GT_Values.VN[tier] + EnumChatFormatting.RESET;
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
        int voltage = recipeEuPerTick / amperage;
        String voltageDescription = GT_Utility.formatNumbers(voltage) + " EU";
        byte recipeTier = GT_Utility.getTier(recipeEuPerTick / amperage);
        if (recipeTier >= 0 && recipeTier < 16) {
            voltageDescription += " (" + GT_Values.VN[recipeTier] + ")";
        }
        return voltageDescription;
    }

    @Override
    public String getAmperageString() {
        return GT_Utility.formatNumbers(amperage);
    }
}
