package gtPlusPlus.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import org.lwjgl.opengl.GL11;

/**
 * Used for larger GUI ("basicmachines/FissionFuel")
 */
public class GT_NEI_MultiNoCell extends GTPP_NEI_DefaultHandler {

    public GT_NEI_MultiNoCell(GT_Recipe_Map aMap) {
        super(aMap);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new GT_NEI_MultiNoCell(mRecipeMap);
    }

    @Override
    public void drawBackground(final int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(-4, -8, 1, 3, 174, 89);
    }

    @Override
    protected int getDescriptionYOffset() {
        return 85;
    }
}
