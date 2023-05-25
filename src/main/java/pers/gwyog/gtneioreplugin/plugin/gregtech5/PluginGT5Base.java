package pers.gwyog.gtneioreplugin.plugin.gregtech5;

import java.awt.Point;
import java.awt.Rectangle;

import net.minecraft.client.resources.I18n;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.GuiRecipe;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import pers.gwyog.gtneioreplugin.plugin.PluginBase;
import pers.gwyog.gtneioreplugin.util.GuiRecipeHelper;

public abstract class PluginGT5Base extends PluginBase {

    protected static String getLocalizedNameForItem(Materials aMaterial, String aFormat) {
        return String.format(aFormat.replace("%s", "%temp").replace("%material", "%s"), aMaterial.mLocalizedName)
                .replace("%temp", "%s");
    }

    protected static String getLocalizedNameForItem(String aFormat, int aMaterialID) {
        if (aMaterialID >= 0 && aMaterialID < 1000) {
            Materials aMaterial = GregTech_API.sGeneratedMaterials[aMaterialID];
            if (aMaterial != null) {
                return getLocalizedNameForItem(aMaterial, aFormat);
            }
        }
        return aFormat;
    }

    public static String getGTOreLocalizedName(short index) {

        if (!getLocalizedNameForItem(GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(index)), index % 1000)
                .contains("Awakened"))
            return getLocalizedNameForItem(
                    GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(index)),
                    index % 1000);
        else return "Aw. Draconium Ore";
    }

    protected static String getGTOreUnlocalizedName(short index) {
        return "gt.blockores." + index + ".name";
    }

    static void drawLine(String lineKey, String value, int x, int y) {
        GuiDraw.drawString(I18n.format(lineKey) + ": " + value, x, y, 0x404040, false);
    }

    /**
     * The dimension names for a given recipe index
     *
     * @param recipe The recipe index being handled
     * @return A CSV string of dimension name abbreviations
     */
    protected abstract String getDimensionNames(int recipe);

    /**
     * Produce a rectangle covering the area of displayed dimension names
     *
     * @param gui      An instance of the currentscreen
     * @param recipe   The recipe index being handled
     * @param dimNames Dimension names to produce a rectangle for
     * @return Rectangle area of dimension names
     */
    protected Rectangle getDimensionNamesRect(GuiRecipe<?> gui, int recipe, String dimNames) {
        int dimNamesHeight = dimNames.length() > 70 ? 30 : (dimNames.length() > 36 ? 20 : 10);
        Point offset = gui.getRecipePosition(recipe);
        return new Rectangle(
                GuiRecipeHelper.getGuiLeft(gui) + offset.x + 2,
                GuiRecipeHelper.getGuiTop(gui) + offset.y + 110,
                GuiRecipeHelper.getXSize(gui) - 9,
                dimNamesHeight);
    }

    protected int getMaximumMaterialIndex(short meta, boolean smallOre) {
        int offset = smallOre ? 16000 : 0;
        if (!getGTOreLocalizedName((short) (meta + offset + 5000))
                .equals(getGTOreUnlocalizedName((short) (meta + offset + 5000))))
            return 7;
        else if (!getGTOreLocalizedName((short) (meta + offset + 5000))
                .equals(getGTOreUnlocalizedName((short) (meta + offset + 5000))))
            return 6;
        else return 5;
    }

    /**
     * Draw the dimension header and the dimension names over up to 3 lines
     *
     * @param dimNames A CSV string of dimension name abbreviations
     */
    protected void drawDimNames(String dimNames) {
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": ", 2, 100, 0x404040, false);
    }
}
