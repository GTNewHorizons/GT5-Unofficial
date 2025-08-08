package gtneioreplugin.plugin.gregtech5;

import net.minecraft.client.resources.I18n;

import codechicken.lib.gui.GuiDraw;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTLanguageManager;
import gtneioreplugin.plugin.PluginBase;

public abstract class PluginGT5Base extends PluginBase {

    public static String getGTOreLocalizedName(short index) {
        String name = Materials
            .getLocalizedNameForItem(GTLanguageManager.getTranslation(getGTOreUnlocalizedName(index)), index % 1000);
        if (!name.contains("Awakened")) return name;
        else return "Aw. Draconium Ore";
    }

    protected static String getGTOreUnlocalizedName(short index) {
        return "gt.blockores." + index + ".name";
    }

    static void drawLine(String lineKey, String value, int x, int y) {
        GuiDraw.drawString(I18n.format(lineKey) + ": " + value, x, y, 0x404040, false);
    }

    protected int getMaximumMaterialIndex(short meta, boolean smallOre) {
        int offset = smallOre ? 16000 : 0;
        if (!getGTOreLocalizedName((short) (meta + offset + 5000))
            .equals(getGTOreUnlocalizedName((short) (meta + offset + 5000)))) return 7;
        else if (!getGTOreLocalizedName((short) (meta + offset + 5000))
            .equals(getGTOreUnlocalizedName((short) (meta + offset + 5000)))) return 6;
        else return 5;
    }

    /**
     * Draw the dimension header and the dimension names over up to 3 lines
     *
     */
    protected void drawDimNames() {
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": ", 2, 100, 0x404040, false);
    }
}
