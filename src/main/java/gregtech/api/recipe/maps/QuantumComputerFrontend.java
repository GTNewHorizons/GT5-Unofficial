package gregtech.api.recipe.maps;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.recipe.QuantumComputerRecipeData;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

import java.util.ArrayList;
import java.util.List;

public class QuantumComputerFrontend extends RecipeMapFrontend {
    public QuantumComputerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder, NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.neiSpecialInfoFormatter(new QuantumComputerMetaDataFormatter()));
    }

    private static class QuantumComputerMetaDataFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            List<String> result = new ArrayList<>();

            QuantumComputerRecipeData data = recipeInfo.recipe.getMetadata(GTRecipeConstants.QUANTUM_COMPUTER_DATA);
            if (data != null) {
                // If this is false, it's a cooling component.
                if (data.subZero) {
                    result.add("Heating Constant: " + data.heatConstant);
                    result.add("Computation: " + data.computation);
                } else {
                    result.add("Cooling Constant: " + data.coolConstant);
                }
                result.add("Maximum Heat: " + data.maxHeat);
            }
            return result;
        }
    }
}
