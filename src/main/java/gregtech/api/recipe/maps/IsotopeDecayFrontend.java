package gregtech.api.recipe.maps;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipeConstants;
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

        recipeInfo.drawText(StatCollector.translateToLocalFormatted("GT5U.gui.text.decay-type", switch (decayType) {
            case Unknown -> StatCollector.translateToLocal("GT5U.gui.text.unknown");
            case Alpha -> StatCollector.translateToLocal("GT5U.gui.text.alpha");
            case SpontaneousFission -> StatCollector.translateToLocal("GT5U.gui.text.spontaneous-fission");
            case Cluster -> StatCollector.translateToLocal("GT5U.gui.text.cluster");
            case AlphaTransfer -> StatCollector.translateToLocal("GT5U.gui.text.alpha-transfer");
            case BetaMinus -> StatCollector.translateToLocal("GT5U.gui.text.beta-minus");
            case BetaPlus -> StatCollector.translateToLocal("GT5U.gui.text.beta-plus");
        }));

        double halflife = recipeInfo.recipe.getMetadataOrDefault(GTRecipeConstants.HALF_LIFE, 1d);
        double perStackSecond = 64d / halflife;

        String unit = StatCollector.translateToLocal("GTPP.time.seconds")
            .toLowerCase();

        if (halflife > (60d * 60d * 24d)) {
            halflife /= 60d * 60d * 24d;
            unit = StatCollector.translateToLocal("GTPP.time.days")
                .toLowerCase();
        } else if (halflife > (60d * 60d)) {
            halflife /= 60d * 60d;
            unit = StatCollector.translateToLocal("GTPP.time.hours")
                .toLowerCase();
        } else if (halflife > 60d) {
            halflife /= 60d;
            unit = StatCollector.translateToLocal("GTPP.time.minutes")
                .toLowerCase();
        }

        recipeInfo
            .drawText(StatCollector.translateToLocalFormatted("GT5U.gui.text.half-life", formatNumber(halflife), unit));
        recipeInfo.drawText(
            StatCollector.translateToLocalFormatted("GT5U.gui.text.isotope-prod-per-s", formatNumber(perStackSecond)));

        drawRecipeOwnerInfo(recipeInfo);
    }
}
