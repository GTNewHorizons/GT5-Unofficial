package bartworks.API.recipe;

import java.util.Arrays;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import bartworks.common.tileentities.tiered.MTERadioHatch;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.api.util.recipe.Sievert;
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
            Sievert data = recipeInfo.recipe.getMetadataOrDefault(GTRecipeConstants.SIEVERT, new Sievert(0));
            int radioLevel = data.sievert;
            int mass = recipeInfo.recipe.getMetadataOrDefault(GTRecipeConstants.MASS, 0);
            long time = MTERadioHatch.calcDecayTicks(radioLevel);
            return Arrays.asList(
                StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.0", radioLevel),
                StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.1", mass),
                StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.2", time * mass / 20.0));
        }
    }
}
