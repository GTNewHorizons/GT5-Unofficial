package bartworks.API.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTRecipeConstants.GLASS;
import static gregtech.api.util.GTUtility.getTierNameWithParentheses;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Alignment;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipeConstants;
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
    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
        GTNEIDefaultHandler.FixedPositionedStack pStack) {
        if (pStack.isFluid()) {
            currentTip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("nei.biovat.input.tooltip"));
            return currentTip;
        }
        return super.handleNEIItemInputTooltip(currentTip, pStack);
    }

    @Override
    protected List<String> handleNEIItemOutputTooltip(List<String> currentTip,
        GTNEIDefaultHandler.FixedPositionedStack pStack) {
        if (pStack.isFluid()) {
            currentTip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("nei.biovat.output.tooltip"));
            return currentTip;
        }
        return super.handleNEIItemOutputTooltip(currentTip, pStack);
    }

    @Override
    protected void drawNEIOverlayForInput(GTNEIDefaultHandler.FixedPositionedStack stack) {
        drawFluidOverlay(stack);
    }

    @Override
    protected void drawNEIOverlayForOutput(GTNEIDefaultHandler.FixedPositionedStack stack) {
        drawFluidOverlay(stack);
    }

    private void drawFluidOverlay(GTNEIDefaultHandler.FixedPositionedStack stack) {
        if (stack.isFluid()) {
            drawNEIOverlayText(
                "+",
                stack,
                colorOverride.getTextColorOrDefault("nei_overlay_yellow", 0xFDD835),
                0.5f,
                true,
                Alignment.TopRight);
            return;
        }
        super.drawNEIOverlayForOutput(stack);
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
}
