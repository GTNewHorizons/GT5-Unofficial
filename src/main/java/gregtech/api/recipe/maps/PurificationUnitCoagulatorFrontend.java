package gregtech.api.recipe.maps;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PurificationUnitCoagulatorFrontend extends RecipeMapFrontend {

    public PurificationUnitCoagulatorFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        // There is only one fluid input, the grade 1 purified water
        return UIHelper.getGridPositions(1, 70, 80, 1);
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        builder.widget(
            new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_WAVES, 16)
                .setProgress(progressSupplier)
                .setSynced(false, false)
                .setPos(new Pos2d(70, 50).add(windowOffset))
                .setSize(128, 48));
    }
}
