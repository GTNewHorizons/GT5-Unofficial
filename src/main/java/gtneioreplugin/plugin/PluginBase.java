package gtneioreplugin.plugin;

import java.awt.Rectangle;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.TemplateRecipeHandler;

public abstract class PluginBase extends TemplateRecipeHandler {

    @Override
    public int recipiesPerPage() {
        return 1;
    }

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
            new RecipeTransferRect(new Rectangle(getGuiWidth() - stringLength - 3, 5, stringLength, 9), getOutputId()));
    }

    public abstract String getOutputId();

    public int getGuiWidth() {
        return 166;
    }

    /**
     * Draw the "see all recipes" transfer label
     */
    protected void drawSeeAllRecipesLabel() {
        GuiDraw.drawStringR(
            EnumChatFormatting.BOLD + I18n.format("gtnop.gui.nei.seeAll"),
            getGuiWidth() - 3,
            5,
            0x404040,
            false);
    }
}
