package gregtech.common.power;

import static gregtech.api.util.GT_Utility.trans;

import gregtech.api.enums.GT_Values;
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
 * This is also used for calculating overclock for singleblock machines, while multiblock machines use different system.
 * This difference comes from multiblocks not having functionality for displaying OCed description on NEI, but it's
 * still a bit weird... Maybe it can be improved, but for now it's not that bad.
 * <p>
 * See also: {@link gregtech.nei.GT_NEI_DefaultHandler#getUsageAndCatalystHandler}
 */
public abstract class Power {

    /**
     * Tier of the (maybe virtual) machine this power belongs to.
     */
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
     * Computes power usage and duration for the give recipe.
     * <p>
     * This method should be called prior to any method except for getTier() and getTierString().
     */
    public abstract void compute(GT_Recipe recipe);

    public final void compute(int euPerTick, int duration) {
        compute(
            GT_Values.RA.stdBuilder()
                .eut(euPerTick)
                .duration(duration)
                .build()
                .orElseThrow(RuntimeException::new));
    }

    public int getEUPerTick() {
        return recipeEuPerTick;
    }

    public int getDurationTicks() {
        return recipeDuration;
    }

    public final double getDurationSeconds() {
        return 0.05d * getDurationTicks();
    }

    public final String getDurationStringSeconds() {
        return GT_Utility.formatNumbers(getDurationSeconds()) + GT_Utility.trans("161", " secs");
    }

    public final String getDurationStringTicks() {
        if (getDurationTicks() == 1) {
            return GT_Utility.formatNumbers(getDurationTicks()) + GT_Utility.trans("209.1", " tick");
        }
        return GT_Utility.formatNumbers(getDurationTicks()) + GT_Utility.trans("209", " ticks");
    }

    /**
     * Draws info about this power object on NEI recipe GUI. Override {@link #drawNEIDescImpl} for implementation.
     */
    public final void drawNEIDesc(NEIRecipeInfo recipeInfo, RecipeMapFrontend frontend) {
        if (getEUPerTick() > 0) {
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
