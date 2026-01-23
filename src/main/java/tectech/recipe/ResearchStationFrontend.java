package tectech.recipe;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;
import tectech.thing.gui.TecTechUITextures;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResearchStationFrontend extends RecipeMapFrontend {

    public ResearchStationFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return Collections.singletonList(new Pos2d(52, 33));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return Collections.singletonList(new Pos2d(106, 33));
    }

    @Override
    public Pos2d getSpecialItemPosition() {
        return new Pos2d(124, 62);
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        long eut = recipeInfo.recipe.mEUt;
        int computation = recipeInfo.recipe.mDuration;
        short ampere = (short) (recipeInfo.recipe.mSpecialValue & 0xFFFF);
        short minComputationPerSec = (short) (recipeInfo.recipe.mSpecialValue >>> 16);

        recipeInfo.drawText(
            translateToLocalFormatted(
                "tt.nei.research.max_eu",
                formatNumber(
                    (1 + (computation - minComputationPerSec) / minComputationPerSec) * eut * ampere * 20)));

        recipeInfo.drawText(
            StatCollector.translateToLocalFormatted("GT5U.gui.text.usage_line", formatNumber(eut * ampere)));

        recipeInfo
            .drawText(translateToLocalFormatted("tt.nei.research.computation", formatNumber(computation)));

        recipeInfo.drawText(
            translateToLocalFormatted(
                "tt.nei.research.min_computation",
                formatNumber(minComputationPerSec)));
    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    public void addProgressBar(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        int bar1Width = 25;
        int bar2Width = 11;
        int bar3Height = 18;
        List<Supplier<Float>> splitProgress = splitProgress(progressSupplier, bar1Width, bar2Width, bar3Height);
        builder.widget(
            new ProgressBar().setTexture(TecTechUITextures.PROGRESSBAR_RESEARCH_STATION_1, bar1Width)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(splitProgress.get(0))
                .setSynced(false, false)
                .setPos(new Pos2d(81, 40).add(windowOffset))
                .setSize(bar1Width, 5));
        builder.widget(
            new ProgressBar().setTexture(TecTechUITextures.PROGRESSBAR_RESEARCH_STATION_2, bar2Width)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(splitProgress.get(1))
                .setSynced(false, false)
                .setPos(new Pos2d(124, 40).add(windowOffset))
                .setSize(bar2Width, 5));
        builder.widget(
            new ProgressBar().setTexture(TecTechUITextures.PROGRESSBAR_RESEARCH_STATION_3, bar3Height)
                .setDirection(ProgressBar.Direction.DOWN)
                .setProgress(splitProgress.get(2))
                .setSynced(false, false)
                .setPos(new Pos2d(128, 44).add(windowOffset))
                .setSize(10, bar3Height));
    }
}
