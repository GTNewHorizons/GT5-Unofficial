package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.Materials;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PurificationUnitPlasmaHeaterFrontend extends PurificationUnitRecipeMapFrontend {

    public PurificationUnitPlasmaHeaterFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            120,
            uiPropertiesBuilder.logoPos(new Pos2d(152, 90))
                .progressBarTexture(new FallbackableUITexture(GTUITextures.PROGRESSBAR_PLASMA_HEATER)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 120)));
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(30, 83));
        return positions;
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(111, 82));
        return positions;
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        neiCachedRecipe.mInputs.add(
            new PositionedStack(GTUtility.getFluidDisplayStack(Materials.Helium.getPlasma(10L), true), 26, 53, false));
        neiCachedRecipe.mInputs.add(
            new PositionedStack(
                GTUtility.getFluidDisplayStack(Materials.SuperCoolant.getFluid(100L), true),
                107,
                52,
                false));
    }
}
