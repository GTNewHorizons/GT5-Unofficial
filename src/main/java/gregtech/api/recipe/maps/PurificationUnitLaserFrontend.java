package gregtech.api.recipe.maps;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;

public class PurificationUnitLaserFrontend extends PurificationUnitRecipeMapFrontend {

    public PurificationUnitLaserFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            120,
            uiPropertiesBuilder.logoPos(new Pos2d(147, 102))
                .progressBarTexture(new FallbackableUITexture(GTUITextures.PROGRESSBAR_UV_TREATMENT)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 120)));
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int itemInputCount){
        return ImmutableList.of(
            new Pos2d(62, -6),
            new Pos2d(40, -1),
            new Pos2d(32, 20),
            new Pos2d(52, 27),
            new Pos2d(75, 29),
            new Pos2d(98, 27),
            new Pos2d(118, 20),
            new Pos2d(110, -1),
            new Pos2d(88, -6));
    }

    @Override
    public @NotNull List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return ImmutableList.of(new Pos2d(10, 89));
    }

    @Override
    public @NotNull List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return ImmutableList.of(new Pos2d(147, 89));
    }
}
