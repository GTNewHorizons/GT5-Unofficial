package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;

public class PurificationUnitParticleExtractorFrontend extends RecipeMapFrontend {

    public static final List<ItemStack> inputItems = new ArrayList<>(30);
    public static final List<ItemStack> inputItemsShuffled = new ArrayList<>(30);

    public PurificationUnitParticleExtractorFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int itemInputCount) {
        if (itemInputCount == 6) {
            final List<Pos2d> list = super.getItemInputPositions(2);
            final List<Pos2d> ret = new ArrayList<>(6);
            ret.add(list.get(0));
            ret.add(list.get(1));
            ret.add(list.get(1));
            ret.add(list.get(1));
            ret.add(list.get(1));
            ret.add(list.get(1));
            return ret;
        } else return super.getItemInputPositions(itemInputCount);
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        if (neiCachedRecipe.mInputs.size() != 3) {
            List<Pos2d> positions = UIHelper.getGridPositions(2, 30, 14, 2, 1);
            Pos2d pos1 = positions.get(0);
            Pos2d pos2 = positions.get(1);
            neiCachedRecipe.mInputs.remove(5);
            neiCachedRecipe.mInputs.remove(4);
            neiCachedRecipe.mInputs.remove(3);
            neiCachedRecipe.mInputs.remove(2);
            neiCachedRecipe.mInputs.set(0, new PositionedStack(inputItems, pos1.x, pos1.y, true));
            neiCachedRecipe.mInputs.set(1, new PositionedStack(inputItemsShuffled, pos2.x, pos2.y, true));
        }
        super.drawNEIOverlays(neiCachedRecipe);
    }
}
