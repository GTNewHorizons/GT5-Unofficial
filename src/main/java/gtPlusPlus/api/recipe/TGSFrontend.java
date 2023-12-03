package gtPlusPlus.api.recipe;

import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GT_NEI_DefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;
import gtPlusPlus.core.item.ModItems;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TGSFrontend extends RecipeMapFrontend {

    public TGSFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder, NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.neiSpecialInfoFormatter(new TGSSpecialValueFormatter()));
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected List<String> handleNEIItemOutputTooltip(List<String> currentTip,
            GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
        if (ModItems.fluidFertBasic != null && pStack.isChanceBased()) {
            currentTip.add(
                    GRAY + StatCollector.translateToLocalFormatted(
                            "gtpp.nei.tgs.sapling",
                            StatCollector.translateToLocal(ModItems.fluidFertBasic.getUnlocalizedName())));
        } else {
            super.handleNEIItemOutputTooltip(currentTip, pStack);
        }
        return currentTip;
    }

    @Override
    protected void drawNEIOverlayForOutput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {}

    private static class TGSSpecialValueFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            if (ModItems.fluidFertBasic == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(
                    StatCollector.translateToLocal("gtpp.nei.tgs.1"),
                    StatCollector.translateToLocalFormatted(
                            "gtpp.nei.tgs.2",
                            StatCollector.translateToLocal(ModItems.fluidFertBasic.getUnlocalizedName())),
                    StatCollector.translateToLocal("gtpp.nei.tgs.3"));
        }
    }
}
