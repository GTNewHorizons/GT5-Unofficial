package gregtech.api.recipe.maps;

import java.util.Collections;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;

@ParametersAreNonnullByDefault
public final class MIPRRecipeFrontend extends RecipeMapFrontend {

    public MIPRRecipeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.itemInputPositionsGetter(count -> Collections.emptyList())
                .fluidInputPositionsGetter(count -> Collections.singletonList(new Pos2d(52, 62)))
                .itemOutputPositionsGetter(count -> Collections.singletonList(new Pos2d(106, 24)))
                .fluidOutputPositionsGetter(count -> Collections.singletonList(new Pos2d(106, 62))),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 82)));
    }
}
