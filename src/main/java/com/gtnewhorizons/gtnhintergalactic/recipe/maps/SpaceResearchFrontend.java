package com.gtnewhorizons.gtnhintergalactic.recipe.maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.gtnhintergalactic.recipe.IG_Recipe;
import com.gtnewhorizons.modularui.api.math.Pos2d;

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
public class SpaceResearchFrontend extends RecipeMapFrontend {

    public SpaceResearchFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
            NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
                uiPropertiesBuilder,
                neiPropertiesBuilder.neiSpecialInfoFormatter(new SpaceResearchSpecialValueFormatter()));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return Collections.singletonList(new Pos2d(142, 35));
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        List<Pos2d> inputPositions = new ArrayList<>();
        inputPositions.add(new Pos2d(80, 35));
        inputPositions.add(new Pos2d(62, 8));
        inputPositions.add(new Pos2d(80, 8));
        inputPositions.add(new Pos2d(98, 8));
        inputPositions.add(new Pos2d(62, 60));
        inputPositions.add(new Pos2d(80, 60));
        inputPositions.add(new Pos2d(98, 60));
        return inputPositions;
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 34, 17, 1);
    }

    private static class SpaceResearchSpecialValueFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            List<String> specialInfo = new ArrayList<>();
            specialInfo.add(
                    GCCoreUtil.translateWithFormat("tt.nei.research.min_computation", recipeInfo.recipe.mSpecialValue));
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
