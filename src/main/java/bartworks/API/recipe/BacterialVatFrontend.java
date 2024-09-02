package bartworks.API.recipe;

import java.util.Arrays;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Alignment;

import bartworks.common.tileentities.multis.MTEBioVat;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BacterialVatFrontend extends RecipeMapFrontend {

    public BacterialVatFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder,
            neiPropertiesBuilder.neiSpecialInfoFormatter(new BacterialVatSpecialValueFormatter()));
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

    private static class BacterialVatSpecialValueFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            int[] tSpecialA = MTEBioVat.specialValueUnpack(recipeInfo.recipe.mSpecialValue);
            String glassTier = StatCollector.translateToLocalFormatted("nei.biovat.0.name", tSpecialA[0]);
            String sievert;
            if (tSpecialA[2] == 1) {
                sievert = StatCollector.translateToLocalFormatted("nei.biovat.1.name", tSpecialA[3]);
            } else {
                sievert = StatCollector.translateToLocalFormatted("nei.biovat.2.name", tSpecialA[3]);
            }
            return Arrays.asList(glassTier, sievert);
        }
    }
}
