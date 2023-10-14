package gregtech.common.power;

import static gregtech.api.util.GT_Utility.trans;

import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_Utility;
import gregtech.nei.NEIRecipeInfo;

public class SteamPower extends Power {

    private final double euPerTickMultiplier;
    private final double durationMultiplier;
    private final String[] STEAM_TIER_NAMES = { "Bronze", "Steel" };

    public SteamPower(byte tier, double euPerTickMultiplier, double durationMultiplier) {
        super(tier);
        this.euPerTickMultiplier = euPerTickMultiplier;
        this.durationMultiplier = durationMultiplier;
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
        recipeEuPerTick = (int) (euPerTick * euPerTickMultiplier);
        recipeDuration = (int) (duration * durationMultiplier);
    }

    @Override
    protected void drawNEIDescImpl(NEIRecipeInfo recipeInfo, RecipeMapFrontend frontend) {
        frontend.drawNEIText(recipeInfo, trans("153", "Usage: ") + getSteamUsageString());
    }

    @Override
    protected String getTotalPowerString() {
        return GT_Utility.formatNumbers(convertEUToSteam(recipeDuration * recipeEuPerTick)) + " Steam";
    }

    private String getSteamUsageString() {
        return GT_Utility.formatNumbers(20L * convertEUToSteam(recipeEuPerTick)) + " L/s Steam";
    }

    private static int convertEUToSteam(int eu) {
        // 2L normal steam == 1EU
        return 2 * eu;
    }
}
