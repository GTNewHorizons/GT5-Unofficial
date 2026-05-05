package tectech.recipe;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.enums.NaniteTier;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.AssemblyLineFrontend;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler.FixedPositionedStack;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BECAssemblyFrontend extends AssemblyLineFrontend {

    public BECAssemblyFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    protected List<String> handleNEIItemInputTooltip(List<String> currentTip, FixedPositionedStack pStack) {
        currentTip = super.handleNEIItemInputTooltip(currentTip, pStack);

        int slot = pStack.recipe.mInputs.indexOf(pStack);

        NaniteTier tier = GTDataUtils
            .getIndexSafe(pStack.recipe.mRecipe.getMetadata(GTRecipeConstants.NANITE_TIERS), slot);

        if (tier != null) {
            currentTip.add(GRAY + GTUtility.translate("gt.tooltip.nanite-tier", tier.describe()));
        }

        return currentTip;
    }

    @Override
    protected void drawSpecialInfo(RecipeDisplayInfo recipeInfo) {
        super.drawSpecialInfo(recipeInfo);

        NaniteTier[] tiers = recipeInfo.recipe.getMetadata(GTRecipeConstants.NANITE_TIERS);

        if (tiers != null && tiers.length > 0) {
            NaniteTier maxTier = tiers[0];

            for (NaniteTier tier : tiers) {
                if (tier.tier > maxTier.tier) maxTier = tier;
            }

            recipeInfo.drawText(GTUtility.translate("gt.tooltip.max-nanite", maxTier.describe()));
        }
    }
}
