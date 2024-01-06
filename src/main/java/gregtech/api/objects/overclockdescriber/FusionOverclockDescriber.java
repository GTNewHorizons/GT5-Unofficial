package gregtech.api.objects.overclockdescriber;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_FusionComputer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FusionOverclockDescriber extends EUOverclockDescriber {

    protected final long capableStartup;

    /**
     * The values of {@link GT_MetaTileEntity_FusionComputer#capableStartupCanonical()} from MK1 ~ MK5
     */
    public static long getCapableStartupCanonical(int tier) {
        return switch (tier) {
            case 1 -> 160_000_000L;
            case 2 -> 320_000_000L;
            case 3 -> 640_000_000L;
            case 4 -> 5_120_000_000L;
            default -> 20_480_000_000L;
        };
    }

    public FusionOverclockDescriber(byte energyTier, long capableStartup) {
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

    protected int overclock(long startEnergy) {
        return switch (getFusionTier()) {
            case 1 -> 0;
            case 2 -> (startEnergy <= getCapableStartupCanonical(1)) ? 1 : 0;
            case 3 -> (startEnergy <= getCapableStartupCanonical(1)) ? 2
                : ((startEnergy <= getCapableStartupCanonical(2)) ? 1 : 0);
            case 4 -> (startEnergy <= getCapableStartupCanonical(1)) ? 3
                : (startEnergy <= getCapableStartupCanonical(2)) ? 2
                    : (startEnergy <= getCapableStartupCanonical(3)) ? 1 : 0;
            case 5 -> (startEnergy <= getCapableStartupCanonical(1)) ? 4
                : (startEnergy <= getCapableStartupCanonical(2)) ? 3
                    : (startEnergy <= getCapableStartupCanonical(3)) ? 2
                        : (startEnergy <= getCapableStartupCanonical(4)) ? 1 : 0;
            default -> throw new IllegalStateException("Unexpected fusion tier: " + getFusionTier());
        };
    }

    protected int getFusionTier() {
        return this.tier - 5; // Mk1 <-> LuV
    }
}
