package bartworks.API.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTRecipeConstants.GLASS;
import static gregtech.api.util.GTUtility.getTierNameWithParentheses;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import bartworks.common.loaders.BioItemList;
import codechicken.nei.NEIClientUtils.Alignment;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.Badge;
import gregtech.api.enums.GTValues;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.api.util.recipe.Sievert;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BacterialVatFrontend extends RecipeMapFrontend {

    public BacterialVatFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {
        for (PositionedStack stack : recipe.mInputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed
                && GTUtility.areStacksEqual(fixed.item, BioItemList.getPetriDish(null), true)) {
                fixed.setMaxSize(0);
            }
        }

        for (PositionedStack stack : recipe.mInputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && fixed.isFluid()) {
                fixed.setCustomBadge(
                    new Badge("+", StatCollector.translateToLocal("nei.biovat.input.tooltip")).setShadow(true)
                        .setAlignment(Alignment.TopRight));
            }
        }

        for (PositionedStack stack : recipe.mOutputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && fixed.isFluid()) {
                fixed.setCustomBadge(
                    new Badge("+", StatCollector.translateToLocal("nei.biovat.output.tooltip")).setShadow(true)
                        .setAlignment(Alignment.TopRight));
            }
        }
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        long eut = recipeInfo.recipe.mEUt;
        long duration = recipeInfo.recipe.mDuration;
        int glassTier = recipeInfo.recipe.getMetadataOrDefault(GLASS, 3);
        Sievert data = recipeInfo.recipe.getMetadataOrDefault(GTRecipeConstants.SIEVERT, new Sievert(0, false));
        int sievert = data.sievert;
        boolean isExact = data.isExact;
        recipeInfo
            .drawText(StatCollector.translateToLocalFormatted("GT5U.nei.display.total", formatNumber(eut * duration)));
        recipeInfo.drawText(
            StatCollector.translateToLocalFormatted(
                "GT5U.nei.display.usage",
                formatNumber(eut),
                getTierNameWithParentheses(eut)));

        recipeInfo.drawText(StatCollector.translateToLocalFormatted("nei.biovat.0.name", GTValues.VN[glassTier]));
        if (sievert != 0) {
            if (isExact) {
                recipeInfo.drawText(StatCollector.translateToLocalFormatted("nei.biovat.1.name", sievert));
            } else {
                recipeInfo.drawText(StatCollector.translateToLocalFormatted("nei.biovat.2.name", sievert));
            }
        }
    }

    @Override
    public final Pos2d getSpecialItemPosition() {
        return new Pos2d(16, 62);
    }
}
