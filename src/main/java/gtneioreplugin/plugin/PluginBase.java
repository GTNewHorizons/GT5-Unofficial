package gtneioreplugin.plugin;

import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;

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
        return "gtneioreplugin:textures/gui/nei/guiBaseOre.png";
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, getGuiWidth(), getGuiHeight());
    }

    @Override
    public void loadTransferRects() {
        // Keep a minimal transfer rect so NEI category wiring remains intact,
        // without rendering any "see all" label text.
        transferRects.add(new RecipeTransferRect(new Rectangle(getGuiWidth() - 1, 1, 1, 1), getOutputId()));
    }

    public abstract String getOutputId();

    public int getGuiWidth() {
        return 166;
    }

    public int getGuiHeight() {
        return 166;
    }

}
