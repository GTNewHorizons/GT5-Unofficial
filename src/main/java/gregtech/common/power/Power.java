package gregtech.common.power;

import static gregtech.api.util.GT_Utility.trans;

import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.nei.NEIRecipeInfo;

/**
 * This class unifies EU and steam behaviors into abstracted common place. It's useful especially for NEI,
 * to perform virtual overclocking and show machine-dependent info.
 * <p>
 * Override {@link gregtech.api.interfaces.metatileentity.IMetaTileEntity#getPower()} to use derivatives.
 * When looking up NEI recipe catalyst, Power object for the corresponding machine will be used.
 * <p>
 * See also: {@link gregtech.nei.GT_NEI_DefaultHandler#getUsageAndCatalystHandler}
 */
public abstract class Power {

    protected final byte tier;
    protected int recipeEuPerTick;
    protected int recipeDuration;

    public Power(byte tier) {
        this.tier = tier;
    }

    /**
     * @return Tier of this power. Used to limit recipes shown on NEI, based on recipe EU/t.
     */
    public byte getTier() {
        return tier;
    }

    /**
     * @return Tier display of this power, shown on NEI header in a form of {@code Machine Name (tier)}
     */
    public abstract String getTierString();

    /**
     * Sets recipe EU/t and duration to use for the give parameters.
     * <p>
     * This method should be called prior to usage of any value except the power tier.
     */
    public abstract void computePowerUsageAndDuration(int euPerTick, int duration);

    public void computePowerUsageAndDuration(int euPerTick, int duration, int specialValue) {
        computePowerUsageAndDuration(euPerTick, duration);
    }

    public int getEuPerTick() {
        return recipeEuPerTick;
    }

    public int getDurationTicks() {
        return recipeDuration;
    }

    public double getDurationSeconds() {
        return 0.05d * getDurationTicks();
    }

    public String getDurationStringSeconds() {
        return GT_Utility.formatNumbers(getDurationSeconds()) + GT_Utility.trans("161", " secs");
    }

    public String getDurationStringTicks() {
        if (getDurationTicks() == 1) {
            return GT_Utility.formatNumbers(getDurationTicks()) + GT_Utility.trans("209.1", " tick");
        }
        return GT_Utility.formatNumbers(getDurationTicks()) + GT_Utility.trans("209", " ticks");
    }

    /**
     * Draws info about this power object on NEI recipe GUI. Override {@link #drawNEIDescImpl} for implementation.
     */
    public final void drawNEIDesc(NEIRecipeInfo recipeInfo, RecipeMapFrontend frontend) {
        if (getEuPerTick() > 0) {
            frontend.drawNEIText(recipeInfo, trans("152", "Total: ") + getTotalPowerString());
            drawNEIDescImpl(recipeInfo, frontend);
        }
    }

    /**
     * Implement this to draw info about this power object on NEI recipe GUI.
     */
    protected abstract void drawNEIDescImpl(NEIRecipeInfo recipeInfo, RecipeMapFrontend frontend);

    /**
     * @return Total power to consume for the recipe previously calculated.
     */
    protected abstract String getTotalPowerString();

    /**
     * Used to limit the shown recipes when searching recipes with NEI recipe catalyst. Unless overridden, this method
     * doesn't do anything special (except for a bit worse performance).
     * <p>
     * In order to make use of this method, {@link gregtech.api.recipe.RecipeMapBuilder#useCustomFilterForNEI}
     * should be enabled for the recipemap.
     *
     * @return If this power can handle the supplied recipe
     */
    public boolean canHandle(GT_Recipe recipe) {
        byte tier = GT_Utility.getTier(recipe.mEUt);
        return this.tier >= tier;
    }
}
