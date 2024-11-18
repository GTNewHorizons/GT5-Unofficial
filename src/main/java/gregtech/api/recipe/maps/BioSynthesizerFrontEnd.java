package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.recipe.AORecipeData;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public class BioSynthesizerFrontEnd extends RecipeMapFrontend {

    public BioSynthesizerFrontEnd(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.neiSpecialInfoFormatter(new BioSynthesizerMetaDataFormatter()));
    }

    private static class BioSynthesizerMetaDataFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            List<String> result = new ArrayList<>();

            AORecipeData data = recipeInfo.recipe.getMetadata(GTRecipeConstants.AO_DATA);
            if (data != null) {
                result.add("Required Intelligence: " + data.requiredIntelligence);
                result.add("Required Count: " + data.requiredCount);
                result.add("Danger Level: " + data.dangerLevel);
            }
            return result;
        }
    }
}
