package goodgenerator.api.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ExtremeHeatExchangerBackend extends FuelBackend {

    public ExtremeHeatExchangerBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    public GT_Recipe compileRecipe(GT_Recipe recipe) {
        if (!(recipe instanceof ExtremeHeatExchangerRecipe)) {
            throw new RuntimeException("Recipe must be instance of ExtremeHeatExchangerRecipe");
        }
        return super.compileRecipe(recipe);
    }
}
