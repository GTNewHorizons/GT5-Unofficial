package gregtech.api.objects.overclockdescriber;

import static gregtech.api.util.GTUtility.trans;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.GTMod;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.api.util.OverclockCalculator;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Provides an overclock behavior that will run on machines with the ability to draw information about it on NEI.
 * <p>
 * Implement {@link gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider} for corresponding machine to use
 * derivative of this class when looking up NEI recipe catalyst.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class OverclockDescriber {

    /**
     * Tier of the (maybe virtual) machine this object belongs to.
     */
    protected final byte tier;

    public OverclockDescriber(byte tier) {
        this.tier = tier;
    }

    /**
     * @return Tier of this object. Used to limit recipes shown on NEI, based on recipe EU/t.
     */
    public final byte getTier() {
        return tier;
    }

    /**
     * @return Tier display of this object, shown on NEI header in a form of {@code Machine Name (tier)}
     */
    public abstract String getTierString();

    /**
     * Creates overclock calculator from given template. This template should be used instead of building from the
     * ground to avoid issues coming from different caller using different templates, but it's not applicable when using
     * {@link OverclockCalculator#ofNoOverclock(GTRecipe)}.
     *
     * @param template Calculator that can be used as template. Recipe EU/t and duration are already set.
     * @param recipe   Recipe to calculate.
     */
    public abstract OverclockCalculator createCalculator(OverclockCalculator template, GTRecipe recipe);

    /**
     * Draws info about the energy this object can handle on NEI recipe GUI.
     */
    public abstract void drawEnergyInfo(RecipeDisplayInfo recipeInfo);

    public void drawDurationInfo(RecipeDisplayInfo recipeInfo) {
        if (getDurationTicks(recipeInfo.calculator) <= 0) return;

        String textToDraw = trans("158", "Time: ");
        if (GTMod.gregtechproxy.mNEIRecipeSecondMode) {
            textToDraw += getDurationStringSeconds(recipeInfo.calculator);
            if (getDurationSeconds(recipeInfo.calculator) <= 1.0d) {
                textToDraw += String.format(" (%s)", getDurationStringTicks(recipeInfo.calculator));
            }
        } else {
            textToDraw += getDurationStringTicks(recipeInfo.calculator);
        }
        recipeInfo.drawText(textToDraw);
    }

    /**
     * Used to limit the shown recipes when searching recipes with NEI recipe catalyst. Unless overridden, this method
     * doesn't do anything special (except for a bit worse performance).
     * <p>
     * In order to make use of this method, {@link gregtech.api.recipe.RecipeMapBuilder#useCustomFilterForNEI}
     * should be enabled for the recipemap.
     *
     * @return If this object can handle the supplied recipe
     */
    public boolean canHandle(GTRecipe recipe) {
        byte tier = GTUtility.getTier(recipe.mEUt);
        return this.tier >= tier;
    }

    private int getDurationTicks(OverclockCalculator calculator) {
        return calculator.getDuration();
    }

    private double getDurationSeconds(OverclockCalculator calculator) {
        return 0.05d * getDurationTicks(calculator);
    }

    private String getDurationStringSeconds(OverclockCalculator calculator) {
        return GTUtility.formatNumbers(getDurationSeconds(calculator)) + GTUtility.trans("161", " secs");
    }

    private String getDurationStringTicks(OverclockCalculator calculator) {
        String ticksString = getDurationTicks(calculator) == 1 ? GTUtility.trans("209.1", " tick")
            : GTUtility.trans("209", " ticks");
        return GTUtility.formatNumbers(getDurationTicks(calculator)) + ticksString;
    }
}
