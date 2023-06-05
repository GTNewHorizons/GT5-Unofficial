package gregtech.nei;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import codechicken.lib.gui.GuiDraw;

public class NEIHandlerAbsoluteTooltip {

    private final Rectangle area;
    private final String tooltip;
    private Dimension displaySize;

    public NEIHandlerAbsoluteTooltip(String tooltip, Rectangle area) {
        this.tooltip = tooltip;
        this.area = area;
    }

    public void handleTooltip(List<String> currenttip, int recipeIndex) {
        displaySize = GuiDraw.displaySize();
        if (shouldAddTooltip(recipeIndex)) {
            currenttip.add(tooltip);
        }
    }

    private boolean shouldAddTooltip(int recipeIndex) {
        return isPageFirstRecipe(recipeIndex) && mouseInArea();
    }

    private boolean mouseInArea() {
        Point mousePos = getRelMouse();
        return area.contains(mousePos);
    }

    private Point getRelMouse() {
        int ySize = Math.min(Math.max(displaySize.height - 68, 166), 370);
        int guiLeft = (displaySize.width - 176) / 2;
        int guiTop = (displaySize.height - ySize) / 2 + 10;
        Point mousePos = GuiDraw.getMousePosition();
        return new Point(mousePos.x - guiLeft - 5, mousePos.y - guiTop - 38);
    }

    private boolean isPageFirstRecipe(int recipe) {
        int actualRecipesPerPage = getActualRecipesPerPage();
        return actualRecipesPerPage < 2 || recipe % 2 == 0;
    }

    private int getActualRecipesPerPage() {
        int ySize = Math.min(Math.max(displaySize.height - 68, 166), 370);
        return (ySize - (12 * 3)) / 135;
    }
}
