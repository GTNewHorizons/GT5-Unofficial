package kubatech.api.gui;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;

import java.util.function.Supplier;

public class HighTemperatureGasCooledReactorRecipeMapFrontend extends RecipeMapFrontend {

    public HighTemperatureGasCooledReactorRecipeMapFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder, Pos2d windowOffset) {
        super.addGregTechLogo(builder, windowOffset);
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        super.addProgressBar(builder, progressSupplier, windowOffset);
    }
}
