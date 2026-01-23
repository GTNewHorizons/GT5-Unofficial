package gregtech.api.recipe.maps;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.nei.RecipeDisplayInfo;

public class IsotopeDecayFrontend extends RecipeMapFrontend {

    public IsotopeDecayFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        GTRecipeConstants.DecayType decayType = recipeInfo.recipe
            .getMetadataOrDefault(GTRecipeConstants.DECAY_TYPE, GTRecipeConstants.DecayType.Unknown);

        recipeInfo.drawText(GTUtility.translate("GT5U.gui.text.decay-type", switch (decayType) {
            case Unknown -> GTUtility.translate("GT5U.gui.text.unknown");
            case Alpha -> GTUtility.translate("GT5U.gui.text.alpha");
            case SpontaneousFission -> GTUtility.translate("GT5U.gui.text.spontaneous-fission");
            case Cluster -> GTUtility.translate("GT5U.gui.text.cluster");
            case AlphaTransfer -> GTUtility.translate("GT5U.gui.text.alpha-transfer");
            case BetaMinus -> GTUtility.translate("GT5U.gui.text.beta-minus");
            case BetaPlus -> GTUtility.translate("GT5U.gui.text.beta-plus");
        }));

        double halflife = recipeInfo.recipe.getMetadataOrDefault(GTRecipeConstants.HALF_LIFE, 1d);
        double perStackSecond = 64d / halflife;

        String unit = GTUtility.translate("GTPP.time.seconds")
            .toLowerCase();

        if (halflife > (60d * 60d * 24d)) {
            halflife /= 60d * 60d * 24d;
            unit = GTUtility.translate("GTPP.time.days")
                .toLowerCase();
        } else if (halflife > (60d * 60d)) {
            halflife /= 60d * 60d;
            unit = GTUtility.translate("GTPP.time.hours")
                .toLowerCase();
        } else if (halflife > 60d) {
            halflife /= 60d;
            unit = GTUtility.translate("GTPP.time.minutes")
                .toLowerCase();
        }

        recipeInfo.drawText(GTUtility.translate("GT5U.gui.text.half-life", formatNumber(halflife), unit));
        recipeInfo
            .drawText(GTUtility.translate("GT5U.gui.text.isotope-prod-per-s", formatNumber(perStackSecond)));

        drawRecipeOwnerInfo(recipeInfo);
    }
}
