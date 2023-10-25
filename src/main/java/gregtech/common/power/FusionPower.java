package gregtech.common.power;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FusionPower extends BasicMachineEUPower {

    protected final long capableStartup;

    public FusionPower(byte energyTier, long capableStartup) {
        super(energyTier, 1);
        this.capableStartup = capableStartup;
    }

    @Override
    public GT_OverclockCalculator createCalculator(GT_OverclockCalculator template, GT_Recipe recipe) {
        return super.createCalculator(template, recipe).limitOverclockCount(overclock(recipe.mSpecialValue))
            .setEUtIncreasePerOC(getEUtIncreasePerOC())
            .setDurationDecreasePerOC(getDurationDecreasePerOC());
    }

    protected int getEUtIncreasePerOC() {
        return 1;
    }

    protected int getDurationDecreasePerOC() {
        return 1;
    }

    @Override
    public String getTierString() {
        return GT_Values.TIER_COLORS[tier] + "MK " + getFusionTier() + EnumChatFormatting.RESET;
    }

    @Override
    public boolean canHandle(GT_Recipe recipe) {
        byte tier = GT_Utility.getTier(recipe.mEUt);
        if (this.tier < tier) {
            return false;
        }
        return this.capableStartup >= recipe.mSpecialValue;
    }

    protected int overclock(int startEnergy) {
        return switch (getFusionTier()) {
            case 1 -> 0;
            case 2 -> (startEnergy <= 160000000) ? 1 : 0;
            case 3 -> (startEnergy <= 160000000) ? 2 : ((startEnergy <= 320000000) ? 1 : 0);
            case 4 -> (startEnergy <= 160000000) ? 3
                : (startEnergy <= 320000000) ? 2 : (startEnergy <= 640000000) ? 1 : 0;
            case 5 -> (startEnergy <= 160000000) ? 4
                : (startEnergy <= 320000000) ? 3 : (startEnergy <= 640000000) ? 2 : (startEnergy <= 1280000000) ? 1 : 0;
            default -> throw new IllegalStateException("Unexpected fusion tier: " + getFusionTier());
        };
    }

    protected int getFusionTier() {
        return this.tier - 5; // Mk1 <-> LuV
    }
}
