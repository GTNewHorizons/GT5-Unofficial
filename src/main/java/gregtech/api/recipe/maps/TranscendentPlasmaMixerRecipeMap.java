package gregtech.api.recipe.maps;

import static gregtech.api.util.GT_Utility.formatNumbers;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.NEIRecipeInfo;

public class TranscendentPlasmaMixerRecipeMap extends RecipeMap {

    public TranscendentPlasmaMixerRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName,
        String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
        int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
        int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
        boolean aNEIAllowed) {
        super(
            aRecipeList,
            aUnlocalizedName,
            aLocalName,
            aNEIName,
            aNEIGUIPath,
            aUsualInputCount,
            aUsualOutputCount,
            aMinimalInputItems,
            aMinimalInputFluids,
            aAmperage,
            aNEISpecialValuePre,
            aNEISpecialValueMultiplier,
            aNEISpecialValuePost,
            aShowVoltageAmperageInNEI,
            aNEIAllowed);
        setUsualFluidInputCount(20);
        setUsualFluidOutputCount(1);
        setProgressBarPos(86, 44);
        setNEITransferRect(
            new Rectangle(
                progressBarPos.x - (16 / 2),
                progressBarPos.y,
                progressBarSize.width + 16,
                progressBarSize.height));
        setLogoPos(87, 99);
        setNEIBackgroundSize(172, 118);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 60, 8, 1);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 6, 26, 4, 5);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 114, 44, 1);
    }

    @Override
    protected void drawNEIEnergyInfo(NEIRecipeInfo recipeInfo) {
        // These look odd because recipeInfo.recipe.mEUt is actually the EU per litre of fluid processed, not
        // the EU/t.
        drawNEIText(
            recipeInfo,
            GT_Utility.trans("152", "Total: ")
                + formatNumbers(1000L * recipeInfo.recipe.mDuration / 100L * recipeInfo.recipe.mEUt)
                + " EU");
        // 1000 / (20 ticks * 5 seconds) = 10L/t. 10L/t * x EU/L = 10 * x EU/t.
        long averageUsage = 10L * recipeInfo.recipe.mEUt;
        drawNEIText(
            recipeInfo,
            "Average: " + formatNumbers(averageUsage) + " EU/t" + GT_Utility.getTierNameWithParentheses(averageUsage));
    }
}
