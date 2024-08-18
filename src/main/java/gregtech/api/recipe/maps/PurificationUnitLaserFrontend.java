package gregtech.api.recipe.maps;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitUVTreatment;
import gregtech.nei.GT_NEI_DefaultHandler;

public class PurificationUnitLaserFrontend extends RecipeMapFrontend {

    public PurificationUnitLaserFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        final int numLenses = GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS.size();
        List<Pos2d> positions = UIHelper.getGridPositions(numLenses, 12, -4, 3, 3);
        // Put in lens items
        for (int i = 0; i < numLenses; ++i) {
            Pos2d position = positions.get(i);
            ItemStack lens = GT_MetaTileEntity_PurificationUnitUVTreatment.LENS_ITEMS.get(i);
            neiCachedRecipe.mInputs.add(new PositionedStack(lens, position.x, position.y, false));
        }
        super.drawNEIOverlays(neiCachedRecipe);
    }
}
