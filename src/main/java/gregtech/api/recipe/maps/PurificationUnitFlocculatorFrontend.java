package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.Materials;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GT_NEI_DefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PurificationUnitFlocculatorFrontend extends RecipeMapFrontend {

    public PurificationUnitFlocculatorFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.logoPos(new Pos2d(160, 100))
                .addSpecialTexture(new Size(170, 80), new Pos2d(3, 3), GT_UITextures.BACKGROUND_FLOCCULATION_RECIPE),
            neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(9, 39));
        return positions;
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(151, 39));
        return positions;
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 114, 81, 3, 1);
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        // builder.setBackground(GT_UITextures.BACKGROUND_FLOCCULATION_RECIPE);
    }

    @Override
    public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        super.drawNEIOverlays(neiCachedRecipe);

        // Display flocculation chemical
        neiCachedRecipe.mInputs.add(
            new PositionedStack(
                GT_Utility.getFluidDisplayStack(Materials.PolyAluminiumChlorideSolution.getFluid(1000L), false, true),
                5,
                -1,
                false));
        // Display waste output
        neiCachedRecipe.mOutputs.add(
            new PositionedStack(
                GT_Utility.getFluidDisplayStack(Materials.FerrousWastewater.getFluid(1000L), false, true),
                147,
                48,
                false));
    }
}
