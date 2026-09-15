package gtnhlanth.common.tileentity.recipe.beamline;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.RecipeDisplayInfo;

public class SourceChamberFrontend extends RecipeMapFrontend {

    public SourceChamberFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        drawEnergyInfo(recipeInfo);
        // drawDurationInfo(recipeInfo); Unnecessary and misleading
        drawSpecialInfo(recipeInfo);
        drawMetadataInfo(recipeInfo);
        drawRecipeOwnerInfo(recipeInfo);
    }

}
