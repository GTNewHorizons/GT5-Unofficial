package com.gtnewhorizons.gtnhintergalactic.recipe.maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.gtnhintergalactic.recipe.IG_Recipe;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceAssemblerFrontend extends RecipeMapFrontend {

    public SpaceAssemblerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
            NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
                uiPropertiesBuilder,
                neiPropertiesBuilder.neiSpecialInfoFormatter(new SpaceAssemblerSpecialValueFormatter()));
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
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 106, 8, 1);
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        int bar1Width = 17;
        int bar2Width = 18;
        List<Supplier<Float>> splitProgress = splitProgress(progressSupplier, bar1Width, bar2Width);
        builder.widget(
                new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_1, 17)
                        .setDirection(ProgressBar.Direction.RIGHT).setProgress(splitProgress.get(0))
                        .setSynced(false, false).setPos(new Pos2d(88, 8).add(windowOffset)).setSize(bar1Width, 72));
        builder.widget(
                new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_2, 18)
                        .setDirection(ProgressBar.Direction.RIGHT).setProgress(splitProgress.get(1))
                        .setSynced(false, false).setPos(new Pos2d(124, 8).add(windowOffset)).setSize(bar2Width, 72));
    }

    private static class SpaceAssemblerSpecialValueFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            List<String> specialInfo = new ArrayList<>();
            specialInfo.add(GCCoreUtil.translateWithFormat("ig.nei.module", recipeInfo.recipe.mSpecialValue));
            if (recipeInfo.recipe instanceof IG_Recipe recipe) {
                String neededProject = recipe.getNeededSpaceProject();
                String neededProjectLocation = recipe.getNeededSpaceProjectLocation();
                if (neededProject != null && !neededProject.isEmpty()) {
                    specialInfo.add(
                            String.format(
                                    GCCoreUtil.translate("ig.nei.spaceassembler.project"),
                                    SpaceProjectManager.getProject(neededProject).getLocalizedName()));
                    specialInfo.add(
                            String.format(
                                    GCCoreUtil.translate("ig.nei.spaceassembler.projectAt"),
                                    neededProjectLocation == null || neededProjectLocation.isEmpty()
                                            ? GCCoreUtil.translate("ig.nei.spaceassembler.projectAnyLocation")
                                            : GCCoreUtil.translate(
                                                    SpaceProjectManager.getLocation(neededProjectLocation)
                                                            .getUnlocalizedName())));
                }
            }
            return specialInfo;
        }
    }

}
