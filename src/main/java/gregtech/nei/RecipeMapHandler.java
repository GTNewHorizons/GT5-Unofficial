package gregtech.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.gui.GT_GUIColorOverride;
import gregtech.api.util.GT_Recipe;
import net.minecraft.client.Minecraft;

/**
 * This abstract class represents an NEI handler that is constructed from a
 * {@link GT_Recipe.GT_Recipe_Map}, and allows us to sort NEI handlers by recipe map.
 */
abstract class RecipeMapHandler extends TemplateRecipeHandler {
    protected final GT_Recipe.GT_Recipe_Map mRecipeMap;

    private GT_GUIColorOverride colorOverride;
    private int overrideTextColor = -1;

    RecipeMapHandler(GT_Recipe.GT_Recipe_Map mRecipeMap) {
        this.mRecipeMap = mRecipeMap;
    }

    GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return mRecipeMap;
    }

    protected void updateOverrideTextColor() {
        colorOverride = new GT_GUIColorOverride(mRecipeMap.mNEIGUIPath);
        overrideTextColor = colorOverride.getTextColorOrDefault("nei", -1);
    }

    protected void drawText(int aX, int aY, String aString, int aColor) {
        Minecraft.getMinecraft().fontRenderer.drawString(aString, aX, aY, overrideTextColor != -1 ? overrideTextColor : aColor);
    }

    protected void drawLine(int lineNumber, String line) {
        drawText(10, getDescriptionYOffset() + lineNumber * 10, line, overrideTextColor != -1 ? overrideTextColor : 0xFF000000);
    }

    protected int getDescriptionYOffset() {
        return 73;
    }
}
