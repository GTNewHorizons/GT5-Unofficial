package gregtech.nei;

import net.minecraft.client.Minecraft;

import codechicken.nei.recipe.TemplateRecipeHandler;

import com.gtnewhorizons.modularui.api.ModularUITextures;

import gregtech.api.gui.GT_GUIColorOverride;
import gregtech.api.util.GT_Recipe;

/**
 * This abstract class represents an NEI handler that is constructed from a {@link GT_Recipe.GT_Recipe_Map}, and allows
 * us to sort NEI handlers by recipe map.
 */
abstract class RecipeMapHandler extends TemplateRecipeHandler {

    protected final GT_Recipe.GT_Recipe_Map mRecipeMap;

    protected final GT_GUIColorOverride colorOverride;
    private int overrideTextColor = -1;

    RecipeMapHandler(GT_Recipe.GT_Recipe_Map mRecipeMap) {
        this.mRecipeMap = mRecipeMap;
        colorOverride = GT_GUIColorOverride.get(ModularUITextures.VANILLA_BACKGROUND.location);
    }

    GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return mRecipeMap;
    }

    protected void updateOverrideTextColor() {
        overrideTextColor = colorOverride.getTextColorOrDefault("nei", -1);
    }

    protected void drawText(int aX, int aY, String aString, int aColor) {
        Minecraft.getMinecraft().fontRenderer
            .drawString(aString, aX, aY, overrideTextColor != -1 ? overrideTextColor : aColor);
    }
}
