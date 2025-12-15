package gregtech.api.recipe.maps;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CauldronFrontend extends RecipeMapFrontend {

    public CauldronFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        recipeInfo.drawText(StatCollector.translateToLocal("GT5U.cauldron_recipe.supports"));
        recipeInfo.drawText(StatCollector.translateToLocal("GT5U.cauldron_recipe.consumed"));

        drawRecipeOwnerInfo(recipeInfo);
    }
}
