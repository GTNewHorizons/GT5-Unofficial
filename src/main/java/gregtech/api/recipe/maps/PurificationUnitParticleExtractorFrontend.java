package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;

public class PurificationUnitParticleExtractorFrontend extends RecipeMapFrontend {

    public static final ArrayList<ItemStack> inputItems = new ArrayList<>();
    public static final ArrayList<ItemStack> inputItemsShuffled = new ArrayList<>();

    public PurificationUnitParticleExtractorFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        List<Pos2d> positions = UIHelper.getGridPositions(2, 30, 14, 2, 1);
        Pos2d pos1 = positions.get(0);
        Pos2d pos2 = positions.get(1);
        neiCachedRecipe.mInputs.set(0, new PositionedStack(inputItems, pos1.x, pos1.y, true));
        neiCachedRecipe.mInputs.set(1, new PositionedStack(inputItemsShuffled, pos2.x, pos2.y, true));
        super.drawNEIOverlays(neiCachedRecipe);
    }
}
