package gregtech.api.recipe.maps;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_Utility;
import gregtech.nei.GT_NEI_DefaultHandler;

public class PurificationUnitPhAdjustmentFrontend extends RecipeMapFrontend {

    public PurificationUnitPhAdjustmentFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        neiCachedRecipe.mInputs.add(new PositionedStack(Materials.SodiumHydroxide.getDust(64), 46, 13, false));
        neiCachedRecipe.mInputs.add(
            new PositionedStack(
                GT_Utility.getFluidDisplayStack(Materials.HydrochloricAcid.getFluid(1000L), true),
                102,
                13,
                false));
    }
}
