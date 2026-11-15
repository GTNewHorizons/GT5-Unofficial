package gregtech.api.recipe.maps;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.NEIScrollArea;

/**
 * The item outputs of the multiblock centrifuge scroll when a recipe has more than its output slots.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargeCentrifugeFrontend extends LargeNEIFrontend {

    private static final int OUTPUT_X = 106;

    public LargeCentrifugeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        NEIScrollArea.scrollItemOutputs(neiCachedRecipe, OUTPUT_X, yOrigin, xDirMaxCount, itemRowCount);
    }
}
