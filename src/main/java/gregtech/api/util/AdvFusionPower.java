package gregtech.api.util;

import static gregtech.api.enums.GT_Values.V;

import gregtech.common.power.FusionPower;
import gregtech.nei.FusionSpecialValueFormatter;

public class AdvFusionPower extends FusionPower {

    public AdvFusionPower(byte tier, int startupPower) {
        super(tier, startupPower);
    }

    @Override
    public void computePowerUsageAndDuration(int euPerTick, int duration, int specialValue) {
        originalVoltage = computeVoltageForEuRate(euPerTick);
        recipeEuPerTick = euPerTick;
        recipeDuration = duration;
        // It's safe to assume fusion is above ULV. We put this as safety check here anyway
        if (tier > 0) {
            int maxPossibleOverclocks = FusionSpecialValueFormatter.getFusionTier(this.specialValue, V[tier - 1])
                    - FusionSpecialValueFormatter.getFusionTier(specialValue, euPerTick);
            // Isn't too low EUt check?
            long tempEUt = Math.max(euPerTick, V[1]);

            recipeDuration = duration;

            while (tempEUt <= V[tier - 1] * (long) amperage && maxPossibleOverclocks-- > 0 && recipeDuration > 1) {
                tempEUt <<= 2; // this actually controls overclocking
                recipeDuration >>= 2; // this is effect of overclocking
            }
            if (tempEUt > Integer.MAX_VALUE - 1) {
                recipeEuPerTick = Integer.MAX_VALUE - 1;
                recipeDuration = Integer.MAX_VALUE - 1;
            } else {
                recipeEuPerTick = (int) tempEUt;
                if (recipeEuPerTick == 0) recipeEuPerTick = 1;
                if (recipeDuration == 0) recipeDuration = 1; // set time to 1 tick
            }
        }
        wasOverclocked = checkIfOverclocked();
    }
}
