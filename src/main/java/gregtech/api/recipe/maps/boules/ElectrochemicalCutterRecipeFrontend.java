package gregtech.api.recipe.maps.boules;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;

@ParametersAreNonnullByDefault
public final class ElectrochemicalCutterRecipeFrontend extends RecipeMapFrontend {

    public ElectrochemicalCutterRecipeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 82)));
    }
}
