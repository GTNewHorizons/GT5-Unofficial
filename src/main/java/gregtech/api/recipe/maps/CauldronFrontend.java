package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CauldronFrontend extends RecipeMapFrontend {

    public static final ArrayList<ItemStack> inputItems = new ArrayList<>();
    public static final ArrayList<ItemStack> outputItems = new ArrayList<>();

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

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        List<Pos2d> positions = UIHelper.getGridPositions(2, 48, 14, 2, 1);
        Pos2d pos1 = positions.get(0);
        Pos2d pos2 = positions.get(1);
        neiCachedRecipe.mInputs.set(0, new PositionedStack(inputItems, pos1.x, pos1.y, true));
        neiCachedRecipe.mOutputs.set(0, new PositionedStack(outputItems, pos2.x + 36, pos2.y, true));
        super.drawNEIOverlays(neiCachedRecipe);
    }

}
