package gregtech.loaders.postload.recipes.beamcrafter;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.RecipeDisplayInfo;

public class LargeHadronColliderFrontend extends RecipeMapFrontend {

    public LargeHadronColliderFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.logoPos(new Pos2d(152, 97))
                .progressBarTexture(null)
                .progressBarSize(new Size(0, 0))
                .progressBarPos(new Pos2d(0, 0)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 164)));
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {}

    @Override
    public void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return ImmutableList.of(new Pos2d(28, 39));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemInputCount) {
        return ImmutableList.of(
            new Pos2d(96, 23),
            new Pos2d(115, 19),
            new Pos2d(134, 19),
            new Pos2d(153, 23),
            new Pos2d(96, 63),
            new Pos2d(115, 67),
            new Pos2d(134, 67),
            new Pos2d(153, 63));
    }

}
