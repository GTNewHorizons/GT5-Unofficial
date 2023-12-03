package com.github.technus.tectech.recipe;

import java.util.ArrayList;
import java.util.List;

import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GT_Recipe;

public class TecTechRecipeMaps {

    public static void init() {}

    public static final List<GT_Recipe.GT_Recipe_AssemblyLine> researchableALRecipeList = new ArrayList<>();

    public static final RecipeMap<RecipeMapBackend> eyeOfHarmonyRecipes = RecipeMapBuilder.of("gt.recipe.eyeofharmony")
            .maxIO(
                    EyeOfHarmonyFrontend.maxItemInputs,
                    EyeOfHarmonyFrontend.maxItemOutputs,
                    EyeOfHarmonyFrontend.maxFluidInputs,
                    EyeOfHarmonyFrontend.maxFluidOutputs)
            .minInputs(1, 0).progressBar(GT_UITextures.PROGRESSBAR_HAMMER, ProgressBar.Direction.DOWN)
            .progressBarPos(78, 24 + 2).logoPos(10, 10)
            .neiHandlerInfo(
                    builder -> builder.setDisplayStack(CustomItemList.Machine_Multi_EyeOfHarmony.get(1))
                            .setMaxRecipesPerPage(1))
            .frontend(EyeOfHarmonyFrontend::new).build();
    public static final RecipeMap<RecipeMapBackend> researchStationFakeRecipes = RecipeMapBuilder
            .of("gt.recipe.researchStation").maxIO(1, 1, 0, 0).useSpecialSlot()
            .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
                if (isSpecial) {
                    return GT_UITextures.OVERLAY_SLOT_DATA_ORB;
                }
                if (isOutput) {
                    return TecTechUITextures.OVERLAY_SLOT_MESH;
                }
                return GT_UITextures.OVERLAY_SLOT_MICROSCOPE;
            }).addSpecialTexture(19, 12, 84, 60, TecTechUITextures.PICTURE_HEAT_SINK)
            .addSpecialTexture(41, 22, 40, 40, TecTechUITextures.PICTURE_RACK_LARGE)
            .logo(TecTechUITextures.PICTURE_TECTECH_LOGO).logoSize(18, 18).logoPos(151, 63)
            .neiTransferRect(81, 33, 25, 18).neiTransferRect(124, 33, 18, 29).frontend(ResearchStationFrontend::new)
            .neiHandlerInfo(builder -> builder.setDisplayStack(CustomItemList.Machine_Multi_Research.get(1))).build();
}
