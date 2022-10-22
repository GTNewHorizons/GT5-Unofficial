package gregtech.common.power;

import static gregtech.api.enums.GT_Values.V;

public class BasicMachineEUPower extends EUPower {
    protected static final String OC = " (OC)";
    protected boolean wasOverclocked;

    public BasicMachineEUPower(byte tier, int amperage) {
        super(tier, amperage);
    }

    @Override
    public void computePowerUsageAndDuration(int euPerTick, int duration) {
        super.computePowerUsageAndDuration(euPerTick, duration);
        if (tier == 0) {
            //Long time calculation
            long xMaxProgresstime = ((long) duration) << 1;
            if (xMaxProgresstime > Integer.MAX_VALUE - 1) {
                //make impossible if too long
                recipeEuPerTick = Integer.MAX_VALUE - 1;
                recipeDuration = Integer.MAX_VALUE - 1;
            } else {
                recipeEuPerTick = euPerTick >> 2;
                recipeDuration = (int) xMaxProgresstime;
            }
        } else {
            //Long EUt calculation
            long xEUt = euPerTick;
            //Isnt too low EUt check?
            long tempEUt = Math.max(xEUt, V[1]);

            recipeDuration = duration;

            while (tempEUt <= V[tier - 1] * (long) amperage) {
                tempEUt <<= 2;//this actually controls overclocking
                //xEUt *= 4;//this is effect of everclocking
                recipeDuration >>= 1;//this is effect of overclocking
                xEUt = recipeDuration == 0 ? xEUt >> 1 : xEUt << 2;//U know, if the time is less than 1 tick make the machine use 2x less power
            }
            if (xEUt > Integer.MAX_VALUE - 1) {
                recipeEuPerTick = Integer.MAX_VALUE - 1;
                recipeDuration = Integer.MAX_VALUE - 1;
            } else {
                recipeEuPerTick = (int) xEUt;
                if (recipeEuPerTick == 0)
                    recipeEuPerTick = 1;
                if (recipeDuration == 0)
                    recipeDuration = 1;//set time to 1 tick
            }
        }
        wasOverclocked = checkIfOverclocked();
    }

    @Override
    public String getPowerUsageString() {
        return decorateWithOverclockLabel(super.getPowerUsageString());
    }

    protected String decorateWithOverclockLabel(String s) {
        if (wasOverclocked) {
            s += OC;
        }
        return s;
    }

    protected boolean checkIfOverclocked() {
        return originalVoltage != computeVoltageForEuRate(recipeEuPerTick);
    }
}
