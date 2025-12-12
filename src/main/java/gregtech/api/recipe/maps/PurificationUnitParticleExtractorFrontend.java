package gregtech.api.recipe.maps;

import codechicken.nei.PositionedStack;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PurificationUnitParticleExtractorFrontend extends RecipeMapFrontend {
    public static final List<ItemStack> inputItems = new ArrayList<>(30);
    public static final List<ItemStack> inputItemsShuffled = new ArrayList<>(30);
    public PurificationUnitParticleExtractorFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        if (neiCachedRecipe.mInputs.size() != 2 ){
            List<Pos2d> positions = UIHelper.getGridPositions(2, 30, 14, 2, 1);
            Pos2d pos1 = positions.get(0);
            Pos2d pos2 = positions.get(1);
            neiCachedRecipe.mInputs.clear();
            neiCachedRecipe.mInputs.set(0, new PositionedStack(inputItems, pos1.x, pos1.y, true));
            neiCachedRecipe.mInputs.set(1, new PositionedStack(inputItemsShuffled, pos2.x, pos2.y, true));
        }
        super.drawNEIOverlays(neiCachedRecipe);
    }
}
