package gtneioreplugin.plugin;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.TemplateRecipeHandler;

public abstract class PluginBase extends TemplateRecipeHandler {

    protected static final int LEFT_PADDING = 2;
    protected static final int TITLE_Y_POS = 1;

    @Override
    public String getRecipeName() {
        return null;
    }

    @Override
    public String getGuiTexture() {
        return "gtneioreplugin:textures/gui/nei/guiBase.png";
    }

    @Override
    public void loadTransferRects() {
        int stringLength = GuiDraw.getStringWidth(EnumChatFormatting.BOLD + I18n.format("gtnop.gui.nei.seeAll"));
        transferRects.add(
            new RecipeTransferRect(new Rectangle(0, TITLE_Y_POS, getGuiWidth(), 10), getOutputId()));
    }

    public abstract String getOutputId();

    public int getGuiWidth() {
        return 166;
    }

    protected java.util.List<String> getTitleLines(String text) {
        return GuiDraw.fontRenderer.listFormattedStringToWidth(text, getGuiWidth() - 4);
    }

    protected void drawTitle(List<String> lines) {
        GuiDraw.drawRect(0, TITLE_Y_POS - 1, getGuiWidth(), lines.size() * 10 + 1, 0xff939393);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int x = (getGuiWidth() - GuiDraw.fontRenderer.getStringWidth(line)) / 2;
            GuiDraw.drawString(line, x, TITLE_Y_POS + i * 11, 0xfafafa, true);
        }
    }
}
