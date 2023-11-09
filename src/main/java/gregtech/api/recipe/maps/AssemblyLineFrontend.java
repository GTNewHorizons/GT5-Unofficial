package gregtech.api.recipe.maps;

import java.util.Collections;
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
public class AssemblyLineFrontend extends RecipeMapFrontend {

    public AssemblyLineFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 16, 8, 4);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return Collections.singletonList(new Pos2d(142, 8));
    }

    @Override
    public Pos2d getSpecialItemPosition() {
        return new Pos2d(142, 44);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 106, 8, 1);
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        int bar1Width = 17;
        int bar2Width = 18;
        List<Supplier<Float>> splitProgress = splitProgress(progressSupplier, bar1Width, bar2Width);
        builder.widget(
            new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_1, bar1Width)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(splitProgress.get(0))
                .setSynced(false, false)
                .setPos(new Pos2d(88, 8).add(windowOffset))
                .setSize(bar1Width, 72));
        builder.widget(
            new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_2, bar2Width)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(splitProgress.get(1))
                .setSynced(false, false)
                .setPos(new Pos2d(124, 8).add(windowOffset))
                .setSize(bar2Width, 72));
        builder.widget(
            new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_3, 18)
                .setDirection(ProgressBar.Direction.UP)
                .setProgress(progressSupplier)
                .setSynced(false, false)
                .setPos(new Pos2d(146, 26).add(windowOffset))
                .setSize(10, 18));
    }
}
