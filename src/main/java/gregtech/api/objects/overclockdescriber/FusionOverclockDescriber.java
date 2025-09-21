package gregtech.api.objects.overclockdescriber;

import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;

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
            .setMaxOverclocks(maxOverclocks(recipe.getMetadataOrDefault(FUSION_THRESHOLD, 0L), recipe.mEUt))
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
        return this.capableStartup >= recipe.getMetadataOrDefault(FUSION_THRESHOLD, 0L);
    }

    protected int maxOverclocks(long startEnergy, long voltage) {
        // Fusion Computer tier - recipe tier
        return getFusionTier() - FusionSpecialValueFormatter.getFusionTier(startEnergy, voltage);
    }

    protected int getFusionTier() {
        return this.tier - 5; // Mk1 <-> LuV
    }
}
