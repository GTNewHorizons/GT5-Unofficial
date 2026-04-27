package gtneioreplugin.plugin;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.resources.I18n;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.gui.modularui.GTUITextures;

public abstract class PluginBase extends TemplateRecipeHandler {

    protected static final int LEFT_PADDING = 3;
    protected static final int TITLE_Y_POS = 3;
    protected static final int BOTTOM_PADDING = 2;

    protected static final int LINE_KEY_COLOR = 0x404040;
    protected static final int LINE_VALUE_COLOR = 0x303030;

    @Override
    public String getRecipeName() {
        return null;
    }

    @Override
    public String getGuiTexture() {
        return GTUITextures.BACKGROUND_NEI_SINGLE_RECIPE.location.toString() + ".png";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(0, TITLE_Y_POS, getGuiWidth(), 10), getOutputId()));
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        GTUITextures.BACKGROUND_NEI_SINGLE_RECIPE.draw(0, 0, getGuiWidth(), getRecipeHeight(recipe) - 2);
    }

    public abstract String getOutputId();

    public int getGuiWidth() {
        return 166;
    }

    protected List<String> getTitleLines(String text) {
        text = I18n.format("gtnop.gui.nei.title", text);
        return GuiDraw.fontRenderer.listFormattedStringToWidth(text, getGuiWidth() - BOTTOM_PADDING);
    }

    protected void drawTitle(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int x = (getGuiWidth() - GuiDraw.fontRenderer.getStringWidth(line)) / 2;
            GuiDraw.drawString(line, x, TITLE_Y_POS + i * 10, 0xffffff, true);
        }
    }
}
