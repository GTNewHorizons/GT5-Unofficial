package gregtech.api.objects.overclockdescriber;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.api.util.OverclockCalculator;
import gregtech.nei.formatter.FusionSpecialValueFormatter;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FusionOverclockDescriber extends EUOverclockDescriber {

    protected final long capableStartup;

    public FusionOverclockDescriber(byte energyTier, long capableStartup) {
        super(energyTier, 1);
        this.capableStartup = capableStartup;
    }

    @Override
    public OverclockCalculator createCalculator(OverclockCalculator template, GTRecipe recipe) {
        return super.createCalculator(template, recipe)
            .limitOverclockCount(overclock(recipe.mSpecialValue, recipe.mEUt))
            .setEUtIncreasePerOC(getEUtIncreasePerOC())
            .setDurationDecreasePerOC(getDurationDecreasePerOC());
    }

    protected double getEUtIncreasePerOC() {
        return 2.0;
    }

    protected double getDurationDecreasePerOC() {
        return 2.0;
    }

    @Override
    public String getTierString() {
        return GTValues.TIER_COLORS[tier] + "MK " + getFusionTier() + EnumChatFormatting.RESET;
    }

    @Override
    public boolean canHandle(GTRecipe recipe) {
        byte tier = GTUtility.getTier(recipe.mEUt);
        if (this.tier < tier) {
            return false;
        }
        return this.capableStartup >= recipe.mSpecialValue;
    }

    protected int overclock(long startEnergy, long voltage) {
        // Fusion Computer tier - recipe tier
        return Math.max(getFusionTier() - FusionSpecialValueFormatter.getFusionTier(startEnergy, voltage), 0);
    }

    protected int getFusionTier() {
        return this.tier - 5; // Mk1 <-> LuV
    }
}
