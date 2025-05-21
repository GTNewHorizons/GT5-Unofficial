package gregtech.api.recipe.maps.nanochip;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.CustomBackgroundRecipeMapFrontend;

public class NanochipOpticalOrganizerFrontend extends CustomBackgroundRecipeMapFrontend {

    public NanochipOpticalOrganizerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            120,
            uiPropertiesBuilder
                .progressBarTexture(new FallbackableUITexture(GTUITextures.PROGRESSBAR_OPTICAL_ORGANIZER)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 120)));
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return ImmutableList.of(new Pos2d(22, 29), new Pos2d(26, 49));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return ImmutableList.of(new Pos2d(138, 41));
    }
}
