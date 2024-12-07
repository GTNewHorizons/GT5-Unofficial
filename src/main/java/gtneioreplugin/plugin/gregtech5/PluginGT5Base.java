package gtneioreplugin.plugin.gregtech5;

import net.minecraft.client.resources.I18n;
import codechicken.lib.gui.GuiDraw;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IMaterial;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.plugin.PluginBase;

public abstract class PluginGT5Base extends PluginBase {

    protected static String getGTOreLocalizedName(IMaterial ore, boolean small) {
        if (ore == Materials.DraconiumAwakened) return "Aw. Draconium Ore";

        try (OreInfo<IMaterial> info = OreInfo.getNewInfo()) {
            info.material = ore;
            info.isSmall = small;

            return OreManager.getLocalizedName(info);
        }
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
