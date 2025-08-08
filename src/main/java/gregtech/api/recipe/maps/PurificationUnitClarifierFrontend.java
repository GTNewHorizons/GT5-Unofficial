package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitClarifier;
import gregtech.nei.GTNEIDefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PurificationUnitClarifierFrontend extends PurificationUnitRecipeMapFrontend {

    public PurificationUnitClarifierFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            80,
            uiPropertiesBuilder.logoPos(new Pos2d(160, 100))
                .progressBarTexture(new FallbackableUITexture(GTUITextures.PROGRESSBAR_CLARIFIER))
                .logoPos(new Pos2d(152, 90)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 120)));
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(6, 7));
        return positions;
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(154, 7));
        return positions;
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(79, 43));
        return positions;
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 136, 43, 2, 2);
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        super.drawNEIOverlays(neiCachedRecipe);

        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack.item.isItemEqual(ItemList.ActivatedCarbonFilterMesh.get(1))) {
                drawNEIOverlayText((int) (MTEPurificationUnitClarifier.FILTER_DAMAGE_RATE) + "%", stack);
            }
        }
    }
}
