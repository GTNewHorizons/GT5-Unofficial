package goodgenerator.api.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ExtremeHeatExchangerBackend extends FuelBackend {

    public ExtremeHeatExchangerBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    public GTRecipe compileRecipe(GTRecipe recipe) {
        if (!(recipe instanceof ExtremeHeatExchangerRecipe)) {
            throw new RuntimeException("Recipe must be instance of ExtremeHeatExchangerRecipe");
        }
        return super.compileRecipe(recipe);
    }
}
