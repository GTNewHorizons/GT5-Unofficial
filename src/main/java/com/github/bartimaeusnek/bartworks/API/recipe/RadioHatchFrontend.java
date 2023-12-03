package com.github.bartimaeusnek.bartworks.API.recipe;

import java.util.Arrays;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RadioHatchFrontend extends RecipeMapFrontend {

    public RadioHatchFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
            NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.neiSpecialInfoFormatter(new RadioHatchSpecialInfoFormatter()));
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

    private static class RadioHatchSpecialInfoFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            int radioLevel = recipeInfo.recipe.mEUt;
            int amount = recipeInfo.recipe.mDuration;
            long time = recipeInfo.recipe.mSpecialValue;
            return Arrays.asList(
                    StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.0", radioLevel),
                    StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.1", amount),
                    StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.2", time * amount / 20.0));
        }
    }
}
