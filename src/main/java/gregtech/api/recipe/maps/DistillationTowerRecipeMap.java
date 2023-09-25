package gregtech.api.recipe.maps;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.Mods.GregTech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.RecipeMap;

public class DistillationTowerRecipeMap extends RecipeMap {

    public DistillationTowerRecipeMap() {
        super(
            new HashSet<>(110),
            "gt.recipe.distillationtower",
            "Distillation Tower",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "DistillationTower"),
            2,
            1,
            0,
            0,
            1,
            E,
            1,
            E,
            true,
            true);
        setLogoPos(80, 62);
    }

    @Override
    public IDrawable getOverlayForSlot(boolean isFluid, boolean isOutput, int index, boolean isSpecial) {
        if (isOutput) {
            if (isFluid) {
                return GT_UITextures.OVERLAY_SLOTS_NUMBER[index + 1];
            } else {
                return GT_UITextures.OVERLAY_SLOTS_NUMBER[0];
            }
        }
        return super.getOverlayForSlot(isFluid, false, index, isSpecial);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return Collections.singletonList(new Pos2d(106, 62));
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        List<Pos2d> results = new ArrayList<>();
        for (int i = 1; i < fluidOutputCount + 1; i++) {
            results.add(new Pos2d(106 + (i % 3) * 18, 62 - (i / 3) * 18));
        }
        return results;
    }
}
