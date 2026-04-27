package gtneioreplugin.plugin;

import java.awt.Rectangle;
import java.util.List;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gtneioreplugin.GTNEIOrePlugin;

public abstract class PluginBase extends TemplateRecipeHandler {

    protected static final int LEFT_PADDING = 2;
    protected static final int TITLE_Y_POS = 1;
    protected static final int BOTTOM_PADDING = 2;

    public static final AdaptableUITexture BACKGROUND = AdaptableUITexture
        .of(GTNEIOrePlugin.MODID, "gui/nei/background", 8, 8, 1);

    @Override
    public String getRecipeName() {
        return null;
    }

    @Override
    public String getGuiTexture() {
        return BACKGROUND.location.toString() + ".png";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(0, TITLE_Y_POS, getGuiWidth(), 10), getOutputId()));
    }

    @Override
    public void drawBackground(int recipe) {
        BACKGROUND.draw(0, 0, getGuiWidth(), getRecipeHeight(recipe) - 2);
    }

    public abstract String getOutputId();

    public int getGuiWidth() {
        return 166;
    }

    protected java.util.List<String> getTitleLines(String text) {
        return GuiDraw.fontRenderer.listFormattedStringToWidth(text, getGuiWidth() - BOTTOM_PADDING);
    }

    protected void drawTitle(List<String> lines) {
        GuiDraw.drawRect(1, TITLE_Y_POS, getGuiWidth() - 2, lines.size() * 10, 0xff939393);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int x = (getGuiWidth() - GuiDraw.fontRenderer.getStringWidth(line)) / 2;
            GuiDraw.drawString(line, x, TITLE_Y_POS + 1 + i * 10, 0xfafafa, true);
        }
    }
}
