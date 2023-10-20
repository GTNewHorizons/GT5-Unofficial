package gregtech.common.power;

import static gregtech.api.enums.GT_Values.V;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.formatter.FusionSpecialValueFormatter;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FusionPower extends BasicMachineEUPower {

    protected final long capableStartup;

    public FusionPower(byte tier, long capableStartup) {
        super(tier, 1);
        this.capableStartup = capableStartup;
    }

    @Override
    public void compute(GT_Recipe recipe) {
        originalEUt = recipe.mEUt;
        final int maxPossibleOverclocks = FusionSpecialValueFormatter.getFusionTier(capableStartup, V[tier])
            - FusionSpecialValueFormatter.getFusionTier(recipe.mSpecialValue, recipe.mEUt);
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(recipe.mEUt)
            .setEUt(V[tier])
            .setDuration(recipe.mDuration)
            .setEUtIncreasePerOC(getEUtIncreasePerOC())
            .limitOverclockCount(maxPossibleOverclocks)
            .calculate();
        recipeEuPerTick = (int) calculator.getConsumption();
        recipeDuration = calculator.getDuration();
        wasOverclocked = checkIfOverclocked();
    }

    protected int getEUtIncreasePerOC() {
        return 1;
    }

    @Override
    public String getTierString() {
        return GT_Values.TIER_COLORS[tier] + "MK "
            + (tier - 5) // Mk1 <-> LuV
            + EnumChatFormatting.RESET;
    }

    @Override
    public boolean canHandle(GT_Recipe recipe) {
        byte tier = GT_Utility.getTier(recipe.mEUt);
        if (this.tier < tier) {
            return false;
        }
        return this.capableStartup >= recipe.mSpecialValue;
    }
}
