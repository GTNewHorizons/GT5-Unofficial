package gtneioreplugin.plugin.gregtech5;

import net.minecraft.client.resources.I18n;

import codechicken.lib.gui.GuiDraw;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.blocks.BlockOres2;
import gregtech.common.blocks.BlockOres2.StoneType;
import gtneioreplugin.plugin.PluginBase;

public abstract class PluginGT5Base extends PluginBase {

    public static String getGTOreLocalizedName(Materials ore, boolean small) {
        if (ore == Materials.DraconiumAwakened) return "Aw. Draconium Ore";

        String name = GTLanguageManager.getTranslation(getGTOreUnlocalizedName(ore, small));
        return ore.getLocalizedNameForItem(name);
    }

    protected static String getGTOreUnlocalizedName(Materials ore, boolean small) {
        return "gt.blockores2." + BlockOres2.getMeta(StoneType.Stone, ore.mMetaItemSubID, small, false) + ".name";
    }

    static void drawLine(String lineKey, String value, int x, int y) {
        GuiDraw.drawString(I18n.format(lineKey) + ": " + value, x, y, 0x404040, false);
    }

    /**
     * Draw the dimension header and the dimension names over up to 3 lines
     *
     */
    protected void drawDimNames() {
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": ", 2, 100, 0x404040, false);
    }
}
